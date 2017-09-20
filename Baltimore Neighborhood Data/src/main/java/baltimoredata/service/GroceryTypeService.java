package baltimoredata.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import baltimoredata.model.GroceryType;

public interface GroceryTypeService {
	public Long getGroceryTypeCount();
	public Page<GroceryType> getGroceryTypes(Pageable pageReq);
	public GroceryType getGroceryTypeById(Integer id);
	public GroceryType getGroceryTypeByName(String name);
	public GroceryType addGroceryType(GroceryType g);
	public void modifyGroceryTypeById(Integer id, GroceryType modified);
	public void modifyGroceryTypeByName(String name, GroceryType modified);
	public Long deleteGroceryTypeById(Integer id);
	public Long deleteGroceryTypeByName(String name);
}
