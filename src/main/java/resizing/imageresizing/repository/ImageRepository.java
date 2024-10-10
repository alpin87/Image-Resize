package resizing.imageresizing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import resizing.imageresizing.domain.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
