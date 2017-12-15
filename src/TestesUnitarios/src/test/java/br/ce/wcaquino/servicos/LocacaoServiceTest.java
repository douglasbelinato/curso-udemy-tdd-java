package br.ce.wcaquino.servicos;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTest {
	
	@Test
	public void teste() {
		// Cenário
		Usuario usuario = new Usuario("Douglas");
		Filme filme = new Filme("Star Wars VII - O despertar da força", 10, 4.5);
		
		// Ação
		Locacao locacao = new LocacaoService().alugarFilme(usuario, filme);
		
		// Verificação
		Assert.assertTrue(locacao.getValor() == 4.5);
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
	}

}
