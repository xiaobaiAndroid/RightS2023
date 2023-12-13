package com.bzf.module_db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.bzf.module_db.DBTableNames;

/**
 * @date： 2023/12/2
 * @author: 78495
*/
@Entity(tableName = DBTableNames.TABLE_NAME_HISTORY_SEARCH)
public class HistorySearchTable {

    @PrimaryKey
    @NonNull
    private String id;

    private String address;

    //维度
    private double latitude;
    //经度
    private double longitude;

    //POI 的行政区划代码
    private String adCode;
    private String adName;

    private String cityName;
    private String cityCode;

    private String provinceName;
    private String provinceCode;

    //返回逆地理编码查询时POI坐标点相对于地理坐标点的方向。
    private String direction;

    //POI的图片信息
    private String poiPhotoTitle;
    private String poiPhotoUrl;

    //POI 的id，即其唯一标识。
    private String poiId;

    //POI的名称
    private String title;

    //兴趣点类型编码
    private String typeCode;

    private long updateDate;
    private long createDate;

    //是否收藏
    private boolean collected = false;

    private String data1;
    private String data2;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAdCode() {
        return adCode;
    }

    public void setAdCode(String adCode) {
        this.adCode = adCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getPoiPhotoTitle() {
        return poiPhotoTitle;
    }

    public void setPoiPhotoTitle(String poiPhotoTitle) {
        this.poiPhotoTitle = poiPhotoTitle;
    }

    public String getPoiPhotoUrl() {
        return poiPhotoUrl;
    }

    public void setPoiPhotoUrl(String poiPhotoUrl) {
        this.poiPhotoUrl = poiPhotoUrl;
    }

    public String getPoiId() {
        return poiId;
    }

    public void setPoiId(String poiId) {
        this.poiId = poiId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(long updateDate) {
        this.updateDate = updateDate;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    public String getData1() {
        return data1;
    }

    public void setData1(String data1) {
        this.data1 = data1;
    }

    public String getData2() {
        return data2;
    }

    public void setData2(String data2) {
        this.data2 = data2;
    }

    public String getAdName() {
        return adName;
    }

    public void setAdName(String adName) {
        this.adName = adName;
    }
}
