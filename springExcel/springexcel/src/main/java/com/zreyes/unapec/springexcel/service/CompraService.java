package com.zreyes.unapec.springexcel.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zreyes.unapec.springexcel.model.Compra;
import com.zreyes.unapec.springexcel.model.Producto;
@Service
public class CompraService implements ICompraService {
	private static String SHEET_NAME = "Compra";
	private Sheet sheet;
	private IExcelService _exService;
	
	@Autowired
	public CompraService (IExcelService exServ) {
		this._exService = exServ;
		ensureCreated();
	}
	
	@Override
	public List<Compra> GetPaged(int pageSize, int page) {
		int lastRow = this._exService.getWb().getSheet(SHEET_NAME).getLastRowNum();
		ArrayList<Compra> compras= new ArrayList<Compra>();
		
		for(int i = 1; i <= lastRow; i++) {
			Compra c = this.rowToModel(i, true);
			compras.add(c);
		}
		return compras;
	}
	
	@Override
	public Compra GetById(int id) {
		int lastRow = this._exService.getWb().getSheet(SHEET_NAME).getLastRowNum();
		for(int i = 1; i <= lastRow; i++) {
			Compra c = this.rowToModel(i, true);
			if(c.getId() == id) {
				return c;
			}
		}
		return null;
	}
	
	@Override
	public Compra Add(Compra compra) {
		try {
			int newRow = this._exService.getWb().getSheet(SHEET_NAME).getLastRowNum() + 1;

			List<Compra> compras = this.GetPaged(0, 0);
			if(compras.isEmpty()) {
				compra.setId(1);
			} else {
				Compra comp = Collections.max(compras, Comparator.comparing(e -> e.getId()));
				compra.setId(comp.getId() + 1);
			}
			modelToRow(newRow, compra);
			this._exService.SaveChanges();
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return compra;
	}
	
	@Override
	public Compra Update(Compra compra) {
		int lastRow = this._exService.getWb().getSheet(SHEET_NAME).getLastRowNum();
		for(int i = 1; i <= lastRow; i++) {
			Compra c = this.rowToModel(i, true);
			if(c.getId() == compra.getId()) {
				this.modelToRow(i, compra);	
				break;
			}
		}
		
		_exService.SaveChanges();
		return compra;
	}
	
	@Override
	public Compra Delete(Compra compra) {
		int lastRow = this._exService.getWb().getSheet(SHEET_NAME).getLastRowNum();
		for(int i = 1; i <= lastRow; i++) {
			Compra c = this.rowToModel(i, true);
			if(c.getId() == compra.getId()) {
				Row rowToDelete = this.sheet.getRow(i);
				this.sheet.shiftRows(rowToDelete.getRowNum() + 1, this.sheet.getLastRowNum() + 1, -1);	
				break;
			}
		}
		
		_exService.SaveChanges();
		return compra;
	}
	
	private void ensureCreated() {
		try {
			Sheet sheet = this._exService.getWb().getSheet(SHEET_NAME);
			if(sheet == null) {
				sheet = _exService.getWb().createSheet(SHEET_NAME);
				Row titleRow = sheet.createRow(0);
				titleRow.createCell(0).setCellValue("ID");
				titleRow.createCell(1).setCellValue("SUPLIDOR ID");
				titleRow.createCell(2).setCellValue("NOMBRE SUPLIDOR");
				titleRow.createCell(3).setCellValue("COSTO EXTRA");
				titleRow.createCell(4).setCellValue("DESCUENTO ORDEN");
				titleRow.createCell(5).setCellValue("TAZA INTERES");
				titleRow.createCell(6).setCellValue("COMENTARIO");
				titleRow.createCell(7).setCellValue("TOTAL DE ORDEN");
				this._exService.SaveChanges();
			}
			
			this.sheet = sheet;
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private Compra rowToModel(int rowNo, boolean defaultOnWrongFormat) {
		Compra c = new Compra();
		
		Row row = this.sheet.getRow(rowNo);
		
		Cell cellID = row.getCell(0);
		if(cellID.getCellType() == CellType.NUMERIC) {
			c.setId((int) cellID.getNumericCellValue());
		}
		
		Cell celdaSuplidorID = row.getCell(1);
		if(celdaSuplidorID.getCellType() == CellType.NUMERIC) {
			c.setSuplidorID((int) celdaSuplidorID.getNumericCellValue());
		}
		
		Cell celdaCostoExtra = row.getCell(3);
		if(celdaCostoExtra.getCellType() == CellType.NUMERIC) {
			c.setCostoExtra(new BigDecimal(celdaCostoExtra.getNumericCellValue()));
		}
		
		Cell celdaDescuentoOrden = row.getCell(4);
		if(celdaDescuentoOrden.getCellType() == CellType.NUMERIC) {
			c.setDescuentoOrden(new BigDecimal(celdaDescuentoOrden.getNumericCellValue()));
		}
		
		Cell celdaTazaInteres = row.getCell(5);
		if(celdaTazaInteres.getCellType() == CellType.NUMERIC) {
			c.setTazaInteres(new BigDecimal(celdaTazaInteres.getNumericCellValue()));
		}
		
		Cell celdaComentario = row.getCell(6);
		if(celdaComentario.getCellType() == CellType.STRING) {
			c.setComentario(celdaComentario.getStringCellValue());
		}
		
		return c;
	}
	
	private int modelToRow(int rowNo, Compra c) {
		Row row = this.sheet.getRow(rowNo);
		
		if(row == null) {
			row = this.sheet.createRow(rowNo);
		}
		
		try {
			row.getCell(0).setCellValue(c.getId());
			row.getCell(1).setCellValue(c.getSuplidorID());
			row.getCell(3).setCellValue(c.getCostoExtra().doubleValue());
			row.getCell(4).setCellValue(c.getDescuentoOrden().doubleValue());
			row.getCell(5).setCellValue(c.getTazaInteres().doubleValue());
			row.getCell(6).setCellValue(c.getComentario());
		} catch(NullPointerException ex) {
			row.createCell(0, CellType.NUMERIC).setCellValue(c.getId());
			row.createCell(1, CellType.NUMERIC).setCellValue(c.getSuplidorID());
			row.createCell(3, CellType.NUMERIC).setCellValue(c.getCostoExtra().doubleValue());
			row.createCell(4, CellType.NUMERIC).setCellValue(c.getDescuentoOrden().doubleValue());
			row.createCell(5, CellType.NUMERIC).setCellValue(c.getTazaInteres().doubleValue());
			row.createCell(6, CellType.STRING).setCellValue(c.getComentario());
		}
		
		return rowNo;
	}
}
