package resizing.imageresizing.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INVALID_INPUT_VALUE(400, "E001", "잘못된 입력 값"),
    INVALID_IMAGE_FILE(400, "E002", "잘못된 이미지 파일"),
    IMAGE_PROCESSING_ERROR(500, "E003", "이미지 처리 에러"),
    S3_UPLOAD_ERROR(500, "E004", "S3 업로드 에러"),
    INTERNAL_SERVER_ERROR(500, "E999", "서버 에러"),
    IMAGE_NOT_FOUND(404, "E005", "이미지를 찾을 수 없음");

    private final int status;
    private final String code;
    private final String message;
}
