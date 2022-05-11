package Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Train {
	
	private String id;
	private String name;
	private List<StoppingInfo> stops = new ArrayList();
	private List<String> route = new ArrayList();
	private List<String> days = new ArrayList();
	
	public Train(String id ,String name,  List<StoppingInfo> stops, List<String> route , List<String> days) {
		super();
		this.id = id;
		this.name = name;
		this.stops = stops;
		this.route = route;
		this.days = days;
		
	}

	public List<String> getDays() {
		return days;
	}

	public String getId() {
		return id;
	}

    public String getName() {
    	return name;
    }


	public List<StoppingInfo> getStops() {
		return stops;
	}



	public List<String> getRoute() {
		return route;
	}
    
 
	
//	@Override
//	public String toString() {
//		return "\n -------------------------------- Train id - " + id  + "--------------------------------------" + "\n---------------------route-----------\n" + stops + "\n" + vacancy;
//	}
//    
	
    
}
