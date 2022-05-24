package bgu.spl.net.api;

import bgu.spl.net.Utils;
import bgu.spl.net.impl.Message;
import bgu.spl.net.impl.Messages.*;
import bgu.spl.net.impl.ServerToClientMessage;

import java.util.Arrays;

public class BGUSocialEncoderDecoder implements MessageEncoderDecoder<Message> {
    private byte[] bytes = new byte[1 << 10]; //start with 1k
    private int len = 0;

    @Override
    public Message decodeNextByte(byte nextByte) {
        //notice that the top 128 ascii characters have the same representation as their utf-8 counterparts
        //this allow us to do the following comparison
        if (nextByte == ';') {
            return popMessage();
        }
        pushByte(nextByte);
        return null; //not a line yet
    }

    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }
        bytes[len++] = nextByte;
    }

    private Message popMessage() {
        String opCode = "";
        Message ans = null;
        short opCodeShort = Utils.bytesToShort(Arrays.copyOfRange(bytes, 0, 2));
        switch (opCodeShort) {
            case 1:
                ans = new RegisterMessage(Arrays.copyOfRange(bytes, 2, len));
                break;
            case 2:
                ans = new LoginMessage(Arrays.copyOfRange(bytes, 2, len));
                break;
            case 3:
                ans = new LogoutMessage((short) 3);
                break;
            case 4:
                ans = new FollowMessage(Arrays.copyOfRange(bytes, 2, len));
                break;
            case 5:
                ans = new PostMessage(Arrays.copyOfRange(bytes, 2, len));
                break;
            case 6:
                ans = new PMMessage(Arrays.copyOfRange(bytes, 2, len));
                break;
            case 7:
                ans = new LogstatMessage((short) 7);
                break;
            case 8:
                ans = new StatMessage(Arrays.copyOfRange(bytes, 2, len));
                break;
            case 12:
                ans = new BlockMessage(Arrays.copyOfRange(bytes, 2, len));
                break;
        }
        len = 0;
        return ans;
    }

    @Override
    public byte[] encode(Message message) {
        if (!(message instanceof ServerToClientMessage))
            throw new IllegalArgumentException("Only ServerToClient Messages are supported");
        byte[] ans = ((ServerToClientMessage) message).encode();
        int pointer = -1;
        for (int i = 0; i < ans.length; i++) {
            if (ans[i] == ';') {
                pointer = i;
            }
        }
        return Arrays.copyOfRange(ans, 0, pointer + 1);
    }
}

