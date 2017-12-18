package br.ce.wcaquino.matchers;

import java.util.Calendar;

public class MatchersProprios {
	
	public static DiaSemanaMatcher caiEm(Integer diaSemana) {
		return new DiaSemanaMatcher(diaSemana);
	}
	
	public static DiaSemanaMatcher caiNumaSegunda() {
		return caiEm(Calendar.MONDAY); 
	}
	
	public static MesmaDataMatcher ehHojeComDiferencaDias(Integer qtdDias) {
		return new MesmaDataMatcher(qtdDias);
	}
	
	public static MesmaDataMatcher ehHoje() {
		return ehHojeComDiferencaDias(0);
	}

}