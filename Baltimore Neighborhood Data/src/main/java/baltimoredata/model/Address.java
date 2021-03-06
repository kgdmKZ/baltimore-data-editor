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
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonView;

import baltimoredata.view.AddressViews;

/**
 * Address generated by hbm2java
 */
@Entity
@Table(name = "address", catalog = "baltimore_neighborhoods", uniqueConstraints = @UniqueConstraint(columnNames = {
		"neighborhood_id", "street_address" }))
public class Address {

	private Integer id;
	private CouncilDistrict councilDistrict;
	private Neighborhood neighborhood;
	private PoliceDistrict policeDistrict;
	private String streetAddress;
	private Set<Library> libraries = new HashSet<Library>(0);
	private Set<GroceryStore> groceryStores = new HashSet<GroceryStore>(0);
	private Set<Location> locations = new HashSet<Location>(0);

	public Address() {
	}

	public Address(CouncilDistrict councilDistrict, Neighborhood neighborhood, PoliceDistrict policeDistrict) {
		this.councilDistrict = councilDistrict;
		this.neighborhood = neighborhood;
		this.policeDistrict = policeDistrict;
	}

	public Address(CouncilDistrict councilDistrict, Neighborhood neighborhood, PoliceDistrict policeDistrict,
			String streetAddress, Set<Library> libraries, Set<GroceryStore> groceryStores, Set<Location> locations) {
		this.councilDistrict = councilDistrict;
		this.neighborhood = neighborhood;
		this.policeDistrict = policeDistrict;
		this.streetAddress = streetAddress;
		this.libraries = libraries;
		this.groceryStores = groceryStores;
		this.locations = locations;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	@Null
	@JsonView(AddressViews.POIMinimal.class)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "councildistrict", nullable = false)
	@NotNull
	@JsonView(AddressViews.Limited.class)
	public CouncilDistrict getCouncilDistrict() {
		return this.councilDistrict;
	}

	public void setCouncilDistrict(CouncilDistrict councilDistrict) {
		this.councilDistrict = councilDistrict;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "neighborhood_id", nullable = false)
	@NotNull
	@JsonView(AddressViews.Minimal.class)
	public Neighborhood getNeighborhood() {
		return this.neighborhood;
	}

	public void setNeighborhood(Neighborhood neighborhood) {
		this.neighborhood = neighborhood;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "policedistrict", nullable = false)
	@NotNull
	@JsonView(AddressViews.Limited.class)
	public PoliceDistrict getPoliceDistrict() {
		return this.policeDistrict;
	}

	public void setPoliceDistrict(PoliceDistrict policeDistrict) {
		this.policeDistrict = policeDistrict;
	}

	@Column(name = "street_address")
	@JsonView(AddressViews.POIMinimal.class)
	public String getStreetAddress() {
		return this.streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "address")
	@Size(max=0)
	public Set<Library> getLibraries() {
		return this.libraries;
	}

	public void setLibraries(Set<Library> libraries) {
		this.libraries = libraries;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "address")
	@Size(max=0)
	public Set<GroceryStore> getGroceryStores() {
		return this.groceryStores;
	}

	public void setGroceryStores(Set<GroceryStore> groceryStores) {
		this.groceryStores = groceryStores;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "address")
	@Size(max=0)
	public Set<Location> getLocations() {
		return this.locations;
	}

	public void setLocations(Set<Location> locations) {
		this.locations = locations;
	}

}
