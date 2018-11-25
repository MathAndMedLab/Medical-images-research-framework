package features.repositoryaccessors;

import core.algorithm.Algorithm;
import core.data.Data;
import features.reports.pdf.FileData;
import features.repositoryaccessors.data.RepoRequest;

/**
 * {@link Algorithm} that saves file, presented in {@link FileData} using provided {@link RepoRequest}
 */
public class RepoFileSaver implements Algorithm<RepoRequest, Data> {

    @Override
    public Data execute(RepoRequest input) {

        if(!(input.bundle instanceof FileData))
            throw new RuntimeException("invalid request: FileData parse error");

        FileData data = (FileData)input.bundle;
        input.getRepositoryCommander().saveFile(data.fileBytes, input.getLink(), data.name + data.extension);

        return Data.empty;
    }
}
