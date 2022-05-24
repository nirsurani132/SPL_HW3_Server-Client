package bgu.spl.net.api;

import bgu.spl.net.impl.ClientToServerMessage;
import bgu.spl.net.impl.Message;
import bgu.spl.net.impl.Messages.AckMessage;
import bgu.spl.net.impl.Messages.LogoutMessage;
import bgu.spl.net.srv.BidiMessagingProtocol;
import bgu.spl.net.srv.Connections;

import java.net.SocketAddress;

public class BGUSocialProtocol implements BidiMessagingProtocol<Message> {

    private boolean shouldTerminate = false;
    private Connections<Message> connections;
    private int connectionId;

    @Override
    public void start(int connectionId, Connections<Message> connections) {
        this.connectionId = connectionId;
        this.connections = connections;
    }

    @Override
    public void process(Message msg) {
        if (!(msg instanceof ClientToServerMessage))
            throw new IllegalArgumentException("Message is not a ClientToServerMessage");
        Message ans = ((ClientToServerMessage) msg).process(connectionId);
        if (msg instanceof LogoutMessage && ans instanceof AckMessage )
            shouldTerminate = true;
        connections.send(connectionId, ans);
    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }

    @Override
    public void closeConnection(){
        connections.disconnect(connectionId);
    }
}