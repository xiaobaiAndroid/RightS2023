package com.android.launcher.view;

import android.content.Context;
import android.widget.EditText;

import com.android.launcher.R;
import com.lxj.xpopup.core.CenterPopupView;

/**
* @description: 设置原车公里数
* @createDate: 2023/7/15
*/
public class OriginalCarKMView extends CenterPopupView {

    private EditText contentET;
    private Listener listener;

    public OriginalCarKMView(Context context, Listener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        contentET = findViewById(R.id.contentET);
        findViewById(R.id.confirmBt).setOnClickListener(v -> {
            try {
                String km = contentET.getText().toString().trim();
                listener.onInputContent(km);
                dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_original_car_km;
    }

    public interface Listener{

        void onInputContent(String km);
    }
}
