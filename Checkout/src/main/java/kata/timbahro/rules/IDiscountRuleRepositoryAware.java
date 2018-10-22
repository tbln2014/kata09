package kata.timbahro.rules;

import kata.timbahro.repository.DiscountRuleRepository;

public interface IDiscountRuleRepositoryAware {

	/**
	 * inject dependecy for rules, that require access to
	 * {@link DiscountRuleRepository} information.
	 */
	public void setDiscountRuleRepository(DiscountRuleRepository ruleRepository);

}