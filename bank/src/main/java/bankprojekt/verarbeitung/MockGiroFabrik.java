package bankprojekt.verarbeitung;

import org.mockito.Mockito;

public class MockGiroFabrik extends Kontofabrik{

	@Override
	public Konto kontoErstellen(Kunde k, long kontoNr) {
		return Mockito.mock(Girokonto.class);
	}

}
