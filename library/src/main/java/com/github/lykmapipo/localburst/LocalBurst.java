package com.github.lykmapipo.localburst;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

/**
 * <p>
 * Simple Local Broadcast(s) on top of {@link android.support.v4.content.LocalBroadcastManager}
 * </p>
 *
 * @author Lally Elias <a href="maito:lallyelias87@gmail.com">lallyelias87@gmail.com</a>
 */
public final class LocalBurst extends BroadcastReceiver {

    /**
     * local reference for android application {@link Context}
     */
    private Context context;

    /**
     * local reference for {@link LocalBroadcastManager}
     */
    private LocalBroadcastManager localBroadcastManager;

    /**
     * class lock
     */
    private static final Object lock = new Object();

    /**
     * default action name
     */
    public static final String DEFAULT_ACTION = "Default";

    /**
     * class instance
     */
    private static LocalBurst instance;

    /**
     * local {@link OnBroadcastListener} references
     */
    private HashMap<String, HashSet<OnBroadcastListener>> listeners = new
            HashMap<String, HashSet<OnBroadcastListener>>();


    /**
     * Private constructor
     *
     * @param context {@link Context}
     */
    private LocalBurst(Context context) {
        this.context = context.getApplicationContext();
        this.localBroadcastManager = LocalBroadcastManager.getInstance(this.context);
    }


    /**
     * obtain current {@link LocalBurst} instance
     *
     * @return {@link LocalBurst}
     */
    public static LocalBurst getInstance() {
        return instance;
    }

    /**
     * initialize new {@link LocalBurst} instance
     *
     * @return {@link LocalBurst}
     */
    public static synchronized LocalBurst initialize(Context context) {

        synchronized (lock) {
            if (instance == null) {
                instance = new LocalBurst(context);
            }
            return instance;
        }

    }


    @Override
    public void onReceive(Context context, Intent intent) {
        //obtain broadcast action
        String action = intent.getAction();

        //obtain action listeners
        if (this.listeners != null && !this.listeners.isEmpty()) {
            //obtain specific action broadcast listeners
            HashSet<OnBroadcastListener> listeners = this.listeners.get(action);

            //obtain action listener refs
            if (listeners != null && !listeners.isEmpty()) {

                //notify all action listeners
                for (OnBroadcastListener listener : listeners) {
                    Bundle extras = intent.getExtras();
                    listener.onBroadcast(action, extras);
                }

            }
        }

    }

    /**
     * Register a component to be able to receive action broadcasts
     *
     * @param action    {@link String}
     * @param listeners {@link OnBroadcastListener}
     */
    public void on(String action, OnBroadcastListener... listeners) {

        if (isValidAction(action)) {
            //register broadcast receiver
            this.localBroadcastManager.registerReceiver(this, new IntentFilter(action));

            //register {@link OnReceiveBroadcastListener}
            HashSet<OnBroadcastListener> _listeners = new HashSet<OnBroadcastListener>();
            Collections.addAll(_listeners, listeners);

            //get action broadcast listeners
            HashSet<OnBroadcastListener> broadcastListeners = this.listeners.get(action);

            if (broadcastListeners == null) {
                this.listeners.put(action, _listeners);
            } else {
                broadcastListeners.addAll(_listeners);
            }
        }

    }

    /**
     * Register a component to be able to receive action broadcasts
     *
     * @param listener {@link OnBroadcastListener}
     * @param actions  {@link String}
     */
    public void on(OnBroadcastListener listener, String... actions) {
        //prepare action set
        HashSet<String> _actions = new HashSet<String>();
        Collections.addAll(_actions, actions);

        //register broadcast listener
        for (String action : _actions) {
            if (isValidAction(action) && listener != null) {
                this.on(action, listener);
            }
        }

    }


    /**
     * Emit/Notify about specific action
     *
     * @param action action name
     * @param bundle additional details to be handles to receiver of the broadcast
     */
    public void emit(String action, Bundle bundle) {
        if (isValidAction(action)) {
            Intent intent = new Intent(action);
            intent.putExtras(bundle);
            this.localBroadcastManager.sendBroadcast(intent);
        }
    }


    /**
     * Emit/Notify about default action
     *
     * @param bundle additional details to be handles to receiver of the broadcast
     */
    public void emit(Bundle bundle) {
        if (isValidAction(DEFAULT_ACTION)) {
            Intent intent = new Intent(DEFAULT_ACTION);
            intent.putExtras(bundle);
            this.localBroadcastManager.sendBroadcast(intent);
        }
    }


    /**
     * Emit/Notify about specific action
     *
     * @param action action name
     */
    public void emit(String action) {
        if (isValidAction(action)) {
            Intent intent = new Intent(action);
            this.localBroadcastManager.sendBroadcast(intent);
        }
    }


    /**
     * Clear action listener(s)
     *
     * @param actions {@link String}
     */
    public void removeListeners(String... actions) {
        HashSet<String> _actions = new HashSet<String>();
        Collections.addAll(_actions, actions);
        for (String action : _actions) {
            if (isValidAction(action)) {
                this.listeners.remove(action);
            }
        }
    }


    /**
     * Clear action listener(s)
     *
     * @param listeners {@link OnBroadcastListener}
     */
    public void removeListeners(OnBroadcastListener... listeners) {
        HashSet<OnBroadcastListener> _listeners = new HashSet<OnBroadcastListener>();
        Collections.addAll(_listeners, listeners);

        if (!_listeners.isEmpty()) {

            Collection<HashSet<OnBroadcastListener>> _broadcastListeners = this.listeners.values();

            if (_broadcastListeners != null && !_broadcastListeners.isEmpty()) {
                for (HashSet<OnBroadcastListener> broadcastListeners : _broadcastListeners) {
                    broadcastListeners.removeAll(_listeners);
                }

            }

        }
    }


    /**
     * Remove all listeners and release resources
     */
    public void removeAllListeners() {
        //clear listeners
        this.dispose();
    }


    /**
     * Clear listeners and release resources
     */
    public void dispose() {
        //clear listeners
        boolean hasListeners = this.listeners != null;
        if (hasListeners) {
            this.listeners.clear();
        }
    }


    /**
     * Check is specified listener exist in set of listeners
     *
     * @param listeners {@link OnBroadcastListener}
     * @return {@link Boolean}
     */
    public boolean hasListener(OnBroadcastListener... listeners) {
        boolean hasListener = false;

        HashSet<OnBroadcastListener> _listeners = new HashSet<OnBroadcastListener>();
        Collections.addAll(_listeners, listeners);
        Collection<HashSet<OnBroadcastListener>> _broadcastListeners = this.listeners.values();

        if (_broadcastListeners != null) {
            for (HashSet<OnBroadcastListener> broadcastListeners : _broadcastListeners) {
                hasListener = broadcastListeners.containsAll(_listeners);
                if (hasListener) {
                    break;
                }
            }
        }

        return hasListener;
    }


    /**
     * Check if specified action has a set of listeners already registered
     *
     * @param action {@link String}
     */
    public boolean hasListener(String action) {
        boolean hasListener = false;

        if (isValidAction(action)) {
            HashSet<OnBroadcastListener> onBroadcastListeners = this.listeners.get(action);
            hasListener = onBroadcastListeners != null && !onBroadcastListeners.isEmpty();
        }

        return hasListener;
    }


    /**
     * Ensure action is not null or empty
     *
     * @param action
     * @return
     */
    private boolean isValidAction(String action) {
        return action != null && !action.isEmpty();
    }


    /**
     * A listener interface which receivers have to implement in order
     * to be invoked when broadcast is sent
     */
    public interface OnBroadcastListener {
        //TODO handle broadcasting exception or errors

        /**
         * Receive local broadcast and process it
         *
         * @param action name of the action to listen on
         * @param extras intent extras received from the action
         */
        void onBroadcast(String action, Bundle extras);
    }
}
