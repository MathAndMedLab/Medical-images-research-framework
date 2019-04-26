package com.mirf.queue;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class TerminalRunner {


    public static void simpleSyncRun(String command) throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec(command);
        process.waitFor();
    }

    public static String runCommandRedirect(String command, File directory) throws IOException, InterruptedException {
        Process process = new ProcessBuilder(command.split(" "))
                .directory(directory)
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .start();
        process.waitFor(30, TimeUnit.MINUTES);

        return new BufferedReader(new InputStreamReader(process.getInputStream()))
                .lines().collect(Collectors.joining("\n"));
    }
}
