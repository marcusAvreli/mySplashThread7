package mySplashThread7.gui;

import java.awt.Component;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.JTable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mySplashThread7.dynagent.common.utils.RowItem;
import mySplashThread7.events.Event;
import mySplashThread7.events.EventBroker;
import mySplashThread7.events.OnLoadPaneView;
import mySplashThread7.events.UpdateFindings;
import mySplashThread7.gdev.gawt.GTable;
import mySplashThread7.gdev.gawt.GTableModelReduction;
import mySplashThread7.gfld.GFormField;
import mySplashThread7.gfld.GFormTable;
import mySplashThread7.gfld.GTableColumn;
import mySplashThread7.gfld.GTableRow;
import mySplashThread7.nepxion.swing.pagination.PaginationContext;

public class QuickAddressSearchThread implements Runnable {
    
	private static final Logger logger = LoggerFactory.getLogger(QuickAddressSearchThread.class);
    
    private String query;
    private Component owner;
    private GTable target;
    private String[] tag;
    private EventBroker b = EventBroker.getInstance();
    /** Creates a new instance of QuickAddressSearchThread */
    public QuickAddressSearchThread(Component owner, String query,  GTable target) {
        this.query=query;
        this.owner=owner;
        this.target=target;
        
    }

    @Override
    public void run() {
       
       // logger.info("thread started");
       // logger.info("search_query:"+query);
        Hashtable<String, ArrayList<String>> tags = null;
        GFormField m_objFormField= target.getFormField();
        GTableModelReduction objTableModel=       target.getModel();
        List<RowItem> rowItems = objTableModel.getRowData();
    	Vector<GTableRow> tableRows = ((GFormTable)m_objFormField).getRowList();
    	Vector vectorColumnList = ((GFormTable)m_objFormField).getColumnList();
    	Set<Integer> findings = new HashSet<Integer>();
    	for(int i=0;i<vectorColumnList.size();i++) {
    		GTableColumn column = (GTableColumn) vectorColumnList.get(i);
	    	String columnName = column.getId();
	   int tableRowsSize = tableRows.size();
	    	for(int j=0;j<tableRowsSize;j++) {
		    	Object dataColumnValueObject  =tableRows.get(j).getDataColumn(columnName);	
		    	
		    	
		    	
		    	 
		    	
		    	
		    	if(null !=dataColumnValueObject) {
		    		if(dataColumnValueObject instanceof String) {
		    			String dataColumnValue = (String)dataColumnValueObject;
		    			int location = tableRowsSize-1-j;
		    			if(dataColumnValue.equals(query)) {
		    				//logger.info("column_value:"+dataColumnValue);	
		    				findings.add(location);
		    				
		    			}
		    		
		    			if(dataColumnValue.startsWith(query)) {
		    				//logger.info("column_value:"+dataColumnValue);	
		    				findings.add(location);
		    				
		    			}
		    			if(dataColumnValue.endsWith(query)) {
		    				//logger.info("column_value:"+dataColumnValue);	
		    				findings.add(location);
		    				
		    			}			    	
		    			if(dataColumnValue.contains(query)) {
		    				//logger.info("column_value:"+dataColumnValue);	
		    				findings.add(location);
		    			
		    				
		    			}
		    		}		    		
		    	}
	    	}
    	}
		//logger.info("findings:"+findings.size());
	//	logger.info("findings:"+findings);
		ArrayList<Integer> visibleRows = new ArrayList<Integer>(findings);
		
		logger.info("visible rows size:"+visibleRows.size());
		logger.info("found indexes:"+visibleRows);
	
		
	//	GTableModelReduction m_objTableModel = target.getModel();
		
		// m_objTableModel.setVisibleRow(visibleRows);
		// target.setModel(m_objTableModel);
		// target.setModel(m_objTableModel);
		 //gtable changed, so  update pagination context
		 
		 //   b.publishEvent(new UpdateFindings(this,Event.TYPE_UPDATE_FINDINGS,visibleRows));    
		 b.publishEvent(new UpdateFindings(this,Event.TYPE_UPDATE_FINDINGS,visibleRows));
		// PaginationContext paginationContext = new PaginationContext(visibleRows.size());
       
    }
    
}
