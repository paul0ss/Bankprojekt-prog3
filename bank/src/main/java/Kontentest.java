import java.time.LocalDate;
import java.util.List;

import bankprojekt.verarbeitung.Bank;
import bankprojekt.verarbeitung.GesperrtException;
import bankprojekt.verarbeitung.Girokonto;
import bankprojekt.verarbeitung.GirokontoFabrik;
import bankprojekt.verarbeitung.Konto;
import bankprojekt.verarbeitung.Kunde;
import bankprojekt.verarbeitung.Sparbuch;
import bankprojekt.verarbeitung.Waehrung;
import bankprojelt.oberflaeche.*;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Testprogramm für Konten
 * @author Doro
 *
 */
public class Kontentest extends Application{

	/**
	 * Testprogramm für Konten
	 * @param args wird nicht benutzt
	 */
	public static void main(String[] args) {
/*		Kunde ich = new Kunde("Dorothea", "Hubrich", "zuhause", LocalDate.parse("1976-07-13"));

		Girokonto meinGiro = new Girokonto(ich, 1234, 1000.0);
		meinGiro.einzahlen(50);
		System.out.println(meinGiro);
		
		Sparbuch meinSpar = new Sparbuch(ich, 9876);
		meinSpar.einzahlen(50);
		try
		{
			boolean hatGeklappt = meinSpar.abheben(70);
			System.out.println("Abhebung hat geklappt: " + hatGeklappt);
			System.out.println(meinSpar);
		}
		catch (GesperrtException e)
		{
			System.out.println("Zugriff auf gesperrtes Konto - Polizei rufen!");
		}
		
		System.out.println("Und das in Konto gespeicherte Girokonto:");
		Object meins = new Girokonto();
		System.out.println(meins.toString());
		System.out.println("�ber ausgeben-Methode aufgerufen:");
		Konto deins = new Girokonto();
		deins.ausgeben();
		
		int a = 100;
		int b = a;
		a += 50;
		System.out.println(b);
		
		Konto k1 = new Girokonto();
		Konto k2 = k1;
		k1.einzahlen(50);
		System.out.println(k2.getKontostand());
		
		Kunde inhaber = k1.getInhaber();
		inhaber.setAdresse("ganz woanders hin!");
		System.out.println(k1.getInhaber());
*/		
//		Kontoart art;
//		art = Kontoart.GIROKONTO;
//		System.out.println(art.name() + " " 
//						+ art.ordinal());
//		System.out.println(art.getWerbebotschaft());
//		
//		System.out.println("�ber welche Kontoart "
//				+ "m�chten Sie etwas wissen?");
//		String eingabe = "SPARBUCH"; //abfragen
//		art = Kontoart.valueOf(eingabe);
//		System.out.println(art.name() + " " 
//				+ art.ordinal());
//		System.out.println(art.getWerbebotschaft());
//		
//		Kontoart[] alle = Kontoart.values();
//		System.out.println("Unser Prospekt:");
//		for(int i=0; i<alle.length; i++)
//			System.out.println(alle[i]);
		
//		Kunde ich = new Kunde("Dorothea", "Hubrich", "zuhause", LocalDate.parse("1976-07-13"));
//		Kunde trol = new Kunde("Andriy", "Hu4242h", "zuhause", LocalDate.parse("1997-08-22"));
//		Kunde bla = new Kunde("Bla", "Lala", "zuhause", LocalDate.parse("1986-11-01"));
//		Kunde ivan = new Kunde("ivan", "Hpashkevich", "zuhause", LocalDate.parse("2000-12-13"));
//		
//		Bank b = new Bank(4827482);
//		
//		long nummer1 = b.kontoErstellen(new GirokontoFabrik(), ich);
//		b.geldEinzahlen(nummer1, 200);
		launch(args);

}

	@Override
	public void start(Stage primaryStage) throws Exception {
//		Parent root = new KontoOberflaeche();
//		Scene scene = new Scene(root, 600, 300);
//		primaryStage.setScene(scene);
//		primaryStage.setTitle("Bank");
//		primaryStage.show();
	}
}
