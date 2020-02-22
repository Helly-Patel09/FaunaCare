package com.vahapps.faunacare;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Vaibhavi on 17-Apr-18.
 */

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public IBinder peekService(Context myContext, Intent service) {
        Log.d("peek","service");
        return super.peekService(myContext, service);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("broadcast reciever","yes");
        Intent startServiceIntent = new Intent(context, ListenOrder.class);
        context.startService(startServiceIntent);
    }
}
