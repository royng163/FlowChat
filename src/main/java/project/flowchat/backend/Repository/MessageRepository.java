package project.flowchat.backend.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import project.flowchat.backend.Model.MessageModel;

@Repository
public interface MessageRepository extends JpaRepository<MessageModel, Integer> {

    @Query(value = "SELECT image_id FROM Image_Data EXCEPT (SELECT image_id FROM Message_Image UNION SELECT image_id FROM Post_Image)", nativeQuery = true)
    List<Integer> getUnconnectImageList();

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO Message_Image (message_id, image_id) VALUES (?1, ?2)", nativeQuery = true)
    void connectMessageWithImage(Integer messageId, Integer imageId);

    @Query(value = "SELECT image_id FROM Message_Image WHERE message_id = ?1", nativeQuery = true)
    List<Integer> findImageIdByMessageId(Integer messageId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM Message_Image WHERE image_id = ?1", nativeQuery = true)
    void deleteInMessageImage(Integer imageId);

    @Query(value = "WITH Ranked_Message AS (SELECT *, ROW_NUMBER() OVER (PARTITION BY CASE WHEN user_id_from < user_id_to THEN user_id_from ELSE user_id_to END, CASE WHEN user_id_from < user_id_to THEN user_id_to ELSE user_id_from END ORDER BY sent_at DESC) AS message_order FROM Message WHERE is_active = 1 AND (user_id_from = ?1 OR user_id_to = ?1)) SELECT * FROM Ranked_Message WHERE message_order = 1 AND user_id_from NOT IN ?2 AND user_id_to NOT IN ?2 ORDER BY sent_at DESC LIMIT ?3", nativeQuery = true)
    List<MessageModel> findAllContactUsers(Integer userId, List<Integer> excludingUserIdList, Integer userNum);

    @Query(value = "SELECT COUNT(*) FROM Message WHERE is_active = 1 AND read_at IS null AND user_id_from = ?1 AND user_id_to = ?2", nativeQuery = true)
    Integer getUnreadMessageCountByUserPair(Integer userIdFrom, Integer userIdTo);

    @Query(value = "SELECT * FROM Message WHERE ((user_id_from = ?1 AND user_id_to = ?2) OR (user_id_from = ?2 AND user_id_to = ?1)) AND message_id NOT IN ?3 ORDER BY sent_at DESC LIMIT ?4", nativeQuery = true)
    List<MessageModel> findAllMessageByUserPair(Integer userId1, Integer userId2, List<Integer> excludingMessageIdList, Integer messageNum);

    @Query(value = "SELECT COUNT(*) FROM Message WHERE is_active = 1 AND read_at IS null AND user_id_to = ?1", nativeQuery = true)
    Integer getTotalUnreadMessageCount(Integer userId);
}