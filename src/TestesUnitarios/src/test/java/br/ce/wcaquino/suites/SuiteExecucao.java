package br.ce.wcaquino.suites;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import br.ce.wcaquino.servicos.CalculadoraTest;
import br.ce.wcaquino.servicos.CalculoValorLocacaoTest;
import br.ce.wcaquino.servicos.LocacaoServiceTest;

//@RunWith --> Eu informo ao JUnit (Test Runner) que a execução será feita de modo diferente
//@RunWith(Suite.class)
@SuiteClasses({
	//CalculadoraTest.class,
	CalculoValorLocacaoTest.class,
	LocacaoServiceTest.class
})
public class SuiteExecucao {

	@BeforeClass
	public static void beforeClass() {
		System.out.println("Before Class - Suite");
	}
	
	@AfterClass
	public static void afterClass() {
		System.out.println("After Class - Suite");
	}
}
