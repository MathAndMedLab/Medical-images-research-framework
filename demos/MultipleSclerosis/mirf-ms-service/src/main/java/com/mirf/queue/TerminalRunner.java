package com.mirf.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class TerminalRunner {

    private static boolean isWindows = System.getProperty("os.name")
            .toLowerCase().startsWith("windows");

    private static final Logger log = LoggerFactory.getLogger("");

    public static String simpleSyncRun(String command, File ignored) throws IOException, InterruptedException {
        Process process;
        log.info("execute command : " + command);
        if(!isWindows) {
            runLinux(command);
            return "";
        }else {
            process = Runtime.getRuntime().exec(command);
        }

        process.waitFor();
        return new BufferedReader(new InputStreamReader(process.getInputStream()))
                .lines().collect(Collectors.joining("\n"));
    }

    private static void runLinux(String command){
        Process p;
        try {
            p = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", command});
            p.waitFor();
            log.info("exit value for command " + command + ": " + p.exitValue());
            p.destroy();
        } catch (Exception e) {}
    }

    public static String runCommandRedirect(String command, File directory) throws IOException, InterruptedException {
        if(!isWindows)
            command = "/bin/bash -c '" + command + "'";
        log.info("execute command : " + command);

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
