package com.android.launcher.serialport.ttl;

import android.provider.Settings;

import com.android.launcher.MyApp;
import com.android.launcher.usbdriver.HandlerLeftData;
import com.android.launcher.usbdriver.SendHelperLeft;
import com.android.launcher.can.status.Can35dStatus;
import com.bzf.module_db.entity.CarSetupTable;
import com.bzf.module_db.repository.CarSetupRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidParameterException;

import android_serialport_api.SerialPort;
import module.common.utils.LogUtils;


/**
 * 串口工具类
 */
public class SerialHelperttlLd {

    private static final String TAG = SerialHelperttlLd.class.getSimpleName();

    private SerialPort mSerialPort;
    private static FileOutputStream mOutputStream;
    private InputStream mInputStream;
    private SerialHelperttlLd.ReadThread mReadThread;
    private String sPort;
    private boolean _isOpen;
    private byte[] _bLoopData;
    private int iDelay;

    public SerialHelperttlLd(String sPort, int iBaudRate) {
        this.sPort = "/dev/ttyS1";
        this._isOpen = false;
        this._bLoopData = new byte[]{120};
        this.iDelay = 50;
        this.sPort = sPort;
    }

    public void open() throws SecurityException, IOException, InvalidParameterException {
        this.mSerialPort = new SerialPort(new File("/dev/ttyS1"), 115200, 0);
//        this.mSerialPort = new SerialPort(new File("/dev/ttyS1"), 9600, 0);
        this.mOutputStream = this.mSerialPort.getOutputStream();
        this.mInputStream = this.mSerialPort.getInputStream();
        this.mReadThread = new SerialHelperttlLd.ReadThread();
        this.mReadThread.start();
        this._isOpen = true;

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

        this._isOpen = false;
    }

    public static void send(byte[] bOutArray) {
        try {
            if(mOutputStream!=null){
                mOutputStream.write(bOutArray);
            }
        } catch (IOException var3) {
            try {
                mOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static synchronized void sendHex(String sHex) {
        byte[] bOutArray = toByteArray(sHex);
        send(bOutArray);
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
            byte[] buffer = new byte[50];
            while (!this.isInterrupted()) {
                try {
                    if (SerialHelperttlLd.this.mInputStream != null) {
                        int size = SerialHelperttlLd.this.mInputStream.read(buffer);
                        if (size > 0) {
                            byte[] by = new byte[size];
                            System.arraycopy(buffer, 0, by, 0, size);
                            String ss = byteToHex(by);

                            LogUtils.printI(TAG, "data=" + ss);
                            // 处理左侧数据

                            String upperCaseSS = ss.toUpperCase();
                            HandlerLeftData.lefthandlerdata(upperCaseSS);
                            buffer = null;
                            buffer = new byte[50];
                        }
                    }
                } catch (Exception e) {
                    try {
                        if (mInputStream != null) {
                            mInputStream.close();
                        }

                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
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