package module.assistant.executor.analyzer;

import android.text.TextUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import module.assistant.SiriCommandEntity;
import module.assistant.SiriCommandIds;
import module.assistant.executor.ControlPre;
import module.common.MessageEvent;
import module.common.type.ActivityType;
import module.common.type.AppType;
import module.common.utils.BluetoothMusicHelper;
import module.common.utils.FMHelper;
import module.common.utils.GaodeCarMapHelper;
import module.common.utils.LogUtils;
import module.common.utils.SPUtils;
import module.common.utils.ShellCommandUtils;

/*命令分析器
 * @date： 2023/10/30
 * @author: 78495
 */
public class CommandAnalyzer {

    private CommandListener commandListener;
    private NumberAnalyzer numberAnalyzer;

    private static final String TAG = CommandAnalyzer.class.getSimpleName();


    public CommandAnalyzer(CommandListener commandListener) {
        this.commandListener = commandListener;
        this.numberAnalyzer = new NumberAnalyzer();
    }

    public void analysis(List<SiriCommandEntity> entityList) {
        if (entityList.size() == 1) {
            //处理一个命令
            executeOneCmd(entityList.get(0));
        } else if (entityList.size() == 2) {
            //处理两个命令
            analysisTwoCmd(entityList.get(0), entityList.get(1));
        } else if (entityList.size() >= 3) {
            //处理三个命令
            executeThreeCmd(entityList.get(0), entityList.get(1), entityList.get(3));
        }
    }

    private void analysisTwoCmd(SiriCommandEntity entity1, SiriCommandEntity entity2) {

        LogUtils.printI(TAG,"analysisTwoCmd---entity1="+entity1 +", entity2="+entity2);

        SiriCommandEntity commandEntity = entity1;
        //controlPre的位置
        ControlPre controlPre = getCotrolPre(entity1);
        if (controlPre != ControlPre.NONE) {
            commandEntity = entity2;
        }else{
            controlPre = getCotrolPre(entity2);
            if (controlPre != ControlPre.NONE) {
                commandEntity = entity1;
            }
        }
        executeTwoCmd(commandEntity,controlPre);

    }

    private ControlPre getCotrolPre(SiriCommandEntity entity) {
        ControlPre controlPre = ControlPre.NONE;

        if (SiriCommandIds.OPEN_MARK.equals(entity.getId())) {
            controlPre = ControlPre.OPEN;
        } else if (SiriCommandIds.NAV_TO.equals(entity.getId())) {
            controlPre = ControlPre.NAV;
        } else if (SiriCommandIds.TEMP_SETTING.equals(entity.getId())) {
            controlPre = ControlPre.TEMP_SETTING;
        } else if (SiriCommandIds.WIND_SETTING.equals(entity.getId())) {
            controlPre = ControlPre.WIND_SETTING;
        }
        return controlPre;
    }

    private void executeThreeCmd(SiriCommandEntity siriCommandEntity, SiriCommandEntity siriCommandEntity1, SiriCommandEntity siriCommandEntity2) {

    }

    private void executeTwoCmd(SiriCommandEntity siriCommandEntity, ControlPre controlPre) {
        String id = siriCommandEntity.getId();

        LogUtils.printI(TAG,"executeTwoCmd---siriCommandEntity="+siriCommandEntity +", controlPre="+controlPre.name());

        if(controlPre == ControlPre.OPEN){
            if (SiriCommandIds.MEDIA.equals(id)) {
                if(commandListener !=null){
                    commandListener.openMedia();
                }
            } else if (SiriCommandIds.MAP.equals(id)) {
                if(commandListener !=null){
                    commandListener.openMap();
                }
            } else if (SiriCommandIds.FM.equals(id)) {
                if(commandListener !=null){
                    commandListener.openFM();
                }
            }else if (SiriCommandIds.MAIN.equals(id)) {
                if(commandListener !=null){
                    commandListener.toLauncherMain();
                }
            }
        }else if(controlPre == ControlPre.NAV){

        }else if(controlPre == ControlPre.TEMP_SETTING){
            int number = numberAnalyzer.analysis(id);
            if(number==NumberAnalyzer.UNKNOWN){
                if(SiriCommandIds.LOWEST.equals(id)){
                    number = 16;
                }else if(SiriCommandIds.HIGHEST.equals(id)){
                    number = 28;
                }
            }
            if(commandListener!=null){
                commandListener.setAcTemp(number);
            }
        }else if(controlPre == ControlPre.WIND_SETTING){
            int number = numberAnalyzer.analysis(id);
            if(number==NumberAnalyzer.UNKNOWN){
                if(SiriCommandIds.LOWEST.equals(id)){
                    number = 1;
                }else if(SiriCommandIds.HIGHEST.equals(id)){
                    number = 7;
                }
            }
            if(commandListener!=null){
                commandListener.setAcWind(number);
            }
        }
    }


    //执行一个命令
    private void executeOneCmd(SiriCommandEntity siriCommandEntity) {
        String id = siriCommandEntity.getId();
        //返回操作
        if (SiriCommandIds.BACK.equals(id)) {
            if(commandListener!=null){
                commandListener.back();
            }
        } else if (SiriCommandIds.NEXT.equals(id)) {
            if(commandListener!=null){
                commandListener.next();
            }
        } else if (SiriCommandIds.PREVIOUS.equals(id)) {
            if(commandListener!=null){
                commandListener.previous();
            }
        } else if (SiriCommandIds.VOLUME_INCREASE.equals(id)) {
            if(commandListener!=null){
                commandListener.volumeIncrease();
            }
        } else if (SiriCommandIds.VOLUME_DECREASE.equals(id)) {
            if(commandListener!=null){
                commandListener.volumeDecrease();
            }
        } else if (SiriCommandIds.SILENCE.equals(id)) {
            if(commandListener!=null){
                commandListener.silence();
            }
        }else if(SiriCommandIds.NUMBER_16.equals(id)){
            if(commandListener!=null){
                commandListener.setAcTemp(16);
            }
        }else if(SiriCommandIds.NUMBER_17.equals(id)){
            if(commandListener!=null){
                commandListener.setAcTemp(17);
            }
        }else if(SiriCommandIds.NUMBER_18.equals(id)){
            if(commandListener!=null){
                commandListener.setAcTemp(18);
            }
        }else if(SiriCommandIds.NUMBER_19.equals(id)){
            if(commandListener!=null){
                commandListener.setAcTemp(19);
            }
        }else if(SiriCommandIds.NUMBER_20.equals(id)){
            if(commandListener!=null){
                commandListener.setAcTemp(20);
            }
        }else if(SiriCommandIds.NUMBER_21.equals(id)){
            if(commandListener!=null){
                commandListener.setAcTemp(21);
            }
        }else if(SiriCommandIds.NUMBER_22.equals(id)){
            if(commandListener!=null){
                commandListener.setAcTemp(22);
            }
        }else if(SiriCommandIds.NUMBER_23.equals(id)){
            if(commandListener!=null){
                commandListener.setAcTemp(23);
            }
        }else if(SiriCommandIds.NUMBER_24.equals(id)){
            if(commandListener!=null){
                commandListener.setAcTemp(24);
            }
        }else if(SiriCommandIds.NUMBER_25.equals(id)){
            if(commandListener!=null){
                commandListener.setAcTemp(25);
            }
        }else if(SiriCommandIds.NUMBER_26.equals(id)){
            if(commandListener!=null){
                commandListener.setAcTemp(26);
            }
        }else if(SiriCommandIds.NUMBER_27.equals(id)){
            if(commandListener!=null){
                commandListener.setAcTemp(27);
            }
        }else if(SiriCommandIds.NUMBER_28.equals(id)){
            if(commandListener!=null){
                commandListener.setAcTemp(28);
            }
        }
    }

}
