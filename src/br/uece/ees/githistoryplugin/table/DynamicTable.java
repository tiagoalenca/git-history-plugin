package br.uece.ees.githistoryplugin.table;

import java.util.Arrays;
import java.util.Date;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import br.uece.ees.githistoryplugin.helper.DateHelper;
import br.uece.ees.githistoryplugin.helper.FloatHelper;
import br.uece.ees.githistoryplugin.helper.ReflectionHelper;
import br.uece.ees.githistoryplugin.helper.TextHelper;

public class DynamicTable extends Composite {
	
	private TableViewer tableViewer = null;
	private Text text = null;
	
	private DateHelper dateHelper = new DateHelper();
	private ReflectionHelper reflectionHelper = new ReflectionHelper();
	private FloatHelper floatHelper = new FloatHelper();
	private TextHelper textHelper = new TextHelper();
	
	private int sumOfColumnWeight;
	private final int[] columnWeight;
	
	public DynamicTable(final Composite parent, final String[] capitalizedFields) {
		super(parent, SWT.NONE);
		setLayout(new FillLayout(SWT.VERTICAL));
		
		columnWeight = new int[capitalizedFields.length];
		Arrays.fill(columnWeight, 1);
		calculateSumOfColumnWeight();
		
		tableViewer = new TableViewer(this, SWT.BORDER | SWT.FULL_SELECTION | SWT.WRAP | SWT.MULTI | SWT.V_SCROLL | SWT.FILL);
		tableViewer.setContentProvider(new ArrayContentProvider());

		final Table table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		for(int i=0; i<capitalizedFields.length; ++i) {
			final String propertyName = capitalizedFields[i];
			
			TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
			tableViewerColumn.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					Object value = reflectionHelper.getPropertyValue(element, propertyName);
					if(value instanceof Date)
						return dateHelper.format((Date)value);
					else
						if(value instanceof Float)
							return floatHelper.percentFormat((Float)value);
						else
							return value == null? null: value.toString();
				}
			});
			
			new TableColumnSorter(tableViewerColumn, propertyName);
			TableColumn tableColumn = tableViewerColumn.getColumn();
			tableColumn.setText(textHelper.capitalizationSplit(capitalizedFields[i]));
			tableColumn.setResizable(true);
		}
		
		table.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				resizeColumns();
			}
		});
		
		text = new Text(this, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
	}
	
	public void setColumnWeights(int ...newWeights) {
		for(int i=0; i<columnWeight.length; ++i) {
			if(i == newWeights.length) break;
			columnWeight[i] = newWeights[i];
		}
			
		calculateSumOfColumnWeight();
	}
	
	public Table getTable() {
		return tableViewer.getTable();
	}

	public TableViewer getTableViewer() {
		return tableViewer;
	}	
	
	public Text getText() {
		return text;
	}
	
	private void resizeColumns() {
		Table table = getTable();
		Rectangle area = table.getClientArea();
		
		float normalWidth = (area.width/(float)sumOfColumnWeight)/1.5f;
		
		TableColumn[] columns = table.getColumns();
		for(int i=0; i<columns.length; ++i) {
			if(i==3)
				columns[i].setWidth((int)(normalWidth*columnWeight[i])*3);
			else
				columns[i].setWidth((int)(normalWidth*columnWeight[i]));
		}
	}
	
	private void calculateSumOfColumnWeight() {
		int sum=0;
		for(int v: columnWeight) sum+=v;
		sumOfColumnWeight = sum;
	}
}
