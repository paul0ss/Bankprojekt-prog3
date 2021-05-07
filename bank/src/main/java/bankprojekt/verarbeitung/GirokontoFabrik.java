package bankprojekt.verarbeitung;

public class GirokontoFabrik extends Kontofabrik{

	@Override
	public Konto kontoErstellen(Kunde k, long kontoNr) {
		// TODO Auto-generated method stub
		return new Girokonto(k, kontoNr, 500);
	}

}
