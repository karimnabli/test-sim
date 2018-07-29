/**
 * 
 */
package org.kn.qa.sim;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * @author karim
 *
 */
public class PropertiesReader {
	private static final Logger LOGGER = Logger.getLogger(PropertiesReader.class.getName());

	public static String getProperty(String key) {
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = PropertiesReader.class.getClassLoader().getResourceAsStream("jetty.properties");																									
			prop.load(input);
			return prop.getProperty(key);
		} catch (FileNotFoundException e) {
			LOGGER.severe("jetty.properties not found!");
			e.printStackTrace();
		} catch (IOException e) {
			LOGGER.severe("Error! " + e.getMessage());
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					LOGGER.severe("Error: close InputStream! " + e.getMessage());
					e.printStackTrace();
				}
			}
		}

		return null;

	}
}
