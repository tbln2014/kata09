package kata.timbahro;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Scanner;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import kata.timbahro.model.CheckOutModel;
import kata.timbahro.model.ItemDiscount;
import kata.timbahro.model.ItemIdentity;
import kata.timbahro.repository.DiscountRuleRepository;
import kata.timbahro.repository.ItemRepository;
import kata.timbahro.rules.IDiscountRule;
import kata.timbahro.rules.NoDiscountRule;
import kata.timbahro.rules.PercentageDiscountRule;
import kata.timbahro.rules.QuantitiveDiscountRule;

/**
 * Simple implementation of {@link CheckOut} using a pre-defined items-set,
 * using the default discount model.
 * 
 * Optional possibility to offer a general percentage discount.
 * 
 * @author tim.bahro
 */
public class CheckOutApplication {

	private static final String MSG_INIT_ITEMS = "Loading default items from configuration file %s.";
	private static final String MSG_INIT_DISCOUNT_RULES = "Loading default discount rules.";
	private static final String MSG_DISPLAY_TOTAL = "New Total: %s";
	private static final String MSG_SCAN_ITEM = "Please enter item-name to scan (possible items: %s):";
	private static final String MSG_CURRENTLY_AVAIL_DISCOUNTS_FOR_ITEMS = "Currently available discount options:%s%s";
	private static final String MSG_GENERAL_PERCENTAGE_DISCOUNT = "Do you want to offer a general percentage discount for all items without special discount offers? [0..100%]";
	private static final String STORE_FILE = "/default-items.json";

	public static void main(String[] args) throws JsonSyntaxException, IOException {

		println(String.format(MSG_INIT_ITEMS, STORE_FILE));

		final CheckOutModel model = new GsonBuilder().setPrettyPrinting().create().fromJson(
				IOUtils.toString(CheckOutApplication.class.getResourceAsStream(STORE_FILE), "cp1252"),
				CheckOutModel.class);
		final ItemRepository itemRepository = ItemRepository.fromModel(model);

		try (Scanner scanner = new Scanner(System.in)) {

			// Do we offer a general percentage discount? If yes, we need to initialize
			// another default discount strategy
			BigDecimal defaultPercentageDiscount = BigDecimal
					.valueOf(Double.parseDouble(prompt(scanner, MSG_GENERAL_PERCENTAGE_DISCOUNT)) / 100.0);
			IDiscountRule defaultDiscountRule = BigDecimal.ZERO.equals(defaultPercentageDiscount) ? new NoDiscountRule()
					: new PercentageDiscountRule(defaultPercentageDiscount);

			println(MSG_INIT_DISCOUNT_RULES);

			final DiscountRuleRepository discountRules = initDiscounts(model, defaultDiscountRule);
			println(String.format(MSG_CURRENTLY_AVAIL_DISCOUNTS_FOR_ITEMS, System.lineSeparator(),
					String.valueOf(discountRules)));

			final CheckOut checkOut = new CheckOut(itemRepository, discountRules);

			boolean continueUserPrompt = true;
			while (continueUserPrompt) {
				String itemName = prompt(scanner,
						String.format(MSG_SCAN_ITEM, StringUtils.join(model.getKnownItems(), ", ")));

				continueUserPrompt = StringUtils.isNotBlank(itemName);
				checkOut.scan(ItemIdentity.of(itemName));

				println(String.format(MSG_DISPLAY_TOTAL, String.valueOf(checkOut.getTotal())));
			}
		}
	}

	private static DiscountRuleRepository initDiscounts(final CheckOutModel model, IDiscountRule defaultDiscountRule) {
		final DiscountRuleRepository discountRules = new DiscountRuleRepository(defaultDiscountRule);
		discountRules.save(ItemDiscount.builder().item(model.getItemIdentityByName("A"))
				.discount(new QuantitiveDiscountRule(3, true, BigDecimal.valueOf(20.0))).build());
		discountRules.save(ItemDiscount.builder().item(model.getItemIdentityByName("B"))
				.discount(new QuantitiveDiscountRule(2, true, BigDecimal.valueOf(15.0))).build());
		return discountRules;
	}

	private static String prompt(Scanner scanner, String promptText) {
		println(promptText);
		return scanner.nextLine();
	}

	private static void println(String text) {
		System.out.println(text);
	}

}
