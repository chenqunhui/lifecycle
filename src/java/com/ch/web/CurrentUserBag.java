package com.ch.web;

/**
 *
 * 当前登录用户存储
 *
 */

public class CurrentUserBag {
    private static ThreadLocal<Object> _t = new ThreadLocal<>();

    public static void cleanAndSet(Object o) {
        _t.remove();
        _t.set(o);
    }

    public static Object get() {
        return _t.get();
    }

    public static void clean() {
        _t.remove();
    }
}
