package com.awl.tch.brandemi.validation;

import com.awl.tch.brandemi.util.Constants;

public class ManufacturerServiceFactory {

	public static ManufacturerService getManufacturerService(String manufacturerName, String[] args)
	{
		if(manufacturerName != null){
			// Change for respective service call.
			if( manufacturerName.toUpperCase().contains(Constants.SAMSUNG)){
				manufacturerName = Constants.SAMSUNG;
			}else if( manufacturerName.toUpperCase().contains(Constants.LG)){
				manufacturerName = Constants.LG;
			}

			switch (manufacturerName) {
			case Constants.SAMSUNG:
				return SamsungService.getInstance();
			case Constants.LG:
				if(args.length>=3 && args[2]!=null)
					return LGServiceFactory.getLGService(args[2]);
				break;
			default:
				break;
			}
		}
		return null;
	}
}
