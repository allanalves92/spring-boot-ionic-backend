package com.allanalves.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.allanalves.cursomc.domain.Cidade;
import com.allanalves.cursomc.domain.Cliente;
import com.allanalves.cursomc.domain.Endereco;
import com.allanalves.cursomc.domain.enums.TipoCliente;
import com.allanalves.cursomc.dto.ClienteDTO;
import com.allanalves.cursomc.dto.ClienteNewDTO;
import com.allanalves.cursomc.exceptions.DataIntegrityException;
import com.allanalves.cursomc.exceptions.ObjectNotFoundException;
import com.allanalves.cursomc.repositories.ClienteRepository;
import com.allanalves.cursomc.repositories.EnderecoRepository;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repository;

	@Autowired
	private EnderecoRepository enderecoRepository;

	public Cliente find(Integer id) {
		Optional<Cliente> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}

	@Transactional
	public Cliente insert(Cliente cliente) {
		enderecoRepository.saveAll(cliente.getEnderecos());
		return repository.save(cliente);
	}

	public Cliente update(Cliente cliente) {
		Cliente newObj = find(cliente.getId());
		updateData(newObj, cliente);
		return repository.save(newObj);
	}

	public void delete(Integer id) {
		find(id);

		try {
			repository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir um cliente que possui pedidos");
		}
	}

	public List<Cliente> findAll() {
		return repository.findAll();
	}

	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repository.findAll(pageRequest);
	}

	public Cliente fromDTO(ClienteDTO clienteDTO) {
		return new Cliente(clienteDTO.getId(), clienteDTO.getNome(), clienteDTO.getEmail(), null, null);
	}

	public Cliente fromDTO(ClienteNewDTO clienteDTO) {
		Cliente cli = new Cliente(null, clienteDTO.getNome(), clienteDTO.getEmail(), clienteDTO.getCpfOuCnpj(),
				TipoCliente.toEnum(clienteDTO.getTipo()));

		Cidade cid = new Cidade(clienteDTO.getCidadeId(), null, null);

		Endereco end = new Endereco(null, clienteDTO.getLogradouro(), clienteDTO.getNumero(),
				clienteDTO.getComplemento(), clienteDTO.getBairro(), clienteDTO.getCep(), cli, cid);

		cli.getEnderecos().add(end);
		cli.getTelefones().add(clienteDTO.getTelefone1());

		if (clienteDTO.getTelefone2() != null) {
			cli.getTelefones().add(clienteDTO.getTelefone2());
		}

		if (clienteDTO.getTelefone3() != null) {
			cli.getTelefones().add(clienteDTO.getTelefone3());
		}
		return cli;
	}

	private void updateData(Cliente newObj, Cliente cliente) {
		newObj.setNome(cliente.getNome());
		newObj.setEmail(cliente.getEmail());
	}
}
