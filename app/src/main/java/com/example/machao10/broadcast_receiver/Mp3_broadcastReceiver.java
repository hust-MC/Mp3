package com.example.machao10.broadcast_receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Mp3_broadcastReceiver extends BroadcastReceiver {
    public Mp3_broadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int cmd = intent.getExtras("cmd");
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
