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
package org.purchasing.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.compiere.model.*;
import org.compiere.util.KeyNamePair;

/** Generated Interface for M_ForecastProcess
 *  @author iDempiere (generated) 
 *  @version Release 2.1
 */
@SuppressWarnings("all")
public interface I_M_ForecastProcess 
{

    /** TableName=M_ForecastProcess */
    public static final String Table_Name = "M_ForecastProcess";

    /** AD_Table_ID=1000000 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    /** Column name AD_Client_ID */
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";

	/** Get Client.
	  * Client/Tenant for this installation.
	  */
	public int getAD_Client_ID();

    /** Column name AD_Org_ID */
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";

	/** Set Organization.
	  * Organizational entity within client
	  */
	public void setAD_Org_ID (int AD_Org_ID);

	/** Get Organization.
	  * Organizational entity within client
	  */
	public int getAD_Org_ID();

    /** Column name C_OrderLine_ID */
    public static final String COLUMNNAME_C_OrderLine_ID = "C_OrderLine_ID";

	/** Set Sales Order Line.
	  * Sales Order Line
	  */
	public void setC_OrderLine_ID (int C_OrderLine_ID);

	/** Get Sales Order Line.
	  * Sales Order Line
	  */
	public int getC_OrderLine_ID();

	public org.compiere.model.I_C_OrderLine getC_OrderLine() throws RuntimeException;

    /** Column name C_Order_ID */
    public static final String COLUMNNAME_C_Order_ID = "C_Order_ID";

	/** Set Order.
	  * Order
	  */
	public void setC_Order_ID (int C_Order_ID);

	/** Get Order.
	  * Order
	  */
	public int getC_Order_ID();

	public org.compiere.model.I_C_Order getC_Order() throws RuntimeException;

    /** Column name Created */
    public static final String COLUMNNAME_Created = "Created";

	/** Get Created.
	  * Date this record was created
	  */
	public Timestamp getCreated();

    /** Column name CreatedBy */
    public static final String COLUMNNAME_CreatedBy = "CreatedBy";

	/** Get Created By.
	  * User who created this records
	  */
	public int getCreatedBy();

    /** Column name Description */
    public static final String COLUMNNAME_Description = "Description";

	/** Set Description.
	  * Optional short description of the record
	  */
	public void setDescription (String Description);

	/** Get Description.
	  * Optional short description of the record
	  */
	public String getDescription();

    /** Column name IsActive */
    public static final String COLUMNNAME_IsActive = "IsActive";

	/** Set Active.
	  * The record is active in the system
	  */
	public void setIsActive (boolean IsActive);

	/** Get Active.
	  * The record is active in the system
	  */
	public boolean isActive();

    /** Column name M_ForecastLine_ID */
    public static final String COLUMNNAME_M_ForecastLine_ID = "M_ForecastLine_ID";

	/** Set Forecast Line.
	  * Forecast Line
	  */
	public void setM_ForecastLine_ID (int M_ForecastLine_ID);

	/** Get Forecast Line.
	  * Forecast Line
	  */
	public int getM_ForecastLine_ID();

	public org.compiere.model.I_M_ForecastLine getM_ForecastLine() throws RuntimeException;

    /** Column name M_ForecastLine_Sales_ID */
    public static final String COLUMNNAME_M_ForecastLine_Sales_ID = "M_ForecastLine_Sales_ID";

	/** Set Forecast Line Sales	  */
	public void setM_ForecastLine_Sales_ID (int M_ForecastLine_Sales_ID);

	/** Get Forecast Line Sales	  */
	public int getM_ForecastLine_Sales_ID();

	public org.compiere.model.I_C_OrderLine getM_ForecastLine_Sales() throws RuntimeException;

    /** Column name M_ForecastPOLine_ID */
    public static final String COLUMNNAME_M_ForecastPOLine_ID = "M_ForecastPOLine_ID";

	/** Set M_ForecastPOLine_ID.
	  * Generated PO Line
	  */
	public void setM_ForecastPOLine_ID (int M_ForecastPOLine_ID);

	/** Get M_ForecastPOLine_ID.
	  * Generated PO Line
	  */
	public int getM_ForecastPOLine_ID();

	public org.compiere.model.I_C_OrderLine getM_ForecastPOLine() throws RuntimeException;

    /** Column name M_ForecastProcess_ID */
    public static final String COLUMNNAME_M_ForecastProcess_ID = "M_ForecastProcess_ID";

	/** Set ForecastProcess	  */
	public void setM_ForecastProcess_ID (int M_ForecastProcess_ID);

	/** Get ForecastProcess	  */
	public int getM_ForecastProcess_ID();

    /** Column name M_ForecastProcess_UU */
    public static final String COLUMNNAME_M_ForecastProcess_UU = "M_ForecastProcess_UU";

	/** Set M_ForecastProcess_UU	  */
	public void setM_ForecastProcess_UU (String M_ForecastProcess_UU);

	/** Get M_ForecastProcess_UU	  */
	public String getM_ForecastProcess_UU();

    /** Column name M_Forecast_ID */
    public static final String COLUMNNAME_M_Forecast_ID = "M_Forecast_ID";

	/** Set Sales Forecast.
	  * Material Forecast
	  */
	public void setM_Forecast_ID (int M_Forecast_ID);

	/** Get Sales Forecast.
	  * Material Forecast
	  */
	public int getM_Forecast_ID();

	public org.compiere.model.I_M_Forecast getM_Forecast() throws RuntimeException;

    /** Column name Processed */
    public static final String COLUMNNAME_Processed = "Processed";

	/** Set Processed.
	  * The document has been processed
	  */
	public void setProcessed (boolean Processed);

	/** Get Processed.
	  * The document has been processed
	  */
	public boolean isProcessed();

    /** Column name PurchaseLeadTime */
    public static final String COLUMNNAME_PurchaseLeadTime = "PurchaseLeadTime";

	/** Set PurchaseLeadTime.
	  * Lead time to purchase item before Promised Date or Order
	  */
	public void setPurchaseLeadTime (Timestamp PurchaseLeadTime);

	/** Get PurchaseLeadTime.
	  * Lead time to purchase item before Promised Date or Order
	  */
	public Timestamp getPurchaseLeadTime();

    /** Column name Updated */
    public static final String COLUMNNAME_Updated = "Updated";

	/** Get Updated.
	  * Date this record was updated
	  */
	public Timestamp getUpdated();

    /** Column name UpdatedBy */
    public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

	/** Get Updated By.
	  * User who updated this records
	  */
	public int getUpdatedBy();
}
