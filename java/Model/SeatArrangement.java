package Model;

import java.util.List;
import java.util.Map;



public class SeatArrangement {
	
	private Map<String , Map<String , List<String>>> Ac1;
	private Map<String , Map<String , List<String>>> Ac2;
	private Map<String , Map<String , List<String>>> Ac3;
	private Map<String , Map<String , List<String>>> Sleeper;
	private Map<String , Map<String , List<String>>> SS;
	
	public SeatArrangement(Map<String , Map<String , List<String>>> ac1, Map<String, Map<String, List<String>>> ac2,
			Map<String, Map<String, List<String>>> ac3, Map<String, Map<String, List<String>>> sleeper , Map<String , Map<String , List<String>>> SS) {
		super();
		Ac1 = ac1;
		Ac2 = ac2;
		Ac3 = ac3;
		Sleeper = sleeper;
		this.SS = SS;
	}


	public Map<String , Map<String , List<String>>> getAc1() {
		return Ac1;
	}


	public Map<String, Map<String, List<String>>> getAc2() {
		return Ac2;
	}


	public Map<String, Map<String, List<String>>> getAc3() {
		return Ac3;
	}


	public Map<String, Map<String, List<String>>> getSleeper() {
		return Sleeper;
	}

    public Map<String , Map<String , List<String>>> getSS(){
    	return SS;
    }

	public String toString() {
		return "\n" + " First Class AC - " + Ac1 
					+ "\n 2-Tier AC - "    + Ac2 
					+ "\n 3 - Tier AC - "  + Ac3
					+ "\n Sleeper - "      + Sleeper
					+ "\n SS - "           + SS;
	}
}
