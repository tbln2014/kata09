package kata.timbahro.rules;

import kata.timbahro.repository.IDiscountRuleRepository;

public interface IDiscountRuleRepositoryAware {

	public void setDiscountRuleRepository(IDiscountRuleRepository ruleRepository);

}