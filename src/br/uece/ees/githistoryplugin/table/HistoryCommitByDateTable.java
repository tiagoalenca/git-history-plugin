package br.uece.ees.githistoryplugin.table;

import java.io.IOException;
import java.text.SimpleDateFormat;
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


public class HistoryCommitByDateTable extends HistoryCommitFiles{
	
	List<HistoryCommitByDate> listHistoryCommitByDate = null;
	
	public Collection<HistoryCommitByDate> getPercentCommitByDate(RepositoryAccount repositoryAccount, int size) throws IOException{
		Map<String, Collection<RepositoryCommit>> map = new HashMap<String, Collection<RepositoryCommit>>();
		SimpleDateFormat format = new SimpleDateFormat("MM/yyyy");
		
		Collection<RepositoryCommit> commits = super.getCommits(repositoryAccount, size);
		Float totalCommits = Float.valueOf(commits.size());		
		
		for (RepositoryCommit commit : commits) {
			String dateCommit = format.format(commit.getCommit().getAuthor().getDate());
			Collection<RepositoryCommit> com = new ArrayList<>();

			if(!map.containsKey(dateCommit))
			{
				com.add(commit);
				map.put(dateCommit, com);
			} else
			{
				com.addAll(map.get(dateCommit));
				com.add(commit);
				map.replace(dateCommit, com);
			}
		}
		
		listHistoryCommitByDate = new ArrayList<HistoryCommitByDate>();

		for (String chave : map.keySet())  
		{  
			HistoryCommitByDate historyCommitByDate = new HistoryCommitByDate();
			historyCommitByDate.data = chave;
			historyCommitByDate.percent = (map.get(chave).size() / totalCommits);
			historyCommitByDate.repositoryCommit = map.get(chave);
			
			listHistoryCommitByDate.add(historyCommitByDate);
		}  
		
		Collections.sort(listHistoryCommitByDate);
		return listHistoryCommitByDate;
	}

	@Override
	public Collection<HistoryCommit> getHistoryCommits(RepositoryAccount repositoryAccount, int size) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<HistoryCommitByAuthor> getPercentCommitByAuthor(RepositoryAccount repositoryAccount, int size) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
