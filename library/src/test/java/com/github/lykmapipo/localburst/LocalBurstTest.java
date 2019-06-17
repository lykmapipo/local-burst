package com.github.lykmapipo.localburst;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author lally elias
 * @email lallyelias87@gmail.com, lally.elias@byteskode.com
 */

@RunWith(RobolectricTestRunner.class)
public class LocalBurstTest {

    private Context context;
    private String ACTION_ONE = "ONE";
    private String ACTION_TWO = "TWO";


    @Before
    public void setup() {
        context = ApplicationProvider.getApplicationContext();
        LocalBurst.create(context);
    }


    @Test
    public void shouldBeAbleToGetLocalBroadcastInstance() {

        LocalBurst broadcast = LocalBurst.getInstance();

        assertThat(broadcast, is(not(equalTo(null))));
    }


    @Test
    public void shouldBeAbleToReceiveBroadcast() {
        LocalBurst broadcast = LocalBurst.getInstance();

        LocalBurst.OnBroadcastListener listener = new LocalBurst.OnBroadcastListener() {
            @Override
            public void onBroadcast(String action, Bundle extras) {
                assertThat(action, is(equalTo(ACTION_ONE)));
                assertThat(extras, is(nullValue()));
            }
        };
        broadcast.on(ACTION_ONE, listener);

        //emit broadcast
        Intent intent = new Intent(ACTION_ONE);
        broadcast.onReceive(context, intent);
    }


    @Test
    public void shouldBeAbleToReceiveBroadcastWithExtras() {
        LocalBurst broadcast = LocalBurst.getInstance();

        LocalBurst.OnBroadcastListener listener = new LocalBurst.OnBroadcastListener() {
            @Override
            public void onBroadcast(String action, Bundle extras) {
                assertThat(action, is(equalTo(ACTION_ONE)));

                assertThat(extras, is(notNullValue()));
                String extra = extras.getString(ACTION_TWO, "");
                assertThat(extra, is(equalTo(ACTION_TWO)));
            }
        };
        broadcast.on(ACTION_ONE, listener);

        //emit broadcast
        Intent intent = new Intent(ACTION_ONE);
        intent.putExtra(ACTION_TWO, ACTION_TWO);
        broadcast.onReceive(context, intent);
    }


    @Test
    public void shouldBeAbleToEmitBroadcast() {
        LocalBurst broadcast = LocalBurst.getInstance();

        LocalBurst.OnBroadcastListener listener = new LocalBurst.OnBroadcastListener() {
            @Override
            public void onBroadcast(String action, Bundle extras) {
                assertThat(action, is(equalTo(ACTION_ONE)));

                assertThat(extras, is(notNullValue()));
                String extra = extras.getString(ACTION_TWO, "");
                assertThat(extra, is(equalTo(ACTION_TWO)));
            }
        };
        broadcast.on(ACTION_ONE, listener);

        //emit broadcast
        Bundle bundle = new Bundle();
        bundle.putString(ACTION_TWO, ACTION_TWO);
        broadcast.emit(ACTION_ONE, bundle);

    }

    @Test
    public void shouldBeAbleToEmitBroadcast_Static() {

        LocalBurst.OnBroadcastListener listener = new LocalBurst.OnBroadcastListener() {
            @Override
            public void onBroadcast(String action, Bundle extras) {
                assertThat(action, is(equalTo(ACTION_ONE)));

                assertThat(extras, is(notNullValue()));
                String extra = extras.getString(ACTION_TWO, "");
                assertThat(extra, is(equalTo(ACTION_TWO)));
            }
        };
        LocalBurst.$on(ACTION_ONE, listener);

        //emit broadcast
        Bundle bundle = new Bundle();
        bundle.putString(ACTION_TWO, ACTION_TWO);
        LocalBurst.$emit(ACTION_ONE, bundle);

    }


    @Test
    public void shouldBeAbleToRemoveAllListenersPerSpecifiedActions() {

        LocalBurst broadcast = LocalBurst.getInstance();

        LocalBurst.OnBroadcastListener listener = new LocalBurst.OnBroadcastListener() {
            @Override
            public void onBroadcast(String action, Bundle extras) {
                assertThat(action, is(equalTo(ACTION_ONE)));
            }
        };
        broadcast.on(ACTION_ONE, listener);

        boolean actionHasListener = broadcast.hasListener(ACTION_ONE);

        assertThat(actionHasListener, is(true));

        //remove listeners using action a name
        broadcast.removeListeners(ACTION_ONE);

        actionHasListener = broadcast.hasListener(ACTION_ONE);

        assertThat(actionHasListener, is(false));

    }


    @Test
    public void shouldBeAbleToRemoveSpecifiedListener() {

        LocalBurst broadcast = LocalBurst.getInstance();

        LocalBurst.OnBroadcastListener listener = new LocalBurst.OnBroadcastListener() {
            @Override
            public void onBroadcast(String action, Bundle extras) {
                assertThat(action, is(equalTo(ACTION_ONE)));
            }
        };
        broadcast.on(ACTION_ONE, listener);

        boolean hasListener = broadcast.hasListener(listener);

        assertThat(hasListener, is(true));

        //remove specified listener
        broadcast.removeListeners(listener);

        hasListener = broadcast.hasListener(listener);

        assertThat(hasListener, is(false));

    }


    @Test
    public void shouldBeAbleToRemoveAllListeners() {

        LocalBurst broadcast = LocalBurst.getInstance();

        LocalBurst.OnBroadcastListener listener = new LocalBurst.OnBroadcastListener() {
            @Override
            public void onBroadcast(String action, Bundle extras) {
                assertThat(action, is(equalTo(ACTION_ONE)));
            }
        };
        broadcast.on(ACTION_ONE, listener);

        boolean hasListener = broadcast.hasListener(listener);
        boolean actionHasListener = broadcast.hasListener(ACTION_ONE);

        assertThat(hasListener, is(true));
        assertThat(actionHasListener, is(true));

        broadcast.removeAllListeners();

        hasListener = broadcast.hasListener(listener);
        actionHasListener = broadcast.hasListener(ACTION_ONE);

        assertThat(hasListener, is(false));
        assertThat(actionHasListener, is(false));

    }


    @Test
    public void shouldBeAbleToRegisterBroadcastListener() {
        LocalBurst broadcast = LocalBurst.getInstance();

        LocalBurst.OnBroadcastListener listener = new LocalBurst.OnBroadcastListener() {
            @Override
            public void onBroadcast(String action, Bundle extras) {
                assertThat(action, is(equalTo(ACTION_ONE)));
            }
        };
        broadcast.on(ACTION_ONE, listener);

        boolean hasListener = broadcast.hasListener(listener);
        boolean actionHasListener = broadcast.hasListener(ACTION_ONE);

        assertThat(hasListener, is(true));
        assertThat(actionHasListener, is(true));
    }


    @After
    public void cleanup() {
        LocalBurst.getInstance().dispose();
        context = null;
    }
}
