package devracom.Mnemosyne.services;

import devracom.Mnemosyne.controllers.FileController;
import devracom.Mnemosyne.models.Account;
import devracom.Mnemosyne.models.FileInfo;
import devracom.Mnemosyne.models.dto.FilesResponse;
import devracom.Mnemosyne.repositories.AccountRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileService {
    private final AccountRepository accountRepository;
    private final String root = "uploads";

    public FileService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;

        try {
            Files.createDirectories(Path.of(root));
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    public void save(MultipartFile file) {
        try {
            String basePath = root + "/" + getAccountHash();
            Path userePath = Files.createDirectories(Path.of(basePath));

            Files.copy(file.getInputStream(), userePath.resolve(Objects.requireNonNull(file.getOriginalFilename())));
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("A file of that name already exists.");
            }

            throw new RuntimeException(e.getMessage());
        }
    }

    public Resource load(String filename) {
        try {
            String basePath = root + "/" + getAccountHash();
            Path file = Path.of(basePath).resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public FilesResponse loadAll(String targetPath) {
        Path basePath = Path.of(root + "/" + getAccountHash() + targetPath);

        try {
            Stream<Path> filesPaths = Files.walk(basePath, 1)
                    .filter(path -> !path.equals(basePath));
                    //.map(basePath::relativize);

            Long folderSize = Files.walk(basePath).mapToLong(p -> p.toFile().length()).sum();

            List<FileInfo> files = filesPaths.map(path -> {
                String filename = path.getFileName().toString();
                String ext = com.google.common.io.Files.getFileExtension(filename);
                Long fileSize = path.toFile().length();
                String url = MvcUriComponentsBuilder.fromMethodName(
                                FileController.class,
                                "getFile",
                                path.getFileName().toString()
                        )
                        .build()
                        .toString();

                return new FileInfo(filename, ext, fileSize, url);
            }).collect(Collectors.toList());

            return new FilesResponse(files, folderSize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }

    public void delete() {

    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(Path.of(root + "/" + getAccountHash()).toFile());
    }

    private Long getAccountHash() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        Account userAccount = accountRepository.findByUsername(userDetails.getUsername()).orElseThrow();

        return userAccount.getId();
    }
}
