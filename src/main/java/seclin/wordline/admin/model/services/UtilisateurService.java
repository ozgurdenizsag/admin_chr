package seclin.wordline.admin.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seclin.wordline.admin.model.repository.UtilisateurRepository;

import java.util.List;

@Service
public class UtilisateurService {
    @Autowired
    private UtilisateurRepository repository;

    @Transactional
    public void updateUtilisateurRoles(long id, List<String> roles){
        System.out.println("Avant d'appeler la vraie MAJ..");
        repository.updateRoles(id,roles);
    }

    @Transactional
    public void updatePasswordUtilisateur(long id, String newPassword){
        repository.updatePassword(id,newPassword);
    }
}
