package com.awl.tch.iso8583;

import com.solab.iso8583.CustomField;

public class BcdEncodedField35 implements CustomField<String> {

	@Override
	public String decodeField(String paramString) {
		// TODO Auto-generated method stub
		//throw new Exception("Function is not implemented yet");
		return null;
	}

	@Override
	public String encodeField(String paramT) {
		// TODO Auto-generated method stub
		/*int length = paramT.length();
		byte[] buf = new byte[length];
		Bcd.encode(paramT, buf);*/
		
		//return paramT.substring(0,paramT.length()-1);
		return paramT;
	}


	

	

}
