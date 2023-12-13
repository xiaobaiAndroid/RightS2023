package module.assistant.executor;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import module.assistant.SiriCommandEntity;
import module.assistant.SiriCommandIds;
import module.assistant.executor.analyzer.CommandAnalyzer;
import module.assistant.executor.analyzer.CommandListener;
import module.common.MessageEvent;
import module.common.type.ActivityType;
import module.common.type.AppType;
import module.common.utils.BluetoothMusicHelper;
import module.common.utils.FMHelper;
import module.common.utils.GaodeCarMapHelper;
import module.common.utils.SPUtils;
import module.common.utils.ShellCommandUtils;

/**
 * 语音命令执行者
 * @date： 2023/10/27
 * @author: 78495
*/
public class CommandExecutor implements CommandListener {

    private static final String TAG = CommandExecutor.class.getSimpleName();

    private Context context;
    private CommandExecutor.Listener listener;
    private final ExecutorService executorService;

    private CommandAnalyzer analyzer;


    public CommandExecutor(Context context, CommandExecutor.Listener listener) {
        this.context = context;
        this.listener = listener;
        executorService = Executors.newCachedThreadPool();
        this.analyzer = new CommandAnalyzer(this);
    }

    public void execute(List<SiriCommandEntity> list) {
        executorService.execute(() -> {
            if(list == null || list.isEmpty()){
                return;
            }
            List<SiriCommandEntity> entityList = new ArrayList<>();
            for (SiriCommandEntity entity : list){
                if(entity!=null && entity.getReliability() >= SiriCommandIds.RELIABILITY){
                    entityList.add(entity);
                }
            }

            Collections.sort(entityList, (o1, o2) -> o2.getReliability() - o1.getReliability());

            analyzer.analysis(entityList);
        });

    }

    @Override
    public void toLauncherMain() {

    }

    @Override
    public void openFM() {
        FMHelper.startFM();
    }

    @Override
    public void openMap() {
        GaodeCarMapHelper.start(context);
    }

    @Override
    public void openMedia() {
        EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.TO_MUSIC));
    }

    @Override
    public void openSetting() {

    }

    @Override
    public void setAcTemp(int temp) {
        MessageEvent messageEvent = new MessageEvent(MessageEvent.Type.AC_TEMP_SET);
        messageEvent.data = temp;
        EventBus.getDefault().post(messageEvent);
    }

    @Override
    public void setAcWind(int wind) {
        MessageEvent messageEvent = new MessageEvent(MessageEvent.Type.AC_WIND_SET);
        messageEvent.data = wind;
        EventBus.getDefault().post(messageEvent);
    }

    @Override
    public void back() {
        int appType = SPUtils.getInt(context, SPUtils.SP_FOREGROUND_APP, AppType.SELF.ordinal());
        if (appType == AppType.SELF.ordinal()) {
            ShellCommandUtils.back();
        } else if (appType == AppType.MAP.ordinal()) {

        } else {
            EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.TO_MAIN));
        }
    }

    @Override
    public void next() {
        int appType = SPUtils.getInt(context, SPUtils.SP_FOREGROUND_APP, AppType.SELF.ordinal());
        if (appType == AppType.FM.ordinal()) {
            FMHelper.down(context);
        } else if (appType == AppType.SELF.ordinal()) {
            int currentActivity = SPUtils.getInt(context, SPUtils.SP_CURRENT_ACTIVITY, ActivityType.MAIN.ordinal());
            if (currentActivity == ActivityType.MUSIC_BLUETOOTH.ordinal()) {
                BluetoothMusicHelper.next(context);
            } else if (currentActivity == ActivityType.MUSIC_USB.ordinal()) {

            }
        }
    }

    @Override
    public void previous() {
        int appType = SPUtils.getInt(context, SPUtils.SP_FOREGROUND_APP, AppType.SELF.ordinal());
        if (appType == AppType.FM.ordinal()) {
            FMHelper.up(context);
        } else if (appType == AppType.SELF.ordinal()) {
            int currentActivity = SPUtils.getInt(context, SPUtils.SP_CURRENT_ACTIVITY, ActivityType.MAIN.ordinal());
            if (currentActivity == ActivityType.MUSIC_BLUETOOTH.ordinal()) {
                BluetoothMusicHelper.previous(context);
            } else if (currentActivity == ActivityType.MUSIC_USB.ordinal()) {

            }
        }
    }

    @Override
    public void volumeIncrease() {
        ShellCommandUtils.volumeIncrease();
    }

    @Override
    public void volumeDecrease() {
        ShellCommandUtils.volumeDecrease();
    }

    @Override
    public void silence() {
        ShellCommandUtils.silence();
    }

   public interface Listener{

        //关闭命令执行，等唤醒才会再次执行
        void closeCommandExecute();
    }
}
