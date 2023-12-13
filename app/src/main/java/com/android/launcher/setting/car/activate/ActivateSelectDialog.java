package com.android.launcher.setting.car.activate;

import android.content.Context;
import android.widget.TextView;

import com.android.launcher.R;
import com.lxj.xpopup.core.CenterPopupView;

/**
* @description:
* @createDate: 2023/8/19
*/
public class ActivateSelectDialog extends CenterPopupView {

    public TextView confirmTV;
    public TextView nextIV;
    public TextView previousActivateTV;
    public TextView cancelTV;
    public  OperationType operationType = OperationType.NONE;

    private ActivateSelectDialog.Listener listener;

    public ActivateSelectDialog(Context context, Listener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.layout_dialog_activate_select;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        confirmTV = contentView.findViewById(R.id.confirmTV);
        confirmTV.setOnClickListener(v -> {
            operationType = OperationType.CONFIRM;
            if(listener!=null){
                listener.onConfirm();
            }
        });


        nextIV = contentView.findViewById(R.id.nextActivateTV);
        previousActivateTV = contentView.findViewById(R.id.previousActivateTV);
        cancelTV = contentView.findViewById(R.id.cancelTV);
        nextIV.setOnClickListener(v -> {
            setNextSelectedStatus();
            operationType = OperationType.NEXT;
            if(listener!=null){
                listener.onNext();
            }
        });

        previousActivateTV.setOnClickListener(v -> {
            setPreviousSelectedStatus();
            operationType = OperationType.PREVIOUS;
            if(listener!=null){
                listener.onPrevious();
            }
        });

        cancelTV.setOnClickListener(v -> {
            setCancelSelectedStatus();
            operationType = OperationType.CANCEL;
            if(listener!=null){
                listener.cancel();
            }
        });

        setNextSelectedStatus();
        operationType = OperationType.NEXT;
    }

    public void setCancelSelectedStatus() {
        cancelTV.setTextColor(getResources().getColor(R.color.ffffff));
        cancelTV.setBackgroundColor(getResources().getColor(R.color.colorAccent));

        nextIV.setTextColor(getResources().getColor(R.color.colorBlack));
        nextIV.setBackgroundColor(getResources().getColor(R.color.ffffff));
        confirmTV.setTextColor(getResources().getColor(R.color.colorBlack));
        confirmTV.setBackgroundColor(getResources().getColor(R.color.ffffff));
        previousActivateTV.setTextColor(getResources().getColor(R.color.colorBlack));
        previousActivateTV.setBackgroundColor(getResources().getColor(R.color.ffffff));
    }

    public void setPreviousSelectedStatus() {
        previousActivateTV.setTextColor(getResources().getColor(R.color.ffffff));
        previousActivateTV.setBackgroundColor(getResources().getColor(R.color.colorAccent));

        nextIV.setTextColor(getResources().getColor(R.color.colorBlack));
        nextIV.setBackgroundColor(getResources().getColor(R.color.ffffff));
        confirmTV.setTextColor(getResources().getColor(R.color.colorBlack));
        confirmTV.setBackgroundColor(getResources().getColor(R.color.ffffff));
        cancelTV.setTextColor(getResources().getColor(R.color.colorBlack));
        cancelTV.setBackgroundColor(getResources().getColor(R.color.ffffff));
    }

    public void setNextSelectedStatus() {
        nextIV.setTextColor(getResources().getColor(R.color.ffffff));
        nextIV.setBackgroundColor(getResources().getColor(R.color.colorAccent));

        confirmTV.setTextColor(getResources().getColor(R.color.colorBlack));
        confirmTV.setBackgroundColor(getResources().getColor(R.color.ffffff));
        previousActivateTV.setTextColor(getResources().getColor(R.color.colorBlack));
        previousActivateTV.setBackgroundColor(getResources().getColor(R.color.ffffff));
        cancelTV.setTextColor(getResources().getColor(R.color.colorBlack));
        cancelTV.setBackgroundColor(getResources().getColor(R.color.ffffff));
    }

    public void setConfirmSelectedStatus() {
        confirmTV.setTextColor(getResources().getColor(R.color.ffffff));
        confirmTV.setBackgroundColor(getResources().getColor(R.color.colorAccent));

        nextIV.setTextColor(getResources().getColor(R.color.colorBlack));
        nextIV.setBackgroundColor(getResources().getColor(R.color.ffffff));
        previousActivateTV.setTextColor(getResources().getColor(R.color.colorBlack));
        previousActivateTV.setBackgroundColor(getResources().getColor(R.color.ffffff));
        cancelTV.setTextColor(getResources().getColor(R.color.colorBlack));
        cancelTV.setBackgroundColor(getResources().getColor(R.color.ffffff));
    }


    public interface Listener{
        void onConfirm();
        void onNext();

        void onPrevious();

        void cancel();
    }

    public enum OperationType{
        NONE,
        CONFIRM,
        NEXT,
        PREVIOUS,
        CANCEL
    }
}
