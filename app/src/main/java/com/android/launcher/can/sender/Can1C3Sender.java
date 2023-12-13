package com.android.launcher.can.sender;

import com.android.launcher.entity.BinaryEntity;
import com.android.launcher.usbdriver.MUsbReceiver;
import com.android.launcher.util.DataUtils;

import module.common.utils.LogUtils;

/**
 * 修改后空调，用的时候发，不用的时候不发
 * @date： 2023/11/2
 * @author: 78495
*/
public class Can1C3Sender extends CanSenderBase<Object>{

    private String leftTemp = "00";
    private String d2 = "00";
    private String d3 = "00";
    private BinaryEntity d4 = new BinaryEntity("00");
    private BinaryEntity d5 = new BinaryEntity("00");
    private volatile BinaryEntity d6 = new BinaryEntity("00");

    public Can1C3Sender() {
        super("06", "1C3");
    }

    @Override
    public void send() {


    }

    @Override
    protected void execute() {

    }

    @Override
    public void updateData(Object o) {

    }
}
