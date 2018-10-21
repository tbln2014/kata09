package kata.timbahro.rules;

import kata.timbahro.repository.DiscountRuleRepository;

public interface IDiscountRuleRepositoryAware {

	public void setDiscountRuleRepository(DiscountRuleRepository ruleRepository);

}