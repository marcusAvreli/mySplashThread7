package mySplashThread7.gdev.gawt;

import javax.swing.JPanel;

import mySplashThread7.gfld.GFormField;


//dynagent/Elecom/src/gdev/gawt/
public abstract class GComponent extends JPanel
{
	/** Atributo que guarda la información del campo	 */
    protected GFormField m_objFormField;
	public GComponent(GFormField ff)
    {
        setLayout(null);
        setFormField(ff);
    }
    
	 /**
     * Cambia el campo al que referencia este componente de la interfaz
     * @param ff Es el nuevo campo a referenciar
     */
    public void setFormField(GFormField ff)
    {
        m_objFormField = ff;
    }
    /**
     * Obtiene el campo al que hace referencia este componente de la interfaz
     * @return GFormField - El campo con la información del XML.
     */
    public GFormField getFormField()
    {
        return m_objFormField;
    }
}
