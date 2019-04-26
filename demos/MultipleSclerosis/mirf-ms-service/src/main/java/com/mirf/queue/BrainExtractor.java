package com.mirf.queue;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class BrainExtractor implements FileProcessor {

    private String command;
    private static final Logger log = LoggerFactory.getLogger("");

    public BrainExtractor(String command) {
        this.command = command;

    }

    @Override
    public Path process(Path file) {

        Path outDir = file.getParent().resolve("out");
        try {
            Files.createDirectory(outDir);
        } catch (IOException e) {
            log.error("failed to create out directory for brain extractor. Ex: " + e);
            throw new RuntimeException(e);
        }

        try {
            log.info("unzipping " + file.toString());
            TerminalRunner.runCommandRedirect("gzip -d " + file.toString(), file.getParent().toFile());
            file = Paths.get(file.toString().replace(".gz", ""));

            log.info("Run brain extractor for " + file.toString());
            TerminalRunner.runCommandRedirect("python " + command + " -i " + file.toString() + " -o " + outDir, outDir.toFile());

            log.info("Brain extraction for " + file.toString() + " finished ");
            Path result = outDir.resolve("brain.nii");

            TerminalRunner.runCommandRedirect("gzip " + result.getFileName(), result.getParent().toFile());
            result = outDir.resolve("brain.nii.gz");

            return result;
        } catch (Exception e) {
            log.error("failed to create out directory for brain extractor. Ex: " + e);
            throw new RuntimeException(e);
        }
    }


    @Override
    public String getLogName() {

        return "Skull Strip";
    }
}
