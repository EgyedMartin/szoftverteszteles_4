package hu.bank;

/**
 * Pénzváltó, amely az árfolyamot egy külső ExchangeRateService-től kéri le.
 */
public class CurrencyConverter {

    private final ExchangeRateService exchangeRateService;

    public CurrencyConverter(ExchangeRateService exchangeRateService) {
        if (exchangeRateService == null) {
            throw new IllegalArgumentException("Az ExchangeRateService nem lehet null.");
        }
        this.exchangeRateService = exchangeRateService;
    }

    /**
     * Átváltja az összeget a megadott devizák között.
     */
    public double convert(double amount, String from, String to) {
        if (!(amount > 0) || Double.isInfinite(amount)) {
            throw new IllegalArgumentException("Az átváltandó összegnek pozitívnak kell lennie.");
        }
        return amount * exchangeRateService.getRate(from, to);
    }
}
