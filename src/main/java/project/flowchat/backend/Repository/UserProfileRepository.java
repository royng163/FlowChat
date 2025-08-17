package project.flowchat.backend.Repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.flowchat.backend.Model.UserProfileModel;

import java.util.List;


@Repository
public interface UserProfileRepository extends JpaRepository<UserProfileModel, Integer> {

    /**
     * Find user avatarId by userID
     * @param userId userId Integer
     * @return avatarId or null
     */
    @Query(value = "SELECT avatar_id FROM User_Profile WHERE user_id = ?1", nativeQuery = true)
    Integer findAvatarIdByUserId(Integer userId);

    /**
     * Check if a user have followed another user before
     * @param userIdFrom userIdFrom Integer
     * @param userIdTo userIdTo Integer
     * @return userIdFrom Integer if a record is found, otherwise null
     */
    @Query(value = "SELECT user_id_from FROM Follow WHERE user_id_from = ?1 AND user_id_to = ?2", nativeQuery = true)
    Integer checkIfUserFollowed(Integer userIdFrom, Integer userIdTo);

    /**
     * Add a record to the Follow table
     * @param userIdFrom userIdFrom Integer
     * @param userIdTo userIdTo Integer
     */
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO Follow (user_id_from, user_id_to) VALUES (?1, ?2)", nativeQuery = true)
    void followUser(Integer userIdFrom, Integer userIdTo);

    /**
     * Delete a record from the Follow table
     * @param userIdFrom userIdFrom Integer
     * @param userIdTo userIdTo Integer
     */
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Follow WHERE user_id_from = ?1 AND user_id_to = ?2", nativeQuery = true)
    void unfollowUser(Integer userIdFrom, Integer userIdTo);

    /**
     * Check if a user have blocked another user before
     * @param userIdFrom userIdFrom Integer
     * @param userIdTo userIdTo Integer
     * @return userIdFrom Integer if a record is found, otherwise null
     */
    @Query(value = "SELECT user_id_from FROM Block WHERE user_id_from = ?1 AND user_id_to = ?2", nativeQuery = true)
    Integer checkIfUserBlocked(Integer userIdFrom, Integer userIdTo);

    /**
     * Add a record to the Block table
     * @param userIdFrom userIdFrom Integer
     * @param userIdTo userIdTo Integer
     */
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO Block (user_id_from, user_id_to) VALUES (?1, ?2)", nativeQuery = true)
    void blockUser(Integer userIdFrom, Integer userIdTo);

    /**
     * Delete a record from the Block table
     * @param userIdFrom userIdFrom Integer
     * @param userIdTo userIdTo Integer
     */
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Block WHERE user_id_from = ?1 AND user_id_to = ?2", nativeQuery = true)
    void unblockUser(Integer userIdFrom, Integer userIdTo);

    /**
     * Find user profile by userId
     * @param userId userId Integer
     * @return UserProfileModel
     */
    @Query(value = "SELECT * FROM User_Profile WHERE user_id = ?1", nativeQuery = true)
    UserProfileModel findProfileByUserId(Integer userId);

    /**
     * Find the number of followings of a user
     * @param userId userId Integer
     * @return number of followings of a user
     */
    @Query(value = "SELECT COUNT(*) FROM Follow WHERE user_id_from = ?1", nativeQuery = true)
    Integer countFollowingByUserId(Integer userId);

    /**
     * Find the number of followers of a user
     * @param userId userId Integer
     * @return number of followers of a user
     */
    @Query(value = "SELECT COUNT(*) FROM Follow WHERE user_id_to = ?1", nativeQuery = true)
    Integer countFollowerByUserId(Integer userId);

    /**
     * Find a list of user profile of the followings of a user, ordered by username
     * @param userId userId Integer
     * @param excludingUserIdList a list of userId that have already retrieved
     * @param userNum required number of user profiles
     * @return a list of UserProfileModel
     */
    @Query(value =   "SELECT UP.user_id, UP.username, UP.description, UP.avatar_id, UP.updated_at\n" +
            "FROM User_Profile UP\n" +
            "JOIN Follow F\n" +
            "ON UP.user_id = F.user_id_to\n" +
            "WHERE user_id_from = ?1\n" +
            "AND user_id NOT IN ?2\n" +
            "ORDER BY username ASC\n" +
            "LIMIT ?3", nativeQuery = true)
    List<UserProfileModel> findFollowingListByUserId(Integer userId, List<Integer> excludingUserIdList, Integer userNum);

    /**
     * Find a list of user profile of the followers of a user, ordered by username
     * @param userId userId Integer
     * @param excludingUserIdList a list of userId that have already retrieved
     * @param userNum required number of user profiles
     * @return a list of UserProfileModel
     */
    @Query(value =   "SELECT UP.user_id, UP.username, UP.description, UP.avatar_id, UP.updated_at\n" +
            "FROM User_Profile UP\n" +
            "JOIN Follow F\n" +
            "ON UP.user_id = F.user_id_from\n" +
            "WHERE user_id_to = ?1\n" +
            "AND user_id NOT IN ?2\n" +
            "ORDER BY username ASC\n" +
            "LIMIT ?3", nativeQuery = true)
    List<UserProfileModel> findFollowerListByUserId(Integer userId, List<Integer> excludingUserIdList, Integer userNum);

    /**
     * Find a list of user profile of the blocking of a user, ordered by username
     * @param userId userId Integer
     * @param excludingUserIdList a list of userId that have already retrieved
     * @param userNum required number of user profiles
     * @return a list of UserProfileModel
     */
    @Query(value =   "SELECT UP.user_id, UP.username, UP.description, UP.avatar_id, UP.updated_at\n" +
            "FROM User_Profile UP\n" +
            "JOIN Block B\n" +
            "ON UP.user_id = B.user_id_to\n" +
            "WHERE user_id_from = ?1\n" +
            "AND user_id NOT IN ?2\n" +
            "ORDER BY username ASC\n" +
            "LIMIT ?3", nativeQuery = true)
    List<UserProfileModel> findBlockingListByUserId(Integer userId, List<Integer> excludingUserIdList, Integer userNum);

    /**
     * Find a list of UserProfileModel of the active users with case-insensitive keywords in usernames or emails, ordered randomly
     * @param keyword keywords case-insensitive String
     * @param excludingUserIdList a list of userId that have already retrieved
     * @param searchNum required number of queries
     * @return a lists of UserProfileModel
     */
    @Query(value =   "SELECT UP.user_id, UP.username, UP.description, UP.avatar_id, UP.updated_at\n" +
            "FROM User_Profile UP\n" +
            "JOIN User_Account UA\n" +
            "ON UP.user_id = UA.user_id\n" +
            "WHERE UA.is_active = 1\n" +
            "AND UA.username LIKE ?1\n" +
            "AND UP.user_id NOT IN ?2\n" +
            "ORDER BY RAND()\n" +
            "LIMIT ?3", nativeQuery = true)
    List<UserProfileModel> findSearchListByKeyword(String keyword, List<Integer> excludingUserIdList, Integer searchNum);
}