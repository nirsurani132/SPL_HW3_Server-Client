package bgu.spl.net.impl.Messages;

import bgu.spl.net.Utils;
import bgu.spl.net.impl.BGUSocialConnections;
import bgu.spl.net.impl.ClientToServerMessage;
import bgu.spl.net.impl.Message;
import bgu.spl.net.impl.ServerToClientMessage;
import bgu.spl.net.impl.db.DB;
import bgu.spl.net.impl.db.Entities.User;

import java.util.LinkedList;

public class LoginMessage extends ClientToServerMessage {
    public LoginMessage(byte[] bytes) {
        super(Utils.getOpCode("LOGIN"));
        this.args = this.stringsDividedByZeroes(bytes);
    }

    public String getUserName() {
        return this.args.getFirst();
    }

    public String getPassword() {
        return this.args.get(1);
    }

    public String getCaptcha() {
        // if CATCHA Byte is 0 - return null , means Login ERROR
        if(args.size() == 2)
            return null;
        return this.args.get(2);
    }

    @Override
    public ServerToClientMessage process(int ConnectionId) {
        DB myDB = DB.getInstance();
        User currUser;
        try {
            currUser = myDB.getUser(getUserName());
            if( currUser == null || !currUser.isValidPassword(getPassword()) || getCaptcha() == null || !getCaptcha().equals("1")) {
                System.out.println("Login failed on arguments");
                return new ErrorMessage(getOpcode());
            }
            if(myDB.getUserConnectionId(currUser) != null){
                throw new IllegalArgumentException("User already logged in");
            }
            myDB.registerConnectionID(ConnectionId, currUser);
            BGUSocialConnections connections = BGUSocialConnections.getInstance();
            LinkedList<ServerToClientMessage> toSend = new LinkedList<>();
            toSend.add(new AckMessage(getOpcode()));
            toSend.addAll(currUser.getUnSeenNotifications());
            for (int i=0 ; i<toSend.size()-1 ; i++) {
                connections.send(ConnectionId, toSend.getFirst());
                if (toSend.getFirst() instanceof NotificationMessage)
                    currUser.userMarkFirstNotificationAsSeen();
            }
            return toSend.getLast();
        }
        catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            return new ErrorMessage(getOpcode());
        }
    }
}
