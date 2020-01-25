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

import com.zreyes.unapec.springexcel.model.Categoria;
import com.zreyes.unapec.springexcel.model.Cliente;
import com.zreyes.unapec.springexcel.model.Compra;
import com.zreyes.unapec.springexcel.service.ICategoriaService;
import com.zreyes.unapec.springexcel.service.ICompraService;

@Controller
@RequestMapping("/Compra")
public class CompraController {
	
	@Autowired
	private ICompraService _service;
	
	@GetMapping("/Index")
	public String compras(Model model) {
		List<Compra> compras= this._service.GetPaged(0, 0);
		model.addAttribute("compras", compras);
		
		return "/views/Compra/Compras";
	}
	
	@GetMapping("/{id}")
	public String detalleCompra(@PathVariable("id") int id, Model model) {
		Compra c = this._service.GetById(id);
		if(c == null) {
			return "notfound";
		}
		
		model.addAttribute("compra", c);
		
		return "/views/Compra/Detalle";
	}
	
	@GetMapping("/Eliminar/{id}")
	public String eliminarCompra(@PathVariable("id") int id) {
		Compra c = this._service.GetById(id);
		
		if(c == null) {
			return  "notfound";
		}
		
		this._service.Delete(c);

		return "redirect:/Compra/Index";
	}
	
	@GetMapping("/Editar/{id}")
	public String editarCompra(@PathVariable("id") int id, Model model) {
		Compra c = this._service.GetById(id);
		
		if(c == null) {
			return "notfound";
		}
		
		model.addAttribute("compra",c);
		
		return "/views/Compra/Editar";
	}
	
	@PostMapping("/Editar/{id}")
	public String editarCompra(@PathVariable("id") int id, 
			@Valid @ModelAttribute("compra") Compra compra,
			BindingResult result) {
		if(result.hasErrors()) {
			return "error";
		}
		
		this._service.Update(compra);
		return "redirect:/Compra/Index";
	}	
	
	@GetMapping("/Nueva")
	public String nuevaCompra(Model model) {
		model.addAttribute("name");
		return "/views/Compra/Nueva"; 
	}
	
	@PostMapping("/Nueva")
	public String nuevaCompra(@Valid @ModelAttribute("compra")Compra compra,
			BindingResult result, ModelMap model) {
		if(result.hasErrors()) {
			return "error";
		}
		
		this._service.Add(compra);
		return "redirect:/Compra/Index";
	}
}
