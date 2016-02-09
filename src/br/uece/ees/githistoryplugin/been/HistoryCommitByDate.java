package br.uece.ees.githistoryplugin.been;

import java.util.Date;

import br.uece.ees.githistoryplugin.helper.DateHelper;


public class HistoryCommitByDate extends RepositoryCommitFile implements Comparable<HistoryCommitByDate>{
	
	public String data;
	public Float percent;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Float getPercent() {
		return percent;
	}

	public void setPercent(Float percent) {
		this.percent = percent;
	}

	@Override
	public String toString() {
		return data + " - " + percent;
	}
	
	@Override
    public int compareTo(HistoryCommitByDate o){
		DateHelper dateHelper = new DateHelper();
		Date d1 = dateHelper.period(this.getData());
		Date d2 = dateHelper.period(o.getData());
		
		return (d1.before(d2) ? 1 : -1);
    }
}
