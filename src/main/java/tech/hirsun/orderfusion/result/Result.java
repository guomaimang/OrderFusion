package tech.hirsun.orderfusion.result;


import lombok.Data;
import lombok.Getter;

@Getter
public class Result<T> {

    private int code;
    private String msg;
    private T data;

    private Result(T data) {
        if (data == null) {
            return;
        }
        this.code = 0;
        this.msg = "success";
        this.data = data;
    }

    // For success result
    public static <T> Result<T> success(T data) {
        return new Result<>(data);
    }

    // For error result
    public static Result error(CodeMessage codeMessage) {
        if (codeMessage == null) {
            return null;
        }

        Result errResult = new Result<>(codeMessage);
        errResult.code = codeMessage.getCode();
        errResult.msg = codeMessage.getMessage();
        errResult.data = null;
        return errResult;
    }




}
