package com.android.launcher.map;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItemV2;
import com.amap.api.services.poisearch.Photo;
import com.amap.api.services.poisearch.PoiResultV2;
import com.amap.api.services.poisearch.PoiSearchV2;
import com.android.launcher.MainActivity;
import com.android.launcher.R;
import com.android.launcher.base.FragmentBase;
import com.android.launcher.view.LinearDividerDecoration;
import com.bzf.module_db.entity.HistorySearchTable;
import com.bzf.module_db.repository.HistorySearchRepository;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.util.KeyboardUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import module.common.MessageEvent;
import module.common.utils.DensityUtil;
import module.common.utils.LogUtils;
import module.common.utils.UUIDStringUtils;

/**
 * @date： 2023/11/30
 * @author: 78495
 */
public class MapSearchFragment extends FragmentBase implements PoiSearchV2.OnPoiSearchListener {

    private RecyclerView resultRV;

    private MapSearchAdapter mapSearchAdapter = new MapSearchAdapter(new ArrayList<>());
    private SearchHistoryAdapter historyAdapter = new SearchHistoryAdapter(new ArrayList<>());
    private BasePopupView loadingDialog;
    private EditText searchEt;
    private Button searchBt;
    private ImageView backIV;

    @Override
    public void disposeMessageEvent(MessageEvent event) {

    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        resultRV = view.findViewById(R.id.resultRV);
        resultRV.setLayoutManager(new LinearLayoutManager(getContext()));

        initHistoryView();

        resultRV.setAdapter(historyAdapter);



        ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.cl_cccccc));
        int height = DensityUtil.dip2px(getContext(), 0.5f);
        int padding = DensityUtil.dip2px(getContext(), 20f);
        LinearDividerDecoration<ColorDrawable> decoration = new LinearDividerDecoration<>(mapSearchAdapter, colorDrawable, height, padding, padding);
        decoration.setmDrawLastItem(true);
        decoration.setDrawHeader(false);
        resultRV.addItemDecoration(decoration);

        searchEt = view.findViewById(R.id.searchEt);
        searchBt = view.findViewById(R.id.searchBt);
        backIV = view.findViewById(R.id.backIV);

        searchBt.setOnClickListener(v -> {
            String address = searchEt.getText().toString().trim();
            String city = "";
            if(MainActivity.mRegeocodeAddress!=null){
                city = MainActivity.mRegeocodeAddress.getCity();
            }
            PoiSearchV2.Query query = new PoiSearchV2.Query(address, getResources().getString(R.string.poi_types), city);
            query.setPageSize(20);
            try {
                PoiSearchV2 poiSearch = new PoiSearchV2(getContext(), query);
                poiSearch.setOnPoiSearchListener(MapSearchFragment.this);
                poiSearch.searchPOIAsyn();

                loadingDialog = new XPopup.Builder(getActivity())
                        .hasStatusBar(false)
                        .hasShadowBg(false)
                        .dismissOnTouchOutside(false)
                        .dismissOnBackPressed(false)
                        .asLoading("").show();
            } catch (AMapException e) {
                e.printStackTrace();
            }
        });
        mapSearchAdapter.setOnItemClickListener((adapter, view1, position) -> {
            PoiItemV2 poiItemV2 = mapSearchAdapter.getItem(position);
            MainActivity activity = (MainActivity) getActivity();
            activity.toMapNavPathSelectFragment(poiItemV2);
            searchEt.setText("");
            saveHistory(poiItemV2);
        });

        backIV.setOnClickListener(v -> {
            KeyboardUtils.hideSoftInput(backIV);
            EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.BACK_EVENT));
        });
    }

    private void saveHistory(PoiItemV2 poiItemV2) {
        if(poiItemV2 == null){
            return;
        }
        mExecutor.execute(() -> {
            LatLonPoint latLonPoint = poiItemV2.getLatLonPoint();
            List<Photo> photos = poiItemV2.getPhotos();

            HistorySearchTable historySearchTable = new HistorySearchTable();
            historySearchTable.setId(UUIDStringUtils.randomUUID());
            historySearchTable.setTitle(poiItemV2.getTitle());
            historySearchTable.setAdCode(poiItemV2.getAdCode());
            historySearchTable.setAdName(poiItemV2.getAdName());
            historySearchTable.setCityCode(poiItemV2.getCityCode());
            historySearchTable.setCityName(poiItemV2.getCityName());
            historySearchTable.setProvinceCode(poiItemV2.getProvinceCode());
            historySearchTable.setProvinceName(poiItemV2.getProvinceName());
            historySearchTable.setCreateDate(System.currentTimeMillis());
            historySearchTable.setUpdateDate(System.currentTimeMillis());
            historySearchTable.setPoiId(poiItemV2.getPoiId());

            if(latLonPoint!=null){
                historySearchTable.setLatitude(latLonPoint.getLatitude());
                historySearchTable.setLongitude(latLonPoint.getLongitude());
            }
            historySearchTable.setAddress(poiItemV2.getSnippet());

            if(photos!=null && photos.size() > 0){
                Photo photo = photos.get(0);
                if(photo!=null){
                    historySearchTable.setPoiPhotoTitle(photo.getTitle());
                    historySearchTable.setPoiPhotoUrl(photo.getUrl());
                }
            }
            HistorySearchRepository.getInstance().saveData(getContext(),historySearchTable);

            List<HistorySearchTable> searchTables = HistorySearchRepository.getInstance().getData(getContext());
            getActivity().runOnUiThread(() -> {
                if(historyAdapter!=null){
                    historyAdapter.setNewData(searchTables);
                    resultRV.setAdapter(historyAdapter);
                }
            });
        });
    }

    private void initHistoryView() {
        HistorySearchHeaderView searchHeaderView = new HistorySearchHeaderView(getContext());
        historyAdapter.addHeaderView(searchHeaderView);

        searchHeaderView.setClearOnClickListener(v -> mExecutor.execute(() -> {
            HistorySearchRepository.getInstance().removeAll(getContext());
            getActivity().runOnUiThread(() -> {
                if (historyAdapter != null) {
                    historyAdapter.setNewData(new ArrayList<>());
                }
            });
        }));

        mExecutor.execute(() -> {
            List<HistorySearchTable> searchTables = HistorySearchRepository.getInstance().getData(getContext());
            getActivity().runOnUiThread(() -> {
                if (historyAdapter != null) {
                    historyAdapter.setNewData(searchTables);
                }
            });
        });

        historyAdapter.setOnItemClickListener((adapter, view, position) -> {
            try {
                HistorySearchTable searchTable = historyAdapter.getItem(position);
                if(searchTable!=null){
                    MainActivity activity = (MainActivity) getActivity();
                    LatLonPoint latLonPoint = new LatLonPoint(searchTable.getLatitude(), searchTable.getLongitude());
                    PoiItemV2 poiItemV2 = new PoiItemV2(searchTable.getPoiId(), latLonPoint, searchTable.getTitle(), searchTable.getAddress());
                    activity.toMapNavPathSelectFragment(poiItemV2);
                }
                searchTable.setUpdateDate(System.currentTimeMillis());
                mExecutor.execute(() -> HistorySearchRepository.getInstance().updateData(getContext(),searchTable));

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_map_search;
    }

    @Override
    public void onPoiSearched(PoiResultV2 pageResult,
                              int errorCode) {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }

        LogUtils.printI(TAG, "onPoiSearched----errorCode=" + errorCode);
        ArrayList<PoiItemV2> pois = pageResult.getPois();
        mapSearchAdapter.setNewData(pois);
        resultRV.setAdapter(mapSearchAdapter);
    }

    @Override
    public void onPoiItemSearched(PoiItemV2 poiItem,
                                  int errorCode) {
        LogUtils.printI(TAG, "onPoiItemSearched----errorCode=" + errorCode);
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }
}
