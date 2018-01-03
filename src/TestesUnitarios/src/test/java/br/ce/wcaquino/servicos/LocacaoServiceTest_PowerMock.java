package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.builder.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builder.UsuarioBuilder.umUsuario;
import static br.ce.wcaquino.matchers.MatchersProprios.caiNumaSegunda;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;

@RunWith(PowerMockRunner.class) // Informa o JUnit que o runner de execução será gerenciado pelo PowerMock
@PrepareForTest({LocacaoService.class})
public class LocacaoServiceTest_PowerMock {
	
	@InjectMocks
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
		locacaoService = PowerMockito.spy(locacaoService); // spy do powermockito
	}
	
	@Test
	public void deveAlugarFilme() throws Exception {
		// Posso tirar para poder manipular a data com o PowerMock
		// Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		// Cenário
		Usuario usuario = umUsuario().agora();
		
		List<Filme> filmes = Arrays.asList(umFilme().comValor(5.0).agora());
		
		PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(28, 4, 2017)); // data que é um sábado
		
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
		PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(29, 4, 2017));
		
		// Ação
		Locacao locacao = locacaoService.alugarFilme(usuario, filmes);
		
		// Verificação
		//assertThat(locacao.getDataRetorno(), caiEm(Calendar.SUNDAY));
		assertThat(locacao.getDataRetorno(), caiNumaSegunda());
		
		// verifica qts vezes foi criada uma instância de Date
		PowerMockito.verifyNew(Date.class, Mockito.times(2)).withNoArguments();
	}

	@Test
	public void deveAlugarFilmeSemCalcularValor() throws Exception {
		// Cenário
		Usuario usuario = umUsuario().agora();		
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
		PowerMockito.doReturn(1.0).when(locacaoService, "calcularValorLocacao", filmes);
				
		// Ação
		Locacao locacao = locacaoService.alugarFilme(usuario, filmes);
		
		// Verificação
		Assert.assertThat(locacao.getValor(), is(1.0));
		PowerMockito.verifyPrivate(locacaoService).invoke("calcularValorLocacao", filmes);
	}
	
	@Test
	public void deveCalcularLocacao() throws Exception {
		// Cenário
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
		// Ação
		// Classe Whitebox existe tanto no Mockito qto no PowerMock, porém, é
		// a do PoweMock que permite executar o método privado de modo concreto.
		Double valor = (Double) Whitebox.invokeMethod(locacaoService, "calcularValorLocacao", filmes);
		
		// Verificação
		Assert.assertThat(valor, is(4.0));
		
	}
	

}
