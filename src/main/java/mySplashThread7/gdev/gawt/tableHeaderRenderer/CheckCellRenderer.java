package mySplashThread7.gdev.gawt.tableHeaderRenderer;


import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

import mySplashThread7.dynagent.common.utils.Auxiliar;
import mySplashThread7.dynagent.common.utils.RowItem;
import mySplashThread7.gdev.gawt.GTable;
import mySplashThread7.gdev.gawt.GTableModelReduction;
import mySplashThread7.gdev.gawt.utils.TristateCheckBox;
import mySplashThread7.gen.GConfigView;
import mySplashThread7.gfld.GTableColumn;

public class CheckCellRenderer extends JCheckBox implements TableCellRenderer{
	private static final long serialVersionUID = 1L;

	private GTableColumn column;
	private GTable gTable;
	private boolean modeFilter;
	
	public CheckCellRenderer(GTableColumn column, GTable gTable, boolean filter) {
		super();
		this.column=column;
		this.gTable=gTable;
		this.modeFilter=filter;
		setOpaque(true);
		setBorder(BorderFactory.createEmptyBorder(0, GConfigView.horizontalMarginCell, 0, GConfigView.horizontalMarginCell));
	}
	
	public Component getTableCellRendererComponent(JTable table, Object val, boolean isSelected, boolean hasFocus, int row, int col) {

		//System.err.println("Valueeee renderer para row:"+row+" y col:"+col+" value:"+val+" selected:"+isSelected);
		super.setHorizontalAlignment(SwingConstants.CENTER);
		super.setVerticalAlignment(JLabel.CENTER);
		super.setMargin(new Insets(0,0,0,0));
		super.setIconTextGap(0);
		
		GTableModelReduction tfm = (GTableModelReduction) table.getModel();
		if(tfm.getRowData().size()>row){
			RowItem rowItem = (RowItem) tfm.getRowData().get(row);		
			
		}
		boolean bolVal=false;
		if(val==null){
			if(!modeFilter){
				super.setText("<"+column.getLabel()+">");
				super.setFont(new Font(getFont().getName(), Font.ITALIC, getFont().getSize()));
				super.setHorizontalTextPosition(TristateCheckBox.CENTER);
			    super.setVerticalTextPosition(TristateCheckBox.CENTER);
			    //if(table.isValid() && gTable.getModel().getColumnModel().getColumn(col).getWidth()<GViewBalancer.getDimString(getText(), false, getFont(), ((Graphics2D)table.getGraphics()).getFontRenderContext(),false).width && gTable.isTopLabel())
				//	setToolTipText(column.getLabel());
			    setForeground(Color.gray);
			}
		}
		else if(val instanceof Boolean){
			setText(null);
			bolVal=(Boolean)val;
		}else if(val instanceof String){
			setText(null);
			String[] buf=((String)val).split(":");
			bolVal=(buf[0].equals("null")?false:new Boolean(buf[0]));
		}
		
		if(tfm.getRowData().size()>row){
			if(Auxiliar.equals(gTable.getModel().getColumnSelectionRowTable(),col)){
				if(!bolVal)
					setToolTipText("Seleccionar");
				else setToolTipText("Deseleccionar");
				
				if(tfm.getRowItemFromIndex(row).isPermanent()){
					super.setForeground(table.getSelectionForeground());
					super.setBackground(GConfigView.colorBackgroundPermanent);
				}else{
					if (isSelected) {
						super.setForeground(table.getSelectionForeground());
						super.setBackground(table.getSelectionBackground());
					} else {
						super.setForeground(table.getForeground());
						super.setBackground(table.getBackground());
					}
				}
			}else{
				setToolTipText(this.column.getLabel());
				
				if (isSelected) {
					super.setForeground(table.getSelectionForeground());
					super.setBackground(table.getSelectionBackground());
				} else {
					super.setForeground(table.getForeground());
					super.setBackground(table.getBackground());
				}
			}
		}
		super.setSelected(bolVal);
		return this;
	}

}
