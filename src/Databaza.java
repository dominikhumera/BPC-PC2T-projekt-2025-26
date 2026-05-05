import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Databaza {
	private static final String SUBOR_DATABAZY = "databaza.txt";
	private static final String SQLITE_URL = "jdbc:sqlite:databaza.db";

	private Map<Integer, Zamestnanec> databaza;

	public Databaza() {
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

	public boolean nacitatSQL() {
		if (!inicializovatSQLiteDriver()) {
			return false;
		}

		try (Connection connection = DriverManager.getConnection(SQLITE_URL);
				Statement statement = connection.createStatement()) {
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS zamestnanci ("
					+ "id INTEGER PRIMARY KEY, "
					+ "typ TEXT NOT NULL, "
					+ "meno TEXT NOT NULL, "
					+ "priezvisko TEXT NOT NULL, "
					+ "rok INTEGER NOT NULL)");
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS spoluprace ("
					+ "id1 INTEGER NOT NULL, "
					+ "id2 INTEGER NOT NULL, "
					+ "hodnota INTEGER NOT NULL, "
					+ "PRIMARY KEY (id1, id2))");

			databaza.clear();

			try (ResultSet rs = statement.executeQuery("SELECT id, typ, meno, priezvisko, rok FROM zamestnanci")) {
				while (rs.next()) {
					int id = rs.getInt("id");
					String typ = rs.getString("typ");
					String meno = rs.getString("meno");
					String priezvisko = rs.getString("priezvisko");
					int rok = rs.getInt("rok");

					if ("Analytik".equals(typ)) {
						databaza.put(id, new Analytik(meno, priezvisko, rok, id));
					} else if ("Bezpecak".equals(typ)) {
						databaza.put(id, new Bezpecak(meno, priezvisko, rok, id));
					}
				}
			}

			try (ResultSet rs = statement.executeQuery("SELECT id1, id2, hodnota FROM spoluprace")) {
				while (rs.next()) {
					int id1 = rs.getInt("id1");
					int id2 = rs.getInt("id2");
					int hodnota = rs.getInt("hodnota");
					Zamestnanec z1 = databaza.get(id1);
					Zamestnanec z2 = databaza.get(id2);
					if (z1 != null && z2 != null) {
						z1.setSpolupraca(id2, hodnota);
						z2.setSpolupraca(id1, hodnota);
					}
				}
			}

			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	public boolean ulozitSQL() {
		if (!inicializovatSQLiteDriver()) {
			return false;
		}

		try (Connection connection = DriverManager.getConnection(SQLITE_URL);
				Statement statement = connection.createStatement()) {
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS zamestnanci ("
					+ "id INTEGER PRIMARY KEY, "
					+ "typ TEXT NOT NULL, "
					+ "meno TEXT NOT NULL, "
					+ "priezvisko TEXT NOT NULL, "
					+ "rok INTEGER NOT NULL)");
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS spoluprace ("
					+ "id1 INTEGER NOT NULL, "
					+ "id2 INTEGER NOT NULL, "
					+ "hodnota INTEGER NOT NULL, "
					+ "PRIMARY KEY (id1, id2))");

			connection.setAutoCommit(false);
			try {
				statement.executeUpdate("DELETE FROM spoluprace");
				statement.executeUpdate("DELETE FROM zamestnanci");

				try (PreparedStatement ps = connection.prepareStatement(
						"INSERT INTO zamestnanci(id, typ, meno, priezvisko, rok) VALUES (?, ?, ?, ?, ?)")) {
					for (Zamestnanec z : databaza.values()) {
						ps.setInt(1, z.getID());
						ps.setString(2, (z instanceof Analytik) ? "Analytik" : "Bezpecak");
						ps.setString(3, z.getMeno());
						ps.setString(4, z.getPriezvisko());
						ps.setInt(5, z.getRok());
						ps.addBatch();
					}
					ps.executeBatch();
				}

				try (PreparedStatement ps = connection.prepareStatement(
						"INSERT INTO spoluprace(id1, id2, hodnota) VALUES (?, ?, ?)")) {
					for (Zamestnanec z : databaza.values()) {
						for (Map.Entry<Integer, Integer> zaznam : z.getDatabazaSpoluprace().entrySet()) {
							if (z.getID() < zaznam.getKey()) {
								ps.setInt(1, z.getID());
								ps.setInt(2, zaznam.getKey());
								ps.setInt(3, zaznam.getValue());
								ps.addBatch();
							}
						}
					}
					ps.executeBatch();
				}

				connection.commit();
				return true;
			} catch (SQLException e) {
				connection.rollback();
				return false;
			}
		} catch (SQLException e) {
			return false;
		}
	}

	public boolean inicializovatSQLiteDriver() {
		try {
			Class.forName("org.sqlite.JDBC");
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	public int getNajvyssieID() {
		int maxID = 0;
		for (int id : databaza.keySet()) {
			if (id > maxID) {
				maxID = id;
			}
		}
		return maxID;
	}

	public void vypisPocetZamestnancovVSkupinach() {
		int analytici = 0;
		int bezpecaci = 0;

		for (Zamestnanec z : databaza.values()) {
			if (z instanceof Analytik) {
				analytici++;
			} else if (z instanceof Bezpecak) {
				bezpecaci++;
			}
		}

		System.out.println("Pocet analytikov: " + analytici);
		System.out.println("Pocet bezpecakov: " + bezpecaci);
	}

	public void vypisGlobalnuStatistiku() {
		int pocetSpoluprac = 0;
		int sumaHodnot = 0;
		int maxPocetSpoluprac = 0;
		int zamestnanecMaxPocet = -1;
		int maxSumaHodnot = 0;
		int zamestnanecMaxSuma = -1;

		for (Zamestnanec z : databaza.values()) {
			int pocet = z.getDatabazaSpoluprace().size();
			int suma = 0;
			for (int hodnota : z.getDatabazaSpoluprace().values()) {
				suma += hodnota;
			}

			if (pocet > maxPocetSpoluprac) {
				maxPocetSpoluprac = pocet;
				zamestnanecMaxPocet = z.getID();
			}

			if (suma > maxSumaHodnot) {
				maxSumaHodnot = suma;
				zamestnanecMaxSuma = z.getID();
			}
		}

		for (Zamestnanec z : databaza.values()) {
			for (Map.Entry<Integer, Integer> zaznam : z.getDatabazaSpoluprace().entrySet()) {
				if (z.getID() < zaznam.getKey()) {
					pocetSpoluprac++;
					sumaHodnot += zaznam.getValue();
				}
			}
		}

		if (pocetSpoluprac == 0) {
			System.out.println("V databaze nie su ziadne spoluprace.");
			return;
		}

		double priemer = (double) sumaHodnot / pocetSpoluprac;
		System.out.println("Pocet zamestnancov: " + databaza.size());
		System.out.println("Pocet spolupraci: " + pocetSpoluprac);
		System.out.println("Priemerna hodnota spoluprace v databaze: " + priemer);

		if (zamestnanecMaxPocet != -1) {
			Zamestnanec z = databaza.get(zamestnanecMaxPocet);
			System.out.println("Zamestnanec s najviac spolupracami: " + z.getMeno() + " " + z.getPriezvisko() + " (ID: " + z.getID() + ", pocet: " + maxPocetSpoluprac + ")");
		}

		if (zamestnanecMaxSuma != -1) {
			Zamestnanec z = databaza.get(zamestnanecMaxSuma);
			System.out.println("Zamestnanec s najvyssim suctom hodnot spoluprace: " + z.getMeno() + " " + z.getPriezvisko() + " (ID: " + z.getID() + ", suma: " + maxSumaHodnot + ")");
		}
	}

	public void abecedneZoradenie(String skupina) {
		List<String> zoradeneMena = new ArrayList<>();
		for (Zamestnanec z : databaza.values()) {
			if ("Analytik".equals(skupina) && z instanceof Analytik) {
				zoradeneMena.add(z.getPriezvisko() + " " + z.getMeno());
			} else if ("Bezpecak".equals(skupina) && z instanceof Bezpecak) {
				zoradeneMena.add(z.getPriezvisko() + " " + z.getMeno());
			}
		}

		Collections.sort(zoradeneMena);
		for (String meno : zoradeneMena) {
			System.out.println(meno);
		}
	}

	public void setAnalytik(String meno, String priezvisko, int rok, int ID) {
		databaza.put(ID, new Analytik(meno, priezvisko, rok, ID));
	}

	public void setBezpecak(String meno, String priezvisko, int rok, int ID) {
		databaza.put(ID, new Bezpecak(meno, priezvisko, rok, ID));
	}

	public boolean getInfoZamestnanec(int ID) {
		Zamestnanec z = databaza.get(ID);
		if (z == null) return false;
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

	public Zamestnanec getZamestnanec(int ID) {
		return databaza.get(ID);
	}

	public void setSpolupraca(int ID1, int ID2, int s) {
		Zamestnanec z1 = databaza.get(ID1);
		z1.setSpolupraca(ID2, s);
		databaza.put(ID1, z1);

		Zamestnanec z2 = databaza.get(ID2);
		z2.setSpolupraca(ID1, s);
		databaza.put(ID2, z2);
	}

	public void getSpolupraca(int ID1, int ID2) {
		Zamestnanec z1 = databaza.get(ID1);
		int s = z1.getHodnotaSpoluprace(ID2);
		System.out.println("Hodnota spoluprace medzi zamestanancom s ID:" + ID1 + " a s ID:" + ID2 + " je:" + s);
	}

	public boolean klucCheck(int kluc) {
		if (databaza.containsKey(kluc)) return true;
		return false;
	}

	public void deleteZamestnanec(int ID) {
		databaza.remove(ID);
	}

	public void getStatistika(int ID) {
		Zamestnanec z = databaza.get(ID);
		z.statistika();
	}

	public void zrusenieSpoluprace(int ID) {
		for (int key : databaza.keySet()) {
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
