package br.uece.ees.githistoryplugin.table;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Table;

import br.uece.ees.githistoryplugin.helper.ReflectionHelper;

public class TableColumnSorter extends ViewerComparator {
	public static final int ASC = 1;
	public static final int NONE = 0;
	public static final int DESC = -1;

	private ReflectionHelper reflectionHelper = new ReflectionHelper();
	
	private final TableViewerColumn m_column;
	private final TableViewer m_viewer;
	private final Table m_table;
	private int m_direction = NONE;
	private String propertyName;

	public TableColumnSorter(TableViewerColumn column, String propertyName) {
		this.propertyName = propertyName;
		m_column = column;
		m_viewer = (TableViewer) column.getViewer();
		m_table = m_viewer.getTable();
		m_column.getColumn().addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (m_viewer.getComparator() != null) {
					if (m_viewer.getComparator() == TableColumnSorter.this) {
						if (m_direction == ASC) {
							setSorter(DESC);
						} else if (m_direction == DESC) {
							setSorter(NONE);
						}
					} else {
						setSorter(ASC);
					}
				} else {
					setSorter(ASC);
				}
			}
		});
	}

	public void setSorter(int direction) {
		if (direction == NONE) {
			m_table.setSortColumn(null);
			m_table.setSortDirection(SWT.NONE);
			m_viewer.setComparator(null);
		} else {
			m_table.setSortColumn(m_column.getColumn());
			m_direction = direction;
			if (m_direction == ASC) {
				m_table.setSortDirection(SWT.DOWN);
			} else {
				m_table.setSortDirection(SWT.UP);
			}
			if (m_viewer.getComparator() == this) {
				m_viewer.refresh();
			} else {
				m_viewer.setComparator(this);
			}
		}
	}

	public int compare(Viewer viewer, Object e1, Object e2) {
		return m_direction * doCompare(viewer, e1, e2);
	}

	@SuppressWarnings("unchecked")
	protected int doCompare(Viewer viewer, Object e1, Object e2) {
		Object v1 = reflectionHelper.getPropertyValue(e1, propertyName);
		Object v2 = reflectionHelper.getPropertyValue(e2, propertyName);
		
		if(v1 == null) 
			return 1;
		
		if(v1 instanceof Comparable) {  
			Comparable<Object> c1 = (Comparable<Object>) v1;
			Comparable<Object> c2 = (Comparable<Object>) v2;
			
			if(c2 == null) 
				return -1;
			return c1.compareTo(c2);
		} 
		
		return 0;
	}
}
