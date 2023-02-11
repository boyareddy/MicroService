package com.roche.connect.appinstaller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

public class SaveToExcelFile {
	SaveYamlProperties saveYamlProperties = new SaveYamlProperties();

	public void saveToExcel(Map<String, String> input) {
		String password = input.get("password");
		String loginName = input.get("loginName");
		String email = input.get("email");
		input.put("password", getEncodedPasssord(loginName, password, email));
		input.put("userId", loginName + "#@#" + email.split("@")[1]);
		input.put("enabled", "TRUE");
		input.put("retired", "FALSE");
		input.put("locked", "FALSE");
		input.put("company", "1");
		input.put("roles", "R12");
		input.put("userPreferences", "R12");
		input.put("contact", "R12");
		saveToExcelSheet(input, saveYamlProperties.getFilepath("sec.securtiyuser.excel.sheetname"),
				saveYamlProperties.getFilepath("brownstone.sec.init.excel.location"));
		
		saveToExcelSheet(input, saveYamlProperties.getFilepath("sec.passwordhistory.excel.sheetname"),
				saveYamlProperties.getFilepath("brownstone.sec.init.excel.location"));
		
		saveToExcelSheet(input, saveYamlProperties.getFilepath("metadata.user.excel.sheetname"),
				saveYamlProperties.getFilepath("brownstone.metadata.init.excel.location"));
		
		saveToExcelSheet(input, saveYamlProperties.getFilepath("app.pasuser.excel.sheetname"),
				saveYamlProperties.getFilepath("pas.admin.api.init.excel.location"));
		
		saveToExcelSheet(input, saveYamlProperties.getFilepath("app.application.user.execl.sheetname"),
				saveYamlProperties.getFilepath("pas.email.init.excel.location"));
		
		saveToExcelSheet(input, saveYamlProperties.getFilepath("app.application.user.execl.sheetname"),
				saveYamlProperties.getFilepath("brownstone.app.init.execl.location"));
	}

	public void saveToExcelSheet(Map<String, String> input, String sheetName, String fileLocation) {
		try {
			FileInputStream file = new FileInputStream(new File(fileLocation));
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet workSheet = workbook.getSheet(sheetName);
			Row typeRow = workSheet.getRow(workSheet.getFirstRowNum());
			Row headerRow = workSheet.getRow(workSheet.getFirstRowNum() + 2);
			int size = headerRow.getLastCellNum() - headerRow.getFirstCellNum();
			int rowIndex = workSheet.getLastRowNum();
			Row newRow = workSheet.createRow(rowIndex+1);
			int startingIndex = headerRow.getFirstCellNum();
			System.out.println("SheetName :"+sheetName + "\n Workbook name :"+fileLocation+"\n ---------------");
			for (Map.Entry<String, String> entry : input.entrySet()) {
				for (int i = 0; i < size; i++) {
					if ((headerRow.getCell(startingIndex + i).getStringCellValue().equals(entry.getKey()))) {
						Cell cell = newRow.createCell(startingIndex + i);
						if (entry.getValue().toLowerCase().equalsIgnoreCase("true")
								|| entry.getValue().toLowerCase().equalsIgnoreCase("false"))
							cell.setCellValue(Boolean.valueOf(entry.getValue()));
						else
							cell.setCellValue(entry.getValue());
					}
				}
			}
			file.close();
			FileOutputStream fout = new FileOutputStream(new File(fileLocation));
			workbook.write(fout);
			fout.close();
			System.out.println("Excel updated successfully");

		} catch (FileNotFoundException e) {
			System.out.println("Targeted file is missing / opened already :" + e.getMessage());
		} catch (IOException e) {
			System.out.println("Targerted file is missing: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getEncodedPasssord(String username, String password, String email) {
		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		return encoder.encodePassword(password, username + "#@#" + email.split("@")[1]);
	}

}
