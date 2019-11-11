package com.awl.tch.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.awl.tch.dao.LoggingDao;
import com.awl.tch.model.LoggingDTO;
import com.awl.tch.server.FilterChain;
import com.awl.tch.server.Request;
import com.awl.tch.server.Response;

@Component(value="LoggingFilter")
public class LoggingFilter implements Filter {

	private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

	@Autowired
	@Qualifier("loggingDao")
	private LoggingDao loggingDao;

	@Override
	public void doFilter(Request request, Response response, FilterChain chain) {
		// TODO Auto-generated method stub
		long startTime = System.currentTimeMillis();

		//long id = loggingDao.getSequence(Constants.TCH_LOGS_ID_SEQ);

		LoggingDTO loggingDtoRequest = new LoggingDTO();
		try{

			//loggingDtoRequest.setId(id);
			loggingDtoRequest.setRequestType(request.getRequestType());
			if(request.getRequestJSON().contains("trk2")){
				int i = request.getRequestJSON().indexOf("trk2");
				int j = request.getRequestJSON().substring(i).indexOf(",")-1;
				String temp = request.getRequestJSON().substring(i,i+j);
				loggingDtoRequest.setRequest(request.getRequestJSON().replaceAll(temp,"track2"));
			}else{
				loggingDtoRequest.setRequest(request.getRequestJSON());
			}
			//loggingDtoRequest.setStatus("S");
			//loggingDao.save(loggingDtoRequest);
		}catch(Exception e)
		{
			logger.error("Exception while saving requesting log :"+e);
		}

		logger.debug("Inside LoggingFilter :before");
		chain.doFilter(request, response);

		long endTime = System.currentTimeMillis();
		long totalTimeInMillis = endTime - startTime;
		try{
			//loggingDtoResponse.setId(id);
			loggingDtoRequest.setResponse(response.getResponseJSON());
			loggingDtoRequest.setProcessingTime(String.valueOf(totalTimeInMillis));
			loggingDtoRequest.setStatus("S");
			loggingDao.save(loggingDtoRequest);
		}catch(Exception e)
		{
			logger.error("Exception while updating response log :"+e);
		}
		logger.debug("Inside LoggingFilter :After");
		
		logger.debug("Request :" + loggingDtoRequest.getRequest() + "\n Response :" + loggingDtoRequest.getResponse());
	}
}
