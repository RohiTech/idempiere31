package org.purchasing.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;

import org.compiere.model.MBPartner;
import org.compiere.model.MConversionRate;
import org.compiere.model.MDocType; 
import org.purchasing.model.MForecastLine;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MPeriod;
import org.compiere.model.MProductPO;
import org.compiere.util.CLogger;
import org.compiere.util.DB; 
import org.compiere.util.Env;


/**
 * Helper class to house utils of plugin
 * @author red1
 *
 */
public class Helper {
	String trxName = "";
	private static CLogger log = CLogger.getCLogger(Helper.class);
	/** Consolidate Document		*/
	private boolean		m_ConsolidateDocument = true;
	/** List of POs for Consolidation	*/
	private ArrayList<MOrder>	m_pos = new ArrayList<MOrder>();
	String box = "";

	public Helper(String trxName){
		this.trxName = trxName;
	}
	/**
	 * Adjusting the qty to the DatePromised scenario of further ordered vs reserved qtys
	 * 
	 * @param DatePromised
	 * @param product
	 * @param m_M_Warehouse_ID
	 * @return
	 */
	public BigDecimal getReserveOrders(int sameID, Timestamp DatePromised, int product, int m_M_Warehouse_ID, int m_M_AttributeSetInstance_ID) {
		double qty = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		//	Orders
		sql = "SELECT o.DatePromised, ol.QtyReserved, b.DocBaseType "			 
			+ "FROM C_Order o"
			+ " INNER JOIN C_OrderLine ol ON (o.C_Order_ID=ol.C_Order_ID) "
			+	"INNER JOIN C_DocType b ON (o.C_DocType_ID=b.C_DocType_ID) "
			+ "WHERE ol.QtyReserved<>0"
			+ " AND ol.M_Product_ID=?";
		if (DatePromised != null)
			sql += " AND ol.DatePromised<=?";//on or before required date to be valid
		if (m_M_Warehouse_ID > 0)
			sql += " AND o.M_Warehouse_ID="+m_M_Warehouse_ID;
		if (m_M_AttributeSetInstance_ID >0)
			sql += " AND ol.m_M_AttributeSetInstance_ID="+m_M_AttributeSetInstance_ID;
		if (sameID>0)
			sql +=" AND C_OrderLine_ID <> "+sameID;//avoiding same orderline been counted
			//this will slate those after as redundant TODO by an InfoWindow to manage i.e. cancel option
		sql += "ORDER BY o.DatePromised";
		try
		{
			pstmt = DB.prepareStatement(sql, trxName);
			pstmt.setInt(1, product);
			if (DatePromised != null)
				pstmt.setTimestamp(2, DatePromised); 
			rs = pstmt.executeQuery();
			while (rs.next())
			{ 
				double oq = rs.getDouble(2);
				String DocBaseType = rs.getString(3); 
				if (MDocType.DOCBASETYPE_PurchaseOrder.equals(DocBaseType))
				{ 
					qty -= oq;  //purchase lessen the gap
				}
				else
				{ //TODO are you sure you are not recursive processing previous processed SOs?
					qty += oq; //sales increases the gap
				} 
 			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		return new BigDecimal(qty);
	}
	/**Factor lead time into Forecast DatePromised ability to deliver
	 * Product Purchasing Lead Time (DeliveryTime_Promised)
	 * Compare with transaction's DatePromised if its later
	 * thanks to: http://stackoverflow.com/questions/7450045/how-do-i-increment-a-java-sql-timestamp-by-14-days
	 */
	public Timestamp deliveryTimePromised(int product, Timestamp orderDatePromised) {
		//use buffer time to compare if transaction already set later promised date
		Timestamp leadTime = new Timestamp(0);
		MProductPO[] purchasing = MProductPO.getOfProduct(Env.getCtx(), product, trxName);
		if (purchasing.length>0){
			int delivery = purchasing[0].getDeliveryTime_Promised();
			if (purchasing.length>1)
				log.warning("WARNING: Product Purchasing records more than one! Delivery time only from first vendor.");
			//
			Calendar cal = Calendar.getInstance();
			cal.setTime(orderDatePromised);
			cal.add(Calendar.DAY_OF_WEEK, -delivery);
			leadTime.setTime(cal.getTime().getTime());
 
		}
		return leadTime;
	}
	
	/**
	 * Helper method to handle Forecastprocesses conversion to Purchase Orders for Forecast
	 */
	public String createPOfromForecastProcesses(List<MForecastProcess> fp){
		int ID = 0;
 		MOrder purchaseorder = null; 
		for (MForecastProcess forecastprocess : fp)
		{  
			//adapted from *public class ProjectGenPO*
			createPO(forecastprocess);

		} 
		//box is array of processed purchase Document no.
		return box;	
	}
	
	/**
	 * 	Create PO from Detail Amt/Qty
	 * Set DateOrdered from ForecastProcess.LeadTime
	 * 	@param forecastproject
	 */
	private String createPO (MForecastProcess forecastprocess)
	{
		MForecastLine forecastline = (MForecastLine)forecastprocess.getM_ForecastLine();
		if (forecastline.getM_Product_ID() == 0)
		{
			log.severe(forecastline.get_ID()+ " Line has no Product");
			return null;
		}
		if (forecastprocess.isProcessed())
		{
			log.severe(forecastline.get_ID()+ " Line was ordered previously");
			return null;
		}

		//	PO Record
		MProductPO[] pos = MProductPO.getOfProduct(Env.getCtx(), forecastline.getM_Product_ID(), trxName);
		if (pos == null || pos.length == 0)
		{
			log.severe(forecastline.get_ID()+ " Product has no PO record");
			return null;
		}

		//	Create to Order
		MOrder order = null;
		//	try to find PO to C_BPartner
		for (int i = 0; i < m_pos.size(); i++)
		{
			MOrder test = (MOrder)m_pos.get(i);
			if (test.getC_BPartner_ID() == pos[0].getC_BPartner_ID())
			{
				order = test;
				break;
			}
		}
		if (order == null)	//	create new Order
		{
			//	Vendor
			MBPartner bp = new MBPartner (Env.getCtx(), pos[0].getC_BPartner_ID(), trxName);
			//	New Order
			order = new MOrder (Env.getCtx(), 0, trxName);
			order.setIsSOTrx(false);
			order.setM_Warehouse_ID(forecastline.getM_Warehouse_ID());
			order.setC_DocTypeTarget_ID();
			int AD_Org_ID = forecastline.getAD_Org_ID();
			if (AD_Org_ID == 0)
			{
				log.warning("createPOfromDetailLine - AD_Org_ID=0");
				AD_Org_ID = Env.getAD_Org_ID(Env.getCtx());	
				if (AD_Org_ID != 0)
					forecastline.setAD_Org_ID(AD_Org_ID);
			}
			order.setClientOrg (forecastline.getAD_Client_ID (), AD_Org_ID);
			order.setBPartner (bp);
			order.saveEx(trxName);
			//	optionally save for consolidation
			if (m_ConsolidateDocument)
				m_pos.add(order);
			if (box.length()>0)
				box = box+", ";
			box = box+order.getDocumentNo();	
		}

		//	Create Line
		MOrderLine orderLine = new MOrderLine (order);
		orderLine.setM_Product_ID(forecastline.getM_Product_ID(), true);
		orderLine.setQty(forecastline.getQty()); 
		orderLine.setDescription("FORECAST GENERATED");
		orderLine.setDatePromised(forecastline.getDatePromised());
		orderLine.setDateOrdered(forecastprocess.getPurchaseLeadTime());

		//	(Vendor) PriceList Price
		orderLine.setPrice();
		if (orderLine.getPriceActual().signum() == 0)
		{
			//	Try to find purchase price
			BigDecimal poPrice = pos[0].getPricePO();
			int C_Currency_ID = pos[0].getC_Currency_ID();
			//
			if (poPrice == null || poPrice.signum() == 0)
				poPrice = pos[0].getPriceLastPO();
			if (poPrice == null || poPrice.signum() == 0)
				poPrice = pos[0].getPriceList();
			//	We have a price
			if (poPrice != null && poPrice.signum() != 0)
			{
				if (order.getC_Currency_ID() != C_Currency_ID)
					poPrice = MConversionRate.convert(Env.getCtx(), poPrice, 
						C_Currency_ID, order.getC_Currency_ID(), 
						order.getDateAcct(), order.getC_ConversionType_ID(), 
						order.getAD_Client_ID(), order.getAD_Org_ID());
				orderLine.setPrice(poPrice);
			}
		}
		
		orderLine.setTax();
		orderLine.saveEx(trxName);

		//	update LineProcess
		forecastprocess.setProcessed(true);
		// with new POline for traceability
		forecastprocess.setM_ForecastPOLine_ID(orderLine.get_ID());;
		
		forecastprocess.saveEx(); //TODO check qty below
		log.fine(forecastline.get_ID() + forecastline.getQty().toString() + order.getDocumentNo());
		
		return box;
	}	//	createPOfromDetailLine
	
	public int handleNewForecastLine(int AD_Org_ID, MOrderLine orderline, MPeriod period,
			int M_Forecast_ID, int M_Warehouse_ID, int M_ForecastLine_ID, BigDecimal qty, int product,
			int M_AttributeSetInstance_ID, Timestamp DatePromised) { 
		//create ForecastLine as child of Forecast 
		MForecastLine forecastline = new MForecastLine(Env.getCtx(),0,trxName);
		forecastline.setM_Forecast_ID(M_Forecast_ID);
		forecastline.setAD_Org_ID(AD_Org_ID);
		int cnt = 0;
		//DatePromised remains same throughout even till PO. only lead time (order date) is changed (see further below).
		forecastline.setDatePromised(DatePromised);		
		//check qty not to be zero
		qty = qty.add(getReserveOrders(orderline!=null?orderline.get_ID():0, DatePromised, product, M_Warehouse_ID, M_AttributeSetInstance_ID));
		if (qty.compareTo(Env.ZERO)<1)
			return cnt; //no show there is no qty to forecast nor purchase.
		//set Qty with Reserved/Ordered from future orders by DatePromised
		forecastline.setQty(qty); 
		
		//set product and save
		forecastline.setM_Product_ID(product);
		forecastline.setC_Period_ID(period.get_ID());
		forecastline.setM_Warehouse_ID(M_Warehouse_ID);
		forecastline.saveEx(trxName);
		
		//create grand-child Purchase Forecast Process as unprocessed
		MForecastProcess forecastprocess = new MForecastProcess(Env.getCtx(),0,trxName);
		forecastprocess.setM_Forecast_ID(forecastline.getM_Forecast_ID());
		forecastprocess.setM_ForecastLine_ID(forecastline.getM_ForecastLine_ID());
		forecastprocess.setProcessed(false);
		
 		forecastprocess.setPurchaseLeadTime(deliveryTimePromised(product, DatePromised));

		//set grand child references to parent and grand parent.
		if (M_ForecastLine_ID>0) //relate to which sales forecast line this po forecast line comes from
			forecastprocess.setM_ForecastLine_Sales_ID(M_ForecastLine_ID);
		else if (orderline!=null){
			// 
			forecastprocess.setC_Order_ID(orderline.getC_Order_ID());
			forecastprocess.setC_OrderLine_ID(orderline.getC_OrderLine_ID());
		}
		forecastprocess.setM_Forecast_ID(forecastline.getM_Forecast_ID());
		forecastprocess.setM_ForecastLine_ID(forecastline.getM_ForecastLine_ID());
		forecastprocess.saveEx(trxName);
		cnt++;
		//grand child is saved
		
		//if Qty is zero check for substitute product to replace		
		
		return cnt;
	}
}
