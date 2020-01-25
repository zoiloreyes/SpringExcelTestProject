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
import com.zreyes.unapec.springexcel.model.Venta;

@Service
public class VentaService implements IVentaService {
	private static String SHEET_NAME = "Venta";
	private Sheet sheet;
	private IExcelService _exService;
	
	@Autowired
	public VentaService (IExcelService exServ) {
		this._exService = exServ;
		ensureCreated();
	}
	
	@Override
	public List<Venta> GetPaged(int pageSize, int page) {
		int lastRow = this._exService.getWb().getSheet(SHEET_NAME).getLastRowNum();
		ArrayList<Venta> ventas = new ArrayList<Venta>();
		
		for(int i = 1; i <= lastRow; i++) {
			Venta v = this.rowToModel(i, true);
			ventas.add(v);
		}
		return ventas;
	}
	
	@Override
	public Venta GetById(int id) {
		int lastRow = this._exService.getWb().getSheet(SHEET_NAME).getLastRowNum();
		for(int i = 1; i <= lastRow; i++) {
			Venta v = this.rowToModel(i, true);
			if(v.getId() == id) {
				return v;
			}
		}
		return null;
	}
	
	@Override
	public Venta Add(Venta venta) {
		try {
			int newRow = this._exService.getWb().getSheet(SHEET_NAME).getLastRowNum() + 1;

			List<Venta> ventas = this.GetPaged(0, 0);
			if(ventas.isEmpty()) {
				venta.setId(1);
			} else {
				Venta vent = Collections.max(ventas, Comparator.comparing(e -> e.getId()));
				venta.setId(vent.getId() + 1);
			}
			modelToRow(newRow, venta);
			this._exService.SaveChanges();
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return venta;
	}
	
	@Override
	public Venta Update(Venta venta) {
		int lastRow = this._exService.getWb().getSheet(SHEET_NAME).getLastRowNum();
		for(int i = 1; i <= lastRow; i++) {
			Venta v = this.rowToModel(i, true);
			if(v.getId() == venta.getId()) {
				this.modelToRow(i, venta);	
				break;
			}
		}
		
		_exService.SaveChanges();
		return venta;
	}
	
	@Override
	public Venta Delete(Venta venta) {
		int lastRow = this._exService.getWb().getSheet(SHEET_NAME).getLastRowNum();
		for(int i = 1; i <= lastRow; i++) {
			Venta v = this.rowToModel(i, true);
			if(v.getId() == venta.getId()) {
				Row rowToDelete = this.sheet.getRow(i);
				this.sheet.shiftRows(rowToDelete.getRowNum() + 1, this.sheet.getLastRowNum() + 1, -1);	
				break;
			}
		}
		
		_exService.SaveChanges();
		return venta;
	}
	
	private void ensureCreated() {
		try {
			Sheet sheet = this._exService.getWb().getSheet(SHEET_NAME);
			if(sheet == null) {
				sheet = _exService.getWb().createSheet(SHEET_NAME);
				Row titleRow = sheet.createRow(0);
				titleRow.createCell(0).setCellValue("ID");
				titleRow.createCell(1).setCellValue("CLIENTE ID");
				titleRow.createCell(2).setCellValue("NOMBRE CLIENTE");
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
	
	private Venta rowToModel(int rowNo, boolean defaultOnWrongFormat) {
		Venta v = new Venta();
		
		Row row = this.sheet.getRow(rowNo);
		
		Cell cellID = row.getCell(0);
		if(cellID.getCellType() == CellType.NUMERIC) {
			v.setId((int) cellID.getNumericCellValue());
		}
		
		Cell celdaClienteID = row.getCell(1);
		if(celdaClienteID.getCellType() == CellType.NUMERIC) {
			v.setClienteID((int) celdaClienteID.getNumericCellValue());
		}
		
		Cell celdaCostoExtra = row.getCell(3);
		if(celdaCostoExtra.getCellType() == CellType.NUMERIC) {
			v.setCostoExtra(new BigDecimal(celdaCostoExtra.getNumericCellValue()));
		}
		
		Cell celdaDescuentoOrden = row.getCell(4);
		if(celdaDescuentoOrden.getCellType() == CellType.NUMERIC) {
			v.setDescuentoOrden(new BigDecimal(celdaDescuentoOrden.getNumericCellValue()));
		}
		
		Cell celdaTazaInteres = row.getCell(5);
		if(celdaTazaInteres.getCellType() == CellType.NUMERIC) {
			v.setTazaInteres(new BigDecimal(celdaTazaInteres.getNumericCellValue()));
		}
		
		Cell celdaComentario = row.getCell(6);
		if(celdaComentario.getCellType() == CellType.STRING) {
			v.setComentario(celdaComentario.getStringCellValue());
		}
		
		return v;
	}
	
	private int modelToRow(int rowNo, Venta v) {
		Row row = this.sheet.getRow(rowNo);
		
		if(row == null) {
			row = this.sheet.createRow(rowNo);
		}
		
		try {
			row.getCell(0).setCellValue(v.getId());
			row.getCell(1).setCellValue(v.getClienteID());
			row.getCell(3).setCellValue(v.getCostoExtra().doubleValue());
			row.getCell(4).setCellValue(v.getDescuentoOrden().doubleValue());
			row.getCell(5).setCellValue(v.getTazaInteres().doubleValue());
			row.getCell(6).setCellValue(v.getComentario());
		} catch(NullPointerException ex) {
			row.createCell(0, CellType.NUMERIC).setCellValue(v.getId());
			row.createCell(1, CellType.NUMERIC).setCellValue(v.getClienteID());
			row.createCell(3, CellType.NUMERIC).setCellValue(v.getCostoExtra().doubleValue());
			row.createCell(4, CellType.NUMERIC).setCellValue(v.getDescuentoOrden().doubleValue());
			row.createCell(5, CellType.NUMERIC).setCellValue(v.getTazaInteres().doubleValue());
			row.createCell(6, CellType.STRING).setCellValue(v.getComentario());
		}
		
		return rowNo;
	}
}
