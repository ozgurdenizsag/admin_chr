package seclin.wordline.admin.controller;

import org.springframework.http.HttpStatus;
import seclin.wordline.admin.model.Utilisateur;
import seclin.wordline.admin.model.UtilisateurRepository;
import seclin.wordline.admin.models.AuthenticationRequest;
import seclin.wordline.admin.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

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
        repository.save(new Utilisateur("Admin","Admin", roles1));

        //Utilisateur Chef avec CHEF
        List<String> roles2 = new ArrayList<>();
        roles2.add("CHEF");
        repository.save(new Utilisateur("Chef","Chef", roles2));
 */

        return ResponseEntity.ok().body("Le peuplement des tables est suspendu..");
    }


    @PostMapping("/authenticate")
    public ResponseEntity<String[]> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception{
        //Idéntification des identifiants
        System.out.println("methode createAuthenticationToken");
        String login = authenticationRequest.getUsername();
        String password = authenticationRequest.getPassword();
        try {
            System.out.println("dans try");
            Utilisateur utilisateur = repository.findByLoginAndPassword(login,password);
            System.out.println("apres le get depuis le repo");
            if ( utilisateur != null) {
                System.out.println("dans if");
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
