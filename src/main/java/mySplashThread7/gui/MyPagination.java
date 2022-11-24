package mySplashThread7.gui;

import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import mySplashThread7.gdev.gawt.GTable;

public  class MyPagination extends JPanel{

	private JScrollPane scrollPane;
	private GTable gtable;
	private JPanel jpanel;
	public MyPagination() {
		jpanel = new JPanel();
	}
	
	public GTable getGtable() {
		return gtable;
	}

	public void setGtable(GTable gtable) {
		this.gtable = gtable;
	}

	public void setScrollPane(JScrollPane scrollPane) {
		this.scrollPane = scrollPane;
	}
	public void buildPagination() {
		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						// .addGap(10, 10, 10)
						//.addComponent(bar).addGap(10, 10, 10)
						.addComponent(scrollPane).addGap(10, 10, 10)
				// .addComponent(jlabel)

				));
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				// .addComponent(jlabel,GroupLayout.DEFAULT_SIZE,GroupLayout.DEFAULT_SIZE,
				// Short.MAX_VALUE)
				//.addComponent(bar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				);

	}
	/*
	public void runTroughPages() {
		this.gtable.cutTable(10, 15);
		
		
	}
	*/
}
