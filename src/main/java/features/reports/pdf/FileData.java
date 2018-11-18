package features.reports.pdf;

import core.data.Data;

public class FileData extends Data {

    public final byte[] rawData;
    public final String name;
    public final String extension;


    public FileData(byte[] rawData, String name, String extension) {
        this.rawData = rawData;
        this.name = name;
        this.extension = extension;
    }
}
