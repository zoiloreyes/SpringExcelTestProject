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

import com.zreyes.unapec.springexcel.model.Precio;
import com.zreyes.unapec.springexcel.model.Precio;
import com.zreyes.unapec.springexcel.service.IPrecioService;
import com.zreyes.unapec.springexcel.service.IPrecioService;

@Controller
@RequestMapping("/Precio")
public class PrecioController {
	
	@Autowired
	private IPrecioService _service;
	
	@GetMapping("/Index")
	public String precios(Model model) {
		List<Precio> precios = this._service.GetPaged(0, 0);
		model.addAttribute("precios", precios);
		
		return "/views/Precio/Precios";
	}
	
	@GetMapping("/{id}")
	public String detallePrecio(@PathVariable("id") int id, Model model) {
		Precio p = this._service.GetById(id);
		if(p == null) {
			return "notfound";
		}
		
		model.addAttribute("precio", p);
		
		return "/views/Precio/Detalle";
	}
	
	@GetMapping("/Eliminar/{id}")
	public String eliminarPrecio(@PathVariable("id") int id) {
		Precio c = this._service.GetById(id);
		
		if(c == null) {
			return  "notfound";
		}
		
		this._service.Delete(c);

		return "redirect:/Precio/Index";
	}
	
	@GetMapping("/Editar/{id}")
	public String editarPrecio(@PathVariable("id") int id, Model model) {
		Precio c = this._service.GetById(id);
		
		if(c == null) {
			return "notfound";
		}
		
		model.addAttribute("precio",c);
		
		return "/views/Precio/Editar";
	}
	
	@PostMapping("/Editar/{id}")
	public String editarPrecio(@PathVariable("id") int id, 
			@Valid @ModelAttribute("precio") Precio precio,
			BindingResult result) {
		if(result.hasErrors()) {
			return "error";
		}
		
		this._service.Update(precio);
		return "redirect:/Precio/Index";
	}	
	
	@GetMapping("/Nueva")
	public String nuevaPrecio(Model model) {
		model.addAttribute("name");
		return "/views/Precio/Nueva"; 
	}
	
	@PostMapping("/Nueva")
	public String nuevaPrecio(@Valid @ModelAttribute("precio")Precio precio,
			BindingResult result, ModelMap model) {
		if(result.hasErrors()) {
			return "error";
		}
		
		this._service.Add(precio);
		return "redirect:/Precio/Index";
	}
	

}
