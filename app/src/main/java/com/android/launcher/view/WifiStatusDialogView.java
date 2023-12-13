package com.android.launcher.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import module.common.MessageEvent;
import com.android.launcher.R;
import com.lxj.xpopup.core.CenterPopupView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@Deprecated
@SuppressLint("ViewConstructor")
public class WifiStatusDialogView extends CenterPopupView {

    private TextView messageTV;

    private String message;

    public WifiStatusDialogView(Context context, String message) {
        super(context);
        this.message = message;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
         messageTV = findViewById(R.id.messageTV);
        EventBus.getDefault().register(this);
//        messageTV.setText(message);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if(event.type == MessageEvent.Type.WIFI_CONNECTING){
//            messageTV.setText(getResources().getString(R.string.wifi_connecting));
        }else if(event.type == MessageEvent.Type.WIFI_CONNECT_SUCCESS){
//            messageTV.setText(getResources().getString(R.string.wifi_success));
        }else if(event.type == MessageEvent.Type.WIFI_CONNECT_FAIL){
//            messageTV.setText(getResources().getString(R.string.wifi_fail));
        }

    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_wifi_status;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
