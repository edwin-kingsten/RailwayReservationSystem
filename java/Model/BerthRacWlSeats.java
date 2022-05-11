package Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BerthRacWlSeats {
	private Map<BerthType, List<String>> allBerths;
	private Integer berthCount;
	private List<String> racSeats;
	private List<String> wL;
	
	
	public BerthRacWlSeats(Map<BerthType, List<String>> allBerths, Integer berthCount, List<String> racSeats,
			List<String> wL) {
		super();
		this.allBerths = allBerths;
		this.berthCount = berthCount;
		this.racSeats = racSeats;
		this.wL = wL;
	}
	
	
	public Map<BerthType, List<String>> getAllBerths() {
		return allBerths;
	}
	public Integer getBerthCount() {
		return berthCount;
	}
	public List<String> getRacSeats() {
		return racSeats;
	}
	public List<String> getWL() {
		return wL;
	}
	
	
	
}

