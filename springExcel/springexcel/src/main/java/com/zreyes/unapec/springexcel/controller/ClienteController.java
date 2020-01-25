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
import org.springframework.web.bind.annotation.RequestParam;

import com.zreyes.unapec.springexcel.model.Categoria;
import com.zreyes.unapec.springexcel.model.Cliente;
import com.zreyes.unapec.springexcel.service.IClienteService;

@Controller
@RequestMapping("/Cliente")
public class ClienteController {
	
	@Autowired
	IClienteService cliService;
	
	@GetMapping("/Index")
	public String clientes(Model model) {
		List<Cliente> clientes = this.cliService.GetPaged(0, 0);
		model.addAttribute("clientes", clientes);
		return "/views/cliente/Clientes";
	}
	
	@GetMapping("/{id}")
	public String detalleCliente(@PathVariable("id") int id, Model model) {
		Cliente c = this.cliService.GetById(id);
		if(c == null) {
			return "notfound";
		}
		
		model.addAttribute("cliente", c);
		
		return "/views/Cliente/Detalle";
	}
	
	@GetMapping("/Eliminar/{id}")
	public String eliminarCliente(@PathVariable("id") int id) {
		Cliente c = this.cliService.GetById(id);
		
		if(c == null) {
			return  "notfound";
		}
		
		this.cliService.Delete(c);

		return "redirect:/Cliente/Index";
	}
	
	@GetMapping("/Editar/{id}")
	public String editarCliente(@PathVariable("id") int id, Model model) {
		Cliente c = this.cliService.GetById(id);
		
		if(c == null) {
			return "notfound";
		}
		
		model.addAttribute("cliente",c);
		
		return "/views/Cliente/Editar";
	}
	
	@PostMapping("/Editar/{id}")
	public String editarCliente(@PathVariable("id") int id, 
			@Valid @ModelAttribute("cliente") Cliente cliente,
			BindingResult result) {
		if(result.hasErrors()) {
			return "error";
		}
		
		this.cliService.Update(cliente);
		return "redirect:/Cliente/Index";
	}	
	
	@GetMapping("/Nueva")
	public String nuevaCliente(Model model) {
		model.addAttribute("name");
		return "/views/Cliente/Nueva"; 
	}
	
	@PostMapping("/Nueva")
	public String submitNueva(@Valid @ModelAttribute("cliente")Cliente cliente,
			BindingResult result, ModelMap model) {
		if(result.hasErrors()) {
			return "error";
		}
		
		this.cliService.Add(cliente);
		return "redirect:/Cliente/Index";
	}
}
