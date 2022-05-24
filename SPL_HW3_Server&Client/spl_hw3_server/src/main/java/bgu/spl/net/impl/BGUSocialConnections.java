package bgu.spl.net.impl;

import bgu.spl.net.srv.ConnectionHandler;
import bgu.spl.net.srv.Connections;

import java.util.concurrent.ConcurrentHashMap;

public class BGUSocialConnections implements Connections<Message> {
    ConcurrentHashMap<Integer, ConnectionHandler> connections;
    int idCounter;

    private static class BGUSocialConnectionsHandler{
        public static BGUSocialConnections instance = new BGUSocialConnections();
    }

    public static BGUSocialConnections getInstance() {
        return BGUSocialConnections.BGUSocialConnectionsHandler.instance;
    }

    private BGUSocialConnections() {
        connections = new ConcurrentHashMap<>();
        idCounter = -1;
    }

    @Override
    public boolean send(int connectionId, Message msg) {
        if (!connections.containsKey(connectionId)) return false;
        connections.get(connectionId).send(msg);
        return true;
    }

    @Override
    public void broadcast(Message msg) {
        for (ConnectionHandler c : connections.values()) {
            c.send(msg);
        }
    }

    @Override
    public void disconnect(int connectionId) {
        connections.remove(connectionId);
    }

    @Override
    public int register(ConnectionHandler handler) {
        int ans = idCounter+1;
        connections.put(ans,handler);
        idCounter++;
        return ans;
    }
}
