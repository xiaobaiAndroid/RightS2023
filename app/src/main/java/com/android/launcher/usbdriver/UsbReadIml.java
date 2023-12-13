package com.android.launcher.usbdriver;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbManager;

import com.android.launcher.MyApp;

import module.common.MessageEvent;
import module.common.utils.LogUtils;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.concurrent.LinkedTransferQueue;

/**
 * @date： 2023/10/19
 * @author: 78495
*/
public class UsbReadIml {

    private static final int READ_WAIT_MILLIS = 20;


    private static class Holder {
        private static UsbReadIml usbReadIml = new UsbReadIml();
    }

    public static UsbReadIml getInstance() {
        return Holder.usbReadIml;
    }

    private static UsbSerialPort usbSerialPort;
    private static UsbDeviceConnection usbConnection;

    public volatile boolean connected = false;
    private static final String TAG = UsbReadIml.class.getSimpleName();

    //发送队列
    private static LinkedTransferQueue<byte[]> mSendQueue = new LinkedTransferQueue();

    private static StringBuilder usbJustTagSB;


    private UsbReadIml() {
    }

    /**
     * @description:
     * @createDate: 2023/8/26
     */
    public void connect(int baudRate, int portNum, int deviceId) {
        usbJustTagSB = new StringBuilder();

        UsbDevice device = null;
        UsbManager usbManager = (UsbManager) MyApp.getGlobalContext().getSystemService(Context.USB_SERVICE);
        for (UsbDevice v : usbManager.getDeviceList().values()){
            if (v.getDeviceId() == deviceId) {
                device = v;
            }
        }
        if (device == null) {
            LogUtils.printI(TAG, "连接失败，设备没有找到");
            return;
        }

        UsbSerialDriver driver = UsbSerialProber.getDefaultProber().probeDevice(device);
        if (driver == null) {
            driver = CustomProber.getCustomProber().probeDevice(device);
        }
        if (driver == null) {
            LogUtils.printI(TAG, "连接失败，no driver for device");
            return;
        }
        if (driver.getPorts().size() < portNum) {
            LogUtils.printI(TAG, "连接失败，not enough ports at device");
            return;
        }
        usbSerialPort = driver.getPorts().get(portNum);
        usbConnection = usbManager.openDevice(driver.getDevice());

        try {
            mSendQueue.clear();
            usbSerialPort.open(usbConnection);
            usbSerialPort.setParameters(baudRate, 8, 1, UsbSerialPort.PARITY_NONE);
            LogUtils.printI(TAG, "连接成功");

            connected = true;

            read();
            EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.SERIAL_PORT_CONNECT));
            writeSendCommandData();

            EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.LOAD_USB_OTG));
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.printI(TAG, "connect---e=" + e.getMessage());
            disconnect();
        }
    }


    private static void writeSendCommandData() {
        new Thread(() -> {
            while (usbSerialPort != null && usbSerialPort.isOpen()) {
                try {
                    byte[] bytes = mSendQueue.take();
                    if (usbSerialPort != null && usbConnection != null) {
                        UsbEndpoint usbEndpoint = usbSerialPort.getWriteEndpoint();
                        int transfer = usbConnection.bulkTransfer(usbEndpoint, bytes, bytes.length, 500);
//                            LogUtils.printI(TAG,  "writeSendCommandData------transfer=" + transfer);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtils.printE(TAG, e.getMessage());
                }
            }
        }).start();
    }

    /**
     * 分析 usb是那个
     *
     * @return
     */
    private void analysis() {
        // 先判断是那个usb  是与cd通讯的usb转can 还是与485通讯的usb转485
        try {
            byte[] byt = new byte[1024];
            int len = usbSerialPort.read(byt, READ_WAIT_MILLIS);

            // 读取数据 判断是那个问题

            if (len > 0) {
                byte[] by = new byte[len];
                System.arraycopy(byt, 0, by, 0, len);
                String hht = toHexString(by, by.length).toUpperCase();

                LogUtils.printI(UsbReadIml.class.getName(), "analysis----读取数据成功：" + hht);
                usbJustTagSB.append(hht);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 断开链接
     */
    public void disconnect() {
        connected = false;
        try {
            if (usbSerialPort != null) {
                usbSerialPort.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        usbSerialPort = null;
        try {
            if (usbConnection != null) {
                usbConnection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        usbConnection = null;
    }


    /**
     * 写数据
     *
     * @param bytes
     */
    public void write(byte[] bytes) {
        try {
//            if (usbSerialPort!=null){
//                usbSerialPort.write(bytes,WRITE_WAIT_MILLIS);
//            }

//            if (usbSerialPort != null && usbConnection!=null) {
//                UsbEndpoint usbEndpoint = usbSerialPort.getWriteEndpoint();
//                int transfer = usbConnection.bulkTransfer(usbEndpoint, bytes, bytes.length, 500);
//                LogUtils.printI(TAG, "write----transfer="+transfer +", data="+new String(bytes));
//            }
            mSendQueue.put(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 读取数据
     *
     * @throws RuntimeException
     */
    private void read() throws RuntimeException {
        if (!connected) {
            LogUtils.printI(TAG, "read---没有连接上");
            throw new RuntimeException("没有连接上");
        }
        new Thread(new Runnable() {
            //            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void run() {
                while (connected) {
                    if (usbSerialPort != null) {
                        try {

                            if (usbSerialPort.isOpen() && connected) {

                                byte[] buffer = new byte[128];
                                int len = usbSerialPort.read(buffer, READ_WAIT_MILLIS);
                                if (len > 0) {
                                    byte[] by = new byte[len];
                                    System.arraycopy(buffer, 0, by, 0, len);
                                    String handlerData = toHexString(by, by.length).toUpperCase();
//                                    LogUtils.printI(UsbReadIml_1.class.getName(), "读取数据成功：" + handlerData);
                                    HandlerKeyData.handler(handlerData);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            LogUtils.printI(TAG, "read---e=" + e.getMessage());
                            disconnect();
                        }
                    }
                }
            }
        }).start();
    }

    public static String toHexString(byte[] arg, int length) {
        if (arg != null && arg.length != 0) {
            StringBuilder sb = new StringBuilder();
            char[] hexArray = "0123456789ABCDEF".toCharArray();

            for (int j = 0; j < length; ++j) {
                int v = arg[j] & 255;
                sb.append(hexArray[v >>> 4]).append(hexArray[v & 15]);
            }

            return sb.toString();
        } else {
            return null;
        }
    }

}
