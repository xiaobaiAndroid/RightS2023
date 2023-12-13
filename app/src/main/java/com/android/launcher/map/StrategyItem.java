package com.android.launcher.map;

import com.amap.api.navi.enums.PathPlanningStrategy;
import com.amap.api.navi.view.RouteOverLay;

/**
 * @date： 2023/12/1
 * @author: 78495
*/
public class StrategyItem {

    private int routeId;

    private RouteOverLay routeOverLay;

    private String name;


    //路线距离,单位：公里
    private float distance;

    private long time;

    private boolean selected;

    //路径Id
    private long pathId;

    //获取红绿灯总数
    private int trafficLightCount;



    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public RouteOverLay getRouteOverLay() {
        return routeOverLay;
    }

    public void setRouteOverLay(RouteOverLay routeOverLay) {
        this.routeOverLay = routeOverLay;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public long getPathId() {
        return pathId;
    }

    public void setPathId(long pathId) {
        this.pathId = pathId;
    }

    public int getTrafficLightCount() {
        return trafficLightCount;
    }

    public void setTrafficLightCount(int trafficLightCount) {
        this.trafficLightCount = trafficLightCount;
    }
}
