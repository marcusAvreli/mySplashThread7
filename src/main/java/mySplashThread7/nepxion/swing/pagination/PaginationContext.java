package mySplashThread7.nepxion.swing.pagination;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mySplashThread7.events.Event;
import mySplashThread7.events.EventBroker;
import mySplashThread7.events.EventConsumer;
import mySplashThread7.events.UpdateFindings;
import mySplashThread7.settings.ClientSettings;

//nepxion-swing/src/com/nepxion/swing/pagination/PaginationContext
public class PaginationContext implements EventConsumer
{	
	private static final Logger logger = LoggerFactory.getLogger(PaginationContext.class);
	//number of visible row to start from
	private int rowIndex = 1; // 起始行序号，对于数据库中记录序号
	//number of rows per page
	private int rowCount = 100; // 单页的显示的行数
	//number of all rows
	private int totalRowCount = -1; // 总行数，对于数据库中的全部记录数
	private int pageIndex = 1; // 跳转页序号
	
	
	
	private boolean isValidation = true; // 上下文和状态检验，当分页上下文没有任何改变，则不执行数据库操作（例如当前页是5，进行“跳转到第5页”的操作时候，不执行）
	
	public PaginationContext()
	{
		
	}
	public int getStartIndex() {
		int startIndex = getRowIndex()-1;
		/*if(startIndex == 1) {
			startIndex = 0;
		}*/
		return startIndex;
	}
	public int getEndIndex() {
		int startIndex = getStartIndex();
		int endIndex = startIndex + rowCount;
		if (endIndex > getTotalRowCount()) {
		    endIndex = getTotalRowCount();
		}	
	//	logger.info("startIndex:"+startIndex);
	//	logger.info("endIndex:"+endIndex);
		//logger.info("row count:"+rowCount);
		return endIndex;
	}
	public PaginationContext(int totalRowCount)
	{
		
		this.totalRowCount = totalRowCount;
		ClientSettings clientSettings = ClientSettings.getInstance();
		rowCount = clientSettings.getNumberOfRowsPerPage();
		EventBroker b = EventBroker.getInstance();
		b.subscribeConsumer(this, Event.TYPE_UPDATE_FINDINGS);
	}
	
	public int getRowIndex()
	{
		//logger.info("getRowIndex row_index:"+rowIndex);
		return rowIndex;
	}
	
	public void setRowIndex(int rowIndex)
	{
		this.rowIndex = rowIndex;
	}
	
	public int getRowCount()
	{
		return rowCount;
	}
	
	public void setRowCount()
	{
		logger.info("set row count start");
		this.pageIndex = 1;
		
		if (this.rowCount > this.totalRowCount && this.totalRowCount != -1)
		{
			this.rowCount = this.totalRowCount;
		}
		logger.info("set row count finish");
	}
	
	public int getTotalRowCount()
	{
		return totalRowCount;
	}
	
	public void setTotalRowCount(int totalRowCount)
	{logger.info("set row count start"+ this.rowCount);
		this.totalRowCount = totalRowCount;
		
		if (this.rowCount > this.totalRowCount && this.totalRowCount != -1)
		{
			this.rowCount = this.totalRowCount;
		}logger.info("set row count finish" + this.rowCount);
	}
	
	public int getPageIndex()
	{
		return pageIndex;
	}
	
	public void setPageIndex(int pageIndex)
	{
		//logger.info("setPageIndex pageIndex:"+pageIndex);
		this.pageIndex = pageIndex;
		this.rowIndex = (pageIndex - 1) * rowCount + 1;
		//logger.info("setPageIndex row_index:"+rowIndex);
	}
	
	
	
	
	
	
	
	public boolean isValidation()
	{
		return isValidation;
	}
	
	public void setValidation(boolean isValidation)
	{
		this.isValidation = isValidation;
	}
	
	// 第X页
	protected int getCurrentPageIndex()
	{
		int rowIndex = getRowIndex();
		int rowCount = getRowCount();
		int totalRowCount = getTotalRowCount();
		
		if (rowCount == 0)
		{
			return 1;
		}
		
		return (rowIndex / rowCount) + (rowIndex % rowCount == 0 ? 0 : 1);
	}
	
	// 当前页有X条
	protected int getCurrentPageRowCount()
	{
		int rowIndex = getRowIndex();
		int rowCount = getRowCount();
		int totalRowCount = getTotalRowCount();
		
		int currentPageIndex = getCurrentPageIndex();
		int totalPageCount = getTotalPageCount();
		if (currentPageIndex == totalPageCount)
		{
			return totalRowCount - (currentPageIndex - 1) * rowCount;
		}
		
		return rowCount;
	}
	
	// 共X页
	protected int getTotalPageCount()
	{
		int rowIndex = getRowIndex();
		int rowCount = getRowCount();
		int totalRowCount = getTotalRowCount();
		
		if (rowCount == 0)
		{
			return 1;
		}
		
		int totalPageCount = (totalRowCount / rowCount) + (totalRowCount % rowCount == 0 ? 0 : 1);
		if (totalPageCount < 1)
		{
			totalPageCount = 1;
		}
		
		return totalPageCount;
	}
	@Override
	public void onEvent(Event e) {
		// TODO Auto-generated method stub
		if(e instanceof UpdateFindings) {
			if(e.getPayload() instanceof List) {
			ArrayList<Integer> visibleRows = (ArrayList<Integer>) e.getPayload();
			int visibleSize = visibleRows.size();
			if(visibleSize==0) {			
				setTotalRowCount(-1);
			}else {
				setTotalRowCount(visibleSize);
			}
			setPageIndex(1);
			}
		}
	}
	
	
}