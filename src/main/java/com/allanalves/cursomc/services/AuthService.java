package com.allanalves.cursomc.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.allanalves.cursomc.domain.Cliente;
import com.allanalves.cursomc.repositories.ClienteRepository;
import com.allanalves.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class AuthService {

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private EmailService emailService;

	private Random rand = new Random();

	public void sendNewPassword(String email) {
		Cliente cliente = clienteRepository.findByEmail(email);

		if (cliente == null) {
			throw new ObjectNotFoundException("E-mail não encontrado");
		}

		String newPass = newPassword();
		cliente.setSenha(passwordEncoder.encode(newPass));
		clienteRepository.save(cliente);
		emailService.sendNewPasswordEmail(cliente, newPass);
	}

	private String newPassword() {
		char[] vet = new char[10];

		for (int i = 0; i < 10; i++) {
			vet[i] = randomChar();
		}

		return new String(vet);
	}

	private char randomChar() {
		int opt = rand.nextInt(3);

		// Gera um digito
		if (opt == 0) {
			return (char) (rand.nextInt(10) + 48);
		} else if (opt == 1) { // Gera letra maiuscula
			return (char) (rand.nextInt(26) + 65);
		} else { // Gera letra minuscula
			return (char) (rand.nextInt(26) + 97);
		}
	}

}
