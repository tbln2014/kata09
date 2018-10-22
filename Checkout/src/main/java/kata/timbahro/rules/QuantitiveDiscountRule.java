package kata.timbahro.rules;

import java.math.BigDecimal;

import org.apache.commons.lang3.Validate;

import kata.timbahro.model.Item;
import kata.timbahro.model.ItemIdentity;
import kata.timbahro.repository.DiscountRuleRepository;
import kata.timbahro.util.CurrencyFormatter;

/**
 * Quantitative discount rule which allows to specify discounts based on the
 * total amount of the item, which was associated with this rule in the
 * {@link DiscountRuleRepository}.
 * 
 * Possible extension of this strategy could be a cross-selling discount
 * strategy (but item A and get discount for item B), which can be implemented
 * using the {@link IDiscountRuleRepository#getItemCount(ItemIdentity)} for the
 * cross selling item.
 * 
 * @author tbahro
 */
public class QuantitiveDiscountRule extends IDiscountRule implements IDiscountRuleRepositoryAware {

	public static final String ERR_ITEM_WAS_NOT_SCANNED = "No item was scanned yet. This might be caused by wrong implementation.";

	private int expectedItemCount;
	private BigDecimal discount;
	private boolean singleDiscount;
	private DiscountRuleRepository ruleRepository;

	public QuantitiveDiscountRule(int expectedItemCount, boolean singleDiscount, BigDecimal discount) {
		this.expectedItemCount = expectedItemCount;
		this.singleDiscount = singleDiscount;
		this.discount = discount;
	}

	@Override
	public BigDecimal getDiscount(Item scannedItem) {
		int scannedItemCounter = ruleRepository.getItemCount(scannedItem.getName());
		Validate.isTrue(scannedItemCounter > 0, ERR_ITEM_WAS_NOT_SCANNED);

		int discountCounter = singleDiscount ? 1 : (scannedItemCounter / expectedItemCount);
		return scannedItemCounter % expectedItemCount == 0
				? BigDecimal.valueOf(discountCounter * discount.doubleValue())
				: BigDecimal.ZERO;
	}

	@Override
	public void setDiscountRuleRepository(DiscountRuleRepository ruleRepository) {
		this.ruleRepository = ruleRepository;
	}

	@Override
	public String getDescription() {
		return String.format("discounts %s EUR for buying %s%s items",
				CurrencyFormatter.formatWithCurrency(discount.doubleValue()),
				String.valueOf(singleDiscount ? "" : "every"), String.valueOf(expectedItemCount));
	}

}