package tech.hirsun.orderfusion.result;

public class CodeMessage {
    private int code;
    private String msg;

    // Common Message
    public static CodeMessage SUCCESS = new CodeMessage(0, "success");
    public static CodeMessage SERVER_ERROR = new CodeMessage(50001, "error");

    // Login Message, like 5001x

    // Goods Message, like 5002x

    // Order Message, like 5003x

    // User Message, like 5004x

    // Seckill Message, like 5005x








}
