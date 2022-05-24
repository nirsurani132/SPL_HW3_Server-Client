package bgu.spl.net.impl.Messages;

import bgu.spl.net.Utils;
import bgu.spl.net.impl.ServerToClientMessage;

import java.util.Arrays;

public class ErrorMessage extends ServerToClientMessage {
    private short messageOpCode;

    public ErrorMessage(short messageOpCode) {
        super((short)11);
        this.messageOpCode = messageOpCode;
    }

    public byte[] encode (){
        byte[] ans = new byte[5];
        byte[] opCOdeBytes = Utils.shortToBytes(this.getOpcode());
        byte[] messageOpCodeBytes = Utils.shortToBytes(messageOpCode);
        ans[0] = opCOdeBytes[0];
        ans[1] = opCOdeBytes[1];
        ans[2] = messageOpCodeBytes[0];
        ans[3] = messageOpCodeBytes[1];
        ans[4] = (byte)';';
        return ans;
    }
}
