package hu.bank;

/**
 * Külső árfolyam-szolgáltatás.
 * A tesztekben Mockito segítségével előre megadott árfolyamot ad vissza.
 */
public interface ExchangeRateService {

    double getRate(String from, String to);
}
