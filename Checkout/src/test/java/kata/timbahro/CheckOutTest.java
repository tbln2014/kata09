package kata.timbahro;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import kata.timbahro.model.CheckOutModel;
import kata.timbahro.model.ItemDiscount;
import kata.timbahro.model.ItemIdentity;
import kata.timbahro.repository.DiscountRuleRepository;
import kata.timbahro.repository.ItemRepository;
import kata.timbahro.rules.NoDiscountRule;
import kata.timbahro.rules.PercentageDiscountRule;
import kata.timbahro.rules.QuantitiveDiscountRule;

/**
 * @scope: Component Test
 * @author tbahro
 */
public class CheckOutTest {

	private final ItemIdentity itemA = ItemIdentity.of("A");
	private final ItemIdentity itemB = ItemIdentity.of("B");
	private ItemRepository itemRepository;

	@Before
	public void setup() throws JsonSyntaxException, IOException {
		itemRepository = ItemRepository.fromModel(new GsonBuilder().setPrettyPrinting().create().fromJson(
				IOUtils.toString(this.getClass().getResourceAsStream("/default-items.json"), "cp1252"),
				CheckOutModel.class));
	}

	@Test
	public void test_totals() {

		// test & verify
		assertEquals(BigDecimal.valueOf(0), price(""));
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

	@Test
	public void test_incremental() {

		// prepare
		DiscountRuleRepository discountRules = new DiscountRuleRepository(new NoDiscountRule());
		discountRules.save(ItemDiscount.builder().item(itemA)
				.discount(new QuantitiveDiscountRule(3, true, BigDecimal.valueOf(20.0))).build());
		discountRules.save(ItemDiscount.builder().item(itemB)
				.discount(new QuantitiveDiscountRule(2, true, BigDecimal.valueOf(15.0))).build());

		final CheckOut classUnderTest = new CheckOut(itemRepository, discountRules);

		// test & verify
		assertEquals(BigDecimal.valueOf(0), classUnderTest.getTotal());
		classUnderTest.scan(itemA);
		assertEquals(BigDecimal.valueOf(50.0), classUnderTest.getTotal());
		classUnderTest.scan(itemB);
		assertEquals(BigDecimal.valueOf(80.0), classUnderTest.getTotal());
	}

	@Test
	public void test_defaultPercentageRule() {

		// prepare
		DiscountRuleRepository discountRules = new DiscountRuleRepository(
				new PercentageDiscountRule(BigDecimal.valueOf(0.2)));

		final CheckOut classUnderTest = new CheckOut(itemRepository, discountRules);

		// test & verify
		classUnderTest.scan(itemA);
		assertEquals(BigDecimal.valueOf(50.0 * 0.8), classUnderTest.getTotal());
		classUnderTest.scan(itemB);
		assertEquals(BigDecimal.valueOf(80.0 * 0.8), classUnderTest.getTotal());
	}

	// -- test methods --

	private Object price(String toScan) {

		DiscountRuleRepository discountRules = new DiscountRuleRepository(new NoDiscountRule());
		discountRules.save(ItemDiscount.builder().item(itemA)
				.discount(new QuantitiveDiscountRule(3, true, BigDecimal.valueOf(20.0))).build());
		discountRules.save(ItemDiscount.builder().item(itemB)
				.discount(new QuantitiveDiscountRule(2, true, BigDecimal.valueOf(15.0))).build());

		final CheckOut classUnderTest = new CheckOut(itemRepository, discountRules);
		Arrays.stream(toScan.split("")).forEach((c) -> classUnderTest.scan(ItemIdentity.of(c)));
		return classUnderTest.getTotal();
	}

}
