package br.uece.ees.githistoryplugin.been;

import java.util.Collection;

import org.eclipse.egit.github.core.RepositoryCommit;

public class RepositoryCommitFile {
	
	public Collection<RepositoryCommit> repositoryCommit;
	
	public Collection<RepositoryCommit> getRepositoryCommit() {
		return repositoryCommit;
	}

	public void setRepositoryCommit(Collection<RepositoryCommit> repositoryCommit) {
		this.repositoryCommit = repositoryCommit;
	}
}
