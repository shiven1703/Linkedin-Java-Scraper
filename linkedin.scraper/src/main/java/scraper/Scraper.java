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
import UI.UImanager;

public class Scraper {
	
	private static WebDriver driver = null;
	ArrayList<Order> orders = null;
	private String propertiFilePath = "src/main/resources/config.properties";
	private static String intervalFilePath = "src/main/resources/intervalConfig.txt";

	public Scraper() {
		try {
			
			LogManager.logInfo("Start Scraper.....................................................[Success]");
			System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
			
			//configrations
			ChromeOptions chromeOptions = new ChromeOptions();
			 chromeOptions.addArguments("--headless"); 
			
			driver = new ChromeDriver(chromeOptions);

		} catch (Exception e) {
			LogManager.logError(e.toString());
			LogManager.logError("Error Loading Chrome Web Driver");
		}

	}

	public void startScraper() throws InterruptedException {
		
//		boolean loginStatus = performLogin();
//		// login
//		if(loginStatus == true) {
//			boolean scrapingStatus = scrapData();
//			// scraping
//			if(scrapingStatus == true) {
//				
//				boolean dbStatus = Database.connect();
//				// db
//				if(dbStatus == true) {
//					boolean DataPushStatus = Database.pushToDB(orders);
//					
//					if(DataPushStatus == true) {
//						// closing browser
//						driver.quit();
//						LogManager.logInfo("Close Scraper..................................................[Success]");
//						
//						// calculating timer interval
//						Calendar now = Calendar.getInstance();
//						int scraperInterval = getScraperInterval().getIntervalTime();
//						long scraperIntervalInMili = 0;
//						String scraperIntervalType = getScraperInterval().getIntervalType();
//						
//						if(scraperIntervalType.equals("milisec")) {
//							
//							now.add(Calendar.MILLISECOND, scraperInterval);
//						
//						}else if(scraperIntervalType.equals("Second")){
//						
//							now.add(Calendar.SECOND, scraperInterval);
//							scraperIntervalInMili = TimeUnit.SECONDS.toMillis(scraperInterval);
//						
//						}else if(scraperIntervalType.equals("Minute")) {
//						
//							now.add(Calendar.MINUTE, scraperInterval);
//							scraperIntervalInMili = TimeUnit.MINUTES.toMillis(scraperInterval);
//						
//						}else if(scraperIntervalType.equals("Hour")) {
//						
//							now.add(Calendar.HOUR, scraperInterval);
//							scraperIntervalInMili = TimeUnit.HOURS.toMillis(scraperInterval);
//						
//						}
//						
//						if(GUI.getScraperThreadStatus() == false) {
//							
//							shutDownScraper();
//							
//						}else {
//							// putting on sleep till next interval
//							LogManager.logAlert("                           ********* Next Scrap on : " +  now.get(Calendar.HOUR_OF_DAY) + ":"+ now.get(Calendar.MINUTE) + ":"+ now.get(Calendar.SECOND) + " *********\n");
//							Thread.sleep(scraperIntervalInMili);
//						}
//						
//						
//						
//					}
//				}
//				
//			}
//				
//		}
	}
	
	private boolean scrapData() {
		
		
		try {
			orders = new ArrayList<Order>();
			driver.get(getProperti("ordersPageURL"));
			new WebDriverWait(driver, 20).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'groups-view')]//div[contains(@class, 'group-view relative checkbox-present')]")));
			
		}catch(Exception e) {
			LogManager.logError("Error In Opening Orders Page URL");
		}
		
		try {
			// fetching all orders list
			List<WebElement> ordersList = driver.findElements(By.xpath("//div[contains(@class, 'groups-view')]//div[contains(@class, 'group-view relative checkbox-present')]"));
			
			for(WebElement orderDiv: ordersList) {
				// fetching order details
				String orderNumber = orderDiv.findElement(By.xpath(".//table/tbody/tr/td[1]")).getText().split(" ")[2];
				String orderDate = orderDiv.findElement(By.xpath(".//table/tbody/tr/td[2]")).getText();
				int productsCount = Integer.parseInt(orderDiv.findElement(By.xpath(".//div[contains(@class, 'order-view-order-info')]//div[2]//table/tbody/tr/td")).getText().split(" ")[0]);
				// fetching order products list
				Order mainOrder = new Order(orderNumber, orderDate, productsCount);
				List<WebElement> orderItems = orderDiv.findElements(By.xpath(".//div[contains(@class, 'group-suborders')]//div[contains(@class, 'group-suborder')]"));
				
				for(WebElement item: orderItems) {
					
					Product p = new Product();
					// fetching product details
					String productName = item.findElement(By.xpath(".//div[contains(@class, 'order-product relative delay-shipping checkbox-present ')]//div[1]")).getAttribute("textContent").split("Qty")[0];			
					String qty = item.findElement(By.xpath(".//div[contains(@class, 'order-product relative delay-shipping checkbox-present ')]//div[1]")).getAttribute("textContent").split("Qty:")[1].trim();			
					String subOrderNumber = item.findElement(By.xpath(".//div[2]//div[2]//div[contains(@class,'display-block padding-bottom-5')][1]")).getText().split(":")[1].trim();
					String size = item.findElement(By.xpath(".//div[2]//div[2]//div[contains(@class,'display-block padding-bottom-5')][2]")).getAttribute("textContent").split(":")[1].trim();
					String productSKU = item.findElement(By.xpath(".//div[2]//div[2]//div[3]")).getText().split(":")[1].trim();
					String meeshoProductId = item.findElement(By.xpath(".//div[2]//div[2]//div[4]")).getText().split(":")[1].trim();
					String SLAStatus = item.findElement(By.xpath(".//div[2]//div[contains(@class, 'padding-b-5')][1]")).getAttribute("textContent").split(":")[1].trim();
					String expactedDispatchDate = item.findElement(By.xpath(".//div[2]//div[contains(@class, 'padding-b-5')][2]//table/tbody/tr/td/strong")).getText();
					String imgURL = item.findElement(By.xpath(".//div[1]//div[1]/img")).getAttribute("src");
					
					p.setProductName(productName);
					p.setQty(qty);
					p.setSubOrderNumber(subOrderNumber);
					p.setSize(size);
					p.setProductSKU(productSKU);
					p.setMeeshoProductId(meeshoProductId);
					p.setSLAStatus(SLAStatus);
					p.setExpactedDispatchDate(expactedDispatchDate);
					p.setImgURL(imgURL);
					
					mainOrder.addProduct(p);	
				}
				
				orders.add(mainOrder);
				
			}
			LogManager.logInfo("Scraping Data...................................................[Success]");
			return true;
			
		}catch(Exception e) {
			LogManager.logError("Error in Scraping Data");
			shutDownScraper();
			return false;
		}
		
	}

	private boolean performLogin() {
		try {
			
			driver.get(getProperti("siteURL"));
			LogManager.logInfo("Attempting Login");
			
			// wait until login page is loaded
			WebElement email = new WebDriverWait(driver, 20).until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("//input[contains(@class,'form-control login-input login-envelope')]")));

			// input username and password
			email.sendKeys(getProperti("loginUsername"));
			WebElement password = driver
					.findElement(By.xpath("//input[contains(@class,'form-control login-input login-password')]"));
			password.sendKeys(getProperti("loginPassword"));

			// click login button
			WebElement loginBtn = driver
					.findElement(By.xpath("//button[contains(@class,'login-button login-button-enable')]"));
			loginBtn.click();

			new WebDriverWait(driver, 20).until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("//div[contains(@class,'supplier-mobile-view page-content')]")));
			LogManager.logInfo("Login..................................................................[Success]");
			return true;

		} catch (Exception e) {
			LogManager.logInfo("Login Failed");
			shutDownScraper();
			return false;
		}
	}
	
	public static void ShutDown() {
		try {
			
			driver.quit();
			
		}catch(Exception e) {
			
		}
	}
	
	public static void shutDownScraper() {
		// below statements will execute when thread status is set to false
//		UImanager.setScraperThreadStatus(false);
//		LogManager.logAlert("                       -------------------- Scraper Stopped. ------------------");
//		UImanager.btnStart.setEnabled(true);
//		UImanager.btnStop.setEnabled(false);
	}
	
	private String getProperti(String propertiName) {
		try {

			Properties scraperProperties = new Properties();
//			scraperProperties.load(new FileInputStream(propertiFilePath));
			scraperProperties.load(Scraper.class.getResourceAsStream("/config.properties"));
			return scraperProperties.getProperty(propertiName);

		} catch (FileNotFoundException e) {
			LogManager.logError("Config file Reading error");
			return "";
		} catch (IOException e) {
			LogManager.logError("Config file Reading error");
			e.printStackTrace();
			return "";
		}
	}
	
	public static interval getScraperInterval() {
		
		interval inter = null;

		String data = "";
		int c = 0;
		FileReader r;
		try {
			r = new FileReader("./intervalConfig.txt");
			while((c = r.read()) != -1) {
				data = data + (char)c;
			}
			String[] values = data.split(",");
			inter = new interval();
			inter.setIntervalTime(Integer.parseInt(values[0]));
			inter.setIntervalType(values[1]);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
//        try {
////            FileInputStream file = new FileInputStream(intervalFilePath);
//        	InputStream is = Scraper.class.getResourceAsStream("/intervalConfig.txt");
//            ObjectInputStream oi = new ObjectInputStream(is);
//            
//            inter = (interval) oi.readObject();
//            oi.close();
//            is.close();
////            file.close();
//            return inter;
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            // LogManager.logError("Interval details reading error");
//        } catch (IOException e) {
//            e.printStackTrace();
//            // LogManager.logError("Interval details reading error");
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//            // LogManager.logError("Interval details reading error");
//        }

        return inter;
	}
	
	public static void setScraperInterval(int intervalTime, String intervalTtype) {
		
		FileWriter f;
		try {
			f = new FileWriter("./intervalConfig.txt");
			f.write(intervalTime + "," + intervalTtype);
			f.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
//		 try {
//			 	
//	            FileOutputStream file = new FileOutputStream("");
//	            ObjectOutputStream ow = new ObjectOutputStream(file);
//
//	            interval inter = new interval(intervalTime, intervalTtype);
//	            ow.writeObject(inter);
//	            ow.close();
//	            file.close();
//
//	        } catch (FileNotFoundException e) {
//	            e.printStackTrace();
//	            // LogManager.logError("Interval details Writing error");
//	        } catch (IOException e) {
//	            e.printStackTrace();
//	            // LogManager.logError("Interval details Writing error");
//	        } 
	}
	
}
	
