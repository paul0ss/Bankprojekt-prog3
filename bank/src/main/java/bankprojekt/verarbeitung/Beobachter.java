package bankprojekt.verarbeitung;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class Beobachter implements PropertyChangeListener{

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		System.out.println(evt.getPropertyName()+evt.getNewValue());
		
	}

}
