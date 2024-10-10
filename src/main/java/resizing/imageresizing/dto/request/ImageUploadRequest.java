package resizing.imageresizing.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImageUploadRequest {

    @NotNull(message = "이미지 파일 필요")
    private MultipartFile file;

    @Min(value = 1, message = "너비가 0보다 커야합니다")
    @Max(value = 5000, message = "너비는 5000보다 작거나 같아야합니다")
    private Integer targetWidth;

    @Min(value = 1, message = "높이가 0보다 커야합니다")
    @Max(value = 5000, message = "높이는 5000보다 작거나 같아야합니다.")
    private Integer targetHeight;

    private boolean maintainAspectRatio = true;

    private double quality = 0.8;
}
