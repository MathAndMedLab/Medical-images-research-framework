package com.mirf.queue;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.zip.GZIPInputStream;

class FileManager {

    static Path saveMiltipartFiles(MultipartFile file, Path sessionDirectory, String ext) {

        try {
            InputStream stream = file.getInputStream();
            Path pathToFile = sessionDirectory.resolve("input" + ext);
            Files.copy(stream, pathToFile, StandardCopyOption.REPLACE_EXISTING);
            return pathToFile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Path resolvePath(MultipartFile file, Path sessionDirectory) {
        return sessionDirectory.resolve(file.getOriginalFilename().replaceFirst(".gz", ""));
    }

    private static InputStream getInputStream(MultipartFile file) {
        if (file.getOriginalFilename() == null)
            return null;
        try {
            if (file.getOriginalFilename().endsWith(".gz")) {

                return new GZIPInputStream(file.getInputStream());

            }
            return file.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException("Exception: " + e);
        }
    }
}
