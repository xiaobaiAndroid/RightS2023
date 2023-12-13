package com.android.launcher.setting.car.mileage;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.launcher.R;
import com.android.launcher.view.GridSpaceDecoration;
import com.lxj.xpopup.core.CenterPopupView;

import java.util.ArrayList;
import java.util.List;

import module.common.utils.ToastUtils;

/**
 * @date： 2023/11/4
 * @author: 78495
 */
public class NumberInputView extends CenterPopupView {

    private RecyclerView numberRV;
    private TextView contentTV;
    private NumberAdapter numberAdapter = new NumberAdapter(new ArrayList<>());

    private StringBuilder mileageSB = new StringBuilder();

    private Listener listener;

    public NumberInputView(Context context,Listener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

       TextView confirmTV = findViewById(R.id.confirmTV);
        contentTV = findViewById(R.id.contentTV);
        numberRV = findViewById(R.id.numberRV);

        final int spanCount = 4;
        numberRV.setLayoutManager(new GridLayoutManager(getContext(), spanCount));

        List<String> list = new ArrayList<>();
        list.add("0");
        list.add("1");
        list.add("2");
        list.add("3");

        list.add("4");
        list.add("5");
        list.add("6");
        list.add("7");

        list.add("8");
        list.add("9");
        list.add(".");
        list.add("Del");
        numberAdapter.setNewData(list);

        numberRV.setAdapter(numberAdapter);

        GridSpaceDecoration gridSpaceDecoration = new GridSpaceDecoration(numberAdapter, (int) getResources().getDimension(R.dimen.dp_1), spanCount);
        numberRV.addItemDecoration(gridSpaceDecoration);

        numberAdapter.setOnItemClickListener((adapter, view1, position) -> {
            if(position < numberAdapter.getData().size() - 1){
                if(mileageSB.length() == 0 && position == (numberAdapter.getData().size() - 2)){
                    return;
                }
                String item = numberAdapter.getItem(position);
                //只保留一个"."
                if(mileageSB.length() > 0 && position ==(numberAdapter.getData().size() - 2) && mileageSB.toString().contains(item)){
                    return;
                }
                mileageSB.append(numberAdapter.getItem(position));
                contentTV.setText(mileageSB.toString());
            }else{ //删除
                if(mileageSB.length() > 0){
                    mileageSB.delete(mileageSB.length() - 1,mileageSB.length());
                    contentTV.setText(mileageSB.toString());
                }
            }
        });

        confirmTV.setOnClickListener(v -> {
            if(mileageSB.length() == 0){
                ToastUtils.showShort(getActivity(),getResources().getString(R.string.not_set_mileage));
            }else{
                listener.onCarMileage(mileageSB.toString());
            }
        });
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_number_input;
    }

    interface Listener{
        void onCarMileage(String mileage);
    }
}
