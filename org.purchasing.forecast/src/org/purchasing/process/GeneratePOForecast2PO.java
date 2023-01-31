package org.purchasing.process;

import java.util.List;

import org.compiere.process.SvrProcess;

import org.compiere.model.Query;
import org.compiere.util.Env;
import org.purchasing.model.Helper;
import org.purchasing.model.MForecastProcess;

/**
 * Generate Purchase Orders in bulk (refer another class that handles InfoWindow process T_selection records to generate)
 * @author red1
 *
 */
public class GeneratePOForecast2PO extends SvrProcess{

	@Override
	protected void prepare() { 
	}

	@Override
	protected String doIt() throws Exception {
		List<MForecastProcess> forecastprocesses = new Query(Env.getCtx(),MForecastProcess.Table_Name,MForecastProcess.COLUMNNAME_Processed+"=?",get_TrxName())
		.setParameters(false)
		.setOrderBy(MForecastProcess.COLUMNNAME_M_Forecast_ID)
		.list();
		Helper help = new Helper(get_TrxName());
		return "@DocumentNo@ " + help.createPOfromForecastProcesses(forecastprocesses);
	}

}
