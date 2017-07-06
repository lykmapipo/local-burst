package com.github.lykmapipo.localburst.sample.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.github.lykmapipo.localburst.LocalBurst;
import com.github.lykmapipo.localburst.sample.R;


public class MainActivity extends AppCompatActivity implements LocalBurst.OnBroadcastListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String ACTION = MainActivity.class.getSimpleName();

    private LocalBurst broadcast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //obtain local broadcast instance
        broadcast = LocalBurst.getInstance();

        //simulate force broadcast
        Button emitButton = (Button) findViewById(R.id.btnEmit);
        emitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //emit broadcast
                Bundle bundle = new Bundle();
                bundle.putString(TAG, TAG);
                broadcast.emit(ACTION, bundle);
            }
        });

    }


    @Override
    protected void onPause() {
        super.onPause();
        broadcast.removeListeners(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        broadcast.on(ACTION, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        broadcast.removeListeners(this);
    }

    /**
     * Receive local broadcast and process it
     *
     * @param action name of the action to listen on
     * @param extras intent extras received from the action
     */
    @Override
    public void onBroadcast(String action, Bundle extras) {
        Toast.makeText(this, "Broadcast Received", Toast.LENGTH_LONG).show();
    }
}
