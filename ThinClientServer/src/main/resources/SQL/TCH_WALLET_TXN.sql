--------------------------------------------------------
--  File created - Tuesday-January-02-2018   
--------------------------------------------------------
DROP TABLE "TCH_WALLET_TXN";
--------------------------------------------------------
--  DDL for Table TCH_WALLET_TXN
--------------------------------------------------------
--create table
CREATE TABLE "TCH_WALLET_TXN"
  (
    "W_MID" VARCHAR2(20 BYTE), 
	"W_TID" VARCHAR2(10 BYTE), 
	"W_PLATFORMID" VARCHAR2(30 BYTE), 
	"W_TXN_REF_ID" VARCHAR2(30 BYTE), 
	"W_WALLET_ID" VARCHAR2(10 BYTE), 
	"W_TXN_AMOUNT" VARCHAR2(30 BYTE), 
	"W_TXN_DATENTIME" VARCHAR2(15 BYTE), 
	"W_PROC_CODE" VARCHAR2(10 BYTE), 
	"W_AUTH_TOKEN" VARCHAR2(250 BYTE), 
	"W_ADDITIONAL_INFO" VARCHAR2(30 BYTE), 
	"W_RESPONSE_CODE" VARCHAR2(30 BYTE), 
	"W_RESPONSE_DESC" VARCHAR2(100 BYTE), 
	"W_TXN_ID" VARCHAR2(30 BYTE), 
	"W_TERM_SER_NUM" VARCHAR2(20 BYTE), 
	"W_INV_NUMBER" VARCHAR2(20 BYTE), 
	"W_REQUEST_TYPE" VARCHAR2(20 BYTE), 
	"W_REFUND_APPROVED" VARCHAR2(1 BYTE) DEFAULT 0, 
	"W_TCH_SETTLED_FLAG" VARCHAR2(1 BYTE) DEFAULT 'N', 
	"W_WALLET_TYPE" VARCHAR2(20 BYTE), 
	"W_BATCH_NUMBER" VARCHAR2(9 BYTE), 
	"W_ISCOMPLETED" VARCHAR2(1 BYTE), 
	"W_CREATED" TIMESTAMP (6), 
    CONSTRAINT "W_TXN_REF_ID" PRIMARY KEY ("W_TXN_REF_ID") USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) TABLESPACE "TCHSIT" ENABLE
  );

--------------------------------------------------------
--  DDL for Index W_TXN_REF_ID
--------------------------------------------------------

  CREATE UNIQUE INDEX "W_TXN_REF_ID" ON "TCH_WALLET_TXN" ("W_TXN_REF_ID");
--------------------------------------------------------
--  Constraints for Table TCH_WALLET_TXN
--------------------------------------------------------

