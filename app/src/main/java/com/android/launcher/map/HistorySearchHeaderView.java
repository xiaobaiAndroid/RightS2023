package com.android.launcher.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.launcher.R;

/**
 * @date： 2023/12/2
 * @author: 78495
*/
public class HistorySearchHeaderView extends FrameLayout {

    private final TextView clearTv;

    public HistorySearchHeaderView(Context context) {
        super(context);

       LayoutInflater.from(context).inflate(R.layout.header_history_search, this, true);

        clearTv = findViewById(R.id.clearTv);
    }

    public void setClearOnClickListener(OnClickListener listener){
        clearTv.setOnClickListener(listener);
    }
}
