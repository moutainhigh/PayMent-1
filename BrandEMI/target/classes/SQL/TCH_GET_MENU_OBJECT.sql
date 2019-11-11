create or replace PROCEDURE TCH_GET_MENU_OBJECT 
(
  I_MID IN VARCHAR2,
  C_SCREEN1 OUT SYS_REFCURSOR,
  C_SCREEN2 OUT SYS_REFCURSOR,
  C_SCREEN3 OUT SYS_REFCURSOR,
  C_PROGRAMS OUT SYS_REFCURSOR,
  O_SQL_CODE OUT VARCHAR2,
  O_APP_ERROR_CODE OUT VARCHAR2,
  O_DEBUG_POINT OUT VARCHAR2
) AS 
BEGIN
  NULL;
  
 O_DEBUG_POINT:='1';
 
OPEN C_PROGRAMS FOR SELECT T3.CATEGORYID ,  T3.CHILDID ,   T4.SCHEMEID , T3.PRODUCTMANUFACTURERID 
FROM XP_EMI_PROGRAM_MERCHANT  T1 JOIN  XP_EMI_PROGRAM_PRODUCT T2 ON T1.PROGRAMCODE = T2.PROGRAMCODE
JOIN  XP_EMI_PRODUCT T3 ON T3.PRODUCTCODE = T2.PRODUCTCODE
JOIN XP_EMI_MID_DEALERSCHEME_MAP T4 ON T1.MERCHANTCODE = T4.MID  
JOIN XP_EMI_PROGRAM_SCHEME_MAPPING T5 ON T5.MID_DEALERSCHEMEID = T4.MID_DEALERSCHEMEID AND T5.PROGRAMCODE = T1.PROGRAMCODE 
JOIN XP_EMI_MID_DLR_MAPPING T6 ON T6.MID_DLR_MAP_ID = T4.MID_DLR_MAP_ID AND T4.MANUFACTURERID = T3.PRODUCTMANUFACTURERID
WHERE T1.MERCHANTCODE = I_MID AND  T3.STATUS = 'A' AND T1.STATUS = 'A' AND  T3.PRODUCTMANUFACTURERID 
IN (SELECT DISTINCT T2.MANUFACTURERID FROM XP_EMI_MID_DEALERSCHEME_MAP T2 WHERE T2.MID = I_MID)  ORDER BY 1 ;

 O_DEBUG_POINT:='2';
 
 
OPEN C_SCREEN1 FOR select T1.CATEGORYNAME , T1.CATEGORYID from XP_EMI_BRAND_CATEGORYMASTER T1 
WHERE T1.CATEGORYID IN (SELECT DISTINCT T2.CATEGORYID FROM XP_EMI_MID_DEALERSCHEME_MAP T2 WHERE T2.MID = I_MID) ORDER BY 2; 


 O_DEBUG_POINT:='3';
 
OPEN C_SCREEN2 FOR
SELECT T1.MANUFACTURERNAME , T1.MANUFACTURERID FROM XP_EMI_MANUFACTURER T1 
WHERE T1.MANUFACTURERID IN (SELECT DISTINCT T2.MANUFACTURERID FROM XP_EMI_MID_DEALERSCHEME_MAP T2 WHERE T2.MID = I_MID AND T2.STATUS = 'A') AND T1.STATUS = 'A' ORDER BY 2; 

 O_DEBUG_POINT:='4';
 
OPEN C_SCREEN3 FOR

select T1.SCHEMECODE , T1.SCHEMEID  from XP_EMI_BRAND_SCHEMEMASTER T1 
WHERE T1.SCHEMEID IN (SELECT DISTINCT T2.SCHEMEID FROM XP_EMI_MID_DEALERSCHEME_MAP T2 WHERE T2.MID = I_MID) ORDER BY 2; 

 O_DEBUG_POINT:='5';
 


 
 
 EXCEPTION
    WHEN NO_DATA_FOUND THEN 
      IF O_DEBUG_POINT = '1' THEN 
          O_APP_ERROR_CODE := 'E-001';
      ELSIF O_DEBUG_POINT = '2' THEN 
          O_APP_ERROR_CODE := 'E-002';
      ELSIF O_DEBUG_POINT = '3' THEN 
          O_APP_ERROR_CODE := 'E-003';
      ELSIF O_DEBUG_POINT = '4' THEN 
          O_APP_ERROR_CODE := 'E-004';
      ELSIF O_DEBUG_POINT = '5' THEN 
          O_APP_ERROR_CODE := 'E-005';
      END IF;
    WHEN OTHERS THEN
      IF O_DEBUG_POINT = '1' THEN 
          O_APP_ERROR_CODE := 'E-001';
      ELSIF O_DEBUG_POINT = '2' THEN 
          O_APP_ERROR_CODE := 'E-002';
      ELSIF O_DEBUG_POINT = '3' THEN 
          O_APP_ERROR_CODE := 'E-003';
      ELSIF O_DEBUG_POINT = '4' THEN 
          O_APP_ERROR_CODE := 'E-004';
      ELSIF O_DEBUG_POINT = '5' THEN 
          O_APP_ERROR_CODE := 'E-005';
      END IF;      
      O_SQL_CODE := SQLCODE; 
END TCH_GET_MENU_OBJECT;