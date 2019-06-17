package com.github.lykmapipo.localburst.sample;

import android.app.Application;

import com.github.lykmapipo.localburst.LocalBurst;

/**
 * @author lally elias
 * @email lallyelias87@gmail.com, lally.elias@byteskode.com
 * @date 10/18/16
 */
public class SampleApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //initialize
        LocalBurst.create(getApplicationContext());
    }
}
