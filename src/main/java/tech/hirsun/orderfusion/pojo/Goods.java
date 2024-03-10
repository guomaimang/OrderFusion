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
    Integer isAvailable;

    public static Goods getDraftGoodsForDB(Goods goods){
        Goods draftGoods = new Goods();

        if (goods.getId() != null){
            draftGoods.setId(goods.getId());
        }

        if (goods.getTitle() != null){
            if (goods.getTitle().length() > 0){
                draftGoods.setTitle(goods.getTitle());
            }
        }

        if (goods.getName() != null){
            if (goods.getName().length() > 0){
                draftGoods.setName(goods.getName());
            }
        }

        if (goods.getDescription() != null){
            draftGoods.setDescription(goods.getDescription());
        }

        if (goods.getImageUri() != null){
            if (goods.getImageUri().length() > 0){
                draftGoods.setImageUri(goods.getImageUri());
            }
        }

        if (goods.getPrice() != null){
            draftGoods.setPrice(goods.getPrice());
        }

        if (goods.getStock() != null){
            draftGoods.setStock(goods.getStock());
        }

        if (goods.getIsAvailable() != null){
            draftGoods.setIsAvailable(goods.getIsAvailable());
        }

        return draftGoods;
    }
}

