package module.common.type;

/**
 * @date： 2023/10/18
 * @author: 78495
*/
public enum AppPackageType {

    //Carplay
    CAR_PLAY("com.zjinnova.zlink"),
    //原车仪表
    CAR_METER("com.autochips.dvrmipi2"),
    DSP("com.xyauto.dspSetting"),
    //360全景
    PANORAMA("com.baony.avm360");

    private String typeValue;

    AppPackageType(String typeValue) {
        this.typeValue = typeValue;
    }



    public String getTypeValue() {
        return typeValue;
    }
}
