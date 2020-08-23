package logger;

import java.awt.Color;
import java.awt.Rectangle;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import UI.UImanager;

public class LogManager {

	static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");;
	static LocalDateTime today = null;
	static String logDate = "";
	
	
	
	public static void logError(String errorMsg) {
		today = LocalDateTime.now();
		logDate = formatter.format(today).toString() + " - ";
		UImanager.appendToPane(logDate, Color.BLACK);
		UImanager.appendToPane(errorMsg+ "\n", Color.RED);
		UImanager.logwindow.scrollRectToVisible(new Rectangle(0,UImanager.logwindow.getHeight(),1,20));
	}
	
	public static void logInfo(final String infoMsg) {
		today = LocalDateTime.now();
		logDate = formatter.format(today).toString() + " - ";
		UImanager.appendToPane(logDate, Color.RED);
		UImanager.appendToPane(infoMsg + "\n", new Color(6, 138, 6));
		UImanager.logwindow.scrollRectToVisible(new Rectangle(0,UImanager.logwindow.getHeight(),1,20));
	}
	
	public static void logAlert(final String alertMsg) {
		today = LocalDateTime.now();
		logDate = formatter.format(today).toString() + " - ";
		UImanager.appendToPane(alertMsg, Color.BLUE);
		UImanager.logwindow.scrollRectToVisible(new Rectangle(0,(UImanager.logwindow.getHeight())+5,1,100));
	}


	
}
