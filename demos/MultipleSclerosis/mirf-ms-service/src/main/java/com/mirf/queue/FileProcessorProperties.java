package com.mirf.queue;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file-processor")
public class FileProcessorProperties {

    private String rootDir;

    public String getRootDir() {
        return rootDir;
    }

    public void setRootDir(String rootDir) {
        this.rootDir = rootDir;
    }

    private String skullStripingExecCommand;

    public String getSkullStripingExecCommand() {
        return skullStripingExecCommand;
    }

    public void setSkullStripingExecCommand(String skullStripingExecCommand) {
        this.skullStripingExecCommand = skullStripingExecCommand;
    }

    private String nicMsExecCommand;

    public String getNicMsExecCommand() {
        return nicMsExecCommand;
    }

    public void setNicMsExecCommand(String nicMsExecCommand) {
        this.nicMsExecCommand = nicMsExecCommand;
    }
}
