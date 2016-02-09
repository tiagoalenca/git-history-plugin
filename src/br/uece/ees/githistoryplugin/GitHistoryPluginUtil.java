package br.uece.ees.githistoryplugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.core.resources.IProject;

import br.uece.ees.githistoryplugin.been.RepositoryAccount;

public class GitHistoryPluginUtil {
	
	private static final String GIT = ".git/config";
	private static final String URL = "url";
	
	public static RepositoryAccount getRepositoryAccount(IProject project) {
		RepositoryAccount repositoryAccount = null;
		
		if (project != null){
			BufferedReader br = null;
			
			try {
				String line = null;
				br = new BufferedReader(new FileReader(project.getLocation() + File.separator + GIT));
	 
				while ((line = br.readLine()) != null){
					if(line.contains(URL)){
						// url = https://github.com/tiagoalenca/demo.git
						repositoryAccount = new RepositoryAccount();
						repositoryAccount.setOwner(line.split("/")[3]);
						String projectName = line.split("/")[4];
						repositoryAccount.setName(projectName.split(".git")[0]);
						break;
					}
				}
			} catch (IOException e) {
				System.out.println("Projeto não encontrado");;
			} finally {
				try {
					if (br != null)	br.close();
				} catch (IOException ex) {
					System.out.println("Não foi possível fechar o projeto");
				}
			}
		}
		return repositoryAccount;
	}
}
