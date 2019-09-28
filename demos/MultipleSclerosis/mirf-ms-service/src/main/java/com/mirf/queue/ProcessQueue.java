package com.mirf.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class ProcessQueue implements AutoCloseable {

    private Path rootTempDir;

    private FileProcessor fileProcessor;
    private final String logPrefix ;
    private final Object syncObj = new Object();
    private ConcurrentLinkedQueue<FileProcessRequest> queue = new ConcurrentLinkedQueue<>();
    private List<FileProcessRequest> allRequests = new CopyOnWriteArrayList<>();

    public ProcessQueue(Path rootTempDir, FileProcessor fileProcessor) {

        this.rootTempDir = rootTempDir;
        this.fileProcessor = fileProcessor;
        this.logPrefix = "["  + fileProcessor.getLogName() + " queue] ";

        log.info(logPrefix + "Queue created, rootDir: " + rootTempDir);

        workingThread.start();
    }


    public FileProcessRequest findRequest(UUID sessionId){
        Optional<FileProcessRequest> requestInList = allRequests.stream().filter(x -> x.getId().equals(sessionId)).findFirst();
        return requestInList.orElse(null);
    }

    private  static  final Logger log = LoggerFactory.getLogger("");

    public UUID enqueue(MultipartFile file, String ext) {
        try {
            log.info(logPrefix + "Received new file: " + file.getOriginalFilename());

            UUID id = UUID.randomUUID();

            Path sessionDirectory = rootTempDir.resolve(id.toString());
            log.info("creating dir: " + sessionDirectory.toString());
            Files.createDirectory(sessionDirectory);
            log.info("directory created: " + sessionDirectory.toString());

            Path pathToFile = FileManager.saveMiltipartFiles(file, sessionDirectory, ext);

            FileProcessRequest request = new FileProcessRequest(id, pathToFile, ProcessRequestStatus.Queued);
            queue.add(request);
            allRequests.add(request);
            log.info(logPrefix + "File enqueued, path: " + pathToFile.toString());
            return id;
        } catch (Exception ex) {
            log.info(logPrefix + "Failed to enqueue file: " + file.getOriginalFilename() + " Ex: " + ex);
            throw new RuntimeException("Failed to save file " + file.getOriginalFilename() + " Ex: " + ex);
        }
    }

    public boolean IsFileProcessed(UUID requestId){

        FileProcessRequest request = findRequest(requestId);

        if(request == null){
            log.info(logPrefix + "No request with id: " + requestId.toString() + "found");
            return false;
        }
        return request.getProcessRequestStatus() == ProcessRequestStatus.Processed;
    }

    public Resource findResult(UUID requestId) {
        try {
            FileProcessRequest request = findRequest(requestId);

            if(request == null){
                log.info(logPrefix + "No session with id: " + requestId.toString() + "found");
                return null;
            }

            if(request.getProcessRequestStatus() != ProcessRequestStatus.Processed){
                log.info(logPrefix + "Session with id " + requestId + "is in invalid state: " + request.getProcessRequestStatus() +
                        " Null is returned.");
                return null;
            }

            Resource resource = new UrlResource(request.getProcessedFilePath().toUri());
            if(resource.exists()) {
                //request.setProcessRequestStatus(ProcessRequestStatus.Delivered);
                return resource;
            } else {
                throw new RuntimeException("File not found for session" + requestId);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("File not found for session" + requestId, ex);
        }
    }

    public Resource waitForResult(UUID sessionId){

        Resource resource = null;
        while (resource == null){
            resource = findResult(sessionId);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ignored) { }
        }

        return resource;
    }

    private Thread workingThread = new Thread(() -> {
        try {
            processQueue();
        } catch (InterruptedException e) {
            log.error("Exception during queue processing", e);
        }
    });

    private void processQueue() throws InterruptedException {
        while (true) {
            while (queue.size() == 0)
                Thread.sleep(100);

            FileProcessRequest request = queue.poll();
            log.info(logPrefix + "Polled " + request.getId() + "(" + request.getInitialFilePath() + ") for processing");
            request.setProcessRequestStatus(ProcessRequestStatus.InProcess);
            request.setFileProcessed(fileProcessor.process(request.getInitialFilePath()));
        }
    }

    @Override
    public void close() {

        for (FileProcessRequest request : allRequests) {
            try {
                Files.delete(rootTempDir.resolve(request.getId().toString()));
            } catch (IOException ignored) {}
        }

        workingThread.stop();
    }
}
