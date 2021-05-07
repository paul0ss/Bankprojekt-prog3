package Controller;


import java.time.LocalDate;

import bankprojekt.verarbeitung.GesperrtException;
import bankprojekt.verarbeitung.Girokonto;
import bankprojekt.verarbeitung.Konto;
import bankprojekt.verarbeitung.Kunde;
import bankprojelt.oberflaeche.KontoOberflaeche;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class KontoController extends Application{
	
private Girokonto konto;
private ExceptionLog log;
Kunde ich;
@FXML private Label kontonummerLabel;
@FXML private Label label5;
@FXML private Label kontostandLabel;
@FXML private Button einzahlenButton;
@FXML private Button abhebenButton;
@FXML private TextField betragLabel;
@FXML private TextArea adresseField;
@FXML private CheckBox gesperrtCheck;

public static void main(String[] args) {
	launch(args);
}

	@Override
	public void start(Stage primaryStage) throws Exception {
		log = ExceptionLog.getExceptionLog();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../bank.fxml"));
		loader.setController(this);
		Parent root = loader.load();
		Scene scene = new Scene(root, 600, 400);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	@FXML public void initialize() {
		ich = new Kunde("Andriy", "Lyubar", "Regensburgerstraße 25, 10767", LocalDate.parse("1997-08-22"));
		konto = new Girokonto(ich, 82472893, 500);
		label5.setText("Willkomen "+ich.getVorname() + " "+ich.getNachname() + "!");
		kontonummerLabel.setText(konto.getKontonummerFormatiert());
		kontostandLabel.textProperty().bind(konto.kontostandProperty().asString());
		if(konto.getImPlus() == false) {
			kontostandLabel.setTextFill(Color.RED);
		}else {
			kontostandLabel.setTextFill(Color.GREEN);
		}
		kontostandLabel.textProperty().addListener(e -> {
			if(konto.getImPlusProperty().get()) {
				kontostandLabel.setTextFill(Color.GREEN);;
			}else{
				kontostandLabel.setTextFill(Color.RED);;
			}
		});
		konto.einzahlen(200);
		einzahlenButton.setOnMouseClicked(e -> {
			this.einzahlen(Double.parseDouble(betragLabel.getText()));
		});
		abhebenButton.setOnMouseClicked(e -> {
			this.abheben(Double.parseDouble(betragLabel.getText()));
		});
		adresseField.textProperty().bind(konto.getInhaber().getAdresseProperty());
		gesperrtCheck.textProperty().bind(konto.getGesperrt().asString());
		if(konto.getGesperrt().get()) {
			gesperrtCheck.setSelected(true);
		}else {
			gesperrtCheck.setSelected(false);
		}
		gesperrtCheck.selectedProperty().addListener(e -> {
			if(gesperrtCheck.isSelected()) {
				konto.sperren();
			}else {
				konto.entsperren();
			}
		});
	}
	
	public void einzahlen(double betrag) {
		konto.einzahlen(betrag);
	}

	public void abheben(double parseDouble) {
		try {
			konto.abheben(parseDouble);
		} catch (GesperrtException e) {
			log.logToFile(e);
		}
		
	}

}
