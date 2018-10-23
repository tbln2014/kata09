package kata.timbahro;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
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

	private static final String SEPR = StringUtils.repeat('-', 91);
	private static final String NEWLINE = System.lineSeparator();
	private static final String FILE_ENCODING = "cp1252";
	private static final String SYSTEM_ENCODING = Charset.defaultCharset().name();
	private static final String MSG_INIT_ITEMS = "Loading default items from configuration file %s.";
	private static final String MSG_INIT_DISCOUNT_RULES = "Loading default discount rules." + NEWLINE;
	private static final String MSG_DISPLAY_TOTAL = SEPR + NEWLINE + "NEW TOTAL (INCLUDING %s DISCOUNT): %s" + NEWLINE
			+ SEPR;
	private static final String MSG_SCAN_ITEM = "Please enter item-name to scan (possible items: %s):";
	private static final String MSG_CURRENTLY_AVAIL_DISCOUNTS_FOR_ITEMS = "Currently available discount options:%s%s";
	private static final String MSG_GENERAL_PERCENTAGE_DISCOUNT = NEWLINE + SEPR + NEWLINE
			+ "Do you want to offer a general percentage discount for all items without special discount offers? [0..100%]";
	private static final String STORE_FILE = "/default-items.json";

	public static void main(String[] args) throws JsonSyntaxException, IOException {

		println(String.format(MSG_INIT_ITEMS, STORE_FILE));

		// Load store items/prices from json configuration file
		final CheckOutModel model = new GsonBuilder().setPrettyPrinting().create().fromJson(
				IOUtils.toString(CheckOutApplication.class.getResourceAsStream(STORE_FILE), FILE_ENCODING),
				CheckOutModel.class);
		final ItemRepository itemRepository = ItemRepository.fromModel(model);

		try (Scanner scanner = new Scanner(System.in, SYSTEM_ENCODING)) {
			// Do we offer a general percentage discount? If yes, we need to initialize
			// another default discount strategy
			BigDecimal defaultPercentageDiscount = BigDecimal
					.valueOf(Double.parseDouble(prompt(scanner, MSG_GENERAL_PERCENTAGE_DISCOUNT)) / 100.0);
			IDiscountRule defaultDiscountRule = initDefaultDiscount(defaultPercentageDiscount);

			println(MSG_INIT_DISCOUNT_RULES);

			final DiscountRuleRepository discountRules = initDiscounts(model, defaultDiscountRule);
			println(String.format(MSG_CURRENTLY_AVAIL_DISCOUNTS_FOR_ITEMS, NEWLINE, String.valueOf(discountRules)));

			// continuously prompt items-names to be scanned until user returns empty
			// item-name
			final CheckOut checkOut = new CheckOut(itemRepository, discountRules);
			boolean continueUserPrompt = true;
			while (continueUserPrompt) {
				String itemName = prompt(scanner,
						String.format(MSG_SCAN_ITEM, StringUtils.join(model.getKnownItems(), ", ")));

				continueUserPrompt = StringUtils.isNotBlank(itemName);
				checkOut.scan(ItemIdentity.of(itemName));

				println(String.format(MSG_DISPLAY_TOTAL, checkOut.getDiscountWithCurrency(),
						checkOut.getTotalWithCurrency()));
			}
		}
	}

	private static IDiscountRule initDefaultDiscount(BigDecimal defaultPercentageDiscount) {
		IDiscountRule defaultDiscountRule = defaultPercentageDiscount.compareTo(BigDecimal.ZERO) == 0
				? new NoDiscountRule()
				: new PercentageDiscountRule(defaultPercentageDiscount);
		return defaultDiscountRule;
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
