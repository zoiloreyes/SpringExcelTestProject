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

import com.zreyes.unapec.springexcel.model.CompraDetalle;
import com.zreyes.unapec.springexcel.model.Producto;

@Service
public class CompraDetalleService implements ICompraDetalleService {
	private static String SHEET_NAME = "Detalle Compra";
	private Sheet sheet;
	private IExcelService _exService;
	
	@Autowired
	public CompraDetalleService (IExcelService exServ) {
		this._exService = exServ;
		ensureCreated();
	}
	
	@Override
	public List<CompraDetalle> GetPaged(int pageSize, int page) {
		int lastRow = this._exService.getWb().getSheet(SHEET_NAME).getLastRowNum();
		ArrayList<CompraDetalle> comprasDetalle = new ArrayList<CompraDetalle>();
		
		for(int i = 1; i <= lastRow; i++) {
			CompraDetalle c = this.rowToModel(i, true);
			comprasDetalle.add(c);
		}
		return comprasDetalle;
	}
	
	@Override
	public CompraDetalle GetById(int id) {
		int lastRow = this._exService.getWb().getSheet(SHEET_NAME).getLastRowNum();
		for(int i = 1; i <= lastRow; i++) {
			CompraDetalle cd = this.rowToModel(i, true);
			if(cd.getId() == id) {
				return cd;
			}
		}
		return null;
	}
	
	@Override
	public CompraDetalle Add(CompraDetalle compraDetalle) {
		try {
			int newRow = this._exService.getWb().getSheet(SHEET_NAME).getLastRowNum() + 1;

			List<CompraDetalle> cps = this.GetPaged(0, 0);
			if(cps.isEmpty()) {
				compraDetalle.setId(1);
			} else {
				CompraDetalle cp = Collections.max(cps, Comparator.comparing(e -> e.getId()));
				compraDetalle.setId(cp.getId() + 1);
			}
			modelToRow(newRow, compraDetalle);
			this._exService.SaveChanges();
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return compraDetalle;
	}
	
	@Override
	public CompraDetalle Update(CompraDetalle compraDetalle) {
		int lastRow = this._exService.getWb().getSheet(SHEET_NAME).getLastRowNum();
		for(int i = 1; i <= lastRow; i++) {
			CompraDetalle cp = this.rowToModel(i, true);
			if(cp.getId() == compraDetalle.getId()) {
				this.modelToRow(i, compraDetalle);	
				break;
			}
		}
		
		_exService.SaveChanges();
		return compraDetalle;
	}
	
	@Override
	public CompraDetalle Delete(CompraDetalle compraDetalle) {
		int lastRow = this._exService.getWb().getSheet(SHEET_NAME).getLastRowNum();
		for(int i = 1; i <= lastRow; i++) {
			CompraDetalle cp = this.rowToModel(i, true);
			if(cp.getId() == compraDetalle.getId()) {
				Row rowToDelete = this.sheet.getRow(i);
				this.sheet.shiftRows(rowToDelete.getRowNum() + 1, this.sheet.getLastRowNum() + 1, -1);	
				break;
			}
		}
		
		_exService.SaveChanges();
		return compraDetalle;
	}
	
	private void ensureCreated() {
		try {
			Sheet sheet = this._exService.getWb().getSheet(SHEET_NAME);
			if(sheet == null) {
				sheet = _exService.getWb().createSheet(SHEET_NAME);
				Row titleRow = sheet.createRow(0);
				titleRow.createCell(0).setCellValue("ID");
				titleRow.createCell(1).setCellValue("COMPRA ID");
				titleRow.createCell(2).setCellValue("CANTIDAD");
				titleRow.createCell(3).setCellValue("DESCUENTO X UNIDAD");
				titleRow.createCell(4).setCellValue("COMENTARIO");
				titleRow.createCell(5).setCellValue("PRECIO UNIDAD");
				titleRow.createCell(6).setCellValue("MONTO BRUTO");
				
				this._exService.SaveChanges();
			}
			
			this.sheet = sheet;
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private CompraDetalle rowToModel(int rowNo, boolean defaultOnWrongFormat) {
		CompraDetalle dc = new CompraDetalle();
		
		Row row = this.sheet.getRow(rowNo);
		
		Cell celdaID = row.getCell(0);
		if(celdaID.getCellType() == CellType.NUMERIC) {
			dc.setId((int) celdaID.getNumericCellValue());
		}
		
		Cell celdaCompraID = row.getCell(1);
		if(celdaCompraID.getCellType() == CellType.NUMERIC) {
			dc.setCompraID((int) celdaCompraID.getNumericCellValue());
		}
		
		Cell celdaCantidad = row.getCell(2);
		if(celdaCantidad.getCellType() == CellType.NUMERIC) {
			dc.setCantidad((int) celdaCantidad.getNumericCellValue());
		}
		
		Cell celdaDescuento= row.getCell(3);
		if(celdaDescuento.getCellType() == CellType.NUMERIC) {
			dc.setDescuentoXUnidad(new BigDecimal(celdaDescuento.getNumericCellValue()));
		}
		
		Cell celdaComentario = row.getCell(4);
		if(celdaComentario.getCellType() == CellType.STRING) {
			dc.setComentario(celdaComentario.getStringCellValue());
		}
		
		Cell celdaPrecio = row.getCell(5);
		if(celdaPrecio.getCellType() == CellType.NUMERIC) {
			dc.setPrecioUnidad(new BigDecimal(celdaPrecio.getNumericCellValue()));
		}
		
		
		return dc;
	}
	
	private int modelToRow(int rowNo, CompraDetalle cd) {
		Row row = this.sheet.getRow(rowNo);
		
		if(row == null) {
			row = this.sheet.createRow(rowNo);
		}
		
		try {
			row.getCell(0).setCellValue(cd.getId());
			row.getCell(1).setCellValue(cd.getCompraID());
			row.getCell(2).setCellValue(cd.getCantidad());
			row.getCell(3).setCellValue(cd.getDescuentoXUnidad().doubleValue());
			row.getCell(4).setCellValue(cd.getComentario());
			row.getCell(5).setCellValue(cd.getPrecioUnidad().doubleValue());
		} catch(NullPointerException ex) {
			row.createCell(0,CellType.NUMERIC).setCellValue(cd.getId());
			row.createCell(1,CellType.NUMERIC).setCellValue(cd.getCompraID());
			row.createCell(2,CellType.NUMERIC).setCellValue(cd.getCantidad());
			row.createCell(3,CellType.NUMERIC).setCellValue(cd.getDescuentoXUnidad().doubleValue());
			row.createCell(4,CellType.STRING).setCellValue(cd.getComentario());
			row.createCell(5,CellType.NUMERIC).setCellValue(cd.getPrecioUnidad().doubleValue());
		}
		
		return rowNo;
	}
}
