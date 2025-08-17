package project.flowchat.backend.Repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.flowchat.backend.Model.PostModel;

import java.util.List;


@Repository
public interface ForumRepository extends JpaRepository<PostModel, Integer> {

    @Query(value = "SELECT tag_id from Tag_Data where tag_name = ?1", nativeQuery = true)
    Integer getTagIdFromTagName(String tagName);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO Post_Tag (post_id, tag_id) VALUES (?1, ?2)", nativeQuery = true)
    void connectPostWithTag(Integer postId, Integer tagId);

    @Query(value = "SELECT * FROM Post WHERE attach_to = ?1 AND is_active = 1 ORDER BY popularity_score DESC", nativeQuery = true)
    List<PostModel> findActivePostCommentByAttachTo(Integer postId);

    @Query(value = "SELECT * FROM Post WHERE post_id = ?1", nativeQuery = true)
    PostModel findPostByPostId(Integer postId);

    @Query(value = "SELECT image_id FROM Post_Image WHERE post_id = ?1", nativeQuery = true)
    List<Integer> findImageIdByPostId(Integer postId);

    @Query(value = "SELECT tag_name FROM Post_Tag PT JOIN Tag_Data TD ON PT.tag_id = TD.tag_id WHERE post_id = ?1", nativeQuery = true)
    List<String> findPostTagNameByPostId(Integer postId);

    @Query(value = "SELECT tag_id FROM Post_Tag WHERE post_id = ?1", nativeQuery = true)
    List<Integer> findTagIdByPostId(Integer postId);

    @Query(value = "SELECT * FROM Post WHERE is_active = 1 AND attach_to = 0 AND post_id NOT IN ?1 ORDER BY updated_at DESC LIMIT ?2", nativeQuery = true)
    List<PostModel> findLatestActivePostByRange(List<Integer> excludingPostIdList, Integer postNum);

    @Query(value = "SELECT * FROM Post WHERE is_active = 1 AND attach_to = 0 AND user_id IN (SELECT user_id_to FROM Follow WHERE user_id_from = ?1) AND post_id NOT IN ?2 ORDER BY popularity_score DESC LIMIT ?3", nativeQuery = true)
    List<PostModel> findFollowingActivePostByRange(Integer userId, List<Integer> excludingPostIdList, Integer postNum);

    @Query(value = "SELECT * FROM Post WHERE is_active = 1 AND attach_to = 0 AND user_id = ?1 AND post_id NOT IN ?2 ORDER BY updated_at DESC LIMIT ?3", nativeQuery = true)
    List<PostModel> findUserActivePostByRange(Integer userId, List<Integer> excludingPostIdList, Integer postNum);

    @Query(value = "SELECT * FROM Post WHERE is_active = 1 AND attach_to != 0 AND user_id = ?1 AND post_id NOT IN ?2 ORDER BY updated_at DESC LIMIT ?3", nativeQuery = true)
    List<PostModel> findUserActiveCommentByRange(Integer userId, List<Integer> excludingCommentIdList, Integer commentNum);

    @Query(value = "SELECT * FROM Post WHERE is_active = 1 AND attach_to = 0 AND post_id NOT IN ?1 ORDER BY popularity_score DESC LIMIT ?2", nativeQuery = true)
    List<PostModel> findPopularActivePostByRange(List<Integer> excludingPostIdList, Integer postNum);

    @Query(value = "SELECT P.* FROM Post P JOIN Post_Tag PT ON P.post_id = PT.post_id WHERE is_active = 1 AND attach_to = 0 AND tag_id = ?1 AND P.post_id NOT IN ?2 ORDER BY updated_at DESC LIMIT ?3", nativeQuery = true)
    List<PostModel> findLatestActivePostByRangeAndTag(Integer tagId, List<Integer> excludingPostIdList, Integer postNum);

    @Query(value = "SELECT P.* FROM Post P JOIN Post_Tag PT ON P.post_id = PT.post_id WHERE is_active = 1 AND attach_to = 0 AND tag_id = ?1 AND P.post_id NOT IN ?2 ORDER BY popularity_score DESC LIMIT ?3", nativeQuery = true)
    List<PostModel> findPopularActivePostByRangeAndTag(Integer tagId, List<Integer> excludingPostIdList, Integer postNum);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO Recommendation (user_id, tag_id, updated_at) VALUES (?1, ?2, DATE_ADD(UTC_TIMESTAMP(), INTERVAL 8 HOUR))", nativeQuery = true)
    void addRecommendationTagIdByUserId(Integer userId, Integer tagId);

    @Query(value = "SELECT tag_id FROM Recommendation WHERE user_id = ?1 ORDER BY score DESC LIMIT 5", nativeQuery = true)
    List<Integer> findRecommendedTagByHighestScore(Integer userId);

    @Query(value = "SELECT * FROM Post WHERE is_active = 1 AND attach_to = 0 AND (title LIKE ?1 OR content LIKE ?1) AND post_id NOT IN ?2 ORDER BY popularity_score DESC LIMIT ?3", nativeQuery = true)
    List<PostModel> findPopularActivePostByRangeAndKeyword(String keyword, List<Integer> excludingPostIdList, Integer searchNum);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Post SET comment_count = comment_count + 1 WHERE post_id = ?1", nativeQuery = true)
    void addCommentCountByOne(Integer postId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO Post_Image (post_id, image_id) VALUES (?1, ?2)", nativeQuery = true)
    void connectPostWithImage(Integer postId, Integer imageId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Post_Tag WHERE post_id = ?1", nativeQuery = true)
    void deleteTagInPost(Integer postId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Post SET comment_count = comment_count - ?2 WHERE post_id = ?1", nativeQuery = true)
    void removeCommentCountByNum(Integer postId, int count);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Post SET comment_count = comment_count + ?2 WHERE post_id = ?1", nativeQuery = true)
    void addCommentCountByNum(Integer postId, int count);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Post_Image WHERE image_id = ?1", nativeQuery = true)
    void deleteInPostImage(Integer imageId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Image_Data WHERE image_id = ?1", nativeQuery = true)
    void deleteInImageData(Integer imageId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Post SET comment_count = comment_count - 1 WHERE post_id = ?1", nativeQuery = true)
    void minusCommentCountByOne(Integer postId);

    @Query(value = "SELECT post_id FROM `Like` WHERE post_id = ?1 AND user_id = ?2", nativeQuery = true)
    Integer isLikeClick(Integer postId, Integer userId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO `Like` VALUES (?1, ?2)", nativeQuery = true)
    void addLikeRelationship(Integer postId, Integer userId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Post SET like_count = like_count + 1 WHERE post_id = ?1", nativeQuery = true)
    void addLikeCount(Integer postId);

    @Query(value = "SELECT post_id FROM Dislike WHERE post_id = ?1 AND user_id = ?2", nativeQuery = true)
    Integer isDislikeClick(Integer postId, Integer userId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO Dislike VALUES (?1, ?2)", nativeQuery = true)
    void addDislikeRelationship(Integer postId, Integer userId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Post SET dislike_count = dislike_count + 1 WHERE post_id = ?1", nativeQuery = true)
    void addDislikeCount(Integer postId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM `Like` WHERE post_id = ?1 AND user_id = ?2", nativeQuery = true)
    void removeLikeRelationship(Integer postId, Integer userId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Post SET like_count = like_count - 1 WHERE post_id = ?1", nativeQuery = true)
    void minusLikeCount(Integer postId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Dislike WHERE post_id = ?1 AND user_id = ?2", nativeQuery = true)
    void removeDislikeRelationship(Integer postId, Integer userId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Post SET dislike_count = dislike_count - 1 WHERE post_id = ?1", nativeQuery = true)
    void minusDislikeCount(Integer postId);

    @Query(value = "SELECT tag_id, tag_name FROM Tag_Data", nativeQuery = true)
    List<List<String>> findAllTag();

    @Query(value = "SELECT is_active FROM Post WHERE post_id = ?1", nativeQuery = true)
    boolean postOrCommentIsActive(Integer postId);

    @Query(value = "SELECT post_id FROM `View` WHERE post_id = ?1 AND user_id = ?2", nativeQuery = true)
    Integer isPostView(Integer postId, Integer userId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO `View` VALUES (?1, ?2)", nativeQuery = true)
    void addViewRelationship(Integer postId, Integer userId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Post SET view_count = view_count + 1 WHERE post_id = ?1", nativeQuery = true)
    void addViewCount(Integer postId);

    @Query(value = "SELECT P.post_id FROM Post P JOIN `View` V ON P.post_id = V.post_id AND V.user_id = ?1 WHERE DATE_ADD(UTC_TIMESTAMP(), INTERVAL 8 HOUR) <= DATE_ADD(updated_at, INTERVAL 1 WEEK)", nativeQuery = true)
    List<Integer> findViewPostListByUserId(Integer userId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Post SET popularity_score = ?2 WHERE post_id = ?1", nativeQuery = true)
    void updatePostPopularity(Integer postId, int val);

    @Query(value = "SELECT user_id, tag_id FROM Recommendation WHERE DATE_ADD(UTC_TIMESTAMP(), INTERVAL 8 HOUR) > DATE_ADD(updated_at, INTERVAL 3 DAY) AND score > 0", nativeQuery = true)
    List<List<Integer>> findInfrequentRecommendation();

    @Query(value = "SELECT score FROM Recommendation WHERE user_id = ?1 AND tag_id = ?2", nativeQuery = true)
    Integer findRecommendationScore(Integer userId, Integer tagId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Recommendation SET updated_at = (DATE_ADD(UTC_TIMESTAMP(), INTERVAL 8 HOUR)) WHERE user_id = ?1 AND tag_id = ?2", nativeQuery = true)
    void updateRecommendationTime(Integer userId, Integer tagId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Recommendation SET score = ?3 WHERE user_id = ?1 AND tag_id = ?2", nativeQuery = true)
    void updateRecommendationScore(Integer userId, Integer tagId, int val);

    @Query(value = "SELECT COUNT(*) FROM Post WHERE user_id = ?1 AND attach_to = 0", nativeQuery = true)
    Integer countPostByUserId(Integer userId);

    @Query(value = "SELECT COUNT(*) FROM Post WHERE user_id = ?1 AND attach_to != 0", nativeQuery = true)
    Integer countCommentByUserId(Integer userId);

    @Query(value = "SELECT SUM(like_count) FROM Post WHERE user_id = ?1 AND attach_to = 0", nativeQuery = true)
    Integer countPostLikeByUserId(Integer userId);

    @Query(value = "SELECT SUM(like_count) FROM Post WHERE user_id = ?1 AND attach_to != 0", nativeQuery = true)
    Integer countCommentLikeByUserId(Integer userId);

    @Query(value = "SELECT SUM(dislike_count) FROM Post WHERE user_id = ?1 AND attach_to = 0", nativeQuery = true)
    Integer countPostDislikeByUserId(Integer userId);

    @Query(value = "SELECT SUM(dislike_count) FROM Post WHERE user_id = ?1 AND attach_to != 0", nativeQuery = true)
    Integer countCommentDislikeByUserId(Integer userId);
}