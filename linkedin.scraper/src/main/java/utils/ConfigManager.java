package utils;

import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;

public class ConfigManager {

	private FileBasedConfiguration configuration;
	private FileBasedConfigurationBuilder<FileBasedConfiguration> builder;

	public ConfigManager() {
		Parameters params = new Parameters();
		builder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
				.configure(params.properties().setFileName("config.properties"));
		try {
			configuration = builder.getConfiguration();
			builder.setAutoSave(true);
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}

	public String getProperty(String key) {
		return (String) configuration.getProperty(key);
	}

	public boolean setProperties(String email, String password) {
		boolean isUpdated = false;
		try {
			configuration.setProperty("email", email);
			configuration.setProperty("password", password);
			configuration.setProperty("isCredentialsUpdated", "true");
			isUpdated = true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return isUpdated;
	}
}
