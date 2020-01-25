package com.zreyes.unapec.springexcel.service;

import org.apache.poi.ss.usermodel.Workbook;

public interface IExcelService {
	public Workbook getWb();
	byte[] getFileBytes();
	void SaveChanges();
}
