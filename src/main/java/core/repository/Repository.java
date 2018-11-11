package core.repository;

import core.data.medImage.ImageSeries;

/**
 * Provides method for repository interaction. To access your data storage,
 * use one of the implementations or write your own
 */
public interface Repository {
    ImageSeries getImageSeries(String link);
}
