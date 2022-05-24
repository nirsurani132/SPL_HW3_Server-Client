package bgu.spl.net.impl;

import bgu.spl.net.Utils;
import bgu.spl.net.impl.Messages.RegisterMessage;

import java.util.LinkedList;

public abstract class Message {
    protected short opcode;
    protected LinkedList<String> args;

    public Message(short opcode) {
        this.opcode = opcode;
        this.args = new LinkedList<String>();
    }

    public static Message createMessage (String str){
        String[] split = str.split(" ");
        short opcode = Utils.getOpCode(split[0]);
        LinkedList<String> args = new LinkedList<String>();
        for (int i = 1; i < split.length; i++) {
            args.add(split[i]);
        }
        switch (opcode){
            case 1:
                return new RegisterMessage(opcode, args);
        }
        return null;
    }

    public Message (short opcode, LinkedList<String> args){
        this.opcode = opcode;
        this.args = args;
    }

    public Message(String str) {
        String[] split = str.split(" ");
        opcode = Utils.getOpCode(split[0]);
        args = new LinkedList<String>();
        for (int i = 1; i < split.length; i++) {
            args.add(split[i]);
        }
    }

    public short getOpcode() {
        return opcode;
    }

    public LinkedList<String> getArgs() {
        return args;
    }

    public String getArgAt(int index) {
        return args.get(index);
    }

    public void addArg(String str) {
        args.add(str);
    }

    @Override
    public String toString() {
        String ans = Utils.getStringFromOpCode(opcode);
        for (String arg : args) {
            ans += " " + arg;
        }
        return ans + ";";
    }

    protected LinkedList<String> stringsDividedByZeroes(byte[] bytes) {
        LinkedList<String> ans = new LinkedList<>();
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            if (b == (byte)'\0') {
                ans.add(sb.toString());
                sb = new StringBuilder();
            }
            else {
                sb.append((char)b);
            }
        }
        if(sb.length() != 0) {
            ans.add(sb.toString());
        }
        return ans;
    }

    protected String argsToStringDividedBySpace (){
        String ans = "";
        for (String arg : args) {
            ans += arg + " ";
        }
        return ans;
    }

}
