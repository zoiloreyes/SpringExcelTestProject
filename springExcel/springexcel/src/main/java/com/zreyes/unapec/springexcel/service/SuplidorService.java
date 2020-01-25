package com.zreyes.unapec.springexcel.service;

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

import com.zreyes.unapec.springexcel.model.Cliente;
import com.zreyes.unapec.springexcel.model.Suplidor;

@Service
public class SuplidorService implements ISuplidorService {
	private static String SHEET_NAME = "Suplidor";
	private Sheet sheet;
	private IExcelService _exService;
	
	@Autowired
	public SuplidorService (IExcelService exServ) {
		this._exService = exServ;
		ensureCreated();
	}
	
	@Override
	public Suplidor Add(Suplidor suplidor) {
		try {
			int newRow = this._exService.getWb().getSheet(SHEET_NAME).getLastRowNum() + 1;

			List<Suplidor> suplidores= this.GetPaged(0, 0);
			if(suplidores.isEmpty()) {
				suplidor.setId(1);
			} else {
				Suplidor c = Collections.max(suplidores, Comparator.comparing(e -> e.getId()));
				suplidor.setId(c.getId() + 1);
			}
			modelToRow(newRow, suplidor);
			this._exService.SaveChanges();
		}catch(Exception e) {
			System.out.println("asdasd");
		}
		return suplidor;
	}
	
	@Override
	public Suplidor Update(Suplidor suplidor) {
		int lastRow = this._exService.getWb().getSheet(SHEET_NAME).getLastRowNum();
		for(int i = 1; i <= lastRow; i++) {
			Suplidor s = this.rowToModel(i, true);
			if(s.getId() == suplidor.getId()) {
				this.modelToRow(i, suplidor);	
				break;
			}
		}
		
		_exService.SaveChanges();
		return suplidor;
	}
	
	@Override
	public Suplidor Delete(Suplidor suplidor) {
		int lastRow = this._exService.getWb().getSheet(SHEET_NAME).getLastRowNum();
		for(int i = 1; i <= lastRow; i++) {
			Suplidor s = this.rowToModel(i,true);
			if(s.getId() == suplidor.getId()) {
				Row rowToDelete = this.sheet.getRow(i);
				this.sheet.shiftRows(rowToDelete.getRowNum() + 1, this.sheet.getLastRowNum() + 1, -1);	
				break;
			}
		}
		
		_exService.SaveChanges();
		return suplidor;
	}
	
	@Override
	public Suplidor GetById(int id) {
		int lastRow = this._exService.getWb().getSheet(SHEET_NAME).getLastRowNum();
		for(int i = 1; i <= lastRow; i++) {
			Suplidor s = this.rowToModel(i, true);
			if(s.getId() == id) {
				return s;
			}
		}
		return null;
	}
	
	@Override
	public List<Suplidor> GetPaged(int page, int pageSize) {
		int lastRow = this._exService.getWb().getSheet(SHEET_NAME).getLastRowNum();
		List<Suplidor> suplidores = new ArrayList<Suplidor>();
		
		for(int i = 1; i <= lastRow; i++) {
			Suplidor c = this.rowToModel(i, true);
			suplidores.add(c);
		}
		return suplidores;
	}
	
	
	
	private void ensureCreated() {
		try {
			Sheet sheet = this._exService.getWb().getSheet(SHEET_NAME);
			if(sheet == null) {
				sheet = _exService.getWb().createSheet(SHEET_NAME);
				Row titleRow = sheet.createRow(0);
				titleRow.createCell(0).setCellValue("ID");
				titleRow.createCell(1).setCellValue("NOMBRE");
				titleRow.createCell(2).setCellValue("APELLIDO");
				titleRow.createCell(3).setCellValue("CORREO");
				titleRow.createCell(4).setCellValue("TELEFONO");
				titleRow.createCell(5).setCellValue("COMENTARIO");
				
				this._exService.SaveChanges();				
			}
			
			this.sheet = sheet;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private Suplidor rowToModel(int rowNo, boolean defaultOnWrongFormat) {
		Suplidor s = new Suplidor();
		
		Row row = this.sheet.getRow(rowNo);
		
		Cell celdaID = row.getCell(0);
		if(celdaID.getCellType() == CellType.NUMERIC) {
			s.setId((int) celdaID.getNumericCellValue());
		}
		
		Cell celdaNombre = row.getCell(1);
		if(celdaNombre.getCellType() == CellType.STRING) {
			s.setNombre(celdaNombre.getStringCellValue());
		}
		
		Cell celdaApellido = row.getCell(2);
		if(celdaApellido.getCellType() == CellType.STRING) {
			s.setApellido(celdaApellido.getStringCellValue());
		}
		
		Cell celdaCorreo = row.getCell(3);
		if(celdaCorreo.getCellType() == CellType.STRING) {
			s.setCorreo(celdaCorreo.getStringCellValue());
		}
		
		Cell celdaTelefono = row.getCell(4);
		if(celdaTelefono.getCellType() == CellType.STRING) {
			s.setTelefono(celdaTelefono.getStringCellValue());
		}
		
		Cell celdaComentario = row.getCell(5);
		if(celdaComentario.getCellType() == CellType.STRING) {
			s.setComentario(celdaComentario.getStringCellValue());
		}
		
		return s;
	}
	
	private int modelToRow(int rowNo, Suplidor s) {
		Row row = this.sheet.getRow(rowNo);
		
		if(row == null) {
			row = this.sheet.createRow(rowNo);
		}
		
		try {
			row.getCell(0).setCellValue(s.getId());
			row.getCell(1).setCellValue(s.getNombre());
			row.getCell(2).setCellValue(s.getApellido());
			row.getCell(3).setCellValue(s.getCorreo());
			row.getCell(4).setCellValue(s.getTelefono());
			row.getCell(5).setCellValue(s.getComentario());			
		}catch(NullPointerException ex) {
			row.createCell(0, CellType.NUMERIC).setCellValue(s.getId());
			row.createCell(1, CellType.STRING).setCellValue(s.getNombre());
			row.createCell(2, CellType.STRING).setCellValue(s.getApellido());
			row.createCell(3, CellType.STRING).setCellValue(s.getCorreo());
			row.createCell(4, CellType.STRING).setCellValue(s.getTelefono());
			row.createCell(5, CellType.STRING).setCellValue(s.getComentario());
		}
		
		return rowNo;
	}
	
}
