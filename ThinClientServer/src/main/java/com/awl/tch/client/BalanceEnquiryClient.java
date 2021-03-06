package com.awl.tch.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class BalanceEnquiryClient {
	public static void main(String[] args) {
		
		long startTime = System.currentTimeMillis();
		
		int i=0;
		while(i<1)			
		{
			
       String hostName = "127.0.0.1";
		//	String hostName = "10.10.11.126";
        int portNumber = 9001;
 
        try (
            Socket kkSocket = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(kkSocket.getInputStream()));
        ) {
            
            String reqStr = "{ 	\"reqTyp\":	\"BALENQ\", 	\"reqObj\":	{ 		\"tSerNum\":	\"16060WL24120018\", 		\"cltId\":	\"TC0000000000446\", 		\"binNum\":	\"5186050060\", 		\"crdEntMd\":	\"SWIPE\", 		\"panNum\":	\"9F236B53D1235CD2131B939F20E6A58D\", 		\"expDt\":	\"1812\", 		\"stNum\":	\"000066\", 		\"trk2\":	\"2D21406585AE67C7671B39856C5C99F9958F384B9FF6896775CFCA0BD78F1C84F8460FCBD68408C4\",		\"lst4DgtVl\":	\"0031\", 		\"dt\":	\"170104\", 		\"tm\":	\"140413\" 	} }\n";
            //String req ="{ 	\"requestType\":	\"SALE\", 	\"requestObject\":	{ 		\"termSerNum\":	\"14298WL22307843\", 		\"binNumber\":	\"5300300000\", 		\"crdEntryMod\":	\"SWIPE\", 		\"panNum\":	\"5204740000001176\", 		\"expDate\":	\"1812\", 		\"orgAmount\":	\"11111\", 		\"track2\":	\"5204740000001176D181299911011111000F\", 		\"cltId\":	\"TC0000000000363\", 		\"invNumber\":	\"000001\", 		\"lstFourDgtVal\":	\"1176\", 		\"date\":	\"161017\", 		\"time\":	\"181528\" 	} }";
            out.print(reqStr);
            out.flush();
            String incommingMessage = "";
            char cbuf[] = new char[1024];
			incommingMessage = new String(cbuf);
			System.out.println("Server :"+incommingMessage);
            /*while ((fromServer = in.readLine()) != null) {
                System.out.println("Server: " + fromServer+":"+i);
                    
               
            }*/
			 Thread.sleep(1L);
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            System.exit(1);
        } catch (Exception e) {
        	 System.exit(1);
		}
        
        
        i++;
       
		}
		
		long endTime = System.currentTimeMillis();
		System.out.println(endTime - startTime );
		
	}
}
