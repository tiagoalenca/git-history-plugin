package br.uece.ees.githistoryplugin.table;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.egit.github.core.RepositoryCommit;

import br.uece.ees.githistoryplugin.been.HistoryCommit;
import br.uece.ees.githistoryplugin.been.HistoryCommitByAuthor;
import br.uece.ees.githistoryplugin.been.HistoryCommitByDate;
import br.uece.ees.githistoryplugin.been.RepositoryAccount;

public class HistoryCommitTable extends HistoryCommitFiles{
	
	List<HistoryCommit> historyLogs = null;
	
	public Collection<HistoryCommit> getHistoryCommits(RepositoryAccount repositoryAccount, int size) throws IOException{
		historyLogs = new ArrayList<HistoryCommit>();
		
		for (RepositoryCommit commit : super.getCommits(repositoryAccount, size)) {
			HistoryCommit log = new HistoryCommit();
			log.autor = commit.getCommit().getAuthor().getEmail();
			log.revisao = commit.getSha().substring(0,7);
			log.data = commit.getCommit().getAuthor().getDate();
			log.mensagem = commit.getCommit().getMessage();
			
			Collection<RepositoryCommit> com = new ArrayList<>();
			com.add(commit);
			log.repositoryCommit = com;
			
			historyLogs.add(log);
		}
		
		Collections.sort(historyLogs);
		return historyLogs;
	}

	@Override
	public Collection<HistoryCommitByAuthor> getPercentCommitByAuthor(RepositoryAccount repositoryAccount, int size) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<HistoryCommitByDate> getPercentCommitByDate(RepositoryAccount repositoryAccount, int size) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	
}
