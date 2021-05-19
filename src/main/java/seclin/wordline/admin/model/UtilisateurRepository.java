package seclin.wordline.admin.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

    @Repository
    public interface UtilisateurRepository extends CrudRepository<Utilisateur, Long> {

        List<Utilisateur> findByLogin(String login);

        Utilisateur findById(long id);

    }
