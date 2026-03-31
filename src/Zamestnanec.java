
public class Zamestnanec {

	private int ID;
	private String priezvisko;
	private String meno;
	private int rok;

	public Zamestnanec(String meno, String priezvisko, int rok, int ID) {
		
		this.ID = ID;
		this.priezvisko = priezvisko;
		this.meno = meno;
		this.rok = rok;
		
	}
	
	public String getMeno()
	{
		return meno;
	}
	
	public String getPriezvisko()
	{
		return priezvisko;
	}
	
	public int getID()
	{
		return ID;
	}
	
	public int getRok()
	{
		return rok;
	}
}
