package mySplashThread7;

import java.awt.ComponentOrientation;

import javax.swing.JTable;
import javax.swing.table.TableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mySplashThread7.muntjak.tinylookandfeel.table.TinyTableHeaderRenderer;
import mySplashThread7.muntjak.tinylookandfeel.table.TinyTableHeaderUI;

public class MagTable extends JTable {
	private static final Logger logger = LoggerFactory.getLogger(MagTable.class);
	public MagTable(TableModel model) {
		
	    this.setModel(model);
	    this.setAutoCreateRowSorter(true); //?
        init();

	}
	public MagTable() {
	    
	    this.setAutoCreateRowSorter(true); //?
       

	}
	  public void init() {
		  setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		  /*
		   ClientSettings clientSettings = ClientSettings.getInstance();
			 applyComponentOrientation(ComponentOrientation
		                .getOrientation(clientSettings.getLocale()));
			 
			 */
		  setNewHeaderUI();
	  }
	 public void setNewHeaderUI() {
	        //display:
	        for (int i = 0; i < getTableHeader().getColumnModel().getColumnCount(); i++) {
	            getTableHeader().getColumnModel().getColumn(i).setHeaderRenderer(new TinyTableHeaderRenderer());
	        }
	        //behaviour:
	        getTableHeader().setUI(new TinyTableHeaderUI());
	    }

}
