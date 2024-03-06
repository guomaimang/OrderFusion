package tech.hirsun.orderfusion.pojo;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private String randomSalt;
    private String avatarUri;
    private String isFrozen;
    private String isAdmin;
}


