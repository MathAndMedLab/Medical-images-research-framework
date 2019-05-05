package com.mirf.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;

public class NicMsLesion implements FileProcessor {

    private String command;
    private static final Logger log = LoggerFactory.getLogger("");

    public NicMsLesion(String command) {
        this.command = command;
    }

    @Override
    public Path process(Path file) {
        Path inDir = file.getParent().resolve("in");

        try {
            Files.createDirectory(inDir);
        } catch (IOException e) {
            log.error("failed to create input directory for brain extractor. Ex: " + e);
            throw new RuntimeException(e);
        }

        inDir = inDir.resolve("modalities");

        try {
            Files.createDirectory(inDir);
        } catch (IOException e) {
            log.error("failed to create input directory for brain extractor. Ex: " + e);
            throw new RuntimeException(e);
        }

        try {
            file = Files.copy(file, inDir.resolve(file.getFileName()), StandardCopyOption.REPLACE_EXISTING);

            log.info("unzipping " + file.toString());
            TerminalRunner.simpleSyncRun("gzip -d \"" + file.toString() + "\"", inDir.toFile());
            String unzipped = file.toString().replace(".gz", "");
            TerminalRunner.simpleSyncRun("tar -xf \"" + unzipped + "\" -C \"" + inDir + "\"", inDir.toFile());
            TerminalRunner.simpleSyncRun("rm \"" + unzipped + "\"", inDir.toFile());


            log.info("Run segmentation " + inDir.toString());

            String processOutput = TerminalRunner.simpleSyncRun("python " + command + " -dir " + inDir.getParent().toString(), inDir.toFile());
            log.info("Segmentation for " + inDir.toString() + " finished, out: " + processOutput);

            return inDir.resolve("_hard_seg.nii.gz");
        } catch (Exception e) {
            log.error("failed to create out directory for brain extractor. Ex: " + e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getLogName() {
        return "NicMsLesion";
    }


    private void simpleSyncRun(String command) throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec(command);
        process.waitFor();
    }

}
