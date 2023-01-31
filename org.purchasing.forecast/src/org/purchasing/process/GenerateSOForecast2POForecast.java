package org.purchasing.process;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.compiere.process.SvrProcess;

import org.compiere.model.MCalendar;
import org.compiere.model.MForecast;
import org.compiere.model.MForecastLine;
import org.compiere.model.MPeriod;
import org.compiere.model.Query;
import org.compiere.util.Env;
import org.purchasing.model.Helper;
import org.purchasing.model.MForecastProcess;

public class GenerateSOForecast2POForecast extends SvrProcess{

	@Override
	protected void prepare() {
		/** TODO define these params in AD
		 * @param C_Period_ID
		 * @param M_Product_ID
		 * @param m_M_Warehouse_ID
		 * @param C_BPartner_ID
		 * @param AD_Org_ID
		 */
	}

	@Override
	protected String doIt() throws Exception {
		//Get only SO forecastlines
		List<MForecastLine> SOforecastlines = getForecastLines();
		//iterate and generate PO, order by Forecast header
		int ID = 0;
		int cnt=0;
		MCalendar calendar = MCalendar.getDefault(Env.getCtx());
		//good enough setting of Period from line level dating
		MForecast poforecast = null;
		for (MForecastLine soforecastline:SOforecastlines){
			MPeriod period = MPeriod.findByCalendar(Env.getCtx(), soforecastline.getDatePromised(), calendar.getC_Calendar_ID(), get_TrxName());
			if (ID!=soforecastline.getM_Forecast_ID()){
				
				//create new PO Forecast header
				poforecast = new MForecast(Env.getCtx(),0,get_TrxName());
				poforecast.setName("PO Forecast from SalesForecast"); 
				poforecast.setC_Calendar_ID(calendar.getC_Calendar_ID());
				poforecast.setC_Year_ID(period.getC_Year_ID());
				poforecast.saveEx(get_TrxName());
				ID = soforecastline.getM_Forecast_ID();
			}
			BigDecimal qty = soforecastline.getQty();
			int product = soforecastline.getM_Product_ID();
			int M_Warehouse_ID = soforecastline.getM_Warehouse_ID(); 
			Timestamp DatePromised = soforecastline.getDatePromised(); 
			int AD_Org_ID = soforecastline.getAD_Org_ID();
			Helper help = new Helper(get_TrxName());
			
			//helper method to do the rest, null as not Orderline
			cnt = cnt + help.handleNewForecastLine(AD_Org_ID, null,period, poforecast.getM_Forecast_ID(), M_Warehouse_ID,soforecastline.getM_ForecastLine_ID(), qty, product,
					0, DatePromised);
		}
		 return "New Purchase Forecasts: " + cnt;
	}

	
	/**
	 * Only Forecast Lines without Process sub detail are SO Forecast
	 * @param C_Period_ID
	 * @param M_Product_ID
	 * @param m_M_Warehouse_ID
	 * @param C_BPartner_ID
	 * @param AD_Org_ID
	 * @return
	 */
	private List<MForecastLine> getForecastLines() {
		List<MForecastLine> forecastlines = new Query(Env.getCtx(),MForecastLine.Table_Name,"",get_TrxName())
		.setOrderBy(MForecastLine.COLUMNNAME_M_Forecast_ID)
		.list();
		int i=0;
		List<MForecastLine> list = new ArrayList();
		for (MForecastLine forecastline:forecastlines){
			MForecastProcess process = new Query(Env.getCtx(),MForecastProcess.Table_Name, MForecastProcess.COLUMNNAME_M_ForecastLine_Sales_ID+"=? OR "
					+ MForecastProcess.COLUMNNAME_M_ForecastLine_ID+"=?",get_TrxName())
			.setParameters(forecastline.getM_ForecastLine_ID(),forecastline.getM_ForecastLine_ID()).first();
			if (process==null) { 
				list.add(forecastline);
			} 
		}
		return list;
	}
	
}
