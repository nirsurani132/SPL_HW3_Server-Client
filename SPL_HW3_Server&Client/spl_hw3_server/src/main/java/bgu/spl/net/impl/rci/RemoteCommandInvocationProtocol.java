package bgu.spl.net.impl.rci;

import bgu.spl.net.api.MessagingProtocol;
import java.io.Serializable;
import java.net.SocketAddress;

public class RemoteCommandInvocationProtocol<T> implements MessagingProtocol<Serializable> {

    private T arg;

    public RemoteCommandInvocationProtocol(T arg) {
        this.arg = arg;
    }

    @Override
    //We Added address as param
    public Serializable process(Serializable msg , SocketAddress address) {
        return ((Command) msg).execute(arg);
    }

    @Override
    public boolean shouldTerminate() {
        return false;
    }

}
