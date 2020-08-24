package scraper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import logger.LogManager;
import selenium.ChromeManager;
import UI.UImanager;

public class Scraper {

	private static WebDriver driver;
	private static ChromeManager chromeManager ;

	public Scraper() {
		try {

			LogManager.logInfo("Start Scraper.....................................................[Success]");
			System.setProperty("webdriver.chrome.driver", "chromedriver.exe");

			// configurations
			ChromeOptions browserConfig = new ChromeOptions();
			// browserConfig.addArguments("--headless");
			browserConfig.addArguments("--incognito");
			driver = new ChromeDriver(browserConfig);
			chromeManager = new ChromeManager();

		} catch (Exception e) {
			LogManager.logError(e.toString());
			LogManager.logError("Error Loading Chrome Web Driver");
		}
	}

	public void startScraper() throws InterruptedException {

		boolean isLoaggedIn = performLogin();

		// login to account
		if (isLoaggedIn == true) {
			boolean isPostFound = moveToPostURL();
			// scraping comments from post
			if (isPostFound == true) {
				getPostComments();
			}
		}
		
		shutDownScraper();
//		shutDownChrome();

	}

	private boolean performLogin() {
		boolean loginStatus = chromeManager.login(driver);
		if(loginStatus == false) {
			shutDownChrome();
			shutDownScraper();
		}else {
			UImanager.updateLoginStatus("LoggedIn");
		}
		return loginStatus;
	}
	
	private boolean moveToPostURL() {
		boolean isSuccessfull = chromeManager.moveToURL(driver, UImanager.getLinkedInPostLink());
		if(isSuccessfull == false) {
			shutDownChrome();
			shutDownScraper();
		}
		return isSuccessfull;
	}

	private Post getPostComments() {
		Post postObj = chromeManager.scrapComments(driver);
		return postObj;
	}

	private boolean exportToExcel() {
		return true;
	}

	public static void shutDownScraper() {
		// below statements will execute when thread status is set to false
		UImanager.setScraperThreadStatus(false);
		LogManager.logAlert("                       -------------------- Scraper Stopped. ------------------");
		UImanager.btnStart.setEnabled(true);
		UImanager.btnStop.setEnabled(false);
	}

	public static void shutDownChrome() {
		try {
			driver.quit();
			UImanager.updateLoginStatus("LoggedOut");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
