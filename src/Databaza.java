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
	
	public boolean getZamestnanec(int ID)
	{
		Zamestnanec z = databaza.get(ID);
		if(z==null) return false;
		System.out.println("Meno: " + z.getMeno() + "   Priezvisko: " + z.getPriezvisko() + "   Rok narodenia: " + z.getRok());

		if (z instanceof Analytik) {          
			System.out.println("Analytik");
		} 
		
		if (z instanceof Bezpecak) {         
			System.out.println("Bezpecak");
		} 
		 return true;

	}
}
