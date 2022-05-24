package bgu.spl.net.impl.Messages;

import bgu.spl.net.impl.ClientToServerMessage;
import bgu.spl.net.impl.ServerToClientMessage;
import bgu.spl.net.impl.db.DB;

public class LogoutMessage extends ClientToServerMessage {
    public LogoutMessage(short op) {
        super(op);
    }

    @Override
    public ServerToClientMessage process(int ConnectionId) {
        DB myDB = DB.getInstance();
        try {
            myDB.removeConnectionID(ConnectionId);
            return new AckMessage(getOpcode());
        } catch (IllegalArgumentException e) {
            return new ErrorMessage(getOpcode());
        }
    }
}
