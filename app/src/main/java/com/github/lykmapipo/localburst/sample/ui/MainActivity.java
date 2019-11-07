package com.github.lykmapipo.localburst.sample.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.lykmapipo.localburst.LocalBurst;
import com.github.lykmapipo.localburst.sample.R;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String ACTION = "Custom";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //simulate force custom broadcast
        Button emitCustomButton = findViewById(R.id.btnEmitCustom);
        emitCustomButton.setOnClickListener(v -> {
            //emit broadcast
            Bundle bundle = new Bundle();
            bundle.putString(TAG, TAG);
            LocalBurst.$emit(ACTION, bundle);
        });


        //simulate force default broadcast
        Button emitDefaultButton = findViewById(R.id.btnEmitDefault);
        emitDefaultButton.setOnClickListener(v -> {
            //emit broadcast
            Bundle bundle = new Bundle();
            bundle.putString(TAG, TAG);
            LocalBurst.$emit(bundle);
        });

        // use live data observers

        LocalBurst.observe(this, bundle -> {
            toast(LocalBurst.DEFAULT_ACTION, bundle);
        });

        LocalBurst.observe(this, ACTION, bundle -> {
            toast(ACTION, bundle);
        });

    }

    private void toast(@NonNull String action, @NonNull Bundle extras) {
        Toast.makeText(this, action + " Broadcast Received", Toast.LENGTH_LONG).show();
    }
}
