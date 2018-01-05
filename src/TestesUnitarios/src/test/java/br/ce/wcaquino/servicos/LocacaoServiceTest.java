package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.builder.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builder.FilmeBuilder.umFilmeSemEstoque;
import static br.ce.wcaquino.builder.LocacaoBuilder.umLocacao;
import static br.ce.wcaquino.builder.UsuarioBuilder.umUsuario;
import static br.ce.wcaquino.matchers.MatchersProprios.caiNumaSegunda;
import static br.ce.wcaquino.matchers.MatchersProprios.ehHoje;
import static br.ce.wcaquino.matchers.MatchersProprios.ehHojeComDiferencaDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;

//@RunWith(ParallelRunner.class)
public class LocacaoServiceTest {
	
	@InjectMocks @Spy
	private LocacaoService locacaoService;
	
	@Mock
	private LocacaoDAO dao;
	
	@Mock
	private SPCService spcService;
	
	@Mock
	private EmailService emailService;
	
	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Before
	public void setup() {		
		MockitoAnnotations.initMocks(this);
		System.out.println("Iniciando 2...");
		CalculadoraTest.ordem.append("2");
	}
	
	@After
	public void tearDown() {
		System.out.println("Finalizando 2...");
	}
	
	@AfterClass
	public static void tearDownClass() {
		System.out.println(CalculadoraTest.ordem.toString());
	}
	
	@Test
	public void deveAlugarFilme() throws Exception {
		// Posso tirar para poder manipular a data com o PowerMock
		// Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		// Cenário
		Usuario usuario = umUsuario().agora();
		
		List<Filme> filmes = Arrays.asList(umFilme().comValor(5.0).agora());
		
		// Contornando o uso de PowerMock para chamar métodos privados e chamadas estáticas 
		// Altera o comportamento do método obterData() - que agora é um método protected para permitir a manipulação
		// do new Date()
		Mockito.doReturn(DataUtils.obterData(28, 4, 2017)).when(locacaoService).obterData();
		
		// Ação
		Locacao locacao = locacaoService.alugarFilme(usuario, filmes);
		
		// Verificação
		//Assert.assertEquals(4.5, locacao.getValor(), 0.01);
		error.checkThat(locacao.getValor(), is(equalTo(5.0)));
		error.checkThat(locacao.getValor(), is(not(6.0)));
		
		//Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
		//error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		//error.checkThat(locacao.getDataLocacao(), ehHoje());
		
		// Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
		//error.checkThat(isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(true));
		//error.checkThat(locacao.getDataRetorno(), ehHojeComDiferencaDias(1));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), DataUtils.obterData(28, 4, 2017)), is(true));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterData(29, 4, 2017)), is(true));
	}
	
	// Tratando exceções - Modo elegante
	@Test(expected = FilmeSemEstoqueException.class)	 
	public void naoDeveAlugarFilmeSemEstoque() throws Exception {
		// Cenário
		Usuario usuario = umUsuario().agora();
		Filme filme1 = umFilmeSemEstoque().agora();
		
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
		Filme filme1 = umFilme().agora();
		
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
		Usuario usuario = umUsuario().agora();

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
		Usuario usuario = umUsuario().agora();
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
	public void deveDevolverNaSegundaAoAlugarNoSabado() throws Exception {
		// Posso tirar para poder manipular a data com o PowerMock
		// Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		// Cenário
		Usuario usuario = umUsuario().agora();
		Filme filme1 = umFilme().agora();
		
		List<Filme> filmes = new ArrayList<>();
		filmes.add(filme1);
		
		// data que é um sábado
		// Contornando o uso de PowerMock para chamar métodos privados e chamadas estáticas 
		// Altera o comportamento do método obterData() - que agora é um método protected para permitir a manipulação
		// do new Date()
		Mockito.doReturn(DataUtils.obterData(29, 4, 2017)).when(locacaoService).obterData();
				
		// Ação
		Locacao locacao = locacaoService.alugarFilme(usuario, filmes);
		
		// Verificação
		//assertThat(locacao.getDataRetorno(), caiEm(Calendar.SUNDAY));
		assertThat(locacao.getDataRetorno(), caiNumaSegunda());
	}
	
	@Test
	public void naoDeveAlugarFilmeParaNegativadoSPC() throws Exception {
		// cenário
		Usuario usuario = umUsuario().agora();
		//Usuario usuario2 = umUsuario().comNome("Douglas").agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
		when(spcService.possuiNegatividade(Mockito.any(Usuario.class))).thenReturn(true);
					
		// ação
		try {
			locacaoService.alugarFilme(usuario, filmes);
			// Garante que não seja gerado um falso positivo, caso a exceção não seja lançada
			Assert.fail();
		} catch (LocadoraException e) {
			assertThat(e.getMessage(), is("Usuário negativado"));
		}
		//locacaoService.alugarFilme(usuario2, filmes); // para checar se não ocorre falso positivo
		
		// verificação
		verify(spcService).possuiNegatividade(usuario);
	}
	
	@Test
	public void deveEnviarEmailParaLocacoesAtrasadas() {
		// cenário
		Usuario usuario = umUsuario().agora();
		//Usuario usuario2 = umUsuario().comNome("Douglas").agora();
		Usuario usuario3 = umUsuario().comNome("Usuário em dia").agora();
		Usuario usuario4 = umUsuario().comNome("Outro atrasado").agora();
		List<Locacao> locacoes = Arrays.asList(
				umLocacao().atrasado().comUsuario(usuario).agora(),
				umLocacao().comUsuario(usuario3).agora(),
				umLocacao().atrasado().comUsuario(usuario4).agora(),
				umLocacao().atrasado().comUsuario(usuario4).agora());
		
		when(dao.obterLocacoesPendentes()).thenReturn(locacoes);
		
		// ações
		locacaoService.notificarAtrasos();
		
		// verificação
		verify(emailService, times(3)).notificarAtraso(Mockito.any(Usuario.class));
		
		verify(emailService).notificarAtraso(usuario);
		verify(emailService, atLeastOnce()).notificarAtraso(usuario4);
		verify(emailService, never()).notificarAtraso(usuario3);
		//verify(emailService).notificarAtraso(usuario2); // para checar se não ocorre falso positivo
		
		Mockito.verifyNoMoreInteractions(emailService);
	}
	
	@Test
	public void deveTratarErroNoSpc() throws Exception {
		// cenário
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
		when(spcService.possuiNegatividade(usuario)).thenThrow(new Exception("SPC Exception call"));
		
		// verificação
		expectedException.expect(LocadoraException.class);
		expectedException.expectMessage("Problemas com SPC, tente novamente");
		
		// ação
		locacaoService.alugarFilme(usuario, filmes);
	}
	
	@Test
	public void deveProrrogarUmaLocacao() {
		// cenário
		Locacao locacao = umLocacao().agora();
		
		// ação
		locacaoService.prorrogarLocacao(locacao, 3);
		
		// verificação
		ArgumentCaptor<Locacao> argCapt = ArgumentCaptor.forClass(Locacao.class);
		Mockito.verify(dao).salvar(argCapt.capture());
		Locacao locacaoRetornada = argCapt.getValue();
		
		error.checkThat(locacaoRetornada.getValor(), is(12.0));
		error.checkThat(locacaoRetornada.getDataLocacao(), ehHoje());
		error.checkThat(locacaoRetornada.getDataRetorno(), ehHojeComDiferencaDias(3));
	}
	
	@Test
	public void deveCalcularLocacao() throws Exception {
		// Cenário
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
		Thread.sleep(5000);
		
		// Ação
		// Utilizando a API Reflexion do Java para acessar o método privado
		// sem o uso do PowerMock
		// Útil quando o método privado é mto complexo e se quer dar um foco
		// maior nele.
		Class<LocacaoService> clazz = LocacaoService.class;
		Method method = clazz.getDeclaredMethod("calcularValorLocacao", List.class);
		method.setAccessible(true);
		
		Double valor = (Double) method.invoke(locacaoService, filmes);
		
		// Verificação
		Assert.assertThat(valor, is(4.0));
		
	}
	

}
