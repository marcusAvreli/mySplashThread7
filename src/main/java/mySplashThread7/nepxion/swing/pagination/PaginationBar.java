package mySplashThread7.nepxion.swing.pagination;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.ScrollPaneLayout;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicLabelUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mySplashThread7.events.Event;
import mySplashThread7.events.EventBroker;
import mySplashThread7.events.EventConsumer;
import mySplashThread7.events.UpdateFindings;
import mySplashThread7.gdev.gawt.GTable;
import mySplashThread7.gfld.GFormTable;
import mySplashThread7.nepxion.swing.locale.SwingLocale;

//https://github.com/semantic-web-software/dynagent/tree/master/Elecom
//GTable

import mySplashThread7.nepxion.swing.textfield.JNumberTextField;
import mySplashThread7.settings.ClientSettings;



public abstract class PaginationBar
	extends JPanel implements EventConsumer
{
	private static final Logger logger = LoggerFactory.getLogger(PaginationBar.class);
  
	/**
	 * 
	 */
	private JLabel currentPageIndexLabel; // 第X页
	private JLabel currentPageRowCountLabel; // 当前页有X条
	
	private JLabel totalPageCountLabel; // 共X页
	private JLabel totalRowCountLabel; // 总共X条
	private JLabel maxPageRowCountLabel; // 每页X条
	
	private JButton firstPageButton;
	private JButton backwardPageButton;
	private JButton forwardPageButton;
	private JButton lastPageButton;
	JScrollPane jScrollPane1;
	private JNumberTextField pageIndexDirectTextField; // 跳转到第X页
//	private JButton pageIndexDirectButton;
	
	
	
//	private JBasicComboBox sortNameComboBox; // 排序名称
//	private JBasicComboBox sortTypeComboBox; // 排序方式 ：升序/降序
//	private JButton submitSortButton;
	
	private GTable table;
	private JList list;
	
	private JPanel pageInfoPanel;
	private JPanel pageButtonPanel;
	private JPanel pageIndexDirectPanel;
	private JPanel maxPageRowCountDirectPanel;
	private JPanel sortPanel;
	private int maxPageRowCountDirect=-1;
	//private JLabel currentPageIndexLbl;
	
	private PaginationContext paginationContext;
	
	public PaginationBar(GTable table)
	{
		this(table, null);
	}
	
	public PaginationBar(JList list)
	{
		this(list, null);
	}
	
	public PaginationBar(GTable table, PaginationContext context)
	{
		
		this(context);
		//logger.info("checkPost_1");
		this.table = table;
		
		maxPageRowCountDirect = paginationContext.getRowCount();
		logger.info("max rows per page:"+maxPageRowCountDirect);
		try {
			clearRowDatas();
		
		
		paginationContext.setPageIndex(1);
	
		directRowCount(maxPageRowCountDirect);
		} catch (Exception e1) {
			logger.error("error",e1);
			e1.printStackTrace();
		}
		
		
	}
	
	public PaginationBar(JList list, PaginationContext context)
	{
		this(context);
		this.list = list;
		
	}
	
	public PaginationBar(PaginationContext context)
	{
		//logger.info("checkPost_start");
		EventBroker b = EventBroker.getInstance();
		b.subscribeConsumer(this, Event.TYPE_UPDATE_FINDINGS);
		
		paginationContext = context;
		/*ClientSettings clientSettings = ClientSettings.getInstance();
		 applyComponentOrientation(ComponentOrientation
	                .getOrientation(clientSettings.getLocale()));
		 
		 logger.info(":clientSettings.getLocale():"+clientSettings.getLocale().getCountry());
		 */
		MessageFormat mf = new MessageFormat(SwingLocale.getString("page_number_of"));
		String resultString = mf.format(new Object [] {"1",paginationContext.getTotalPageCount()});
		currentPageIndexLabel = createLabel(resultString);
		
		//currentPageIndexLabel = createLabel("");
		currentPageRowCountLabel = createLabel("");
		
		totalPageCountLabel = createLabel("");
		
		mf = new MessageFormat(SwingLocale.getString("showing_rows_of"));
		int startIndex = paginationContext.getStartIndex();
		int endIndex = paginationContext.getEndIndex();
		resultString = mf.format(new Object [] {startIndex,endIndex,paginationContext.getTotalRowCount()});
		totalRowCountLabel = createLabel(resultString);
		
		maxPageRowCountLabel = createLabel("");
		
		firstPageButton = new JButton(SwingLocale.getString("FirstPage"));
		firstPageButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				boolean isValidation = paginationContext.isValidation();
				int currentPageIndex = paginationContext.getCurrentPageIndex();
				if (isValidation && currentPageIndex == 1)
				{					
					//JOptionPane.showMessageDialog(HandleManager.getFrame(PaginationBar.this), SwingLocale.getString("first_page_description"), SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);
					
					return;
				}
				try
				{
					clearRowDatas();
					paginationContext.setPageIndex(1);
					directRowIndex(paginationContext.getRowIndex());
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}
		);
	//	logger.info("checkPost_1");
		backwardPageButton = new JButton(SwingLocale.getString("Back"));
		backwardPageButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				boolean isValidation = paginationContext.isValidation();
				int currentPageIndex = paginationContext.getCurrentPageIndex();
				if (isValidation && currentPageIndex == 1)
				{
				//	JOptionPane.showMessageDialog(HandleManager.getFrame(PaginationBar.this), SwingLocale.getString("first_page_description"), SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);
					
					return;
				}
				try
				{
					clearRowDatas();
					paginationContext.setPageIndex(currentPageIndex - 1);
					directRowIndex(paginationContext.getRowIndex());
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}
		);
	//	logger.info("checkPost_2");
		forwardPageButton = new JButton(SwingLocale.getString("Forward"));
		forwardPageButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				boolean isValidation = paginationContext.isValidation();
				int currentPageIndex = paginationContext.getCurrentPageIndex();
				int totalPageCount = paginationContext.getTotalPageCount();
				if (isValidation && currentPageIndex == totalPageCount)
				{
					//JOptionPane.showMessageDialog(HandleManager.getFrame(PaginationBar.this), SwingLocale.getString("last_page_description"), SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);
					JOptionPane.showMessageDialog(PaginationBar.this, "You have to give a source.\nPlease, " +
					        "give a source for the journal.", "Invalid journal source", JOptionPane.ERROR_MESSAGE);
					
					return;
				}
				try
				{
					//logger.info("checkPost_1");
					clearRowDatas();
					//logger.info("currentPageIndex:"+currentPageIndex);
					paginationContext.setPageIndex(currentPageIndex + 1);
					//logger.info("checkPost_3:"+paginationContext.getRowIndex());
					directRowIndex(paginationContext.getRowIndex());
					//logger.info("checkPost_4");
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}
		);
	//	logger.info("checkPost_3");
		lastPageButton = new JButton(SwingLocale.getString("LastPage"));
		lastPageButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				boolean isValidation = paginationContext.isValidation();
				int currentPageIndex = paginationContext.getCurrentPageIndex();
				int totalPageCount = paginationContext.getTotalPageCount();
				if (isValidation && currentPageIndex == totalPageCount)
				{
					//JOptionPane.showMessageDialog(HandleManager.getFrame(PaginationBar.this), SwingLocale.getString("last_page_description"), SwingLocale.getString("warning"), JBasicOptionPane.WARNING_MESSAGE);
					JOptionPane.showMessageDialog(PaginationBar.this, "You have to give a source.\nPlease, " +
					        "give a source for the journal.", "Invalid journal source", JOptionPane.ERROR_MESSAGE);
					
					return;
				}
				try
				{
					clearRowDatas();
					paginationContext.setPageIndex(totalPageCount);
					directRowIndex(paginationContext.getRowIndex());
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}
		);
	//	logger.info("checkPost_4");
		pageIndexDirectTextField = new JNumberTextField(1);
		pageIndexDirectTextField.setHorizontalAlignment(SwingConstants.CENTER);
		
		//pageIndexDirectTextField.setAlignmentX(CENTER_ALIGNMENT);
		//pageIndexDirectTextField.setAlignmentY(CENTER_ALIGNMENT);
		pageIndexDirectTextField.addKeyListener(new KeyAdapter() {

		    @Override
			public void keyTyped(KeyEvent e) {
		        if (e.getKeyChar() == KeyEvent.VK_ENTER) {
		        	int pageIndexDirect = -1;
		           logger.info("enter pressed");
		           pageIndexDirect = Integer.parseInt(pageIndexDirectTextField.getText());
		           
		           try
					{
						clearRowDatas();
						paginationContext.setPageIndex(pageIndexDirect);
						directRowIndex(paginationContext.getRowIndex());
					}
					catch (Exception ex)
					{
						ex.printStackTrace();
					}
		        }
		        
		    }
		});
		//logger.info("checkPost_5");
		setPageIndexDirectTextFieldWidth(35);
		//pageIndexDirectButton = new JButton("Submit");
		/*pageIndexDirectButton.addActionListener(new ActionListener()
		{
		
			
			public void actionPerformed(ActionEvent e)
			{
				int pageIndexDirect = -1;
				try
				{
					pageIndexDirect = Integer.parseInt(pageIndexDirectTextField.getText());
				}
				catch (NumberFormatException ex)
				{
				//	pageIndexDirectTextField.showTip(SwingLocale.getString("invalid_page_count"), JBalloonTip.TIP_ICON_ERROR_MESSAGE, 1, 9);
					
					return;
				}
				
				boolean isValidation = paginationContext.isValidation();
				int currentPageIndex = paginationContext.getCurrentPageIndex();
				int totalPageCount = paginationContext.getTotalPageCount();
				
				if (pageIndexDirect > totalPageCount)
				{
					//pageIndexDirectTextField.showTip(SwingLocale.getString("page_count_not_more_than") + totalPageCount, JBalloonTip.TIP_ICON_ERROR_MESSAGE, 2, 12);
					
					return;
				}
				
				if (isValidation && currentPageIndex == pageIndexDirect)
				{
					return;
				}
				try
				{
					clearRowDatas();
					paginationContext.setPageIndex(pageIndexDirect);
					directRowIndex(paginationContext.getRowIndex());
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}
		);
		*/
		
	/*	maxPageRowCountDirectTextField = new JNumberTextField(1);
		setMaxPageRowCountDirectTextFieldWidth(35);
		maxPageRowCountDirectButton = new JButton("Submit");
		maxPageRowCountDirectButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				int maxPageRowCountDirect = -1;
				try
				{
					maxPageRowCountDirect = Integer.parseInt(maxPageRowCountDirectTextField.getText());
					logger.info("maxPageRowCountDirect:"+maxPageRowCountDirect);
				}
				catch (NumberFormatException e1)
				{
					//maxPageRowCountDirectTextField.showTip(SwingLocale.getString("item_count_not_null"));
					
					return;
				}
				
				boolean isValidation = paginationContext.isValidation();
				int totalRowCount = paginationContext.getTotalRowCount();
				
				if (maxPageRowCountDirect > totalRowCount)
				{
					//maxPageRowCountDirectTextField.showTip(item_count_not_more_than") + totalRowCount);
					
					return;
				}
				
				try
				{
					//logger.info("checkPost_1");
					clearRowDatas();
					//logger.info("checkPost_2");
					paginationContext.setPageIndex(1);
					//logger.info("checkPost_3");
				//	paginationContext.setRowCount(maxPageRowCountDirect);
					//logger.info("checkPost_4");
					directRowCount(maxPageRowCountDirect);
					//logger.info("checkPost_5");
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}
		);*/
		
		
		//new ArrayList();
		
		
		
		
		
		//sortTypeComboBox = new JBasicComboBox(sortTypeList.toArray());
		//setSortTypeComboBoxWidth(60);
		
		//logger.info("checkPost_6");
		try
		{
			setPaginationContext(context);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		initPageInfoPanel();
		//setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		 Border border = createCompoundBorder();
		 setBorder(border);
		
		initPageButtonPanel();
		initPageIndexDirectPanel();
		initMaxPageRowCountDirectPanel();
		initSortPanel();
		//logger.info("checkPost_finish");
		//setLayout();
		
		
	}
	public void setLayout() {
		JLabel label = new JLabel("test2");
	    add(label);
	    jScrollPane1= new JScrollPane();
		if(null == table) {
			logger.info("table is null");
		}
		if(null == table.getTable()) {
			logger.info("mag table is null");
		}
		
	    jScrollPane1.setViewportView(table.getTable());
	    /*jScrollPane1.setLayout(new ScrollPaneLayout() {

	        @Override
	        public void layoutContainer(Container parent) {
	            JScrollPane scrollPane = (JScrollPane) parent;
	            scrollPane.setComponentOrientation(
	              ComponentOrientation.RIGHT_TO_LEFT);
	            super.layoutContainer(parent);
	            scrollPane.setComponentOrientation(
	              ComponentOrientation.RIGHT_TO_LEFT);
	        }
	    });*/
		 GroupLayout layout = new GroupLayout(this);
		 
	     layout.setAutoCreateGaps(true);
	     layout.setAutoCreateContainerGaps(true);
		 this.setLayout(layout);
		layout.setHorizontalGroup(layout.createSequentialGroup()
		.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
				.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
		.addGroup(layout.createParallelGroup()
				.addComponent(pageInfoPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		.addGroup(layout.createParallelGroup()
				.addComponent(pageButtonPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		.addGroup(layout.createParallelGroup()
				.addComponent(pageIndexDirectPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		.addGroup(layout.createParallelGroup()
				.addComponent(maxPageRowCountDirectPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		.addGroup(layout.createParallelGroup()
				.addComponent(sortPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		//pageIndexDirectPanel
		//maxPageRowCountDirectPanel
		));
		
		layout.setVerticalGroup(layout.createSequentialGroup()
		.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE))
		.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)	
				.addComponent(pageInfoPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)	
				.addComponent(pageButtonPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)	
				.addComponent(pageIndexDirectPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)	
				.addComponent(maxPageRowCountDirectPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(sortPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
	}
	public Border createCompoundBorder() {
		Border compound;
		Border redline = BorderFactory.createLineBorder(Color.GREEN);
		Border raisedbevel = BorderFactory.createRaisedBevelBorder();
		Border loweredbevel = BorderFactory.createLoweredBevelBorder();
		compound = BorderFactory.createCompoundBorder(
                raisedbevel, loweredbevel);
		//Add a red outline to the frame.
		compound = BorderFactory.createCompoundBorder(
		                          redline, compound);
		//Add a title to the red-outlined frame.
		compound = BorderFactory.createTitledBorder(
		                          compound, "Pagination Bar",
		                          TitledBorder.CENTER,
		                          TitledBorder.LEFT);
		return compound;
	}
	
	
	 private void initPageInfoPanel(){	
		 	//logger.info("initPageInfoPanel");
			String totalRows = String.valueOf(paginationContext.getRowCount());
			ClientSettings clientSettings = ClientSettings.getInstance();
			pageInfoPanel = new JPanel();
			BoxLayout boxLayout = new BoxLayout(pageInfoPanel, BoxLayout.LINE_AXIS);			
			pageInfoPanel.setLayout(boxLayout);		
			
			
			pageInfoPanel.applyComponentOrientation(ComponentOrientation.getOrientation(clientSettings.getLocale()));
			 
			
			pageInfoPanel.add(currentPageIndexLabel);
			JSeparator jsp2 = new JSeparator(JSeparator.VERTICAL);
			pageInfoPanel.add(Box.createHorizontalStrut(8));
			pageInfoPanel.add(jsp2);					
					 
			MessageFormat mf = new MessageFormat(SwingLocale.getString("showing_rows"));
			String resultString = mf.format(new Object [] {totalRows});
			pageInfoPanel.add(createLabel(resultString));
			
			JSeparator jsp3 = new JSeparator(JSeparator.VERTICAL);
			pageInfoPanel.add(Box.createHorizontalStrut(8));
			pageInfoPanel.add(jsp3);
			
			//mf = new MessageFormat(SwingLocale.getString("showing_rows_of"));
			//resultString = mf.format(new Object [] {startIndex,endIndex,paginationContext.getTotalRowCount()});
			pageInfoPanel.add(totalRowCountLabel);
			 
			 /*
			pageInfoPanel.add(createLabel(SwingLocale.getString("sequence")));
			pageInfoPanel.add(Box.createHorizontalStrut(2));
			pageInfoPanel.add(currentPageIndexLabel);
			pageInfoPanel.add(Box.createHorizontalStrut(2));
			pageInfoPanel.add(createLabel(SwingLocale.getString("page")));
			pageInfoPanel.add(Box.createHorizontalStrut(5));
			
			pageInfoPanel.add(currentPageRowCountLabel);
			pageInfoPanel.add(Box.createHorizontalStrut(2));
			pageInfoPanel.add(createLabel(SwingLocale.getString("item")));
			
			pageInfoPanel.add(Box.createHorizontalStrut(2));
			pageInfoPanel.add(createLabel("separator.png"));
			pageInfoPanel.add(Box.createHorizontalStrut(2));
			
			pageInfoPanel.add(createLabel(SwingLocale.getString("total")));
			pageInfoPanel.add(Box.createHorizontalStrut(2));
			pageInfoPanel.add(totalPageCountLabel);
			pageInfoPanel.add(Box.createHorizontalStrut(2));
			pageInfoPanel.add(createLabel(SwingLocale.getString("page")));
			
			pageInfoPanel.add(Box.createHorizontalStrut(5));
			
			pageInfoPanel.add(totalRowCountLabel);
			pageInfoPanel.add(Box.createHorizontalStrut(2));
			pageInfoPanel.add(createLabel(SwingLocale.getString("item")));
			pageInfoPanel.add(Box.createHorizontalStrut(5));
			
			pageInfoPanel.add(createLabel(SwingLocale.getString("a_page_has")));
			pageInfoPanel.add(Box.createHorizontalStrut(2));
			pageInfoPanel.add(maxPageRowCountLabel);
			pageInfoPanel.add(Box.createHorizontalStrut(2));
			pageInfoPanel.add(createLabel(SwingLocale.getString("item")+"test"));
			*/
			
		}
	
	
	private void initPageButtonPanel()
	{
		
		//logger.info("initPageButtonPanel");
		pageButtonPanel = new JPanel();
		pageButtonPanel.setLayout(new BoxLayout(pageButtonPanel, BoxLayout.LINE_AXIS));
		
		ClientSettings clientSettings = ClientSettings.getInstance();
		
		pageButtonPanel.applyComponentOrientation(ComponentOrientation
	                .getOrientation(clientSettings.getLocale()));
		
		
		
		pageButtonPanel.add(firstPageButton);
		pageButtonPanel.add(backwardPageButton);
		
		pageIndexDirectTextField.setPreferredSize(new Dimension(50,40));
		pageIndexDirectTextField.setMaximumSize(new Dimension(50,20));
		pageButtonPanel.add(pageIndexDirectTextField);
		pageButtonPanel.add(forwardPageButton);
		pageButtonPanel.add(lastPageButton);
		
		//ButtonManager.updateUI(pageButtonPanel, new Dimension(150, 50));
		
	}
	
	private void initPageIndexDirectPanel()
	{
		//logger.info("initPageIndexDirectPanel");
		pageIndexDirectPanel = new JPanel();
	//	pageIndexDirectPanel.setLayout(new BoxLayout(pageIndexDirectPanel, BoxLayout.X_AXIS));
		
		
		//pageIndexDirectPanel.add(createLabel("jump"));
		//pageIndexDirectPanel.add(Box.createHorizontalStrut(2));
		//pageIndexDirectPanel.add(pageIndexDirectTextField);
		//pageIndexDirectPanel.add(Box.createHorizontalStrut(2));
		//pageIndexDirectPanel.add(createLabel("page"));
		//pageIndexDirectPanel.add(pageIndexDirectButton);
		
		//ButtonManager.updateUI(pageIndexDirectPanel, new Dimension(22, 22));
	}
	
	private void initMaxPageRowCountDirectPanel()
	{
		maxPageRowCountDirectPanel = new JPanel();
		
	}
	
	private void initSortPanel()
	{
		sortPanel = new JPanel();
		sortPanel.setLayout(new BoxLayout(sortPanel, BoxLayout.X_AXIS));
		
		
		//ButtonManager.updateUI(sortPanel, new Dimension(22, 22));
	}
	
	private JLabel createLabel(String text)
	{
		JLabel label = new JLabel(text);
		label.setUI(new BasicLabelUI());
		
		return label;
	}
	
	private JLabel createLabel(Icon icon)
	{
		JLabel label = new JLabel(icon);
		label.setUI(new BasicLabelUI());
		
		return label;
	}
	
	public void setSortNameComboBoxWidth(int width)
	{
		//DimensionManager.setDimension(sortNameComboBox, new Dimension(width, 22));
	}
	
	public void setSortNameComboBoxPopupWidth(int width)
	{
		//sortNameComboBox.setPopupMenuWidth(width);
	}
	
	public void setSortTypeComboBoxWidth(int width)
	{
		//DimensionManager.setDimension(sortTypeComboBox, new Dimension(width, 22));
	}
	
	public void setSortTypeComboBoxPopupWidth(int width)
	{
		//sortTypeComboBox.setPopupMenuWidth(width);
	}
	
	public void setPageIndexDirectTextFieldWidth(int width)
	{
		//DimensionManager.setDimension(pageIndexDirectTextField, new Dimension(width, 22));
	}
	
	public void setMaxPageRowCountDirectTextFieldWidth(int width)
	{
		//DimensionManager.setDimension(maxPageRowCountDirectTextField, new Dimension(width, 22));
	}
	
	private void setCurrentPageIndex(int currentPageIndex){
		//logger.info("setCurrentPageIndex");
		if (currentPageIndex < 1)
		{
			throw new IllegalArgumentException("test");
		}
		MessageFormat mf = new MessageFormat(SwingLocale.getString("page_number_of"));
		String resultString = mf.format(new Object [] {currentPageIndex,paginationContext.getTotalPageCount()});
		pageIndexDirectTextField.setText(String.valueOf(currentPageIndex));
		currentPageIndexLabel.setText(resultString);
	}
	
	private void setCurrentPageRowCount(int currentPageRowCount)
	{
		//logger.info("setCurrentPageRowCount"+currentPageRowCount);
		currentPageRowCountLabel.setText(currentPageRowCount + "");
	}
	
	private void setTotalPageCount(int totalPageCount)
	{
		
		totalPageCountLabel.setText(totalPageCount + "");
	}
	
	private void setTotalRowCount(int totalRowCount)
	{
		MessageFormat mf = new MessageFormat(SwingLocale.getString("showing_rows_of"));
		int startIndex = paginationContext.getStartIndex();
		int endIndex = paginationContext.getEndIndex();
		String resultString = mf.format(new Object [] {startIndex,endIndex,totalRowCount});
		
		totalRowCountLabel.setText(resultString);
	}
	
	private void setMaxPageRowCount(int maxPageRowCount)
	{
		//logger.info("set max page row count:"+maxPageRowCount);
		maxPageRowCountLabel.setText(maxPageRowCount + "");
	}
	
	public void setSortNameList(List sortNameList)
	{
		//sortNameComboBox.setModel(new DefaultComboBoxModel(sortNameList.toArray()));
	}
	

	public void setSortTypeList(List sortTypeList)
	{
		//sortTypeComboBox.setModel(new DefaultComboBoxModel(sortTypeList.toArray()));
	}
	
	
	
	private void setPageIndexDirect(int pageIndexDirect)
	{
		
	}
	
	private void setMaxPageRowCountDirect(int maxPageRowCountDirect)
	{
		
	}
	
	
	
	
	
	
	
	
	public GTable getTable()
	{
		return table;
	}
	
	public void setTable(GTable table)
	{
		this.table = table;
	}
	
	public JList getList()
	{
		return list;
	}
	
	public void setList(JList list)
	{
		this.list = list;
	}
	
	public JPanel getPageInfoPanel()
	{
		return pageInfoPanel;
	}
	
	public JPanel getPageButtonPanel()
	{
		return pageButtonPanel;
	}
	
	public JPanel getPageIndexDirectPanel()
	{
		return pageIndexDirectPanel;
	}
	
	public JPanel getMaxPageRowCountDirectPanel()
	{
		return maxPageRowCountDirectPanel;
	}
	
	public JPanel getSortPanel()
	{
		return sortPanel;
	}
	
	public PaginationContext getPaginationContext()
	{
		return paginationContext;
	}
	
	public void setPaginationContext(PaginationContext paginationContext)
		throws Exception
	{
		if (paginationContext != null)
		{
			this.paginationContext = paginationContext;
			
			updatePagination();
			
			clearRowDatas();
		}
	}
	
	public void updatePagination()
	{
	//	logger.info("currentPage: "+paginationContext.getCurrentPageIndex());
	//	logger.info("Total_Page:"+paginationContext.getTotalPageCount());
	//	logger.info("Total_Row:"+paginationContext.getTotalRowCount());		
		setCurrentPageIndex(paginationContext.getCurrentPageIndex());
		setCurrentPageRowCount(paginationContext.getCurrentPageRowCount());
		setTotalPageCount(paginationContext.getTotalPageCount());
		setTotalRowCount(paginationContext.getTotalRowCount());
		setMaxPageRowCount(paginationContext.getRowCount());
		setPageIndexDirect(paginationContext.getCurrentPageIndex());
		setMaxPageRowCountDirect(paginationContext.getRowCount());
		
	}
	
	public void setPaginationEnabled(boolean enabled)
	{
		firstPageButton.setEnabled(enabled);
		backwardPageButton.setEnabled(enabled);
		forwardPageButton.setEnabled(enabled);
		lastPageButton.setEnabled(enabled);
		
		
	}
	
	public abstract void directRowIndex(int rowIndex)
		throws Exception;
	
	public abstract void directRowCount(int rowCount)
		throws Exception;

	public abstract void clearRowDatas()
		throws Exception;
	public void refresh() {
try {
//	clearRowDatas();
	// jScrollPane1.setViewportView(table.getTable());
	maxPageRowCountDirect = paginationContext.getRowCount();
	directRowCount(maxPageRowCountDirect);
} catch (Exception e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
		
		
		paginationContext.setPageIndex(1);
	}

	
}