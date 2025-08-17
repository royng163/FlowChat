package project.flowchat.backend.Repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.flowchat.backend.Model.LicenseModel;


@Repository
public interface LicenseRepository extends JpaRepository<LicenseModel, Integer> {

    @Query(value = "SELECT * FROM Authentication WHERE email = ?1 AND key_code = ?2 LIMIT 1", nativeQuery = true)
    LicenseModel getKeyInfo(String email, String key);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Authentication SET is_available = 0 WHERE email = ?1 AND key_code = ?2", nativeQuery = true)
    void setKeyUnavailable(String email, String key);
}