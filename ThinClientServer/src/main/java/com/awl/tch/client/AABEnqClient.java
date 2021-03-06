package com.awl.tch.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class AABEnqClient {
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		int i=150;
		while(i<151)			
		{
			
       String hostName = "127.0.0.1";
        int portNumber = 9001;
 
        try (
            Socket kkSocket = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(kkSocket.getInputStream()));
        ) {
        	String str = "{ 	\"reqTyp\":	\"ENQ\", 	\"reqObj\":	{ 		\"tSerNum\":	\"16256WL24997206\", 		\"cltId\":	\"TC0000000000476\", 		\"appNm\":	\"AAB\", 		\"unqId\":	\"12345\", 		\"refVl\":	\"512\", 			\"dt\":	\"161203\", 		\"tm\":	\"165613\" 	} }\n";
            out.print(str);
            out.flush();
            String incommingMessage = "";
            char cbuf[] = new char[4096];
			int charRead =  in.read(cbuf);
			incommingMessage = new String(cbuf);
			System.out.println("Server :"+charRead + ":"+incommingMessage);
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
