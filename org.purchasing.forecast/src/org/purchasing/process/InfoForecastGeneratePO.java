package org.purchasing.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.compiere.process.SvrProcess;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.purchasing.model.Helper;
import org.purchasing.model.MForecastProcess;
import org.compiere.model.MBPartner;
import org.compiere.model.MBPartnerLocation;
import org.compiere.model.MConversionRate;
import org.compiere.model.MForecast;
import org.compiere.model.MForecastLine;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MOrgInfo;
import org.compiere.model.MProduct;
import org.compiere.model.MProductPO;
import org.compiere.model.MProject;
import org.compiere.model.MProjectLine;
import org.compiere.model.PO;
import org.compiere.model.Query;

public class InfoForecastGeneratePO extends SvrProcess{ 

	@Override
	protected void prepare() {
		//  taken from org.compiere.process.OrderPOCreate
		//but without params (hardcoded)
 	}

	@Override
	protected String doIt() throws Exception {
		//  
		String whereClause = "EXISTS (SELECT T_Selection_ID FROM T_Selection WHERE  T_Selection.AD_PInstance_ID=? " +
						"AND T_Selection.T_Selection_ID=M_ForecastProcess.M_ForecastProcess_ID)";		
				
				List <MForecastProcess> fp = new Query(getCtx(), MForecastProcess.Table_Name, whereClause, get_TrxName())
												.setClient_ID()
												.setParameters(new Object[]{getAD_PInstance_ID()	})
												.list();
				 	  
				Helper help = new Helper(get_TrxName());
				return "@DocumentNo@ " + help.createPOfromForecastProcesses(fp);
	}	
	

}
