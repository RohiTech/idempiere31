/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
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
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.process.SvrProcess;

import org.compiere.model.MDocType;
import org.compiere.model.MPeriodControl;
import org.compiere.util.CLogger;
import org.compiere.util.DB;

/**
 *	Verify Document Types.
 *	- Make sure that there is a DocumentType for all Document Base Types
 *	- Create missing Period Controls for Document Type 
 *	
 *  @author Jorg Janke
 *  @version $Id: DocumentTypeVerify.java,v 1.2 2006/07/30 00:51:01 jjanke Exp $
 */
public class DocumentTypeVerify extends SvrProcess
{
	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (DocumentTypeVerify.class);
	
	
	/**
	 *	No Parameters (Nop)
	 */
	protected void prepare()
	{
	}	//	prepare

	/**
	 * 	Execute process
	 *	@return info
	 *	@throws Exception
	 */
	protected String doIt() throws Exception
	{
		createDocumentTypes(getCtx(), getAD_Client_ID(), this, get_TrxName());
		createPeriodControls(getCtx(), getAD_Client_ID(), this, get_TrxName());
		return "OK";
	}	//	doIt

	
	/**************************************************************************
	 * 	Create Missing Document Types
	 * 	@param ctx context
	 * 	@param AD_Client_ID client
	 * 	@param sp server process
	 *	@param trxName transaction
	 */
	public static void createDocumentTypes(Properties ctx, int AD_Client_ID, 
		SvrProcess sp, String trxName)
	{
		if (s_log.isLoggable(Level.INFO)) s_log.info("AD_Client_ID=" + AD_Client_ID);
		String sql = "SELECT rl.Value, rl.Name "
			+ "FROM AD_Ref_List rl "
			+ "WHERE rl.AD_Reference_ID=183"
			+ " AND rl.IsActive='Y' AND NOT EXISTS "
			+ " (SELECT * FROM C_DocType dt WHERE dt.AD_Client_ID=? AND rl.Value=dt.DocBaseType)";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, trxName);
			pstmt.setInt(1, AD_Client_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				String name = rs.getString(2);
				String value = rs.getString(1);
				if (s_log.isLoggable(Level.CONFIG)) s_log.config(name + "=" + value);
				MDocType dt = new MDocType (ctx, value, name, trxName);
				if (dt.save())
				{
					if (sp != null)
						sp.addLog (0, null, null, name);
					else
						s_log.fine(name);
				}
				else
				{
					if (sp != null)
						sp.addLog (0, null, null, "Not created: " + name);
					else
						s_log.warning("Not created: " + name);
				}
			}
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
	}	//	createDocumentTypes
	
	
	/**
	 * 	Create Period Controls
	 * 	@param ctx context
	 * 	@param AD_Client_ID client
	 * 	@param sp server process
	 *	@param trxName transaction
	 */
	public static void createPeriodControls(Properties ctx, int AD_Client_ID, 
		SvrProcess sp, String trxName)
	{
		if (s_log.isLoggable(Level.INFO)) s_log.info("AD_Client_ID=" + AD_Client_ID);

		//	Delete Duplicates
		String sql = "DELETE C_PeriodControl pc1 "
			+ "WHERE (C_Period_ID, DocBaseType) IN "
				+ "(SELECT C_Period_ID, DocBaseType "
				+ "FROM C_PeriodControl pc2 "
				+ "GROUP BY C_Period_ID, DocBaseType "
				+ "HAVING COUNT(*) > 1)"
			+ " AND C_PeriodControl_ID NOT IN "
				+ "(SELECT MIN(C_PeriodControl_ID) "
				+ "FROM C_PeriodControl pc3 "
				+ "GROUP BY C_Period_ID, DocBaseType)";
		int no = DB.executeUpdate(sql, false, trxName);
		if (s_log.isLoggable(Level.INFO)) s_log.info("Duplicates deleted #" + no);
		
		//	Insert Missing
		sql = "SELECT DISTINCT p.AD_Client_ID, p.C_Period_ID, dt.DocBaseType "
			+ "FROM C_Period p"
			+ " FULL JOIN C_DocType dt ON (p.AD_Client_ID=dt.AD_Client_ID) "
			+ "WHERE p.AD_Client_ID=?"
			+ " AND NOT EXISTS"
			+ " (SELECT * FROM C_PeriodControl pc "
				+ "WHERE pc.C_Period_ID=p.C_Period_ID AND pc.DocBaseType=dt.DocBaseType)";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int counter = 0;
		try
		{
			pstmt = DB.prepareStatement(sql, trxName);
			pstmt.setInt(1, AD_Client_ID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				int Client_ID = rs.getInt(1);
				int C_Period_ID = rs.getInt(2);
				String DocBaseType = rs.getString(3);
				if (s_log.isLoggable(Level.CONFIG)) s_log.config("AD_Client_ID=" + Client_ID 
					+ ", C_Period_ID=" + C_Period_ID + ", DocBaseType=" + DocBaseType);
				//
				MPeriodControl pc = new MPeriodControl (ctx, Client_ID, 
					C_Period_ID, DocBaseType, trxName);
				if (pc.save())
				{
					counter++;
					if (s_log.isLoggable(Level.FINE)) s_log.fine(pc.toString());
				}
				else
					s_log.warning("Not saved: " + pc);
			}
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		if (sp != null)
			sp.addLog (0, null, new BigDecimal(counter), "@C_PeriodControl_ID@ @Created@");
		if (s_log.isLoggable(Level.INFO)) s_log.info("Inserted #" + counter);
	}	//	createPeriodControls

}	//	DocumentTypeVerify
