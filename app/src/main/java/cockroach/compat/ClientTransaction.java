package cockroach.compat;

import android.os.IBinder;

public class ClientTransaction {

    public IBinder getActivityToken() {
        return null;
    }
}
