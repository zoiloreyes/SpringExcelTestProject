package com.zreyes.unapec.springexcel.service;

import java.util.List;

import com.zreyes.unapec.springexcel.model.Suplidor;

public interface ISuplidorService {

	Suplidor Add(Suplidor suplidor);

	Suplidor Update(Suplidor suplidor);

	Suplidor Delete(Suplidor suplidor);

	Suplidor GetById(int id);

	List<Suplidor> GetPaged(int page, int pageSize);

}