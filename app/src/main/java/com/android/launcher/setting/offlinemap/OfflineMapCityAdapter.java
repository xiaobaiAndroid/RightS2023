package com.android.launcher.setting.offlinemap;

import com.amap.api.maps.offlinemap.OfflineMapCity;
import com.android.launcher.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @date： 2023/12/7
 * @author: 78495
*/
public class OfflineMapCityAdapter extends BaseQuickAdapter<OfflineMapCity, BaseViewHolder> {

    public OfflineMapCityAdapter(List<OfflineMapCity> data) {
        super(R.layout.item_offlinemap_update,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OfflineMapCity item) {
        helper.setText(R.id.cityTV,item.getCity());
        if(item.getcompleteCode() == 100){ //返回城市下载完成的百分比，100表示下载完成。
            helper.setText(R.id.updateTv,mContext.getResources().getString(R.string.update));
        }else{
            helper.setText(R.id.updateTv,mContext.getResources().getString(R.string.download));
        }
        helper.addOnClickListener(R.id.updateTv);
    }
}
