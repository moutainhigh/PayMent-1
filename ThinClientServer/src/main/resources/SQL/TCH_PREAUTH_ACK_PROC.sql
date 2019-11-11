create or replace PROCEDURE TCH_PREAUTH_ACK_PROC (
  I_TERMINAL_SERIAL_NUMBER IN VARCHAR2
, I_APP_NAME IN VARCHAR2
, I_CLIENT_ID IN VARCHAR2
, I_INVOICE_NUMBER IN VARCHAR2
, O_SQL_CODE OUT VARCHAR2
, O_APP_ERROR_CODE OUT VARCHAR2
, O_DEBUG_POINT_ACK OUT VARCHAR2 
, C_EXST_SALE_PARAMETER OUT SYS_REFCURSOR
, C_UTILITY_INFO OUT SYS_REFCURSOR
) AS 

V_COUNT NUMBER(4):=NULL;
V_MID VARCHAR2(25):=NULL;
  BEGIN
  
    O_DEBUG_POINT_ACK:=1;
    
    SELECT DISTINCT(TMP.MID) INTO V_MID FROM TC_TERM_PARAMETER TTP JOIN TC_ME_PARAMETER TMP 
      ON TTP.MEPARAMID = TMP.MEPARAMID JOIN TC_TID_HWSR_MAPPING THM 
      ON THM.MID = TMP.MID 
      WHERE THM.HWSRNO = I_TERMINAL_SERIAL_NUMBER;
      
    SELECT COUNT(1) INTO V_COUNT FROM TCH_PAYMENT_AUTH_TXN TPT WHERE TPT.P_MERCHANTID = V_MID
    AND TPT.P_INVOICENUMBER = I_INVOICE_NUMBER AND TPT.P_CLIENTID = I_CLIENT_ID
    AND TPT.P_TERMINAL_SERIAL_NUMBER = I_TERMINAL_SERIAL_NUMBER AND TPT.P_REQUEST_TYPE = 'PREAUTH';
   
   
    
    -- CHECK FOR PREVIOUS ENTRY PRESENT OR NOT 
    IF V_COUNT = 0 THEN
      O_APP_ERROR_CODE:='AB';
      RAISE_APPLICATION_ERROR('-21000','NO MAPPING FOUND');
    ELSIF V_COUNT > 1 THEN
      O_DEBUG_POINT_ACK:=2;
      O_APP_ERROR_CODE:='S-016';
      RAISE_APPLICATION_ERROR('-21000','TOO MANY ROWS');
    END IF;
    
    O_DEBUG_POINT_ACK:=3;
    
    OPEN C_EXST_SALE_PARAMETER FOR
      SELECT TPT.* FROM TCH_PAYMENT_AUTH_TXN TPT WHERE TPT.P_MERCHANTID = V_MID
      AND TPT.P_INVOICENUMBER = I_INVOICE_NUMBER AND TPT.P_CLIENTID = I_CLIENT_ID
      AND TPT.P_TERMINAL_SERIAL_NUMBER = I_TERMINAL_SERIAL_NUMBER AND TPT.P_REQUEST_TYPE = 'PREAUTH';
    
  IF I_APP_NAME IS NOT NULL THEN
      BEGIN 
        OPEN C_UTILITY_INFO FOR
          SELECT U_PRINT_LABELS FROM TCH_UTILITY_INFO WHERE U_APP_NAME = I_APP_NAME;
      END;
    END IF;  
    O_DEBUG_POINT_ACK:=4;    
    
  EXCEPTION
    WHEN NO_DATA_FOUND THEN   
      IF O_DEBUG_POINT_ACK = '1' THEN 
          O_APP_ERROR_CODE := 'AB';
      END IF;
    WHEN TOO_MANY_ROWS THEN 
        IF O_DEBUG_POINT_ACK = '1' THEN 
          O_APP_ERROR_CODE := 'S-016'; 
        END IF ;
    WHEN OTHERS THEN
      IF O_DEBUG_POINT_ACK = '2' THEN 
          O_APP_ERROR_CODE := 'S-016'; 
      END IF ;      
      O_SQL_CODE := SQLCODE; 

END TCH_PREAUTH_ACK_PROC;