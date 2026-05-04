
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


public class Main {

	public static void main(String[] args) {
		Scanner sc=new Scanner(System.in);
		int volba;
		boolean koniec=false;
		String meno;
		String priezvisko;
		String skupina;
		int ID;
		int ID1;
		int ID2;
		int rok;
		int spolupraca;
		Databaza databaza = new Databaza();

		boolean sqlLoaded = databaza.nacitatSQL();
		if (!sqlLoaded) {
			databaza.nacitat();
		}
		databaza.ulozitSQL();

		int lastIdFromFile = readLastID();
		int dbMaxId = databaza.getNajvyssieID();
		ID = Math.max(lastIdFromFile, dbMaxId);
		
		while(!koniec) {
			
			System.out.println("1. Pridaj zamestnanca");
			System.out.println("2. Vypis zamestnanca podla ID");
			System.out.println("3. Nastavenie spoluprace");
			System.out.println("4. Vypis spoluprace");
			System.out.println("5. Vymazanie zamestnanca");
			System.out.println("6. Statistika spoluprace pracovnika");
			System.out.println("7. Spustit dovednost pracovnika");
			System.out.println("8. Abecedny vypis zamestanancov");
			System.out.println("9. Vypis poctu zamestnancov v skupinach");
			System.out.println("10. Globalna statistika spoluprace");
			System.out.println("11. KONIEC");
			volba = intCheck(sc);
			
			switch(volba) {
			
			case 11: 
				
				koniec = true;
				break;

			case 10:

				databaza.vypisGlobalnuStatistiku();
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
				ulozANacitajDatabazu(databaza);
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
				ulozANacitajDatabazu(databaza);
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
				ulozANacitajDatabazu(databaza);
				break;
				
			case 6:
				
				System.out.println("Zadajte ID zamestnanca:");
				ID1 = IDcheck(sc, databaza);
				if (ID1 == -1) break;
				databaza.getStatistika(ID1);
				break;
				
			case 7:
				
				System.out.println("Zadajte ID zamestnanca:");
				ID1 = IDcheck(sc, databaza);
				if (ID1 == -1) break;
				databaza.spustiDovednost(ID1);
				break;
				
			case 8:
				
				System.out.println("Vyberte skupinu pre abecedny vypis: Analytik/Bezpecak");
				while (true) {
					skupina = sc.next();
					if (skupina.equals("Analytik") || skupina.equals("Bezpecak")) {
						databaza.abecedneZoradenie(skupina);
						break;
					}
					System.out.println("Zadajte Analytik alebo Bezpecak!!!");
				}
				break;

			case 9:

				databaza.vypisPocetZamestnancovVSkupinach();
				break;
			}			
			
		}

		databaza.ulozit();
		databaza.ulozitSQL();
		writeLastID(ID);
		sc.close();

	}

	public static void ulozANacitajDatabazu(Databaza databaza)
	{
		if (!databaza.ulozit()) {
			System.out.println("Ulozenie do suboru zlyhalo.");
			return;
		}

		if (!databaza.nacitat()) {
			System.out.println("Nacitanie zo suboru zlyhalo.");
		}
	}

	public static int intCheck(Scanner sc) 
	{
		while (true) {
			try {
				return sc.nextInt();
			} catch(Exception e) {
				System.out.println("zadejte prosim cele cislo ");
				sc.nextLine();
			}
		}
	}
	
	public static int IDcheck(Scanner sc, Databaza databaza) 
	{
		while(true) {
			try
			{
				int cislo = sc.nextInt();
				if(databaza.klucCheck(cislo) && cislo > 0) return cislo;
				if(cislo == -1) return -1;
				System.out.println("ID neexistuje, skuste znova.");
				System.out.println("Pre zrusenie napiste -1");
			}
			catch(Exception e)
			{
				System.out.println("Zadejte prosim cele cislo ");
				sc.nextLine();
			}
		}
	}


	public static int readLastID() {
		File f = new File("last_id.txt");
		if (!f.exists()) return 0;
		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
			String line = br.readLine();
			if (line == null) return 0;
			try {
				return Integer.parseInt(line.trim());
			} catch (NumberFormatException e) {
				return 0;
			}
		} catch (IOException e) {
			return 0;
		}
	}

	public static void writeLastID(int ID) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter("last_id.txt", false))) {
			bw.write(Integer.toString(ID));
		} catch (IOException e) {
			System.out.println("Chyba pri uklade last_id: " + e.getMessage());
		}
	}

}

