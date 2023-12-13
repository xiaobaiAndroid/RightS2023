package module.common.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * @date： 2023/10/18
 * @author: 78495
*/
public class ToastUtils {


    public static synchronized void showShort(Context context,String message){
        if(context!=null){
            Toast toast = Toast.makeText(context, StringUtils.removeNull(message), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,DensityUtil.dip2px(context,300));
            toast.show();
        }
    }

    public static synchronized void showLong(Context context,String message){
        if(context!=null){
            Toast toast = Toast.makeText(context,StringUtils.removeNull(message),Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,DensityUtil.dip2px(context,300));
            toast.show();
        }
    }
}
