package bgu.spl.net.impl.Messages;

import bgu.spl.net.Utils;
import bgu.spl.net.impl.BGUSocialConnections;
import bgu.spl.net.impl.ClientToServerMessage;
import bgu.spl.net.impl.ServerToClientMessage;
import bgu.spl.net.impl.db.DB;
import bgu.spl.net.impl.db.Entities.User;
import bgu.spl.net.srv.Connections;

import java.net.SocketAddress;
import java.util.LinkedList;


public class StatMessage extends ClientToServerMessage {
    public StatMessage(byte[] bytes) {
        super(Utils.getOpCode("STAT"));
        this.args = stringsDividedByZeroes(bytes);
    }

    public String[] getListOfUsers(){
        return args.get(0).split("\\|");
    }

    @Override
    public ServerToClientMessage process(int connectionId) {
        try {
            LinkedList<ServerToClientMessage> toSend = new LinkedList<>();
            DB db = DB.getInstance();
            User me = db.getUserByConnectionId(connectionId);
            for (String userName : getListOfUsers()) {
                User currentUser = db.getUser(userName);
                if(me.isBlocked(currentUser) || currentUser.isBlocked(me))
                    throw new Exception("User is blocked");
                toSend.add(new AckMessage(this.getOpcode(),currentUser.getStat()));
            }
            BGUSocialConnections conn = BGUSocialConnections.getInstance();
            for (int i = 0; i < toSend.size()-1; i++)
                conn.send(connectionId,toSend.get(i));
            return toSend.getLast();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return new ErrorMessage(this.getOpcode());
        }
    }
}


