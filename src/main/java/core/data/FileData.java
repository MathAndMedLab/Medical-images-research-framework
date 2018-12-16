package core.data;

import core.data.Data;

/**
 * {@link Data} that represents single file
 */
public class FileData extends Data {

    public final byte[] fileBytes;
    public final String name;
    public final String extension;


    public FileData(byte[] fileBytes, String name, String extension) {
        this.fileBytes = fileBytes;
        this.name = name;
        this.extension = extension;
    }
}
