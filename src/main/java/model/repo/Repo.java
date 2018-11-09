package model.repo;

import model.data.ImageSeries;

public interface Repo {
    ImageSeries GetImageSeries(String link);
}
