package com.zreyes.unapec.springexcel.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zreyes.unapec.springexcel.model.Precio;
import com.zreyes.unapec.springexcel.model.Producto;

@Service
public class PrecioService implements IPrecioService {
	private static String SHEET_NAME = "Precio";
	private Sheet sheet;
	private IExcelService _exService;
	
	@Autowired
	public PrecioService (IExcelService exServ) {
		this._exService = exServ;
		ensureCreated();
	}
	
	@Override
	public List<Precio> GetPaged(int pageSize, int page) {
		int lastRow = this._exService.getWb().getSheet(SHEET_NAME).getLastRowNum();
		ArrayList<Precio> precios = new ArrayList<Precio>();
		
		for(int i = 1; i <= lastRow; i++) {
			Precio p = this.rowToModel(i, true);
			precios.add(p);
		}
		return precios;
	}
	
	@Override
	public Precio GetById(int id) {
		int lastRow = this._exService.getWb().getSheet(SHEET_NAME).getLastRowNum();
		for(int i = 1; i <= lastRow; i++) {
			Precio p = this.rowToModel(i, true);
			if(p.getId() == id) {
				return p;
			}
		}
		return null;
	}
	
	@Override
	public Precio Add(Precio precio) {
		try {
			int newRow = this._exService.getWb().getSheet(SHEET_NAME).getLastRowNum() + 1;

			List<Precio> precios = this.GetPaged(0, 0);
			if(precios.isEmpty()) {
				precio.setId(1);
			} else {
				Precio p = Collections.max(precios, Comparator.comparing(e -> e.getId()));
				precio.setId(p.getId() + 1);
			}
			modelToRow(newRow, precio);
			this._exService.SaveChanges();
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return precio;
	}
	
	@Override
	public Precio Update(Precio precio) {
		int lastRow = this._exService.getWb().getSheet(SHEET_NAME).getLastRowNum();
		for(int i = 1; i <= lastRow; i++) {
			Precio p = this.rowToModel(i, true);
			if(p.getId() == precio.getId()) {
				this.modelToRow(i, precio);	
				break;
			}
		}
		
		_exService.SaveChanges();
		return precio;
	}
	
	@Override
	public Precio Delete(Precio precio) {
		int lastRow = this._exService.getWb().getSheet(SHEET_NAME).getLastRowNum();
		for(int i = 1; i <= lastRow; i++) {
			Precio p = this.rowToModel(i, true);
			if(p.getId() == precio.getId()) {
				Row rowToDelete = this.sheet.getRow(i);
				this.sheet.shiftRows(rowToDelete.getRowNum() + 1, this.sheet.getLastRowNum() + 1, -1);	
				break;
			}
		}
		
		_exService.SaveChanges();
		return precio;
	}
	
	private void ensureCreated() {
		try {
			Sheet sheet = this._exService.getWb().getSheet(SHEET_NAME);
			if(sheet == null) {
				sheet = _exService.getWb().createSheet(SHEET_NAME);
				Row titleRow = sheet.createRow(0);
				titleRow.createCell(0).setCellValue("ID");
				titleRow.createCell(1).setCellValue("PRODUCTO ID");
				titleRow.createCell(2).setCellValue("DESDE FECHA");
				titleRow.createCell(3).setCellValue("PRECIO COMPRA");
				titleRow.createCell(4).setCellValue("PRECIO VENTA");
				
				this._exService.SaveChanges();
			}
			
			this.sheet = sheet;
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private Precio rowToModel(int rowNo, boolean defaultOnWrongFormat) {
		Precio p = new Precio();
		
		Row row = this.sheet.getRow(rowNo);
		
		Cell celdaID = row.getCell(0);
		if(celdaID.getCellType() == CellType.NUMERIC) {
			p.setId((int) celdaID.getNumericCellValue());
		}
		
		Cell celdaProductoID = row.getCell(1);
		if(celdaProductoID.getCellType() == CellType.NUMERIC) {
			p.setProductoID((int) celdaProductoID.getNumericCellValue());
		}
		
		Cell celdaDesdeFecha = row.getCell(2);
		if(DateUtil.isCellDateFormatted(celdaDesdeFecha)) {
			p.setDesdeFecha(celdaDesdeFecha.getDateCellValue());
		}
		
		Cell celdaPrecioCompra = row.getCell(3);
		if(celdaPrecioCompra.getCellType() == CellType.NUMERIC) {
			p.setPrecioCompra(new BigDecimal(celdaPrecioCompra.getNumericCellValue()));
		}
		
		Cell celdaPrecioVenta = row.getCell(4);
		if(celdaPrecioVenta.getCellType() == CellType.NUMERIC) {
			p.setPrecioVenta(new BigDecimal(celdaPrecioVenta.getNumericCellValue()));
		}
		
		return p;
	}
	
	private int modelToRow(int rowNo, Precio p) {
		Row row = this.sheet.getRow(rowNo);
		
		if(row == null) {
			row = this.sheet.createRow(rowNo);
		}
		
		try {
			row.getCell(0).setCellValue(p.getId());
			row.getCell(1).setCellValue(p.getProductoID());
			row.getCell(2).setCellValue(p.getDesdeFecha());
			row.getCell(3).setCellValue(p.getPrecioCompra().doubleValue());
			row.getCell(4).setCellValue(p.getPrecioVenta().doubleValue());
		}catch(NullPointerException ex) {
			row.createCell(0, CellType.NUMERIC).setCellValue(p.getId());
			row.createCell(1, CellType.NUMERIC).setCellValue(p.getProductoID());
			row.createCell(2).setCellValue(p.getDesdeFecha());
			row.createCell(3,CellType.NUMERIC).setCellValue(p.getPrecioCompra().doubleValue());
			row.createCell(4,CellType.NUMERIC).setCellValue(p.getPrecioVenta().doubleValue());
		}
		
		return rowNo;
	}
}
