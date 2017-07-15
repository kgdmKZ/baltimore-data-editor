package baltimoredata.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import baltimoredata.model.Area;
import baltimoredata.model.projection.LimitedArea;

public interface AreaRepository extends CrudRepository<Area, Integer> {
	Area findByCsa2010(String csa2010);
	
	@Query(value="SELECT a.id AS id, a.csa2010 AS csa2010, a.male10 AS male10, a.female10 AS female10, a.paa10 AS paa10,"
			+ "a.phisp10 AS phisp10, a.ppac10 AS ppac10, a.pasi10 AS pasi10, a.fam10 AS fam10, a.hhm7514 AS hhm7514,"
			+ "a.age1810 AS age1810, a.racdiv10 AS racdiv10, a.pwhite10 AS pwhite10, a.age6410 AS age6410, a.hhpov14 AS "
			+ "hhpov14, a.femhhs10 AS femhhs10, a.hh40inc14 AS hh40inc14, a.hhchpov14 AS hhchpov14, a.hh60inc14 AS hh60inc14,"
			+ " a.hh25inc14 AS hh25inc14, a.p2more10 AS p2more10, a.age6510 AS age6510, a.hhsize10 AS hhsize10, a.age2410 "
			+ "AS age2410, a.hh75inc14 AS hh75inc14, a.age510 AS age510 FROM Area a"
			)
	List<LimitedArea> listAll(Pageable pageable);
	
	
}
