package module.assistant;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.GrammarListener;
import com.iflytek.cloud.LexiconListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvent;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.VoiceWakeuper;
import com.iflytek.cloud.WakeuperListener;
import com.iflytek.cloud.WakeuperResult;
import com.iflytek.cloud.util.ResourceUtil;

import org.json.JSONObject;

import java.util.List;

import module.assistant.executor.CommandExecutor;
import module.assistant.util.FucUtil;
import module.assistant.util.JsonParser;
import module.common.utils.LogUtils;

/**
 * @date： 2023/10/24
 * @author: 78495
 */
public class CarSiriManager implements CommandExecutor.Listener {

    private static final String TAG = CarSiriManager.class.getSimpleName();

    // 语音识别对象
    private SpeechRecognizer mAsr;


    private String mEngineType = SpeechConstant.TYPE_LOCAL;

    private final String GRAMMAR_TYPE_BNF = "bnf";

    private Context context;

    // 本地语法文件
    private String mLocalGrammar = null;
    // 本地词典
    private String mLocalLexicon = null;
    // 本地语法构建路径
    private String grmPath;

    private VoiceWakeuper voiceWakeuper;

    private View floatingView;

    //是否已经唤醒
    private volatile boolean isWaking = false;


    // 返回结果格式，支持：xml,json
    private String mResultType = "json";

    private CommandExecutor commandExecutor;

    public CarSiriManager(Context context) {
        this.context = context;
        try {
            commandExecutor = new CommandExecutor(context, this);
            CarSiriUtils.init(context);

            grmPath = context.getExternalFilesDir("msc").getAbsolutePath() + "/grm";

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void buildGrammar(SpeechRecognizer recognizer, String localGrammar, GrammarListener listener) {
        recognizer.setParameter(SpeechConstant.PARAMS, null);
        recognizer.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
        // 设置引擎类型
        recognizer.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置语法构建路径
        recognizer.setParameter(ResourceUtil.GRM_BUILD_PATH, grmPath);
        //设置音频焦点, true: 让SDK获取系统的音频焦点
        recognizer.setParameter(SpeechConstant.KEY_REQUEST_FOCUS,"null");
        //后端点超时
        recognizer.setParameter(SpeechConstant.VAD_BOS,"1000");
        // 设置资源路径
        recognizer.setParameter(ResourceUtil.ASR_RES_PATH, getResourcePath());
        int ret = recognizer.buildGrammar(GRAMMAR_TYPE_BNF, localGrammar, listener);
        if (ret != ErrorCode.SUCCESS) {
            LogUtils.printI(TAG, "语法构建失败,错误码：" + ret + ",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
        }
    }

    //获取识别资源路径
    private String getResourcePath() {
        StringBuffer tempBuffer = new StringBuffer();
        //识别通用资源
        tempBuffer.append(ResourceUtil.generateResourcePath(context, ResourceUtil.RESOURCE_TYPE.assets, "asr/common.jet"));
        return tempBuffer.toString();
    }

    //初始化唤醒
    public void initIvw() {
        voiceWakeuper = VoiceWakeuper.createWakeuper(context, null);
        if (voiceWakeuper == null) {
            LogUtils.printI(TAG, "initIvw---voiceWakeuper is null");
            return;
        }

        // 清空参数
        voiceWakeuper.setParameter(SpeechConstant.PARAMS, null);
        // 唤醒门限值，根据资源携带的唤醒词个数按照“id:门限;id:门限”的格式传入
        voiceWakeuper.setParameter(SpeechConstant.IVW_THRESHOLD, "0:" + 1450);
        // 设置唤醒模式
        voiceWakeuper.setParameter(SpeechConstant.IVW_SST, "wakeup");
        // 设置持续进行唤醒 0:唤醒一次，1：持续唤醒
        voiceWakeuper.setParameter(SpeechConstant.KEEP_ALIVE, "1");
        // 设置闭环优化网络模式,模式0：关闭闭环优化功能; 模式1：开启闭环优化功能，允许上传优化数据。需开发者自行管理优化资源。
        voiceWakeuper.setParameter(SpeechConstant.IVW_NET_MODE, "0");

        //设置音频焦点, true: 让SDK获取系统的音频焦点
        voiceWakeuper.setParameter(SpeechConstant.KEY_REQUEST_FOCUS,"null");

        // 设置唤醒资源路径
        voiceWakeuper.setParameter(SpeechConstant.IVW_RES_PATH, getIvwResource());
        // 设置唤醒录音保存路径，保存最近一分钟的音频
        voiceWakeuper.setParameter(SpeechConstant.IVW_AUDIO_PATH,
                context.getExternalFilesDir("msc").getAbsolutePath() + "/ivw.wav");
        voiceWakeuper.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        voiceWakeuper.startListening(mWakeuperListener);
    }

    private String getIvwResource() {
        final String resPath = ResourceUtil.generateResourcePath(context, ResourceUtil.RESOURCE_TYPE.assets, "ivw/" + context.getString(R.string.app_id) + ".jet");
        LogUtils.printI(TAG, "getIvwResource---resPath=" + resPath);
        return resPath;
    }


    private void updateLexicon(SpeechRecognizer recognizer, String name, LexiconListener listener) {
        recognizer.setParameter(SpeechConstant.PARAMS, null);
        // 设置引擎类型
        recognizer.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
        // 设置资源路径
        recognizer.setParameter(ResourceUtil.ASR_RES_PATH, getResourcePath());
        //使用8k音频的时候请解开注释
//				mAsr.setParameter(SpeechConstant.SAMPLE_RATE, "8000");
        // 设置语法构建路径
        recognizer.setParameter(ResourceUtil.GRM_BUILD_PATH, grmPath);
        // 设置语法名称, 用,号设置多个语法文件：control
        recognizer.setParameter(SpeechConstant.GRAMMAR_LIST, name);
        // 设置文本编码格式
        recognizer.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
        int ret = recognizer.updateLexicon(name, mLocalLexicon, listener);

        if (ret != ErrorCode.SUCCESS) {
            Log.i(TAG, "更新词典失败,错误码  ret =" + ret);
        }
    }

    public void destroy() {
        try {
            if (null != mAsr) {
                // 退出时释放连接
                mAsr.cancel();
                mAsr.destroy();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (floatingView != null) {
                CarMICView.closeFloating(context, floatingView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (VoiceWakeuper.getWakeuper() != null) {
            VoiceWakeuper.getWakeuper().destroy();
        }
    }


    private WakeuperListener mWakeuperListener = new WakeuperListener() {


        @Override
        public void onResult(WakeuperResult result) {
            try {
                String text = result.getResultString();
                JSONObject object;
                object = new JSONObject(text);
                StringBuffer buffer = new StringBuffer();
                buffer.append("【RAW】 " + text);
                buffer.append("\n");
                buffer.append("【操作类型】" + object.optString("sst"));
                buffer.append("\n");
                buffer.append("【唤醒词id】" + object.optString("id"));
                buffer.append("\n");
                buffer.append("【得分】" + object.optString("score"));
                buffer.append("\n");
                buffer.append("【前端点】" + object.optString("bos"));
                buffer.append("\n");
                buffer.append("【尾端点】" + object.optString("eos"));

                LogUtils.printI(TAG, "onResult---text=" + object.toString());

                if (!isWaking) {
                    isWaking = true;
                    if (floatingView != null) {
                        CarMICView.closeFloating(context, floatingView);
                        floatingView = null;
                    }
                    floatingView = CarMICView.showFloating(context);
                    if (!setAsrParam()) {
                        Log.i(TAG, "请先构建语法。");
                        return;
                    }
                    startRecognize();

//                    if (VoiceWakeuper.getWakeuper() != null) {
//                        VoiceWakeuper.getWakeuper().stopListening();
//                        VoiceWakeuper.getWakeuper().destroy();
//                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onError(SpeechError error) {
            LogUtils.printI(TAG, "唤醒---onError---error=" + error);
        }

        @Override
        public void onBeginOfSpeech() {
            LogUtils.printI(TAG, "唤醒---onBeginOfSpeech---");
        }

        @Override
        public void onEvent(int eventType, int isLast, int arg2, Bundle obj) {
            switch (eventType) {
                // EVENT_RECORD_DATA 事件仅在 NOTIFY_RECORD_DATA 参数值为 真 时返回
                case SpeechEvent.EVENT_RECORD_DATA:
                    final byte[] audio = obj.getByteArray(SpeechEvent.KEY_EVENT_RECORD_DATA);
                    LogUtils.printI(TAG, "ivw audio length: " + audio.length);
                    break;
            }
        }

        @Override
        public void onVolumeChanged(int volume) {
//            LogUtils.printI(TAG,"唤醒---onVolumeChanged---volume="+volume);
        }
    };


    //初始化离线命令
    public void initAsr() {

        Log.i(TAG, "grmPath=" + grmPath);
        // 初始化识别对象
        mAsr = SpeechRecognizer.createRecognizer(context, errCode -> LogUtils.printI(TAG, "initAsr---errCode=" + errCode));
        if (mAsr == null) {
            Log.i(TAG, "masr is null");
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("导航")
                .append("\n").append("电话本")
                .append("\n").append("主界面")
                .append("\n").append("地图")
                .append("\n").append("退下吧")
                .append("\n").append("退下")
                .append("\n").append("关闭")
                .append("\n").append("后退")
                .append("\n").append("返回")
                .append("\n").append("导航到")
                .append("\n").append("收音机")
                .append("\n").append("通讯录")
                .append("\n").append("电话本")
                .append("\n").append("电话簿")
                .append("\n").append("下一个")
                .append("\n").append("下一首")
                .append("\n").append("上一个")
                .append("\n").append("上一首")
                .append("\n").append("多媒体")
                .append("\n").append("音乐")
                .append("\n").append("设定")
                .append("\n").append("设置")
                .append("\n").append("桌面");

        // 初始化语法、命令词
        mLocalLexicon = stringBuilder.toString();
//        mLocalGrammar = FucUtil.readFile(this, "call.bnf", "utf-8");
        mLocalGrammar = FucUtil.readFile(context, "control.bnf", "utf-8");
        buildGrammar(mAsr, mLocalGrammar, grammarListener);

    }

    /**
     * 更新词典监听器。
     */
    private LexiconListener lexiconListener = (lexiconId, error) -> {
        if (error == null) {
            Log.i(TAG, "词典更新成功");
        } else {
            Log.i(TAG, "词典更新失败,错误码  ret =" + error.getErrorCode());
        }
    };

    /**
     * 构建语法监听器。
     */
    private GrammarListener grammarListener = (grammarId, error) -> {
        if (error == null) {
            Log.i(TAG, "语法构建成功：" + grammarId);
//            updateLexicon(mAsr, "control", lexiconListener);
        } else {
            Log.i(TAG, "语法构建失败,错误码：" + error.getErrorCode());
        }
    };


    private void startRecognize() {
        if (!mAsr.isListening()) {
            int ret = mAsr.startListening(mRecognizerListener);
            if (ret != ErrorCode.SUCCESS) {
                Log.i(TAG, "识别失败,错误码: " + ret + ",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
            }
        }
    }


    /**
     * 命令识别参数设置
     *
     * @return
     */
    public boolean setAsrParam() {
        boolean result = false;
        // 清空参数
        mAsr.setParameter(SpeechConstant.PARAMS, null);
        // 设置识别引擎
        mAsr.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);

        // 设置本地识别资源
        mAsr.setParameter(ResourceUtil.ASR_RES_PATH, getResourcePath());
        // 设置语法构建路径
        mAsr.setParameter(ResourceUtil.GRM_BUILD_PATH, grmPath);
        // 设置返回结果格式
        mAsr.setParameter(SpeechConstant.RESULT_TYPE, mResultType);
        // 设置本地识别使用语法id
        mAsr.setParameter(SpeechConstant.LOCAL_GRAMMAR, "control");
        // 设置识别的门限值
        mAsr.setParameter(SpeechConstant.MIXED_THRESHOLD, String.valueOf(SiriCommandIds.RELIABILITY));
        // 使用8k音频的时候请解开注释
//			mAsr.setParameter(SpeechConstant.SAMPLE_RATE, "8000");
        result = true;

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        mAsr.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mAsr.setParameter(SpeechConstant.ASR_AUDIO_PATH,
                context.getExternalFilesDir("msc").getAbsolutePath() + "/asr.wav");
        return result;
    }


    /**
     * 识别监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
//            Log.i(TAG, "当前正在说话，音量大小：：volume=" + volume);
        }

        @Override
        public void onResult(final RecognizerResult result, boolean isLast) {
            if (isWaking) {
                if (null != result && !TextUtils.isEmpty(result.getResultString())) {
                    List<SiriCommandEntity> list = JsonParser.parseGrammarResult(result.getResultString());
                    if (!list.isEmpty()) {
                        if (commandExecutor != null) {
                            commandExecutor.execute(list);
                        }

                    }
                    LogUtils.printI(TAG, "识别到的命令：list=" + list);
                } else {
                    LogUtils.printI(TAG, "recognizer result : null");
                }
                mAsr.stopListening();
                if (isWaking) {
                    startRecognize();
                }
            }
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            LogUtils.printI(TAG, "结束说话");


        }

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            LogUtils.printI(TAG, "开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            LogUtils.printI(TAG, "error=" + error);
            if (isWaking) {
                if (error.getErrorCode() == 20005) { //没有匹配的结果
                    startRecognize();
                } else if (error.getErrorCode() == 23008) {
                    startRecognize();
                }
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }

    };

    @Override
    public void closeCommandExecute() {
        if (floatingView != null) {
            try {
                CarMICView.closeFloating(context, floatingView);
                floatingView = null;
                if (mAsr != null) {
                    mAsr.stopListening();
                    mAsr.destroy();
                }
                isWaking = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
