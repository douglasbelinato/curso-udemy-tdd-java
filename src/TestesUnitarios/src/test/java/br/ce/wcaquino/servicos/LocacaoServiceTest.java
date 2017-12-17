package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
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
	public void deveAlugarFilme() throws Exception {
		Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		// Cenário
		Usuario usuario = new Usuario("Douglas");
		
		List<Filme> filmes = Arrays.asList(new Filme("Star Wars VII - O despertar da força", 10, 4.5),
									       new Filme("Matrix Reloaded", 10, 4.5));
		
		// Ação
		Locacao locacao = locacaoService.alugarFilme(usuario, filmes);
		
		// Verificação
		//Assert.assertEquals(4.5, locacao.getValor(), 0.01);
		error.checkThat(locacao.getValor(), is(equalTo(9.0)));
		error.checkThat(locacao.getValor(), is(not(6.0)));
		
		//Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
		error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		
		// Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
		error.checkThat(isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(true));		
	}
	
	// Tratando exceções - Modo elegante
	@Test(expected = FilmeSemEstoqueException.class)	 
	public void naoDeveAlugarFilmeSemEstoque() throws Exception {
		// Cenário
		Usuario usuario = new Usuario("Douglas");
		Filme filme1 = new Filme("Star Wars VII - O despertar da força", 0, 4.5);
		
		List<Filme> filmes = new ArrayList<>();
		filmes.add(filme1);
		
		// Ação
		locacaoService.alugarFilme(usuario, filmes);		
	}
	
	// usando a forma robusta
	@Test
	public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {		
		// Cenário
		Usuario usuario = null;
		Filme filme1 = new Filme("Star Wars VII - O despertar da força", 10, 4.5);
		
		List<Filme> filmes = new ArrayList<>();
		filmes.add(filme1);
		
		// Ação
		try {
			locacaoService.alugarFilme(usuario, filmes);
			Assert.fail();
		} catch (LocadoraException e) {
			// Verificação
			assertThat(e.getMessage(), is("Usuário vazio"));
		}		
	}
	
	// Usando modo com @Rule ExpectedException
	@Test
	public void naoDeveAlugarFilmeSemListaDeFilmes() throws FilmeSemEstoqueException, LocadoraException {
		// Cenário
		Usuario usuario = new Usuario("Douglas");

		List<Filme> filmes = new ArrayList<>();
		
		// Verificação
		expectedException.expect(LocadoraException.class);
		expectedException.expectMessage("Lista de filmes vazia");
		
		// Ação
		locacaoService.alugarFilme(usuario, filmes);		
	}
	
	// Usando modo com @Rule ExpectedException
	@Test
	public void naoDeveAlugarFilmeSemFilme() throws FilmeSemEstoqueException, LocadoraException {
		// Cenário
		Usuario usuario = new Usuario("Douglas");
		Filme filme1 = null;
		
		List<Filme> filmes = new ArrayList<>();
		filmes.add(filme1);
		
		// Verificação
		expectedException.expect(LocadoraException.class);
		expectedException.expectMessage("Filme vazio");
		
		// Ação
		locacaoService.alugarFilme(usuario, filmes);		
	}
	
	@Test
	public void deveDevolverNaSegundaAoAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException {
		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		// Cenário
		Usuario usuario = new Usuario("Douglas");
		Filme filme1 = new Filme("Star Wars VII - O despertar da força", 10, 5.0);
		
		List<Filme> filmes = new ArrayList<>();
		filmes.add(filme1);
		
		// Ação
		Locacao locacao = locacaoService.alugarFilme(usuario, filmes);
		
		// Verificação
		boolean ehSegunda = DataUtils.verificarDiaSemana(locacao.getDataRetorno(), Calendar.MONDAY);
		assertTrue(ehSegunda);
	}

}
