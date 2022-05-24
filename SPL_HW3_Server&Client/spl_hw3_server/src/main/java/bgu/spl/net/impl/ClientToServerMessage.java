package bgu.spl.net.impl;

import java.net.SocketAddress;
import java.util.LinkedList;

public abstract class ClientToServerMessage extends Message {
    public ClientToServerMessage(short opCode){super(opCode);}
    public ClientToServerMessage(short opCode, LinkedList<String> args){super(opCode, args);}
    public abstract ServerToClientMessage process(int connectionId);
}
