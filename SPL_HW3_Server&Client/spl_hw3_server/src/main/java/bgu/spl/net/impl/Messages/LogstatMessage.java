

package bgu.spl.net.impl.Messages;

import bgu.spl.net.impl.BGUSocialConnections;
import bgu.spl.net.impl.ClientToServerMessage;
import bgu.spl.net.impl.ServerToClientMessage;
import bgu.spl.net.impl.db.DB;
import bgu.spl.net.impl.db.Entities.User;

import java.util.LinkedList;

public class LogstatMessage extends ClientToServerMessage {
    public LogstatMessage(short op) {
        super(op);
    }

    @Override
    public ServerToClientMessage process(int connectionId) {
        try{
            DB db = DB.getInstance();
            User me = db.getUserByConnectionId(connectionId);
            LinkedList<ServerToClientMessage> toSend = new LinkedList<>();
            for (User user : db.getConnectedUsers()){
                if(!(me.isBlocked(user) || user.isBlocked(me)))
                    toSend.add(new AckMessage(this.getOpcode(),user.getStat()));
            }
            BGUSocialConnections connections = BGUSocialConnections.getInstance();
            for (int i = 0; i < toSend.size()-1; i++)
                connections.send(connectionId,toSend.get(i));
            return toSend.getLast();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return new ErrorMessage(this.getOpcode());
        }
    }
}
