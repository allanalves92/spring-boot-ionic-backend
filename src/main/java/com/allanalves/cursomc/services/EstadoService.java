package com.allanalves.cursomc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.allanalves.cursomc.domain.Estado;
import com.allanalves.cursomc.repositories.EstadoRepository;

@Service
public class EstadoService {

	@Autowired
	private EstadoRepository repository;

	public List<Estado> findAll() {
		return repository.findAllByOrderByNome();
	}
}
