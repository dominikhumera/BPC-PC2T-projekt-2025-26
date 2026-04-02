import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Spolupraca {
	
	private Map<Integer, Integer> spolupraca;
	
	public Spolupraca() {
		
		spolupraca = new HashMap<>();	
		
	}
	
	
	public void setHodnotaSpoluprace(int ID, int s)
	{
		spolupraca.put(ID, s);
	}
	
	public int getHodnotaSpoluprace(int ID)
	{
		return spolupraca.get(ID);
	}
	
	
	public void zrusenieSpoluprace(int ID) {
		spolupraca.remove(ID);
	}
	
	public void statistikaSpoluprace(){
		
		if(spolupraca.isEmpty()) {
			System.out.println("Neexistuje ziadna spolupraca");
			return;
		}
		int pocetSpolupraci = spolupraca.size();
		int sum = 0;
		float priemerSpolupraca = 0;
		int max = 0;
		int min = Integer.MAX_VALUE;
		List<Integer> maxSpolupraca = new ArrayList<>();
		List<Integer> minSpolupraca = new ArrayList<>();
		for (int kluc : spolupraca.keySet()) {
			sum += spolupraca.get(kluc);
			if( max < spolupraca.get(kluc)) {
				max = spolupraca.get(kluc);
				maxSpolupraca.clear();
				maxSpolupraca.add(kluc);
			}
			else if( max == spolupraca.get(kluc)) {
				maxSpolupraca.add(kluc);
			}
			if( min > spolupraca.get(kluc)) {
				min = spolupraca.get(kluc);
				minSpolupraca.clear();
				minSpolupraca.add(kluc);
			}
			else if( min == spolupraca.get(kluc)) {
				minSpolupraca.add(kluc);
			}
			
	    }
		priemerSpolupraca = (float)sum/pocetSpolupraci;
		
		System.out.println("Pocet spolupraci zamestnanca: " + pocetSpolupraci );
		System.out.println("Priemerna hodnota spoluprace zamestnanca: " + priemerSpolupraca );
		System.out.print("Maximalna hodnota spoluprace zamestnanca je: " + max);
		System.out.print(" ");
		System.out.print("Je to so zamestnancami s ID: ");
		for(int ID : maxSpolupraca) {
			System.out.print(ID+ "," );
		}
		System.out.println(" ");
		
		System.out.print("Minimalna hodnota spoluprace zamestnanca je: " + min);
		System.out.print(" ");
		System.out.print("Je to so zamestnancami s ID: ");
		for(int ID : minSpolupraca) {
			System.out.println(ID+ ",");
		}
	}
}
