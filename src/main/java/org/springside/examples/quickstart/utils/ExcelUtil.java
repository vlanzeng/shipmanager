package org.springside.examples.quickstart.utils;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExcelUtil<T> {
	
	
	
	@SuppressWarnings({ "unchecked", "deprecation" ,"rawtypes"})
	 public  void exportExcel(String title, String[] headers,  
	            List<T> dataset, HttpServletResponse response)  
	    {  
	        // 声明一个工作薄  
	        HSSFWorkbook workbook = new HSSFWorkbook();  
	        // 生成一个表格  
	        HSSFSheet sheet = workbook.createSheet(title);  
	  
	        // 产生表格标题行  
	        HSSFRow row = sheet.createRow(0);  
	        for (short i = 0; i < headers.length; i++)  
	        {  
	            HSSFCell cell = row.createCell(i);  
	            HSSFRichTextString text = new HSSFRichTextString(headers[i]);  
	            sheet.setColumnWidth(i, headers[i].getBytes().length * 5 * 120);
	            cell.setCellValue(text);  
	        }  
	  
	        // 遍历集合数据，产生数据行  
	        Iterator<T> it = dataset.iterator();  
	        int index = 0;  
	        try {
				while (it.hasNext()){  
					index++;  
					row = sheet.createRow(index);  
					T t = (T) it.next();  
					// 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值  
					Field[] fields = t.getClass().getDeclaredFields();  
					for (short i = 0; i < fields.length; i++)  
					{  
						HSSFCell cell = row.createCell(i);  
						Field field = fields[i];  
						String fieldName = field.getName();  
						String getMethodName = "get"  
								+ fieldName.substring(0, 1).toUpperCase()  
								+ fieldName.substring(1);  
						Class tCls = t.getClass();  
						Method getMethod = tCls.getMethod(getMethodName,  
								new Class[]  
										{});  
						Object value = getMethod.invoke(t, new Object[]{});  
						cell.setCellValue(new HSSFRichTextString( null==value ? "" : value.toString()  ));
					}  
				}
			    response.setContentType("application/vnd.ms-excel");
				   String    downloadFileName = new String(URLDecoder.decode(title,"UTF-8").getBytes(),"ISO8859-1");
				        response.setHeader("Content-disposition", "attachment;filename="
				                + downloadFileName + ".xls");
				        OutputStream ouputStream = response.getOutputStream();
				        workbook.write(ouputStream);
				        ouputStream.flush();
				        ouputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}  
	    } 
}

