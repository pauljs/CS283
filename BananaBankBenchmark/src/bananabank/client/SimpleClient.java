package bananabank.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;


public class SimpleClient {

	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket s = new Socket("localhost", 2000);
		System.out.println("Client is connected to the server");
		PrintStream w = new PrintStream(s.getOutputStream());
		w.println("100 22222 55555");
		BufferedReader r = new BufferedReader(new InputStreamReader(s.getInputStream()));
		String line;
		line = r.readLine();
		System.out.println(line);
		w.close();
		//w.flush();
		/*
		System.out.println("Client Receiving");
		BufferedReader r;
		try {
			r = new BufferedReader(new InputStreamReader(s.getInputStream()));
			String line;
			while((line = r.readLine()) != null) {
				line = r.r
				System.out.println("Received: " + line);
			}
			w.close();//implicitly closes everything when closes PrintWriter
			System.out.println("Close disconnected");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(!s.isClosed()) {
				s.close();
			}
		}*/
		System.out.println("Client finished");
	}
}
