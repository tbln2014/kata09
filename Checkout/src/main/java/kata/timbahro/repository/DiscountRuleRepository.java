package kata.timbahro.repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.Validate;

import kata.timbahro.model.Item;
import kata.timbahro.model.ItemIdentity;
import kata.timbahro.rules.IDiscountRule;
import kata.timbahro.rules.IDiscountRuleRepositoryAware;

public class DiscountRuleRepository {

	private static final String ERR_ONLY_ONE_DISCOUNT_PER_ITEM = "Only one discount-option per item allowed.";
	private Map<ItemIdentity, IDiscountRule> activeDiscounts = new LinkedHashMap<>();
	private Map<ItemIdentity, AtomicInteger> itemsCounted = new HashMap<>();
	private IDiscountRule defaultRule;

	public DiscountRuleRepository(IDiscountRule defaultRule) {
		this.defaultRule = defaultRule;
	}

	public BigDecimal getDiscountForItem(Item item) {
		incrementScannedItems(item.getName());

		IDiscountRule discountOption = activeDiscounts.get(item.getName());
		return discountOption == null ? defaultRule.getDiscount(item) : discountOption.getDiscount(item);
	}

	public void save(ItemIdentity item, IDiscountRule discountStrategyForItem) {
		Validate.isTrue(activeDiscounts.get(item) == null, ERR_ONLY_ONE_DISCOUNT_PER_ITEM);
		if (discountStrategyForItem instanceof IDiscountRuleRepositoryAware) {
			((IDiscountRuleRepositoryAware) discountStrategyForItem).setDiscountRuleRepository(this);
		}
		activeDiscounts.put(item, discountStrategyForItem);
	}

	public int getItemCount(ItemIdentity item) {
		return itemsCounted.get(item).intValue();
	}

	private void incrementScannedItems(ItemIdentity scannedItem) {
		itemsCounted.compute(scannedItem,
				(key, value) -> value == null ? new AtomicInteger(1) : new AtomicInteger(value.addAndGet(1)));
	}

}