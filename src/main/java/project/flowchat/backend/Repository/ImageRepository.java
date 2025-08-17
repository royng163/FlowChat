package project.flowchat.backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import project.flowchat.backend.Model.ImageModel;

@Repository
public interface ImageRepository extends JpaRepository<ImageModel, Integer> {

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Image_Data WHERE image_id = ?1", nativeQuery = true)
    void deleteImageById(Integer imageId);
}