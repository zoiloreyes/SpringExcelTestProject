package com.zreyes.unapec.springexcel.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
public class ExcelService implements IExcelService{
	private static String fileName = "db.xlsx";
	private Workbook wb;
	
	public ExcelService() {
		ensureCreated();
	}
	
	private void ensureCreated() {
		try {
			this.wb = WorkbookFactory.create(new FileInputStream(fileName));
		} catch(FileNotFoundException fe) {
			this.wb = new XSSFWorkbook();
			Sheet sheet = this.wb.createSheet("Sheet1");
			Row row = sheet.createRow(0);
			Cell cell = row.createCell(0);
			cell.setCellValue("");
			
			this.SaveChanges();
			this.ensureCreated();
		} catch (EncryptedDocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void Reload() throws EncryptedDocumentException, FileNotFoundException, IOException {
		this.wb = WorkbookFactory.create(new FileInputStream(fileName));
	}
	

	public void SaveChanges() {
		try
        {
            //Write the workbook in file system
            FileOutputStream out = new FileOutputStream(fileName);
            this.getWb().write(out);
            out.close();
            System.out.println("excel saved");
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
	}
	
	protected void finalize() {
		if(this.getWb() != null) {
			try {
				this.getWb().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public byte[] getFileBytes() {
		FileInputStream in = null;
		try {
			in = new FileInputStream(fileName);
			return IOUtils.toByteArray(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}finally {
			if(in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public Workbook getWb() {
		return wb;
	}
}
