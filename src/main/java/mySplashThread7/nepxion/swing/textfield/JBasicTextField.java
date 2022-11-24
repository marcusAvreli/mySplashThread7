package mySplashThread7.nepxion.swing.textfield;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.Icon;
import javax.swing.JTextField;
import javax.swing.text.Document;

//nepxion-swing/src/com/nepxion/swing/textfield
public class JBasicTextField
	extends JTextField implements ActionListener, FocusListener
{
	private boolean isEnterAcceleratorSupported; // 支持回车切换到下一焦点
	private boolean isSelectionAllSupported; // 支持得到焦点就全选文字
	
	
	
	public JBasicTextField()
	{
		super();
		
		initComponents();
	}
	
	public JBasicTextField(Document doc, String text, int columns)
	{
		super(doc, text, columns);
		
		initComponents();
	}
	
	public JBasicTextField(int columns)
	{
		super(columns);
		
		initComponents();
	}
	
	public JBasicTextField(String text, int columns)
	{
		super(text, columns);
		
		initComponents();
	}
	
	public JBasicTextField(String text)
	{
		super(text);
		
		initComponents();
	}
	
	private void initComponents()
	{
		
		supportEnterAccelerator(true);
		// supportSelectionAll(true);
	}
	
	public boolean isEnterAcceleratorSupported()
	{
		return isEnterAcceleratorSupported;
	}
	
	public void supportEnterAccelerator(boolean isEnterAcceleratorSupported)
	{
		this.isEnterAcceleratorSupported = isEnterAcceleratorSupported;
		
		if (isEnterAcceleratorSupported)
		{
			addActionListener(this);
		}
		else
		{
			removeActionListener(this);
		}
	}
	
	public boolean isSelectionAllSupported()
	{
		return isSelectionAllSupported;
	}
	
	public void supportSelectionAll(boolean isSelectionAllSupported)
	{
		this.isSelectionAllSupported = isSelectionAllSupported;
		
		if (isSelectionAllSupported)
		{
			addFocusListener(this);
		}
		else
		{
			removeFocusListener(this);
		}
	}
	
	public void actionPerformed(ActionEvent e)
	{
		transferFocus();
	}
	
	public void focusGained(FocusEvent e)
	{
		selectAll();
	}
	
	public void focusLost(FocusEvent e)
	{
		
	}
	

	
	
	
	
	
	
	

}