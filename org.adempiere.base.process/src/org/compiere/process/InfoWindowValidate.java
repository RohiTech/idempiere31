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

import org.compiere.process.SvrProcess;

import org.compiere.model.MInfoWindow;

/**
 * 	Validate Info Window SQL
 *	
 *  @author Jorg Janke
 *  @version $Id: InfoWindowValidate.java,v 1.2 2006/07/30 00:51:01 jjanke Exp $
 */
public class InfoWindowValidate extends SvrProcess
{
	/**	Info Window			*/
	private int p_AD_InfoWindow_ID = 0;
	
	/**
	 * 	Prepare
	 */
	protected void prepare ()
	{
		p_AD_InfoWindow_ID = getRecord_ID();
	}	//	prepare

	/**
	 * 	Process
	 *	@return info
	 *	@throws Exception
	 */
	protected String doIt ()
		throws Exception
	{
		MInfoWindow infoWindow = new MInfoWindow(getCtx(), p_AD_InfoWindow_ID, (String)null);
		infoWindow.validate();
		infoWindow.saveEx();

		return infoWindow.isValid() ? "@OK@" : "@NotValid@";
	}	//	doIt
	
}	//	InfoWindowValidate
