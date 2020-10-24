# Linkedin-Java-Scraper
The Scraper is built with Java and Selenium. You can enter Username and password of LinkedIn account along with the URL of the post and scraper will start a chrome instance and scrap all the comments(Top level), emails, and users profile URL. The data will be available in the folder where you .jar file in an excel format.

# Dependencies

- Java
- Chrome
- Selenium chrome driver (According to your chrome version)

###### For scrapy runtime Server
```
Clone the repo

1) Create a new folder on your system

2) Copy latest .jar file available in the "EXE" folder of the repo and put it into the folder you created in the above step

3) Install Chrome

4) Find your chrome version and download selenium chrome driver for that version (Driver link: https://chromedriver.chromium.org/downloads)

5) Copy downloaded chromedriver in the same folder where you .jar file is

6) Run the .jar file

7) all done
```

# Note
The application runs in two steps. In the first, it will perform login and load all the comments on the web page by opening the post URL(It will wait for 60 sec after loading all the comments to handle network issue). Now, in the second part, it will start scraping the comments (By waiting random time between 1 to 30 seconds).

# Home
![Screenshot (125)](https://user-images.githubusercontent.com/32254687/97078177-d0e23580-1607-11eb-9c90-3089718abacd.png)

# config
![Screenshot (142)](https://user-images.githubusercontent.com/32254687/97078211-17d02b00-1608-11eb-94d7-c88d78c95511.png)

# Performing login
![Screenshot (132)](https://user-images.githubusercontent.com/32254687/97078245-6b427900-1608-11eb-8e55-324506382b4a.png)

# Loading comments
![Screenshot (138)](https://user-images.githubusercontent.com/32254687/97078258-9d53db00-1608-11eb-83ae-8223b44852e7.png)

# Scraping comments
![Screenshot (139)](https://user-images.githubusercontent.com/32254687/97078290-f3c11980-1608-11eb-9378-ee8227b0b474.png)

# Scraped data (Excel file)
![Screenshot (141)](https://user-images.githubusercontent.com/32254687/97078306-19e6b980-1609-11eb-8a62-951897395841.png)
