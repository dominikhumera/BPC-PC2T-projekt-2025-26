import java.util.Map;
import java.util.Scanner;

public class Bezpecak extends Zamestnanec {
	
	public Bezpecak(String meno, String priezvisko, int rok, int ID) {
		super(meno, priezvisko, rok, ID);
	}

	@Override
	void dovednost(Map<Integer, Zamestnanec> vsetci) {
		int ID;
		Scanner sc=new Scanner(System.in);
		System.out.println("Zadajte ID zamestnance, ktoreho rizikovost chcete zistit:");
		try {
          ID = sc.nextInt();
        } catch (Exception e) {
            System.out.println("Neplatny vstup!");
            return;
        }
		
		if (!vsetci.containsKey(ID)) {
            System.out.println("Zamestnanec s tymto ID neexistuje.");
            return;
        }
		
		Zamestnanec z = vsetci.get(ID);
		Map<Integer, Integer> spoluprace = z.getDatabazaSpoluprace();
		
		int pocetSpoluprac = 0;
		int sum = 0;
		for(int kluc : spoluprace.keySet()) {
			sum += spoluprace.get(kluc);
			pocetSpoluprac++;
		}
		
		if(pocetSpoluprac == 0) {
			System.out.println("Pracovnik nema ziadnu spolupracu, riziko sa neda zistit.");
			return;
		}
		
		double priemer = (double)sum/pocetSpoluprac;
		
		double riziko = (Math.pow(pocetSpoluprac, 1.2) * (11 - priemer)) / 10;
		
		System.out.println("Rizikove skore zamestnanca s ID: " + ID + " je: " + riziko);
		
	}
	
	
}
