package bgu.spl.net.impl.db;

import bgu.spl.net.impl.db.Entities.PM;
import bgu.spl.net.impl.db.Entities.Post;
import bgu.spl.net.impl.db.Entities.User;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DB {
    ConcurrentHashMap<String, User> users;
    ConcurrentHashMap<Integer, User> ConnectionIdToUserMap;
    ConcurrentHashMap<User,Integer> UserToConnectionIDMap;
    ConcurrentLinkedQueue<Post> Posts;
    ConcurrentLinkedQueue<PM> PrivateMSGs;

    final Object usersLock;
    final Object registerSocketLock;

    public Collection<User> getConnectedUsers() {
        return ConnectionIdToUserMap.values();
    }


    private static class DBHolder{
        private static DB instance = new DB();
    }
    public static DB getInstance() {
        return DBHolder.instance;
    }

    private DB(){
        users = new ConcurrentHashMap<>();
        ConnectionIdToUserMap = new ConcurrentHashMap<>();
        usersLock = new Object();
        registerSocketLock = new Object();
        Posts = new ConcurrentLinkedQueue<>();
        UserToConnectionIDMap = new ConcurrentHashMap<>();
        PrivateMSGs = new ConcurrentLinkedQueue<>();
    }

    public void addUser(User user){
        synchronized (usersLock) {
            if(users.containsKey(user.getUsername()))
                throw new IllegalArgumentException("User already exists");
            users.put(user.getUsername(), user);
        }
    }

    public User getUser(String username){
            if(!users.containsKey(username.toLowerCase(Locale.ROOT)))
                throw new IllegalArgumentException("User not exists");
            return users.get(username.toLowerCase(Locale.ROOT));
    }

    public void registerConnectionID(Integer connectionId, User user){
        synchronized (registerSocketLock) {
            if(ConnectionIdToUserMap.containsKey(connectionId))
                throw new IllegalArgumentException("User already registered");
            ConnectionIdToUserMap.put(connectionId, user);
            UserToConnectionIDMap.put(user,connectionId);
        }
    }

    public void removeConnectionID(Integer connectionId){
        synchronized (registerSocketLock) {
            if(!ConnectionIdToUserMap.containsKey(connectionId))
                throw new IllegalArgumentException("User not login");
            User user = ConnectionIdToUserMap.get(connectionId);
            ConnectionIdToUserMap.remove(connectionId);
            UserToConnectionIDMap.remove(user);
        }
    }

    public User getUserByConnectionId(Integer connectionId){
        if(!ConnectionIdToUserMap.containsKey(connectionId))
            throw new IllegalArgumentException("No Available User by curr address");
        return ConnectionIdToUserMap.get(connectionId);
    }

    public boolean isConnectionLoggedIn(Integer connectionId) {
        return ConnectionIdToUserMap.containsKey(connectionId);
    }

    public void addPost(Post newPost) {
        Posts.add(newPost);
    }

    public Integer getUserConnectionId(User u) {
        return UserToConnectionIDMap.get(u);
    }

    public void addPM(PM pm) {
        PrivateMSGs.add(pm);
    }
}
