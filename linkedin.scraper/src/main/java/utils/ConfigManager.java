package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ConfigManager {

	public boolean setProperties(String email, String password, boolean isCredentialsUpdated) {
		boolean isSuccessfull = false;
		FileOutputStream f;
		try {
			f = new FileOutputStream(new File("config.txt"));
			ObjectOutputStream o = new ObjectOutputStream(f);
			
			Config newConfig = new Config(email, password, isCredentialsUpdated);
			o.writeObject(newConfig);
			
			o.close();
			f.close();
			isSuccessfull = true;
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isSuccessfull;
	}
	
	public String getProperty(String property) {
		FileInputStream fi;
		try {
			
			File f = new File("config.txt");
			if(f.exists() == false) {
				f.createNewFile();
			}
			
			fi = new FileInputStream(new File("config.txt"));
			ObjectInputStream oi = new ObjectInputStream(fi);
			
			Config config = (Config)oi.readObject();
			
			if(property.equals("email")) {
				return config.email.toString();
			}else if(property.equals("password")) {
				return config.password.toString();
			}else if(property.equals("isCredentialsUpdated")) {
				return Boolean.toString(config.isCredentialsUpdated);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
}


class Config implements Serializable{
	
	public String email;
	public String password;
	public boolean isCredentialsUpdated;
	
	public Config(String email, String password, boolean isCredentialsUpdated){
		this.email = email;
		this.password = password;
		this.isCredentialsUpdated = isCredentialsUpdated;
	}
	
}
