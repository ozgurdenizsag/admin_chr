package seclin.wordline.admin.model.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "utilisateur")
public class Utilisateur {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;
    private String login;
    private String password;
    private String email;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;

    public Utilisateur() {
    }

    public Utilisateur(String login, String password, String email, List<String> roles) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.roles = roles;
    }

    @Override
    public String toString() {
        return String.format(
                "Utilisateur[id=%d, login='%s', password='%s', roles='%s']",
                id, login, password, roles);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }


}
