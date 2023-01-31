package org.purchasing.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.model.MWarehouse;

public class MForecastLine extends org.compiere.model.MForecastLine{

	public MForecastLine(Properties ctx, int M_ForecastLine_ID, String trxName) {
		super(ctx, M_ForecastLine_ID, trxName);
		// TODO Auto-generated constructor stub
	}
	public MForecastLine (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MForecastLine
	
	protected boolean beforeSave (boolean newRecord)
	{
		if (newRecord 
			|| is_ValueChanged("AD_Org_ID") || is_ValueChanged("M_Warehouse_ID"))
		{	
			MWarehouse wh = MWarehouse.get(getCtx(), getM_Warehouse_ID());
			if (wh.getAD_Org_ID() != getAD_Org_ID())
			{
				//red1 Allow outside Org/Warehouse
				//				throw new WarehouseInvalidForOrgException(wh.getName(), MOrg.get(getCtx(), getAD_Org_ID()).getName());
			}
		}
		return true;
	}
}
