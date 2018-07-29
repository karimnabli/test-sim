package org.kn.qa.sim;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

import org.eclipse.jetty.server.Server;

public class Monitor extends Thread {

	private static Logger LOGGER = Logger.getLogger(Monitor.class.getName());

	private Server[] servers;
	private ServerSocket serverSocket;

	public Monitor(int port, Server[] servers) throws IOException {
		if (port <= 0) {
			throw new IllegalStateException("Bad stop PORT");
		}
		this.servers = servers;
		setDaemon(true);
		setName("StopJettyMonitor");
		serverSocket = new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"));
		serverSocket.setReuseAddress(true);
	}

	public void run() {
		while (serverSocket != null) {
			Socket socket = null;
			try {
				socket = serverSocket.accept();
				socket.setSoLinger(false, 0);
				LineNumberReader lin = new LineNumberReader(new InputStreamReader(socket.getInputStream()));
				String cmd = lin.readLine();
				if ("stop".equals(cmd)) {
					try {
						socket.close();
					} catch (Exception e) {
						LOGGER.warning(e.getMessage());
					}
					try {
						serverSocket.close();
					} catch (Exception e) {
						LOGGER.warning(e.getMessage());
					}
					serverSocket = null;

					for (int i = 0; servers != null && i < servers.length; i++) {
						try {
							LOGGER.info("Stopping server " + i);
							servers[i].stop();
						} catch (Exception e) {
							LOGGER.severe(e.getMessage());
						}
					}

				} else
					LOGGER.info("Unsupported monitor operation");
			} catch (Exception e) {
				LOGGER.severe(e.getMessage());
			} finally {
				if (socket != null) {
					try {
						socket.close();
					} catch (Exception e) {
						LOGGER.severe(e.getMessage());
					}
				}
			}
		}
	}
}