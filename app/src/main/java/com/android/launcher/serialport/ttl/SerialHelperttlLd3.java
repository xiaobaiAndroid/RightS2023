package com.android.launcher.serialport.ttl;

import android.text.TextUtils;

import com.android.launcher.usbdriver.HandlerCanData;
import com.android.launcher.util.DataUtils;
import com.android.launcher.util.FuncUtil;
import com.android.launcher.util.HandlerRequestbyte;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidParameterException;
import java.util.concurrent.LinkedTransferQueue;

import android_serialport_api.SerialPort;
import module.common.utils.LogUtils;

/**
 * @description:
 * @createDate: 2023/8/26
 */
public class SerialHelperttlLd3 {

    private static final String TAG = SerialHelperttlLd3.class.getSimpleName();

    private SerialPort mSerialPort;
    private static FileOutputStream mOutputStream;
    private InputStream mInputStream;
    private ReadThread mReadThread;
    private String sPort;
    private boolean _isOpen;
    private byte[] _bLoopData;
    private int iDelay;

    public static volatile String lastData = "";

    public HandlerRequestbyte handlerRequestbyte = new HandlerRequestbyte();

    //发送队列
    private static LinkedTransferQueue<byte[]> mSendQueue = new LinkedTransferQueue();

    public SerialHelperttlLd3(String sPort, int iBaudRate) {
        this.sPort = "/dev/ttyS3";
        this._isOpen = false;
        this._bLoopData = new byte[]{120};
        this.iDelay = 50;
        this.sPort = sPort;
    }

    public void open() throws SecurityException, IOException, InvalidParameterException {
        this.mSerialPort = new SerialPort(new File("/dev/ttyS3"), 115200, 0);
//        this.mSerialPort = new SerialPort(new File("/dev/ttyS1"), 9600, 0);
        this.mOutputStream = this.mSerialPort.getOutputStream();
        this.mInputStream = this.mSerialPort.getInputStream();
        this.mReadThread = new ReadThread();
        this.mReadThread.start();
        writeSendCommandData();
        this._isOpen = true;
        LogUtils.printI(TAG, "open----");
    }

    private void writeSendCommandData() {
        new Thread(() -> {
            while (mOutputStream != null) {
                try {
                    byte[] bytes = mSendQueue.take();
                    if (mOutputStream != null) {
                        mOutputStream.write(bytes);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtils.printE(TAG, e.getMessage());
                }
            }
        }).start();
    }


    public void close() {
        if (this.mReadThread != null) {
            this.mReadThread.interrupt();
        }

        if (this.mSerialPort != null) {
            this.mSerialPort.close();
            this.mSerialPort = null;
        }
        if (this.mInputStream != null) {
            try {
                this.mInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.mInputStream = null;
        }
        if (mOutputStream != null) {
            try {
                mOutputStream.close();
                mOutputStream = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this._isOpen = false;
    }


    public static synchronized void sendHex(String hex) {
        try {
            if (TextUtils.isEmpty(hex)) {
                return;
            }
            if (hex.length() < 32) {
                LogUtils.printI(SerialHelperttlLd3.class.getSimpleName(), "hex=" + hex);
            }

            byte[] bOutArray = DataUtils.hexStringToByteArray(hex);
            mSendQueue.put(bOutArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean setBaudRate(int iBaud) {
        if (this._isOpen) {
            return false;
        } else {
            return true;
        }
    }

    public boolean setBaudRate(String sBaud) {
        int iBaud = Integer.parseInt(sBaud);
        return this.setBaudRate(iBaud);
    }

    public String getPort() {
        return this.sPort;
    }

    public boolean setPort(String sPort) {
        if (this._isOpen) {
            return false;
        } else {
            this.sPort = sPort;
            return true;
        }
    }

    public byte[] getbLoopData() {
        return this._bLoopData;
    }


    public boolean isOpen() {
        return this._isOpen;
    }

    public int getiDelay() {
        return this.iDelay;
    }

    public void setiDelay(int iDelay) {
        this.iDelay = iDelay;
    }

    /**
     * 读取串口数据线程
     */
    private class ReadThread extends Thread {
        private ReadThread() {
        }

        public void run() {
            super.run();
            while (!this.isInterrupted()) {
                try {
//                    LogUtils.printI(TAG, "ReadThread----start---");
                    if (mInputStream != null) {
                        byte[] buffer = new byte[128];
                        int size = mInputStream.read(buffer);
//                        LogUtils.printI(TAG, "ReadThread----size="+size);
                        if (size > 0) {
                            byte[] by = new byte[size];
                            System.arraycopy(buffer, 0, by, 0, size);
                            String ss = toHexString(by, by.length);
                            HandlerCanData.handlerdata(ss.toUpperCase());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    retry();
                }
            }
        }
    }

    public String toHexString(byte[] arg, int length) {
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


    private void retry() {
        LogUtils.printI(TAG, "retry----");
        new Thread(() -> {
            try {
                FuncUtil.serialHelperttl3.close();
                FuncUtil.serialHelperttl3 = null;
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                FuncUtil.initSerialHelper3();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }


    /**
     * 数据转成16进制数据
     *
     * @param buffer
     * @return
     */
    private String byteToHex(byte[] buffer) {
        if (buffer == null) {
            return null;
        }
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < buffer.length; i++) {
            b.append(String.format("%02x", buffer[i] & 0xFF));
        }
        return b.toString();
    }

    /**
     * 数据转成二进制数据发送
     *
     * @param arg
     * @return
     */
    public static byte[] toByteArray(String arg) {
        if (arg != null) {
            char[] NewArray = new char[1000];
            char[] array = arg.toCharArray();
            int length = 0;
            for (int i = 0; i < array.length; i++) {
                if (array[i] != ' ') {
                    NewArray[length] = array[i];
                    length++;
                }
            }
            int EvenLength = (length % 2 == 0) ? length : length + 1;
            if (EvenLength != 0) {
                int[] data = new int[EvenLength];
                data[EvenLength - 1] = 0;
                for (int i = 0; i < length; i++) {
                    if (NewArray[i] >= '0' && NewArray[i] <= '9') {
                        data[i] = NewArray[i] - '0';
                    } else if (NewArray[i] >= 'a' && NewArray[i] <= 'f') {
                        data[i] = NewArray[i] - 'a' + 10;
                    } else if (NewArray[i] >= 'A' && NewArray[i] <= 'F') {
                        data[i] = NewArray[i] - 'A' + 10;
                    }
                }
                byte[] byteArray = new byte[EvenLength / 2];
                for (int i = 0; i < EvenLength / 2; i++) {
                    byteArray[i] = (byte) (data[i * 2] * 16 + data[i * 2 + 1]);
                }
                return byteArray;
            }
        }
        return new byte[]{};
    }

}
