package br.uece.ees.githistoryplugin.table;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.egit.github.core.CommitFile;
import org.eclipse.egit.github.core.IRepositoryIdProvider;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CommitService;

import br.uece.ees.githistoryplugin.been.HistoryCommit;
import br.uece.ees.githistoryplugin.been.HistoryCommitByAuthor;
import br.uece.ees.githistoryplugin.been.HistoryCommitByDate;
import br.uece.ees.githistoryplugin.been.RepositoryAccount;
import br.uece.ees.githistoryplugin.been.RepositoryCommitFile;

public abstract class HistoryCommitFiles {
	
	public Set<String> getCommitFiles(RepositoryCommitFile repositoryCommitFile, RepositoryAccount repositoryAccount){
		IRepositoryIdProvider repo = new RepositoryId(repositoryAccount.getOwner(), repositoryAccount.getName());
		
		GitHubClient client = new GitHubClient();
		client.setOAuth2Token(RepositoryAccount.token);
		
		CommitService service = new CommitService(client);
		
		SortedSet<String> sortedSet = new TreeSet<String>(); 
		for (RepositoryCommit commit : repositoryCommitFile.repositoryCommit)
		{
			try {
				RepositoryCommit repositoryCommit = service.getCommit(repo, commit.getSha());
				for (CommitFile file : repositoryCommit.getFiles())
				{
					sortedSet.add(file.getFilename());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return sortedSet;
	}
	
	public Collection<RepositoryCommit> getCommits(RepositoryAccount repositoryAccount, int size) throws IOException{
		IRepositoryIdProvider repo = new RepositoryId(repositoryAccount.getOwner(), repositoryAccount.getName());
		
		GitHubClient client = new GitHubClient();
		client.setOAuth2Token(RepositoryAccount.token);
		
		CommitService service = new CommitService(client);
		
		return size != 0 ? service.pageCommits(repo, size).next() : service.getCommits(repo);
	}
	
	public abstract Collection<HistoryCommit> getHistoryCommits(RepositoryAccount repositoryAccount, int size) throws IOException;
	
	public abstract Collection<HistoryCommitByAuthor> getPercentCommitByAuthor(RepositoryAccount repositoryAccount, int size) throws IOException;
	
	public abstract Collection<HistoryCommitByDate> getPercentCommitByDate(RepositoryAccount repositoryAccount, int size) throws IOException;
}
