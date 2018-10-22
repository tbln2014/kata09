package kata.timbahro.util;

public class CurrencyFormatter {

	private static String CURRENCY_CODE = "EUR";

	public static String formatWithCurrency(double value) {
		return String.format("%.2f " + CURRENCY_CODE, value);
	}

}
