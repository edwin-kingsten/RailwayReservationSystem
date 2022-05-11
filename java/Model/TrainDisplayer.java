package Model;

import java.util.List;

public class TrainDisplayer {
	
	private String id;
	private String Name;
	private String src;
	private String dest;
	private String srcName;
	private String destName;
	private String startTime;
	private String endTime;
	private String travelTime;
	private List<String> availableDays;
	
	
	public TrainDisplayer(String id, String name, String src, String dest, String srcName, String destName,
			String startTime, String endTime, String travelTime, List<String> availableDays) {
		super();
		this.id = id;
		Name = name;
		this.src = src;
		this.dest = dest;
		this.srcName = srcName;
		this.destName = destName;
		this.startTime = startTime;
		this.endTime = endTime;
		this.travelTime = travelTime;
		this.availableDays = availableDays;
	}


	public String getId() {
		return id;
	}


	public String getName() {
		return Name;
	}


	public String getSrc() {
		return src;
	}


	public String getDest() {
		return dest;
	}


	public String getSrcName() {
		return srcName;
	}


	public String getDestName() {
		return destName;
	}


	public String getStartTime() {
		return startTime;
	}


	public String getEndTime() {
		return endTime;
	}


	public String getTravelTime() {
		return travelTime;
	}


	public List<String> getAvailableDays() {
		return availableDays;
	}


	@Override
	public String toString() {
		return "TrainDisplayer [id=" + id + ", Name=" + Name + ", src=" + src + ", dest=" + dest + ", srcName="
				+ srcName + ", destName=" + destName + ", startTime=" + startTime + ", endTime=" + endTime
				+ ", travelTime=" + travelTime + ", availableDays=" + availableDays + "]";
	}
    
	

}
