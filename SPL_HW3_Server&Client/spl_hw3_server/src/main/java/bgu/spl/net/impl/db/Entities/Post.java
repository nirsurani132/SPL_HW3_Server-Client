package bgu.spl.net.impl.db.Entities;

import java.util.LinkedList;

public class Post {
    User creator;
    String content;
    LinkedList<User> taggedUsers;

    public Post( User creator, String content, LinkedList<User> taggedUsers) {
        this.creator = creator;
        this.content = content;
        this.taggedUsers = taggedUsers;
    }

    public String getContent() {
        return content;
    }

    public User getCreator() {
        return creator;
    }

    public LinkedList<User> getTaggedUsers() {
        return taggedUsers;
    }

}
