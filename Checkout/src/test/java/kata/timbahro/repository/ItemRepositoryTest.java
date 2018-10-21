package kata.timbahro.repository;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

import kata.timbahro.model.Item;
import kata.timbahro.model.ItemIdentity;

/**
 * @scope: Component Test
 * @author tbahro
 */
public class ItemRepositoryTest {

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

}
