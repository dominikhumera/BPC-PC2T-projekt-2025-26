import java.util.Scanner;

public class main {

	public static void main(String[] args) {
		Scanner sc=new Scanner(System.in);
		int volba = 0;
		boolean koniec=false;
		String meno;
		String priezvisko;
		String skupina;
		int ID = 0;
		int rok;
		Databaza databaza = new Databaza();
		
		while(!koniec) {
			
			System.out.println("1. Pridaj zamestnanca");
			System.out.println("2. Vypis zamestnanca podla ID");
			System.out.println("3. KONIEC");
			volba=intCheck(sc);
			
			switch(volba) {
			case 3: 
				
				koniec = true;
				break;
			
			case 1:
				
				System.out.println("Zadajte meno zamestnanca");
				meno = sc.next();
				System.out.println("Zadajte priezvisko zamestnanca");
				priezvisko = sc.next();
				System.out.println("Zadajte rok narodenia zamestnanca");
				rok = intCheck(sc);
				ID++;
				System.out.println("Vyberte skupinu pre zamestanca Analytik/Bezpecak");
				skupina = sc.next();
				if(skupina.equals("Bezpecak")) {
					databaza.setBezpecak(meno, priezvisko, rok, ID);
				}
				if(skupina.equals("Analytik")) {
					databaza.setAnalytik(meno, priezvisko, rok, ID);
				}
				break;
				
			case 2:
				
				System.out.println("Zadajte ID zamestnanca");
				int currentID = intCheck(sc);
				databaza.getZamestnanec(currentID);
				break;
			}
			
			
			
			
			
			
			
			
			
		}

	}

	public static int intCheck(Scanner sc) 
	{
		int cislo = 0;
		try
		{
			cislo = sc.nextInt();
		}
		catch(Exception e)
		{
			System.out.println("Nastala vyjimka typu "+e.toString());
			System.out.println("zadejte prosim cele cislo ");
			sc.nextLine();
			cislo = intCheck(sc);
		}
		return cislo;
	}
}

