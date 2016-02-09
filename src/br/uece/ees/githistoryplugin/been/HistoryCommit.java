package br.uece.ees.githistoryplugin.been;

import java.util.Date;


public class HistoryCommit extends RepositoryCommitFile implements Comparable<HistoryCommit>{
	
	public String autor;
	public String revisao;
	public Date data;
	public String mensagem;

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public String getRevisao() {
		return revisao;
	}

	public void setRevisao(String revisao) {
		this.revisao = revisao;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	@Override
	public String toString() {
		return autor + " - " + revisao + " - " + data + " - " + mensagem;
	}
	
	@Override
    public int compareTo(HistoryCommit o){
		return (this.getData().before(o.getData()) ? 1 : -1);
    }
}
