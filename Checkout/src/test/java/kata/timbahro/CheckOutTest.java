package kata.timbahro;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.Test;

import kata.timbahro.discounts.QuantitiveDiscount;
import kata.timbahro.model.Item;
import kata.timbahro.model.ItemIdentity;
import kata.timbahro.repository.DiscountRepository;
import kata.timbahro.repository.ItemRepository;

/**
 * @scope: Component Test
 * @author tbahro
 */
public class CheckOutTest {

	@Test
	public void test_totals_defaultPricingRules() {

		// test & verify
//		assertEquals(BigDecimal.valueOf(0), price(""));
		assertEquals(BigDecimal.valueOf(50.0), price("A"));
		assertEquals(BigDecimal.valueOf(80.0), price("AB"));
		assertEquals(BigDecimal.valueOf(115.0), price("CDBA"));

		assertEquals(BigDecimal.valueOf(100.0), price("AA"));
		assertEquals(BigDecimal.valueOf(130.0), price("AAA"));
		assertEquals(BigDecimal.valueOf(180.0), price("AAAA"));
		assertEquals(BigDecimal.valueOf(230.0), price("AAAAA"));
		assertEquals(BigDecimal.valueOf(260.0), price("AAAAAA"));

		assertEquals(BigDecimal.valueOf(160.0), price("AAAB"));
		assertEquals(BigDecimal.valueOf(175.0), price("AAABB"));
		assertEquals(BigDecimal.valueOf(190.0), price("AAABBD"));
		assertEquals(BigDecimal.valueOf(190.0), price("DABABA"));
	}

	// -- test methods --

	private Object price(String toScan) {
		ItemIdentity itemA = ItemIdentity.of("A");
		ItemIdentity itemB = ItemIdentity.of("B");
		ItemIdentity itemC = ItemIdentity.of("C");
		ItemIdentity itemD = ItemIdentity.of("D");

		ItemRepository itemRepository = new ItemRepository();
		itemRepository.save(Item.builder().name(itemA).price(BigDecimal.valueOf(50.0)).build());
		itemRepository.save(Item.builder().name(itemB).price(BigDecimal.valueOf(30.0)).build());
		itemRepository.save(Item.builder().name(itemC).price(BigDecimal.valueOf(20.0)).build());
		itemRepository.save(Item.builder().name(itemD).price(BigDecimal.valueOf(15.0)).build());

		DiscountRepository discountRules = new DiscountRepository();
		discountRules.save(itemA, new QuantitiveDiscount(3, BigDecimal.valueOf(20.0)));

		final CheckOut classUnderTest = new CheckOut(itemRepository, discountRules);
		Arrays.stream(toScan.split("")).forEach((c) -> classUnderTest.scan(ItemIdentity.of(c)));
		return classUnderTest.getTotal();
	}

}
