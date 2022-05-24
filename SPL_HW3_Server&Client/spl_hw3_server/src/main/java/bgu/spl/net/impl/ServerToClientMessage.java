package bgu.spl.net.impl;

public abstract class ServerToClientMessage  extends  Message {
    public ServerToClientMessage(short opcode) {
        super(opcode);
    }
    public abstract byte[] encode();
}
