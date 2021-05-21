package seclin.wordline.admin.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import seclin.wordline.admin.model.Utilisateur;
import seclin.wordline.admin.model.UtilisateurRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UtilisateurRepository repository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        Utilisateur utilisateur = repository.findByLogin(userName);

        List<GrantedAuthority> roles = new ArrayList<>();

        for (String myRole : utilisateur.getRoles()) {
            roles.add(new SimpleGrantedAuthority(myRole));
        }

        return new User(utilisateur.getLogin(), utilisateur.getPassword(), roles);
    }
}
