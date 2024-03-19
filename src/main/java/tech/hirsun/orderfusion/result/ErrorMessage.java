package tech.hirsun.orderfusion.result;

import lombok.Getter;
import lombok.Setter;


public class ErrorMessage {
    @Getter @Setter
    private int code;
    @Getter @Setter
    private String message;



    // Server Message, like 5000x
    public static ErrorMessage SERVER_ERROR = new ErrorMessage(50001, "Server Error");
    public static ErrorMessage RECAPTCHA_ERROR = new ErrorMessage(50002, "Recaptcha Error. Please click it.");
    public static ErrorMessage REQUEST_ILLEGAL = new ErrorMessage(50003, "Request Illegal.");
    public static ErrorMessage REFUSE_SERVICE = new ErrorMessage(50004, "Access Limit Reached.");

    // Login/User Message, like 5001x
    public static ErrorMessage USER_NOT_EXIST = new ErrorMessage(50011, "No match found. Please try again.");
    public static ErrorMessage USER_NOT_LOGIN = new ErrorMessage(50012, "User not Login. Please login first.");

    public static ErrorMessage USER_NO_PERMISSION = new ErrorMessage(50013, "No Permission. Please try again.");

    // Goods Message, like 5002x

    // Order Message, like 5003x

    public static ErrorMessage ORDER_NO_PERMISSION_GENERATION = new ErrorMessage(50031, "No Permission. It may be that there is insufficient stock or the product has been removed.");

    // User Message, like 5004x

    // Seckill Message, like 5005x



    //constructor
    private ErrorMessage() {
    }

    public ErrorMessage(int code, String message) {
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
