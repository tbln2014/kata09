package kata.timbahro.repository;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.Validate;

import kata.timbahro.model.Item;
import kata.timbahro.model.ItemIdentity;

public class DiscountRepository {

	private static final String ERR_ONLY_ONE_DISCOUNT_PER_ITEM = "Only one discount-option per item allowed.";
	private Map<ItemIdentity, IDiscounts> activeDiscounts = new LinkedHashMap<ItemIdentity, IDiscounts>();

	public BigDecimal getDiscountForItem(Item item) {
		IDiscounts discountOption = activeDiscounts.get(item.getName());
		discountOption.isApplicable(item.getName());
		return discountOption.getDiscount(item.getName());
	}

	public void save(ItemIdentity item, IDiscounts discountStrategyForItem) {
		Validate.isTrue(activeDiscounts.get(item) == null, ERR_ONLY_ONE_DISCOUNT_PER_ITEM);
		activeDiscounts.put(item, discountStrategyForItem);
	}

}
