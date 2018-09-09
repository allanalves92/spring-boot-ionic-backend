package com.allanalves.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import com.allanalves.cursomc.domain.Pedido;

public interface EmailService {
	
	void sendOrderConfirmationEmail(Pedido pedido);
	
	void sendEmail(SimpleMailMessage msg);

}
