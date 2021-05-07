package bankprojekt.verarbeitung;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

import Controller.ExceptionLog;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * stellt ein allgemeines Konto dar
 */
public abstract class Konto implements Comparable<Konto>, Serializable
{
	/** 
	 * der Kontoinhaber
	 */
	private Kunde inhaber;
	
	ExceptionLog log;

	/**
	 * die Kontonummer
	 */
	private final long nummer;

	/**
	 * der aktuelle Kontostand
	 */
	//private double kontostand;
	
	private ReadOnlyDoubleWrapper kontostand = new ReadOnlyDoubleWrapper();
	
	public final ReadOnlyDoubleProperty kontostandProperty() {
		return kontostand.getReadOnlyProperty();
	}
	
	/**
	 * die aktuelle Waehrung von dem Konto 
	 */
	private Waehrung waehrung;

	/**
	 * setzt den aktuellen Kontostand
	 * @param kontostand neuer Kontostand
	 */
	protected void setKontostand(double kontostand) {
		this.kontostand.set(kontostand);
		
		if(this.kontostand.get() < 0){
			imPlus.set(false);
		}
	}

	/**
	 * Wenn das Konto gesperrt ist (gesperrt = true), können keine Aktionen daran mehr vorgenommen werden,
	 * die zum Schaden des Kontoinhabers wären (abheben, Inhaberwechsel)
	 */
	private SimpleBooleanProperty gesperrt = new SimpleBooleanProperty();
	
	private ReadOnlyBooleanWrapper imPlus = new ReadOnlyBooleanWrapper();
	
	public ReadOnlyBooleanProperty getImPlusProperty() {
		return imPlus.getReadOnlyProperty();
	}
	
	public boolean getImPlus() {
		return imPlus.get();
	}
	
	public SimpleBooleanProperty getGesperrt() {
		return gesperrt;
	}
	
	private PropertyChangeSupport support = new PropertyChangeSupport(this);
	
	public void addListener(PropertyChangeListener pcl) {
		support.addPropertyChangeListener(pcl);
	}
	
	public void deleteListener(PropertyChangeListener pcl) {
		support.removePropertyChangeListener(pcl);
	}

	/**
	 * Setzt die beiden Eigenschaften kontoinhaber und kontonummer auf die angegebenen Werte,
	 * der anfängliche Kontostand wird auf 0 gesetzt.
	 *
	 * @param inhaber der Inhaber
	 * @param kontonummer die gewünschte Kontonummer
	 * @throws IllegalArgumentException wenn der Inhaber null
	 */
	public Konto(Kunde inhaber, long kontonummer) {
		if(inhaber == null)
			throw new IllegalArgumentException("Inhaber darf nicht null sein!");
		this.inhaber = inhaber;
		this.nummer = kontonummer;
		//this.kontostand = 0;
		kontostand.set(0);
		imPlus.set(true);
		this.waehrung = Waehrung.EUR;
		this.gesperrt.set(false);;
		this.addListener(new Beobachter());
		log = ExceptionLog.getExceptionLog();
	}
	
	/**
	 * setzt alle Eigenschaften des Kontos auf Standardwerte
	 */
	public Konto() {
		this(Kunde.MUSTERMANN, 1234567);
		this.addListener(new Beobachter());
		log = ExceptionLog.getExceptionLog();
	}

	/**
	 * liefert den Kontoinhaber zurück
	 * @return   der Inhaber
	 */
	public final Kunde getInhaber() {
		return this.inhaber;
	}
	
	/**
	 * setzt den Kontoinhaber
	 * @param kinh   neuer Kontoinhaber
	 * @throws GesperrtException wenn das Konto gesperrt ist
	 * @throws IllegalArgumentException wenn kinh null ist
	 */
	public final void setInhaber(Kunde kinh) throws GesperrtException{
		if (kinh == null)
			throw new IllegalArgumentException("Der Inhaber darf nicht null sein!");
		if(this.gesperrt.getValue())
			throw new GesperrtException(this.nummer);        
		this.inhaber = kinh;

	}
	
	/**
	 * liefert den aktuellen Kontostand
	 * @return   double
	 */
	public final double getKontostand() {
		return kontostand.get();
	}

	/**
	 * liefert die Kontonummer zurück
	 * @return   long
	 */
	public final long getNummer() {
		return nummer;
	}

	/**
	 * liefert zurück, ob das Konto gesperrt ist oder nicht
	 * @return true, wenn das Konto gesperrt ist
	 */
	public final boolean isGesperrt() {
		return gesperrt.getValue();
	}
	
	/**
	 * Erhöht den Kontostand um den eingezahlten Betrag.
	 *
	 * @param betrag double
	 * @throws IllegalArgumentException wenn der betrag negativ ist 
	 */
	public void einzahlen(double betrag) throws IllegalArgumentException{
		if (betrag < 0 || Double.isNaN(betrag)) {
			throw new IllegalArgumentException("Falscher Betrag");
		}
		double old = getKontostand();
		setKontostand(getKontostand() + betrag);
		support.firePropertyChange("Es wurde geld eingezahlt!", old, getKontostand());
		if(getKontostand() < 0) {
			imPlus.set(false);
		}else {
			imPlus.set(true);
		}
	}
	
	/**
	 * Sie zahlt den in der Währung w angegebenen Betrag ein.
	 * @param betrag
	 * @param w
	 */
	public void einzahlen(double betrag, Waehrung w) {
		double endbetrag = waehrung.euroInWaehrungUmrechnen(w.waehrungInEuroUmrechnen(betrag));
		einzahlen(endbetrag);
	}
	
	/**
	 * Gibt eine Zeichenkettendarstellung der Kontodaten zurück.
	 */
	@Override
	public String toString() {
		String ausgabe;
		ausgabe = "Kontonummer: " + this.getKontonummerFormatiert()
				+ System.getProperty("line.separator");
		ausgabe += "Inhaber: " + this.inhaber;
		ausgabe += "Aktueller Kontostand: " + this.kontostand + " Euro ";
		ausgabe += this.getGesperrtText() + System.getProperty("line.separator");
		return ausgabe;
	}
	
	/**
	 * Sie liefert die Währung zurück, in der das Konto aktuell geführt wird.
	 * @return
	 */
	public Waehrung getAktuelleWaehrung() {
		return this.waehrung;
	}
	
	public abstract void kontoUpdate (Waehrung neu);
	
	/**
	 * Sie wechselt die Währung, in der das Konto aktuell geführt wird
	 * @param neu
	 */
	public final void waehrungswechsel(Waehrung neu) {
		this.setKontostand(neu.euroInWaehrungUmrechnen(this.getAktuelleWaehrung().waehrungInEuroUmrechnen(this.getKontostand())));
		kontoUpdate(neu);
		Waehrung old = this.waehrung;
		this.waehrung = neu;
		support.firePropertyChange("Waehrung wurde geändert!", old, neu);
	}

	/**
	 * Mit dieser Methode wird der geforderte Betrag vom Konto abgehoben, wenn es nicht gesperrt ist.
	 *
	 * @param betrag double
	 * @throws GesperrtException wenn das Konto gesperrt ist
	 * @throws IllegalArgumentException wenn der betrag negativ ist 
	 * @return true, wenn die Abhebung geklappt hat, 
	 * 		   false, wenn sie abgelehnt wurde
	 */
	public boolean abheben(double betrag) throws GesperrtException{
		if (betrag < 0 || Double.isNaN(betrag)) {
			throw new IllegalArgumentException("Betrag ungültig");
		}
		if(this.isGesperrt()) {
			throw new GesperrtException(this.getNummer());
		}
		bereitsAbgehobenChecken();
		double old = getKontostand();
		boolean result = geldAbziehen(betrag);
		if(result) {
			support.firePropertyChange("Das Geld wurde abgehoben", old, getKontostand());
			if(getKontostand() < 0) {
				imPlus.set(false);
			}else {
				imPlus.set(true);
			}
			return true;
		}else {
			return false;
		}
		
	}
	
	protected abstract boolean geldAbziehen(double betrag);

	protected void bereitsAbgehobenChecken() {
		
	}

	/**
	 * Sie hebt den gewünschten in der Währung w angegebenen Betrag ab.
	 * @param betrag
	 * @param w
	 * @return
	 * @throws GesperrtException
	 */
	public boolean abheben(double betrag, Waehrung w) throws GesperrtException {
		return this.abheben(this.getAktuelleWaehrung().euroInWaehrungUmrechnen(w.waehrungInEuroUmrechnen(betrag)));
	}
	
	/**
	 * sperrt das Konto, Aktionen zum Schaden des Benutzers sind nicht mehr möglich.
	 */
	public final void sperren() {
		this.gesperrt.set(true);
		support.firePropertyChange("Das Konto wurde gesperrt!", false, true);
	}

	/**
	 * entsperrt das Konto, alle Kontoaktionen sind wieder möglich.
	 */
	public final void entsperren() {
		this.gesperrt.set(false);
		support.firePropertyChange("Das Konto wurde entsperrt!", true, false);
	}
	
	
	/**
	 * liefert eine String-Ausgabe, wenn das Konto gesperrt ist
	 * @return "GESPERRT", wenn das Konto gesperrt ist, ansonsten ""
	 */
	public final String getGesperrtText()
	{
		if (this.gesperrt.getValue())
		{
			return "GESPERRT";
		}
		else
		{
			return "";
		}
	}
	
	/**
	 * liefert die ordentlich formatierte Kontonummer
	 * @return auf 10 Stellen formatierte Kontonummer
	 */
	public String getKontonummerFormatiert()
	{
		return String.format("%10d", this.nummer);
	}
	
	/**
	 * liefert den ordentlich formatierten Kontostand
	 * @return formatierter Kontostand mit 2 Nachkommastellen und Währungssymbol 
	 */
	public String getKontostandFormatiert()
	{
		return String.format("%10.2f " + waehrung.toString() , this.getKontostand());
	}
	
	/**
	 * Vergleich von this mit other; Zwei Konten gelten als gleich,
	 * wen sie die gleiche Kontonummer haben
	 * @param other das Vergleichskonto
	 * @return true, wenn beide Konten die gleiche Nummer haben
	 */
	@Override
	public boolean equals(Object other)
	{
		if(this == other)
			return true;
		if(other == null)
			return false;
		if(this.getClass() != other.getClass())
			return false;
		if(this.nummer == ((Konto)other).nummer)
			return true;
		else
			return false;
	}
	
	@Override
	public int hashCode()
	{
		return 31 + (int) (this.nummer ^ (this.nummer >>> 32));
	}

	@Override
	public int compareTo(Konto other)
	{
		if(other.getNummer() > this.getNummer())
			return -1;
		if(other.getNummer() < this.getNummer())
			return 1;
		return 0;
	}
	
	/**
	 * gibt this auf der Konsole aus
	 * 
	 * NICHT AUFRUFEN: Widerspricht der Regel: Trenne
	 * Verarbeitung von der Ein-/Ausgabe!
	 */
	public void ausgeben()
	{
		System.out.println(this.toString());
	}
}
