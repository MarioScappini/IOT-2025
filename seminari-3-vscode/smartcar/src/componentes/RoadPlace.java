package componentes;

public class RoadPlace {

	protected String road = null;
	protected int lane = 0;
	protected int km = 0;
	protected int roadLanes = 0;
	
	public RoadPlace(String road, int km, int lane, int roadLanes) {
        this.road = road;
        this.km = km;
        this.lane = lane;
        this.roadLanes = roadLanes;
    }

    // Constructor with default roadLanes = 2
    public RoadPlace(String road, int km, int lane) {
        this(road, km, lane, 2);
    }

    // Constructor with default lane = 0 and roadLanes = 2
    public RoadPlace(String road, int km) {
        this(road, km, 0, 2);
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
