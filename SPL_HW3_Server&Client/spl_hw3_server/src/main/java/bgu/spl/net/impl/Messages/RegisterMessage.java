package bgu.spl.net.impl.Messages;

import bgu.spl.net.Utils;
import bgu.spl.net.impl.ClientToServerMessage;
import bgu.spl.net.impl.Message;
import bgu.spl.net.impl.ServerToClientMessage;
import bgu.spl.net.impl.db.DB;
import bgu.spl.net.impl.db.Entities.User;

import java.net.SocketAddress;
import java.util.Arrays;
import java.util.LinkedList;

public class RegisterMessage extends ClientToServerMessage {
    public RegisterMessage(byte[] bytes) {
        super(Utils.getOpCode("REGISTER"));
        this.args = this.stringsDividedByZeroes(bytes);
    }

    public RegisterMessage(String args){
        super(Utils.getOpCode("REGISTER"));
        this.args = (LinkedList<String>)Arrays.asList(args.split(" "));
    }

    public RegisterMessage(short opCode, LinkedList<String> args){
        super(opCode,args);
    }

    protected byte[] encode() {
        String messageString = this.toString();
        byte[] ans = new byte[messageString.getBytes().length];
        int pointer = 0;
        byte[] opCodeBytes = Utils.shortToBytes(this.getOpcode());
        for(byte b : opCodeBytes) {
            if (pointer >= ans.length-1) {
                ans = Arrays.copyOf(ans, ans.length * 2);
            }
            ans[pointer] = b;
            pointer++;
        }
        for (String arg : this.getArgs()) {
            byte[] argBytes = arg.getBytes();
            for(byte b : argBytes) {
                if (pointer >= ans.length-1) {
                    ans = Arrays.copyOf(ans, ans.length * 2);
                }
                ans[pointer] = b;
                pointer++;
            }
            ans[pointer] = (byte)0;
            pointer++;
        }
        pointer--; //remove the last 0
        ans[pointer] = (byte)';';
        return Arrays.copyOfRange(ans,0,pointer); //uses utf8 by default
    }

    public String getUserName(){return args.getFirst();}

    public String getPassword(){return args.get(1);}

    public String getBirthDate(){return args.getLast();}

    @Override
    public ServerToClientMessage process(int ConnectionId) {
        DB myDB = DB.getInstance();
        try {
            if(args.size() != 3)
                throw new IllegalArgumentException("Invalid number of arguments");
            if (myDB.isConnectionLoggedIn(ConnectionId))
                throw new IllegalArgumentException("User is already logged in");
            myDB.addUser(new User(getUserName(), getPassword(), getBirthDate()));
            return new AckMessage(getOpcode());
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return new ErrorMessage(this.getOpcode());
        }
    }
}
