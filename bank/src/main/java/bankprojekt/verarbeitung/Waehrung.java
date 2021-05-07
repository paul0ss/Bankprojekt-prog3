package bankprojekt.verarbeitung;


public enum Waehrung {
	EUR(1.0),
	BGN(1.95583),
	LTL(3.4528),
	KM(1.95583);
	
	double kurs;
	
	private Waehrung(double x) {
		kurs = x;
	}
	
	public double euroInWaehrungUmrechnen(double betrag) {
		return betrag * this.kurs;
	}
	
	public double waehrungInEuroUmrechnen(double betrag) {
		return betrag / this.kurs;
	}
	
	public double getKurs() {
		return this.kurs;
	}
	
}
