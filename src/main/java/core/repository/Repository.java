package core.repository;

import core.data.medimage.ImageSeries;

/**
 * Provides method for repository interaction. To access your data storage,
 * use one of the implementations or create custom implementation
 */
public interface Repository {
    ImageSeries getImageSeries(String link);

    void saveFile(byte[] file, String link, String filename);
}
