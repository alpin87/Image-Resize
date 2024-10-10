package resizing.imageresizing.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import resizing.imageresizing.domain.Image;
import resizing.imageresizing.dto.request.ImageUploadRequest;
import resizing.imageresizing.dto.response.ImageUploadResponse;
import resizing.imageresizing.dto.response.ImageUploadResponse.ImageMetadata;
import resizing.imageresizing.exception.CustomException;
import resizing.imageresizing.exception.ErrorCode;
import resizing.imageresizing.repository.ImageRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.amazonaws.http.apache.utils.ApacheUtils.createResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {
    private final S3Service s3Service;

    private final ImageRepository imageRepository;

    public ImageUploadResponse uploadAndResize(ImageUploadRequest request) {
        MultipartFile file = request.getFile();
        validateImage(file);

        try {
            String originalUrl = s3Service.uploadOriginal(file);
            BufferedImage originalImage = ImageIO.read(file.getInputStream());
            BufferedImage resizedImage = resizeImage(originalImage, request);
            String resizedUrl = s3Service.uploadResized(resizedImage, file.getContentType());

            Image image = Image.builder()
                    .originalUrl(originalUrl)
                    .resizedUrl(resizedUrl)
                    .fileName(file.getOriginalFilename())
                    .contentType(file.getContentType())
                    .originalSize(file.getSize())
                    .resizedSize(estimateResizedSize(resizedImage))
                    .width(resizedImage.getWidth())
                    .height(resizedImage.getHeight())
                    .build();

            Image savedImage = imageRepository.save(image);

            return createResponse(savedImage);
        } catch (Exception e) {
            log.error("이미지 처리 실패", e);
            throw new CustomException(ErrorCode.IMAGE_PROCESSING_ERROR, e.getMessage());
        }
    }
    public ImageUploadResponse getImage(Long id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.IMAGE_NOT_FOUND, "ID로 이미지를 찾을 수 없음: " + id));
        return createResponse(image);
    }

    private ImageUploadResponse createResponse(Image image) {
        return ImageUploadResponse.builder()
                .originalUrl(image.getOriginalUrl())
                .resizedUrl(image.getResizedUrl())
                .metadata(ImageUploadResponse.ImageMetadata.builder()
                        .fileName(image.getFileName())
                        .contentType(image.getContentType())
                        .originalSize(image.getOriginalSize())
                        .resizedSize(image.getResizedSize())
                        .width(image.getWidth())
                        .height(image.getHeight())
                        .build())
                .build();
    }


    private BufferedImage resizeImage(BufferedImage originalImage, ImageUploadRequest request) throws IOException {
        int targetWidth = request.getTargetWidth() != null ? request.getTargetWidth() : 800;
        int targetHeight = request.getTargetHeight() != null ? request.getTargetHeight() : 600;

        return Thumbnails.of(originalImage)
                .size(targetWidth, targetHeight)
                .keepAspectRatio(request.isMaintainAspectRatio())
                .outputQuality(request.getQuality())
                .asBufferedImage();
    }

    private ImageMetadata createMetadata(MultipartFile file, BufferedImage originalImage, BufferedImage resizedImage) {
        return ImageMetadata.builder()
                .fileName(file.getOriginalFilename())
                .contentType(file.getContentType())
                .originalSize(file.getSize())
                .resizedSize(estimateResizedSize(resizedImage))
                .width(resizedImage.getWidth())
                .height(resizedImage.getHeight())
                .build();
    }

    private long estimateResizedSize(BufferedImage resizedImage) {
        return (long) (resizedImage.getWidth() * resizedImage.getHeight() * 3 * 0.7);
    }

    private void validateImage(MultipartFile file) {
        if (file.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_IMAGE_FILE, "파일이 비워져있습니다.");
        }
        if (!file.getContentType().startsWith("image/")) {
            throw new CustomException(ErrorCode.INVALID_IMAGE_FILE,
                    "잘못된 파일 형식 : " + file.getContentType());
        }
    }
}