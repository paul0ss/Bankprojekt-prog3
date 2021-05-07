package bankprojekt.verarbeitung;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import Controller.ExceptionLog;

public class Bank implements Cloneable, Serializable{

	HashMap<Long, Konto> kontenListe = new HashMap<>();
	
	ExceptionLog log;

	long neueKnr = 1000000000;

	long blz;

	/**
	 * erstellt eine Bank mit der angegebenen Bankleitzahl
	 * 
	 * @param bankleitzahl
	 */
	public Bank(long bankleitzahl) {
		blz = bankleitzahl;
		log = ExceptionLog.getExceptionLog();
	}

	/**
	 * liefert die Bankleitzahl zurück
	 * 
	 * @return
	 */
	public long getBankleitzahl() {
		return blz;
	}

//	public long girokontoErstellen(Kunde inhaber) {
//		Konto konto = new Girokonto(inhaber, neueKnr, 500);
//		kontenListe.put(neueKnr, konto);
//		return neueKnr++;
//	}
//
//	public long sparbuchErstellen(Kunde inhaber) {
//		Konto konto = new Sparbuch(inhaber, neueKnr);
//		kontenListe.put(neueKnr, konto);
//		return neueKnr++;
//	}
	
	public long kontoErstellen(Kontofabrik fabrik, Kunde inhaber) {
		Konto k = fabrik.kontoErstellen(inhaber, neueKnr);
		kontenListe.put(neueKnr, k);
		return neueKnr++;
	}
	
	public long kontoHinzufuegen(Konto k) {
		kontenListe.put(neueKnr, k);
		return neueKnr++;
	}

	public List<Long> getAlleKontonummern() {
		return new ArrayList<Long>(kontenListe.keySet());
	}

	public boolean geldAbheben(long von, double betrag) throws GesperrtException {
		if (kontenListe.containsKey(von) == false) {
			return false;
		}
		return kontenListe.get(von).abheben(betrag);
	}

	public void geldEinzahlen(long auf, double betrag) {
		if (kontenListe.containsKey(auf) == false) {
			System.out.println("Das Konto ist nicht vorhanden");
		} else {
			kontenListe.get(auf).einzahlen(betrag);
		}
	}

	public boolean kontoLoeschen(long nummer) {
		if (kontenListe.containsKey(nummer) == false) {
			return false;
		} else {
			kontenListe.remove(nummer);
			return true;
		}
	}

	public double getKontostand(long nummer) {
		return kontenListe.get(nummer).getKontostand();
	}

	public boolean geldUeberweisen(long vonKontonr, long nachKontonr, double betrag, String verwendungszweck) {
		if ((kontenListe.get(vonKontonr) instanceof Ueberweisungsfaehig == false)
				&& (kontenListe.get(nachKontonr) instanceof Ueberweisungsfaehig == false)) {
			return false;
		} else if ((kontenListe.containsKey(vonKontonr) == false) && (kontenListe.containsKey(nachKontonr) == false)) {
			return false;
		} else if ((kontenListe.get(vonKontonr).isGesperrt() == true)
				&& (kontenListe.get(nachKontonr).isGesperrt() == true)) {
			return false;
		} else if (kontenListe.get(vonKontonr) instanceof Girokonto) {
			Girokonto giro = (Girokonto) kontenListe.get(vonKontonr);
			if ((giro.getKontostand() + giro.getDispo()) < betrag) {
				return false;
			}
		}
		boolean abgesendet = false;
		Ueberweisungsfaehig u1 = (Ueberweisungsfaehig) kontenListe.get(vonKontonr);
		Ueberweisungsfaehig u2 = (Ueberweisungsfaehig) kontenListe.get(nachKontonr);
		try {
			abgesendet = u1.ueberweisungAbsenden(betrag, kontenListe.get(nachKontonr).getInhaber().getName(),
					nachKontonr, blz, "einfach so");
		} catch (GesperrtException e) {
			log.logToFile(e);
			return false;
		}
		if (abgesendet == true) {
			u2.ueberweisungEmpfangen(betrag, kontenListe.get(vonKontonr).getInhaber().getName(), vonKontonr, blz,
					"einfach so");
		}
		return true;
	}
	
	
//	public long mockEinfuegen(Konto k) {
//		kontenListe.put(neueKnr, k);
//		return neueKnr++;
//	}
	
	public void pleitegeierSperren() {
		kontenListe.values().stream().filter(k -> {
			if(k.getKontostand() < 0)
				return true;
			return false;
		}).forEach(k -> k.sperren());
	}
	
	public List<Kunde> getKundenMitVollemKonto(double minimum){
		return kontenListe.values().stream().filter((Konto k) -> {if(k.getKontostand() > minimum)
					return true;
				return false;}).map(k -> k.getInhaber()).distinct().collect(Collectors.toList());
		
	}
	
	public String getKundenGeburtstage() {
		return kontenListe.values().stream().map(k-> k.getInhaber()).distinct().sorted((k1, k2) -> k1.getGeburtstag().compareTo(k2.getGeburtstag())).map(k -> k.getName() + " " + k.getGeburtstag()).reduce("", (a, b) -> a + b.toString() + "; ");
	}
	
	public List<Long> getKontonummernLuecken(){
		List<Long> allNumbers = new ArrayList<>();
		for(long i = 1000000000; i < neueKnr; i++) {
			allNumbers.add(i);
		}
		return allNumbers.stream().filter(x -> !kontenListe.containsKey(x)).collect(Collectors.toList());
		}
	
	@Override
	public Bank clone() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		Bank b = null;
		try {
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.writeObject(this);
			byte[] arr = os.toByteArray();
			ByteArrayInputStream is = new ByteArrayInputStream(arr);
			ObjectInputStream ois = new ObjectInputStream(is);
			b = (Bank)ois.readObject();
			oos.close();
			ois.close();
		} catch (IOException | ClassNotFoundException e) {
			log.logToFile(e);
			e.printStackTrace();
		}
		return b;
	}
	}


