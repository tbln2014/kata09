package kata.timbahro.model;

import java.util.List;

import lombok.Data;

/**
 * Used for serializing/deserializing items which can be scanned in store.
 * 
 * @author tbahro
 */

@Data
public class CheckOutModel {

	private List<ItemIdentity> knownItems;
	private List<Item> cashableItems;

	public ItemIdentity getItemIdentityByName(String name) {
		return knownItems.stream().filter(i -> i.getName().equals(name)).findFirst().get();
	}

}
