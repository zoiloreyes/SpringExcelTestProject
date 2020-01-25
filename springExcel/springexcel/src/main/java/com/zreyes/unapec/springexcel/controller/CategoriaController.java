package com.zreyes.unapec.springexcel.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
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
import org.springframework.web.bind.annotation.RequestParam;

import com.zreyes.unapec.springexcel.model.Categoria;
import com.zreyes.unapec.springexcel.service.ICategoriaService;
import com.zreyes.unapec.springexcel.service.IExcelService;

@Controller
@RequestMapping("/Categoria")
public class CategoriaController {
	
	@Autowired
	private ICategoriaService _exService;

	@GetMapping("/Index")
	public String categorias(Model model) {
		List<Categoria> categorias = this._exService.GetPaged(0, 0);
		model.addAttribute("categorias", categorias);
		
		return "/views/Categoria/Categorias";
	}
		
	@GetMapping("/{id}")
	public String detalleCategoria(@PathVariable("id") int id, Model model) {
		Categoria c = this._exService.GetById(id);
		if(c == null) {
			return "notfound";
		}
		
		model.addAttribute("categoria", c);
		
		return "/views/Categoria/Detalle";
	}
	
	@GetMapping("/Eliminar/{id}")
	public String eliminarCategoria(@PathVariable("id") int id) {
		Categoria c = this._exService.GetById(id);
		
		if(c == null) {
			return  "notfound";
		}
		
		this._exService.Delete(c);

		return "redirect:/Categoria/Index";
	}
	
	@GetMapping("/Editar/{id}")
	public String editarCategoria(@PathVariable("id") int id, Model model) {
		Categoria c = this._exService.GetById(id);
		
		if(c == null) {
			return "notfound";
		}
		
		model.addAttribute("categoria",c);
		
		return "/views/Categoria/Editar";
	}
	
	@PostMapping("/Editar/{id}")
	public String editarCategoria(@PathVariable("id") int id, 
			@Valid @ModelAttribute("categoria") Categoria categoria,
			BindingResult result) {
		if(result.hasErrors()) {
			return "error";
		}
		
		this._exService.Update(categoria);
		return "redirect:/Categoria/Index";
	}	
	
	@GetMapping("/Nueva")
	public String nuevaCategoria(Model model) {
		model.addAttribute("name");
		return "/views/Categoria/Nueva"; 
	}
	
	@PostMapping("/Nueva")
	public String submitNueva(@Valid @ModelAttribute("categoria")Categoria categoria,
			BindingResult result, ModelMap model) {
		if(result.hasErrors()) {
			return "error";
		}
		
		this._exService.Add(categoria);
		return "redirect:/Categoria/Index";
	}
}
