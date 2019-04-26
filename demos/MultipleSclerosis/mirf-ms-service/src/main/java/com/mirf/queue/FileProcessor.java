package com.mirf.queue;

import java.io.File;
import java.nio.file.Path;

public interface FileProcessor {

    Path process(Path file);

    String getLogName();
}
