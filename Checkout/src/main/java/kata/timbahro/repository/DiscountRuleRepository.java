package kata.timbahro.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import kata.timbahro.model.Item;
import kata.timbahro.model.ItemDiscount;
import kata.timbahro.model.ItemIdentity;
import kata.timbahro.rules.IDiscountRule;
import kata.timbahro.rules.IDiscountRuleRepositoryAware;

public class DiscountRuleRepository {

	private static final String MSG_SINGLE_DISCOUNT = "Active discount for item %s (discount option: %s)";
	private static final String MSG_DEFAULT_DISCOUNT_AVAILABLE = "Default discount option currently avaiable for all items without special discout offers!%s%s%s";
	private static final String ERR_ONLY_ONE_DISCOUNT_PER_ITEM = "Only one discount-option per item allowed.";
	private transient Map<ItemIdentity, AtomicInteger> itemsCounted = new HashMap<>();
	private List<ItemDiscount> activeDiscounts = new ArrayList<>();
	private IDiscountRule defaultRule;

	public DiscountRuleRepository(IDiscountRule defaultRule) {
		this.defaultRule = defaultRule;
	}

	public BigDecimal getDiscountForItem(Item item) {
		incrementScannedItems(item.getName());

		Optional<ItemDiscount> discountOption = activeDiscounts.stream().filter(d -> d.getItem().equals(item.getName()))
				.findFirst();
		return discountOption.isPresent() ? discountOption.get().getDiscount().getDiscount(item)
				: defaultRule.getDiscount(item);
	}

	public void save(ItemDiscount discountItem) {
		Validate.isTrue(!activeDiscounts.contains(discountItem), ERR_ONLY_ONE_DISCOUNT_PER_ITEM);
		if (discountItem.getDiscount() instanceof IDiscountRuleRepositoryAware) {
			((IDiscountRuleRepositoryAware) discountItem.getDiscount()).setDiscountRuleRepository(this);
		}
		activeDiscounts.add(discountItem);
	}

	public int getItemCount(ItemIdentity item) {
		return itemsCounted.get(item).intValue();
	}

	private void incrementScannedItems(ItemIdentity scannedItem) {
		itemsCounted.compute(scannedItem,
				(key, value) -> value == null ? new AtomicInteger(1) : new AtomicInteger(value.addAndGet(1)));
	}

	@Override
	public String toString() {
		StringBuffer toString = new StringBuffer();
		String defaultDiscount = defaultRule.getDescription();
		if (StringUtils.isNotBlank(defaultDiscount)) {
			toString.append(String.format(MSG_DEFAULT_DISCOUNT_AVAILABLE, System.lineSeparator(),
					String.valueOf(defaultDiscount), System.lineSeparator()));
		}
		activeDiscounts.stream()
				.forEach(d -> toString.append(
						String.format(MSG_SINGLE_DISCOUNT, d.getItem().getName(), d.getDiscount().getDescription())
								+ System.lineSeparator()));
		return toString.toString();
	}

}