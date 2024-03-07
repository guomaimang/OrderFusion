package tech.hirsun.orderfusion.pojo;
import lombok.*;

import java.util.Date;

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
    private int isFrozen;
    private int isAdmin;
    Date registerTime;

    // Return to Frontend
    public User(String name, String email, String avatarUri, int isAdmin) {
        this.name = name;
        this.email = email;
        this.avatarUri = avatarUri;
        this.isAdmin = isAdmin;
    }
}