package com.example.webbongden.socket;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/ws/notification/{username}")
public class NotificationSocket {

    private static final Map<String, Session> activeSessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        System.out.println("WebSocket opened for user: " + username);
        activeSessions.put(username, session);
    }

    @OnClose
    public void onClose(Session session, @PathParam("username") String username) {
        System.out.println("WebSocket closed for user: " + username);
        activeSessions.remove(username);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("WebSocket error: " + throwable.getMessage());
    }

    public static void sendMessageToUser(String username, String message) {
        Session userSession = activeSessions.get(username);
        if (userSession != null && userSession.isOpen()) {
            try {
                userSession.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Could not find active WebSocket session for user: " + username);
        }
    }
}