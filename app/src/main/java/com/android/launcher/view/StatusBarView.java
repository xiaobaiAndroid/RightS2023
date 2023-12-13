package com.android.launcher.view;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.launcher.MainActivity;
import com.android.launcher.R;
import com.android.launcher.service.LivingService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import module.common.MessageEvent;
import module.common.utils.ButtonUtils;
import module.common.utils.LogUtils;
import module.common.utils.StringUtils;

/**
 * @description: 状态栏
 * @createDate: 2023/5/5
 */
public class StatusBarView extends FrameLayout {

    private TextView mClockTimeTV;
    private ImageView mWifiStateIV;
    private ImageView mUsbStateIV;
    private ImageView mCDStateIV;
    private ImageView usbDriveIV;
    private ImageView bluetoothIV;
    private ImageView wifiIV;
    private TextView bluetoothNameTV;
    private TextView wifiNameTV;


    public StatusBarView(Context context) {
        this(context, null);
    }

    public StatusBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StatusBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public StatusBarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initSetup(context);
    }

    private void initSetup(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_status_bar, this, true);

        mClockTimeTV = view.findViewById(R.id.clockTimeTV);
        usbDriveIV = view.findViewById(R.id.usbDriveIV);
        bluetoothIV = view.findViewById(R.id.bluetoothIV);
        wifiIV = view.findViewById(R.id.wifiIV);
        bluetoothNameTV = view.findViewById(R.id.bluetoothNameTV);
        wifiNameTV = view.findViewById(R.id.wifiNameTV);
        mUsbStateIV = view.findViewById(R.id.usbStateIV);
        mCDStateIV = view.findViewById(R.id.cdStateIV);

        ButtonUtils.setSelected(usbDriveIV,false);
        ButtonUtils.setSelected(bluetoothIV,false);
        ButtonUtils.setSelected(wifiIV,false);
        ButtonUtils.setSelected(mUsbStateIV,false);
        ButtonUtils.setSelected(mCDStateIV,false);


        updateBlueDevice(LivingService.connectBlueDevice);

        mClockTimeTV.setText(StringUtils.removeNull(LivingService.currentTime));

        updateWifiInfo(MainActivity.connectedWifiInfo);
    }

    private void updateWifiInfo(WifiInfo wifiInfo) {
        if(wifiNameTV == null){
            return;
        }
        if(wifiInfo!=null){
            try {
                String ssid = wifiInfo.getSSID().replaceAll("\"", "");
                wifiNameTV.setText(ssid);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ButtonUtils.setSelected(wifiIV,true);
        }else{
            wifiNameTV.setText("");
            ButtonUtils.setSelected(wifiIV,false);
        }
    }


    public void setTime(String time) {
        if (mClockTimeTV != null) {
            mClockTimeTV.setText(time);
        }
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if(event.type == MessageEvent.Type.READ_OTG_MUSIC){
            ButtonUtils.setSelected(usbDriveIV,true);
        }else if (event.type == MessageEvent.Type.USB_ACCESSORY_DETACHED){
            ButtonUtils.setSelected(usbDriveIV,false);
        }else if(event.type == MessageEvent.Type.BLUETOOTH_CONNECTED){
            try {
                if(event.data instanceof BluetoothDevice){
                    updateBlueDevice((BluetoothDevice) event.data);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else if(event.type == MessageEvent.Type.BLUETOOTH_DISCONNECTED){
            if(bluetoothNameTV!=null){
                ButtonUtils.setStatusBarSelected(bluetoothIV,false);
                bluetoothNameTV.setText("");
            }
        }else if(event.type == MessageEvent.Type.WIFI_CONNECTED){
            updateWifiInfo(MainActivity.connectedWifiInfo);
        }else if(event.type == MessageEvent.Type.WIFI_DISCONNECTED){
            updateWifiInfo(MainActivity.connectedWifiInfo);
        }else if(event.type == MessageEvent.Type.CD_CONNECT){
            try {
                LogUtils.printI(StatusBarView.class.getSimpleName(),event.type.name() + ", "+event.data);
                if(event.data!=null && event.data instanceof Boolean){
                    boolean isConnected = (boolean) event.data;
                    ButtonUtils.setSelected(mCDStateIV,isConnected);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(event.type == MessageEvent.Type.SERIAL_PORT_CONNECT){
            ButtonUtils.setSelected(mUsbStateIV,true);
        }
    }

    private void updateBlueDevice(BluetoothDevice device) {
        if(device!=null){
            ButtonUtils.setStatusBarSelected(bluetoothIV,true);
            bluetoothNameTV.setText(device.getName());
        }else{
            ButtonUtils.setStatusBarSelected(bluetoothIV,false);
            bluetoothNameTV.setText("");
        }

    }

    public void setWifiImage(int resId) {
//        try {
//            mWifiStateIV.setImageResource(resId);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public void setMouseImage(int resId) {
        try {
            mCDStateIV.setImageResource(resId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setKeyboardImage(int resId) {
        try {
            mUsbStateIV.setImageResource(resId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showWifiView() {
//        if(mWifiStateIV != null){
//            mWifiStateIV.setVisibility(View.VISIBLE);
//        }
    }

    public void showKeyboardView() {
        if (mUsbStateIV != null) {
            mUsbStateIV.setVisibility(View.VISIBLE);
        }
    }

    public void showMouseView() {
        if (mCDStateIV != null) {
            mCDStateIV.setVisibility(View.VISIBLE);
        }
    }


}
