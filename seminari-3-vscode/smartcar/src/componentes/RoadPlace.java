package componentes;

public class RoadPlace {

	protected String road = null;
	protected int lane = null;
	protected int km = 0;
	protected int roadLanes = 0;
	
	public RoadPlace(String road, int km,int lane = 0,int roadLanes=2) {
		this.road = road;
		this.km = km;
		this.lane = lane;
		this.roadLanes = roadLanes;
	}
	
	public int getRoadLanes(){
		return this.roadLanes;
	}

	public void setRoadLanes(int roadLanes){
		this.roadLanes = roadLanes;
	}

	public void setKm(int km) {
		this.km = km;
	}

	public void setLane(int lane){
		this.lane = lane;
	}

	public int getLane(){
		return this.lane;
	}
	
	public int getKm() {
		return km;
	}
	
	public String getRoad() {
		return road;
	}
	
	public void setRoad(String road) {
		this.road = road;
	}
	
}
