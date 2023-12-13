package com.android.launcher.util;

import android.util.Log;

import com.android.launcher.MainActivity;
import com.android.launcher.MyApp;

/**
* @description:
* @createDate: 2023/4/28
*/
public class ActivityNavigationUtils {

    public static void toNavMain(){

        Log.i("bzf"," App.currentActivityStr="+ MyApp.currentActivityStr);
        if(!MainActivity.class.getName().equals(MyApp.currentActivityStr)){
//            if(CarInfoActivity.class.getName().equals(App.currentActivityStr)){
//                MessageEvent messageEvent = new MessageEvent(MessageEvent.Type.CARINFOACTIVITY_BACK);
//                EventBus.getDefault().post(messageEvent);
//            }else  if(CarSetActivity.class.getName().equals(App.currentActivityStr)){
//                MessageEvent messageEvent = new MessageEvent(MessageEvent.Type.CARSETACTIVITY_BACK);
//                EventBus.getDefault().post(messageEvent);
//            } else  if(CarInfoEnergyActivity_2.class.getName().equals(App.currentActivityStr)){
//                MessageEvent messageEvent = new MessageEvent(MessageEvent.Type.CARSETACTIVITY_BACK);
//                EventBus.getDefault().post(messageEvent);
//            } else  if(CarContactsActivity.class.getName().equals(App.currentActivityStr) || CarMusicActivity.class.getName().equals(App.currentActivityStr)){
//                MessageEvent messageEvent = new MessageEvent(MessageEvent.Type.CARSETACTIVITY_BACK);
//                EventBus.getDefault().post(messageEvent);
//            }else if(App.GAODE_ACTIVITY_TAG.equals(App.currentActivityStr) || MapSearchActivity.class.getName().equals(App.currentActivityStr)){
//                    GaodeCarMapHelper.finish(App.getGlobalContext());
//                    Intent intent = new Intent(App.getGlobalContext(), NavActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    App.getGlobalContext().startActivity(intent);
//            }else{
//                Intent intent = new Intent(App.getGlobalContext(), NavActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                App.getGlobalContext().startActivity(intent);
//            }
        }

//        MessageEvent messageEvent = new MessageEvent(MessageEvent.Type.TO_NAV_ACTIVITY);
//        EventBus.getDefault().post(messageEvent);


    }
}
