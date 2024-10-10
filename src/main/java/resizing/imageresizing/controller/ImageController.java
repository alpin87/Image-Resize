package resizing.imageresizing.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import resizing.imageresizing.dto.request.ImageUploadRequest;
import resizing.imageresizing.dto.response.ImageUploadResponse;
import resizing.imageresizing.service.ImageService;

@RestController
@RequestMapping("/api/images")
@Tag(name = "Image", description = "이미지 업로드 및 사이즈 크기 조정")
@RequiredArgsConstructor
@Slf4j
public class ImageController {

    private final ImageService imageService;

    @Operation(
            summary = "이미지 업로드 및 크기 조정",
            description = "이미지 업로드하고 원본 이미지와 리사이징된 이미지 URL을 반환"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이미지 업로드 및 리사이징 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력"),
            @ApiResponse(responseCode = "500", description = "서버 에러")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageUploadResponse> uploadImage(
            @Valid ImageUploadRequest request) {
        log.info("파일에 대한 이미지 업로드 요청 수신: {}", request.getFile().getOriginalFilename());
        ImageUploadResponse response = imageService.uploadAndResize(request);
        return ResponseEntity.ok(response);
    }
}