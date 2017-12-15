package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.Test;

import br.ce.wcaquino.entidades.Usuario;

public class AssertTest {
	
	@Test
	public void test() {
		Assert.assertTrue(true);
		Assert.assertFalse(false);
		
		Assert.assertEquals("Erro de comparação", 1, 1);
		Assert.assertEquals(4.123, 4.1234567, 00.1);
		
		Assert.assertEquals("texto", "texto");
		Assert.assertNotEquals("texto", "outroTexto");
		Assert.assertTrue("texto".equalsIgnoreCase("TextO"));
		
		Usuario usuario1 = new Usuario("José");
		Usuario usuario2 = new Usuario("José");
		Usuario usuario3 = usuario2;
		Usuario usuario4 = null;
		
		Assert.assertEquals(usuario1, usuario2);
		Assert.assertSame(usuario2, usuario3);
		Assert.assertNotSame(usuario1, usuario2);
		Assert.assertNull(usuario4);
		Assert.assertNotNull(usuario1);
	}

}
