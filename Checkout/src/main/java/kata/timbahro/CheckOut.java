package kata.timbahro;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

import kata.timbahro.model.Item;
import kata.timbahro.model.ItemIdentity;
import kata.timbahro.repository.DiscountRepository;
import kata.timbahro.repository.ItemRepository;

public class CheckOut {

	private ItemRepository items;
	private DiscountRepository discounts;

	private BigDecimal total = BigDecimal.ZERO;
	private BigDecimal discount = BigDecimal.ZERO;

	public CheckOut(ItemRepository items, DiscountRepository discounts) {
		this.items = items;
		this.discounts = discounts;
	}

	/** throws NoSuchElementException, if item is unknown in item repository */
	public void scan(ItemIdentity unitName) throws NoSuchElementException {
		Item scannedItem = items.findById(unitName).get();
		total = total.add(scannedItem.getPrice());
		discount = discount.add(discounts.getDiscountForItem(scannedItem));
	}

	public BigDecimal getTotal() {
		return total.subtract(discount);
	}

}
