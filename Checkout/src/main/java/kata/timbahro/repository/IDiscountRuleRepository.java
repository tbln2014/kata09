package kata.timbahro.repository;

import kata.timbahro.model.ItemIdentity;

/**
 * methods for interactions between discount rules and the discount resository
 */
public interface IDiscountRuleRepository {

	/**
	 * @return how often has the requested item been scanned
	 */
	public int getItemCount(ItemIdentity item);
}
