package br.ce.wcaquino.servicos;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.ce.wcaquino.exceptions.NaoPodeDividirPorZeroException;

// Quando não especifico o Runner da classe com a anotação
// @RunWith, a classe executa com o Runner padrão do JUnit.
// Seria o mesmo que colocar:
// @RunWith(JUnit4.class)
// @RunWith(BlockJUnit4ClassRunner) // a partir da versão 4.5 do JUnit
//@RunWith(ParallelRunner.class)
public class CalculadoraTest {
	
	private Calculadora calc;
	
	public static StringBuffer ordem = new StringBuffer();
	
	@Before
	public void setup() {
		calc = new Calculadora();
		System.out.println("Iniciando...");
		ordem.append("1");
	}
	
	@After
	public void tearDown() {
		System.out.println("Finalizando...");
	}
	
	@AfterClass
	public static void tearDownClass() {
		System.out.println(ordem.toString());
	}
	
	@Test
	public void deveSomarDoisValores() {
		// cenário
		int a = 5;
		int b = 3;

		// ação
		int resultado = calc.somar(a,b);
		
		// verificação
		Assert.assertEquals(8, resultado);
	}
	
	@Test
	public void deveSubtrairDoisValores() {
		// cenário
		int a = 8;
		int b = 5;
		
		// ação
		int resultado = calc.subtrair(a,b);
		
		// verificação
		Assert.assertEquals(3, resultado);
	}
	
	@Test
	public void deveDividirDoisValores() throws NaoPodeDividirPorZeroException {
		// cenário
		int a = 6;
		int b = 3;
		
		// ação
		int resultado = calc.dividir(a,b);
		
		// verificação
		Assert.assertEquals(2, resultado);
	}
	
	@Test(expected = NaoPodeDividirPorZeroException.class)
	public void deveLancarExcecaoAoDividirPorZero() throws NaoPodeDividirPorZeroException {
		// cenário
		int a = 10;
		int b = 0;
		
		// ação
		int resultado = calc.dividir(a,b);
		
		// verificação
		Assert.assertEquals(2, resultado);
	}

}
