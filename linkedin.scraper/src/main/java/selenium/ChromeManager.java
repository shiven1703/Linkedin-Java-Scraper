package selenium;

import logger.LogManager;
import utils.ConfigManager;
import scraper.Post;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ChromeManager {

	ConfigManager configManager = new ConfigManager();

	public boolean login(WebDriver driver) {

		boolean isSuccessfull = false;

		try {
			// opening login page
			driver.get("https://www.linkedin.com/login");
			LogManager.logInfo("Attempting Login");

			// wait until login page is loaded
			WebElement emailField = new WebDriverWait(driver, 20).until(
					ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[contains(@id,'username')]")));

			// email field is already fetched above, below will fetch password field
			WebElement passwordField = driver.findElement(By.xpath("//input[contains(@id,'password')]"));

			// input username and password
			emailField.sendKeys(configManager.getProperty("email"));
			passwordField.sendKeys(configManager.getProperty("password"));

			// find login button and perform click
			WebElement loginBtn = driver
					.findElement(By.xpath("//button[contains(@type,'submit') and text()='Sign in']"));
			loginBtn.click();

			// wait until home page is visible
			new WebDriverWait(driver, 20).until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("//div[contains(@class, 'neptune-grid three-column ghost-animate-in')]")));

			LogManager.logInfo("Login..................................................................[Success]");
			isSuccessfull = true;

		} catch (Exception e) {
			e.printStackTrace();
			LogManager.logError("Login Faild.");
			LogManager.logAlert("Possible Error: Wrong Username and Password.");
			LogManager.logAlert("\nPossible Error: Captcha Verification is preventing login.\n");
			LogManager.logAlert("Possible Error: Page is taking more then 20 seconds to load.\n");
		}

		return isSuccessfull;

	}

	public boolean moveToURL(WebDriver driver, String postURL) {
		boolean isFound = false;
		try {

			driver.get(postURL);
			// wait till post loads up
			new WebDriverWait(driver, 20).until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath("//section[contains(@class, 'fixed-full')]")));

			// check for possible error
			if (driver.findElements(By.xpath("//section[contains(@class, 'feed-shared-error')]")).size() > 0) {
				LogManager.logError("Error opening Post URL");
			}

			isFound = true;

		} catch (Exception e) {
			LogManager.logError("Error opening Post URL");
			e.printStackTrace();
		}
		return isFound;
	}

	public Post scrapComments(WebDriver driver) {
		Post post = new Post();
		
		// this code will load all comments
		try {
			while (driver.findElements(By.xpath("//span[text()='Load more comments']")).size() > 0) {
				driver.findElement(By.xpath("//span[text()='Load more comments']")).click();
				new WebDriverWait(driver, 5).until(
						ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='Load more comments']")));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		// below code will scrap the comment section of post after load all the comments
		WebElement commentSection = driver.findElement(By.xpath("//div[contains(@class, 'comments-comments-list')]"));
		for(WebElement comment: commentSection.findElements(By.xpath("//div[contains(@class, 'comments-comment-item-content-body')]"))) {
			System.out.println(comment.getText().toString());
		}
		return post;
	}
}
