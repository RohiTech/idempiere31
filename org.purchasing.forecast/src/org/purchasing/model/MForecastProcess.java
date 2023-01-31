package org.purchasing.model;

import java.sql.ResultSet;
import java.util.Properties;

/**Helper class for new Purchasing Forecast Resource record details
 * 1. Check existence of previous Process (Model - Forecast/Line/Process)
 * 2. Create model with (a) Promised Date + Product Purchasing Lead Time (b) Qty in stock + reserve/ordered balance < DatePromised
 * @author red1
 */
public class MForecastProcess extends X_M_ForecastProcess {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3173485299694806655L;

	public MForecastProcess(Properties ctx, int M_ForecastProcess_ID,
			String trxName) {
		super(ctx, M_ForecastProcess_ID, trxName);
		// 
	}

	public MForecastProcess(Properties ctx, ResultSet rs, String trxName) {
		super(ctx,rs,trxName);
	}
}
