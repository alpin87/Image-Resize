package resizing.imageresizing.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageUploadResponse {
    private String originalUrl;
    private String resizedUrl;
    private ImageMetadata metadata;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImageMetadata {
        private String fileName;
        private String contentType;
        private long originalSize;
        private long resizedSize;
        private int width;
        private int height;
    }
}
