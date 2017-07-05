package baltimoredata.model;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * GroceryType generated by hbm2java
 */
@Entity
@Table(name = "grocery_type", catalog = "baltimore_neighborhoods", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class GroceryType {

	private Integer id;
	private String name;
	private Set<GroceryStore> groceryStores = new HashSet<GroceryStore>(0);

	public GroceryType() {
	}

	public GroceryType(String name) {
		this.name = name;
	}

	public GroceryType(String name, Set<GroceryStore> groceryStores) {
		this.name = name;
		this.groceryStores = groceryStores;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "name", unique = true, nullable = false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "groceryType")
	public Set<GroceryStore> getGroceryStores() {
		return this.groceryStores;
	}

	public void setGroceryStores(Set<GroceryStore> groceryStores) {
		this.groceryStores = groceryStores;
	}

}