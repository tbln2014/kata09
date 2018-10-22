package kata.timbahro.rules;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import kata.timbahro.model.Item;
import kata.timbahro.model.ItemIdentity;
import kata.timbahro.repository.DiscountRuleRepository;

@RunWith(MockitoJUnitRunner.class)
public class QuantitiveDiscountRuleTest {

	private static final int EXPECTED_ITEM_COUNT_FOR_DISCOUNT = 3;

	private static final ItemIdentity TEST_ITEM = ItemIdentity.of("Test-Item");

	private static final boolean WITH_REPEATATIVE_DISCOUNT = false;
	private static final boolean NO_REPEATATIVE_DISCOUNT = true;

	@Rule
	public ExpectedException expect = ExpectedException.none();

	@Mock
	private DiscountRuleRepository repositoryMock;

	private Item testItem = Item.builder().price(BigDecimal.valueOf(100.0)).name(TEST_ITEM).build();

	@Test
	public void testQuantitativeDiscount_verifiesCorrectUsage_throwsError() {

		// prepare
		expect.expectMessage(QuantitiveDiscountRule.ERR_ITEM_WAS_NOT_SCANNED);

		QuantitiveDiscountRule classUnderTest = new QuantitiveDiscountRule(3, NO_REPEATATIVE_DISCOUNT,
				BigDecimal.valueOf(10.0));
		classUnderTest.setDiscountRuleRepository(repositoryMock);

		classUnderTest.getDiscount(testItem);
	}

	@Test
	public void testQuantitativeDiscount_noRepetitiveDiscount_expectedItemAmountNotReached_noDiscount() {

		// prepare
		QuantitiveDiscountRule classUnderTest = new QuantitiveDiscountRule(3, NO_REPEATATIVE_DISCOUNT,
				BigDecimal.valueOf(10.0));
		classUnderTest.setDiscountRuleRepository(repositoryMock);

		// mock correct call hierarchy - item has to be scanned before discount can be
		// calculated
		when(repositoryMock.getItemCount(eq(TEST_ITEM))).thenReturn(Integer.valueOf(1));

		// test & verify
		assertEquals(BigDecimal.valueOf(0),
				classUnderTest.getDiscount(Item.builder().price(BigDecimal.valueOf(100.0)).name(TEST_ITEM).build()));
	}

	@Test
	public void testQuantitativeDiscount_noRepetitiveDiscount_expectedItemAmountReached_discount() {

		// prepare
		QuantitiveDiscountRule classUnderTest = new QuantitiveDiscountRule(3, NO_REPEATATIVE_DISCOUNT,
				BigDecimal.valueOf(10.0));
		classUnderTest.setDiscountRuleRepository(repositoryMock);

		// mock correct call hierarchy - item has to be scanned before discount can be
		// calculated
		when(repositoryMock.getItemCount(eq(TEST_ITEM))).thenReturn(Integer.valueOf(3));

		// test & verify
		assertEquals(BigDecimal.valueOf(10.0),
				classUnderTest.getDiscount(Item.builder().price(BigDecimal.valueOf(100.0)).name(TEST_ITEM).build()));
	}

	/**
	 * Verifies that (if enabled) the repetivive discount amount is calculated.
	 * 
	 * The discount amount is added, each time the item is scanned the expected
	 * times of occurance for this discount option.
	 */
	@Test
	public void testQuantitativeDiscount_withRepetitiveDiscount_expectedItemAmountReached_discount() {

		// prepare
		QuantitiveDiscountRule classUnderTest = new QuantitiveDiscountRule(EXPECTED_ITEM_COUNT_FOR_DISCOUNT,
				WITH_REPEATATIVE_DISCOUNT, BigDecimal.valueOf(10.0));
		classUnderTest.setDiscountRuleRepository(repositoryMock);

		// mock correct call hierarchy - item has to be scanned before discount can be
		// calculated
		when(repositoryMock.getItemCount(eq(TEST_ITEM))).thenReturn(Integer.valueOf(6));

		// test & verify
		assertEquals(BigDecimal.valueOf(20.0),
				classUnderTest.getDiscount(Item.builder().price(BigDecimal.valueOf(100.0)).name(TEST_ITEM).build()));
	}

}