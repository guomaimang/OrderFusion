package tech.hirsun.orderfusion.result;


import lombok.Data;

@Data
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


    public static <T> Result<T> success(T data) {
        return new Result<T>(data);
    }

    public static <T> Result<T> error(T data) {
        return new Result<T>(data);
    }






}
