package com.allanalves.cursomc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.allanalves.cursomc.domain.Cidade;
import com.allanalves.cursomc.repositories.CidadeRepository;

@Service
public class CidadeService {

	@Autowired
	private CidadeRepository repository;

	public List<Cidade> findByEstado(Integer id) {
		return repository.findByEstadoId(id);
	}

}
