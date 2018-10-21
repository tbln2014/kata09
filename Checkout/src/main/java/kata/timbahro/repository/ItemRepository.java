package kata.timbahro.repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.Validate;

import kata.timbahro.model.Item;
import kata.timbahro.model.ItemIdentity;

public class ItemRepository /** implements CrudRepository<Item, ItemIdentity> */
{

	private static final String ERR_UNIT_ALREADY_IN_STORAGE = "Given item (%s) can't be added to item storage because it is already present.";
	private Map<ItemIdentity, Item> itemStorage = new ConcurrentHashMap<ItemIdentity, Item>();

	public Optional<Item> findById(ItemIdentity id) {
		return Optional.ofNullable(itemStorage.get(id));
	}

	public <S extends Item> S save(S entity) {
		ItemIdentity key = entity.getName();
		Validate.isTrue(!existsById(key), String.format(ERR_UNIT_ALREADY_IN_STORAGE, key));
		itemStorage.put(key, entity);
		return entity;
	}

	public boolean existsById(ItemIdentity id) {
		return null != itemStorage.get(id);
	}

}
