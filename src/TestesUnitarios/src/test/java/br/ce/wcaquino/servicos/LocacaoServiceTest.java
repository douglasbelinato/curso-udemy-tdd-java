package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTest {
	
	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Test
	public void testeLocacao() throws Exception {
		// Cenário
		Usuario usuario = new Usuario("Douglas");
		Filme filme = new Filme("Star Wars VII - O despertar da força", 10, 4.5);
		
		// Ação
		Locacao locacao = new LocacaoService().alugarFilme(usuario, filme);
		
		// Verificação
		//Assert.assertEquals(4.5, locacao.getValor(), 0.01);
		error.checkThat(locacao.getValor(), is(equalTo(4.5)));
		error.checkThat(locacao.getValor(), is(not(6.0)));
		
		//Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
		error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		
		// Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
		error.checkThat(isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(true));		
	}
	
	// Tratando exceções - Modo elegante
	@Test(expected = Exception.class)	 
	public void testeLocacaoFilmeSemEstoque() throws Exception {
		// Cenário
		Usuario usuario = new Usuario("Douglas");
		Filme filme = new Filme("Star Wars VII - O despertar da força", 0, 4.5);
		
		// Ação
		new LocacaoService().alugarFilme(usuario, filme);		
	}
	
	// Tratando exceções - Modo robusto
	@Test
	public void testeLocacaoFilmeSemEstoque2() {
		// Cenário
		Usuario usuario = new Usuario("Douglas");
		Filme filme = new Filme("Star Wars VII - O despertar da força", 0, 4.5);
		
		// Ação
		try {
			new LocacaoService().alugarFilme(usuario, filme);
			Assert.fail("Deveria lançar exceção");
		} catch (Exception e) {
			assertThat(e.getMessage(), is("Filme sem estoque"));
		}
	}
	
	// Tratando exceções - Modo com @Rule ExpectedException
		@Test
		public void testeLocacaoFilmeSemEstoque3() throws Exception {
			// Cenário
			Usuario usuario = new Usuario("Douglas");
			Filme filme = new Filme("Star Wars VII - O despertar da força", 0, 4.5);
			
			expectedException.expect(Exception.class);
			expectedException.expectMessage("Filme sem estoque");
			
			// Ação
			new LocacaoService().alugarFilme(usuario, filme);
			
		}

}
