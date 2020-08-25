package utils;

import scraper.Post;
import scraper.Comment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import logger.LogManager;

public class ExcelSheetManager {

	public boolean createExcelSheet(Post post) {

		boolean isCreated = false;

		// below code will create workbook > spreadsheet > row;
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet spreadsheet = workbook.createSheet(" Employee Info ");
		int rowCounter = 0;
		FileOutputStream out;

		Map<String, Object[]> data = new TreeMap<String, Object[]>();
		rowCounter++;

		// titles for excel sheet
		data.put(Integer.toString(rowCounter), new Object[] { "Name", "Comment", "Email Address", "Profile URL" });

		// preparing object for excel file
		for (Comment comment : post.getAllComments()) {
			rowCounter++;
			data.put(Integer.toString(rowCounter),
					new Object[] { comment.getAuthor(), comment.getCommentText(), comment.getEmail(), comment.getProfileLink() });
		}

		// Writing data to excel file
		XSSFRow row;
		Set<String> keyid = data.keySet();
		int rowid = 0;

		for (String key : keyid) {
			row = spreadsheet.createRow(rowid++);
			Object[] objectArr = data.get(key);
			int cellid = 0;

			for (Object obj : objectArr) {
				Cell cell = row.createCell(cellid++);
				cell.setCellValue((String) obj);
			}
		}
		

		// Write the workbook in file system
		try {
			String filename = "data_" + getCurrentDateTime() + ".xlsx";
			out = new FileOutputStream(new File(filename));
			workbook.write(out);
			out.close();
			isCreated = true;
			LogManager.logInfo("Excel File Generated in current folder.");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return isCreated;

	}
	
	public String getCurrentDateTime() {
		Date date = new Date(); 
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy_HH_mm_ss");
		return formatter.format(date); 
	}

}
