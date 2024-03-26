package tech.hirsun.orderfusion.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeckillEventAction {

    private Integer status;
    private Integer orderId;
    private String message;
}
