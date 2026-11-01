package hu.bank;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Mock objektum viselkedésének beállítása when(...).thenReturn(...) segítségével.
 */
class ExchangeRateServiceTest {

    private static final double DELTA = 1e-9;

    private ExchangeRateService rateService;

    @BeforeEach
    void setUp() {
        rateService = mock(ExchangeRateService.class);
    }

    @Test
    @DisplayName("A mock a beállított árfolyamot adja vissza")
    void mockReturnsConfiguredRate() {
        when(rateService.getRate("EUR", "HUF")).thenReturn(400.0);

        assertEquals(400.0, rateService.getRate("EUR", "HUF"), DELTA);
        verify(rateService).getRate("EUR", "HUF");
    }

    @Test
    @DisplayName("Be nem állított devizapárra a mock alapértéke 0.0")
    void unconfiguredPairReturnsDefault() {
        when(rateService.getRate("EUR", "HUF")).thenReturn(400.0);

        assertEquals(0.0, rateService.getRate("USD", "HUF"), DELTA);
    }

    @Test
    @DisplayName("Több devizapár eltérő árfolyammal állítható be")
    void multipleRatesCanBeConfigured() {
        when(rateService.getRate("EUR", "HUF")).thenReturn(400.0);
        when(rateService.getRate("USD", "HUF")).thenReturn(360.0);

        assertEquals(400.0, rateService.getRate("EUR", "HUF"), DELTA);
        assertEquals(360.0, rateService.getRate("USD", "HUF"), DELTA);
    }

    @Test
    @DisplayName("A CurrencyConverter a mockolt árfolyammal számol")
    void converterUsesMockedRate() {
        when(rateService.getRate("EUR", "HUF")).thenReturn(400.0);
        CurrencyConverter converter = new CurrencyConverter(rateService);

        double result = converter.convert(100, "EUR", "HUF");

        assertEquals(40_000.0, result, DELTA);
        verify(rateService).getRate("EUR", "HUF");
    }

    @Test
    @DisplayName("Érvénytelen összegnél nem kérdezi le az árfolyamot")
    void invalidAmountDoesNotQueryRate() {
        CurrencyConverter converter = new CurrencyConverter(rateService);

        assertThrows(IllegalArgumentException.class, () -> converter.convert(-5, "EUR", "HUF"));

        verify(rateService, never()).getRate(anyString(), anyString());
    }
}
