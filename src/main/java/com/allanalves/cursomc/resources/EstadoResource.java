package com.allanalves.cursomc.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.allanalves.cursomc.domain.Cidade;
import com.allanalves.cursomc.domain.Estado;
import com.allanalves.cursomc.dto.CidadeDTO;
import com.allanalves.cursomc.dto.EstadoDTO;
import com.allanalves.cursomc.services.CidadeService;
import com.allanalves.cursomc.services.EstadoService;

@RestController
@RequestMapping(value = "/estados")
public class EstadoResource {

	@Autowired
	private EstadoService estadoService;

	@Autowired
	private CidadeService cidadeService;

	@GetMapping
	public ResponseEntity<List<EstadoDTO>> findAll() {
		List<Estado> estados = estadoService.findAll();
		List<EstadoDTO> listDto = estados.stream().map(estado -> new EstadoDTO(estado)).collect(Collectors.toList());
		return ResponseEntity.ok(listDto);
	}

	@GetMapping(value = "/{estadoId}/cidades")
	public ResponseEntity<List<CidadeDTO>> findCidades(@PathVariable Integer estadoId) {
		List<Cidade> cidades = cidadeService.findByEstado(estadoId);
		List<CidadeDTO> listDto = cidades.stream().map(cidade -> new CidadeDTO(cidade)).collect(Collectors.toList());
		return ResponseEntity.ok(listDto);
	}

}
