package bgu.spl.net;


import java.util.Arrays;
import java.util.HashMap;

public class Utils {
    public static String[] forbiddenWords = {"fuck", "shit", "ass", "zona"};
    public static HashMap<String,Short> opCodeMap = new HashMap<String,Short>(){{
        put("REGISTER",(short)1);
        put("LOGIN",(short)2);
        put("LOGOUT",(short)3);
        put("FOLLOW",(short)4);
        put("POST",(short)5);
        put("PM",(short)6);
        put("LOGSTAT",(short)7);
        put("STAT",(short)8);
        put("NOTIFICATION",(short)9);
        put("ACK",(short)10);
        put("ERROR",(short)11);
        put("BLOCK",(short)12);
    }};

    public static  HashMap<Short,String> OpCodeMapReverse = new HashMap<Short,String>(){{
        put((short)1,"REGISTER");
        put((short)2,"LOGIN");
        put((short)3,"LOGOUT");
        put((short)4,"FOLLOW");
        put((short)5,"POST");
        put((short)6,"PM");
        put((short)7,"LOGSTAT");
        put((short)8,"STAT");
        put((short)9,"NOTIFICATION");
        put((short)10,"ACK");
        put((short)11,"ERROR");
        put((short)12,"BLOCK");
    }};


    public static short getOpCode(String opCode){
        return opCodeMap.get(opCode);
    }

    public static String getStringFromOpCode(short opCode){
        return OpCodeMapReverse.get(opCode);
    }

    public static short bytesToShort(byte[] byteArr) {
        short result = (short) ((byteArr[0] & 0xff) << 8);
        result += (short) (byteArr[1] & 0xff);
        return result;
    }

    public static byte[] shortToBytes(short num) {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte) ((num >> 8) & 0xFF);
        bytesArr[1] = (byte) (num & 0xFF);
        return bytesArr;
    }

    public static String censorship (String message){
        String [] messageWords = message.split(" ");
        for (int i = 0; i < messageWords.length; i++) {
            if (Arrays.asList(forbiddenWords).contains(messageWords[i])){
                messageWords[i] = "<filtered>";
            }
        }
        StringBuilder ans = new StringBuilder(messageWords[0]);
        for (int i = 1; i<messageWords.length; i++)
            ans.append(" ").append(messageWords[i]);
        return ans.toString();
    }
}
