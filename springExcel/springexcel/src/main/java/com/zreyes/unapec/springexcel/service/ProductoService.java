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

import com.zreyes.unapec.springexcel.model.Categoria;
import com.zreyes.unapec.springexcel.model.Producto;

@Service
public class ProductoService implements IProductoService {
	private static String SHEET_NAME = "Producto";
	private Sheet sheet;
	private IExcelService _exService;
	
	@Autowired
	public ProductoService (IExcelService exServ) {
		this._exService = exServ;
		ensureCreated();
	}
	
	@Override
	public List<Producto> GetPaged(int pageSize, int page) {
		int lastRow = this._exService.getWb().getSheet(SHEET_NAME).getLastRowNum();
		ArrayList<Producto> productos = new ArrayList<Producto>();
		
		for(int i = 1; i <= lastRow; i++) {
			Producto p = this.rowToModel(i, true);
			productos.add(p);
		}
		return productos;
	}
	
	
	@Override
	public Producto GetById(int id) {
		int lastRow = this._exService.getWb().getSheet(SHEET_NAME).getLastRowNum();
		for(int i = 1; i <= lastRow; i++) {
			Producto p = this.rowToModel(i, true);
			if(p.getId() == id) {
				return p;
			}
		}
		return null;
	}
	
	@Override
	public Producto Add(Producto producto) {
		try {
			int newRow = this._exService.getWb().getSheet(SHEET_NAME).getLastRowNum() + 1;

			List<Producto> productos = this.GetPaged(0, 0);
			if(productos.isEmpty()) {
				producto.setId(1);
			} else {
				Producto cat = Collections.max(productos, Comparator.comparing(e -> e.getId()));
				producto.setId(cat.getId() + 1);
			}
			modelToRow(newRow, producto);
			this._exService.SaveChanges();
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return producto;
	}
	
	@Override
	public Producto Update(Producto producto) {
		int lastRow = this._exService.getWb().getSheet(SHEET_NAME).getLastRowNum();
		for(int i = 1; i <= lastRow; i++) {
			Producto p = this.rowToModel(i, true);
			if(p.getId() == producto.getId()) {
				this.modelToRow(i, producto);	
				break;
			}
		}
		
		_exService.SaveChanges();
		return producto;
	}
	
	@Override
	public Producto Delete(Producto producto) {
		int lastRow = this._exService.getWb().getSheet(SHEET_NAME).getLastRowNum();
		for(int i = 1; i <= lastRow; i++) {
			Producto p = this.rowToModel(i, true);
			if(p.getId() == producto.getId()) {
				Row rowToDelete = this.sheet.getRow(i);
				this.sheet.shiftRows(rowToDelete.getRowNum() + 1, this.sheet.getLastRowNum() + 1, -1);	
				break;
			}
		}
		
		_exService.SaveChanges();
		return producto;
	}
	
	private void ensureCreated() {
		try {
			Sheet sheet = this._exService.getWb().getSheet(SHEET_NAME);
			if(sheet == null) {
				sheet = _exService.getWb().createSheet(SHEET_NAME);
				Row titleRow = sheet.createRow(0);
				titleRow.createCell(0).setCellValue("ID");
				titleRow.createCell(1).setCellValue("NOMBRE");
				titleRow.createCell(2).setCellValue("DESCRIPCION");
				titleRow.createCell(3).setCellValue("INVENTARIO INICIO");
				titleRow.createCell(4).setCellValue("PUNTO DE PEDIDO");
				titleRow.createCell(5).setCellValue("UNIDAD");
				titleRow.createCell(6).setCellValue("CATEGORIAID");
				titleRow.createCell(7).setCellValue("COMENTARIO");
				titleRow.createCell(8).setCellValue("INVENTARIO EN MANO");
				titleRow.createCell(9).setCellValue("ORDEN PENDIENTE");
				titleRow.createCell(10).setCellValue("VALOR INVENTARIO");
				
				this._exService.SaveChanges();
			}
			
			this.sheet = sheet;
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private Producto rowToModel(int rowNo, boolean defaultOnWrongFormat) {
		Producto p = new Producto();
		
		Row row = this.sheet.getRow(rowNo);
		
		Cell celdaID = row.getCell(0);
		if(celdaID.getCellType() == CellType.NUMERIC) {
			p.setId((int) celdaID.getNumericCellValue());
		}
		
		Cell celdaNombre = row.getCell(1);
		if(celdaNombre.getCellType() == CellType.STRING) {
			p.setNombre(celdaNombre.getStringCellValue());
		}
		
		Cell celdaDescripcion = row.getCell(2);
		if(celdaDescripcion.getCellType() == CellType.STRING) {
			p.setDescripcion(celdaDescripcion.getStringCellValue());
		}
		
		Cell celdaInventarioInicio = row.getCell(3);
		if(celdaInventarioInicio.getCellType() == CellType.NUMERIC) {
			p.setInicioInventario((int) celdaInventarioInicio.getNumericCellValue());
		}
		
		Cell celdaPuntoPedido = row.getCell(4);
		if(celdaPuntoPedido.getCellType() == CellType.NUMERIC) {
			p.setPuntoDePedido((int) celdaPuntoPedido.getNumericCellValue());
		}
		
		Cell celdaUnidad = row.getCell(5);
		if(celdaUnidad.getCellType() == CellType.STRING) {
			p.setUnidadMedida(celdaUnidad.getStringCellValue());
		}
		
		Cell celdaCategoriaID = row.getCell(6);
		if(celdaCategoriaID.getCellType() == CellType.NUMERIC) {
			p.setCategoriaID((int) celdaCategoriaID.getNumericCellValue());
		}
		
		Cell celdaComentario = row.getCell(7);
		if(celdaComentario.getCellType() == CellType.STRING) {
			p.setComentario(celdaComentario.getStringCellValue());
		}
		
		Cell celdaCantidadInventario = row.getCell(8);
		if(celdaCantidadInventario.getCellType() == CellType.NUMERIC) {
			p.setCantidadInventario((int) celdaCantidadInventario.getNumericCellValue());
		}
		
		return p;
	}
	
	private int modelToRow(int rowNo, Producto p) {
		Row row = this.sheet.getRow(rowNo);
		
		if(row == null) {
			row = this.sheet.createRow(rowNo);
		}
		
		try {
			row.getCell(0).setCellValue(p.getId());
			row.getCell(1).setCellValue(p.getNombre());
			row.getCell(2).setCellValue(p.getDescripcion());
			row.getCell(3).setCellValue(p.getInicioInventario());
			row.getCell(4).setCellValue(p.getPuntoDePedido());
			row.getCell(5).setCellValue(p.getUnidadMedida());
			row.getCell(6).setCellValue(p.getCategoriaID());
			row.getCell(7).setCellValue(p.getComentario());
			row.getCell(8).setCellValue(p.getCantidadInventario());
		} catch(NullPointerException ex) {
			row.createCell(0,CellType.NUMERIC).setCellValue(p.getId());
			row.createCell(1,CellType.STRING).setCellValue(p.getNombre());
			row.createCell(2,CellType.STRING).setCellValue(p.getDescripcion());
			row.createCell(3,CellType.NUMERIC).setCellValue(p.getInicioInventario());
			row.createCell(4,CellType.NUMERIC).setCellValue(p.getPuntoDePedido());
			row.createCell(5,CellType.STRING).setCellValue(p.getUnidadMedida());
			row.createCell(6,CellType.NUMERIC).setCellValue(p.getCategoriaID());
			row.createCell(7,CellType.STRING).setCellValue(p.getComentario());
			row.createCell(8,CellType.NUMERIC).setCellValue(p.getCantidadInventario());
		}
		
		return rowNo;
	}
}
