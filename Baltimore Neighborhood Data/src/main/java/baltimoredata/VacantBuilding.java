package baltimoredata;

import java.io.Serializable;
import java.util.Date;

public class VacantBuilding {

	private Integer id;
	private Area area;
	private String referenceid;
	private String block;
	private String lot;
	private String buildingaddress;
	private Date noticedate;
	private String neighborhood;
	private String policedistrict;
	private int councildistrict;
	private Serializable location;

	public VacantBuilding() {
	}

	public VacantBuilding(Area area, String referenceid, String block, String lot, String buildingaddress,
			Date noticedate, String neighborhood, String policedistrict, int councildistrict, Serializable location) {
		this.area = area;
		this.referenceid = referenceid;
		this.block = block;
		this.lot = lot;
		this.buildingaddress = buildingaddress;
		this.noticedate = noticedate;
		this.neighborhood = neighborhood;
		this.policedistrict = policedistrict;
		this.councildistrict = councildistrict;
		this.location = location;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Area getArea() {
		return this.area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public String getReferenceid() {
		return this.referenceid;
	}

	public void setReferenceid(String referenceid) {
		this.referenceid = referenceid;
	}

	public String getBlock() {
		return this.block;
	}

	public void setBlock(String block) {
		this.block = block;
	}

	public String getLot() {
		return this.lot;
	}

	public void setLot(String lot) {
		this.lot = lot;
	}

	public String getBuildingaddress() {
		return this.buildingaddress;
	}

	public void setBuildingaddress(String buildingaddress) {
		this.buildingaddress = buildingaddress;
	}

	public Date getNoticedate() {
		return this.noticedate;
	}

	public void setNoticedate(Date noticedate) {
		this.noticedate = noticedate;
	}

	public String getNeighborhood() {
		return this.neighborhood;
	}

	public void setNeighborhood(String neighborhood) {
		this.neighborhood = neighborhood;
	}

	public String getPolicedistrict() {
		return this.policedistrict;
	}

	public void setPolicedistrict(String policedistrict) {
		this.policedistrict = policedistrict;
	}

	public int getCouncildistrict() {
		return this.councildistrict;
	}

	public void setCouncildistrict(int councildistrict) {
		this.councildistrict = councildistrict;
	}

	public Serializable getLocation() {
		return this.location;
	}

	public void setLocation(Serializable location) {
		this.location = location;
	}

}

