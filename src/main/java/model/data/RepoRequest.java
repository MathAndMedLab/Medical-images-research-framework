package model.data;

import model.repo.Repo;

public class RepoRequest extends Data {

    private Repo repo;
    private String link;

    public RepoRequest(Repo repo, String link) {
        this.repo = repo;
        this.link = link;
    }

    public Repo getRepo() {
        return repo;
    }

    public String getLink() {
        return link;
    }

}
