
  CREATE TABLE "TCH"."TC_ME_PARAMETER" 
   (	"MEPARAMID" NUMBER NOT NULL ENABLE, 
	"BANKCODE" VARCHAR2(5 BYTE) NOT NULL ENABLE, 
	"INSTITUTIONID" NUMBER NOT NULL ENABLE, 
	"MERTYPE" VARCHAR2(10 BYTE) NOT NULL ENABLE, 
	"BANKNAME" VARCHAR2(100 BYTE) NOT NULL ENABLE, 
	"MID" VARCHAR2(15 BYTE) NOT NULL ENABLE, 
	"DBANAME" VARCHAR2(300 BYTE) NOT NULL ENABLE, 
	"STATENAME" VARCHAR2(1000 BYTE) NOT NULL ENABLE, 
	"CITYNAME" VARCHAR2(1000 BYTE) NOT NULL ENABLE, 
	"LOCATION" VARCHAR2(3000 BYTE) NOT NULL ENABLE, 
	"PRIMARYPHONE" VARCHAR2(100 BYTE) NOT NULL ENABLE, 
	"SECONDARYPHONE" VARCHAR2(100 BYTE) NOT NULL ENABLE, 
	"HEARTBEATINTERVAL" VARCHAR2(6 BYTE), 
	"ADDEDON" DATE, 
	"ADDEDBY" VARCHAR2(1000 BYTE), 
	"ADDIPADDRESS" VARCHAR2(100 BYTE), 
	"MODIFIEDON" DATE, 
	"MODIFIEDBY" VARCHAR2(1000 BYTE), 
	"MODIFIEDIPADDRESS" VARCHAR2(100 BYTE), 
	 CONSTRAINT "TC_ME_PARAMETER_PK" PRIMARY KEY ("MEPARAMID")
   );