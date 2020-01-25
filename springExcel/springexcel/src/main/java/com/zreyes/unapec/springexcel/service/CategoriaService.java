package com.zreyes.unapec.springexcel.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zreyes.unapec.springexcel.model.Categoria;

@Service
public class CategoriaService implements ICategoriaService {
	private static String SHEET_NAME = "Categoria";
	private Sheet sheet;
	private IExcelService _exService;
	
	@Autowired
	public CategoriaService (IExcelService exServ) {
		this._exService = exServ;
		ensureCreated();
	}
	
	@Override
	public List<Categoria> GetPaged(int pageSize, int page) {
		int lastRow = this._exService.getWb().getSheet(SHEET_NAME).getLastRowNum();
		ArrayList<Categoria> categorias = new ArrayList<Categoria>();
		
		for(int i = 1; i <= lastRow; i++) {
			Categoria cat = this.rowToModel(i, true);
			categorias.add(cat);
		}
		return categorias;
	}

	@Override
	public Categoria GetById(int id) {
		int lastRow = this._exService.getWb().getSheet(SHEET_NAME).getLastRowNum();
		for(int i = 1; i <= lastRow; i++) {
			Categoria cat = this.rowToModel(i, true);
			if(cat.getId() == id) {
				return cat;
			}
		}
		return null;
	}

	@Override
	public Categoria Add(Categoria categoria) {
		try {
			int newRow = this._exService.getWb().getSheet(SHEET_NAME).getLastRowNum() + 1;

			List<Categoria> categorias = this.GetPaged(0, 0);
			if(categorias.isEmpty()) {
				categoria.setId(1);
			} else {
				Categoria cat = Collections.max(categorias, Comparator.comparing(e -> e.getId()));
				categoria.setId(cat.getId() + 1);
			}
			modelToRow(newRow, categoria);
			this._exService.SaveChanges();
		}catch(Exception e) {
			System.out.println("asdasd");
		}
		return categoria;
	}

	@Override
	public Categoria Update(Categoria categoria) {
		int lastRow = this._exService.getWb().getSheet(SHEET_NAME).getLastRowNum();
		for(int i = 1; i <= lastRow; i++) {
			Categoria cat = this.rowToModel(i, true);
			if(cat.getId() == categoria.getId()) {
				this.modelToRow(i, categoria);	
				break;
			}
		}
		
		_exService.SaveChanges();
		return categoria;
	}

	@Override
	public Categoria Delete(Categoria categoria) {
		int lastRow = this._exService.getWb().getSheet(SHEET_NAME).getLastRowNum();
		for(int i = 1; i <= lastRow; i++) {
			Categoria cat = this.rowToModel(i, true);
			if(cat.getId() == categoria.getId()) {
				Row rowToDelete = this.sheet.getRow(i);
				this.sheet.shiftRows(rowToDelete.getRowNum() + 1, this.sheet.getLastRowNum() + 1, -1);	
				break;
			}
		}
		
		_exService.SaveChanges();
		return categoria;
	}

	private void ensureCreated() {
		try {
			Sheet sheet = this._exService.getWb().getSheet(SHEET_NAME);
			if(sheet == null) {
				sheet = _exService.getWb().createSheet(SHEET_NAME);
				Row titleRow = sheet.createRow(0);
				titleRow.createCell(0).setCellValue("ID");
				titleRow.createCell(1).setCellValue("NOMBRE");
				
				this._exService.SaveChanges();
			}
			
			this.sheet = sheet;
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private Categoria rowToModel(int rowNo, boolean defaultOnWrongFormat) {
		Categoria cat = new Categoria();

		Row row = this.sheet.getRow(rowNo);
		FormulaEvaluator evaluator = sheet.getWorkbook().getCreationHelper().createFormulaEvaluator();
		
		Cell celdaID = row.getCell(0);
		if(celdaID.getCellType() == CellType.NUMERIC) {
			cat.setId((int) celdaID.getNumericCellValue());
		}
		
		Cell celdaNombre = row.getCell(1);
		if(celdaNombre.getCellType() == CellType.STRING) {
			cat.setNombre(celdaNombre.getStringCellValue());
		}
		
		return cat;
	};
	
	private int modelToRow(int rowNo, Categoria cat) {
		Row row = this.sheet.getRow(rowNo);
		
		if(row == null) {
			row = this.sheet.createRow(rowNo);
		}
				
		try {
			row.getCell(0).setCellValue(cat.getId());
			row.getCell(1).setCellValue(cat.getNombre());
		} catch(NullPointerException ex) {
			row.createCell(0,CellType.NUMERIC).setCellValue(cat.getId());
			row.createCell(1).setCellValue(cat.getNombre());
		}
			
		return rowNo;
	}
}
