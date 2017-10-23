package baltimoredata.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.vividsolutions.jts.geom.Point;

import com.fasterxml.jackson.annotation.JsonView;

import baltimoredata.view.POIViews;

/**
 * Location generated by hbm2java
 */
@Entity
@Table(name = "location", catalog = "baltimore_neighborhoods", uniqueConstraints = {
		@UniqueConstraint(columnNames = "address_id"),
		@UniqueConstraint(columnNames = { "block_number", "lot_number" }), @UniqueConstraint(columnNames = "coords") })
public class Location {

	private Integer id;
	private Address address;
	private Point coords;
	private String blockNumber;
	private String lotNumber;
	private Set<VacantBuilding> vacantBuildings = new HashSet<VacantBuilding>(0);

	public Location() {
	}

	public Location(Address address, Point coords, String blockNumber, String lotNumber) {
		this.address = address;
		this.coords = coords;
		this.blockNumber = blockNumber;
		this.lotNumber = lotNumber;
	}

	public Location(Address address, Point coords, String blockNumber, String lotNumber, Set<VacantBuilding> vacantBuildings) {
		this.address = address;
		this.coords = coords;
		this.blockNumber = blockNumber;
		this.lotNumber = lotNumber;
		this.vacantBuildings = vacantBuildings;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	@Null
	@JsonView(POIViews.Minimal.class)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "address_id", unique = true, nullable = false)
	@NotNull
	@JsonView(POIViews.Limited.class)
	public Address getAddress() {
		return this.address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	@Column(name = "coords", unique = true, nullable = false, columnDefinition = "POINT")
	@NotNull
	@JsonView(POIViews.Minimal.class)
	public Point getCoords() {
		return this.coords;
	}

	public void setCoords(Point coords) {
		this.coords = coords;
	}

	@Column(name = "block_number", nullable = false)
	@NotNull
	@Pattern(regexp="^[0-9]{4}[a-z]|[0-9]{1,4}$")
	@JsonView(POIViews.Minimal.class)
	public String getBlockNumber() {
		return this.blockNumber;
	}

	public void setBlockNumber(String blockNumber) {
		this.blockNumber = blockNumber;
	}

	@Column(name = "lot_number", nullable = false)
	@NotNull
	@Pattern(regexp="^[0-9]{3}[a-z]|[0-9]{1,3}$")
	@JsonView(POIViews.Minimal.class)
	public String getLotNumber() {
		return this.lotNumber;
	}

	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "location")
	@Size(max=0)
	public Set<VacantBuilding> getVacantBuildings() {
		return this.vacantBuildings;
	}

	public void setVacantBuildings(Set<VacantBuilding> vacantBuildings) {
		this.vacantBuildings = vacantBuildings;
	}

}
