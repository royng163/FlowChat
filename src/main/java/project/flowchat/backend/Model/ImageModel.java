package project.flowchat.backend.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor

@Data
@Entity
@Table(name = "Image_Data")
public class ImageModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Integer imageId;

    @Column(name = "image_name")
    private String imageName;

    @Lob
    @Column(name = "image_data")
    private byte[] imageData;

    @Column(name = "image_format")
    private String imageFormat;
}