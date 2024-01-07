package tech.hirsun.orderfusion.result;

import lombok.Getter;
import lombok.Setter;


public class CodeMessage {
    @Getter @Setter
    private int code;
    @Getter @Setter
    private String message;

    // Common Message
    public static CodeMessage SUCCESS = new CodeMessage(0, "success");
    public static CodeMessage SERVER_ERROR = new CodeMessage(50001, "Server Error");

    // Login Message, like 5001x

    // Goods Message, like 5002x

    // Order Message, like 5003x

    // User Message, like 5004x

    // Seckill Message, like 5005x

    //constructor
    private CodeMessage() {
    }

    private CodeMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String toString(){
        return "CodeMessage{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }























}
