package kata.timbahro.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class ItemIdentity {

	public String name;

	public static ItemIdentity of(String name) {
		return new ItemIdentity(name);
	}

	@Override
	public String toString() {
		return getName();
	}

}
