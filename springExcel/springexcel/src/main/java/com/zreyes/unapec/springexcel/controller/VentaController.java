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

import com.zreyes.unapec.springexcel.model.Venta;
import com.zreyes.unapec.springexcel.service.IVentaService;

@Controller
@RequestMapping("/Venta")
public class VentaController {

	@Autowired
	private IVentaService _service;
	
	@GetMapping("/Index")
	public String ventas(Model model) {
		List<Venta> ventas= this._service.GetPaged(0, 0);
		model.addAttribute("ventas", ventas);
		
		return "/views/Venta/Ventas";
	}
	
	@GetMapping("/{id}")
	public String detalleVenta(@PathVariable("id") int id, Model model) {
		Venta c = this._service.GetById(id);
		if(c == null) {
			return "notfound";
		}
		
		model.addAttribute("venta", c);
		
		return "/views/Venta/Detalle";
	}
	
	@GetMapping("/Eliminar/{id}")
	public String eliminarVenta(@PathVariable("id") int id) {
		Venta c = this._service.GetById(id);
		
		if(c == null) {
			return  "notfound";
		}
		
		this._service.Delete(c);

		return "redirect:/Venta/Index";
	}
	
	@GetMapping("/Editar/{id}")
	public String editarVenta(@PathVariable("id") int id, Model model) {
		Venta c = this._service.GetById(id);
		
		if(c == null) {
			return "notfound";
		}
		
		model.addAttribute("venta",c);
		
		return "/views/Venta/Editar";
	}
	
	@PostMapping("/Editar/{id}")
	public String editarVenta(@PathVariable("id") int id, 
			@Valid @ModelAttribute("venta") Venta venta,
			BindingResult result) {
		if(result.hasErrors()) {
			return "error";
		}
		
		this._service.Update(venta);
		return "redirect:/Venta/Index";
	}	
	
	@GetMapping("/Nueva")
	public String nuevaVenta(Model model) {
		model.addAttribute("name");
		return "/views/Venta/Nueva"; 
	}
	
	@PostMapping("/Nueva")
	public String nuevaVenta(@Valid @ModelAttribute("venta")Venta venta,
			BindingResult result, ModelMap model) {
		if(result.hasErrors()) {
			return "error";
		}
		
		this._service.Add(venta);
		return "redirect:/Venta/Index";
	}
}
