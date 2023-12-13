package com.android.launcher.can.receiver;

import android.content.Context;

import com.bzf.module_db.entity.Can007Table;
import com.bzf.module_db.repository.Can007Repository;

import org.greenrobot.eventbus.EventBus;

import module.common.MessageEvent;
import module.common.utils.LogUtils;

/**
* @description:  除雾状态
 *
 * 00 00 40 00 3A 4D
* @createDate: 2023/7/24
*/
public class Can007Receiver extends CanReceiverBase{

    public Can007Receiver(Context context) {
        super(context);
    }

    @Override
    protected void disposeData(String data) {
        LogUtils.printI(TAG,"data="+data);
        if(data.length() == 12){
            String d1 = data.substring(0, 2);
            String d2 = data.substring(2, 4);
            String d3 = data.substring(4, 6);
            String d4 = data.substring(6, 8);
            String d5 = data.substring(8, 10);
            String d6 = data.substring(10, 12);

            Can007Table table = Can007Repository.getInstance().getData(context, deviceId);
            table.setD1(d1);
            table.setD2(d2);
            table.setD3(d3);
            table.setD4(d4);
            table.setD5(d5);
            table.setD6(d6);

            Can007Repository.getInstance().updateData(context,table);

            MessageEvent messageEvent = new MessageEvent(MessageEvent.Type.UPDATE_REAR_DEMIST);
            messageEvent.data = d3;
            EventBus.getDefault().post(messageEvent);
        }
    }
}