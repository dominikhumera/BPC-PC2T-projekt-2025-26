import java.util.ArrayList;
import java.util.List;
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
		int ID1;
		int ID2;
		int rok;
		int spolupraca;
		Databaza databaza = new Databaza();
		
		while(!koniec) {
			
			System.out.println("1. Pridaj zamestnanca");
			System.out.println("2. Vypis zamestnanca podla ID");
			System.out.println("3. Nastavenie spoluprace");
			System.out.println("4. Vypis spoluprace");
			System.out.println("5. Vymazanie zamestnanca");
			System.out.println("6. Statistika spoluprace pracovnika");
			System.out.println("7. Spustit dovednost pracovnika");
			System.out.println("8. KONIEC");
			volba = intCheck(sc);
			
			switch(volba) {
			
			case 8: 
				
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
				while(true) {
					skupina = sc.next();
					if(skupina.equals("Bezpecak")) {
						databaza.setBezpecak(meno, priezvisko, rok, ID);
						break;
					}
					else if(skupina.equals("Analytik")) {
						databaza.setAnalytik(meno, priezvisko, rok, ID);
						break;
					}
					else {
						System.out.println("Zadajte Analytik alebo Bezpecak!!!");
					}
				}
				break;
				
			case 2:
				
				System.out.println("Zadajte ID zamestnanca");
				ID1 = IDcheck(sc, databaza);
				if (ID1 == -1) break;
				databaza.getInfoZamestnanec(ID1);
				break;
				
			case 3:
				
				System.out.println("Zadajte ID prveho zamestnance pre nastavenie spoluprace:");
				ID1 = IDcheck(sc, databaza);
				if (ID1 == -1) break;
				System.out.println("Zadajte ID druheho zamestnance pre nastavenie spoluprace:");
				ID2 = IDcheck(sc, databaza);
				if (ID2 == -1) break;
				if(ID1==ID2) {
					System.out.println("Musia byt rozne ID");
					break;
				}
				System.out.println("Zadajte hodnotu spoluprace:");
				spolupraca = intCheck(sc);
				databaza.setSpolupraca(ID1, ID2, spolupraca);
				break;
				
			case 4:
				
				System.out.println("Zadajte ID prveho zamestnanca:");
				ID1 = IDcheck(sc, databaza);
				if (ID1 == -1) break;
				System.out.println("Zadajte ID druheho zamestnanca:");
				ID2 = IDcheck(sc, databaza);
				if (ID2 == -1) break;
				if(ID1==ID2) {
					System.out.println("Musia byt rozne ID");
					break;
				}
				databaza.getSpolupraca(ID1, ID2);
				break;
				
			case 5:
				
				System.out.println("Zadajte ID zamestananca pre vymazanie:");
				ID1 = IDcheck(sc, databaza);
				if (ID1 == -1) break;
				databaza.zrusenieSpoluprace(ID1);
				databaza.deleteZamestnanec(ID1);
				break;
				
			case 6:
				
				System.out.println("Zadajte ID zamestnanca:");
				ID1 = IDcheck(sc, databaza);
				if (ID1 == -1) break;
				databaza.getStatistika(ID1);
				
			case 7:
				
				System.out.println("Zadajte ID zamestnanca:");
				ID1 = IDcheck(sc, databaza);
				if (ID1 == -1) break;
				databaza.spustiDovednost(ID1);
				
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
			System.out.println("zadejte prosim cele cislo ");
			sc.nextLine();
			cislo = intCheck(sc);
		}
		return cislo;
	}
	
	public static int IDcheck(Scanner sc, Databaza databaza) 
	{
		int cislo = 0;
		
		while(true) {
			try
			{
				cislo = sc.nextInt();
			}
			catch(Exception e)
			{
				System.out.println("Zadejte prosim cele cislo ");
				sc.nextLine();
				cislo = intCheck(sc);
			}
			
			if(databaza.klucCheck(cislo) && cislo > 0) return cislo;
			if(cislo == -1) return -1;
			System.out.println("ID neexistuje, skuste znova.");
			System.out.println("Pre zrusenie napiste -1");
		}
	}

}

