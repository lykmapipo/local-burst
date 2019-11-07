package com.github.lykmapipo.localburst.sample;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import com.github.lykmapipo.common.provider.Provider;
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
        LocalBurst.of(new Provider() {
            @NonNull
            @Override
            public Context getApplicationContext() {
                return SampleApp.this;
            }
        });
    }
}
