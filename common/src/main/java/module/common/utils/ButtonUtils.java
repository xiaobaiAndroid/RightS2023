package module.common.utils;

import android.widget.ImageView;
import android.widget.TextView;

import module.common.R;

/**
 * 按钮工具
 * @date： 2023/11/3
 * @author: 78495
*/
public class ButtonUtils {


    public static synchronized void setSelected(ImageView imageView,boolean iSelected){
        if(imageView == null){
            return;
        }
        if(iSelected){
            IconUtils.setColor(imageView,imageView.getResources().getColor(R.color.button_selected));
        }else{
            IconUtils.setColor(imageView,imageView.getResources().getColor(R.color.button_unselected));
        }
    }

    public static synchronized void setTextViewSelected(TextView textView, boolean iSelected){
        if(textView == null){
            return;
        }
        if(iSelected){
            textView.setTextColor(textView.getResources().getColor(R.color.button_selected));
        }else{
            textView.setTextColor(textView.getResources().getColor(R.color.button_unselected));
        }
    }

    public static synchronized void setStatusBarSelected(ImageView imageView,boolean iSelected){
        if(imageView == null){
            return;
        }
        if(iSelected){
            IconUtils.setColor(imageView,imageView.getResources().getColor(R.color.button_selected));
        }else{
            IconUtils.setColor(imageView,imageView.getResources().getColor(R.color.status_ic_unselected));
        }
    }

    //按键设置为故障状态
    public static void setBreakdownStatus(ImageView imageView) {
        IconUtils.setColor(imageView,imageView.getResources().getColor(R.color.button_breakdown));
    }
}
