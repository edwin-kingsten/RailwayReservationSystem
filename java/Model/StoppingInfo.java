package Model;

public class StoppingInfo{
	private String id;
	private String arrives;
	private String departs;
	private String halt;
	private Integer distance;
	private Integer day;
	private Integer index;
	
	
	public StoppingInfo(String id, String arrives, String departs, String halt, Integer distance, Integer day,
			Integer index) {
		super();
		this.id = id;
		this.arrives = arrives;
		this.departs = departs;
		this.halt = halt;
		this.distance = distance;
		this.day = day;
		this.index = index;
	}



	public String getId() {
		return id;
	}



	public String getArrives() {
		return arrives;
	}



	public String getDeparts() {
		return departs;
	}



	public String getHalt() {
		return halt;
	}



	public Integer getDistance() {
		return distance;
	}



	public Integer getDay() {
		return day;
	}



	public Integer getIndex() {
		return index;
	}



	public String toString() {
		return ( 	   ""
				+ "stopping id  - "        + id               + "\t| " 
				     + "Arrival Time - "   + arrives          + "\t| " 
				     + "Halt Time - "      + halt   + " min"  + "\t| "
				     + "Dist - "           + distance + " Km" + "\t| "
				     + "Day - "            + day              
				);
	}
}
