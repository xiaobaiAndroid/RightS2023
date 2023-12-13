package com.android.launcher.can;

import java.util.List;

public interface CanParent {
    void handlerCan(List<String> msg) ;

    void clear();
}
