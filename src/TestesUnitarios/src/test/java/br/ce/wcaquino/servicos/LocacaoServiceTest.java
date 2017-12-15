package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.util.Date;

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
		//Assert.assertEquals(4.5, locacao.getValor(), 0.01);
		assertThat(locacao.getValor(), is(equalTo(4.5)));
		assertThat(locacao.getValor(), is(not(6.0)));
		
		//Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
		assertThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		
		// Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
		assertThat(isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(true));
	}

}
