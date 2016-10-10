package com.johan.jbus;

/**
 * Created by Administrator on 2016/10/10.
 */
public class Event {

    private String tag;
    private Object[] args;

    public Event(String tag, Object... args) {
        this.tag = tag;
        this.args = args;
    }

    public String getTag() {
        return tag;
    }

    public Object[] getArgs() {
        return args;
    }

}
