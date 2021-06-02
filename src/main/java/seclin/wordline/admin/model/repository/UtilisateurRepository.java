package seclin.wordline.admin.model.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import seclin.wordline.admin.model.entities.Utilisateur;

import java.util.List;

    @Repository
    public interface UtilisateurRepository extends CrudRepository<Utilisateur, Long> {
        Utilisateur findByLogin(String login);
        Utilisateur findById(long id);
        Utilisateur findByLoginAndPassword(String login, String password);
        Utilisateur findByLoginAndEmail(String login, String email);
        void deleteById(long id);

        @Modifying
        @Query(value = "update Utilisateur u set u.roles = :roles where u.id = :id")
        void updateRoles (@Param(value = "id") long id, @Param(value = "roles") List<String> roles);

        @Modifying
        @Query(value = "update Utilisateur u set u.password = :newPassword where u.id = :id")
        void updatePassword(@Param(value = "id") long id,@Param(value = "newPassword") String newPassword);
    }
