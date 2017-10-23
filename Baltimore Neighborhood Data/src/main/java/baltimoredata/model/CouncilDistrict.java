package baltimoredata.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonView;

import baltimoredata.view.AddressViews;

/**
 * CouncilDistrict generated by hbm2java
 */
@Entity
@Table(name = "council_district", catalog = "baltimore_neighborhoods", uniqueConstraints = @UniqueConstraint(columnNames = "district_number"))
public class CouncilDistrict {

	private Integer id;
	private int districtNumber;
	private Set<Address> addresses = new HashSet<Address>(0);

	public CouncilDistrict() {
	}

	public CouncilDistrict(int districtNumber) {
		this.districtNumber = districtNumber;
	}

	public CouncilDistrict(int districtNumber, Set<Address> addresses) {
		this.districtNumber = districtNumber;
		this.addresses = addresses;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	@Null
	@JsonView(AddressViews.Limited.class)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "district_number", unique = true, nullable = false)
	@NotNull
	@Min(1)
	@JsonView(AddressViews.Limited.class)
	public int getDistrictNumber() {
		return this.districtNumber;
	}

	public void setDistrictNumber(int districtNumber) {
		this.districtNumber = districtNumber;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "councilDistrict")
	@Size(max=0)
	public Set<Address> getAddresses() {
		return this.addresses;
	}

	public void setAddresses(Set<Address> addresses) {
		this.addresses = addresses;
	}

}
