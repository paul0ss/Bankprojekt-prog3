package bankprojekt.verarbeitung;

public class SparbuchFabrik extends Kontofabrik{

	@Override
	public Konto kontoErstellen(Kunde k, long kontoNr) {
		return new Sparbuch(k, kontoNr);
	}

}
