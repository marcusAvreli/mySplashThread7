package mySplashThread7.nepxion.swing.textfield;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;



public class JNumberTextField
extends JBasicTextField
{
public JNumberTextField()
{
	this(Double.MIN_VALUE);
}

public JNumberTextField(double minimumValue)
{
	this(minimumValue, Double.MAX_VALUE);
}

public JNumberTextField(int maximumLength, int decimalLength)
{
	this(maximumLength, decimalLength, Double.MIN_VALUE, Double.MAX_VALUE);
}

public JNumberTextField(double minimumValue, double maximumValue)
{
	this(NumberDocument.MAXIMUM_LENGTH, 0, minimumValue, maximumValue);
}

public JNumberTextField(int maximumLength, int decimalLength, double minimumValue, double maximumValue)
{
	super();
	
	setDocument(new NumberDocument(maximumLength, decimalLength, minimumValue, maximumValue)
	{
		public void insertString(int offset, String value, AttributeSet attributeSet)
			throws BadLocationException
		{
			boolean isExclude = exclude(value);
			if (isExclude)
			{
				return;
			}
			
			try
			{
				super.insertString(offset, value, attributeSet);
			}
			catch (NumberFormatException e)
			{
				
			}
			catch (IllegalArgumentException e)
			{
				
			}
		}
	}
	);
}

public boolean exclude(String value)
{
	return false;
}
}
