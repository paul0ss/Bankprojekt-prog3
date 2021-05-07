

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import bankprojekt.verarbeitung.GesperrtException;
import bankprojekt.verarbeitung.Girokonto;
import bankprojekt.verarbeitung.Konto;
import bankprojekt.verarbeitung.Kunde;
import bankprojekt.verarbeitung.Waehrung;

public class KontoTest {
	
	Girokonto giro = null;
	Girokonto giro2 = null;
	
	@BeforeEach
	public void beforeEachTest() {
		giro = new Girokonto();
		giro2 = new Girokonto(new Kunde("Andriy", "Lyubar", "Spichernstraße 7", LocalDate.now()), 573847228, 500);
	}
	
	@Test
	public void konstruktorTest()
	{
		Konto k = new Girokonto();
		assertEquals(0.0, k.getKontostand(), "Falscher Kontostand");		
	}
	
	@Test
	public void einzahlenTest() {
		giro.einzahlen(200);
		assertEquals(200, giro.getKontostand());
	}
	
	@Test
	@DisplayName("fiofkriokf")
	public void einzahelnExceptionTest() {
		Exception exception = assertThrows(IllegalArgumentException.class, () -> giro.einzahlen(-200.0));
	}
	
	@Test
	public void einzahlenWaehrungTest() {
		giro.einzahlen(300, Waehrung.BGN);
		assertEquals(153.39, Math.round(giro.getKontostand() * 100.0) / 100.0);
	}
	
	@Test
	public void ueberweisungDurchfuehren() throws GesperrtException {
		giro.einzahlen(300);
		giro.ueberweisungAbsenden(200, "Andriy Lyubar", 573847228, 5345345, "Test");
		giro2.ueberweisungEmpfangen(200, "Mustermann", 99887766, 5345323, "Test");
		assertEquals(100, giro.getKontostand());
		assertEquals(200, giro2.getKontostand());
	}
	
	@Test
	public void ueberweisungExceptionTest() throws GesperrtException {
		giro.einzahlen(300);
		giro.sperren();
		Assertions.assertThrows(GesperrtException.class, () -> giro.ueberweisungAbsenden(200, "Andriy Lyubar", 573847228, 5345345, "Test"));
	}
	
	@Test
	public void abhebenTest() throws GesperrtException {
		giro.einzahlen(200);
		assertTrue(giro.abheben(100));
		assertEquals(100, giro.getKontostand());
	}
	
	@Test
	public void abhebenException() throws GesperrtException {
		giro.einzahlen(300);
		giro.sperren();
		Assertions.assertThrows(GesperrtException.class, () -> giro.abheben(200));
	}
	
	@Test
	public void abhebenException2() throws GesperrtException {
		giro.einzahlen(300);
		Assertions.assertThrows(IllegalArgumentException.class, () -> giro.abheben(-200));
	}
}
