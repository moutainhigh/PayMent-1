create or replace 
PROCEDURE TCH_DETAIL_REPORT_PROC (
  I_TERMINAL_SERIAL_NUMBER IN VARCHAR2
, I_OFFSET IN VARCHAR2
, I_DES_PARAM IN VARCHAR2
, O_MERCHANT_ID OUT VARCHAR2
, O_TERMINAL_ID OUT VARCHAR2
, O_SQL_CODE OUT VARCHAR2
, O_APP_ERROR_CODE OUT VARCHAR2
, O_BATCH_NUMBER OUT VARCHAR2
, O_DEBUG_POINT OUT VARCHAR2
, O_TOTAL_COUNT OUT VARCHAR2
, C_DETAIL_REPORT_DATA OUT SYS_REFCURSOR
, C_SUMMARY_REPORT_DATA OUT SYS_REFCURSOR
, C_SUMMARY_REPORT_SCHEME OUT SYS_REFCURSOR
, C_DETAIL_REPORT_TABLE_DATA OUT SYS_REFCURSOR
)AS 

V_COUNT NUMBER(4):= NULL;
V_MID VARCHAR2(20):=NULL;
V_TID VARCHAR2(20):=NULL;
BEGIN
  
  O_DEBUG_POINT :='1';
  -- GET BATCH NUMBER
  SELECT COUNT(1) INTO V_COUNT FROM TCH_PAYMENT_TXN WHERE P_SETTLEMENT_FLAG = 'N' 
  AND P_TERMINAL_SERIAL_NUMBER = I_TERMINAL_SERIAL_NUMBER
  AND P_RESPONSE_CODE = '00' AND P_REQUEST_TYPE <> 'REVERSAL' ORDER BY TO_NUMBER(P_BATCH_NUMBER);  
    
  IF V_COUNT <> 0 THEN
    BEGIN
           O_DEBUG_POINT:='2B';
          SELECT P_BATCH_NUMBER,P_MERCHANTID,P_TERMINALID INTO O_BATCH_NUMBER,O_MERCHANT_ID,O_TERMINAL_ID FROM TCH_PAYMENT_TXN WHERE
          P_SETTLEMENT_FLAG = 'N' AND P_TERMINAL_SERIAL_NUMBER = I_TERMINAL_SERIAL_NUMBER
          ORDER BY TO_NUMBER(P_BATCH_NUMBER) DESC FETCH FIRST 1 ROWS ONLY;    
          --O_BATCH_NUMBER := LPAD(O_BATCH_NUMBER,6,'0');
         END;
    ELSE 
    O_DEBUG_POINT :='1A';
    RAISE_APPLICATION_ERROR('-21111','NO MAPPING FOUND');
  END IF;

-- GET 10 RECORDS AT A TIME
  O_DEBUG_POINT:='1B';
  
  SELECT COUNT(1) INTO O_TOTAL_COUNT FROM TCH_PAYMENT_TXN WHERE P_RESPONSE_CODE = '00' 
    AND P_SETTLEMENT_FLAG = 'N' 
    AND P_REQUEST_TYPE <> 'TIP'
    AND P_BATCH_NUMBER = O_BATCH_NUMBER 
    AND P_TERMINAL_SERIAL_NUMBER = I_TERMINAL_SERIAL_NUMBER;
    --OFFSET I_OFFSET ROWS FETCH NEXT 10 ROWS ONLY;
    IF I_DES_PARAM <> 1 THEN
      IF O_TOTAL_COUNT >= 5000 THEN
        O_DEBUG_POINT := '1BA';
        RAISE_APPLICATION_ERROR('-21212','NO OF RECORDS MORE THAN 50');
      END IF;
    END IF;
--CHECK CONDITION FOR COUNTINUTY FLAG
  O_TOTAL_COUNT := O_TOTAL_COUNT - I_OFFSET;
  IF O_TOTAL_COUNT < 10 THEN
   BEGIN
     O_TOTAL_COUNT := '0';
   END;
  ELSE
   O_TOTAL_COUNT := '1';
  END IF;
  
-- GET DETAIL REPORT DATA FROM PAYMENT TABLE IN THE PACKET OF 10
  O_DEBUG_POINT:='1D';
  
  OPEN C_DETAIL_REPORT_DATA FOR
    SELECT P_REQUEST_TYPE , P_INVOICENUMBER, P_AUTH_ID, P_LAST_FOUR_DIGIT, P_EXPIRYDATE, P_ORIGINAL_AMOUNT, P_CARD_LABEL, P_TIP_APPROVED
    FROM TCH_PAYMENT_TXN WHERE P_RESPONSE_CODE = '00' 
    AND P_SETTLEMENT_FLAG = 'N' 
    AND P_REQUEST_TYPE <> 'TIP'
    AND P_REQUEST_TYPE <> 'REVERSAL'
    AND P_BATCH_NUMBER = O_BATCH_NUMBER 
    AND P_TERMINAL_SERIAL_NUMBER = I_TERMINAL_SERIAL_NUMBER ORDER BY P_INVOICENUMBER
   OFFSET I_OFFSET ROWS FETCH NEXT 10 ROWS ONLY;

-- GET DETAIL TABLE INFORMATION
  OPEN C_DETAIL_REPORT_TABLE_DATA FOR
    SELECT D_DISPLAY_MSG_PARAM,D_COLUMN_NAME FROM TCH_DETAIL_REPORT ORDER BY D_DISPLAY_SEQ_NUMBER;
    O_BATCH_NUMBER := LPAD(O_BATCH_NUMBER,6,'0');
    O_DEBUG_POINT := '2';
    EXCEPTION
    WHEN NO_DATA_FOUND THEN
      IF O_DEBUG_POINT = '1A' THEN
        O_APP_ERROR_CODE :='D-402';
      END IF;
    WHEN OTHERS THEN
      IF O_DEBUG_POINT = '1BA' THEN
        O_APP_ERROR_CODE :='D-403';
     ELSIF O_DEBUG_POINT = '1A' THEN
        O_APP_ERROR_CODE :='D-402';
      END IF;
  O_SQL_CODE := SQLCODE;
END TCH_DETAIL_REPORT_PROC;