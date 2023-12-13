package com.android.launcher.lin.recevier;

/**
* @description:
* @createDate: 2023/7/24
*/
public abstract class LinReceiverBase {

    protected static final String DATA_HEAD = "AA000000000";

    protected String data;
    protected String lastData;

    protected boolean isStop = false;

    protected String TAG = this.getClass().getSimpleName();

    public final void updateData(String data){
        this.data =data;
        if(!isStop){
            if(lastData.equalsIgnoreCase(data)){
                return;
            }
            lastData = data;
            try {
                disposeData(data);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    protected abstract void disposeData(String data);

    public void stop(){
        isStop = true;
    }

    public void release(){
        data = "";
        lastData = "";
        isStop = true;
    }
}
