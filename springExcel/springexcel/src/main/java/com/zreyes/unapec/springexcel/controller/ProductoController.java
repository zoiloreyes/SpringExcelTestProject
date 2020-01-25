package com.zreyes.unapec.springexcel.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zreyes.unapec.springexcel.model.Producto;
import com.zreyes.unapec.springexcel.service.ICategoriaService;
import com.zreyes.unapec.springexcel.service.IProductoService;

@Controller
@RequestMapping("/Producto")
public class ProductoController {
	
	@Autowired
	private IProductoService _service;
	
	@Autowired
	private ICategoriaService _catServ;
	
	@GetMapping("/Index")
	public String productos(Model model) {
		List<Producto> productos= this._service.GetPaged(0, 0);
		
		productos.forEach((prod) -> {
			prod.setCategoria(_catServ.GetById(prod.getCategoriaID()));
		});
		model.addAttribute("productos", productos);
		
		return "views/Producto/Productos";
	}
	
	@GetMapping("/{id}")
	public String detalleProducto(@PathVariable("id") int id, Model model) {
		Producto c = this._service.GetById(id);
		if(c == null) {
			return "notfound";
		}

		c.setCategoria(_catServ.GetById(c.getCategoriaID()));
		
		model.addAttribute("producto", c);
		
		return "views/Producto/Detalle";
	}
	
	@GetMapping("/Eliminar/{id}")
	public String eliminarProducto(@PathVariable("id") int id) {
		Producto c = this._service.GetById(id);
		
		if(c == null) {
			return  "notfound";
		}
		
		this._service.Delete(c);

		return "redirect:/Producto/Index";
	}
	
	@GetMapping("/Editar/{id}")
	public String editarProducto(@PathVariable("id") int id, Model model) {
		Producto c = this._service.GetById(id);
		
		if(c == null) {
			return "notfound";
		}
		
		model.addAttribute("producto",c);
		model.addAttribute("categorias", _catServ.GetPaged(0,0));
		
		return "views/Producto/Editar";
	}
	
	@PostMapping("/Editar/{id}")
	public String editarProducto(@PathVariable("id") int id, 
			@Valid @ModelAttribute("producto") Producto producto,
			BindingResult result) {
		if(result.hasErrors()) {
			return "error";
		}
		
		this._service.Update(producto);
		return "redirect:/Producto/Index";
	}	
	
	@GetMapping("/Nueva")
	public String nuevaProducto(Model model) {
		model.addAttribute("categorias", _catServ.GetPaged(0,0));
		return "views/Producto/Nueva"; 
	}
	
	@PostMapping("/Nueva")
	public String nuevaProducto(@Valid @ModelAttribute("producto")Producto producto,
			BindingResult result, ModelMap model) {
		if(result.hasErrors()) {
			return "error";
		}
		
		this._service.Add(producto);
		return "redirect:/Producto/Index";
	}

}
