package baltimoredata;

public class Library {

	private Integer id;
	private Area area;
	private String name;
	private String neighborhood;
	private int councildistrict;
	private int zipcode;
	private String policedistrict;
	private String location1Address;
	private String location1State;
	private String location1City;

	public Library() {
	}

	public Library(Area area, String name, String neighborhood, int councildistrict, int zipcode, String policedistrict,
			String location1Address, String location1State, String location1City) {
		this.area = area;
		this.name = name;
		this.neighborhood = neighborhood;
		this.councildistrict = councildistrict;
		this.zipcode = zipcode;
		this.policedistrict = policedistrict;
		this.location1Address = location1Address;
		this.location1State = location1State;
		this.location1City = location1City;
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

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNeighborhood() {
		return this.neighborhood;
	}

	public void setNeighborhood(String neighborhood) {
		this.neighborhood = neighborhood;
	}

	public int getCouncildistrict() {
		return this.councildistrict;
	}

	public void setCouncildistrict(int councildistrict) {
		this.councildistrict = councildistrict;
	}

	public int getZipcode() {
		return this.zipcode;
	}

	public void setZipcode(int zipcode) {
		this.zipcode = zipcode;
	}

	public String getPolicedistrict() {
		return this.policedistrict;
	}

	public void setPolicedistrict(String policedistrict) {
		this.policedistrict = policedistrict;
	}

	public String getLocation1Address() {
		return this.location1Address;
	}

	public void setLocation1Address(String location1Address) {
		this.location1Address = location1Address;
	}

	public String getLocation1State() {
		return this.location1State;
	}

	public void setLocation1State(String location1State) {
		this.location1State = location1State;
	}

	public String getLocation1City() {
		return this.location1City;
	}

	public void setLocation1City(String location1City) {
		this.location1City = location1City;
	}

}

