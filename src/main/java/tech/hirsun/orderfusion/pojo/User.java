package tech.hirsun.orderfusion.pojo;
import lombok.*;
import tech.hirsun.orderfusion.utils.SaltUtils;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;
    private String name;
    private String email;
    private String password;
    private String randomSalt;
    private String avatarUri;
    private Integer isFrozen;
    private Integer isAdmin;
    Date registerTime;

    // Return to Frontend
    public User(String name, String email, String avatarUri, int isAdmin) {
        this.name = name;
        this.email = email;
        this.avatarUri = avatarUri;
        this.isAdmin = isAdmin;
    }

    // New User
    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.randomSalt = SaltUtils.getRandomSalt(6);
        this.registerTime = new Date();
    }
}