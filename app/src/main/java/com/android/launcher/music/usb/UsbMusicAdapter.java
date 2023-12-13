package com.android.launcher.music.usb;

import com.android.launcher.R;
import module.common.utils.StringUtils;

import com.android.launcher.music.CarMusicItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
* @description:
* @createDate: 2023/6/9
*/
public class UsbMusicAdapter extends BaseQuickAdapter<CarMusicItem, BaseViewHolder> {

    public UsbMusicAdapter(List<CarMusicItem> data) {
        super(R.layout.layout_item_usb_muisc,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CarMusicItem item) {
        if(item != null){
            if(item.isSelected()){
                helper.setBackgroundRes(R.id.itemCL,R.drawable.item_seletor);
            }else{
                helper.setBackgroundRes(R.id.itemCL,0);
            }

            helper.setText(R.id.titleTV, StringUtils.removeNull(item.getTitle()));
        }
    }
}
