package com.mirf.service;

import com.mirf.queue.*;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component()
public class QueueManager implements DisposableBean {

    private Path rootDir;

    @Autowired
    public QueueManager(FileProcessorProperties properties) {
        rootDir = Paths.get(properties.getRootDir()).toAbsolutePath();

        createBrainExtractor(properties);

        creteLesionQueue(properties);
    }

    private void creteLesionQueue(FileProcessorProperties properties) {

        Path nicMsOutDir  = rootDir.resolve("nicMs_out");

        if(!Files.exists(nicMsOutDir)) try {
            Files.createDirectory(nicMsOutDir);
        } catch (IOException ignored) { }


        String nicMsCommand = rootDir.resolve(properties.getNicMsExecCommand()).toString();
        this.lesionQueue = new ProcessQueue(nicMsOutDir, new NicMsLesion(nicMsCommand));
    }

    private void createBrainExtractor(FileProcessorProperties properties) {

        Path skullDir  = rootDir.resolve("skull");

        if(!Files.exists(skullDir)) try {
            Files.createDirectory(skullDir);
        } catch (IOException ignored) { }

        String brainExtractCommand = rootDir.resolve(properties.getSkullStripingExecCommand()).toString();
        this.brainExtractorQueue = new ProcessQueue(skullDir, new BrainExtractor(brainExtractCommand));
    }

    private ProcessQueue brainExtractorQueue;
    public ProcessQueue getBrainExtractorQueue(){
        return brainExtractorQueue;
    }

    private ProcessQueue lesionQueue;
    public ProcessQueue getLesionQueue(){
        return lesionQueue;
    }

    public void destroy() {
        brainExtractorQueue.close();
    }

    public ProcessRequestStatus getStatus(UUID requestId) {
        FileProcessRequest request;
        request = brainExtractorQueue.findRequest(requestId);
        if(request != null)
            return request.getProcessRequestStatus();

        request = lesionQueue.findRequest(requestId);
        if(request != null)
            return request.getProcessRequestStatus();

        throw new  RuntimeException("Request with id " + requestId + "not found");
    }
}
