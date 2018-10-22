package kata.timbahro.repository;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.Validate;

import kata.timbahro.model.CheckOutModel;
import kata.timbahro.model.Item;
import kata.timbahro.model.ItemIdentity;

/**
 * Holds all known items, which can be scanned by the cashier to be added to
 * customers checkout bill.
 * 
 * @author tbahro
 */

/** implements CrudRepository<Item, ItemIdentity> */
public class ItemRepository {

	public static final String ERR_UNIT_ALREADY_IN_STORAGE = "Given item (%s) can't be added to item storage because it is already present.";

	private Map<ItemIdentity, Item> itemStorage = new LinkedHashMap<ItemIdentity, Item>();

	public Optional<Item> findById(ItemIdentity id) {
		return Optional.ofNullable(itemStorage.get(id));
	}

	public <S extends Item> S save(S entity) {
		ItemIdentity key = entity.getName();
		Validate.isTrue(!existsById(key), String.format(ERR_UNIT_ALREADY_IN_STORAGE, key));
		itemStorage.computeIfAbsent(key, v -> entity);
		return entity;
	}

	public boolean existsById(ItemIdentity id) {
		return null != itemStorage.get(id);
	}

	public static ItemRepository fromModel(final CheckOutModel model) {
		ItemRepository items = new ItemRepository();
		model.getCashableItems().forEach(i -> items.save(i));
		return items;
	}

}