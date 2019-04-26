package com.mirf;

import java.util.UUID;

public class MirfFilePackage {

    MirfFilePackage(UUID id, ResponseFileType fileType, byte[] file) {
        this.file = file;
        this.fileType = fileType;
        this.id = id;
    }

    private byte[] file;
    public byte[] getFile(){
        return file;
    }

    private UUID id;
    public UUID getId(){
        return id;
    }

    private ResponseFileType fileType;
    public ResponseFileType getFileType() {
        return fileType;
    }
}

