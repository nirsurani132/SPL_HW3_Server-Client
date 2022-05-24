package bgu.spl.net.impl.Messages;

import bgu.spl.net.Utils;
import bgu.spl.net.impl.BGUSocialConnections;
import bgu.spl.net.impl.ClientToServerMessage;
import bgu.spl.net.impl.ServerToClientMessage;
import bgu.spl.net.impl.db.DB;
import bgu.spl.net.impl.db.Entities.Post;
import bgu.spl.net.impl.db.Entities.User;

import java.util.HashSet;
import java.util.LinkedList;

public class PostMessage extends ClientToServerMessage {
    public PostMessage(byte[] content) {
        super(Utils.getOpCode("POST"));
        this.args = stringsDividedByZeroes(content);
    }

    public String getContent() {
        return args.getFirst();
    }

    @Override
    public ServerToClientMessage process(int ConnectionId) {
        try {
            DB db = DB.getInstance();
            User creator = db.getUserByConnectionId(ConnectionId);
            LinkedList<User> taggedUsers = new LinkedList<>();
            for(String st : getContent().split(" "))
                if(st.startsWith("@"))
                    taggedUsers.add(db.getUser(st.substring(1)));
            Post newPost = new Post(creator, getContent(), taggedUsers);
            db.addPost(newPost);
            creator.increaseNumOfPosts();
            BGUSocialConnections connections = BGUSocialConnections.getInstance();
            HashSet<User> usersToNotify = new HashSet<>();
            usersToNotify.addAll(taggedUsers);
            usersToNotify.addAll(creator.getFollowers());
            for(User u : usersToNotify) {
                Integer connectionId = db.getUserConnectionId(u);
                if(!u.isBlocked(creator)) {
                    NotificationMessage nf = new NotificationMessage(NotificationMessage.MessageType.PUBLIC, creator.getUsername(), getContent());
                    if (connectionId != null)
                        connections.send(connectionId, nf);
                    else
                        u.addUnSeenNotification(nf);
                }
            }
            return new AckMessage(getOpcode());
        }
        catch (Exception e) {
            return new ErrorMessage(getOpcode());
        }
    }
}
