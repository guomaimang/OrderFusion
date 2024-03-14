package tech.hirsun.orderfusion.result;

import lombok.Getter;
import lombok.Setter;


public class CodeMessage {
    @Getter @Setter
    private int code;
    @Getter @Setter
    private String message;

    // Common Message
    public static CodeMessage SUCCESS = new CodeMessage(0, "Success");

    // Server Message, like 5000x
    public static CodeMessage SERVER_ERROR = new CodeMessage(50001, "Server Error");
    public static CodeMessage RECAPTCHA_ERROR = new CodeMessage(50002, "Recaptcha Error. Please click it.");
    public static CodeMessage REQUEST_ILLEGAL = new CodeMessage(50003, "Request Illegal.");
    public static CodeMessage REFUSE_SERVICE = new CodeMessage(50004, "Access Limit Reached.");

    // Login/User Message, like 5001x
    public static CodeMessage USER_NOT_EXIST = new CodeMessage(50011, "No match found. Please try again.");
    public static CodeMessage USER_NOT_LOGIN = new CodeMessage(50012, "User not Login. Please login first.");

    public static CodeMessage USER_NO_PERMISSION = new CodeMessage(50013, "No Permission. Please try again.");

    // Goods Message, like 5002x

    // Order Message, like 5003x

    public static CodeMessage ORDER_NO_PERMISSION_GENERATION = new CodeMessage(50031, "No Permission. It may be that there is insufficient stock or the product has been removed.");

    // User Message, like 5004x

    // Seckill Message, like 5005x



    //constructor
    private CodeMessage() {
    }

    public CodeMessage(int code, String message) {
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
