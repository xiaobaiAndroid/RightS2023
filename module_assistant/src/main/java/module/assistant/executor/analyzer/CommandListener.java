package module.assistant.executor.analyzer;

/**
 * 分析的命令
 * @date： 2023/10/30
 * @author: 78495
*/
public interface CommandListener {

    void toLauncherMain();

    void openFM();

    void openMap();

    //打开多媒体
    void openMedia();

    //打开设置
    void openSetting();

    /**
     * 设置空调温度
     * @param temp 温度
     * */
    void setAcTemp(int temp);

    //设置空调风量
    void setAcWind(int wind);


    void back();

    void next();

    void previous();

    void volumeIncrease();

    void volumeDecrease();

    void silence();
}
