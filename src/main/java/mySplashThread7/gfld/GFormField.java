package mySplashThread7.gfld;

import java.util.Vector;

//https://github.com/semantic-web-software/dynagent/tree/master/Elecom
//https://github.com/semantic-web-software/dynagent/blob/a0d356169ef34f3d2422e235fed7866e3dda6d8a/Elecom/src/gdev/gfld/GFormTable.java
public abstract class GFormField implements Comparable
{  protected boolean highlighted = false;
	public boolean isHighlighted() {
		return highlighted;
	}

	public void setHighlighted(boolean highlighted) {
		this.highlighted = highlighted;
	}

	
}
