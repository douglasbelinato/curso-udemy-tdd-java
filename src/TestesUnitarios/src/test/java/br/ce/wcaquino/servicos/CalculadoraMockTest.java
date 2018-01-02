package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

public class CalculadoraMockTest {
	
	@Mock
	private Calculadora calcMock;
	
	@Spy
	private Calculadora calcSpy;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void deveMostrarDiferencaMockSpy() {
		//Mockito.when(calcMock.somar(1, 2)).thenReturn(8);
		//Mockito.when(calcMock.somar(1, 2)).thenCallRealMethod();
		//Mockito.when(calcSpy.somar(1, 2)).thenReturn(8); // Com Spy, na hroa de gravar a expectativa o método somar está executando por completo 
		Mockito.doReturn(8).when(calcSpy).somar(1, 2); // Dessa forma não executo o somar ao gravar a expectativa 
		Mockito.doNothing().when(calcSpy).imprime();
		
		System.out.println("Mock: " + calcMock.somar(1, 2));
		System.out.println("Spy: " + calcSpy.somar(1, 2));
		
		// Para o Mock, o comportamento padrão é:
		// Se o método for void, não executa.
		// Se o método não for void, retorna valor padrão 
		// do tipo de retorno do método.
		System.out.println("Mock:");
		calcMock.imprime();
		
		// Para o Spy, o comportamento padrão é:
		// Executar o método por compelto na classe concreta.
		// Para ele não executar, assim como o Mock, eu tenho que
		// definir Mockito.doNothing().when(calcSpy).imprime();
		System.out.println("Spy:");
		calcSpy.imprime();
	}
	
	@Test
	public void test() {
		Calculadora calc = Mockito.mock(Calculadora.class);
		
		ArgumentCaptor<Integer> argCapt = ArgumentCaptor.forClass(Integer.class);
		Mockito.when(calc.somar(argCapt.capture(), argCapt.capture())).thenReturn(5);
		
		Assert.assertEquals(5, calc.somar(1, 2));
		System.out.println(argCapt.getAllValues());
	}


}
