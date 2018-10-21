package kata.timbahro.discounts;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import kata.timbahro.model.ItemIdentity;
import kata.timbahro.repository.IDiscounts;

public class QuantitiveDiscount implements IDiscounts {

	private Map<ItemIdentity, Integer> itemsCounted = new HashMap<>();
	private int expectedItemCount;
	private BigDecimal discount;

	public QuantitiveDiscount(int expectedItemCount, BigDecimal discount) {
		this.expectedItemCount = expectedItemCount;
		this.discount = discount;
	}

	@Override
	public BigDecimal getDiscount(ItemIdentity scannedItem) {
		int scannedItemCounter = itemsCounted.get(scannedItem).intValue();
		return scannedItemCounter % expectedItemCount == 0 ? BigDecimal.valueOf(discount.doubleValue())
				: BigDecimal.ZERO;
	}

	@Override
	public boolean isApplicable(ItemIdentity scannedItem) {
		incrementItem(scannedItem);
		return true;
	}

	private void incrementItem(ItemIdentity scannedItem) {
		Integer counter = itemsCounted.putIfAbsent(scannedItem, Integer.valueOf(1));
		if (counter != null) {
			counter = Integer.valueOf(counter.intValue() + 1);
			itemsCounted.put(scannedItem, counter);
		}
	}

}
