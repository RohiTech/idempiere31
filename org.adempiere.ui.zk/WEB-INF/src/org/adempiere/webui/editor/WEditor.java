/******************************************************************************
 * Product: Posterita Ajax UI 												  *
 * Copyright (C) 2007 Posterita Ltd.  All Rights Reserved.                    *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * Posterita Ltd., 3, Draper Avenue, Quatre Bornes, Mauritius                 *
 * or via info@posterita.org or http://www.posterita.org/                     *
 *****************************************************************************/

package org.adempiere.webui.editor;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import org.adempiere.webui.AdempiereWebUI;
import org.adempiere.webui.LayoutUtils;
import org.adempiere.webui.component.Bandbox;
import org.adempiere.webui.component.Button;
import org.adempiere.webui.component.Datebox;
import org.adempiere.webui.component.DatetimeBox;
import org.adempiere.webui.component.EditorBox;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.NumberBox;
import org.adempiere.webui.component.Paymentbox;
import org.adempiere.webui.event.ContextMenuListener;
import org.adempiere.webui.event.ValueChangeEvent;
import org.adempiere.webui.event.ValueChangeListener;
import org.adempiere.webui.theme.ThemeManager;
import org.adempiere.webui.window.WFieldRecordInfo;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MRole;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.impl.InputElement;
import org.zkoss.zul.impl.XulElement;

/**
 *
 * @author  <a href="mailto:agramdass@gmail.com">Ashley G Ramdass</a>
 * @date    Mar 11, 2007
 * @version $Revision: 0.10 $
 */
public abstract class WEditor implements EventListener<Event>, PropertyChangeListener
{
    private static final String[] lISTENER_EVENTS = {};

    public static final int MAX_DISPLAY_LENGTH = 35;

    protected GridField gridField;

    protected GridTab gridTab;

    protected Label label;

    protected Component component;

    protected boolean mandatory;

    protected ArrayList<ValueChangeListener> listeners = new ArrayList<ValueChangeListener>();

    private String strLabel;

    private String description;

    private boolean readOnly;

    private boolean updateable;

    private String columnName;

	protected WEditorPopupMenu popupMenu;

	private boolean tableEditor;
	
	private boolean isProcessParameter;

	/**
	 * call to show context menu of this field.
	 * must call after append component of this field to parent
	 */
	public void showMenu() {		
		if (popupMenu == null)
			return;
		
		// handle standard menu item (log, preference) reply to data type of this field.
		if (ContextMenuListener.class.isInstance(this))
			popupMenu.addMenuListener((ContextMenuListener)this);
		
		//popupMenu.setId(mField.getColumnName()+"-popup");
		this.component.getParent().appendChild(popupMenu);
		
		// when field have label, add action zoom when click to label, and show menu when right click to label
		if (!readOnly)
		{				
			if (popupMenu.isZoomEnabled() && this instanceof IZoomableEditor)
			{
				// add action zoom when click to label
				label.addEventListener(Events.ON_CLICK, new EventListener<Event> (){
					public void onEvent(Event event) throws Exception {
						if (Events.ON_CLICK.equals(event.getName())) {
							((IZoomableEditor)WEditor.this).actionZoom();
						}

					}
				});
			}

			// show menu when right click to label
			popupMenu.addContextElement(label);
			
			if (component instanceof XulElement) 
			{
				popupMenu.addContextElement((XulElement) component);
			}
		}
		
	}
	
    public WEditor(Component comp, GridField gridField) {
    	this(comp, gridField, -1);
	}

	/**
     *
     * @param comp
     * @param gridField
     */
    public WEditor(Component comp, GridField gridField, int rowIndex)
    {
        if (comp == null)
        {
            throw new IllegalArgumentException("Component cannot be null");
        }

        if (gridField == null)
        {
            throw new IllegalArgumentException("Grid field cannot be null");
        }

        this.setComponent(comp);
        this.gridField = gridField;
        if (gridField.getGridTab() != null) {
        	comp.setWidgetAttribute(AdempiereWebUI.WIDGET_INSTANCE_NAME, gridField.getGridTab().getTableName()+"0"+gridField.getColumnName());
        	this.gridTab = gridField.getGridTab();
        } else {
        	comp.setWidgetAttribute(AdempiereWebUI.WIDGET_INSTANCE_NAME, gridField.getColumnName());
        }
        this.setMandatory(gridField.isMandatory(false));
        this.readOnly = gridField.isReadOnly();
        this.description = gridField.getDescription();
        this.updateable = gridField.isUpdateable();
        this.columnName = gridField.getColumnName();
        this.strLabel = gridField.getHeader();
        init();
    }

    /**
     * Method is used to distinguish between 2 similar WSearchEditors
     *
     */
    public String getDescription()
    {
    	return description;

    }

	/**
	 * Constructor for use if a grid field is unavailable
	 *
	 * @param comp			The editor's component
	 * @param label			column name (not displayed)
	 * @param description	description of component
	 * @param mandatory		whether a selection must be made
	 * @param readonly		whether or not the editor is read only
	 * @param updateable	whether the editor contents can be changed
	 */
    public WEditor(Component comp, String label, String description, boolean mandatory, boolean readonly, boolean updateable)
    {
    	if (comp == null)
        {
            throw new IllegalArgumentException("Component cannot be null");
        }

    	this.setComponent(comp);
    	this.setMandatory(mandatory);
        this.readOnly = readonly;
        this.description = description;
        this.updateable = updateable;
        this.strLabel = label;
        init();
    }

    /**
	 * Constructor for use if a grid field is unavailable
	 *
	 * @param comp			The editor's component
	 * @param label			column name (not displayed)
	 * @param description	description of component
	 * @param mandatory		whether a selection must be made
	 * @param readonly		whether or not the editor is read only
	 * @param updateable	whether the editor contents can be changed
	 */
    public WEditor(Component comp, String columnName, String label, String description, boolean mandatory, boolean readonly, boolean updateable)
    {
    	if (comp == null)
        {
            throw new IllegalArgumentException("Component cannot be null");
        }

    	this.setComponent(comp);
    	this.setMandatory(mandatory);
        this.readOnly = readonly;
        this.description = description;
        this.updateable = updateable;
        this.strLabel = label;
        this.columnName = columnName;
        init();
    }


    /**
     * Set the editor component.
     * @param comp the editor component
     */
    protected void setComponent(Component comp)
    {
        this.component = comp;
    }

    private void init()
    {
        label = new Label("");
        label.setValue(strLabel);
        label.setTooltiptext(description);
        label.setMandatory(mandatory);
        
        this.setMandatory (mandatory);

        if (readOnly || !updateable)
        {
            this.setReadWrite(false);
        }
        else
        {
            this.setReadWrite(true);
        }

        ((HtmlBasedComponent)component).setTooltiptext(description);
        label.setTooltiptext(description);

        //init listeners
        for (String event : this.getEvents())
        {
            component.addEventListener(event, this);
        }
        
        component.setAttribute("idempiere.editor", this);
    }

    /**
     *
     * @return grid field for this editor ( can be null )
     */
    public GridField getGridField()
    {
        return gridField;
    }

    /**
     *
     * @return columnNames
     */
    public String getColumnName()
    {
        return columnName;
    }

    /**
     * Remove the table qualifier from the supplied column name.
     *
     * The column name may be prefixed with the table name
     * i.e. <code>[table name].[column name]</code>.
     * The function returns
     *
     * @param originalColumnName	The column name to clean
     * @return 	the column name with any table qualifier removed
     * 			i.e. <code>[column name]</code>
     */
    protected String cleanColumnName(String originalColumnName)
	{
		String cleanColumnName;
		/*
		 *  The regular expression to use to find the table qualifier.
		 *  Matches "<table name>."
		 */
		final String regexTablePrefix = ".*\\.";

		cleanColumnName = originalColumnName.replaceAll(regexTablePrefix, "");

		return cleanColumnName;
	}

    protected void setColumnName(String columnName)
    {
    	String cleanColumnName = cleanColumnName(columnName);
    	this.columnName = cleanColumnName;
    }

    /**
     *
     * @return Component
     */
    public Component getComponent()
    {
        return component;
    }

    /**
     * @param gridTab
     */
    public void setGridTab(GridTab gridTab)
    {
    	this.gridTab = gridTab;
    }

    /**
     *
     * @return popup menu instance ( if available )
     */
    public WEditorPopupMenu getPopupMenu()
    {
        return popupMenu;
    }

    /**
     * @param evt
     */
    public void propertyChange(final PropertyChangeEvent evt)
    {
        if (evt.getPropertyName().equals(org.compiere.model.GridField.PROPERTY))
        {
        	if (Executions.getCurrent() == null) {
        		Executions.schedule(this.getComponent().getDesktop(), new EventListener<Event>() {
					@Override
					public void onEvent(Event event) throws Exception {
						setValue((evt.getNewValue()));
					}
				}, new Event("onPropertyChange"));
        	} else {
        		setValue((evt.getNewValue()));
        	}
        }
    }

    /**
     * @param listener
     */
    public void addValueChangeListener(ValueChangeListener listener)
    {
    	if (listener == null)
        {
            return;
        }

    	if (!listeners.contains(listener))
    		listeners.add(listener);
    }

    public boolean removeValuechangeListener(ValueChangeListener listener)
    {
    	return listeners.remove(listener);
    }

    protected void fireValueChange(ValueChangeEvent event)
    {
    	//copy to array to avoid concurrent modification exception
    	ValueChangeListener[] vcl = new ValueChangeListener[listeners.size()];
    	listeners.toArray(vcl);
        for (ValueChangeListener listener : vcl)
        {
            listener.valueChange(event);
        }
    }

    /**
     *
     * @return Label
     */
    public Label getLabel()
    {
        return label;
    }

    /**
     *
     * @param readWrite
     */
    public abstract void setReadWrite(boolean readWrite);

    /**
     *
     * @return editable
     */
    public abstract boolean isReadWrite();

    /**
     *
     * @param visible
     */
    public void setVisible(boolean visible)
    {
        label.setVisible(visible);
        component.setVisible(visible);
    }

    /**
     *
     * @return is visible
     */
    public boolean isVisible()
    {
        return component.isVisible();
    }

    public void setBackground(boolean error)
    {

    }

    public void setBackground(Color color)
    {

    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder(30);
        sb.append(this.getClass().getName());
        sb.append("[").append(this.getColumnName());
        sb.append("=");
        sb.append(this.getValue()).append("]");
        return sb.toString();
    }

    /**
     *
     * @param value
     */
    abstract public void setValue(Object value);

    /**
     *
     * @return Object
     */
    abstract public Object getValue();

    /**
     *
     * @return display text
     */
    abstract public String getDisplay();
    
    public String getDisplayTextForGridView(Object value) {
    	this.setValue(value);
    	String s = getDisplay();
    	if ("<0>".equals(s)) {
    		s = "";
    	}
    	return s;
    }

    /**
     *
     * @return list of events
     */
    public String[] getEvents()
    {
        return WEditor.lISTENER_EVENTS;
    }

    /**
     * Set whether the editor represents a mandatory field.
     * @param mandatory whether the field is mandatory
     */
    public void setMandatory (boolean mandatory)
    {
    	if (this.mandatory != mandatory)
    	{
	        this.mandatory = mandatory;
	        if (label != null)
	        {
	        	label.setMandatory(mandatory);
	        }
    	}
    }

    /**
     *
     * @return boolean
     */
    public boolean isMandatory()
    {
        return this.mandatory;
    }

    /**
     * allow subclass to perform dynamic loading of data
     */
    public void dynamicDisplay()
    {
    }

    /**
     * Stretch editor component to fill container
     */
    public void fillHorizontal() {
    	//stretch component to fill grid cell
        if (getComponent() instanceof HtmlBasedComponent) {
        	//can't stretch bandbox & datebox
        	if (!(getComponent() instanceof Bandbox) &&
        		!(getComponent() instanceof Datebox)) {
        		String width = tableEditor ? "96%" : "100%";
        		if (getComponent() instanceof Button) {
        			if (!tableEditor) {
	        			Button btn = (Button) getComponent();
	        			String zclass = btn.getZclass();
	        			if (gridField.getDisplayType() == DisplayType.Image) {
	        				if (!zclass.contains("image-button-field ")) {
	            				btn.setZclass("image-button-field " + zclass);
	        				}
	        			} else if (!zclass.contains("form-button ")) {
	        				btn.setZclass("form-button " + zclass);
	        			}
        			}
        		} else if (getComponent() instanceof Image) {
        			Image image = (Image) getComponent();
        			image.setWidth("24px");
        			image.setHeight("24px");
        		} else {
        			if (!tableEditor) {
	        			if (getComponent() instanceof InputElement) {
	        				((InputElement)getComponent()).setHflex("1");
	        			} else {
	        				((HtmlBasedComponent)getComponent()).setWidth(width);
	        			}
        			} else {
        				if (getComponent() instanceof Combobox) {
        					LayoutUtils.addSclass("grid-combobox-editor", (HtmlBasedComponent)getComponent());
	        			} else {
	        				((HtmlBasedComponent)getComponent()).setWidth(width);
	        			}
        					
        			}
        			
        			if (getComponent() instanceof Textbox && tableEditor) {
        				Textbox textbox = (Textbox) getComponent();
        				if (textbox.isMultiline()) {
        					textbox.setRows(1);
        				}
        			}
        		}
        	}
        }
    }

	public void updateLabelStyle() {				
		if (getLabel() != null) {
			String style = (isZoomable() ? STYLE_ZOOMABLE_LABEL : "") + (isMandatoryStyle() ? STYLE_EMPTY_MANDATORY_LABEL : STYLE_NORMAL_LABEL);
			getLabel().setStyle(style.intern());			
		}
	}
	
	public boolean isMandatoryStyle() {
		return mandatory && !readOnly && (isProcessParameter || getGridField().isEditable(true)) && isNullOrEmpty();
	}

	public boolean isNullOrEmpty() {
		Object value = getValue();
		return value == null || value.toString().trim().length() == 0;
	}
	
	public boolean isZoomable() {
		WEditorPopupMenu menu = getPopupMenu();
		if (menu != null && menu.isZoomEnabled() && this instanceof IZoomableEditor) {
			return true;
		} else {
			return false;
		}
	}

	public void setTableEditor(boolean b) {
		tableEditor = b;
	}

	/**
	 * @return boolean
	 */
	protected boolean isShowPreference() {
		return MRole.getDefault().isShowPreference() && gridField != null && !gridField.isEncrypted() && !gridField.isEncryptedColumn();
	}

	/**
	 * @param popupMenu
	 */
    protected void addChangeLogMenu(WEditorPopupMenu popupMenu) {
		if (popupMenu != null && gridField != null && gridField.getGridTab() != null)
		{
			WFieldRecordInfo.addMenu(popupMenu);
		}
	}

    /**
     * @param popupMenu
     */
	protected void addTextEditorMenu(WEditorPopupMenu popupMenu) {
		Menuitem editor = new Menuitem(Msg.getMsg(Env.getCtx(), "Editor"), ThemeManager.getThemeResource("images/Editor16.png"));
		editor.setAttribute("EVENT", WEditorPopupMenu.EDITOR_EVENT);
		editor.addEventListener(Events.ON_CLICK, popupMenu);
		popupMenu.appendChild(editor);
	}
	
	public boolean isComponentOfEditor(Component comp) {
		if (comp == getComponent())
			return true;
		if (getComponent() instanceof EditorBox)
		{
			EditorBox component = (EditorBox) getComponent();
			if (comp == component.getTextbox())
				return true;
			if (comp == component.getButton())
				return true;
		}
		else if (getComponent() instanceof DatetimeBox)
		{
			DatetimeBox component = (DatetimeBox) getComponent();
			if (comp == component.getDatebox())
				return true;
			if (comp == component.getTimebox())
				return true;
		}
		else if (getComponent() instanceof NumberBox)
		{
			NumberBox component = (NumberBox) getComponent();
			if (comp == component.getDecimalbox())
				return true;
			if (comp == component.getButton())
				return true;
		}
		else if (getComponent() instanceof Paymentbox)
		{
			Paymentbox component = (Paymentbox) getComponent();
			if (comp == component.getCombobox())
				return true;
			if (comp == component.getButton())
				return true;
		}		
		return false;
	}
	
	public boolean isProcessParameter() {
		return isProcessParameter;
	}

	public void setProcessParameter(boolean isProcessParameter) {
		this.isProcessParameter = isProcessParameter;
	}

	/**
	 * return component use for display value in grid view mode in non edit status
	 * if return null, a label will replace.
	 * because each row must has one instance of this component, don't cache it. just create new instance
	 * @return
	 */
	public Component getDisplayComponent() {
		return null;
	}
	
	private static final String STYLE_ZOOMABLE_LABEL = "cursor: pointer; text-decoration: underline;";
	private static final String STYLE_NORMAL_LABEL = "color: #333;";
	private static final String STYLE_EMPTY_MANDATORY_LABEL = "color: red;";
}
