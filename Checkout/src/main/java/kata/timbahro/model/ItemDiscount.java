package kata.timbahro.model;

import kata.timbahro.rules.IDiscountRule;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ItemDiscount {

	private ItemIdentity item;
	private IDiscountRule discount;

}
