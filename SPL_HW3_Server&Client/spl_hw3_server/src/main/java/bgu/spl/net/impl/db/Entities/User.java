package bgu.spl.net.impl.db.Entities;

import bgu.spl.net.impl.Messages.NotificationMessage;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class User {
    private String username;
    private String password;
    private LocalDate birthDate;
    private ConcurrentHashMap<String, User> followers; // map to all users following me
    private LinkedList<NotificationMessage> unSeenNotifications;
    private int age;
    private AtomicInteger numOfPosts;
    private AtomicInteger numOfFollowing; // num of users i am follow
    private HashSet<User> blockedUsers;

    public User(String username, String password, String birthDate) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH);
        this.username = username.toLowerCase(Locale.ROOT);
        this.password = password;
        this.birthDate = LocalDate.parse(birthDate, format);
        this.age = Period.between(this.birthDate, LocalDate.now()).getYears();
        this.followers = new ConcurrentHashMap<>();
        this.unSeenNotifications = new LinkedList<>();
        this.numOfPosts = new AtomicInteger(0);
        this.numOfFollowing = new AtomicInteger(0);
        blockedUsers = new HashSet<>();
    }

    public java.util.Collection<User> getFollowers() {
        return followers.values();
    }

    public String getUsername() {
        return username;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public boolean isValidPassword(String password) {
        return this.password.equals(password);
    }

    @Override
    public String toString() {
        return "User{" + "username='" + username + '\'' + ", birthDate='" + birthDate + '\'' + '}';
    }

    public void addFollower(User user) {
        if (followers.containsKey(user.getUsername())) throw new IllegalArgumentException("User already follow me");
        if (this == user) throw new IllegalArgumentException("Cant follow myself 🤪");
        if (blockedUsers.contains(user)) throw new IllegalArgumentException("Cant follow blocked user");
        followers.put(user.getUsername(), user);
    }

    public void unFollow(User user) {
        if (followers.containsKey(user.getUsername())) followers.remove(user.getUsername());
        else throw new IllegalArgumentException("Given User doesnt follow this user");
    }

    public void addUnSeenNotification(NotificationMessage notification) {
        unSeenNotifications.add(notification);
    }

    public LinkedList<NotificationMessage> getUnSeenNotifications() {
        return unSeenNotifications;
    }

    public void userMarkFirstNotificationAsSeen() {
        unSeenNotifications.removeFirst();
    }

    public int getAge() {
        return age;
    }

    public void increaseNumOfPosts() {
        numOfPosts.incrementAndGet();
    }


    public void increaseNumOfFollowing() {
        numOfFollowing.incrementAndGet();
    }

    public void decreaseNumOfFollowing() {
        numOfFollowing.decrementAndGet();
    }

    public String getStat() {
        return getAge() + " " + numOfPosts.get() + " " + followers.size() + " " + numOfFollowing.get();
    }

    public boolean isBlocked(User user) {
    	return blockedUsers.contains(user);
    }

    public void blockUser(User user) {
        if (followers.containsValue(user)){
            followers.remove(user.getUsername());
            user.decreaseNumOfFollowing();
        }
    	blockedUsers.add(user);
    }
}
