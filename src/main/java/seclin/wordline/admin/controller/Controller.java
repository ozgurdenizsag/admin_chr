package seclin.wordline.admin.controller;

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

    @Autowired //don't forget the setter
    private UtilisateurRepository repository;

    @GetMapping("/getUtilisateur")
    public ResponseEntity<List<Utilisateur>> getAllUtilisateur(){
        /*
        List<String> roles = new ArrayList<>();
        repository.save(new Utilisateur("Ozgur","Oz MDP", roles));
        roles.add("ADMIN");
        repository.save(new Utilisateur("Deniz","Deniz MDP", roles));
        roles.add("RUN");
        repository.save(new Utilisateur("SAG","SAG MDP", roles));
         */
        List<Utilisateur> utilisateurList = (ArrayList<Utilisateur>) repository.findAll();
        return ResponseEntity.ok().body(utilisateurList);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String[]> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception{
        System.out.println("authenticationRequest est : " + authenticationRequest.getUsername() + authenticationRequest.getPassword());
        try {
            System.out.println("avant try");
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
            System.out.println("apres try");
        } catch (BadCredentialsException e){
            throw new Exception("nom pw pas bon");
        }
        System.out.println("en dehors du try catch");
        System.out.println("on load by username");
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        System.out.println("on genere le token");
        final String jwt = jwtTokenUtil.generateToken(userDetails);

        //return ResponseEntity.ok(new AuthenticationResponse(jwt));
        return ResponseEntity.ok(new String[]{jwt});
    }

}
