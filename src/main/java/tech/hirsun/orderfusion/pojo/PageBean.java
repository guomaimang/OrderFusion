package tech.hirsun.orderfusion.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageBean {
    private Integer total;
    private List rows;
    private Integer totalPage;
    private Integer currentPage;
}
