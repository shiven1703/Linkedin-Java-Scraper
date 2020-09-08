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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import logger.LogManager;
import selenium.ChromeManager;
import utils.ExcelSheetManager;
import UI.UImanager;

public class Scraper {

	private static WebDriver driver;
	private static ChromeManager chromeManager;

	public Scraper() {
		try {

			LogManager.logInfo("Start Scraper.....................................................[Success]");
			String osName = System.getProperty("os.name").toUpperCase();
			if(osName.contains("WINDOWS"))
			{
				System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
			}
			else
			{	
				System.setProperty("webdriver.chrome.driver", "chromedriver");
			}
			// configurations
			ChromeOptions browserConfig = new ChromeOptions();
//			browserConfig.addArguments("--headless");
			browserConfig.addArguments("--incognito");
			browserConfig.setPageLoadStrategy(PageLoadStrategy.EAGER);
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
				Post postObj = getPostComments();
				postObj = extractEmails(postObj);
				exportToExcel(postObj);
			}
		}

		shutDownScraper();
		shutDownChrome();

	}

	private boolean performLogin() {
		boolean loginStatus = chromeManager.login(driver);
		if (loginStatus == false) {
			shutDownChrome();
			shutDownScraper();
		} else {
			UImanager.updateLoginStatus("LoggedIn");
		}
		return loginStatus;
	}

	private boolean moveToPostURL() {
		boolean isSuccessfull = chromeManager.moveToURL(driver, UImanager.getLinkedInPostLink());
		if (isSuccessfull == false) {
			shutDownScraper();
			shutDownChrome();
		}
		return isSuccessfull;
	}

	private Post getPostComments() {
		Post postObj = chromeManager.scrapComments(driver);
		Scraper.shutDownChrome();
		return postObj;
	}

	private Post extractEmails(Post post) {
		System.out.println("Extracing Emails");
		for (int i = 0; i < post.getAllComments().size(); i++) {
			Matcher matcher = Pattern.compile("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}")
					.matcher(post.comments.get(i).getCommentText().toString());
			String email = "";
			while (matcher.find()) {
//					System.out.println("Comment Text : " + post.comments.get(i).getCommentText());
//					System.out.println("Email : " + matcher.group());
				email = email + matcher.group() + ",";
			}
			if (email != null && email.length() > 0) {
				email = email.substring(0, email.length() - 1);
			}
			post.comments.get(i).setEmail(email);
		}
		return post;
	}

	private boolean exportToExcel(Post post) {
		LogManager.logInfo("Generating Excel File.");
		ExcelSheetManager excelManager = new ExcelSheetManager();
		boolean isSuccessfull = excelManager.createExcelSheet(post);
		if (isSuccessfull == false) {
			LogManager.logError("Excel File Creation Failed.");
		}
		return isSuccessfull;
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
//			e.printStackTrace();
		}
	}

}
