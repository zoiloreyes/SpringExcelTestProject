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

import com.zreyes.unapec.springexcel.model.Suplidor;
import com.zreyes.unapec.springexcel.service.ISuplidorService;

@Controller
@RequestMapping("/Suplidor")
public class SuplidorController {
	
	@Autowired
	private ISuplidorService _service;
	
	@GetMapping("/Index")
	public String suplidors(Model model) {
		List<Suplidor> suplidores= this._service.GetPaged(0, 0);
		model.addAttribute("suplidores", suplidores);
		
		return "views/Suplidor/Suplidores";
	}
	
	@GetMapping("/{id}")
	public String detalleSuplidor(@PathVariable("id") int id, Model model) {
		Suplidor c = this._service.GetById(id);
		if(c == null) {
			return "notfound";
		}
		
		model.addAttribute("suplidor", c);
		
		return "views/Suplidor/Detalle";
	}
	
	@GetMapping("/Eliminar/{id}")
	public String eliminarSuplidor(@PathVariable("id") int id) {
		Suplidor c = this._service.GetById(id);
		
		if(c == null) {
			return  "notfound";
		}
		
		this._service.Delete(c);

		return "redirect:/Suplidor/Index";
	}
	
	@GetMapping("/Editar/{id}")
	public String editarSuplidor(@PathVariable("id") int id, Model model) {
		Suplidor c = this._service.GetById(id);
		
		if(c == null) {
			return "notfound";
		}
		
		model.addAttribute("suplidor",c);
		
		return "views/Suplidor/Editar";
	}
	
	@PostMapping("/Editar/{id}")
	public String editarSuplidor(@PathVariable("id") int id, 
			@Valid @ModelAttribute("suplidor") Suplidor suplidor,
			BindingResult result) {
		if(result.hasErrors()) {
			return "error";
		}
		
		this._service.Update(suplidor);
		return "redirect:/Suplidor/Index";
	}	
	
	@GetMapping("/Nueva")
	public String nuevaSuplidor(Model model) {
		model.addAttribute("name");
		return "views/Suplidor/Nueva"; 
	}
	
	@PostMapping("/Nueva")
	public String nuevaSuplidor(@Valid @ModelAttribute("suplidor")Suplidor suplidor,
			BindingResult result, ModelMap model) {
		if(result.hasErrors()) {
			return "error";
		}
		
		this._service.Add(suplidor);
		return "redirect:/Suplidor/Index";
	}

}
