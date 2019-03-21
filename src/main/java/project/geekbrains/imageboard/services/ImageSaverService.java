package project.geekbrains.imageboard.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageSaverService {
    private static final String UPLOADED_FOLDER = "./saved_images/";

    public String saveFile(MultipartFile file) {
        if (file.isEmpty() || !validateImage(file)) {
            return null;
        }
        String filename = UUID.randomUUID().toString() + file.getOriginalFilename();
        try {
            Path path = Paths.get(UPLOADED_FOLDER + filename);
            // System.out.println(path.toAbsolutePath().toString());
            // file.transferTo(path.toAbsolutePath().toFile());
            file.transferTo(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filename;
    }

    private boolean validateImage(MultipartFile file) {
        return file.getContentType().startsWith("image");
    }
}
