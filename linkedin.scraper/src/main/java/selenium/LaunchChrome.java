package selenium;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.WebDriver;

public class LaunchChrome {

	private static WebDriver driver = null;
	
	public static void main(String[] args) {
		System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
		
		ChromeOptions browserConfig = new ChromeOptions();
//		browserConfig.addArguments("--headless"); 
		
		driver = new ChromeDriver(browserConfig);

	}

}
