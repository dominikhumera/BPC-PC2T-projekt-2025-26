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
	
}
