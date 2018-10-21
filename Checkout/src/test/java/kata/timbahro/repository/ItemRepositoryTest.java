package kata.timbahro.repository;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import kata.timbahro.model.Item;
import kata.timbahro.model.ItemIdentity;

/**
 * @scope: Component Test
 * @author tbahro
 */
public class ItemRepositoryTest {

	@Rule
	public ExpectedException expect = ExpectedException.none();

	@Test
	public void testRepository_save() {

		// prepare
		ItemRepository classUnderTest = new ItemRepository();
		Item item1 = Item.builder().name(ItemIdentity.of("A")).price(BigDecimal.valueOf(10.0)).build();

		// test
		classUnderTest.save(item1);

		// verify
		assertEquals(item1, classUnderTest.findById(ItemIdentity.of("A")).get());
	}

	@Test
	public void testRepository_saveSameItemTwice_throwsError() {

		// prepare
		ItemRepository classUnderTest = new ItemRepository();
		Item item1 = Item.builder().name(ItemIdentity.of("A")).price(BigDecimal.valueOf(10.0)).build();

		expect.expectMessage(String.format(ItemRepository.ERR_UNIT_ALREADY_IN_STORAGE, item1.getName()));

		// test
		classUnderTest.save(item1);
		classUnderTest.save(item1);
	}

}
