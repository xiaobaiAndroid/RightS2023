package com.android.launcher.util;//package com.android.launcher.util;
//
//import android.app.Service;
//import android.bluetooth.BluetoothDevice;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.IBinder;
//
//import androidx.annotation.Nullable;
//
//import com.android.launcher.receiver.BluetoothBroadCastReceiver;
//
//public class BroadCastService extends Service {
//
//
//    public BluetoothBroadCastReceiver bluetoothBroadCastReceiver ;
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        //1.创建广播接收者对象
//        bluetoothBroadCastReceiver = new BluetoothBroadCastReceiver();
//        //2.创建intent-filter对象
//        IntentFilter filter = new IntentFilter();
//
//
//        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
//        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
//        filter.addAction("com.android.radio.widget.freq_volue");
//        filter.addAction("com.txznet.adapter.recv");
//        filter.addAction("com.xyauto.bt.songinfo");
//        filter.addAction("android.bluetooth.headsetclient.profile.action.AG_CALL_CHANGED");
//
//                //3.注册广播接收者
//        registerReceiver(bluetoothBroadCastReceiver, filter);
//
////        new Thread(){
////            @Override
////            public void run() {
////                super.run();
////                while (true){
////                    ActivityManager activityManager = (ActivityManager) App.getGlobalContext().getSystemService(Context.ACTIVITY_SERVICE);
////                    List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(1);
////
////                    if (!tasks.isEmpty()) {
////
////                        ComponentName topActivity = tasks.get(0).topActivity ;
////                       Log.i("AAA",topActivity.getPackageName());
////
////                    }else{
////                        Log.i("AAA","----1-1-1-1-");
////                    }
////                }
////            }
////        }.start();
////        softKeyBroadManager = new SoftKeyBroadManager(root);
//
//    }
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        //解除注册
//        unregisterReceiver(bluetoothBroadCastReceiver);
//    }
//
//}
