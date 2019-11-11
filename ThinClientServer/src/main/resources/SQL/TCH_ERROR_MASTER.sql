		--------------------------------------------------------
--  File created - Thursday-February-23-2017   
--------------------------------------------------------
DROP TABLE "TCH_ERROR_MASTER";
--------------------------------------------------------
--  DDL for Table TCH_ERROR_MASTER
--------------------------------------------------------

CREATE TABLE "TCH_ERROR_MASTER" 
   (	"EM_ERROR_CODE" VARCHAR2(5 BYTE), 
	"EM_ERROR_DISPLAY_MSG" VARCHAR2(40 BYTE), 
	"EM_ERROR_ACTUAL_MSG" VARCHAR2(256 BYTE), 
	"EM_TRIGGER_CONDITION" VARCHAR2(256 BYTE), 
	"EM_CREATED" TIMESTAMP (6), 
	"EM_UPDATED" TIMESTAMP (6), 
	"EM_ID" NUMBER(10,0), 
	"EM_MODIFIED_BY" VARCHAR2(30 BYTE), 
	"EM_REASON_TO_MODIFY" VARCHAR2(100 BYTE)
   );
--------------------------------------------------------
--  Constraints for Table TCH_ERROR_MASTER
--------------------------------------------------------
   ALTER TABLE "TCH_ERROR_MASTER" ADD PRIMARY KEY ("EM_ERROR_CODE");
  ALTER TABLE "TCH_ERROR_MASTER" MODIFY ("EM_ERROR_CODE" NOT NULL ENABLE);

  --------------------------------------------------------
--  DDL for Index SYS_C0016935
--------------------------------------------------------

  CREATE UNIQUE INDEX "SYS_C0016935" ON "TCH_ERROR_MASTER" ("EM_ERROR_CODE");
  
-- handshake related error
INSERT INTO TCH_ERROR_MASTER VALUES('H-001','Handshake failure','Hanshake Application error due to unexpected exception','When unexpected exception occurs due to any technical reason', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('H-002','Error parsing response from switch','Error parsing response from switch','When exception occurs during parsing of response from switch', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('H-003','Incorrect response from switch','Incorrect response from switch','When response code returned from switch is not 00', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('H-004','Serial Number not found in Insight DB','Serial Number not found in Insight DB','When terminal serial number mapping is not found in Insight database', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('H-005','Card Objects not found in Insight DB','Card Objects not found in Insight Database','When Card Objects details are not found in Insight Database against terminal serial number', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('H-006','Settle previous batch exception.','Settle previous batch exception.','Previous bacth is open fro settlement.', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('H-007','Terminal is deactivated.','Terminal is deactivated.','Terminal is deactivated.', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('H-008','INCORRECT DATA FOUND','INCORRECT DATA FOUND','Data found in TCH db either multiple or not present', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
--CARD DETAILS RELATED ERROR
INSERT INTO TCH_ERROR_MASTER VALUES('C-101','No mapping found in Insight.','No mapping found in Insight.','No mapping found in insight', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
-- Sale related errors
INSERT INTO TCH_ERROR_MASTER VALUES('S-001','Sale failure','Sale Application error due to unexpected exception','When unexpected exception occurs due to any technical reason', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('S-002','Insight data mapping exception','Insight data mapping exception','Expected information from insight table but getting blank', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('S-003','Handshake mapping exception','Handshake mapping exception','More than one result in handshake table', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('S-004','Client id absent, initiate Handshake','Client id absent, initiate Handshake','Trying to do transaction without Handshake', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('S-005','Terminal detail swapped. Need handshake','Terminal detail swapped. Need handshake','MID TID mismatched in Handshake and insight tables. Need handshake', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('S-007','Detail absent intiate Handshake again','Detail absent intiate Handshake again','Handshake table is empty.', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('S-008','Too many values exception','Too many values exception','Multiple values expected 1', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('S-009','Insight mapping exception.','Insight mapping exception','Error while fetching information from insight', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('S-010','Installation pemding exception','Installation pemding exception','Installed flag is false in Handshake table', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('S-011','Invalid bin value exception','Invalid bin value exception','Bin value is not present in bin range', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('S-012','INVALID EXPIRY DATE','INVALID EXPIRY DATE','INVALID EXPIRY DATE', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('S-013','Invalid amount exception','Invalid amount exception','Either amount enter is zero or negative', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('S-014','Invalid response from switch','Invalid response from switch','Unsuccessful transaction', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('S-015','Error in validator','Error in validator','Error while parsing validator xml', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('S-016','Multiple entry exception','Multiple entry exception','Multiple entry for for same invoice number', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('S-019','CASH BACK NOT ALLOWED','CASH BACK NOT ALLOWED','CASH BACK NOT ALLOWED', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('S-020','SALE + CASH BACK NOT ALLOWED','SALE + CASH BACK NOT ALLOWED','SALE + CASH BACK NOT ALLOWED', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('S-021','AMEX PARAMETER MISSING','AMEX PARAMETER MISSING','Amex mid and tid is missing in table', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('S-302','INVALID AMT','INVALID AMT','INVALID AMT', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);

INSERT INTO TCH_ERROR_MASTER VALUES('S-888','NO RECORD(S) FOR SETTLEMENT','NO RECORD(S) FOR SETTLEMENT','NO RECORDS FOR SETTLEMENT', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('S-999','Intiate handshake.','Intiate handshake.','Paramter change request need to intiate handshake', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);

INSERT INTO TCH_ERROR_MASTER VALUES('S-100','Invalid reuest paramter','Invalid request exception.','Some request parameter is not proper as per speccification', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('S-200','Error in SP exception.','Error in SP exception.','Error occur while executing SP', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);

INSERT INTO TCH_ERROR_MASTER VALUES('S-017','Manual entry not allowed','Manual entry not allowed','Manual entry not allowed for respective card', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('S-018','PIN REQUIRED','PIN REQUIRED','ByPass Not allowed', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
--general error
INSERT INTO TCH_ERROR_MASTER VALUES('Z-001','Request Type Parameter is mandatory','Request Type Parameter is mandatory','When Request Type Parameter is not present', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('Z-002','Invalid Json Format','Invalid Json Format','When Json Format is invalid', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('Z-003','Switch connection exception','Switch connection exception','Switch is either not running or not responding', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('Z-500','Server Internal Error','Server Internal Error','Unexpected error or exception occured in server', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);

-- void payment error
INSERT INTO TCH_ERROR_MASTER VALUES('V-001','Void failure','Void Application error due to unexpected exception','When unexpected exception occurs due to any technical reason', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('V-002','Invalid Request. RRN missing.','Invalid Request. RRN missing.','Invalid request from terminal without rrn', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('V-003','Invalid request parameter','Invalid request parameter','Invalid information in request packet', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('V-004','Invalid invoice number','Invalid invoice number','No data found for entered invoice number', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('V-005','Invalid response from switch','Invalid response from switch','No data found for entered invoice number', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('V-006','Void already done exception','Void already done exception','Void already done for trasanction still intiate the void txn', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('V-007','MULTIPLE ENTRIES EXCEPTION ','MULTIPLE ENTRIES EXCEPTION','More than one entry for void transaction', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('V-008','VOID NOT ALLOWED','VOID NOT ALLOWED','More than one entry for void transaction', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('V-009','VOID NOT ALLOWED','VOID NOT ALLOWED','More than one entry for void transaction', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('V-010','NO RECORD FOUND','NO RECORD FOUND','record not found ', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);

--Refund payment error
INSERT INTO TCH_ERROR_MASTER VALUES('R-001','Refund failure','Refund Application error due to unexpected exception','When unexpected exception occurs due to any technical reason', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('R-002','Refund already done.','Refund already done.','RRN provided is missing in table', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('R-003','Refund not allowed exception','Refund not allowed exception','Card type is debit refund not allowed', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('R-004','Expiry date exception','Expiry date exception','invalid expiry date in request', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('R-007','Invalid response from switch','Invalid response from switch','Invalid response from switch', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('R-008','Refund not allowed.','Refund not allowed','Refund allowed flag is false', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('R-009','Invalid amount for refund','Invalid amount for refund','May be zero or non zero amount for refund', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('R-010','No record found for refund.','No record found for refund.','No record found for refund.', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('R-011','Insufficient amount for refund','Insufficient amount for refund','Insufficient amount for refund	', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('R-012','RRN MISSING','RRN MISSING','RRN MISSING	', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('R-013','MOBILE NUMBER MISSING','MOBILE NUMBER MISSING','MOBILE NUMBER MISSING', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('R-014','MISSING RRN','MISSING RRN','MISSING RRN', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('R-005','INVALID RRN','INVALID RRN','INVALID RRN', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);


--Reversal error
INSERT INTO TCH_ERROR_MASTER VALUES('R-101','Reversal Failure','Reversal Failure','When unexpected exception occurs due to any technical reason', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);

--BalanceEnquiry
INSERT INTO TCH_ERROR_MASTER VALUES('B-101','Balance enquiry failed','Balance enquiry failed','Not getting proper response as expected', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('B-001','Insight mapping Exception','Insight mapping Exception','Mapping either null or not present', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('B-002','Balance Enquiry not allowed','Balance Enquiry not allowed','Balance Enquiry not allowed', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('B-003','Invalid response from switch','Invalid response from switch','Invalid response from switch', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);

-- PreAuth error
INSERT INTO TCH_ERROR_MASTER VALUES('P-002','INVALID BIN','INVALID BIN','When unexpected exception occurs due to any technical reason', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('P-003','Pre Auth not allowed','Pre Auth not allowed','Pre auth not enable for tid. Contact AWL', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('P-004','Invalid response from switch','Invalid response from switch','Invalid response from switch', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('P-005','Insufficient fund for sale completion','Insufficient fund for sale completion','not enough fund to complete sale transaction on pre auth', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('P-006','Invalid amount value for sale','Invalid amount value for sale','May be zero or non zero amount for refund', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('P-007','No records found exception','No records found exception','Records not found based on RRN in payment table', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('P-008','Card is expired exception','Card is expired exception','Card expired as per card expiry date', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('P-009','Invalid amount exception','Invalid amount exception','Either amount enter is zero or negative', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('P-010','Invalid response from switch','Invalid response from switch','Unsuccessful transaction', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('P-011','Not valid Mid for transaction.','Not valid Mid for transaction.','Transaction for preauth and completion have different in MID', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('P-012','Completion failed.Number of days exceed.','Completion failed.Number of days exceed.','Number of days for sale completion exceed than that of actual allowed date by merchant', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('P-013','SALE COMPLETION ALLOWED ONCE.','SALE COMPLETION ALLOWED ONCE.','Sale completion allowed only once on any sale preauth', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('P-014','Insight mapping exception.','Insight mapping exception','Error while fetching information from insight', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('P-015','No mapping for handshake.','No mapping for handshake.','No mapping found for handshake', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('P-200','Error in SP exception.','Error in SP exception.','Error occur while executing SP', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);

--Tip adjustment
INSERT INTO TCH_ERROR_MASTER VALUES('T-101','Tip adjustment Failure','Tip adjustment Failure','When unexpected exception occurs due to any technical reason', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('T-001','Tip Failure','Tip Failure','When unexpected exception occurs due to any technical reason', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('T-002','Transaction has been void.','Transaction has been void','Void already done for trasanction still intiate the void txn', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('T-003','Invalid response from switch','Invalid response from switch','No data found for entered invoice number', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('T-004','Invalid invoice number','Invalid invoice number','No data found for entered invoice number', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('T-005','Tip not allowed','Tip not allowed','Tip percentage is zero', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('T-006','Tip amount exceed.','Tip amount exceed.','Tip percentage is less as compared to amount', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('T-007','Tip not allowed','Tip not allowed','Tip not allowed on refund trasaction', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('T-008','TIP ALLOWED ONCE.','TIP ALLOWED ONCE.','TIP allowed only once on any sale ', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('T-009','Tip not allowed','Tip not allowed','Tip not allowed on refund trasaction', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
--Settlement	
INSERT INTO TCH_ERROR_MASTER VALUES('S-601','NO RECORD FOUND','NO RECORD FOUND','No records found for the settlement', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('S-604','Settlement failed.','Settlement failed.','Settlement not done properly after bacth upload.', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('S-602','No records for settlement','No records for settlement','No records found for the settlement', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('S-616','Error in SP exception.','Error in SP exception.','Error occur while executing SP', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);

-- summary report

INSERT INTO TCH_ERROR_MASTER VALUES('R-201','Summary Report failure.','Summary Report failure.','Error occur while fetching report', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('R-202','NO REOCRDS FOR SUMMARY','NO REOCRDS FOR SUMMARY','Error occur while fetching report', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);

-- sale completion
INSERT INTO TCH_ERROR_MASTER VALUES('R-611','Sale completion failure.','Sale completion failure.','Error occur while fetching report', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);

--sale emi
INSERT INTO TCH_ERROR_MASTER VALUES('S-801','No program code found','No program code found','Program code not found ', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('S-802','EMI SALE flag missing in request','EMI SALE flag missing','As the second request emi flag is missing in it', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('S-803','EMI program object missing.','EMI program object missing.','EMI_SALE is desc flag but emi program object is missing', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('S-804','Emi bin missing in insight table','Emi bin missing in insight table','Emi bin missing in insight table', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);

--DCC
INSERT INTO TCH_ERROR_MASTER VALUES('D-001','Invalid response from switch','Invalid response from switch','Unsuccessful transaction', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);

--DETAIL REPORT
INSERT INTO TCH_ERROR_MASTER VALUES('D-401','Detail report failure','Detail report failure','Unsuccessful transaction', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('D-402','NO RECORD(S)','NO REOCORD(S)','Record not present for respective terminal', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('D-404','BATCH NUM IS TOO OLD','BATCH NUM IS TOO OLD','BATCH FOR WHICH DETAIL REPORT ASKED IS MORE THAN 7 DAYS OLD', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('D-416','Error in sp Exception','Error in sp Exception','Error in sp Exception', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);

--AAB related 
INSERT INTO TCH_ERROR_MASTER VALUES('A-001','AAB service exception','AAB service exception','AAB service exception', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('A-002','Exception in parsing Json','Exception in parsing Json','Exception in parsing Json', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('A-004','No data found','No Data found','No data found in tility info table', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('U-001','No data found','No Data found','No data found in tility info table', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);

--TERMINAL DEACTIVATED
INSERT INTO TCH_ERROR_MASTER VALUES('Z-010','Terminal is deactivated','Terminal is deactivated','Terminal is deactivated', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);

--EMI RELATED MESSAGE
INSERT INTO TCH_ERROR_MASTER VALUES('E-001','NO PROGRAM FOUND','NO PROGRAM FOUND','NO PROGRAM FOUND', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('E-002','NO TENURES FOUND','NO TENURES FOUND','NO TENURES FOUND', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('E-003','TRANSACTION AMOUNT EXCEEDED','TRANSACTION AMOUNT EXCEEDED','Transaction Amount is exceeded for EMI', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('E-004','TRANSACTION AMOUNT IS TOO LOW','TRANSACTION AMOUNT IS TOO LOW','Transaction Amount is to low for EMI', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('E-005','AMOUNT VALUE NOT PRESENT','AMOUNT VALUE NOT PRESENT','Emi amount is not present in insight table', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);

-- EMV ACK Message
INSERT INTO TCH_ERROR_MASTER VALUES('EM-001','NO DATA FOUND','NO DATA FOUND','No data found to update for void', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);


--CONVERT TO INR
INSERT INTO TCH_ERROR_MASTER VALUES('C-001','CONVERT TO INR FAIL','CONVERT TO INR FAIL','CONVERT TO INR FAIL', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('C-002','NO DATA FOUND','NO DATA FOUND','No data found to update for CONVERT TO INR', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('C-003','SP EXCEPTION','SP EXCEPTION','SP EXCEPTION', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('C-004','NO DATA FOUND','NO DATA FOUND','No data found to update for CONVERT TO INR', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
commit;

--BRAND EMI

INSERT INTO TCH_ERROR_MASTER VALUES('BR-02','MAPPING ISSUE','MAPPING ISSUE','MAPPING ISSUE', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('BR-03','INVALID SELECTION','INVALID SELECTION','INVALID SELECTION', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('BR-06','NO SKU CODE FOUND','NO SKU CODE FOUND','Sku code absent for the selected entry', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('BR-07','CARD NOT SUPPORTED','CARD NOT SUPPORTED','CARD NOT SUPPORTED', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('BR-08','PROGRAMS NOT FOUND','PROGRAMS NOT FOUND','PROGRAMS NOT FOUND', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('BR-09','NO DATA FOUND','NO DATA FOUND','NO DATA FOUND', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('BR-10','INVALID DATA ISSUE','INVALID DATA ISSUE','INVALID DATA ISSUE', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('BR-11','INVALID SKU CODE','INVALID SKU CODE','INVALID SKU CODE', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('BR-12','INVALID URL','INVALID URL','INVALID URL', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('BR-13','INTERNAL ERROR','INTERNAL ERROR','INTERNAL ERROR', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('BR-14','EXCEED VALIDATION DAYS','EXCEED VALIDATION DAYS','EXCEED VALIDATION DAYS', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('BR-15','RECORD NOT FOUND','RECORD NOT FOUND','RECORD NOT FOUND', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('BR-16','INVALID INPUT','INVALID INPUT','INVALID INPUT', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('BR-17','PROGRAM EXPIRED','PROGRAM EXPIRED','PROGRAM EXPIRED', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('BR-99','PRESS GREEN KEY FOR FRAMES DOWNLOAD...','NEW FRAMES AVAILBLE FOR DOWNLOAD.PLEASE PRESS GREEN KEY TO CONTINUE..','NEW FRAMES AVAILBLE FOR DOWNLOAD.PLEASE PRESS GREEN KEY TO CONTINUE..', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);


--MPOS
INSERT INTO TCH_ERROR_MASTER VALUES('MP-01','INVALID DATA','PROGRAMS NOT FOUND','PROGRAMS NOT FOUND', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);

--MPOS

INSERT INTO TCH_ERROR_MASTER VALUES('MP-02','EXCEPTION WHILE ENCRYPTION','EXCEPTION WHILE ENCRYPTION','EXCEPTION WHILE ENCRYPTION', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('MP-03','INVALID DATA ISSUE','INVALID DATA ISSUE','INVALID DATA ISSUE', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('MP-04','EXCEPTION WHILE DECRYPTION','EXCEPTION WHILE DECRYPTION','EXCEPTION WHILE DECRYPTION', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);

--IRCTC
INSERT INTO TCH_ERROR_MASTER VALUES('IR-01','NOT VALID KEY','NOT VALID KEY','NOT VALID KEY', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('IR-02','NO DATA SALE INIT DATA AVAILABLE FOR TID','NO DATA SALE INIT DATA AVAILABLE FOR TID','NO DATA SALE INIT DATA AVAILABLE FOR TID', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('IR-03','ENCODING FAILED','ENCODING FAILED','ENCODING FAILED', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);


--WALLET
INSERT INTO TCH_ERROR_MASTER VALUES('W-103','NO RECORD FOUND FOR REFUND','NO RECORD FOUND FOR REFUND','NO RECORD FOUND FOR REFUND', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('W-102','REFUND ALREADY APPROVED','REFUND ALREADY APPROVED','REFUND ALREADY APPROVED', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('W-104','AMOUNT MISMATCH','AMOUNT MISMATCH','AMOUNT MISMATCH', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);

--SBIEPAY
INSERT INTO TCH_ERROR_MASTER VALUES('SB-05','Invalid Card.Use Credit Card for payment','Invalid Card!! Please use Credit Card for payment','Invalid Card!! Please use Credit Card for payment', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);
INSERT INTO TCH_ERROR_MASTER VALUES('SB-06','Invalid Card.Use Debit Card for payment','Invalid Card.Use Debit Card for payment','Invalid Card.Use Debit Card for payment', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP,SEQ_TC_EM_ID.NEXTVAL ,null,null);