package org.me.myandroidstuff;
/*By Lewis Sidebotham - S1318445*/
public class carpark {

	
		//Change to ints
	private String carParkIdentity;
    private int totalCapacity;
    private int carParkOccupancy;
    private String carParkStatus;
    private int occupiedSpaces;
	
    public String getcarParkIdentity(){
		return carParkIdentity;
		}
	public int getTotalCapacity(){
		return totalCapacity;
		}
	public int getCarParkOccupancy(){
		return carParkOccupancy;
		}
	public String getCarParkStatus(){
		return carParkStatus;
	}
	public int getOccupiedSpaces(){
		return occupiedSpaces;
	}
    
    
    public void setCarParkIdentity(String carParkIdentity){
	this.carParkIdentity = carParkIdentity;
		}
    public void setTotalCapacity(int totalCapacity){
		this.totalCapacity = totalCapacity;
		}
	public void setCarParkOccupancy(int carParkOccupancy){
		this.carParkOccupancy = carParkOccupancy;
		}
	public void setCarParkStatus(String carParkStatus){
		this.carParkStatus = carParkStatus;
		}
	public void setOccupiedSpaces(int occupiedSpaces){
		this.occupiedSpaces = occupiedSpaces;
	}
	
	
	
    @Override
	public String toString() {
		return carParkIdentity + "\n"  +  carParkStatus;// + "\n" + totalCapacity + "\n" + carParkOccupancy + "\n" +  occupiedSpaces
	}
	

}
