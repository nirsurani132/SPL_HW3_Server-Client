package bgu.spl.net.impl.Messages;

import bgu.spl.net.Utils;
import bgu.spl.net.impl.BGUSocialConnections;
import bgu.spl.net.impl.ClientToServerMessage;
import bgu.spl.net.impl.ServerToClientMessage;
import bgu.spl.net.impl.db.DB;
import bgu.spl.net.impl.db.Entities.User;

import java.net.SocketAddress;

public class BlockMessage extends ClientToServerMessage {
    public BlockMessage(byte[] content) {
        super(Utils.getOpCode("BLOCK"));
        this.args = stringsDividedByZeroes(content);
    }
    public String getUserName() {
        return args.get(0);
    }

    @Override
    public ServerToClientMessage process(int ConnectionId) {
        try {
            DB db = DB.getInstance();
            User blocker = db.getUserByConnectionId(ConnectionId);
            User blocked = db.getUser(getUserName());
            if (blocker == null || blocked == null) {
                throw new IllegalArgumentException("User not found");
            }
            blocker.blockUser(blocked);
            return new AckMessage(getOpcode());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return new ErrorMessage(getOpcode());
        }
    }
}
