package world.moducare.global.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class S3Service {

    private final AmazonS3Client amazonS3Client;

    @Value("${AWS_BUCKET}")
    private String bucket;

    @Value("${AWS_REGION}")
    private String region;

    public String uploadImage(MultipartFile file) {
        try {
            String fileName = UUID.randomUUID().toString() + ".png";
            String fileUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/" + fileName;

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            Thumbnails.of(file.getInputStream())
                    .width(500)
                    .outputFormat("jpeg")
                    .toOutputStream(outputStream);

            byte[] imageData = outputStream.toByteArray();

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/png");
            metadata.setContentLength(imageData.length);

            ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData);
            amazonS3Client.putObject(bucket, fileName, inputStream, metadata);

            return fileUrl;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

