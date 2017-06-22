package baltimoredata;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class Area {

	private Integer id;
	private String csa2010;
	private int male10;
	private int tpop10;
	private int female10;
	private BigDecimal paa10;
	private BigDecimal phisp10;
	private BigDecimal ppac10;
	private BigDecimal pasi10;
	private BigDecimal fam10;
	private BigDecimal hhm7514;
	private BigDecimal age1810;
	private BigDecimal racdiv10;
	private BigDecimal pwhite10;
	private BigDecimal age6410;
	private BigDecimal hhpov14;
	private BigDecimal femhhs10;
	private BigDecimal hh40inc14;
	private BigDecimal hhchpov14;
	private BigDecimal hh60inc14;
	private BigDecimal hh25inc14;
	private BigDecimal p2more10;
	private BigDecimal age6510;
	private BigDecimal hhsize10;
	private BigDecimal age2410;
	private BigDecimal hh75inc14;
	private BigDecimal age510;
	private Set<Library> libraries = new HashSet<Library>(0);
	private Set<VacantBuilding> vacantBuildings = new HashSet<VacantBuilding>(0);
	private Set<GroceryStore> groceryStores = new HashSet<GroceryStore>(0);

	public Area() {
	}

	public Area(String csa2010, int male10, int tpop10, int female10, BigDecimal paa10, BigDecimal phisp10,
			BigDecimal ppac10, BigDecimal pasi10, BigDecimal fam10, BigDecimal hhm7514, BigDecimal age1810,
			BigDecimal racdiv10, BigDecimal pwhite10, BigDecimal age6410, BigDecimal hhpov14, BigDecimal femhhs10,
			BigDecimal hh40inc14, BigDecimal hhchpov14, BigDecimal hh60inc14, BigDecimal hh25inc14, BigDecimal p2more10,
			BigDecimal age6510, BigDecimal hhsize10, BigDecimal age2410, BigDecimal hh75inc14, BigDecimal age510) {
		this.csa2010 = csa2010;
		this.male10 = male10;
		this.tpop10 = tpop10;
		this.female10 = female10;
		this.paa10 = paa10;
		this.phisp10 = phisp10;
		this.ppac10 = ppac10;
		this.pasi10 = pasi10;
		this.fam10 = fam10;
		this.hhm7514 = hhm7514;
		this.age1810 = age1810;
		this.racdiv10 = racdiv10;
		this.pwhite10 = pwhite10;
		this.age6410 = age6410;
		this.hhpov14 = hhpov14;
		this.femhhs10 = femhhs10;
		this.hh40inc14 = hh40inc14;
		this.hhchpov14 = hhchpov14;
		this.hh60inc14 = hh60inc14;
		this.hh25inc14 = hh25inc14;
		this.p2more10 = p2more10;
		this.age6510 = age6510;
		this.hhsize10 = hhsize10;
		this.age2410 = age2410;
		this.hh75inc14 = hh75inc14;
		this.age510 = age510;
	}

	public Area(String csa2010, int male10, int tpop10, int female10, BigDecimal paa10, BigDecimal phisp10,
			BigDecimal ppac10, BigDecimal pasi10, BigDecimal fam10, BigDecimal hhm7514, BigDecimal age1810,
			BigDecimal racdiv10, BigDecimal pwhite10, BigDecimal age6410, BigDecimal hhpov14, BigDecimal femhhs10,
			BigDecimal hh40inc14, BigDecimal hhchpov14, BigDecimal hh60inc14, BigDecimal hh25inc14, BigDecimal p2more10,
			BigDecimal age6510, BigDecimal hhsize10, BigDecimal age2410, BigDecimal hh75inc14, BigDecimal age510,
			Set<Library> libraries, Set<VacantBuilding> vacantBuildings, Set<GroceryStore> groceryStores) {
		this.csa2010 = csa2010;
		this.male10 = male10;
		this.tpop10 = tpop10;
		this.female10 = female10;
		this.paa10 = paa10;
		this.phisp10 = phisp10;
		this.ppac10 = ppac10;
		this.pasi10 = pasi10;
		this.fam10 = fam10;
		this.hhm7514 = hhm7514;
		this.age1810 = age1810;
		this.racdiv10 = racdiv10;
		this.pwhite10 = pwhite10;
		this.age6410 = age6410;
		this.hhpov14 = hhpov14;
		this.femhhs10 = femhhs10;
		this.hh40inc14 = hh40inc14;
		this.hhchpov14 = hhchpov14;
		this.hh60inc14 = hh60inc14;
		this.hh25inc14 = hh25inc14;
		this.p2more10 = p2more10;
		this.age6510 = age6510;
		this.hhsize10 = hhsize10;
		this.age2410 = age2410;
		this.hh75inc14 = hh75inc14;
		this.age510 = age510;
		this.libraries = libraries;
		this.vacantBuildings = vacantBuildings;
		this.groceryStores = groceryStores;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCsa2010() {
		return this.csa2010;
	}

	public void setCsa2010(String csa2010) {
		this.csa2010 = csa2010;
	}

	public int getMale10() {
		return this.male10;
	}

	public void setMale10(int male10) {
		this.male10 = male10;
	}

	public int getTpop10() {
		return this.tpop10;
	}

	public void setTpop10(int tpop10) {
		this.tpop10 = tpop10;
	}

	public int getFemale10() {
		return this.female10;
	}

	public void setFemale10(int female10) {
		this.female10 = female10;
	}

	public BigDecimal getPaa10() {
		return this.paa10;
	}

	public void setPaa10(BigDecimal paa10) {
		this.paa10 = paa10;
	}

	public BigDecimal getPhisp10() {
		return this.phisp10;
	}

	public void setPhisp10(BigDecimal phisp10) {
		this.phisp10 = phisp10;
	}

	public BigDecimal getPpac10() {
		return this.ppac10;
	}

	public void setPpac10(BigDecimal ppac10) {
		this.ppac10 = ppac10;
	}

	public BigDecimal getPasi10() {
		return this.pasi10;
	}

	public void setPasi10(BigDecimal pasi10) {
		this.pasi10 = pasi10;
	}

	public BigDecimal getFam10() {
		return this.fam10;
	}

	public void setFam10(BigDecimal fam10) {
		this.fam10 = fam10;
	}

	public BigDecimal getHhm7514() {
		return this.hhm7514;
	}

	public void setHhm7514(BigDecimal hhm7514) {
		this.hhm7514 = hhm7514;
	}

	public BigDecimal getAge1810() {
		return this.age1810;
	}

	public void setAge1810(BigDecimal age1810) {
		this.age1810 = age1810;
	}

	public BigDecimal getRacdiv10() {
		return this.racdiv10;
	}

	public void setRacdiv10(BigDecimal racdiv10) {
		this.racdiv10 = racdiv10;
	}

	public BigDecimal getPwhite10() {
		return this.pwhite10;
	}

	public void setPwhite10(BigDecimal pwhite10) {
		this.pwhite10 = pwhite10;
	}

	public BigDecimal getAge6410() {
		return this.age6410;
	}

	public void setAge6410(BigDecimal age6410) {
		this.age6410 = age6410;
	}

	public BigDecimal getHhpov14() {
		return this.hhpov14;
	}

	public void setHhpov14(BigDecimal hhpov14) {
		this.hhpov14 = hhpov14;
	}

	public BigDecimal getFemhhs10() {
		return this.femhhs10;
	}

	public void setFemhhs10(BigDecimal femhhs10) {
		this.femhhs10 = femhhs10;
	}

	public BigDecimal getHh40inc14() {
		return this.hh40inc14;
	}

	public void setHh40inc14(BigDecimal hh40inc14) {
		this.hh40inc14 = hh40inc14;
	}

	public BigDecimal getHhchpov14() {
		return this.hhchpov14;
	}

	public void setHhchpov14(BigDecimal hhchpov14) {
		this.hhchpov14 = hhchpov14;
	}

	public BigDecimal getHh60inc14() {
		return this.hh60inc14;
	}

	public void setHh60inc14(BigDecimal hh60inc14) {
		this.hh60inc14 = hh60inc14;
	}

	public BigDecimal getHh25inc14() {
		return this.hh25inc14;
	}

	public void setHh25inc14(BigDecimal hh25inc14) {
		this.hh25inc14 = hh25inc14;
	}

	public BigDecimal getP2more10() {
		return this.p2more10;
	}

	public void setP2more10(BigDecimal p2more10) {
		this.p2more10 = p2more10;
	}

	public BigDecimal getAge6510() {
		return this.age6510;
	}

	public void setAge6510(BigDecimal age6510) {
		this.age6510 = age6510;
	}

	public BigDecimal getHhsize10() {
		return this.hhsize10;
	}

	public void setHhsize10(BigDecimal hhsize10) {
		this.hhsize10 = hhsize10;
	}

	public BigDecimal getAge2410() {
		return this.age2410;
	}

	public void setAge2410(BigDecimal age2410) {
		this.age2410 = age2410;
	}

	public BigDecimal getHh75inc14() {
		return this.hh75inc14;
	}

	public void setHh75inc14(BigDecimal hh75inc14) {
		this.hh75inc14 = hh75inc14;
	}

	public BigDecimal getAge510() {
		return this.age510;
	}

	public void setAge510(BigDecimal age510) {
		this.age510 = age510;
	}

	public Set<Library> getLibraries() {
		return this.libraries;
	}

	public void setLibraries(Set<Library> libraries) {
		this.libraries = libraries;
	}

	public Set<VacantBuilding> getVacantBuildings() {
		return this.vacantBuildings;
	}

	public void setVacantBuildings(Set<VacantBuilding> vacantBuildings) {
		this.vacantBuildings = vacantBuildings;
	}

	public Set<GroceryStore> getGroceryStores() {
		return this.groceryStores;
	}

	public void setGroceryStores(Set<GroceryStore> groceryStores) {
		this.groceryStores = groceryStores;
	}

}
