import java.util.Map;
import java.util.Set;

public class Analytik extends Zamestnanec {
	

	public Analytik(String meno, String priezvisko, int rok, int ID) {
		super(meno, priezvisko, rok, ID);
	}

	@Override
	void dovednost(Map<Integer, Zamestnanec> vsetci) {
		Zamestnanec najviacSpolupracovnikov = null;
		int pocetSpolocnych = 0;
		int mojeID = this.getID();
		Set<Integer> spolupracovnici = this.getDatabazaSpoluprace().keySet();
		
		for (Zamestnanec ostatni : vsetci.values()) {
			
			if (ostatni.getID() == mojeID) continue; // vynechanie zistovaneho zamestnanca
			
			Set<Integer> spolupracovniciTest = ostatni.getDatabazaSpoluprace().keySet();
			
			int counter = 0;
			
			for (int ID : spolupracovnici) {
                if (spolupracovniciTest.contains(ID)) {
                    counter++;
                }
			}    
			if (counter > pocetSpolocnych) {
				pocetSpolocnych = counter;
				najviacSpolupracovnikov  = ostatni;
            }
		}
		
		if (najviacSpolupracovnikov != null && pocetSpolocnych > 0) {
            System.out.println("Analytik " + getMeno() + " má najviac spoločných spolupracovníkov s: " 
                + najviacSpolupracovnikov.getMeno() + " " + najviacSpolupracovnikov.getPriezvisko() 
                + " (Počet spoločných: " + pocetSpolocnych + ")");
        } else {
            System.out.println("Analytik nemá žiadnych spoločných spolupracovníkov s nikým iným.");
        }
	}
}
