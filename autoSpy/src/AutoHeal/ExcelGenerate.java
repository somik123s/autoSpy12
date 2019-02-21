package AutoHeal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;



import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.nodes.Element;


public class ExcelGenerate {
	public static void excelWrite(ArrayList<String> xpathList, ArrayList<String> xpathNameList, String sheetName){
		try {
			System.out.println(xpathList.size());
			FileOutputStream fos;
			FileInputStream inputStream;
			XSSFWorkbook wb;
			int flag=0;
			String excelName = "D:\\newWorkSpace\\xpathGenerator.xlsx";
			String pageTitle = "";
			File file = new File(excelName);
			if(!file.exists()) {
				fos = new FileOutputStream(file);
				wb = new XSSFWorkbook();
			}
			
			else {
				inputStream = new FileInputStream(excelName);
				wb = new XSSFWorkbook(inputStream);
			}
			
			XSSFSheet sheet = null;
			if(wb.getNumberOfSheets()!=0) {
				for (int i = 0; i < wb.getNumberOfSheets(); i++) {
			           sheet = wb.getSheetAt(i);
			          int l = sheet.getLastRowNum();
			          if(l!=0) {
			        	  pageTitle = sheet.getRow(0).getCell(1).getStringCellValue();
			        	  if(!pageTitle.equalsIgnoreCase(sheetName)) {
			        		  if(i!=wb.getNumberOfSheets()-1) {
			        			  continue;
			        		  }
			        		  else {
			        			  sheet = wb.createSheet("Page"+(i+2));
			        			  EcelUtility.storingData(wb,sheet,sheetName,xpathList,xpathNameList,excelName);			        			  
			        		  }
			        	  }
			        	  //sheet = wb.createSheet("Page"+(i=1));
			        	  else {
			        		  for(int m=0;m<xpathNameList.size();m++) {
			        			  String elName = xpathNameList.get(m);
			        			  for(int n=2;n<sheet.getLastRowNum();n++) {
			        				  
			        				  
			        				  if(!(elName.equals(sheet.getRow(n).getCell(0).getStringCellValue()))) {
			        					  if(n!=l-1) {
			        						  continue;
			        					  } 
			        				  }
			        				  
			        				  else if(elName.equals(sheet.getRow(n).getCell(0).getStringCellValue())){
			        					  
			        					  if(sheet.getRow(n).getCell(1).getStringCellValue().equals(xpathList.get(m))) {
			        						  sheet.getRow(n).createCell(3).setCellValue("Matched");
			        						  sheet.getRow(n).createCell(4).setCellValue(xpathList.get(m));
			        						  break;
			        						  
			        					  }
			        					  
			        					  else{
			        						  sheet.getRow(n).createCell(3).setCellValue("To Be Replaced");
			        						  sheet.getRow(n).createCell(4).setCellValue(xpathList.get(m));
			        						  break;
			        					  }
			        				  }		        				  
			        			  }
			        		  }
			        	  }			        	  
			          }
			        }
			}
			
			else {
				sheet = wb.createSheet("Page1");
				EcelUtility.storingData(wb,sheet,sheetName,xpathList,xpathNameList,excelName);
			}
			
			
			/*XSSFRow titleRow = sheet.createRow(0);
			titleRow.createCell(0).setCellValue("PageTitle");
			titleRow.createCell(1).setCellValue(sheetName);
			XSSFRow r = sheet.createRow(1);
			XSSFCell c = r.createCell(1);
			XSSFCell n = r.createCell(0);
			CellStyle style = wb.createCellStyle();
			style.setFillBackgroundColor(IndexedColors.GREEN.getIndex());
			style.setFillPattern(FillPatternType.FINE_DOTS);
			style.setAlignment(HorizontalAlignment.CENTER);
			Font font = wb.createFont();
			font.setColor(IndexedColors.WHITE.getIndex());
			style.setFont(font);
			
			n.setCellValue("Element Names");
			n.setCellStyle(style);
			c.setCellValue("Captured Xpaths");
			c.setCellStyle(style);
			for(int i=1; i<=xpathList.size(); i++) {
				
				XSSFRow row = sheet.getRow(i+1);
				
				if(row == null) {
					row = sheet.createRow(i+1);
				}
				
				XSSFCell cellEleName = row.createCell(0);
				XSSFCell cellXpath = row.createCell(1);
				cellXpath.setCellValue(xpathList.get(i-1));
				cellEleName.setCellValue(xpathNameList.get(i-1));
				}*/
//				inputStream.close();
				FileOutputStream fileOut = new FileOutputStream(excelName);

				//write this workbook to an Outputstream.
				wb.write(fileOut);
				fileOut.close();
			
		}catch(Exception e) {
			e.getMessage();
		}
	}
	
	public static void createInnerExcel(String path, Element element) {
		
		List attributeList = element.attributes().asList();
		Map<String, String> xpathMap = new HashMap<String, String>();
		for(int i=0; i<attributeList.size(); i++) {
			String[] attributeDetails = attributeList.get(i).toString().split("=\"");
			compareAttributeColumn(path, attributeDetails);
//			System.out.println(attributeList.get(i));
		}
		
	}
	
	static void compareAttributeColumn(String path, String[] attributeDetails) {
		
		System.out.println(path+ "=*/*/*/*/*/*/*/**//*=" + attributeDetails[0]);
		
		
		try {
			ArrayList<String> pathList = new ArrayList<String>();
			String excelFile = "C:\\newWorkSpace\\inner.xlsx";
			String sheetName = "test";
			FileInputStream inputStream = new FileInputStream(excelFile);
			XSSFWorkbook wb = new XSSFWorkbook(inputStream);
			XSSFSheet sheet = wb.getSheetAt(0);
			Row row = sheet.getRow(0);
			int noOfColumns = 0;
			if(row != null) {
				noOfColumns = sheet.getRow(0).getPhysicalNumberOfCells();
			}else {
				sheet.createRow(0);
				noOfColumns = sheet.getRow(0).getPhysicalNumberOfCells();
			}
			String tabaName = "test";
//			System.out.println("***"+noOfColumns);
			
			if(noOfColumns == 0) {
				XSSFRow r = sheet.getRow(0);
				XSSFCell c = r.createCell(0);
				c.setCellValue("xpath");
				pathList.add(path);
				XSSFRow r1 = sheet.createRow(1);
				XSSFCell c1 = r1.createCell(0);
				c1.setCellValue(path);
				FileOutputStream fileOut = new FileOutputStream(excelFile);
				//write this workbook to an Outputstream.
				wb.write(fileOut);
				fileOut.close();
			}else {
				for(int i=0; i<noOfColumns; i++) {
					String attributeName = sheet.getRow(0).getCell(i).getStringCellValue();
					String attributeName1 = attributeName.replaceAll("\"", "");
					int attributeNameRow = 0;
					if(attributeName1.equalsIgnoreCase(attributeDetails[0])) {
						System.out.println("**");
						XSSFRow r;
						if(pathList.contains(path)) {
						
							XSSFRow r1 = sheet.getRow(sheet.getLastRowNum());
							XSSFCell c1 =  r1.createCell(0);
							c1.setCellValue(path);
						
							r = sheet.getRow(sheet.getLastRowNum());
							XSSFCell c = r.createCell(i);
							c.setCellValue(attributeDetails[1]);
							FileOutputStream fileOut = new FileOutputStream(excelFile);
							//write this workbook to an Outputstream.
							wb.write(fileOut);
							fileOut.close();
						}else {
							pathList.add(path);
							r = sheet.createRow(sheet.getLastRowNum()+1);
							XSSFCell c = r.createCell(i);
							c.setCellValue(attributeDetails[1]);
							FileOutputStream fileOut = new FileOutputStream(excelFile);
							
							//write this workbook to an Outputstream.
							wb.write(fileOut);
							fileOut.close();
						}
					}else {
//						sheet.getRow(0).createCell(noOfColumns+1);
						XSSFRow r = sheet.getRow(0);
						XSSFCell c = r.createCell(noOfColumns);
						c.setCellValue(attributeDetails[0]);
						XSSFRow r1 = sheet.getRow(1);
						XSSFCell c1= r1.createCell(noOfColumns);
						c1.setCellValue(attributeDetails[1]);
						FileOutputStream fileOut = new FileOutputStream(excelFile);
						
						//write this workbook to an Outputstream.
						wb.write(fileOut);
						fileOut.close();
					}
					
				}
				
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
    
}
