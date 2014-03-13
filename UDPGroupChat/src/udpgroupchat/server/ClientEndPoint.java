package udpgroupchat.server;

import java.net.InetAddress;
import java.util.TimerTask;

public class ClientEndPoint {
	protected final InetAddress address;
	protected final int port;
	protected final TimerTask timerTask;
	
	public ClientEndPoint(InetAddress addr, int port, TimerTask timerTask) {
		this.address = addr;
		this.port = port;
		this.timerTask = timerTask;
	}

	@Override
	public int hashCode() {
		// the hashcode is the exclusive or (XOR) of the port number and the hashcode of the address object
		return this.port ^ this.address.hashCode();
	}
	
	
}
