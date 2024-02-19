package it.be.epicode.EsercizioUno.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String avatarURL;

    public User(String name, String surname, String email, String password, String avatarURL) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.avatarURL = avatarURL;
    }
}
