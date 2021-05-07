package bankprojekt.verarbeitung;

import org.mockito.Mockito;

public class MockSparFabrik extends Kontofabrik{

	@Override
	public Konto kontoErstellen(Kunde k, long kontoNr) {
		return Mockito.mock(Sparbuch.class);
	}

}
