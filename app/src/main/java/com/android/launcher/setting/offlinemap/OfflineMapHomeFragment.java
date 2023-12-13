package com.android.launcher.setting.offlinemap;

import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.maps.AMapException;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.offlinemap.OfflineMapCity;
import com.amap.api.maps.offlinemap.OfflineMapManager;
import com.amap.api.maps.offlinemap.OfflineMapProvince;
import com.amap.api.maps.offlinemap.OfflineMapStatus;
import com.android.launcher.MainActivity;
import com.android.launcher.R;
import com.android.launcher.base.FragmentBase;
import com.android.launcher.type.SerialPortDataFlag;
import com.android.launcher.usbdriver.SendHelperLeft;
import com.android.launcher.view.LinearSpaceDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lxj.xpopup.XPopup;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import module.common.MessageEvent;
import module.common.utils.LogUtils;
import module.common.utils.StringUtils;

/**
 * 离线地图
 *
 * @date： 2023/12/7
 * @author: 78495
 */
public class OfflineMapHomeFragment extends FragmentBase implements OfflineMapManager.OfflineMapDownloadListener {

    protected OfflineMapProvinceAdapter provinceAdapter = new OfflineMapProvinceAdapter(new ArrayList<>());
    private RecyclerView provinceRV;
    private RecyclerView cityRV;

    private OfflineMapCityAdapter offlineMapCityAdapter = new OfflineMapCityAdapter(new ArrayList<>());
    private OfflineMapManager amapManager;
    private int currentPosition;
    private MapDownloadDialog mapDownloadDialog;
    private int currentCityPosition;
    private int downloadCityNumber;

    @Override
    public void disposeMessageEvent(MessageEvent event) {

    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        provinceRV = view.findViewById(R.id.provinceRV);
        cityRV = view.findViewById(R.id.cityRV);

        provinceRV.setLayoutManager(new LinearLayoutManager(getContext()));
        provinceRV.setItemAnimator(null);
        provinceRV.setAdapter(provinceAdapter);

        cityRV.setItemAnimator(null);
        cityRV.setLayoutManager(new LinearLayoutManager(getContext()));
        cityRV.setAdapter(offlineMapCityAdapter);

        LinearSpaceDecoration linearSpaceDecoration = new LinearSpaceDecoration(offlineMapCityAdapter, (int) getResources().getDimension(R.dimen.item_space));
        linearSpaceDecoration.setmDrawLastItem(false);
        cityRV.addItemDecoration(linearSpaceDecoration);


        provinceAdapter.setOnItemClickListener((adapter, view1, position) -> {
            if (currentPosition == position) {
                return;
            }
            OfflineMapProvinceItem provinceAdapterItem = provinceAdapter.getItem(position);
            provinceAdapterItem.setSelected(true);
            provinceAdapter.notifyItemChanged(position);

            OfflineMapProvinceItem lastItem = provinceAdapter.getItem(currentPosition);
            lastItem.setSelected(false);
            provinceAdapter.notifyItemChanged(currentPosition);

            currentPosition = position;

            OfflineMapProvince offlineMapProvince = provinceAdapterItem.getOfflineMapProvince();
            if (offlineMapProvince != null) {
                ArrayList<OfflineMapCity> cityList = offlineMapProvince.getCityList();
                offlineMapCityAdapter.setNewData(cityList);
            }
        });

        offlineMapCityAdapter.setOnItemChildClickListener((adapter, view12, position) -> {
            OfflineMapCity cityAdapterItem = offlineMapCityAdapter.getItem(position);
            showDownloadDialog(cityAdapterItem);
            currentCityPosition = position;
        });

        //构造OfflineMapManager对象
        try {
            if (amapManager == null) {
                amapManager = new OfflineMapManager(getContext(),this);


                ArrayList<OfflineMapCity> downloadOfflineMapCityList = amapManager.getDownloadOfflineMapCityList();
                LogUtils.printI(TAG,"downloadOfflineMapCityList="+downloadOfflineMapCityList.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDownloadDialog(OfflineMapCity cityAdapterItem) {

        mapDownloadDialog = new MapDownloadDialog(getActivity(), cityAdapterItem, v -> {
            try {
                if (amapManager != null) {
                    amapManager.stop();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            if (mapDownloadDialog != null) {
                mapDownloadDialog.dismiss();
                mapDownloadDialog = null;
            }

            mExecutor.execute(() -> {
                try {
                    String send = SerialPortDataFlag.START_FLAG + SerialPortDataFlag.CANCEL_DOWNLOAD_MAP.getTypeValue() + SerialPortDataFlag.END_FLAG;
                    SendHelperLeft.handler(send);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        });

        new XPopup.Builder(getActivity())
                .dismissOnTouchOutside(false)
                .dismissOnBackPressed(false)
                .hasStatusBar(false)
                .hasShadowBg(true)
                .hasNavigationBar(false)
                .asCustom(mapDownloadDialog).show();

        String city = cityAdapterItem.getCity();
        int getcompleteCode = cityAdapterItem.getcompleteCode();
        int state = cityAdapterItem.getState();
        LogUtils.printI(TAG, "showDownloadDialog---city=" + city + ", " + cityAdapterItem.getJianpin() + ", getcompleteCode=" + getcompleteCode + ", state=" + state);
        try {
            if (TextUtils.isEmpty(cityAdapterItem.getJianpin())) {
                amapManager.downloadByProvinceName(city);
                downloadCityNumber = 0;
            } else {
                amapManager.downloadByCityName(city);
            }
        } catch (AMapException e) {
            e.printStackTrace();
            LogUtils.printE(TAG, "showDownloadDialog---AMapException=" + e.getErrorMessage());
        }
        mExecutor.execute(() -> {
            try {
                String cityHex = StringUtils.stringToHex(city);
                String send;
                if (TextUtils.isEmpty(cityAdapterItem.getJianpin())) {
                    OfflineMapProvinceItem adapterItem = provinceAdapter.getItem(currentPosition);
                    OfflineMapProvince offlineMapProvince = adapterItem.getOfflineMapProvince();
                    int size = offlineMapProvince.getCityList().size();
                    LogUtils.printI(TAG, "size=" + size);
                    send = SerialPortDataFlag.START_FLAG + SerialPortDataFlag.DOWNLOAD_MAP_PROVINCE_CITY_SIZE.getTypeValue() + Integer.toHexString(size).toUpperCase() + SerialPortDataFlag.END_FLAG;
                    SendHelperLeft.handler(send);

                    send = SerialPortDataFlag.START_FLAG + SerialPortDataFlag.DOWNLOAD_MAP_PROVINCE.getTypeValue() + cityHex + SerialPortDataFlag.END_FLAG;
                } else {
                    send = SerialPortDataFlag.START_FLAG + SerialPortDataFlag.DOWNLOAD_MAP_CITY.getTypeValue() + cityHex + SerialPortDataFlag.END_FLAG;
                }
                LogUtils.printI(TAG, "send=" + send);
                SendHelperLeft.handler(send);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_offline_map_home;
    }

    @Override
    protected void setupData() {
        super.setupData();
        mExecutor.execute(() -> {
            try {
                ArrayList<OfflineMapProvince> offlineMapProvinceList = amapManager.getOfflineMapProvinceList();
                List<OfflineMapProvinceItem> provinces = new ArrayList<>();
                if (offlineMapProvinceList.isEmpty()) {
                    return;
                }


                OfflineMapProvinceItem qgProvince = null;
                OfflineMapProvinceItem bjProvince = null;
                OfflineMapProvinceItem shProvince = null;
                OfflineMapProvinceItem tjProvince = null;
                OfflineMapProvinceItem cqProvince = null;
                for (int i = 0; i < offlineMapProvinceList.size(); i++) {
                    OfflineMapProvince offlineMapProvince = offlineMapProvinceList.get(i);
                    LogUtils.printI(TAG, "offlineMapProvince=" + offlineMapProvince.getProvinceName() + ", " + offlineMapProvince.getProvinceCode());
                    OfflineMapProvinceItem offlineMapProvinceItem = new OfflineMapProvinceItem();
                    offlineMapProvinceItem.setProvince(offlineMapProvince);
                    if ("100000".equals(offlineMapProvince.getProvinceCode())) { //全国概要图
                        qgProvince = offlineMapProvinceItem;
                    } else if ("110000".equals(offlineMapProvince.getProvinceCode())) {  //北京市
                        bjProvince = offlineMapProvinceItem;
                    } else if ("310000".equals(offlineMapProvince.getProvinceCode())) {  //上海市
                        shProvince = offlineMapProvinceItem;
                    } else if ("120000".equals(offlineMapProvince.getProvinceCode())) {  //天津市
                        tjProvince = offlineMapProvinceItem;
                    } else if ("500000".equals(offlineMapProvince.getProvinceCode())) {  //重庆市
                        cqProvince = offlineMapProvinceItem;
                    } else {
                        ArrayList<OfflineMapCity> cityList = offlineMapProvince.getCityList();
                        if (cityList != null && !cityList.isEmpty()) {
                            OfflineMapCity offlineMapCity = new OfflineMapCity();
                            offlineMapCity.setState(offlineMapProvince.getState());
                            offlineMapCity.setCity(offlineMapProvince.getProvinceName());
                            offlineMapCity.setCode(offlineMapProvince.getProvinceCode());
                            offlineMapCity.setCompleteCode(offlineMapProvince.getcompleteCode());
                            cityList.add(0, offlineMapCity);
                        }
                        provinces.add(offlineMapProvinceItem);
                    }

                }
                provinces.add(0, qgProvince);
                provinces.add(1, bjProvince);
                provinces.add(2, shProvince);
                provinces.add(3, tjProvince);
                provinces.add(4, cqProvince);
//                Collections.sort(provinces, (o1, o2) -> (o1.getOfflineMapProvince().getProvinceName()).compareTo(o2.getOfflineMapProvince().getProvinceName()));

                currentPosition = 0;
                OfflineMapProvinceItem offlineMapProvinceItem = provinces.get(currentPosition);
                offlineMapProvinceItem.setSelected(true);
                ArrayList<OfflineMapCity> cities = offlineMapProvinceItem.getOfflineMapProvince().getCityList();
                getActivity().runOnUiThread(() -> {
                    if (provinceAdapter != null) {
                        provinceAdapter.setNewData(provinces);
                    }
                    if (cities != null) {
                        offlineMapCityAdapter.setNewData(cities);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    //status: 0:正在下载， 4：下载成功， 1： 解压中
    @Override
    public void onDownload(int status,
                           int completeCode,
                           String name) {
        LogUtils.printI(TAG, "onDownload---status=" + status + ",completeCode=" + completeCode + ", name=" + name);
        //下载进度，下载完成之后为解压进度
        //当前所下载的城市的名字
        if (mapDownloadDialog != null) {
            mapDownloadDialog.updateProgress(status, completeCode);

            OfflineMapCity cityAdapterItem = offlineMapCityAdapter.getItem(currentCityPosition);

            if (TextUtils.isEmpty(cityAdapterItem.getJianpin())) { //下载的是省的全部数据
                OfflineMapProvinceItem provinceItem = provinceAdapter.getItem(currentPosition);
                if (status == 4) {
                    downloadCityNumber++;
                    OfflineMapProvince offlineMapProvince = provinceItem.getOfflineMapProvince();
                    if (downloadCityNumber == offlineMapProvince.getCityList().size()) {
                        mapDownloadDialog.dismiss();
                        cityAdapterItem.setCompleteCode(100);
                        cityAdapterItem.setState(OfflineMapStatus.SUCCESS);
                        offlineMapCityAdapter.notifyItemChanged(0);

                    } else {
                        updateCityDownloadFinishStatus(name);
                    }
                }
            } else {
                if (status == 4) {
                    mapDownloadDialog.dismiss();
//                    OfflineMapCity item = offlineMapCityAdapter.getItem(currentCityPosition);
//                    item.setCompleteCode(100);
//                    offlineMapCityAdapter.notifyItemChanged(currentCityPosition);
                    updateCityDownloadFinishStatus(name);
                }
            }

        }
    }

    //更新城市下载完成的状态
    private void updateCityDownloadFinishStatus(String name) {
        for (int i = 0; i < offlineMapCityAdapter.getItemCount(); i++) {
            OfflineMapCity item = offlineMapCityAdapter.getItem(i);
            if (name.equals(item.getCity())) {
                item.setCompleteCode(100);
                item.setState(OfflineMapStatus.SUCCESS);
                offlineMapCityAdapter.notifyItemChanged(i);
                break;
            }
        }
    }

    //hasNew - true表示有更新，说明官方有新版或者本地未下载
    //name - 被检测更新的城市的名字
    @Override
    public void onCheckUpdate(boolean hasNew, String name) {
        LogUtils.printI(TAG, "onCheckUpdate---hasNew=" + hasNew + ", name=" + name);
    }

    //describe - 删除描述，如 删除成功 "本地无数据"
    //name - 所删除的城市的名字
    @Override
    public void onRemove(boolean success, String name, String describe) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            if (amapManager != null) {
                amapManager.destroy();
                amapManager = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mapDownloadDialog != null) {
            mapDownloadDialog.dismiss();
        }
    }

}
