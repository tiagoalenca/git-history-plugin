package br.uece.ees.githistoryplugin.views;


import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import br.uece.ees.githistoryplugin.GitHistoryPluginUtil;
import br.uece.ees.githistoryplugin.been.HistoryCommit;
import br.uece.ees.githistoryplugin.been.HistoryCommitByAuthor;
import br.uece.ees.githistoryplugin.been.HistoryCommitByDate;
import br.uece.ees.githistoryplugin.been.RepositoryAccount;
import br.uece.ees.githistoryplugin.been.RepositoryCommitFile;
import br.uece.ees.githistoryplugin.table.DynamicTable;
import br.uece.ees.githistoryplugin.table.HistoryCommitByAuthorTable;
import br.uece.ees.githistoryplugin.table.HistoryCommitByDateTable;
import br.uece.ees.githistoryplugin.table.HistoryCommitFiles;
import br.uece.ees.githistoryplugin.table.HistoryCommitTable;


public class GitHistoryPluginView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "br.uece.ees.githistoryplugin.views.GitHistoryPluginView";

	private static final String HISTORY_COMMIT = "Histórico de commits";
	private static final String HISTORY_COMMIT_BY_AUTHOR = "Commits por autor";
	private static final String HISTORY_COMMIT_BY_DATE = "Commits por data";
	private static final String QUANTIDADE_COMMITS = "Quantidade de commits";
	private static final String QUANTIDADE_COMMITS_DEFAULT = "Default";
	private static final String QUANTIDADE_COMMITS_TOTAL = "Total";
	
	private Action historyCommitAction;
	private Action historyCommitByAuthorAction;
	private Action historyCommitByDataAction;
	private Action actionQuantidadeCommitsDefault;
	private Action actionQuantidadeCommitsTotal;
	private MenuManager menuQuantidadeCommits = new MenuManager(QUANTIDADE_COMMITS);
	
	private DynamicTable dynamicTable = null;
	private HistoryCommitFiles historyCommitTable = null;
	private HistoryCommitFiles historyCommitByAuthorTable = null;
	private HistoryCommitFiles historyCommitByDateTable = null;
	
	private RepositoryAccount repositoryAccount = null;
	private Composite parent = null;
	private ISelection selectionAnterior = null;
	private int sizeCommit = 30;
	
	
	private ISelectionListener selectionProjectListener = new ISelectionListener() {
		
		public void selectionChanged(IWorkbenchPart sourcepart, ISelection selection) {
			if (!selection.equals( GitHistoryPluginView.this.selectionAnterior )){
				IProject project = getProjectSelected(sourcepart, selection);
				if (project!= null && project.isOpen()) {
					repositoryAccount = GitHistoryPluginUtil.getRepositoryAccount(project);			
				} else {
					repositoryAccount = null;
				}
				showHistoryCommit(sizeCommit);
				enableActionCommits(sizeCommit);
				GitHistoryPluginView.this.selectionAnterior = selection;
			}
		}

		private IProject getProjectSelected(IWorkbenchPart sourcepart, ISelection selection) {
			IProject javaProject = null;    

			if (sourcepart != GitHistoryPluginView.this && selection instanceof IStructuredSelection) {
	            Object element = ((IStructuredSelection)selection).getFirstElement();    
	            if (element instanceof IJavaElement) {    
	            	 IJavaProject jProject= ((IJavaElement)element).getJavaProject();    
	            	 javaProject = jProject.getProject();  
	            } if (element instanceof IResource) {    
	            	javaProject = ((IResource)element).getProject(); 
	            }
			}
			return javaProject;
		}
	};

	
	private ISelectionChangedListener selectionRowListener = new ISelectionChangedListener() {
		
		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			IStructuredSelection selection = (IStructuredSelection)event.getSelection();
			if (selection instanceof IStructuredSelection) {
				Object element = ((IStructuredSelection)selection).getFirstElement();

				String files = "";
				for (String commitFile : historyCommitTable.getCommitFiles((RepositoryCommitFile) element, repositoryAccount)) {
					files = files + commitFile + "\n";
				}
				dynamicTable.getText().setText(files);
			}
		}
	};
			
	
	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		this.parent = parent;
		showHistoryCommit(sizeCommit);
		
		makeActions();
		hookContextMenu();
		contributeToActionBars();
		this.getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(selectionProjectListener);
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				GitHistoryPluginView.this.fillContextMenu(manager);
			}
		});
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());	
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(historyCommitAction);
		manager.add(new Separator());
		manager.add(historyCommitByAuthorAction);
		manager.add(historyCommitByDataAction);
		manager.add(new Separator());
		manager.add(menuQuantidadeCommits);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(historyCommitAction);
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		manager.add(historyCommitByAuthorAction);
		manager.add(historyCommitByDataAction);
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		manager.add(menuQuantidadeCommits);
	}

	private void makeActions() {
		
		historyCommitAction = new Action() {
			public void run() {
				showHistoryCommit(sizeCommit);
			}
		};
		historyCommitAction.setText(HISTORY_COMMIT);
		historyCommitAction.setToolTipText(HISTORY_COMMIT);

		historyCommitAction.setImageDescriptor(GitHistoryPluginActivator.getImageDescriptor("icons/report-paper.png"));
		
		historyCommitByAuthorAction = new Action() {
			public void run() {
				try {
					showPercentCommitByAuthor(sizeCommit);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		historyCommitByAuthorAction.setText(HISTORY_COMMIT_BY_AUTHOR);
		historyCommitByAuthorAction.setToolTipText(HISTORY_COMMIT_BY_AUTHOR);
		historyCommitByAuthorAction.setImageDescriptor(GitHistoryPluginActivator.getImageDescriptor("icons/user-black.png"));

		historyCommitByDataAction = new Action() {
			public void run() {
				try {
					showPercentCommitByData(sizeCommit);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		historyCommitByDataAction.setText(HISTORY_COMMIT_BY_DATE);
		historyCommitByDataAction.setToolTipText(HISTORY_COMMIT_BY_DATE);
		historyCommitByDataAction.setImageDescriptor(GitHistoryPluginActivator.getImageDescriptor("icons/clock.png"));
		
		actionQuantidadeCommitsDefault = new Action() {
			public void run() {
				int valorDefault = 30;
				try {
					reloadCommitPage(valorDefault);
					sizeCommit = valorDefault;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		actionQuantidadeCommitsDefault.setEnabled(false);
		actionQuantidadeCommitsDefault.setText(QUANTIDADE_COMMITS_DEFAULT);
		actionQuantidadeCommitsDefault.setToolTipText(QUANTIDADE_COMMITS_DEFAULT);
		
		menuQuantidadeCommits.add(actionQuantidadeCommitsDefault);
		menuQuantidadeCommits.setImageDescriptor(GitHistoryPluginActivator.getImageDescriptor("icons/det_pane_hide.png"));
		
		actionQuantidadeCommitsTotal = new Action() {
			public void run() {
				int total = 0;
				try {
					reloadCommitPage(total);
					sizeCommit = total;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		actionQuantidadeCommitsTotal.setText(QUANTIDADE_COMMITS_TOTAL);
		actionQuantidadeCommitsTotal.setToolTipText(QUANTIDADE_COMMITS_TOTAL);
		
		menuQuantidadeCommits.add(actionQuantidadeCommitsTotal);
		
	}

	private void showHistoryCommit(int size){
		if (this.dynamicTable != null)
			this.dynamicTable.dispose();
		
		dynamicTable = new DynamicTable(this.parent, new String[]{"Autor", "Revisao", "Data", "Mensagem"});

		if(repositoryAccount != null){
			historyCommitTable = new HistoryCommitTable();
			try {
				dynamicTable.getTableViewer().setInput(historyCommitTable.getHistoryCommits(repositoryAccount, size));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		dynamicTable.getTableViewer().addSelectionChangedListener(selectionRowListener);
		this.parent.layout();
	}

	private void showPercentCommitByAuthor(int size) throws IOException {
		if (this.dynamicTable != null)
			this.dynamicTable.dispose();
		
		dynamicTable = new DynamicTable(this.parent, new String[]{"Autor", "Percent"});

		if(repositoryAccount != null){
			historyCommitByAuthorTable = new HistoryCommitByAuthorTable();
			dynamicTable.getTableViewer().setInput(historyCommitByAuthorTable.getPercentCommitByAuthor(repositoryAccount, size));
		}
		
		dynamicTable.getTableViewer().addSelectionChangedListener(selectionRowListener);
		this.parent.layout();
	}
	
	private void showPercentCommitByData(int size) throws IOException {
		if (this.dynamicTable != null)
			this.dynamicTable.dispose();
		
		dynamicTable = new DynamicTable(this.parent, new String[]{"Data", "Percent"});

		if(repositoryAccount != null){
			historyCommitByDateTable = new HistoryCommitByDateTable();
			dynamicTable.getTableViewer().setInput(historyCommitByDateTable.getPercentCommitByDate(repositoryAccount, size));
		}
		
		dynamicTable.getTableViewer().addSelectionChangedListener(selectionRowListener);
		this.parent.layout();
	}
	
	private void reloadCommitPage(int size) throws IOException {
		ArrayList<?> input = (ArrayList<?>)dynamicTable.getTableViewer().getInput();
		
		if (input != null && !input.isEmpty()) {
		
			if(input.get(0) instanceof HistoryCommit)
				showHistoryCommit(size);
			else if(input.get(0) instanceof HistoryCommitByAuthor)
				showPercentCommitByAuthor(size);
			else if(input.get(0) instanceof HistoryCommitByDate)
				showPercentCommitByData(size);
		}
		
		enableActionCommits(size);
	}
	
	private void enableActionCommits(int size){
		if(actionQuantidadeCommitsDefault != null && actionQuantidadeCommitsTotal != null){
			if(size == 0){
				actionQuantidadeCommitsTotal.setEnabled(false);
				actionQuantidadeCommitsDefault.setEnabled(true);
			}else{
				actionQuantidadeCommitsDefault.setEnabled(false);
				actionQuantidadeCommitsTotal.setEnabled(true);
			}
		}
	}
	
	@Override
	public void setFocus() {
		if (dynamicTable != null) {
			dynamicTable.getTableViewer().getControl().setFocus();
		}
	}

}