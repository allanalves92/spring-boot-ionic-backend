package com.allanalves.cursomc.resources;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.allanalves.cursomc.domain.Categoria;

@RestController
@RequestMapping(value = "/categorias")
public class CategoriaResource {

	@RequestMapping(method = RequestMethod.GET)
	public List<Categoria> listar() {
		Categoria cate1 = new Categoria(1, "Informática");
		Categoria cate2 = new Categoria(2, "Escritório");

		List<Categoria> lista = new ArrayList<Categoria>();
		lista.add(cate1);
		lista.add(cate2);

		return lista;
	}

}
