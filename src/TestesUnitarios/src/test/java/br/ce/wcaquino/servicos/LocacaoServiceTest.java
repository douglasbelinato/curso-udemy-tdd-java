package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTest {
	
	private LocacaoService locacaoService;
	
	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Before
	public void setup() {
		locacaoService = new LocacaoService();
	}
	
	@Test
	public void testeLocacao() throws Exception {
		// Cenário
		Usuario usuario = new Usuario("Douglas");
		Filme filme = new Filme("Star Wars VII - O despertar da força", 10, 4.5);
		
		// Ação
		Locacao locacao = locacaoService.alugarFilme(usuario, filme);
		
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
	@Test(expected = FilmeSemEstoqueException.class)	 
	public void testeLocacaoFilmeSemEstoque() throws Exception {
		// Cenário
		Usuario usuario = new Usuario("Douglas");
		Filme filme = new Filme("Star Wars VII - O despertar da força", 0, 4.5);
		
		// Ação
		locacaoService.alugarFilme(usuario, filme);		
	}
	
	// usando a forma robusta
	@Test
	public void testeLocacaoUsuarioVazio() throws FilmeSemEstoqueException {		
		// Cenário
		Usuario usuario = null;
		Filme filme = new Filme("Star Wars VII - O despertar da força", 10, 4.5);
		
		// Ação
		try {
			locacaoService.alugarFilme(usuario, filme);
			Assert.fail();
		} catch (LocadoraException e) {
			assertThat(e.getMessage(), is("Usuário vazio"));
		}		
	}
	
	// Usando modo com @Rule ExpectedException
	@Test
	public void testeLocacaoFileVazio() throws FilmeSemEstoqueException, LocadoraException {
		// Cenário
		Usuario usuario = new Usuario("Douglas");
		Filme filme = null;
		
		expectedException.expect(LocadoraException.class);
		expectedException.expectMessage("Filme vazio");
		
		// Ação
		locacaoService.alugarFilme(usuario, filme);		
	}

}
