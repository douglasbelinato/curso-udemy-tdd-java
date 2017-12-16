package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OrdemTest {
	
	private static int contador = 0;
	
	@Test
	public void t01_inicia() {
		contador = 1;
	}
	
	@Test
	public void t02_verifica() {
		Assert.assertEquals(1, contador);
	}
	
	@Test
	public void t03_finaliza() {
		System.out.println("Encerrando...");
	}

}
