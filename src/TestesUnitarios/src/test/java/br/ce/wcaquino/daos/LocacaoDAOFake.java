package br.ce.wcaquino.daos;

import java.util.List;

import br.ce.wcaquino.entidades.Locacao;

/**
 *  Padrão Fake Object
 *
 */
public class LocacaoDAOFake implements LocacaoDAO {

	@Override
	public void salvar(Locacao locacao) {

	}

	@Override
	public List<Locacao> obterLocacoesPendentes() {
		return null;
	}

}
