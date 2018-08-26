package com.allanalves.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.allanalves.cursomc.domain.Pedido;
import com.allanalves.cursomc.exceptions.ObjectNotFoundException;
import com.allanalves.cursomc.repositories.PedidoRepository;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository repository;

	public Pedido buscar(Integer id) {
		Optional<Pedido> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
	}
}
