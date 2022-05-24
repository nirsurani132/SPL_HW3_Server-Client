package bgu.spl.net.impl.Messages;

import bgu.spl.net.Utils;
import bgu.spl.net.impl.ServerToClientMessage;

import java.util.Arrays;

public class AckMessage extends ServerToClientMessage {
    private short messageOpCode;
    private String extra;

    public AckMessage(short messageOpCode, String extra) {
        super((short)10);
        this.messageOpCode = messageOpCode;
        this.extra = extra;
    }

    public AckMessage(short messageOpCode) {
        super((short)10);
        this.messageOpCode = messageOpCode;
        this.extra = null;
    }

    public byte[] encode (){
        byte[] ans = Utils.shortToBytes(this.getOpcode());
        int pointer = 2;
        if (pointer >= ans.length)
            ans = Arrays.copyOf(ans, 5);
        byte[] messageOpCodeBytes = Utils.shortToBytes(messageOpCode);
        ans[2] = messageOpCodeBytes[0];
        ans[3] = messageOpCodeBytes[1];
        pointer = 4;

        //Encode extra - only for stat and logstat
        if (extra != null) {
            ans = Arrays.copyOf(ans, 13);
            for (String arg : extra.split(" ")) {
                byte[] currArg = Utils.shortToBytes((short)Integer.parseInt(arg));
                ans[pointer] = currArg[0];
                ans[pointer + 1] = currArg[1];
                pointer += 2;
            }
        }
        ans[pointer] = (byte)';';
        return ans;
    }
}
