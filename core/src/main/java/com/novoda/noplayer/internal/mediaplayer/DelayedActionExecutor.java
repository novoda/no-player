package com.novoda.noplayer.internal.mediaplayer;

import android.os.Handler;

import java.util.Iterator;
import java.util.Map;

class DelayedActionExecutor {

    private final Handler handler;
    private final Map<Action, Runnable> runnables;

    DelayedActionExecutor(Handler handler, Map<Action, Runnable> runnables) {
        this.handler = handler;
        this.runnables = runnables;
    }

    void performAfterDelay(final Action action, long delayInMillis) {
        Runnable actionRunnable = new Runnable() {
            @Override
            public void run() {
                action.perform();
                runnables.remove(action);
            }
        };
        runnables.put(action, actionRunnable);
        handler.postDelayed(actionRunnable, delayInMillis);
    }

    void clearAllActions() {
        Iterator<Map.Entry<Action, Runnable>> it = runnables.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Action, Runnable> entry = it.next();
            handler.removeCallbacks(entry.getValue());
            it.remove();
        }
    }

    interface Action {

        void perform();
    }
}
