package bgu.spl.net.impl.Messages;

import bgu.spl.net.Utils;
import bgu.spl.net.impl.ServerToClientMessage;

import java.util.Arrays;

public class NotificationMessage extends ServerToClientMessage {
    public enum MessageType {
            PM,
            PUBLIC
    };
    private MessageType type;
    String postingUser;
    String content;

    public NotificationMessage(MessageType type, String postingUser, String content){
        super((short) 9);
        this.type = type;
        this.postingUser = postingUser;
        this.content = content;
    }

    @Override
    public byte[] encode() {
        byte[] ans = Utils.shortToBytes(this.getOpcode());
        int pointer = 2;
        if (pointer >= ans.length)
            ans = Arrays.copyOf(ans, ans.length * 2);
        ans[pointer] = type == MessageType.PM ? (byte) '0' : (byte) '1';
        pointer++;
        String[] argsToCopy = {postingUser, content};
        for (String arg : argsToCopy) {
            for (char c : arg.toCharArray()) {
                if (pointer >= ans.length)
                    ans = Arrays.copyOf(ans, ans.length * 2);
                ans[pointer] = (byte) c;
                pointer++;
            }
            if (pointer >= ans.length)
                ans = Arrays.copyOf(ans, ans.length * 2);
            ans[pointer] = (byte) 0;
            pointer++;
        }
        if (pointer >= ans.length)
            ans = Arrays.copyOf(ans, ans.length + 1);
        ans[pointer] = (byte) ';';
        return ans;
    }
}
