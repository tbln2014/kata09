package kata.timbahro.rules;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

import kata.timbahro.model.Item;
import kata.timbahro.model.ItemIdentity;

public class PercentageDiscountRuleTest {

	@Test
	public void testPercentage() {

		// prepare
		PercentageDiscountRule classUnderTest = new PercentageDiscountRule(BigDecimal.valueOf(0.1));

		// test & verify
		assertEquals(BigDecimal.valueOf(10.0), classUnderTest.getDiscount(
				Item.builder().price(BigDecimal.valueOf(100.0)).name(ItemIdentity.of("Test-Item")).build()));
	}

}
