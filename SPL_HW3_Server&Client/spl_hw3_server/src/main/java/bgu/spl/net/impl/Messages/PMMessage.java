package bgu.spl.net.impl.Messages;

import bgu.spl.net.Utils;
import bgu.spl.net.impl.BGUSocialConnections;
import bgu.spl.net.impl.ClientToServerMessage;
import bgu.spl.net.impl.ServerToClientMessage;
import bgu.spl.net.impl.db.DB;
import bgu.spl.net.impl.db.Entities.PM;
import bgu.spl.net.impl.db.Entities.User;

import javax.management.Notification;
import java.net.SocketAddress;

public class PMMessage extends ClientToServerMessage {
    public PMMessage(byte[] bytes){
        super(Utils.getOpCode("PM"));
        this.args = stringsDividedByZeroes(bytes);
    }

    public String getUserName(){
        return args.getFirst();
    }

    public String getContent(){
        return Utils.censorship(args.get(1));
    }

    public String getDateAndTime(){
        return args.get(2);
    }

    @Override
    public ServerToClientMessage process(int ConnectionId) {
        DB db = DB.getInstance();
        User sender, receiver;
        try{
            sender = db.getUserByConnectionId(ConnectionId);
            receiver = db.getUser(getUserName());
            if(!receiver.getFollowers().contains(sender))
                throw new IllegalArgumentException("You are not following this user - therefore you cant send a PM");
            if(receiver.isBlocked(sender))
                throw new IllegalArgumentException("You are blocked by this user - therefore you cant send a PM");
            PM currPm = new PM(sender, receiver, getContent(), getDateAndTime());
            db.addPM(currPm);
            NotificationMessage notificationMessage = new NotificationMessage(NotificationMessage.MessageType.PM, sender.getUsername(),currPm.getContent() + " " + this.getDateAndTime());
            if(db.getUserConnectionId(receiver) != null){
                BGUSocialConnections connections = BGUSocialConnections.getInstance();
                connections.send(db.getUserConnectionId(receiver), notificationMessage);
            }
            else
                receiver.addUnSeenNotification(notificationMessage);
            return new AckMessage(getOpcode());
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return new ErrorMessage(getOpcode());
        }
    }
}
