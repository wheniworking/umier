package com.longwei.umier.service;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionContext {

    private static Map<String, HttpSession> sessionMap = new ConcurrentHashMap<>();


    public static void addSession(String id, HttpSession session) {
        sessionMap.put(id, session);
    }

    public static HttpSession getSession(String id) {
        return sessionMap.get(id);
    }

    public  static void delSession(String id) {
        sessionMap.remove(id);
    }

}
