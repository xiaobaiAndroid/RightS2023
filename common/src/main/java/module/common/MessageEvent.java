package module.common;

public class MessageEvent{
    public Type type;
    public Object data;

    public MessageEvent(Type type){
        this.type = type;
    }


   public enum Type{
        PHONE_CAR_NUMBER,
        MUSIC_ACTIVITY,
       CAR_INFO_ACTIVITY,
       //收音机
       FM_ACTIVITY,
       GAODE_MAP_ACTIVITY,
       //返回
       BACK_EVENT,
       //到导航主界面
       TO_MAIN,
       //触摸上下左右
       SLIDE_RIGHT,
       SLIDE_LEFT,
       SLIDE_UP,
       SLIDE_DOWN,
       //触摸点击
       TOUCH_CLICK,

       //下载进度
       APK_DOWNLOAD_PROGRESS,
       APK_DOWNLOAD_SHOW,
       APK_INSTALL,

       //汽车信息返回
       CARINFOACTIVITY_BACK,
       //汽车设置返回
       CARSETACTIVITY_BACK,
       //结束当前页面
       FINISH_MAP_SEARCH,

       WIFI_INIT,
       WIFI_CONNECTING,
       WIFI_CONNECT_FAIL,
       WIFI_CONNECT_SUCCESS,
       WIFI_CONNECT_INTERRUPT,

       //更新前空调数据
       UPDATE_FRONT_AC,
       WIFI_CONNECTED, //wifi已连接
       WIFI_DISCONNECTED, //wifi已断开
       SCAN_WIFI_CONNECTED, //扫描wifi已连接
       SCAN_WIFI_FAIL,//扫描wifi失败
       NOT_FIND_HIS_CON_WIFI, //没有找到连接过的wifi
       USB_ACCESSORY_DETACHED,  //USB设备被拔出时
       USB_ACCESSORY_ATTACHED, //当USB设备连接时
       INIT_USB, //初始化USB
       //CD机连接
       CD_CONNECT,
       //串口连接
       SERIAL_PORT_CONNECT,
       PHONE_ANSWER,  //电话接听
       PHONE_CLOSE, //挂断电话
       BLUETOOTH_CONNECTED, //蓝牙连接
       CONTACTS_PULL_FINISH, //蓝牙读取电话本完成
       UPDATE_REAR_AC_DATA, //更新后空调数据
       AIRFLOW_ALLOCATION_DATA, //空调,气流分配
       SHOW_FLOATING, //显示悬浮窗
       CLOSE_FLOATING, //关闭悬浮窗
       FINISH_NAVACTIVITY, //结束导航页
       TO_DSP, //DSP设置页面
       CLOSE_DSP, //关闭DSP页面
       UPDATE_MUSIC_INFO, // 更新音乐信息
       TO_CARPLAY, //打开CarPlay
       READ_OTG_MUSIC, //读取U盘的数据
       LOAD_USB_OTG,
       UPDATE_MUSIC_LIST, // 音乐列表
       MUSIC_PLAY_NEXT, //播放下一首
       MUSIC_PLAY,
       MUSIC_PAUSE,
       MUSIC_PLAY_NEXT_UPDATE, //更新下一首的界面

       BLUETOOTH_MUSIC_CHANGE, //蓝牙音乐播放状态改变
       BLUETOOTH_DISCONNECTED, //蓝牙设备断开
       MUSIC_RESUME, //音乐恢复播放
       START_READ_OTG_MUSIC,
       SWITCH_AIR_TYPE, //切换空调类型
       UNREGISTER_NAV_EVENTBUS, //注销NavActivityEventBus
       GET_BLUETOOTH_CONTACTS, //获取蓝牙联系人
       LIN20_TO_CAN04B,
       LIN10_TO_CAN04B,
       CAN20B_UPDATE, //can20b更新
       CAN2EE_UPDATE, // can2ee
       CAN10C_UPDATE, //10C
       CAN001_UPDATE, //001
       CAN029_UPDATE, //029
       CAN007_UPDATE, //007
       CAN39F_UPDATE, //39F
       CAN025_UPDATE, //025
       CAN10C_TO_LIN30_D6,
       CAN10C_TO_LIN30_D5,
       CAN001_TO_LIN30_D6,
       CAN069_TO_LIN30_D1,
       CAN015_UPDATE,
       CAN015_D1_ADD, //D1增加一档
       CAN015_D1_SUBTRACT, //D1减一档
       CAN069_TO_LIN30_D6,
       CAN39F_TO_LIN30,
       CAN0BC_UPDATE,
       CAN0BB_UPDATE,
       CAN0BC_TO_CAN1DB,
       //气流的fragment
       SHOW_FRAGMENT_AIRFLOW,
       SHOW_METER_TOUCH_FLOATING, //原车仪表盘触摸悬浮球
       CLOSE_METER_TOUCH_FLOATING, //关闭原车仪表盘悬浮球
       UPDATE_CONTACTS_DATA, //更新联系人界面
       //上传日志失败，服务器未响应
       UPLOAD_FAIL_SERVER_NOT_RESPONSE,
       BLUETOOTH_MUSIC_PAUSE, //蓝牙音乐暂停状态
       BLUETOOTH_MUSIC_PLAY, //蓝牙音乐播放状态
       MUSIC_PLAY_PREVIOUS, //音乐上一首
       //时钟
       CLOCK,
       //关闭气流设置悬浮窗
       CLOSE_AIRFLOW_FLOATING,
       //打开音乐
       TO_MUSIC,
       //设定
       TO_SETTING,
       //打开电话簿
       TO_PHONEBOOK,
       //空调温度设置
       AC_TEMP_SET,
       //空调风速设置
       AC_WIND_SET,
       //设置主驾风速增加
       SET_DRIVER_WIND_ADD,
       //设置主驾风速减小
       SET_DRIVER_WIND_SUBTRACT,
       //设置副驾风速增加
       SET_FRONT_SEAT_WIND_ADD,
       //设置副驾风速减小
       SET_FRONT_SEAT_WIND_SUBTRACT,
       SET_DRIVER_TEMP_ADD,
       SET_DRIVER_TEMP_SUBTRACT,
       //副驾温度增加
       SET_FRONT_SEAT_TEMP_ADD,
       SET_FRONT_SEAT_TEMP_SUBTRACT,
       //更新后挡风除雾
       UPDATE_REAR_DEMIST,
        //更新ACKeyStatusTable
       UPDATE_ACKEYSTATUS_TABLE,
        //设置空调自动模式
       SET_AC_AUTO_MODE,
       //前排空调关闭
       FRONT_AC_OFF,
       //蓝牙音乐的歌曲时长
       MUSIC_TOTAL_DURATION,
       //当前歌曲的进度
       CURRENT_MUSIC_PROGRESS,

       //USB音乐的歌曲时长
       USB_MUSIC_TOTAL_DURATION,
       //USB当前歌曲的进度
       USB_CURRENT_MUSIC_PROGRESS,
       //按了Home键
       KEY_HOME,
       //底部控制显示
       CONTROL_CENTER_SHOW,
       //底部控制关闭
       CONTROL_CENTER_HIDE,
       //播放下一首
       HOME_MUSIC_PLAY_NEXT,
       //播放上一首
       HOME_MUSIC_PLAY_PREVIOUS,
        //暂停蓝牙音乐
       STOP_BLUETOOTH_MUSIC,
       //停止USB音乐
       STOP_USB_MUSIC,

       //更新Can1E5数据到视图
       UPDATE_CAN1E5_TO_VIEW,
       //设置空调压缩机开关状态
       SET_AC_COMPRESSOR_STATUS,

       //设置前挡风除雾开关状态
       SET_FRONT_DEMIST_STATUS,
        //关闭控制中心
       CLOSE_CONTROL_CENTER,
       //显示状态栏
       SHOW_STATUS_BAR_VIEW,
       CLOSE_STATUS_BAR_VIEW,
       //打电话
       CALL_PHONE,
       //加载USB音乐
       LOAD_USB_MUSIC_LIST,
       USB_MUSIC_START_PLAY,
       USB_MUSIC_PLAY_AND_REAST,

       //停止地图导航
       STOP_MAP_NAV
   }
}
