 CREATE TABLE "TCH_JCBUPI_BIN" 
   (	"ID" NUMBER NOT NULL ENABLE, 
	"BIN_RANGE_LOW" VARCHAR2(20 BYTE), 
	"BIN_RANGE_HIGH" VARCHAR2(20 BYTE), 
	"SCHEME_CODE" VARCHAR2(10 BYTE), 
	"EFFECTIVE_DATE" DATE, 
	"END_DATE" DATE, 
	"CREDIT_DEBIT_IDENTIFIER" VARCHAR2(10 BYTE), 
	"PIN_BYPASS_FLAG" VARCHAR2(1 BYTE)
   );
   /