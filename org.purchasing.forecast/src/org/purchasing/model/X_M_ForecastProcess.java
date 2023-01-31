/******************************************************************************
 * Product: iDempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2012 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
/** Generated Model - DO NOT CHANGE */
package org.purchasing.model;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.model.*;

/** Generated Model for M_ForecastProcess
 *  @author iDempiere (generated) 
 *  @version Release 2.1 - $Id$ */
public class X_M_ForecastProcess extends PO implements I_M_ForecastProcess, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20150906L;

    /** Standard Constructor */
    public X_M_ForecastProcess (Properties ctx, int M_ForecastProcess_ID, String trxName)
    {
      super (ctx, M_ForecastProcess_ID, trxName);
      /** if (M_ForecastProcess_ID == 0)
        {
			setM_ForecastProcess_ID (0);
        } */
    }

    /** Load Constructor */
    public X_M_ForecastProcess (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 3 - Client - Org 
      */
    protected int get_AccessLevel()
    {
      return accessLevel.intValue();
    }

    /** Load Meta Data */
    protected POInfo initPO (Properties ctx)
    {
      POInfo poi = POInfo.getPOInfo (ctx, Table_ID, get_TrxName());
      return poi;
    }

    public String toString()
    {
      StringBuffer sb = new StringBuffer ("X_M_ForecastProcess[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public org.compiere.model.I_C_OrderLine getC_OrderLine() throws RuntimeException
    {
		return (org.compiere.model.I_C_OrderLine)MTable.get(getCtx(), org.compiere.model.I_C_OrderLine.Table_Name)
			.getPO(getC_OrderLine_ID(), get_TrxName());	}

	/** Set Sales Order Line.
		@param C_OrderLine_ID 
		Sales Order Line
	  */
	public void setC_OrderLine_ID (int C_OrderLine_ID)
	{
		if (C_OrderLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_OrderLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_OrderLine_ID, Integer.valueOf(C_OrderLine_ID));
	}

	/** Get Sales Order Line.
		@return Sales Order Line
	  */
	public int getC_OrderLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_OrderLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Order getC_Order() throws RuntimeException
    {
		return (org.compiere.model.I_C_Order)MTable.get(getCtx(), org.compiere.model.I_C_Order.Table_Name)
			.getPO(getC_Order_ID(), get_TrxName());	}

	/** Set Order.
		@param C_Order_ID 
		Order
	  */
	public void setC_Order_ID (int C_Order_ID)
	{
		if (C_Order_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_Order_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_Order_ID, Integer.valueOf(C_Order_ID));
	}

	/** Get Order.
		@return Order
	  */
	public int getC_Order_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Order_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Description.
		@param Description 
		Optional short description of the record
	  */
	public void setDescription (String Description)
	{
		set_Value (COLUMNNAME_Description, Description);
	}

	/** Get Description.
		@return Optional short description of the record
	  */
	public String getDescription () 
	{
		return (String)get_Value(COLUMNNAME_Description);
	}

	public org.compiere.model.I_M_ForecastLine getM_ForecastLine() throws RuntimeException
    {
		return (org.compiere.model.I_M_ForecastLine)MTable.get(getCtx(), org.compiere.model.I_M_ForecastLine.Table_Name)
			.getPO(getM_ForecastLine_ID(), get_TrxName());	}

	/** Set Forecast Line.
		@param M_ForecastLine_ID 
		Forecast Line
	  */
	public void setM_ForecastLine_ID (int M_ForecastLine_ID)
	{
		if (M_ForecastLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_M_ForecastLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_M_ForecastLine_ID, Integer.valueOf(M_ForecastLine_ID));
	}

	/** Get Forecast Line.
		@return Forecast Line
	  */
	public int getM_ForecastLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_ForecastLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_OrderLine getM_ForecastLine_Sales() throws RuntimeException
    {
		return (org.compiere.model.I_C_OrderLine)MTable.get(getCtx(), org.compiere.model.I_C_OrderLine.Table_Name)
			.getPO(getM_ForecastLine_Sales_ID(), get_TrxName());	}

	/** Set Forecast Line Sales.
		@param M_ForecastLine_Sales_ID Forecast Line Sales	  */
	public void setM_ForecastLine_Sales_ID (int M_ForecastLine_Sales_ID)
	{
		if (M_ForecastLine_Sales_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_M_ForecastLine_Sales_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_M_ForecastLine_Sales_ID, Integer.valueOf(M_ForecastLine_Sales_ID));
	}

	/** Get Forecast Line Sales.
		@return Forecast Line Sales	  */
	public int getM_ForecastLine_Sales_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_ForecastLine_Sales_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_OrderLine getM_ForecastPOLine() throws RuntimeException
    {
		return (org.compiere.model.I_C_OrderLine)MTable.get(getCtx(), org.compiere.model.I_C_OrderLine.Table_Name)
			.getPO(getM_ForecastPOLine_ID(), get_TrxName());	}

	/** Set M_ForecastPOLine_ID.
		@param M_ForecastPOLine_ID 
		Generated PO Line
	  */
	public void setM_ForecastPOLine_ID (int M_ForecastPOLine_ID)
	{
		if (M_ForecastPOLine_ID < 1) 
			set_Value (COLUMNNAME_M_ForecastPOLine_ID, null);
		else 
			set_Value (COLUMNNAME_M_ForecastPOLine_ID, Integer.valueOf(M_ForecastPOLine_ID));
	}

	/** Get M_ForecastPOLine_ID.
		@return Generated PO Line
	  */
	public int getM_ForecastPOLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_ForecastPOLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set ForecastProcess.
		@param M_ForecastProcess_ID ForecastProcess	  */
	public void setM_ForecastProcess_ID (int M_ForecastProcess_ID)
	{
		if (M_ForecastProcess_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_M_ForecastProcess_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_M_ForecastProcess_ID, Integer.valueOf(M_ForecastProcess_ID));
	}

	/** Get ForecastProcess.
		@return ForecastProcess	  */
	public int getM_ForecastProcess_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_ForecastProcess_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set M_ForecastProcess_UU.
		@param M_ForecastProcess_UU M_ForecastProcess_UU	  */
	public void setM_ForecastProcess_UU (String M_ForecastProcess_UU)
	{
		set_Value (COLUMNNAME_M_ForecastProcess_UU, M_ForecastProcess_UU);
	}

	/** Get M_ForecastProcess_UU.
		@return M_ForecastProcess_UU	  */
	public String getM_ForecastProcess_UU () 
	{
		return (String)get_Value(COLUMNNAME_M_ForecastProcess_UU);
	}

	public org.compiere.model.I_M_Forecast getM_Forecast() throws RuntimeException
    {
		return (org.compiere.model.I_M_Forecast)MTable.get(getCtx(), org.compiere.model.I_M_Forecast.Table_Name)
			.getPO(getM_Forecast_ID(), get_TrxName());	}

	/** Set Sales Forecast.
		@param M_Forecast_ID 
		Material Forecast
	  */
	public void setM_Forecast_ID (int M_Forecast_ID)
	{
		if (M_Forecast_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_M_Forecast_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_M_Forecast_ID, Integer.valueOf(M_Forecast_ID));
	}

	/** Get Sales Forecast.
		@return Material Forecast
	  */
	public int getM_Forecast_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Forecast_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Processed.
		@param Processed 
		The document has been processed
	  */
	public void setProcessed (boolean Processed)
	{
		set_Value (COLUMNNAME_Processed, Boolean.valueOf(Processed));
	}

	/** Get Processed.
		@return The document has been processed
	  */
	public boolean isProcessed () 
	{
		Object oo = get_Value(COLUMNNAME_Processed);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set PurchaseLeadTime.
		@param PurchaseLeadTime 
		Lead time to purchase item before Promised Date or Order
	  */
	public void setPurchaseLeadTime (Timestamp PurchaseLeadTime)
	{
		set_Value (COLUMNNAME_PurchaseLeadTime, PurchaseLeadTime);
	}

	/** Get PurchaseLeadTime.
		@return Lead time to purchase item before Promised Date or Order
	  */
	public Timestamp getPurchaseLeadTime () 
	{
		return (Timestamp)get_Value(COLUMNNAME_PurchaseLeadTime);
	}
}