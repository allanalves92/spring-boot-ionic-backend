package com.allanalves.cursomc.services;

import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.allanalves.cursomc.domain.Pedido;

public abstract class AbstractEmailService implements EmailService {

	@Value("${default.sender}")
	private String sender;

	@Autowired
	private TemplateEngine templateEngine;

	@Autowired
	private JavaMailSender javaMailSender;

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

	@Override
	public void sendOrderConfirmationHtmlEmail(Pedido pedido) {
		try {
			MimeMessage mmMessage = prepareMimeMessageFromPedido(pedido);
			sendHtmlEmail(mmMessage);
		} catch (MessagingException e) {
			sendOrderConfirmationEmail(pedido);
		}
	}

	protected MimeMessage prepareMimeMessageFromPedido(Pedido pedido) throws MessagingException {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
		messageHelper.setTo(pedido.getCliente().getEmail());
		messageHelper.setFrom(sender);
		messageHelper.setSubject("Pedido confirmado! Código: " + pedido.getId());
		messageHelper.setSentDate(new Date(System.currentTimeMillis()));
		messageHelper.setText(htmlFromTemplatePedido(pedido), true);
		return mimeMessage;
	}

	protected String htmlFromTemplatePedido(Pedido pedido) {
		Context context = new Context();
		context.setVariable("pedido", pedido);
		return templateEngine.process("email/confirmacaoPedido", context);
	}

}
