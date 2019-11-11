package com.awl.tch.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.awl.tch.constant.Constants;
import com.awl.tch.exceptions.TCHQueryException;
import com.awl.tch.model.GBSSDto;
import com.awl.tch.util.Property;

@Repository(Constants.TCH_GBSS_DAO)
public class GBSSDaoImpl extends GenericDaoImpl<GBSSDto>{
	
	private static Logger logger = LoggerFactory.getLogger(GBSSDaoImpl.class);
	
	public GBSSDto getGBSSinfo(String challenNumber) throws TCHQueryException{
		GBSSDto dto = new GBSSDto();
		String query = "SELECT * FROM TCH_GBSS_REPORT WHERE G_CHALLEN_NUM = ?";
		if(Property.isShowSql()){
			logger.debug(query); 
		}
		try{
			dto = getJdbcTemplate().queryForObject(query,new Object[]{challenNumber},new RowMapper<GBSSDto>(){
				@Override
				public GBSSDto mapRow(ResultSet rs, int rowNum) throws SQLException {
					GBSSDto gbssDto = new GBSSDto();
					gbssDto.setCustomerName(rs.getString("G_CUSTOMER_NAME"));
					gbssDto.setAmount(rs.getString("G_AMOUNT"));
					gbssDto.setCreated(rs.getString("G_CREATED"));
					gbssDto.setPaymentMode(rs.getString("G_PAY_MODE"));
					gbssDto.setTax1(rs.getString("G_TAX1"));
					gbssDto.setTax2(rs.getString("G_TAX2"));
					gbssDto.setFee(rs.getString("G_FEE"));
					gbssDto.setFeePer(rs.getString("G_FEE_PER"));
					gbssDto.setTax1Per(rs.getString("G_TAX1_PER"));
					gbssDto.setTax2Per(rs.getString("G_TAX2_PER"));
					gbssDto.setStatus(rs.getString("G_STATUS"));
					return gbssDto;
				}
				
			});	
		}catch(Exception e){
			logger.debug("exception occured.",e);
			throw new TCHQueryException("G-004","INVALID CHALLEN NUM");
		}
		return dto;
	}
}
