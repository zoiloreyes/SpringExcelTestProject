package com.zreyes.unapec.springexcel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zreyes.unapec.springexcel.service.IExcelService;

@Controller
@RequestMapping("/Database")
public class DatabaseController {
	@Autowired
	IExcelService exService;

	@GetMapping(value = "/db.xlsx", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public @ResponseBody byte[] excelDB() {
		return exService.getFileBytes();
	}
}
