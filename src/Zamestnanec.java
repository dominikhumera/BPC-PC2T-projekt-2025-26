
abstract class Zamestnanec {

	private int ID;
	private String priezvisko;
	private String meno;
	private int rok;
	private Spolupraca spolupraca;

	public Zamestnanec(String meno, String priezvisko, int rok, int ID) {
		
		this.ID = ID;
		this.priezvisko = priezvisko;
		this.meno = meno;
		this.rok = rok;
		this.spolupraca = new Spolupraca();
		
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
	
	public void setSpolupraca(int ID1, int s) {
		spolupraca.setHodnotaSpoluprace(ID1, s);
	}
	
	public int getSpolupraca(int ID1) {
		return spolupraca.getHodnotaSpoluprace(ID1);
	}
	
	
	public void statistika() {
		spolupraca.statistikaSpoluprace();
	}
	
	public void zrusenieSpoluprace(int ID) {
		spolupraca.zrusenieSpoluprace(ID);
	}
	
	
	
}
