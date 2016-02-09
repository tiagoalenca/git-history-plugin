package br.uece.ees.githistoryplugin.been;

public class HistoryCommitByAuthor extends RepositoryCommitFile implements Comparable<HistoryCommitByAuthor>{
	
	public String autor;
	public Float percent;
	
	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public Float getPercent() {
		return percent;
	}

	public void setPercent(Float percent) {
		this.percent = percent;
	}

	@Override
	public String toString() {
		return autor + " - " + percent;
	}

	@Override
    public int compareTo(HistoryCommitByAuthor o){
		return this.getAutor().compareTo(o.getAutor());
    }
	
}
