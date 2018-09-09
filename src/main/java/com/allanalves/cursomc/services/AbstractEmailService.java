package com.allanalves.cursomc.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

import com.allanalves.cursomc.domain.Pedido;

public abstract class AbstractEmailService implements EmailService {

	@Value("${default.sender}")
	private String sender;

	@Override
	public void sendOrderConfirmationEmail(Pedido pedido) {
		SimpleMailMessage mailMessage = prepareSimpleMailMessageFromPedido(pedido);
		sendEmail(mailMessage);
	}

	protected SimpleMailMessage prepareSimpleMailMessageFromPedido(Pedido pedido) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(pedido.getCliente().getEmail());
		mailMessage.setFrom(sender);
		mailMessage.setSubject("Pedido confirmado! Código: " + pedido.getId());
		mailMessage.setSentDate(new Date(System.currentTimeMillis()));
		mailMessage.setText(pedido.toString());
		return mailMessage;
	}

}
