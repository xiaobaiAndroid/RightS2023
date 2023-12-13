package module.common.utils;

import android.graphics.PorterDuff;
import android.widget.ImageView;

/**
 * @date： 2023/10/20
 * @author: 78495
*/
public class IconUtils {

    /**
     * @date： 2023/10/20
     * @author: 78495
     * @param color 0：表示icon本来的颜色
    */
    public static void setColor(ImageView imageView, int color){
        if (color == 0) {
            imageView.setColorFilter(0);

        } else {
            imageView.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }
    }
}
