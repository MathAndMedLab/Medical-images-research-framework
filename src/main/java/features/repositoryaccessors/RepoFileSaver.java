package features.repositoryaccessors;

import core.algorithm.Algorithm;
import core.data.Data;
import features.reports.pdf.FileData;
import features.repositoryaccessors.data.RepoRequest;

public class RepoFileSaver implements Algorithm<RepoRequest, Data> {

    @Override
    public Data execute(RepoRequest input) {

        if(!(input.bundle instanceof FileData))
            throw new RuntimeException("invalid request: FileData parse error");

        FileData data = (FileData)input.bundle;
        input.getRepository().saveFile(data.rawData, input.getLink(), data.name + data.extension);

        return Data.empty;
    }
}
