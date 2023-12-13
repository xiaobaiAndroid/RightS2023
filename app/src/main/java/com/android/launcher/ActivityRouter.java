package com.android.launcher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;


/**
* @description:
* @createDate: 2023/5/30
*/
public class ActivityRouter {



    public static void toNavigation(Context context) {
        if(context == null){
            return;
        }
        try {
            if(MainActivity.class.getName().equals(MyApp.currentActivityStr)){
                return;
            }
            if(context instanceof Activity){
                Activity activity = (Activity) context;
                Intent intent = new Intent(activity, MainActivity.class);
                activity.startActivity(intent);
                MyApp.isInNavActivity = true;
            }else{
                Intent navIntent = new Intent(MyApp.getGlobalContext(), MainActivity.class);
                navIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                MyApp.getGlobalContext().startActivity(navIntent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
