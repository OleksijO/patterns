package coffee_machine.dao;

import coffee_machine.model.entity.goods.Addon;

import java.util.List;

public interface AddonDao extends GenericDao<Addon> {

	List<Addon> getAllFromList(List<Addon> addons);

	void updateQuantityAllInList(List<Addon> addons);

	List<Addon> getAllByIds(List<Integer> addonIds);

	void updateQuantity(Addon addon);
}
