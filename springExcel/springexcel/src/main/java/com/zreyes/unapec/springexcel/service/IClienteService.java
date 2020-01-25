package com.zreyes.unapec.springexcel.service;

import java.util.List;

import com.zreyes.unapec.springexcel.model.Cliente;

public interface IClienteService {

	Cliente Add(Cliente cliente);

	Cliente Update(Cliente cliente);

	Cliente Delete(Cliente cliente);

	Cliente GetById(int id);

	List<Cliente> GetPaged(int page, int pageSize);

}