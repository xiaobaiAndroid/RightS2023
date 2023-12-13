package module.assistant;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import module.common.type.ActivityType;
import module.common.utils.LogUtils;
import module.common.utils.ShellCommandUtils;


/**
 * 语音助手
 *
 * @date： 2023/10/23
 * @author: 78495
 */
public class CarSiriService extends Service {

    private static final String TAG = CarSiriService.class.getSimpleName();
    private ExecutorService executorService;


    private CarSiriManager carSiriManager;

    public CarSiriService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //服务被系统kill掉之后重启进来的
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i(TAG, "onCreate----");

        try {
            carSiriManager = new CarSiriManager(this);
            carSiriManager.initIvw();
            carSiriManager.initAsr();
            executorService = Executors.newCachedThreadPool();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onDestroy() {
        if(carSiriManager!=null){
            carSiriManager.destroy();
        }
        super.onDestroy();
        Log.i(TAG, "onDestroy----");

    }

    public static void startSiriService(Context context) {
        try {
            Intent intent = new Intent(context, CarSiriService.class);
            context.startService(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopSiriService(Context context) {
        try {
            Intent intent = new Intent(context, CarSiriService.class);
            context.stopService(intent);
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage() != null) {
                Log.e("LivingService", e.getMessage());
            }

        }
    }
}
