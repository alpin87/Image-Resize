package resizing.imageresizing.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import resizing.imageresizing.exception.CustomException;
import resizing.imageresizing.exception.ErrorCode;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadOriginal(MultipartFile file) {
        String key = "origin/" + generateFileName(file);
        return uploadFile(file, key);
    }

    public String uploadResized(BufferedImage image, String contentType) {
        String key = "resized/" + UUID.randomUUID() + getExtension(contentType);
        return uploadBufferedImage(image, key, contentType);
    }

    private String uploadFile(MultipartFile file, String key) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            amazonS3.putObject(bucket, key, file.getInputStream(), metadata);
            return getUrl(key);
        } catch (IOException | AmazonServiceException e) {
            throw new CustomException(ErrorCode.S3_UPLOAD_ERROR,
                    "Failed to upload file to S3: " + e.getMessage());
        }
    }

    private String uploadBufferedImage(BufferedImage image, String key, String contentType) {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(image, getExtension(contentType).substring(1), os);
            byte[] imageBytes = os.toByteArray();

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);
            metadata.setContentLength(imageBytes.length);

            amazonS3.putObject(bucket, key, new ByteArrayInputStream(imageBytes), metadata);
            return getUrl(key);
        } catch (IOException | AmazonServiceException e) {
            throw new CustomException(ErrorCode.S3_UPLOAD_ERROR,
                    "리사이징된 이미지를 S3에 업로드 실패: " + e.getMessage());
        }
    }

    private String getUrl(String key) {
        return amazonS3.getUrl(bucket, key).toString();
    }

    private String generateFileName(MultipartFile file) {
        return UUID.randomUUID() + getExtension(file.getContentType());
    }

    private String getExtension(String contentType) {
        return "." + contentType.split("/")[1];
    }
}