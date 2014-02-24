package client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class SimpleServer {
	
	public static void main(String[] args) {
		ServerSocket ss = null;
		try {
			ss = new ServerSocket(3333);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} // must be greater than 1000
		try {
			
			System.out.println("ServerSocket created");
			while(true) {
				Socket cs = ss.accept();
				System.out.println("Client connected");
				
				BufferedReader r = new BufferedReader(new InputStreamReader(cs.getInputStream()));
				PrintStream w = new PrintStream(cs.getOutputStream());
				String line;
				while((line = r.readLine()) != null) {
					System.out.println("Received: " + line);
					w.println(line.toUpperCase());
					//w.flush();
				}				
				w.close(); //implicitly closes everything when closes PrintWriter
				System.out.println("Close disconnected");
			}
		} catch (IOException e) {
			System.out.println("Error occurred, terminating");
			e.printStackTrace();
		} finally {
			if(!ss.isClosed()) {
				try {
					ss.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
} //on command line do telnet localhost 3333
//type something in it