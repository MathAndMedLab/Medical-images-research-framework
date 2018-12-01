package core.repository;

import core.data.medimage.ImageSeries;

/**
 * Provides method for repository interaction. To access your data storage,
 * use one of the implementations or create custom implementation
 */
public interface RepositoryCommander {
    /**
     * Reads all medical images from directory
     *
     * @param link path to the directory
     * @return collected ImageSeries
     */
    ImageSeries getImageSeries(String link);


    /**
     * Saves file to repository
     * @param file raw file bytes
     * @param link target location
     * @param filename Name + extension of the file
     */
    void saveFile(byte[] file, String link, String filename);
}
