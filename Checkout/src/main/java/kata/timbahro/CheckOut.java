package kata.timbahro;

import java.math.BigDecimal;
import java.util.Optional;

import kata.timbahro.model.Item;
import kata.timbahro.model.ItemIdentity;
import kata.timbahro.repository.DiscountRuleRepository;
import kata.timbahro.repository.ItemRepository;

/**
 * Main class for calculating total (including optional discout-rates).
 * 
 * @author tbahro
 */
public class CheckOut {

	private ItemRepository items;
	private DiscountRuleRepository discounts;

	private BigDecimal total = BigDecimal.ZERO;
	private BigDecimal discount = BigDecimal.ZERO;

	public CheckOut(ItemRepository items, DiscountRuleRepository discounts) {
		this.items = items;
		this.discounts = discounts;
	}

	public void scan(ItemIdentity item) {
		Optional<Item> scannedRequested = items.findById(item);
		if (scannedRequested.isPresent()) {
			Item scannedItem = scannedRequested.get();
			total = total.add(scannedItem.getPrice());
			discount = discount.add(discounts.getDiscountForItem(scannedItem));
		}
	}

	public BigDecimal getTotal() {
		return total.subtract(discount);
	}

}
