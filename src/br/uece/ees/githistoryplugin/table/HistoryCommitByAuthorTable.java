package br.uece.ees.githistoryplugin.table;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.egit.github.core.RepositoryCommit;

import br.uece.ees.githistoryplugin.been.HistoryCommit;
import br.uece.ees.githistoryplugin.been.HistoryCommitByAuthor;
import br.uece.ees.githistoryplugin.been.HistoryCommitByDate;
import br.uece.ees.githistoryplugin.been.RepositoryAccount;


public class HistoryCommitByAuthorTable extends HistoryCommitFiles{

	List<HistoryCommitByAuthor> listHistoryCommitByAutor = null;
	
	public Collection<HistoryCommitByAuthor> getPercentCommitByAuthor(RepositoryAccount repositoryAccount, int size) throws IOException{
		Map<String, Collection<RepositoryCommit>> map = new HashMap<String, Collection<RepositoryCommit>>();
		
		Collection<RepositoryCommit> commits = super.getCommits(repositoryAccount, size);
		Float totalCommits = Float.valueOf(commits.size());		
		
		for (RepositoryCommit commit : commits) {
			String authorEmail = commit.getCommit().getAuthor().getEmail();
			Collection<RepositoryCommit> com = new ArrayList<>();

			if(!map.containsKey(authorEmail))
			{
				com.add(commit);
				map.put(authorEmail, com);
			} else
			{
				com.addAll(map.get(authorEmail));
				com.add(commit);
				map.replace(authorEmail, com);
			}
		}
		
		listHistoryCommitByAutor = new ArrayList<HistoryCommitByAuthor>();

		for (String chave : map.keySet())  
		{  
			HistoryCommitByAuthor historyCommitByAuthor = new HistoryCommitByAuthor();
			historyCommitByAuthor.autor = chave;
			historyCommitByAuthor.percent = (map.get(chave).size() / totalCommits);
			historyCommitByAuthor.repositoryCommit = map.get(chave);
			
			listHistoryCommitByAutor.add(historyCommitByAuthor);
		}  
		
		Collections.sort(listHistoryCommitByAutor);
		return listHistoryCommitByAutor;
	}

	@Override
	public Collection<HistoryCommit> getHistoryCommits(RepositoryAccount repositoryAccount, int size) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<HistoryCommitByDate> getPercentCommitByDate(RepositoryAccount repositoryAccount, int size) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
