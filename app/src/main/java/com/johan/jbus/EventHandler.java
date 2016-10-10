package com.johan.jbus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Johan on 2016/10/10.
 */
public class EventHandler {

    private String tag;

    private Object target;

    private Method method;

    public EventHandler(String tag, Object target, Method method) {
        this.tag = tag;
        this.target = target;
        this.method = method;
    }

    public void handle(Object... args) {
        try {
            method.invoke(target, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public boolean isSame(Object object) {
        return this.target == object;
    }

    public boolean isHit(String tag) {
        return this.tag.equals(tag);
    }

}
