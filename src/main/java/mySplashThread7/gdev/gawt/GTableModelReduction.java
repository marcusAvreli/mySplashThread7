package mySplashThread7.gdev.gawt;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mySplashThread7.dynagent.common.Constants;
import mySplashThread7.dynagent.common.utils.Auxiliar;
import mySplashThread7.dynagent.common.utils.GIdRow;
import mySplashThread7.dynagent.common.utils.RowItem;
import mySplashThread7.events.Event;
import mySplashThread7.events.EventBroker;
import mySplashThread7.events.EventConsumer;
import mySplashThread7.events.UpdateFindings;
import mySplashThread7.gen.GConst;
import mySplashThread7.gfld.GFormTable;
import mySplashThread7.gfld.GTableColumn;
import mySplashThread7.gfld.GTableRow;
import mySplashThread7.model.base.entity.CustomApplication;
import mySplashThread7.muntjak.tinylookandfeel.table.SortableTableData;

//https://github.com/semantic-web-software/dynagent/blob/a0d356169ef34f3d2422e235fed7866e3dda6d8a/Elecom/src/gdev/gawt/GTableModel.java
public class GTableModelReduction extends DefaultTableModel implements SortableTableData/*,EventConsumer*/ {
	boolean dobleHeaderSize = false;
	private static final Logger logger = LoggerFactory.getLogger(GTableModelReduction.class);
	private Vector<Vector<Object>> rowDatas;
	private Vector<GTableRow> listaFilas;
	ArrayList<String> columnNames = new ArrayList<String>();
	ArrayList<String> Map_Column_IDForm = new ArrayList<String>();
	List<RowItem> m_rowData = new ArrayList<RowItem>();
	ArrayList<Integer> m_visibleRowMap = new ArrayList<Integer>();
	ArrayList<Integer> columnSintax = new ArrayList<Integer>();
	ArrayList<Integer> m_visibleDataColMap = new ArrayList<Integer>();
	ArrayList<Integer> m_visibleColMap = new ArrayList<Integer>();
	HashSet<Integer> columnsCreation = new HashSet<Integer>();
	HashMap<Integer, Integer> Map_columnsIdProp = new HashMap<Integer, Integer>();
	private boolean m_modoFilter;
	private boolean init;
	private ArrayList<Integer> m_foundRowMap = new ArrayList<Integer>();
	public ArrayList<Integer> getFoundRowMap() {
		return m_foundRowMap;
	}

	public void setFoundRowMap(ArrayList<Integer> m_foundRowMap) {
		this.m_foundRowMap = m_foundRowMap;
	}

	HashSet<Integer> columnsFinder = new HashSet<Integer>();
	HashSet<Integer> columnsFinderIfCreation = new HashSet<Integer>();
	/* JTable parent; */
	GTable parent;
	private boolean m_modoConsulta;

	public HashMap Map_XMLDom_ListOption = new HashMap();

	ArrayList<Boolean> columnEditable = new ArrayList<Boolean>();// Columnas que, aunque a lo mejor se tiene permiso de
																	// edicion, no se le deja al usuario modificarlo en
																	// la tabla

	HashSet<Integer> columnsEnable = new HashSet<Integer>();// Columnas sin permisos de edicion para el usuario

	private boolean m_creationRow;
	boolean m_filteredState = false;

	private boolean m_finderRow;
	private Integer columnSelectionRowTable;// Nos sirve para saber si hay alguna columna de seleccion de fila y cual es

	private boolean lastSetValueSuccess = true;// Nos indica si el ultimo setValueAt ha tenido exito o ha provocado
												// alguna excepcion

	private boolean executingSetValue = false;// Nos indica si se esta ejecutando el setValueAt para que no ejecutar
												// cada valor de la copia masiva en GTable hasta que setValueAt ha
												// terminado con el valor anterior

	public boolean m_cuantitativo = false;

	int m_atGroupTypeColumn = -1;

	int m_iniVirtualColumn;

	// estos dos valores no contabilizan la col de toggleBoton pra desplegar
	ArrayList<Integer> m_groupByColumns = new ArrayList<Integer>();

	ArrayList<String> columnRef = new ArrayList<String>();

	ArrayList<Integer> columnIdProps = new ArrayList<Integer>();

	private Vector listaColumnas;
	HashMap<String, Integer> Map_IDForm_Column = new HashMap<String, Integer>();

	public GTableModelReduction(Vector listaColumnas, Vector<GTableRow> listaFilas) {
		// super(customApplications,columnNames);
		// this.rowDatas = customApplications;
		//EventBroker b = EventBroker.getInstance();
		//b.subscribeConsumer(this, Event.TYPE_UPDATE_FINDINGS);
		this.listaFilas = listaFilas;
		this.listaColumnas = listaColumnas;
		// new Vector<GTableColumn>();
	

		init = true;
		buildTabla(listaColumnas);
		init = false;
		// directEdition=false;
	}

	@Override
	public String getColumnName(int col) {
		return (String) columnNames.get(col);
	}

	public int getFieldColumn(String field) {
		Integer col = (Integer) Map_IDForm_Column.get(field);
		if (col == null)
			return -1;
		return col.intValue();
	}

	public String getFieldIDFromColumn(int column) {
		return (String) Map_Column_IDForm.get(column);
	}

	public ArrayList<String> getIdColumns() {
		return Map_Column_IDForm;
	}

	public int getRealDataColumn(int visCol) {
		Integer col = (Integer) m_visibleDataColMap.get(visCol);
		//logger.info("getRealDataColumn:" + col.intValue());
		if (col == null)
			return -1;
		return col.intValue();
	}

	public int getRealColumn(int visCol) {
		Integer col = (Integer) m_visibleColMap.get(visCol);
		if (col == null)
			return -1;
		return col.intValue();
	}

	public int getColumnDataCount() {
		return columnSintax.size();
	}

	public int getVisibleColumnDataCount() {
		return m_visibleDataColMap.size();
	}

	public void setTable(GTable table) {
		parent = table;

		// Si tiene columna de selección de fila nos registramos en el listener para que
		// al seleccionar alguna otra fila pulsando sobre ella
		// seleccionar las filas que tienen marcado el checkbox. De esta manera evitamos
		// la deseleccion que se produce cuando se hace click en otra fila sin pulsa
		// control
		if (columnSelectionRowTable != null) {
			parent.getTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				// Gestiona cuando hay un cambio de fila en la tabla
				public void valueChanged(ListSelectionEvent e) {
					// System.err.println("ListSelectionEvent GTableModel siendo
					// listS.getMinSelectionIndex():"+((ListSelectionModel)e.getSource()).getMinSelectionIndex()+"
					// idoRowEditing"+gTable.getModel().getIdoRowEditing());
					ListSelectionModel listS = (ListSelectionModel) e.getSource();
					int num = 0;

				}
			});
		}

	}

	private void buildTabla(Vector listaColumnas) {
		//logger.info("start_build_table");
		/* Iterator iCol= dataModel.iterator(); */
		Iterator iCol = listaColumnas.iterator();
		int size = 0;

		if (m_cuantitativo) {
			columnNames.add(" ");
			// columnClass.add(Class.forName("javax.swing.JToggleButton"));
			/* columnEditable.add(new Boolean("true")); */
			columnEditable.add(new Boolean(!m_modoConsulta && !m_modoFilter));
		}
		int col = 0;
		while (iCol.hasNext()) {
			GTableColumn item = (GTableColumn) iCol.next();
			/* org.jdom.Element item= (org.jdom.Element)iCol.next(); */

			String label = item.getLabel();
			/*
			 * String label=item.getAttributeValue("LABEL");
			 * System.out.println("COL "+label+","+m_cuantitativo);
			 */
			int posToken = label.indexOf(":");
			if (posToken != -1) {
				label = "<HTML><TABLE cellpadding=0 vspace=0 cellspacing=0><TR><TC>" + label.substring(0, posToken)
						+ "</TC></TR>" + "<TR><TC>" + label.substring(posToken + 1) + "</TC></TR>" + "</TABLE></HTML>";
				dobleHeaderSize = true;
			}
			boolean visible = true;
			/*
			 * boolean visible= !(item.getAttributeValue("HIDE")!=null &&
			 * item.getAttributeValue("HIDE").equals("TRUE"));
			 */

			if (visible)
				columnNames.add(label);

			String ID = item.getId();
			/* String ID= item.getAttributeValue("ID"); */

			Map_IDForm_Column.put(ID, new Integer(col));
			Map_Column_IDForm.add(ID);

			Integer idProp = item.getIdProp();

			if (item.hasFinder()) {
				columnsFinder.add(col);
				if (item.getTypeFinder() == GTableColumn.CREATION_FINDER)
					columnsFinderIfCreation.add(col);
			}

			if (item.hasCreation())
				columnsCreation.add(col);
			/*
			 * Integer tapos= item.getAttributeValue("TA_POS")==null ? null: new
			 * Integer(item.getAttributeValue("TA_POS"));
			 */
			/* int tm= tapos==null ? -1:m_md.getID_TM( tapos ); */
			int tm = item.getType();
			if (idProp != null) {
				columnSintax.add(new Integer(tm));
				columnIdProps.add(idProp);
				Map_columnsIdProp.put(idProp, col);
				columnRef.add( /* new Integer( */item.getRef()/* ) */);
				/* columnRef.add( new Integer( item.getAttributeValue("REF") ) ); */
				if (visible)
					m_visibleDataColMap.add(new Integer(col));
			} else if (visible)
				m_visibleDataColMap.add(null);

			if (visible)
				m_visibleColMap.add(new Integer(col));

			columnsEnable.add(col);

			// System.err.println("label:"+label+" enable:"+enable);
			// boolean editable = true;
			// columnEditable.add(new Boolean(editable));
		//	logger.info("tm:" + tm + " ID:" + ID);
			if (ID.equals(GConst.ID_COLUMN_TABLE_SELECTION) && tm == GConst.TM_BOOLEAN) {
			//	logger.info("boolean detected:" + col);
				columnSelectionRowTable = col;
			}
			size++;
			col++;
		}

		// logger.info("before_build_rows");
		buildRows(listaFilas, false);
		// logger.info("after_build_rows");
		updateColumnWidths();
		//logger.info("finish_build_table");
	}

	/**
	 * Añade filas a la tabla pudiendo ser un vector de GTableRow o de RowItem
	 * 
	 * @param rows
	 * @param replace
	 * @throws AssignValueException
	 */
	public void buildRows(Vector<?> rows, boolean replace) {
		//logger.info("start_build_rows");
		// System.err.println("**************** Inicio
		// buildRows**********"+System.currentTimeMillis());
		int numRowsBefore = getRowCount();
	//	logger.info("number_of_rows_before:" + numRowsBefore);
		int rowSelection = -1;
		Iterator<?> itr = rows.iterator();
		//logger.info("number of rows:" + rows.size());

		boolean isGTableRow = true;

		while (itr.hasNext()) {
			GTableRow tableRow = (GTableRow) itr.next();
			boolean rowAdded = setTableRow(tableRow, replace);
			if (rowAdded)
				rowSelection = getRowCount() - 1;
		}
		// Deseleccionamos el checkbox de seleccion de todo al insertar nuevos registros
		// We deselect the checkbox of selection of all when inserting new records
		if (replace && columnSelectionRowTable != null) {
			//TableColumn tc = getColumnModel().getColumn(columnSelectionRowTable);
			TableColumn tc = getColumnModel().getColumn(columnSelectionRowTable);
			tc.setHeaderValue(false);
			parent.getTable().getTableHeader().repaint();
		}

		if ((m_creationRow || m_finderRow) && rowSelection != -1) {
			// Hacemos, si existe, que la fila en blanco sea la ultima fila de la tabla
			boolean selection = true;
			if (parent != null && parent.getTable().getSelectedRow() == -1) {
				selection = false;
			}

			if (!init && selection) {
				parent.getTable().setRowSelectionInterval(getRowCount() - 1, getRowCount() - 1);// Fila Creada

			}

		}

		if (!replace && !m_cuantitativo && (parent == null || parent.m_modoFilter)) {
			int endOfRange = numRowsBefore + rows.size() - 1;
		//	logger.info("end_of_range:"+endOfRange);
			fireTableRowsInserted(numRowsBefore, endOfRange);
		}

		//logger.info("finish_build_rows");
	}

	public void updateColumnWidths() {
		// Hacemos que se ejecute en el hilo AWT ya que, si no es asi, a veces se queda
		// bloqueado en las pruebas
		final Runnable update = new Runnable() {
			public void run() {
				updateColumnWidths(0);
			}
		};
		SwingUtilities.invokeLater(update);
	}

	/**
	 * Actualiza el ancho de las columnas de la tabla a partir de su contenido
	 * 
	 * @param depth Nos sirve para evitar que entre en bucle infinito
	 */
	private void updateColumnWidths(final int depth) {
		final Runnable update = new Runnable() {
			public void run() {
				updateColumnWidths(depth + 1);
			}
		};
		if (depth < 10) {
			if (parent != null
					&& (m_modoFilter || parent.getTable().isValid())/*
																	 * Esperamos a que sea valido por que si no los
																	 * tamaños para hacer los calculos no son correctos
																	 */)
				calcColumnWidths(parent.getTable());
			else {
				SwingUtilities.invokeLater(update);
			}
		}
	}

	public void setStateDataFilter(boolean state) {
		boolean changed = m_filteredState != state;
		m_filteredState = state;
		if (changed)
			updateGUI(true);
	}

	public Object getValueAt(int row, int col) {

		int dataCol = getRealDataColumn(col);
		if (dataCol == -1)
			return null;

		return getDataValueAt(row, getRealDataColumn(col));
	}

	public Object getDataValueAt(int row, int col) {
		// System.out.println("GET VALUE
		// AT:CUANT:"+m_cuantitativo+","+row+","+col+","+getRowCount());
		if (getRowCount() < row + 1) {
			System.err.println("Table Form Model:getValueAt, error, no existe el registro " + row);
			return null;
		}
		//logger.info("row:" + row);
		RowItem it = (RowItem) m_rowData.get(getRowIndex(row));
		if (it.getColumnSize() < col + 1) {
			System.err.println("Table Form Model:getValueAt, error, no existe la col, row " + col + "," + row);
			return null;
		}

		Object resultValue = it.getColumnData(col);
		//logger.info("resultValue:" + resultValue + " column_index:" + col);
		return resultValue;
	}

	public boolean setTableRow(boolean replace) {

		return true;
	}

	public boolean setTableRow(GTableRow tableRow, boolean replace) {
		//logger.info("start_set_table_row");
		Iterator<String> itrIdColumns = Map_Column_IDForm.iterator();
		HashMap<String, Object> columnValues = new HashMap<String, Object>();
		while (itrIdColumns.hasNext()) {
			String idColumn = itrIdColumns.next();
			Object value = tableRow.getDataColumn(idColumn);
			columnValues.put(idColumn, value);
		}

		int columns = getColumnCount();
		ArrayList<Object> columnData = new ArrayList<Object>();
		for (int i = 0; i < columns; i++) {
			String id = getFieldIDFromColumn(i);
			Object value = columnValues.get(id);
			columnData.add(value);

		}

		RowItem ritem = buildRowItem(1, columnData);
		//logger.info("finish_set_table_row");
		return setRowItem(ritem, replace);
	}

	private boolean setRowItem(RowItem ritem, boolean replace) {
		//logger.info("start_set_row_item");
		boolean rowAdded = true;

		if (!replace) {
			subAddRow(ritem.getIndex(), ritem);
		}
		//logger.info("finish_set_row_item");
		return rowAdded;
	}

	public void subAddRow(int row, RowItem ritem) {
		//logger.info("start_sub_add_row");
		m_rowData.add(row, ritem);
//Si la insercion no es al final modificamos el index de los siguientes rowItem
		for (int i = row + 1; i < m_rowData.size(); i++) {
			m_rowData.get(i).setIndex(i);
		}
		m_visibleRowMap.add(/* new Integer(row) */m_rowData.size() - 1);

		//logger.info("finish_sub_add_row");

	}

	private RowItem buildRowItem(int row, ArrayList columnData) {
		RowItem ritem = new RowItem();
		ritem.setColumnData(columnData);
		return ritem;
	}

	public void calcColumnWidths(JTable table) {
		table.validate();
		// table.doLayout();
		JTableHeader header = table.getTableHeader();
		TableCellRenderer defaultHeaderRenderer = null;
		if (header != null)
			defaultHeaderRenderer = header.getDefaultRenderer();

		TableColumnModel columns = table.getColumnModel();
		GTableModelReduction data = this;
		int margin = columns.getColumnMargin();
		int rowCount = data.getRowCount() < 10 ? data.getRowCount() : 10;// Nos basamos en los primeros 10 registros
																			// para hacer los calculos ya que si no
																			// tarda demasiado
		int totalWidth = 0;
		int totalWidthRows = 0;

		HashMap<TableColumn, Integer> mapTableColumnToResizeNotPrioritary = new HashMap<TableColumn, Integer>();
		for (int i = columns.getColumnCount() - 1; i >= 0; --i) {
			TableColumn column = columns.getColumn(i);
			int columnIndex = column.getModelIndex();
			int width = -1;
			TableCellRenderer h = column.getHeaderRenderer();
			if (h == null)
				h = defaultHeaderRenderer;
			if (h != null) {
				Component c = h.getTableCellRendererComponent(table, column.getHeaderValue(), false, false, -1, i);
				width = c.getPreferredSize().width;
			}

			// Las columnas prioritarias son las que vamos a respetar el tamaño calculado.
			// Las no prioritarias dependera de si hay espacio suficiente o no. Si no lo hay
			// se les restaran pixeles.
			boolean prioritaryColumn = false;
			if (rowCount > 0) {
				TableCellRenderer r = table.getCellRenderer(0, i);

			}

			int widthRows = width;
			for (int row = rowCount - 1; row >= 0; --row) {
				TableCellRenderer r = table.getCellRenderer(row, i);
				Component c = r.getTableCellRendererComponent(table, data.getValueAt(row, columnIndex), false, false,
						row, i);
				if (c != null) {
					// System.err.println("parent.getTable().getName():"+parent.getTable().getName()+"
					// con preferredSize:"+c.getPreferredSize());
					widthRows = Math.max(widthRows, (int) c.getPreferredSize().getWidth());
				}
			}
			if (widthRows >= 0) {
				if (prioritaryColumn) {
					column.setPreferredWidth(widthRows + margin);// Asignamos el tamaño maximo de la cabecera y de las
																	// filas
				} else {
					column.setPreferredWidth(width + margin);// Asignamos el tamaño de la cabecera. Mas abajo se le
																// asignara el tamaño dependiente del contenido y del
																// espacio disponible
					mapTableColumnToResizeNotPrioritary.put(column, widthRows + margin);
				}
			} /*
				 * else System.err.println("No hay ancho");
				 */
			totalWidth += column.getPreferredWidth();
			totalWidthRows += (widthRows + margin);
		}

		table.validate();
		// table.doLayout();
	}

	public void setValueAt(Object newVal, int rowIndex, int columnIndex) {
		lastSetValueSuccess = true;
		executingSetValue = true;
		RowItem it = (RowItem) m_rowData.get(getRowIndex(rowIndex));

		// System.err.println("newVal "+newVal+" rowIndex "+rowIndex+" columnIndex
		// "+columnIndex+" rowItem:"+it);
		Object oldVal = getValueAt(rowIndex, columnIndex);
		// DEPURACION
		logger.info("setValueAt:"+columnIndex);
		if (Auxiliar.equals(columnSelectionRowTable, columnIndex)) {
			logger.info("setValueAt:"+columnIndex);
			it.setColumnData(columnIndex, newVal);
			fireTableCellUpdated(rowIndex, columnIndex);
			if (Auxiliar.equals(newVal, false)) {
				ListSelectionModel listSelection = parent.getTable().getSelectionModel();
				if (!listSelection.isSelectionEmpty()
						&& listSelection.getMinSelectionIndex() != listSelection.getMaxSelectionIndex()) {
					listSelection.removeSelectionInterval(rowIndex, rowIndex);
				}
				TableColumn tc = getColumnModel().getColumn(columnIndex);
				tc.setHeaderValue(false);
				parent.getTable().getTableHeader().repaint();
			}
		}
	}

	public int getColumnSintax(int col) {
		return ((Integer) columnSintax.get(col)).intValue();
	}

	public int getColumnIdProps(int col) {
		return ((Integer) columnIdProps.get(col)).intValue();
	}

	public Integer getColumnOfIdProp(int idProp) {
		return Map_columnsIdProp.get(idProp);
	}

	public void printRows() {

	}

	void updateGUI(boolean reagrupar) {

		 fireTableDataChanged();
		// printRows();
	}

	public boolean isVisible(int rowIndex, boolean filteringAware) {

		return true;
	}

	public boolean removeNullRow() {

		return true;
	}

	public TableColumnModel getColumnModel() {
		if(null == parent) {
			logger.info("parent is null");
		}
		
		if(null == parent.getTable()) {
			logger.info("table is null:");
		}
		return parent.getTable().getColumnModel();
	}

	public boolean isCellEditable(int row, int col) {

		return true;
	}

	public int getColumnCount() {
		// incluye la columna de boton desplegable en cuantitativo
		return columnNames.size();
	}

	public int getRowCount() {
		if (m_visibleRowMap != null) {
			
			int size = m_visibleRowMap.size();
			//logger.info("m_visibleRowMap: "+size);
			return size;
		}
		return 0;
	}

	public String getColumnName() {
		return "Name";
	}

	private int getRowIndex(int visibleRow) {
		// System.err.println("getRowIndex: visibleRow:"+visibleRow+"
		// m_visibleRowMap:"+m_visibleRowMap);
		return ((Integer) m_visibleRowMap.get(visibleRow)).intValue();
	}

	/*
	 * public Object getValueAt(int rowIndex, int columnIndex) {
	 * logger.info("getValueAt called"); //RowItem it = (RowItem)
	 * m_rowData.get(getRowIndex(row)); CustomApplication si =
	 * this.list.get(rowIndex); switch (columnIndex) { case 0: return si.getName();
	 * 
	 * } return null;
	 * 
	 * 
	 * 
	 * }
	 */

	int getDataColumn(int columnIndex) {
		return columnIndex - (m_cuantitativo ? 1 : 0);
	}

	public boolean isColumnSortable(int column) {
		// Note: For TinyTableModel this works fine. You may
		// take another approach depending on your kind of data.
		if (m_rowData.isEmpty())
			return false;

		Object value = null;
		int i = -1;
		// Buscamos el primer que no sea null ya que si no no podemos saber si los datos
		// de esa columna son Comparable
		while (value == null && i < getRowCount() - 1) {
			i++;
			value = getValueAt(i, column);
		}
		return (value instanceof Comparable);

	}

	public boolean supportsMultiColumnSort() {
		// We support multi column sort
		return true;
	}

	public Integer getColumnSelectionRowTable() {
		return columnSelectionRowTable;
	}

	public List<RowItem> getRowData() {
		return m_rowData;
	}

	public RowItem getRowItemFromIndex(int rowIndex) {
		return m_rowData.get(getRowIndex(rowIndex));
	}

	/*
	 * public LinkedHashMap<String,Integer> getPossibleTypeForValue(String
	 * idParent,Object value,Integer valueCls){ if(m_controlListener!=null) return
	 * m_controlListener.getPossibleTypeForValue(idParent, value, valueCls); return
	 * new LinkedHashMap<String, Integer>(); }
	 */
	public void sortColumns(final int[] columns, final int[] sortingDirections, JTable table) {
		//logger.info("sortColumns_start");
		// Si la primera fila es una fila permanente(favorito) la quitamos para que no
		// moleste al usuario ya que se queda siempre la primera
		if (!m_rowData.isEmpty() && m_rowData.get(0).isPermanent()) {
			try {
				removeRow(0, true);
			} catch (Exception e) {
				System.err.println("Error al intentar quitar de la tabla el favorito al ordenar");
				// e.printStackTrace();
			}
		}

		int[] sr = table.getSelectedRows();
		int[] sc = table.getSelectedColumns();
		int rowIndex = 0;

		Iterator ii = m_rowData.iterator();
		while (ii.hasNext()) {
			RowItem ri = (RowItem) ii.next();
			ri.setIndexOld(ri.getIndex());
		}

		// The sorting part...
		if (columns.length == 0) {
			// The natural order of our data depends on first (Integer) column
			Collections.sort(m_rowData, new Comparator<RowItem>() {
				public int compare(RowItem r1, RowItem r2) {
					// For our data we know that arguments are non-null and are of type Record.
					Comparable val1 = (Comparable) r1.getColumnData(0);
					Comparable val2 = (Comparable) r2.getColumnData(0);
					if (val1 instanceof String)
						return Constants.languageCollator.compare(val1, val2);
					else
						return val1.compareTo(val2);
				}
			});
		} else {
			final ArrayList<Boolean> stringColumnWithNumber = new ArrayList<Boolean>();
			// Miramos si siendo esa columna String, todos sus valores son Integer, para así
			// sabe si ordenar esa columna numericamente en vez de alfabeticamente
			for (int i = 0; i < columns.length; i++) {
				Iterator<RowItem> itr = m_rowData.iterator();
				boolean isNumericOfString = true;
				while (itr.hasNext() && isNumericOfString) {
					RowItem rowItem = itr.next();

					Comparable value = (Comparable) rowItem.getColumnData(columns[i]);
					if (value != null) {
						if (value instanceof String) {
							isNumericOfString = Auxiliar.hasDoubleValue((String) value);
						} else {
							isNumericOfString = false;
						}
					}
				}
				stringColumnWithNumber.add(isNumericOfString);
			}

			// Creamos el mapa de valores en caso de columnas enumerados ya que tenemos que
			// comparar luego con el label y no con el ido
			final HashMap<Integer, HashMap<Integer, String>> mapValuesPossible = new HashMap<Integer, HashMap<Integer, String>>();
			for (int i = 0; i < columns.length; i++) {
				GTableColumn columna = ((GFormTable) parent.m_objFormField).getColumn(columns[i]);
				/*
				 * if(columna.getValuesPossible()!=null){ //Para el caso de que sea un
				 * enumerado, ya que tenemos que comparar con el label y no con el ido.
				 * Machacamos los idos por los label. Esto pasa en las tablas de los
				 * formularios, no en las tablas de busquedas. Iterator<GValue> itrId =
				 * columna.getValuesPossible().iterator(); HashMap<Integer,String> mapValues=new
				 * HashMap<Integer, String>(); while (itrId.hasNext()) { GValue parValue =
				 * itrId.next(); mapValues.put(parValue.getId(), parValue.getLabel()); }
				 * mapValuesPossible.put(i,mapValues); }
				 */
			}

			// Multi column sort
			Collections.sort(m_rowData, new Comparator<RowItem>() {
				public int compare(RowItem r1, RowItem r2) {
					// For our data we know that arguments are non-null and are of type Record.
					if (r1.isNullRow())
						return 1;
					else if (r2.isNullRow())
						return -1;
					else if (r1.isPermanent() && !r2.isPermanent())
						return -1;
					else if (r2.isPermanent() && !r1.isPermanent())
						return 1;
					else {
						for (int i = 0; i < columns.length; i++) {
							Comparable val1 = (Comparable) r1.getColumnData(columns[i]);
							Comparable val2 = (Comparable) r2.getColumnData(columns[i]);

							int result = 0;
							if (val1 == null) {
								if (val2 != null)
									result = -1;
							} else if (val2 == null) {
								result = 1;
							} else if (val1 instanceof String) {
								if (stringColumnWithNumber.get(i)) {// Miramos si tenemos que ordenar esta columna
																	// numerica o alfabeticamente
									result = Double.valueOf((String) val1).compareTo(Double.valueOf((String) val2));
								} else {
									result = Constants.languageCollator.compare(val1, val2);
								}
							} else {

								if (mapValuesPossible.containsKey(i)) {
									// Para el caso de que sea un enumerado, ya que tenemos que comparar con el
									// label y no con el ido. Machacamos los idos por los label. Esto pasa en las
									// tablas de los formularios, no en las tablas de busquedas.
									if (mapValuesPossible.get(i).containsKey(val1)) {
										val1 = mapValuesPossible.get(i).get(val1);
									}
									if (mapValuesPossible.get(i).containsKey(val2)) {
										val2 = mapValuesPossible.get(i).get(val2);
									}
								}

								result = val1.compareTo(val2);
							}

							if (result != 0) {
								if (sortingDirections[i] == SORT_DESCENDING)
									return -result;
								return result;
							}
						}
						return 0;
					}
				}
			});
		}
		// Tell our listeners that data has changed
		fireTableDataChanged();

		// Restore selection
		rowIndex = 0;

		ii = m_rowData.iterator();
		while (ii.hasNext()) {
			RowItem ri = (RowItem) ii.next();
			ri.setIndex(rowIndex++);
		}
		/*List temp =  ((Object) m_rowData).clone();
		/Collections.sort(temp, new Comparator<RowItem>() {
			public int compare(RowItem r1, RowItem r2) {
				if (r1.getIndexOld() > r2.getIndexOld())
					return 1;
				return -1;
			}
		});*/
		// Adding one row selection interval after another is probably inefficient.
		for (int i = 0; i < sr.length; i++) {
			//int row = ((RowItem) temp.get(sr[i])).getIndex();
			//table.addRowSelectionInterval(row, row);
		}
		for (int i = 0; i < sc.length; i++) {
			table.addColumnSelectionInterval(sc[i], sc[i]);
		}
		//logger.info("sortColumns_finished");
	}

	public void removeRow(int dataRowIndex, boolean refreshGui) throws Exception {
		Integer key = new Integer(dataRowIndex);
		int visibleRow = m_visibleRowMap.indexOf(key);
//System.err.println("VISIBLE ROW:"+visibleRow+" getRowCount:"+getRowCount()+" table.getRowCount:"+parent.getTable().getRowCount());
		if (visibleRow != -1) {
			m_visibleRowMap.remove(visibleRow);
			if (m_visibleRowMap.size() > visibleRow) {
				for (int i = visibleRow; i < m_visibleRowMap.size(); i++) {
					Integer indexData = (Integer) m_visibleRowMap.get(i);
					m_visibleRowMap.set(i, new Integer(indexData.intValue() - 1));
				}
			}
		}
		RowItem ri = m_rowData.get(dataRowIndex);
		m_rowData.remove(ri);
		if (refreshGui)
			if (m_cuantitativo)
				updateGUI(true);
			else if (visibleRow != -1)
				fireTableRowsDeleted(visibleRow, visibleRow);

//Si esta en modo edicion mientras se borra una fila hacemos que salga de ese modo ya que su row es incorrecta y provoca una excepcion
		if (parent != null) {
			int editingRow = parent.getTable().getEditingRow();
			if (editingRow >= dataRowIndex) {// Si la fila es menor que la borrada el row es correcto por lo que no
												// hacemos nada
				parent.getTable().removeEditor();
			}

		}
		/*
		 * if((m_creationRow || m_finderRow) && !ri.isNullRow() && !m_modoConsulta){
		 * if(!this.parent.getFormField().isTopLabel())//Si es una tabla de una sola
		 * fila y se ha borrado pues ponemos la nullRow addNullRow(); }
		 */
	}
	/*
	 * public Color getRowColor(int visibleRow) { int rowPos =
	 * getRowIndex(visibleRow); RowItem row = (RowItem) m_rowData.get(rowPos);
	 * return row.getColor(); }
	 */
	/*
	 * public boolean isColumnSortable(int column) {
	 * 
	 * if(m_rowData.isEmpty()) return false;
	 * 
	 * Object value=null; int i=-1; //Buscamos el primer que no sea null ya que si
	 * no no podemos saber si los datos de esa columna son Comparable
	 * while(value==null && i<getRowCount()-1){ i++; value=getValueAt(i, column); }
	 * return (value instanceof Comparable); }
	 */
	
	///pagination
	public void removeData(Boolean permanent) {
		if(permanent==null)
			m_rowData.clear();
		else{
			for(int i=m_rowData.size()-1;i>=0;i--){
				RowItem rItem=m_rowData.get(i);
				if(rItem.isPermanent()==permanent)
					m_rowData.remove(i);	
			}
		}
		updateGUI(true);
		// fireTableRowsDeleted(1,len);
	}
	//pagination
	public void setVisibleRow(ArrayList<Integer> intVisibleRowMap) {
		this.m_visibleRowMap = intVisibleRowMap;
	//	logger.info("intVisibleRowMap: "+intVisibleRowMap.size());
		//updateGUI(false);
		updateGUI(true);
	}
	
	public ArrayList getVisibleRows() {
		return this.m_visibleRowMap;
	}
/*
	@Override
	public void onEvent(Event e) {
		// TODO Auto-generated method stub
		if(e instanceof UpdateFindings) {
			//logger.info("update findings");
			if(e.getPayload() instanceof List) {
			//	logger.info("update findings Gtable");
			///	ArrayList gTable = (ArrayList) e.getPayload();
			//	logger.info("Row Count:"+ gTable.size());
			//	setVisibleRow(gTable);
		//	table = (GTable)e.getPayload();
			//setTable(table);
			try {
				
				//direct(gTable);
				//updatePagination();
				//directRowIndex(10);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			}
		}
	}
*/
}