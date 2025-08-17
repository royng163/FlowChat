package project.flowchat.backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import project.flowchat.backend.Model.UserAccountModel;

import java.util.List;


@Repository
public interface UserAccountRepository extends JpaRepository<UserAccountModel, Integer> {

    @Query(value = "SELECT COUNT(*) FROM User_Account WHERE username = ?1 AND is_active = 1", nativeQuery = true)
    Integer countAllUsersByUsername(String username);

    @Query(value = "SELECT COUNT(*) FROM User_Account WHERE email = ?1 AND is_active = 1", nativeQuery = true)
    Integer countAllUsersByEmail(String email);

    @Query(value = "SELECT role_name FROM Role WHERE role_id = ?1", nativeQuery = true)
    String findRoleById(Integer id);

    @Query(value = "SELECT username FROM User_Account WHERE user_id = ?1", nativeQuery = true)
    String findUsernameByUserId(Integer id);

    @Query(value = "SELECT * FROM User_Account WHERE username = ?1 AND is_active = 1", nativeQuery = true)
    UserAccountModel findUserInfoByUsername(String username);

    @Query(value = "SELECT * FROM User_Account WHERE email = ?1 AND is_active = 1", nativeQuery = true)
    UserAccountModel findUserInfoByEmail(String email);

    @Query(value = "SELECT password_hash FROM User_Account WHERE username = ?1 AND is_active = 1", nativeQuery = true)
    String findHashPasswordByUsername(String username);

    @Query(value = "SELECT password_hash FROM User_Account WHERE email = ?1 AND is_active = 1", nativeQuery = true)
    String findHashPasswordByEmail(String email);

    @Modifying
    @Transactional
    @Query(value = "UPDATE User_Account SET password_hash = ?2 WHERE email = ?1 AND is_active = 1", nativeQuery = true)
    void updatePassword(String email, String passwordHash);

    @Modifying
    @Transactional
    @Query(value = "UPDATE User_Account SET is_active = 0 WHERE username = ?1 AND is_active = 1", nativeQuery = true)
    void deleteAccountByUsername(String username);

    @Modifying
    @Transactional
    @Query(value = "UPDATE User_Account SET is_active = 0 WHERE email = ?1 AND is_active = 1", nativeQuery = true)
    void deleteAccountByEmail(String email);

    @Modifying
    @Transactional
    @Query(value = "UPDATE User_Account SET updated_at = DATE_ADD(UTC_TIMESTAMP(), INTERVAL 8 HOUR) WHERE user_id = ?1", nativeQuery = true)
    void updateUserAccountById(Integer userId);

    @Query(value = "SELECT is_active FROM User_Account WHERE user_id = ?1", nativeQuery = true)
    Boolean findIfUserActive(Integer userId);
}