package com.android.launcher.ac.airflow;

/**
 * @date： 2023/11/14
 * @author: 78495
*/
public class AirflowPatternItem {

    private boolean selected = false;

    private AirflowPatternCmdValue cmdValue;

    private String name;

    public AirflowPatternItem(AirflowPatternCmdValue cmdValue, String name) {
        this.cmdValue = cmdValue;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public AirflowPatternCmdValue getCmdValue() {
        return cmdValue;
    }

    public void setCmdValue(AirflowPatternCmdValue cmdValue) {
        this.cmdValue = cmdValue;
    }
}
