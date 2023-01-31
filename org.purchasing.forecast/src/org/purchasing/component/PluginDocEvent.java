/******************************************************************************
 * Product: iDempiere Free ERP Project based on Compiere (2006)               *
 * Copyright (C) 2014 Redhuan D. Oon All Rights Reserved.                     *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 *  FOR NON-COMMERCIAL DEVELOPER USE ONLY                                     *
 *  @author Redhuan D. Oon  - red1@red1.org  www.red1.org                     *
 *****************************************************************************/

package org.purchasing.component;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;

import org.adempiere.base.event.AbstractEventHandler;
import org.adempiere.base.event.IEventTopics;
import org.adempiere.base.event.LoginEventData;
import org.compiere.model.MCalendar;
import org.compiere.model.MDocType;
import org.compiere.model.MForecast;
import org.compiere.model.MForecastLine;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MPeriod;
import org.compiere.model.MProductPO;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.osgi.service.event.Event;
import org.purchasing.model.Helper;
import org.purchasing.model.MForecastProcess;

/**
 *  @author red1
 */
public class PluginDocEvent extends AbstractEventHandler{
	private static CLogger log = CLogger.getCLogger(PluginDocEvent.class);
	private String trxName = "";
	private PO po = null; 
	@Override
	protected void initialize() { 
	//register EventTopics and TableNames 
		registerTableEvent(IEventTopics.DOC_AFTER_COMPLETE, MOrder.Table_Name);
		registerTableEvent(IEventTopics.DOC_AFTER_VOID, MOrder.Table_Name);
		log.info("<EVENT VALIDATOR FOR SALES ORDER TO GENERATE PURCHASING FORECAST> .. IS NOW INITIALIZED");
		}

	@Override
	protected void doHandleEvent(Event event) {
		setPo(getPO(event));
		setTrxName(po.get_TrxName());
		
		String type = event.getTopic();
		//testing that it works at login
		if (type.equals(IEventTopics.AFTER_LOGIN)) {
			LoginEventData eventData = getEventData(event);
			log.fine(" topic="+event.getTopic()+" AD_Client_ID="+eventData.getAD_Client_ID()
					+" AD_Org_ID="+eventData.getAD_Org_ID()+" AD_Role_ID="+eventData.getAD_Role_ID()
					+" AD_User_ID="+eventData.getAD_User_ID());
			}
		else 
		{
			handlePOForecast(po, type);
			
			log.info(" topic="+event.getTopic()+" po="+po);
		}
	}
	
	/**
	 * Handle in full Forecast model creation
	 * @param po
	 * @param event
	 */
	private void handlePOForecast(PO po, String type){
		if (po instanceof MOrder){
			MOrder order = (MOrder)po;
			if (order.isSOTrx()){
				String where = MForecastProcess.COLUMNNAME_C_OrderLine_ID+"=?";
				MForecast forecast = null;
				MCalendar calendar = MCalendar.getDefault(Env.getCtx(), order.getAD_Client_ID());
				MPeriod period = MPeriod.findByCalendar(Env.getCtx(), order.getDatePromised(), calendar.getC_Calendar_ID(), trxName);

				//check if already existed in ForecastProcess
				MForecastProcess oldforecastprocess = new Query (Env.getCtx(),MForecastProcess.Table_Name,MForecastProcess.COLUMNNAME_C_Order_ID+"=?",trxName)
				.setParameters(order.getC_Order_ID()).first();
				if (oldforecastprocess!=null && !oldforecastprocess.isProcessed()){ //You have not done anything to it, it is just a draft
					//delete (renew) all forecastlines/process
					List <MForecastProcess> processlines = new Query(Env.getCtx(),MForecastProcess.Table_Name,MForecastProcess.COLUMNNAME_C_Order_ID+"=?",trxName)
					.setParameters(order.getC_Order_ID()).list();
					for (MForecastProcess processline:processlines){
						MForecastLine forecastline = (MForecastLine) processline.getM_ForecastLine();
						processline.delete(true); 
						forecastline.delete(true);
					}
					//do not delete old Forecast header but reuse for creation of new children
					forecast = new MForecast(Env.getCtx(),oldforecastprocess.getM_Forecast_ID(),trxName);				
				} else{
					forecast = new MForecast(Env.getCtx(),0,trxName);//or new forecast header
				}
				forecast.setName("PURCHASE FORECAST FOR S/O "+order.getDocumentNo()); 
				forecast.setC_Calendar_ID(calendar.getC_Calendar_ID());
				forecast.setC_Year_ID(period.getC_Year_ID());
				forecast.saveEx(trxName);
				
				//When a Sales Order is made VOID do not delete but made inActive
				if (oldforecastprocess!=null && type==IEventTopics.DOC_AFTER_VOID){
					oldforecastprocess.setIsActive(false);
					oldforecastprocess.saveEx(trxName);
					
				//Sales Order is made COMPLETE
				} else if (type==IEventTopics.DOC_AFTER_COMPLETE) {
					//create new children ForecastProcess either case
					MOrderLine[] orderlines = order.getLines();
					for (MOrderLine orderline:orderlines){
						//obtain elements from orderline 
						BigDecimal qty = orderline.getQtyOrdered();
						int product = orderline.getM_Product_ID();
						int M_Warehouse_ID = orderline.getM_Warehouse_ID();
						int M_AttributeSetInstance_ID = orderline.getM_AttributeSetInstance_ID();
						int AD_Org_ID = orderline.getAD_Org_ID();
						Timestamp DatePromised = orderline.getDatePromised(); 
						
						//create ForecastLine as child of Forecast
						Helper help = new Helper(trxName);					
						help.handleNewForecastLine(AD_Org_ID, orderline, period, forecast.getM_Forecast_ID(), M_Warehouse_ID,0, qty, product, M_AttributeSetInstance_ID, DatePromised);
					}
				}
			}
			

		}
	}

 
	
	private void setPo(PO eventPO) {
		 po = eventPO;
	}

	private void setTrxName(String get_TrxName) {
		trxName = get_TrxName;		
	}
}
