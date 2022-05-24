package bgu.spl.net.impl.Messages;

import bgu.spl.net.Utils;
import bgu.spl.net.impl.BGUSocialConnections;
import bgu.spl.net.impl.ClientToServerMessage;
import bgu.spl.net.impl.ServerToClientMessage;
import bgu.spl.net.impl.db.DB;
import bgu.spl.net.impl.db.Entities.User;

import java.util.Arrays;

public class FollowMessage extends ClientToServerMessage {
    public FollowMessage(byte[] bytes) {
        super(Utils.getOpCode("FOLLOW"));
        args.add(bytes[0] == (char) '1' ? "1" : "0");
        byte[] stringBytes = Arrays.copyOfRange(bytes, 1, bytes.length);
        this.args.add(new String(stringBytes));
    }

    // True == Follow , False == UnFollow
    public boolean getFollow() {
        return args.get(0).equals("0");
    }

    public String getUserName() {
        return args.get(1);
    }

    @Override
    public ServerToClientMessage process(int connectionId) {
        DB myDB = DB.getInstance();
        BGUSocialConnections connections = BGUSocialConnections.getInstance();
        try {
            User me = myDB.getUserByConnectionId(connectionId);
            User toFollowUser = myDB.getUser(getUserName());
            if (toFollowUser == null) throw new IllegalArgumentException("User not found");
            if (toFollowUser.isBlocked(me)) throw new IllegalArgumentException("User is blocked");
            if (getFollow()) {
                toFollowUser.addFollower(me);
                me.increaseNumOfFollowing();

            } else {
                toFollowUser.unFollow(me);
                me.decreaseNumOfFollowing();
            }
            return new AckMessage(getOpcode());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return new ErrorMessage(getOpcode());
        }
    }
}
