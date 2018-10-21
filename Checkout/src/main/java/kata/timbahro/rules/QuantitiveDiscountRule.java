package kata.timbahro.rules;

import java.math.BigDecimal;

import kata.timbahro.model.Item;
import kata.timbahro.model.ItemIdentity;
import kata.timbahro.repository.DiscountRuleRepository;
import kata.timbahro.repository.IDiscountRuleRepository;

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
public class QuantitiveDiscountRule implements IDiscountRule, IDiscountRuleRepositoryAware {

	private int expectedItemCount;
	private BigDecimal discount;
	private boolean singleDiscount;
	private IDiscountRuleRepository ruleRepository;

	public QuantitiveDiscountRule(int expectedItemCount, boolean singleDiscount, BigDecimal discount) {
		this.expectedItemCount = expectedItemCount;
		this.singleDiscount = singleDiscount;
		this.discount = discount;
	}

	@Override
	public BigDecimal getDiscount(Item scannedItem) {
		int scannedItemCounter = ruleRepository.getItemCount(scannedItem.getName());
		int discountCounter = singleDiscount ? 1 : scannedItemCounter / expectedItemCount;
		return scannedItemCounter % expectedItemCount == 0
				? BigDecimal.valueOf(discountCounter * discount.doubleValue())
				: BigDecimal.ZERO;
	}

	@Override
	public void setDiscountRuleRepository(IDiscountRuleRepository ruleRepository) {
		this.ruleRepository = ruleRepository;
	}

}
