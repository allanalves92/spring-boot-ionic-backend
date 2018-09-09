package com.allanalves.cursomc.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.allanalves.cursomc.domain.ItemPedido;
import com.allanalves.cursomc.domain.PagamentoComBoleto;
import com.allanalves.cursomc.domain.Pedido;
import com.allanalves.cursomc.domain.Produto;
import com.allanalves.cursomc.domain.enums.EstadoPagamento;
import com.allanalves.cursomc.exceptions.ObjectNotFoundException;
import com.allanalves.cursomc.repositories.ItemPedidoRepository;
import com.allanalves.cursomc.repositories.PagamentoRepository;
import com.allanalves.cursomc.repositories.PedidoRepository;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository repository;

	@Autowired
	private BoletoService boletoService;

	@Autowired
	private PagamentoRepository pagamentoRepository;

	@Autowired
	private ProdutoService produtoService;

	@Autowired
	private ItemPedidoRepository itemPedidoRepository;

	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private EmailService emailService;

	public Pedido find(Integer id) {
		Optional<Pedido> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
	}

	public Pedido insert(Pedido pedido) {
		pedido.setId(null);
		pedido.setInstante(new Date());
		pedido.setCliente(clienteService.find(pedido.getCliente().getId()));
		pedido.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		pedido.getPagamento().setPedido(pedido);

		if (pedido.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagamentoComBoleto = (PagamentoComBoleto) pedido.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagamentoComBoleto, pedido.getInstante());
		}

		pedido = repository.save(pedido);
		pagamentoRepository.save(pedido.getPagamento());

		for (ItemPedido item : pedido.getItens()) {
			item.setDesconto(0.0);
			Produto produto = produtoService.find(item.getProduto().getId());
			item.setProduto(produto);
			item.setPreco(produto.getPreco());
			item.setPedido(pedido);
		}

		itemPedidoRepository.saveAll(pedido.getItens());

		emailService.sendOrderConfirmationEmail(pedido);

		return pedido;
	}
}
