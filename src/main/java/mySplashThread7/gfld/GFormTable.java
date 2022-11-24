package mySplashThread7.gfld;

import java.util.Vector;



//https://github.com/semantic-web-software/dynagent/tree/master/Elecom
///https://github.com/semantic-web-software/dynagent/blob/a0d356169ef34f3d2422e235fed7866e3dda6d8a/Elecom/src/gdev/gfld/GFormTable.java
public class GFormTable extends GFormField
{
    /** Número de Filas visibles*/
    protected int m_iVisibleRow;
    /** Nos indica si varios registros de la tabla son resumibles en uno. */
    protected boolean m_cuantitativo;
    /** */
    protected int m_atGroupColumn;
    /** */
    protected int m_iniVirtualColumn;
    

  
  


    protected Vector<GTableColumn> m_vColumn = new Vector<GTableColumn>();
    protected Vector<GTableRow> m_vRow = new Vector<GTableRow>();

    public GFormTable()
    {
        super();
        
    }
   

	
    public GTableColumn getColumn(int col)
    {
        if(col<0||col>=m_vColumn.size())
            return null;
        return (GTableColumn)m_vColumn.elementAt(col);
    }
    
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}
	 /**
     * Añade una columna a la tabla.
     * @param col La columna a añadir
     */
    public void addColumn(GTableColumn col)
    {
        m_vColumn.addElement(col);
    }
    
    
    /**
     * Añade una fila a la tabla.
     * @param row La fila a añadir
     */
    public void addRow(GTableRow row)
    {
        m_vRow.addElement(row);
    }
    /**
     * Obtiene la lista de filas de la tabla.
     * @return Vector - Devuelve las filas que contiene la tabla.
     */
    public Vector<GTableRow> getRowList()
    {
        return m_vRow;
    }
    public void setRowList(Vector<GTableRow> vRow)
    {
         m_vRow=vRow;
    }
    
    public double getRowHeight(){
    	/*if(m_iVisibleRow>1)
    		return GConfigView.heightRowTable;
    	else*/ return 20;//Si es de una sola fila nos interesa el alto de un campo normal
    }
    public Vector getColumnList()
    {
        return m_vColumn;
    }
}
