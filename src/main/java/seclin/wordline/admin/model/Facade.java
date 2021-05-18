package seclin.wordline.admin.model;

import java.util.ArrayList;
import java.util.List;

public class Facade {
    public List<Utilisateur> utilisateurList = new ArrayList<>();
    public List<String> roles = new ArrayList<>();

    public List<Utilisateur> getUtilisateurList() {
        return utilisateurList;
    }

    public void setUtilisateurList(List<Utilisateur> utilisateurList) {
        this.utilisateurList = utilisateurList;
    }

    public Facade() {
    }

    {
        roles.add("USER");
        utilisateurList.add(new Utilisateur(1,"Login1", "Pwd1", new ArrayList<>()));
        utilisateurList.add(new Utilisateur(2,"Login2", "Pwd2", roles));
        roles.add("ADMIN");
        utilisateurList.add(new Utilisateur(3,"Login3", "Pwd3", roles));
        roles.add("ROLE_3");
        utilisateurList.add(new Utilisateur(4,"Login4", "Pwd4", roles));
    }
    

}
