package com.johan.jbus;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Bus类
 * Created by Johan on 2016/10/10.
 */
public enum Bus {

    INSTANCE;

    // handler list
    private final List<EventHandler> eventHandlerList = Collections.synchronizedList(new LinkedList<EventHandler>());

    // handle queue
    private final ThreadLocal<ConcurrentLinkedQueue<Event>> eventsDispatch = new ThreadLocal<ConcurrentLinkedQueue<Event>>() {
        @Override
        protected ConcurrentLinkedQueue<Event> initialValue() {
            return new ConcurrentLinkedQueue<>();
        }
    };

    // handle queue state, true if queue handle event
    private final ThreadLocal<Boolean> dispatchState = new ThreadLocal<Boolean>(){
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };

    /**
     * register
     * @param object
     */
    public void register(Object object) {
        synchronized (eventHandlerList) {
            Method[] methods = object.getClass().getDeclaredMethods();
            if (methods == null && methods.length == 0) return;
            for (Method method : methods) {
                if (method.isAnnotationPresent(Subscribe.class)) {
                    Subscribe subscriber = method.getAnnotation(Subscribe.class);
                    String tag = subscriber.value();
                    EventHandler eventHandler = new EventHandler(tag, object, method);
                    eventHandlerList.add(eventHandler);
                }
            }
        }
    }

    /**
     * unregister
     * @param object
     */
    public void unregister(Object object) {
        synchronized (eventHandlerList) {
            Iterator<EventHandler> iterator = eventHandlerList.iterator();
            while (iterator.hasNext()) {
                EventHandler eventHandler = iterator.next();
                if (eventHandler.isSame(object)) {
                    iterator.remove();
                }
            }
        }
    }

    /**
     * post
     * @param tag
     * @param args
     */
    public void post(String tag, Object... args) {
        Event event = new Event(tag, args);
        eventsDispatch.get().offer(event);
        dispatch();
    }

    /**
     * execute event
     */
    private void dispatch() {
        if (dispatchState.get()) {
            return;
        }
        dispatchState.set(true);
        try {
            while (true) {
                Event event = eventsDispatch.get().poll();
                if (event == null) {
                    break;
                }
                handleEvent(event);
            }
        } finally {
            dispatchState.set(false);
        }
    }

    /**
     * handle event
     * @param event
     */
    private void handleEvent(Event event) {
        synchronized (eventHandlerList) {
            for (EventHandler eventHandler : eventHandlerList) {
                if (eventHandler.isHit(event.getTag())) {
                    eventHandler.handle(event.getArgs());
                }
            }
        }
    }

}
