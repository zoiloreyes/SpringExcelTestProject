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
import com.zreyes.unapec.springexcel.model.VentaDetalle;

@Service
public class VentaDetalleService implements IVentaDetalleService {
	private static String SHEET_NAME = "Detalle Compra";
	private Sheet sheet;
	private IExcelService _exService;
	
	@Autowired
	public VentaDetalleService (IExcelService exServ) {
		this._exService = exServ;
		ensureCreated();
	}
	
	@Override
	public List<VentaDetalle> GetPaged(int pageSize, int page) {
		int lastRow = this._exService.getWb().getSheet(SHEET_NAME).getLastRowNum();
		ArrayList<VentaDetalle> ventasDetalle = new ArrayList<VentaDetalle>();
		
		for(int i = 1; i <= lastRow; i++) {
			VentaDetalle v = this.rowToModel(i, true);
			ventasDetalle.add(v);
		}
		return ventasDetalle;
	}
	
	@Override
	public VentaDetalle GetById(int id) {
		int lastRow = this._exService.getWb().getSheet(SHEET_NAME).getLastRowNum();
		for(int i = 1; i <= lastRow; i++) {
			VentaDetalle cd = this.rowToModel(i, true);
			if(cd.getId() == id) {
				return cd;
			}
		}
		return null;
	}
	
	@Override
	public VentaDetalle Add(VentaDetalle ventaDetalle) {
		try {
			int newRow = this._exService.getWb().getSheet(SHEET_NAME).getLastRowNum() + 1;

			List<VentaDetalle> vps = this.GetPaged(0, 0);
			if(vps.isEmpty()) {
				ventaDetalle.setId(1);
			} else {
				VentaDetalle cp = Collections.max(vps, Comparator.comparing(e -> e.getId()));
				ventaDetalle.setId(cp.getId() + 1);
			}
			modelToRow(newRow, ventaDetalle);
			this._exService.SaveChanges();
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return ventaDetalle;
	}
	
	@Override
	public VentaDetalle Update(VentaDetalle ventaDetalle) {
		int lastRow = this._exService.getWb().getSheet(SHEET_NAME).getLastRowNum();
		for(int i = 1; i <= lastRow; i++) {
			VentaDetalle vd = this.rowToModel(i, true);
			if(vd.getId() == ventaDetalle.getId()) {
				this.modelToRow(i, ventaDetalle);	
				break;
			}
		}
		
		_exService.SaveChanges();
		return ventaDetalle;
	}
	
	@Override
	public VentaDetalle Delete(VentaDetalle ventaDetalle) {
		int lastRow = this._exService.getWb().getSheet(SHEET_NAME).getLastRowNum();
		for(int i = 1; i <= lastRow; i++) {
			VentaDetalle cp = this.rowToModel(i, true);
			if(cp.getId() == ventaDetalle.getId()) {
				Row rowToDelete = this.sheet.getRow(i);
				this.sheet.shiftRows(rowToDelete.getRowNum() + 1, this.sheet.getLastRowNum() + 1, -1);	
				break;
			}
		}
		
		_exService.SaveChanges();
		return ventaDetalle;
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
	
	private VentaDetalle rowToModel(int rowNo, boolean defaultOnWrongFormat) {
		VentaDetalle dc = new VentaDetalle();
		
		Row row = this.sheet.getRow(rowNo);
		
		Cell celdaID = row.getCell(0);
		if(celdaID.getCellType() == CellType.NUMERIC) {
			dc.setId((int) celdaID.getNumericCellValue());
		}
		
		Cell celdaVentaID = row.getCell(1);
		if(celdaVentaID.getCellType() == CellType.NUMERIC) {
			dc.setVentaID((int) celdaVentaID.getNumericCellValue());
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
	
	private int modelToRow(int rowNo, VentaDetalle vd) {
		Row row = this.sheet.getRow(rowNo);
		
		if(row == null) {
			row = this.sheet.createRow(rowNo);
		}
		
		try {
			row.getCell(0).setCellValue(vd.getId());
			row.getCell(1).setCellValue(vd.getVentaID());
			row.getCell(2).setCellValue(vd.getCantidad());
			row.getCell(3).setCellValue(vd.getDescuentoXUnidad().doubleValue());
			row.getCell(4).setCellValue(vd.getComentario());
			row.getCell(5).setCellValue(vd.getPrecioUnidad().doubleValue());
		} catch(NullPointerException ex) {
			row.createCell(0,CellType.NUMERIC).setCellValue(vd.getId());
			row.createCell(1,CellType.NUMERIC).setCellValue(vd.getVentaID());
			row.createCell(2,CellType.NUMERIC).setCellValue(vd.getCantidad());
			row.createCell(3,CellType.NUMERIC).setCellValue(vd.getDescuentoXUnidad().doubleValue());
			row.createCell(4,CellType.STRING).setCellValue(vd.getComentario());
			row.createCell(5,CellType.NUMERIC).setCellValue(vd.getPrecioUnidad().doubleValue());
		}
		
		return rowNo;
	}
	
}
