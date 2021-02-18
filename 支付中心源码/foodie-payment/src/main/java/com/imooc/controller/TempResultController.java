package com.imooc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@ApiIgnore
public class TempResultController {

	@GetMapping("/alipayResult")
	public String alipayResult(HttpServletRequest request, HttpServletResponse response) {

		return "alipayResult";
	}
}
