package tech.hirsun.orderfusion.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Goods {
    int id;
    String title;
    String name;
    String description;
    String imageUri;
    double price;
    int stock;
    int iSAvailable;
}

