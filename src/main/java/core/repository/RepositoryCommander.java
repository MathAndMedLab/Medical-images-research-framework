package core.repository;

/**
 * Provides method for repository interaction. To access your data storage,
 * use one of the implementations or create custom implementation
 */
public interface RepositoryCommander {

    //TODO: (avlomakin) add javadoc
    byte[] getFile(String link) throws RepositoryCommanderException;

    String[] getSeriesFileLinks(String link) throws  RepositoryCommanderException;

    /**
     * Saves file to repository
     * @param file raw file bytes
     * @param link target location
     * @param filename Name + extension of the file
     */
    void saveFile(byte[] file, String link, String filename) throws RepositoryCommanderException;
}

