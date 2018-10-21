package kata.timbahro.model;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Item {

	public ItemIdentity name;
	public BigDecimal price;

}
