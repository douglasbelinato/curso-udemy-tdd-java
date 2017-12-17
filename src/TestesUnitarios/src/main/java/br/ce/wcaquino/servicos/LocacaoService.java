package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoService {
	
	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws FilmeSemEstoqueException, LocadoraException {
		Double valorLocacao = 0d;
		double percentualDesconto = 0d;
		
		if (usuario == null) {
			throw new LocadoraException("Usuário vazio");
		}
		
		if (filmes == null || filmes.isEmpty()) {
			throw new LocadoraException("Lista de filmes vazia");
		}
		
		switch (filmes.size()) {
		case 3:
			percentualDesconto = 0.25;
			break;
		case 4:
			percentualDesconto = 0.50;
			break;
		case 5:
			percentualDesconto = 0.75;
			break;
		case 6:
			percentualDesconto = 1d;
			break;
		}
		
		for (Filme filme : filmes) {
			if (filme == null) {
				throw new LocadoraException("Filme vazio");
			}
			
			if (filme.getEstoque() == 0) {
				throw new FilmeSemEstoqueException();
			}
			
			valorLocacao += filme.getPrecoLocacao();
		}
		
		// Obtém o valor do último filme, aplica desconto e tira o valor da soma total do valor locação
		Filme ultimoFilme = filmes.get(filmes.size() - 1);
		valorLocacao -= (ultimoFilme.getPrecoLocacao() * percentualDesconto);
		
		Locacao locacao = new Locacao();
		locacao.setFilmes(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		locacao.setValor(valorLocacao);

		//Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);
		
		if (DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY)) {
			dataEntrega = adicionarDias(dataEntrega, 1);
		}
		
		locacao.setDataRetorno(dataEntrega);
		
		//Salvando a locacao...	
		//TODO adicionar método para salvar
		
		return locacao;
	}

}