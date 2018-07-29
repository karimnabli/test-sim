/**
 * 
 */
package org.kn.qa.sim;

import java.io.File;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Logger;

import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.webapp.WebAppContext;

public class JettyServer {
	private static final int START_PORT = Integer.parseInt(PropertiesReader.getProperty("portStart"));
	private static final int STOP_PORT = Integer.parseInt(PropertiesReader.getProperty("portStop"));
	private static final String WEB_MOCK_PATH = PropertiesReader.getProperty("webmockPath");
	private static final String API_MOCK_PATH = PropertiesReader.getProperty("apimockPath");
	private static final Logger LOGGER = Logger.getLogger(JettyServer.class.getName());
	private static final String WEB_WAR = PropertiesReader.getProperty("webWar");
	private static final String API_WAR = PropertiesReader.getProperty("apiWar");

	public JettyServer() {
		LOGGER.info("JettyServer: create instance");
		LOGGER.info("start port: " + START_PORT);
		LOGGER.info("stop port: " + STOP_PORT);
		LOGGER.info("api-mock path: " + API_MOCK_PATH);
		LOGGER.info("web-mock patht: " + WEB_MOCK_PATH);
	}

	public static void stop() {
		try {
			Socket s = new Socket(InetAddress.getByName("127.0.0.1"), STOP_PORT);
			LOGGER.info("Jetty stopping...");
			s.setSoLinger(false, 0);
			OutputStream out = s.getOutputStream();
			out.write(("stop\r\n").getBytes());
			out.flush();
			s.close();
		} catch (ConnectException e) {
			LOGGER.info("Jetty not running!");
		} catch (Exception e) {
			LOGGER.severe(e.getMessage());
		}
	}

	public static void main(String[] args) throws Exception {
		JettyServer server = new JettyServer();
		server.start();

	}

	private void start() {
		try {
			Server server = new Server(START_PORT);
			// Setup JMX
			MBeanContainer mbContainer = new MBeanContainer(ManagementFactory.getPlatformMBeanServer());
			server.addBean(mbContainer);
			WebAppContext web = new WebAppContext();
			web.setContextPath(WEB_MOCK_PATH);
			File webFile = new File(WEB_WAR);
			web.setWar(webFile.getAbsolutePath());

			WebAppContext api = new WebAppContext();
			api.setContextPath(API_MOCK_PATH);
			File apiFile = new File(API_WAR);
			api.setWar(apiFile.getAbsolutePath());

			ContextHandlerCollection contexts = new ContextHandlerCollection();
			LOGGER.info("Context Collections [OK]");
			contexts.setHandlers(new Handler[] { api, web });
			LOGGER.info("Server: handels contexts");
			server.setHandler(contexts);
			// Start things up!
			LOGGER.info("Server Started on http://localhost:7357");
			server.start();
			server.dumpStdErr();
			Monitor monitor = new Monitor(STOP_PORT, new Server[] { server });
			LOGGER.info("Start Monitor listing to events from the ports "+STOP_PORT );
			monitor.start();
			LOGGER.info("Monitor Started ....");
			server.join();
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
			LOGGER.info(e.getCause().getMessage());
			LOGGER.info(e.getStackTrace().toString());
			e.printStackTrace();
		}
	}
}