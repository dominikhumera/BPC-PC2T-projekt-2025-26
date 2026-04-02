import java.util.HashMap;
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
	
	public void delete(int ID) 
	{
		spolupraca.remove(ID);	
	}
	
	public void zrusenieSpoluprace(Databaza d, int ID) {
		for (int kluc : spolupraca.keySet()) {
			Zamestnanec z = d.getKluc(kluc);
			z.delete(ID);
			d.zmenaZamestnanec(ID,z);
	    }
	}
	
}
