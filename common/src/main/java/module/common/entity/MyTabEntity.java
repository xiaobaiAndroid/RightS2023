package module.common.entity;

import com.flyco.tablayout.listener.CustomTabEntity;

/**
 * @date： 2023/11/15
 * @author: 78495
*/
public class MyTabEntity implements CustomTabEntity {

    private String title;

    public MyTabEntity(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getTabTitle() {
        return title;
    }

    @Override
    public int getTabSelectedIcon() {
        return 0;
    }

    @Override
    public int getTabUnselectedIcon() {
        return 0;
    }
}
