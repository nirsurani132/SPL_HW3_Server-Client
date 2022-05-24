package bgu.spl.net.impl.db.Entities;

public class PM {
    User sender;
    User receiver;
    String content;
    String dateTime;
    boolean isSent;

    public PM(User sender, User receiver, String content, String dateTime) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.dateTime = dateTime;
        this.isSent = false;
    }

    public String getContent() {
        return content;
    }

    public String getDateTime() {
        return dateTime;
    }

    public User getReceiver() {
        return receiver;
    }

    public User getSender() {
        return sender;
    }
    public boolean isSent() {
        return isSent;
    }

    public void setAsSent() {
        this.isSent = true;
    }

}
