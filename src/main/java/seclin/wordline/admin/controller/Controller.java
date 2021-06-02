package seclin.wordline.admin.controller;

import org.springframework.web.bind.annotation.*;
import seclin.wordline.admin.model.entities.Utilisateur;
import seclin.wordline.admin.model.repository.UtilisateurRepository;
import seclin.wordline.admin.model.services.UtilisateurService;
import seclin.wordline.admin.models.AuthenticationRequest;
import seclin.wordline.admin.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class Controller {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private UtilisateurRepository repository;

    @Autowired
    private UtilisateurService utilisateurService;



    @GetMapping("/getUtilisateur")
    public ResponseEntity<List<Utilisateur>> getAllUtilisateur(){
        System.out.println("methode getAllUtilisateur");
        List<Utilisateur> utilisateurList = (ArrayList<Utilisateur>) repository.findAll();
        return ResponseEntity.ok().body(utilisateurList);
    }

    @PostMapping("/populateTable")
    public ResponseEntity<String> populateTable(){
        System.out.println("methode populateTable est suspendu..");
/*
        //Utilisateur Admin avec ADMIN et RUN
        List<String> roles1 = new ArrayList<>();
        roles1.add("ADMIN");
        roles1.add("RUN");
        repository.save(new Utilisateur("Admin","Admin","admin@admin.com", roles1));

        //Utilisateur Chef avec CHEF
        List<String> roles2 = new ArrayList<>();
        roles2.add("CHEF");
        repository.save(new Utilisateur("Chef","0000","chef@chef.com", roles2));
*/
        return ResponseEntity.ok().body("Le peuplement des tables est suspendu..");
    }

    @PostMapping("/setRolesUser")
    public ResponseEntity<String> setRolesUser(){

        Utilisateur utilisateur = repository.findByLogin("Chef");
        utilisateur.getRoles().remove("MANAGER");

        // utilisateurService.updatePasswordUtilisateur(utilisateur.getId(),"0000");

        // utilisateurService.updateUtilisateurRoles(utilisateur.getId(),roles);

        repository.save(utilisateur);

        return ResponseEntity.ok().body("Mise à jour à été lancée.");
    }

    /**
     * Cette méthode est utiliser pour ajouter un nouvel utilisateur et pour le modifier
     * s'il est déjà présent via son id unique
     * @param newUtilisateur un objet utilisateur avec ou sans id suivant si on veut add ou update
     * @return un message indiquant que la modification dans la BDD a été faite.
     */
    @PostMapping("/addUtilisateur")
    public ResponseEntity<String[]> addUtilisateur(@RequestBody Utilisateur newUtilisateur){

        Utilisateur utilisateur = repository.findById(newUtilisateur.getId());
        Utilisateur utilisateurVerifLogin = repository.findByLogin(newUtilisateur.getLogin());
        if (utilisateurVerifLogin != null && utilisateur == null){
            //le login est déjà pris... ET c'est un entregistrement d'un nouvel utilisateur
            return ResponseEntity.ok().body(new String[]{"Le login \"" + newUtilisateur.getLogin()+ "\" que vous avez choisi est déjà pris..."});
        } else {
            if (utilisateur != null){
                utilisateur.setLogin(newUtilisateur.getLogin());
                utilisateur.setPassword(newUtilisateur.getPassword());
                utilisateur.setEmail(newUtilisateur.getEmail());
                utilisateur.setRoles(newUtilisateur.getRoles());
                repository.save(utilisateur);
                return ResponseEntity.ok().body(new String[]{"La mise à jour a été faite.."});
            } else {
                repository.save(new Utilisateur(newUtilisateur.getLogin(),
                        newUtilisateur.getPassword(),
                        newUtilisateur.getEmail(),
                        newUtilisateur.getRoles()));
                return ResponseEntity.ok().body(new String[]{"L'ajout a été fait..."});
            }
        }
    }


    @GetMapping("/getUtilisateurById/{id}")
    public ResponseEntity<Utilisateur> getUtilisateurById(@PathVariable("id") long id){
        Utilisateur utilisateur = repository.findById(id);
        return ResponseEntity.ok().body(utilisateur);
    }

    @GetMapping("/getUtilisateurByLogin/{login}")
    public ResponseEntity<Utilisateur> getUtilisateurByLogin(@PathVariable("login") String login){
        Utilisateur utilisateur = repository.findByLogin(login);
        return ResponseEntity.ok().body(utilisateur);
    }

    @GetMapping("/recupererByLoginAndEmail/{login}/{email}")
    public ResponseEntity<String[]> recupererByLoginAndEmail(@PathVariable("login") String login, @PathVariable("email") String email){
        System.out.println(login + " " + email);
        Utilisateur utilisateur = repository.findByLoginAndEmail(login,email);
        if (utilisateur != null){
            System.out.println(utilisateur);
            return ResponseEntity.ok().body(new String[]{"Un mail avec vos idéntifiants vient de vous être envoyé..."});
        }
        return ResponseEntity.ok().body(new String[]{"Login ou email erroné..."});
    }

    @DeleteMapping("/deleteUtilisateurById/{id}")
    public ResponseEntity<String[]> deleteUtilisateurById(@PathVariable("id") long id){
        repository.deleteById(id);
        return ResponseEntity.ok().body(new String[]{"L'utilisateur dont l'id était le : " + id + " a été supprimé.."});
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String[]> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception{
        //Variabilisation des identifiants
        String login = authenticationRequest.getUsername();
        String password = authenticationRequest.getPassword();
        try {
            Utilisateur utilisateur = repository.findByLoginAndPassword(login,password);
            if ( utilisateur != null) {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login,password));
                final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
                final String jwt = jwtTokenUtil.generateToken(userDetails);
                return ResponseEntity.ok(new String[]{jwt});
            }
            return ResponseEntity.ok(new String[]{""});
            //return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (BadCredentialsException e){
            throw new Exception("Login ou mot de passe errone");
        }
    }

}
