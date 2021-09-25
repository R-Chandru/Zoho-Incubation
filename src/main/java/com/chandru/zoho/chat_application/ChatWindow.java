package com.chandru.zoho.chat_application;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/chat")
public class ChatWindow {
    public static Set<Session> userSession = Collections.newSetFromMap(new ConcurrentHashMap<Session, Boolean>());

    @OnOpen
    public void onOpen(Session session) {
        userSession.add(session);
    }

    @OnClose
    public void onClose(Session session) {
        userSession.remove(session);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        for (Session ses : userSession) {
            ses.getAsyncRemote().sendText(message);
        }
    }

    @OnError
    public void onError(Throwable throwable) {
        System.out.println(throwable.getMessage());
    }
}
