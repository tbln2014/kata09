package kata.timbahro.model;

import lombok.Value;

@Value
public class ItemIdentity {

	public String name;

	public static ItemIdentity of(String name) {
		return new ItemIdentity(name);
	}

}
