package com.zreyes.unapec.springexcel.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zreyes.unapec.springexcel.model.Categoria;
import com.zreyes.unapec.springexcel.model.Cliente;

@Service
public class ClienteService implements IClienteService {
	private static String SHEET_NAME = "Cliente";
	private Sheet sheet;
	private IExcelService _exService;
	
	@Autowired
	public ClienteService (IExcelService exServ) {
		this._exService = exServ;
		ensureCreated();
	}
	
	
	@Override
	public Cliente Add(Cliente cliente) {
		try {
			int newRow = this._exService.getWb().getSheet(SHEET_NAME).getLastRowNum() + 1;

			List<Cliente> clientes = this.GetPaged(0, 0);
			if(clientes.isEmpty()) {
				cliente.setId(1);
			} else {
				Cliente c = Collections.max(clientes, Comparator.comparing(e -> e.getId()));
				cliente.setId(c.getId() + 1);
			}
			modelToRow(newRow, cliente);
			this._exService.SaveChanges();
		}catch(Exception e) {
			System.out.println("asdasd");
		}
		return cliente;
	}
	
	@Override
	public Cliente Update(Cliente cliente) {
		int lastRow = this._exService.getWb().getSheet(SHEET_NAME).getLastRowNum();
		for(int i = 1; i <= lastRow; i++) {
			Cliente c = this.rowToModel(i, true);
			if(c.getId() == cliente.getId()) {
				this.modelToRow(i, cliente);	
				break;
			}
		}
		
		_exService.SaveChanges();
		return cliente;
	}
	
	@Override
	public Cliente Delete(Cliente cliente) {
		int lastRow = this._exService.getWb().getSheet(SHEET_NAME).getLastRowNum();
		for(int i = 1; i <= lastRow; i++) {
			Cliente c = this.rowToModel(i,true);
			if(c.getId() == cliente.getId()) {
				Row rowToDelete = this.sheet.getRow(i);
				this.sheet.shiftRows(rowToDelete.getRowNum() + 1, this.sheet.getLastRowNum() + 1, -1);	
				break;
			}
		}
		
		_exService.SaveChanges();
		return cliente;
	}
	
	@Override
	public Cliente GetById(int id) {
		int lastRow = this._exService.getWb().getSheet(SHEET_NAME).getLastRowNum();
		for(int i = 1; i <= lastRow; i++) {
			Cliente c = this.rowToModel(i, true);
			if(c.getId() == id) {
				return c;
			}
		}
		return null;
	}
	
	@Override
	public List<Cliente> GetPaged(int page, int pageSize) {
		int lastRow = this._exService.getWb().getSheet(SHEET_NAME).getLastRowNum();
		List<Cliente> clientes = new ArrayList<Cliente>();
		
		for(int i = 1; i <= lastRow; i++) {
			Cliente c = this.rowToModel(i, true);
			clientes.add(c);
		}
		return clientes;
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
	
	private Cliente rowToModel(int rowNo, boolean defaultOnWrongFormat) {
		Cliente c = new Cliente();
		
		Row row = this.sheet.getRow(rowNo);
		
		Cell celdaID = row.getCell(0);
		if(celdaID.getCellType() == CellType.NUMERIC) {
			c.setId((int) celdaID.getNumericCellValue());
		}
		
		Cell celdaNombre = row.getCell(1);
		if(celdaNombre.getCellType() == CellType.STRING) {
			c.setNombre(celdaNombre.getStringCellValue());
		}
		
		Cell celdaApellido = row.getCell(2);
		if(celdaApellido.getCellType() == CellType.STRING) {
			c.setApellido(celdaApellido.getStringCellValue());
		}
		
		Cell celdaCorreo = row.getCell(3);
		if(celdaCorreo.getCellType() == CellType.STRING) {
			c.setCorreo(celdaCorreo.getStringCellValue());
		}
		
		Cell celdaTelefono = row.getCell(4);
		if(celdaTelefono.getCellType() == CellType.STRING) {
			c.setTelefono(celdaTelefono.getStringCellValue());
		}
		
		Cell celdaComentario = row.getCell(5);
		if(celdaComentario.getCellType() == CellType.STRING) {
			c.setComentario(celdaComentario.getStringCellValue());
		}
		
		return c;
	}
	
	private int modelToRow(int rowNo, Cliente c) {
		Row row = this.sheet.getRow(rowNo);
		
		if(row == null) {
			row = this.sheet.createRow(rowNo);
		}
		
		try {
			row.getCell(0).setCellValue(c.getId());
			row.getCell(1).setCellValue(c.getNombre());
			row.getCell(2).setCellValue(c.getApellido());
			row.getCell(3).setCellValue(c.getCorreo());
			row.getCell(4).setCellValue(c.getTelefono());
			row.getCell(5).setCellValue(c.getComentario());			
		}catch(NullPointerException ex) {
			row.createCell(0, CellType.NUMERIC).setCellValue(c.getId());
			row.createCell(1, CellType.STRING).setCellValue(c.getNombre());
			row.createCell(2, CellType.STRING).setCellValue(c.getApellido());
			row.createCell(3, CellType.STRING).setCellValue(c.getCorreo());
			row.createCell(4, CellType.STRING).setCellValue(c.getTelefono());
			row.createCell(5, CellType.STRING).setCellValue(c.getComentario());
		}
		
		return rowNo;
	}
}
