package com.github.lykmapipo.localburst;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.github.lykmapipo.common.Common;
import com.github.lykmapipo.common.provider.Provider;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

/**
 * Simple Local Broadcast(s) on top of {@link LocalBroadcastManager}
 *
 * @since 0.1.0
 */
public final class LocalBurst extends BroadcastReceiver {
    /**
     * default action name
     */
    public static final String DEFAULT_ACTION = "Default";

    /**
     * class instance
     */
    private static LocalBurst instance;

    /**
     * local reference for application {@link Provider}
     */
    private Provider appProvider;

    /**
     * local reference for {@link LocalBroadcastManager}
     */
    private LocalBroadcastManager localBroadcastManager;

    /**
     * local {@link OnBroadcastListener} references
     */
    private HashMap<String, Set<OnBroadcastListener>> listeners =
            new HashMap<String, Set<OnBroadcastListener>>();


    /**
     * Private constructor
     *
     * @param provider {@link Provider}
     */
    private LocalBurst(@NonNull Provider provider) {
        this.appProvider = provider;

        Context context = this.appProvider.getApplicationContext();
        this.localBroadcastManager =
                LocalBroadcastManager.getInstance(context);
    }

    /**
     * initialize new {@link LocalBurst} instance
     *
     * @return {@link LocalBurst}
     */
    @NonNull
    public static synchronized LocalBurst of(@NonNull Provider provider) {
        if (instance == null) {
            Common.of(provider);
            instance = new LocalBurst(provider);
        }
        return instance;
    }


    /**
     * obtain current {@link LocalBurst} instance
     *
     * @return {@link LocalBurst}
     */
    @Nullable
    public static synchronized LocalBurst getInstance() {
        return instance;
    }


    /**
     * Emit/Notify about specific action
     *
     * @param action action name
     */
    public static synchronized void $emit(@NonNull String action) {
        LocalBurst instance = LocalBurst.getInstance();
        if (instance != null) {
            instance.emit(action);
        }
    }

    /**
     * Emit/Notify about specific action
     *
     * @param action action name
     * @param bundle additional details to be handles to receiver of the broadcast
     */
    public static synchronized void $emit(@NonNull String action, @NonNull Bundle bundle) {
        LocalBurst instance = LocalBurst.getInstance();
        if (instance != null) {
            instance.emit(action, bundle);
        }
    }

    /**
     * Emit/Notify about default action
     *
     * @param bundle additional details to be handles to receiver of the broadcast
     */
    public static synchronized void $emit(@NonNull Bundle bundle) {
        LocalBurst instance = LocalBurst.getInstance();
        if (instance != null) {
            instance.emit(bundle);
        }
    }

    /**
     * Register a component to be able to receive action broadcasts
     *
     * @param listener {@link OnBroadcastListener}
     * @param actions  {@link String}
     */
    public static synchronized void $on(@NonNull OnBroadcastListener listener, @NonNull String... actions) {
        LocalBurst instance = LocalBurst.getInstance();
        if (instance != null) {
            instance.on(listener, actions);
        }
    }

    /**
     * Clear action listener(s)
     *
     * @param actions {@link String}
     */
    public static synchronized void $removeListeners(String... actions) {
        LocalBurst instance = LocalBurst.getInstance();
        if (instance != null) {
            instance.removeListeners(actions);
        }
    }

    /**
     * Register a component to be able to receive action broadcasts
     *
     * @param action    {@link String}
     * @param listeners {@link OnBroadcastListener}
     * @since 0.6.0
     */
    public static synchronized void $on(@NonNull String action, @NonNull OnBroadcastListener... listeners) {
        LocalBurst instance = LocalBurst.getInstance();
        if (instance != null) {
            instance.on(action, listeners);
        }
    }

    /**
     * Clear action listener(s)
     *
     * @param listeners {@link OnBroadcastListener}
     * @since 0.6.0
     */
    public static synchronized void $removeListeners(OnBroadcastListener... listeners) {
        LocalBurst instance = LocalBurst.getInstance();
        if (instance != null) {
            instance.removeListeners(listeners);
        }
    }

    /**
     * Listen for broadcasts on default action
     *
     * @param owner    The LifecycleOwner which controls the observer
     * @param observer The observer that will receive the network status
     * @since 0.6.0
     */
    @MainThread
    public static synchronized void observe(
            @NonNull LifecycleOwner owner, @NonNull Observer<Bundle> observer) {
        observe(owner, DEFAULT_ACTION, observer);
    }

    /**
     * Listen for broadcasted actions
     *
     * @param owner    The LifecycleOwner which controls the observer
     * @param action   valid action to observe
     * @param observer The observer that will receive the network status
     * @since 0.6.0
     */
    @MainThread
    public static synchronized void observe(
            @NonNull LifecycleOwner owner, @NonNull String action,
            @NonNull Observer<Bundle> observer) {
        LocalBroadcastLiveData broadcast = new LocalBroadcastLiveData(action);
        broadcast.observe(owner, observer);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //obtain broadcast action
        String action = Common.Strings.valueOr(intent.getAction(), DEFAULT_ACTION);

        //obtain action listeners
        if (this.listeners != null && !this.listeners.isEmpty()) {
            //obtain specific action broadcast listeners
            Set<OnBroadcastListener> listeners = this.listeners.get(action);

            //obtain action listener refs
            if (listeners != null && !listeners.isEmpty()) {

                //notify all action listeners
                for (OnBroadcastListener listener : listeners) {
                    Bundle extras = Common.Bundles.from(intent.getExtras());
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
    public void on(@NonNull String action, @NonNull OnBroadcastListener... listeners) {

        if (isValidAction(action)) {
            //register broadcast receiver
            this.localBroadcastManager.registerReceiver(this, new IntentFilter(action));

            //register {@link OnReceiveBroadcastListener}
            Set<OnBroadcastListener> _listeners = Common.Value.setOf(listeners);

            //get action broadcast listeners
            Set<OnBroadcastListener> broadcastListeners = this.listeners.get(action);

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
    public void on(@NonNull OnBroadcastListener listener, @NonNull String... actions) {
        //prepare action set
        Set<String> _actions = Common.Value.setOf(actions);

        //register broadcast listener
        for (String action : _actions) {
            if (isValidAction(action)) {
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
    public void emit(@NonNull String action, @NonNull Bundle bundle) {
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
    public void emit(@NonNull Bundle bundle) {
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
    public void emit(@NonNull String action) {
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
        Set<String> _actions = Common.Value.setOf(actions);
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
        Set<OnBroadcastListener> _listeners = Common.Value.setOf(listeners);

        if (!_listeners.isEmpty()) {

            Collection<Set<OnBroadcastListener>> _broadcastListeners = this.listeners.values();

            for (Set<OnBroadcastListener> broadcastListeners : _broadcastListeners) {
                broadcastListeners.removeAll(_listeners);
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
        // TODO: clear refs
    }


    /**
     * Check is specified listener exist in set of listeners
     *
     * @param listeners {@link OnBroadcastListener}
     * @return {@link Boolean}
     */
    public boolean hasListener(OnBroadcastListener... listeners) {
        boolean hasListener = false;

        Set<OnBroadcastListener> _listeners = Common.Value.setOf(listeners);
        Collection<Set<OnBroadcastListener>> _broadcastListeners = this.listeners.values();

        for (Set<OnBroadcastListener> broadcastListeners : _broadcastListeners) {
            hasListener = broadcastListeners.containsAll(_listeners);
            if (hasListener) {
                break;
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
            Set<OnBroadcastListener> onBroadcastListeners = this.listeners.get(action);
            hasListener = onBroadcastListeners != null && !onBroadcastListeners.isEmpty();
        }

        return hasListener;
    }


    /**
     * Ensure action is not null or empty
     *
     * @param action action name
     * @return whether action is valid
     */
    private Boolean isValidAction(String action) {
        return !Common.Strings.isEmpty(action);
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
        void onBroadcast(@NonNull String action, @NonNull Bundle extras);
    }

    /**
     * A {@see LiveData} class which wraps the {@link OnBroadcastListener}.
     *
     * @since 0.6.0
     */
    private static class LocalBroadcastLiveData extends LiveData<Bundle> {
        // refs
        private String action;
        private OnBroadcastListener listener = (action, extras) -> postValue(extras);

        public LocalBroadcastLiveData(@NonNull String action) {
            this.action = action;
        }

        @Override
        protected void onActive() {
            super.onActive();
            LocalBurst.$on(this.action, listener);
        }

        @Override
        protected void onInactive() {
            super.onInactive();
            LocalBurst.$removeListeners(this.action);
        }
    }
}
