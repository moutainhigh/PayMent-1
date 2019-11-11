create or replace PROCEDURE TCH_REFUND_WALLET_PROC(
I_RRN IN VARCHAR2,
I_TERMINAL_SERIAL_NUMBER IN VARCHAR2,
O_SQL_CODE OUT VARCHAR2,
O_APP_ERROR_CODE OUT VARCHAR2,
O_DEBUG_POINT OUT VARCHAR2,
C_REFUND_DATA OUT SYS_REFCURSOR
)
AS 
V_COUNT NUMBER(20) := NULL;
V_RRN VARCHAR2(12) := NULL;

BEGIN

    O_DEBUG_POINT:='1A'; 

    SELECT COUNT(1) INTO V_COUNT 
    FROM TCH_WALLET_TXN TWT
    WHERE TWT.W_TERM_SER_NUM = I_TERMINAL_SERIAL_NUMBER
    AND TWT.W_TXN_ID = I_RRN
    AND TWT.W_REQUEST_TYPE = 'SALE';

    IF V_COUNT = 0 THEN
        O_DEBUG_POINT := '1AA';
        RAISE_APPLICATION_ERROR(-20135,'INVALID RRN');
    ELSE
        SELECT COUNT(1) INTO V_COUNT
        FROM TCH_WALLET_TXN TWT
        WHERE TWT.W_TXN_ID = I_RRN
        AND TWT.W_REQUEST_TYPE = 'SALE'
        AND TWT.W_REFUND_APPROVED = '0' -- NOT APPROVED
        AND TWT.W_TERM_SER_NUM = I_TERMINAL_SERIAL_NUMBER;

        IF V_COUNT = 0 THEN
            O_DEBUG_POINT := '1AB';
            RAISE_APPLICATION_ERROR(-20134, 'REFUND NOT ALLOWED');
        ELSE
            OPEN C_REFUND_DATA FOR
            SELECT TWT.* FROM TCH_WALLET_TXN TWT
            WHERE TWT.W_TXN_ID = I_RRN
            AND TWT.W_REQUEST_TYPE = 'SALE'
            AND TWT.W_REFUND_APPROVED = '0'
            AND TWT.W_TERM_SER_NUM = I_TERMINAL_SERIAL_NUMBER;
        END IF;
    END IF;

    EXCEPTION 
    WHEN NO_DATA_FOUND THEN
        IF O_DEBUG_POINT = '1AB' THEN
            O_APP_ERROR_CODE := 'W-102';
        END IF;

    WHEN OTHERS THEN
        IF O_DEBUG_POINT = '1AA' THEN
            O_APP_ERROR_CODE := 'W-103';
        END IF;

END TCH_REFUND_WALLET_PROC;