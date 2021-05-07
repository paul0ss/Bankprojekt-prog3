import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import bankprojekt.verarbeitung.Bank;
import bankprojekt.verarbeitung.GesperrtException;
import bankprojekt.verarbeitung.Girokonto;
import bankprojekt.verarbeitung.GirokontoFabrik;
import bankprojekt.verarbeitung.Konto;
import bankprojekt.verarbeitung.Kontofabrik;
import bankprojekt.verarbeitung.Kunde;
import bankprojekt.verarbeitung.MockGiroFabrik;
import bankprojekt.verarbeitung.Sparbuch;
import bankprojekt.verarbeitung.Ueberweisungsfaehig;

class BankTest {
	
	Bank bank = new Bank(873847874);
	Kontofabrik fabrik;
	Kunde kunde1 = Mockito.mock(Kunde.class);
	Kunde kunde2 = Mockito.mock(Kunde.class);
	Kunde kunde3 = Mockito.mock(Kunde.class);
	Girokonto giro1 = Mockito.mock(Girokonto.class);
	Girokonto giro2 = Mockito.mock(Girokonto.class);
	Sparbuch spar1 = Mockito.mock(Sparbuch.class);
	long giro1Nr;
	long giro2Nr;
	long spar1Nr;
	
	@BeforeEach
	void setUpBeforeClass() {
		giro1Nr = bank.kontoHinzufuegen(giro1);
		giro2Nr = bank.kontoHinzufuegen(giro2);
		spar1Nr = bank.kontoHinzufuegen(spar1);
	}
	
	@Test
	void getBlzTest() {
		assertEquals(873847874, bank.getBankleitzahl());
	}
	
//	@Test
//	void girokontoErstellenTest() {
//		long kundeNr = bank.girokontoErstellen(kunde1);
//		assertEquals(1000000003, kundeNr);
//		kundeNr = bank.girokontoErstellen(kunde2);
//		assertEquals(1000000004, kundeNr);
//	}
//	
//	@Test
//	void sparbuchErstellenTest() {
//		long kundeNr1 = bank.sparbuchErstellen(kunde1);
//		assertEquals(1000000003, kundeNr1);
//		kundeNr1 = bank.sparbuchErstellen(kunde2);
//		assertEquals(1000000004, kundeNr1);
//	}
	
	@Test
	void geldAbhebenTest() throws GesperrtException {
		Mockito.when(giro1.abheben(ArgumentMatchers.anyDouble())).thenReturn(true);
		boolean result = bank.geldAbheben(giro1Nr, 200);
		Mockito.verify(giro1).abheben(ArgumentMatchers.anyDouble());
		assertTrue(result);
	}
	
	@Test
	void geldAbhebenExceptionTest() throws GesperrtException {
		Mockito.when(giro1.abheben(ArgumentMatchers.anyDouble())).thenThrow(GesperrtException.class);
		assertThrows(GesperrtException.class, () -> bank.geldAbheben(giro1Nr, 200));
		Mockito.verify(giro1).abheben(ArgumentMatchers.anyDouble());
	}
	
	@Test
	void geldEinzahlenTest() {
		bank.geldEinzahlen(giro2Nr, 300);
		Mockito.verify(giro2, Mockito.times(1)).einzahlen(ArgumentMatchers.anyDouble());
	}
	
	@Test
	void cloneTest() {
		Kunde k = new Kunde();
		long nr = bank.kontoErstellen(new GirokontoFabrik(), kunde1);
		Bank clone = bank.clone();
		bank.geldEinzahlen(nr, 200);
		assertEquals(200, bank.getKontostand(nr));
		assertEquals(0, clone.getKontostand(nr));
	}
	
	
	
	
	
//	@Test
//	void geldUeberweisenTest() throws GesperrtException {
//		Mockito.when(giro1.ueberweisungAbsenden(ArgumentMatchers.anyDouble(), ArgumentMatchers.anyString(), ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong(), ArgumentMatchers.anyString())).thenReturn(true);
//		boolean result = bank.geldUeberweisen(giro1Nr, giro2Nr, 200, "Test");
//		Mockito.verify(giro1).ueberweisungAbsenden(ArgumentMatchers.anyDouble(), ArgumentMatchers.anyString(), ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong(), ArgumentMatchers.anyString());
//		Mockito.verify(giro2).ueberweisungEmpfangen(ArgumentMatchers.anyDouble(), ArgumentMatchers.anyString(), ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong(), ArgumentMatchers.anyString());
//		assertTrue(result);
//		
//	}
}