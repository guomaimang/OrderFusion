package tech.hirsun.orderfusion.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Goods {
    Integer id;
    String title;
    String name;
    String description;
    String imageUri;
    Double price;
    Integer stock;
    Integer iSAvailable;
}

