import java.util.HashMap;
import java.util.Map;

public class Databaza {

	private Map<Integer, Zamestnanec> databaza;
	

	public Databaza(){
		databaza = new HashMap<>();
	}
	
	public void setAnalytik(String meno, String priezvisko, int rok, int ID)
	{
		databaza.put(ID, new Analytik(meno, priezvisko, rok, ID));
	}
	
	public void setBezpecak(String meno, String priezvisko, int rok, int ID)
	{
		databaza.put(ID, new Bezpecak(meno, priezvisko, rok, ID));
	}
	
	public boolean getInfoZamestnanec(int ID)
	{

		Zamestnanec z = databaza.get(ID);
		if(z==null) return false;
		System.out.println("Meno: " + z.getMeno() + "   Priezvisko: " + z.getPriezvisko() + "   Rok narodenia: " + z.getRok());
		System.out.println("ID:" + ID);
		
		if (z instanceof Analytik) {          
			System.out.println("Analytik");
		} 
		
		if (z instanceof Bezpecak) {         
			System.out.println("Bezpecak");
		} 
		 return true;

	}
	
	public Zamestnanec getZamestnanec(int ID)
	{
		return databaza.get(ID);
	}
	
	public void setSpolupraca(int ID1, int ID2, int s)
	{
		Zamestnanec z1 = databaza.get(ID1);
		z1.setSpolupraca(ID2,s);
		databaza.put(ID1, z1);
		
		Zamestnanec z2 = databaza.get(ID2);
		z2.setSpolupraca(ID1,s);
		databaza.put(ID2, z2);	
		
	}
	
	public void getSpolupraca(int ID1, int ID2)
	{
		Zamestnanec z1 = databaza.get(ID1);
		int s = z1.getHodnotaSpoluprace(ID2);
		System.out.println("Hodnota spoluprace medzi zamestanancom s ID:" + ID1 + " a s ID:" + ID2 + " je:" + s);	
	}
		
	
	public boolean klucCheck(int kluc) {
		
		if(databaza.containsKey(kluc)) return true;
		return false;
	
	}
	
	
	public void deleteZamestnanec(int ID) {
		databaza.remove(ID);
	}
	
	
	public void getStatistika(int ID)
	{
		Zamestnanec z = databaza.get(ID);
		z.statistika();
	}
	
	public void zrusenieSpoluprace(int ID) {
		for(int key : databaza.keySet()) {
			Zamestnanec z = databaza.get(key);
			z.zrusenieSpoluprace(ID);
		}
		
	}
	
	public void spustiDovednost(int ID) {
	    Zamestnanec z = databaza.get(ID);
	    if (z != null) {
	        z.dovednost(this.databaza);
	    }
	}
	
}
