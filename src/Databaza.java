import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Databaza {

	private static final String SUBOR_DATABAZY = "databaza.txt";

	private Map<Integer, Zamestnanec> databaza;
	

	public Databaza(){
		databaza = new HashMap<>();
	}

	public boolean nacitat() {
		File subor = new File(SUBOR_DATABAZY);
		if (!subor.exists()) {
			return false;
		}

		databaza.clear();
		List<String[]> spolupraceNaNacitanie = new ArrayList<>();
		try (BufferedReader citac = new BufferedReader(new FileReader(subor))) {
			String riadok;
			while ((riadok = citac.readLine()) != null) {
				if (riadok.isBlank()) {
					continue;
				}

				String[] casti = riadok.split("\\|", -1);
				if (casti[0].equals("EMP") && casti.length >= 6) {
					int id = Integer.parseInt(casti[1]);
					String typ = casti[2];
					String meno = casti[3];
					String priezvisko = casti[4];
					int rok = Integer.parseInt(casti[5]);

					if (typ.equals("Analytik")) {
						databaza.put(id, new Analytik(meno, priezvisko, rok, id));
					} else if (typ.equals("Bezpecak")) {
						databaza.put(id, new Bezpecak(meno, priezvisko, rok, id));
					}
				} else if (casti[0].equals("COL") && casti.length >= 4) {
					spolupraceNaNacitanie.add(casti);
				}
			}

			for (String[] casti : spolupraceNaNacitanie) {
				int id1 = Integer.parseInt(casti[1]);
				int id2 = Integer.parseInt(casti[2]);
				int hodnota = Integer.parseInt(casti[3]);
				Zamestnanec z1 = databaza.get(id1);
				Zamestnanec z2 = databaza.get(id2);
				if (z1 != null && z2 != null) {
					z1.setSpolupraca(id2, hodnota);
					z2.setSpolupraca(id1, hodnota);
				}
			}
			return true;
		} catch (IOException | NumberFormatException e) {
			databaza.clear();
			return false;
		}
	}

	public boolean ulozit() {
		File subor = new File(SUBOR_DATABAZY);
		try {
			if (!subor.exists()) {
				subor.createNewFile();
			}

			try (BufferedWriter zapisovac = new BufferedWriter(new FileWriter(subor, false))) {
				for (Zamestnanec z : databaza.values()) {
					String typ = (z instanceof Analytik) ? "Analytik" : "Bezpecak";
					zapisovac.write("EMP|" + z.getID() + "|" + typ + "|" + z.getMeno() + "|" + z.getPriezvisko() + "|" + z.getRok());
					zapisovac.newLine();
				}

				for (Zamestnanec z : databaza.values()) {
					for (Map.Entry<Integer, Integer> zaznam : z.getDatabazaSpoluprace().entrySet()) {
						if (z.getID() < zaznam.getKey()) {
							zapisovac.write("COL|" + z.getID() + "|" + zaznam.getKey() + "|" + zaznam.getValue());
							zapisovac.newLine();
						}
					}
				}
			}

			return true;
		} catch (IOException e) {
			return false;
		}
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
	
	public void abecedneZoradenie() {
		
		List<String> zoradeneMena = new ArrayList<>();
		Collections.sort(zoradeneMena);
		for (Zamestnanec z : databaza.values()) {
	        zoradeneMena.add(z.getPriezvisko() + " " + z.getMeno());
	    }
	    
	    Collections.sort(zoradeneMena);
	    
	    for (String meno : zoradeneMena) {
	        System.out.println(meno);
	    }
		
	}
	
}
