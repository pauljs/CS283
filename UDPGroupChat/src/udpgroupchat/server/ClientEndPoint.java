package udpgroupchat.server;

import java.net.InetAddress;

public class ClientEndPoint {
	protected final InetAddress address;
	protected final int port;
	protected final ClientTimerTask timerTask;
	
	public ClientEndPoint(InetAddress addr, int port, ClientTimerTask timerTask) {
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
