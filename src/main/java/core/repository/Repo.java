package core.repository;

import core.data.medImage.ImageSeries;

public interface Repo {
    ImageSeries GetImageSeries(String link);
}
