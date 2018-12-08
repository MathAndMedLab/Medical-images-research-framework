package features.repository;

import core.repository.RepositoryCommander;
import core.repository.RepositoryCommanderException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Local file system commander, Link = path on a filesystem
 */
public class LocalRepositoryCommander implements RepositoryCommander {

    @Override
    public byte[] getFile(String path) throws RepositoryCommanderException {
        var filePath = Paths.get(path);
        try {
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new RepositoryCommanderException("Failed to read file bytes", e);
        }
    }

    @Override
    public String[] getSeriesFileLinks(String link) {
        return Arrays.stream(new File(link).listFiles()).filter(File::isFile).map(File::getPath).toArray(String[]::new);
    }


    @Override
    public void saveFile(byte[] file, String link, String filename) throws RepositoryCommanderException {
        try {
            var stream = new FileOutputStream(link + "/" + filename);
            stream.write(file);
        } catch (IOException e) {
            throw new RepositoryCommanderException("Failed to write file bytes", e);
        }
    }
}
