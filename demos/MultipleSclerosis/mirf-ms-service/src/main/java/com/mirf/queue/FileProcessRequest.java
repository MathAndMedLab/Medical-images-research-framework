package com.mirf.queue;

import java.nio.file.Path;
import java.util.UUID;

public class FileProcessRequest {

    public UUID getId() {
        return id;
    }

    public Path getInitialFilePath() {
        return initialFilePath;
    }

    private Path processedFilePath;

    public ProcessRequestStatus getProcessRequestStatus() {
        return processRequestStatus;
    }

    private UUID id;
    private Path initialFilePath;

    public void setProcessRequestStatus(ProcessRequestStatus processRequestStatus) {
        this.processRequestStatus = processRequestStatus;
    }

    private ProcessRequestStatus processRequestStatus;

    public FileProcessRequest(UUID id, Path initialFilePath, ProcessRequestStatus processRequestStatus) {
        this.id = id;
        this.initialFilePath = initialFilePath;
        this.processRequestStatus = processRequestStatus;
    }

    public void updateStatus(ProcessRequestStatus newStatus){
        processRequestStatus = newStatus;
    }

    @Override
    public boolean equals(Object obj) {

        if(obj instanceof FileProcessRequest)
            return this.id == ((FileProcessRequest) obj).id;

        else return false;
    }

    public Path getProcessedFilePath() {
        return processedFilePath;
    }

    public void setFileProcessed(Path file){
        if(this.processRequestStatus != ProcessRequestStatus.InProcess)
            throw new RuntimeException("Failed to set processed file: invalid session state: " + this.processRequestStatus);

        processRequestStatus = ProcessRequestStatus.Processed;

        processedFilePath = file;
    }
}
