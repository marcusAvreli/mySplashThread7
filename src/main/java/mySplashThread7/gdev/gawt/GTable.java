package mySplashThread7.gdev.gawt;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
/*
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
*/
import javax.swing.table.TableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
import mySelectAll3.CheckHeaderRenderer;
import mySelectAll3.common.utils.Auxiliar;
import mySelectAll3.gawt.tableCellRenderer.CheckCellRenderer;
import mySelectAll3.gdev.gawt.tableCellEditor.CheckCellEditor;
import mySelectAll3.gen.GConst;
import mySelectAll3.gfld.GTableColumn;
import mySelectAll3.tableHeaderRenderer.MultiLineHeaderRenderer;
*/
import mySplashThread7.MagTable;
import mySplashThread7.dynagent.common.utils.Auxiliar;
import mySplashThread7.dynagent.common.utils.RowItem;
import mySplashThread7.events.Event;
import mySplashThread7.events.EventBroker;
import mySplashThread7.events.EventConsumer;
import mySplashThread7.events.UpdateFindings;
import mySplashThread7.gdev.gawt.tableCellEditor.CheckCellEditor;
import mySplashThread7.gdev.gawt.tableHeaderRenderer.CheckCellRenderer;
import mySplashThread7.gdev.gawt.tableHeaderRenderer.CheckHeaderRenderer;
import mySplashThread7.gen.GConst;
import mySplashThread7.gfld.GFormTable;
import mySplashThread7.gfld.GTableColumn;
import mySplashThread7.gfld.GTableRow;
//https://github.com/semantic-web-software/dynagent/blob/a0d356169ef34f3d2422e235fed7866e3dda6d8a/Elecom/src/gdev/gawt/GTable.java
public class GTable extends GComponent  implements ActionListener, MouseListener/*,EventConsumer*/ {
	//final Component c=this;
	private static final Logger logger = LoggerFactory.getLogger(GTable.class);
	private static final long serialVersionUID = 1L;
	private MagTable m_objTable;
	protected GTableModelReduction m_objTableModel;	
	boolean m_modoFilter;	
	//private  Vector<Vector<Object>> customApplications;
	private Vector<GTableRow> m_listaFilas;
	private Vector m_listaColumnas;
	private double m_heightRow;
	private  GFormTable m_gFormTable;
	private boolean processingPasteRows=false;
	private FocusListener focusListener;
	private boolean m_modoConsulta;
	private boolean manageFocus;
	boolean hideHeader = false;
private boolean rowsFiltered = false;

	public boolean isRowsFiltered() {
	return rowsFiltered;
}

public void setRowsFiltered(boolean rowsFiltered) {
	this.rowsFiltered = rowsFiltered;
}

	private HashMap<String, Object> m_moa;
	private ArrayList<Integer> m_idProps = new ArrayList<Integer>();

	
	public GTable	(	GFormTable ff/* session ses, */
					
					) {
		super(ff);
		this.m_gFormTable  = ff;
		//this.customApplications = customApplications;
		//this.m_listaColumnas = listaColumnas;
		m_listaFilas = ff.getRowList();
		EventBroker b = EventBroker.getInstance();
		//b.subscribeConsumer(this, Event.TYPE_UPDATE_FINDINGS);
		m_listaColumnas = ff.getColumnList();
		double height = ff.getRowHeight();
		m_heightRow = height;
		m_moa = new HashMap<String, Object>();// moa;

	}

	public GFormTable getGformTable() {
		return m_gFormTable;
	}
	public void setGformTable(GFormTable inPut) {
		 m_gFormTable=inPut;
	}
	public void createComponent()  {
		//logger.info("start_create_component");
		String m_name = "Table name";
		
		//m_objTable = new MagTable(m_objTableModel);
		m_objTable = new MagTable();
		m_objTable.setName(m_name);
		//m_objTableModel = new GTableModelReduction(customApplications,m_listaColumnas); 
		
		focusListener = new FocusListener(){
			public void focusGained(FocusEvent ev){
			
				
				if(!ev.isTemporary() && m_objTable.getRowCount()>0 /*&& !isTopLabel()*/ && m_objTable.getSelectedRowCount()==0){
					//System.err.println("Entra en seleccion");
					int row=0;
					int column=0;
					if(/*ev.getComponent() instanceof JButton || */m_modoFilter || m_modoConsulta || ev.getSource() instanceof JButton/*Para el boton de acciones de una sola fila*/){
						if(getRowCount()>0)//getRowCount descarta las nullRow
							m_objTable.setRowSelectionInterval(row, row);
					}else{
						
					}
					
				}
				
				
				//if(m_objTable.isEditing())
				//	m_objTable.getEditorComponent().requestFocusInWindow();
				
				//if(!thisFinal.isAncestorOf(ev.getOppositeComponent())/* && (old!=null && old.equals(window))*/){
				if(manageFocus){
					if(!m_modoConsulta && !m_modoFilter){
						
					}
				}

				/*if(m_controlListener!=null && !isEditing){
					m_controlListener.startEditionTable(getId());
					isEditing=true;
				}*/
					
			}
			public void focusLost(FocusEvent ev){
				//System.err.println("FocusLost Table "+m_label+" component:"+(ev.getComponent()!=null?ev.getComponent().getClass()+" "+ev.getComponent().hashCode():null)+" opposite:"+(ev.getOppositeComponent()!=null?ev.getOppositeComponent().getClass()+" "+ev.getOppositeComponent().hashCode():null)+" isTemporary:"+ev.isTemporary());
				if(/*!ev.isTemporary() &&*/ ev.getOppositeComponent()!=null && ev.getSource()!=null){
					//System.err.println("FocusLost gain "+ev.getOppositeComponent().getClass()+" lost "+ev.getComponent().getClass());
					//m_objTableModel.focusLost(ev);
					//notifyFocusListener(ev, true);
					
					Window old = SwingUtilities.getWindowAncestor(ev.getOppositeComponent());
				
				}
				
			}
		};
		m_objTable.addFocusListener(focusListener);
		
		m_objTableModel = new GTableModelReduction( m_listaColumnas,m_listaFilas);
		
		m_objTable.setModel(m_objTableModel);		
		m_objTable.init();
		m_objTable.setRowHeight((int) m_heightRow);
		m_objTable.getTableHeader().setPreferredSize(
				new Dimension(m_objTable.getTableHeader().getPreferredSize().width,(int) m_heightRow));
		m_objTableModel.setTable(this);
		buildRenders();
		
		
		// tbm.BuildData(data, false);

		/* int rows = Integer.parseInt(itemView.getAttributeValue("ROWS")); */
		/* int rows=m_ff.getRows(); */
		
		
	//	logger.info("finish_create_component");
	}
	
	
		public void cutTable(int startIndex,int endIndex) {	
			ArrayList<Integer> visibleRows = new ArrayList<Integer>();
			ArrayList<Integer> foundRows = m_objTableModel.getFoundRowMap();
			if(null!=foundRows && !foundRows.isEmpty()) {
				for(int i=startIndex;i<endIndex;i++) {
					visibleRows.add(foundRows.get(i));			
				}
			}else {			
				for(int i=startIndex;i<endIndex;i++) {
					visibleRows.add(i);			
				}
			}
			m_objTableModel.setVisibleRow(visibleRows);			
		}
	
		
		
	void buildRenders() {
		//logger.info("start_build_render");
		TableColumnModel tcm = m_objTable.getColumnModel();

		int col = 0;

		

		for (int pos = 0; pos < tcm.getColumnCount(); pos++) {
			TableColumn tc = tcm.getColumn(pos);
			//System.err.println("Pos "+pos+" hideHeader "+hideHeader+" tfm.dobleHeaderSize "+tfm.dobleHeaderSize+" tfm.m_cuantitativo "+tfm.m_cuantitativo);
							
			
			col = pos;
			col = m_objTableModel.getRealColumn(col);
			
			//MultiLineHeaderRenderer renderer = new MultiLineHeaderRenderer();

			GTableColumn columna = ((GFormTable) m_objFormField).getColumn(col);
			//logger.info("hideHeader:"+hideHeader);
			if (!hideHeader){
				if(m_objTableModel.dobleHeaderSize){
					logger.info("hideHeader_checkpos_1");
				//	tc.setHeaderRenderer(renderer);
				}else if(Auxiliar.equals(m_objTableModel.getColumnSelectionRowTable(),col)){
					//logger.info("hideHeader_checkpos_2");
					tc.setHeaderRenderer(new CheckHeaderRenderer(columna, this));
					tc.setHeaderValue(false);
					
				}
			}
			Integer idProp = columna.getIdProp();
			m_idProps.add(idProp);

			int typeField = columna.getType();
			switch (typeField) {
			case GConst.TM_BOOLEAN:
				if(columna.getId().equals(GConst.ID_COLUMN_TABLE_SELECTION)){
					tc.setCellRenderer(new CheckCellRenderer(columna, this, m_modoFilter));
					tc.setCellEditor(new CheckCellEditor(columna, this, focusListener));
				}
			}
		}
		
		//logger.info("finish_build_render");
		
		
	}	
	
	
	
	
	
	public void setComponentBounds(Rectangle rc) {
	
	}

	public MagTable getTable() {
		return m_objTable;
	}
	
	public void mouseClicked(MouseEvent e) {
		try{
			//System.err.println("MOUSE CLICK");
			//m_menuBotonera.setVisible(false);
		}catch(Exception ex){
			ex.printStackTrace();
			//m_server.logError(SwingUtilities.getWindowAncestor(m_objTable),ex,"Error al intentar ocultar la botonera");
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent e) {
		try{
			//System.err.println("MOUSE EXIT");
			//m_menuBotonera.setVisible(false);
			clearSeleccion();
		}catch(Exception ex){
			ex.printStackTrace();
			//m_server.logError(SwingUtilities.getWindowAncestor(m_objTable),ex,"Error al intentar ocultar la botonera");
		}
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	public GTableModelReduction getModel(){
		return m_objTableModel;
	}

	public void setModel(GTableModelReduction objTableModel){
		this.m_objTableModel=objTableModel;
		m_objTable.setModel(m_objTableModel);		
	}
	
	public boolean isProcessingPasteRows() {
		return processingPasteRows;
	}
	
	

	

	
	//Utilizado cuando no se quiere que cuente la nullRow. Se podria hacer mirando el ultimo registro ya que la nullRow esta al final, pero no es seguro que no funcione bien en algun caso
		public int getRowCount(){
			GTableModelReduction tfm = (GTableModelReduction) m_objTable.getModel();
			int size=0;
			Iterator<RowItem> itr=tfm.getRowData().iterator();
			while(itr.hasNext()){
				RowItem ri=itr.next();
				if(!ri.isNullRow())
					size++;
			}
			return size;
		}

	
		public void selectAll(boolean select) {
			ArrayList visibleRows = m_objTableModel.getVisibleRows();
			if(m_objTableModel.getColumnSelectionRowTable()!=null){//Si existe una columna de seleccion de fila modificamos sus valores
				for(RowItem rowItem:m_objTableModel.getRowData()){
					//if(visibleRows.contains(rowItem.getIndex())){
					//if(!isRowsFiltered()) {
						rowItem.setColumnData(m_objTableModel.getColumnSelectionRowTable(),select);
					//}
					/*}else {
						if(rowItem.isFiltered()) {
							rowItem.setColumnData(m_objTableModel.getColumnSelectionRowTable(),select);
						}
					}
					*/
				}
			}
			
			if(!select){
				m_objTable.clearSelection();
			}else{
				m_objTable.selectAll();
			}
		}

		public boolean isSelectAll(){
			return m_objTable.getSelectedRowCount()==m_objTable.getRowCount();
		}
	
		public void clearSeleccion() {
			m_objTable.clearSelection();
		}
		public ArrayList getDataSelectedRows() {
			int[] rows = m_objTable.getSelectedRows();
			logger.info("MagTable selected rows:"+rows.length);
			if (rows.length == 0)
				return null;
			ArrayList<ArrayList<Object>> result = new ArrayList<ArrayList<Object>>();
			GTableModelReduction tfm = (GTableModelReduction) m_objTable.getModel();
			for (int row = 0; row < rows.length; row++) {
				ArrayList<Object> currentRowData = new ArrayList<Object>();
				for (int col = 0; col < tfm.getVisibleColumnDataCount(); col++) {
					Object val = tfm.getDataValueAt(rows[row], col);
				
					currentRowData.add(val);
				}
				result.add(currentRowData);
			}
			return result;
		}
		public void selectRow(int ido,boolean permanent) {
			GTableModelReduction tfm = (GTableModelReduction) m_objTable.getModel();
			//int visRow = tfm.findRow(ido, true, permanent);
			//m_objTable.changeSelection(visRow, 0, false, false);
		}
/*
		@Override
		public void onEvent(Event e) {
			// TODO Auto-generated method stub
			if(e instanceof UpdateFindings) {
				
				//Vector<Integer> visibleRows = (Vector<Integer>) e.getPayload();
				if(null != e.getPayload()) {
					if(e.getPayload() instanceof List) {
						List visibleIndexes = (List) e.getPayload(); 
						
						for(RowItem rowItem : m_objTableModel.getRowData()){
							int rowItemIndex = rowItem.getIndex();
							if(visibleIndexes.contains(rowItemIndex)) {
								rowItem.setFiltered(true);
							}
							
						}
					
					}
					setRowsFiltered(false);
				}
			}
			
		}*/
}