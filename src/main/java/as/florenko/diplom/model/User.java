package as.florenko.diplom.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "user")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(name = "login")
    private String login;
    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;
}
