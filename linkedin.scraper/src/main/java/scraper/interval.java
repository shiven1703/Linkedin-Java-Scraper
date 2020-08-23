package scraper;
import java.io.Serializable;;

public class interval implements Serializable {

	private int intervalTime;
	private String intervalType;
	
	public interval() {
		intervalTime = 0;
		intervalType = "";
	}
	
	public interval(int time, String type){
		this.intervalTime = time;
		this.intervalType = type;
	}
	
	public int getIntervalTime() {
		return intervalTime;
	}

	public void setIntervalTime(int intervalTime) {
		this.intervalTime = intervalTime;
	}

	public String getIntervalType() {
		return intervalType;
	}

	public void setIntervalType(String intervalType) {
		this.intervalType = intervalType;
	}
	
	
	
}
