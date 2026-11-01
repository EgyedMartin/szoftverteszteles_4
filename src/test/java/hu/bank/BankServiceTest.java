package hu.bank;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * A BankService tesztjei. A NotificationService-t mock objektum helyettesíti,
 * így a tesztek nem használnak valódi értesítési szolgáltatást.
 */
class BankServiceTest {

    private static final double DELTA = 1e-9;

    private NotificationService notificationService;
    private BankService bankService;
    private BankAccount account;

    @BeforeEach
    void setUp() {
        notificationService = mock(NotificationService.class);
        bankService = new BankService(notificationService);
        account = new BankAccount();
    }

    @Test
    @DisplayName("1. A befizetés megfelelően növeli a számla egyenlegét")
    void depositIncreasesBalance() {
        bankService.deposit(account, 1000);

        assertEquals(1000.0, account.getBalance(), DELTA);
    }

    @Test
    @DisplayName("2. Sikeres befizetés után meghívódik a send() metódus")
    void depositSendsNotification() {
        bankService.deposit(account, 1000);

        verify(notificationService).send(anyString());
    }

    @Test
    @DisplayName("3. verify(): pontosan egyszer, a megfelelő üzenettel küld értesítést")
    void depositSendsCorrectNotificationOnce() {
        bankService.deposit(account, 1000);

        verify(notificationService, times(1)).send("Deposit successful");
        verifyNoMoreInteractions(notificationService);
        assertEquals(1000.0, account.getBalance(), DELTA);
    }

    @Test
    @DisplayName("3b. Minden sikeres befizetés külön értesítést küld")
    void eachDepositSendsNotification() {
        bankService.deposit(account, 1000);
        bankService.deposit(account, 500);

        verify(notificationService, times(2)).send("Deposit successful");
        assertEquals(1500.0, account.getBalance(), DELTA);
    }

    @Test
    @DisplayName("4. Negatív befizetésnél nincs értesítés, az egyenleg nem változik")
    void negativeDepositSendsNoNotification() {
        assertThrows(IllegalArgumentException.class, () -> bankService.deposit(account, -100));

        verify(notificationService, never()).send(anyString());
        assertEquals(0.0, account.getBalance(), DELTA);
    }

    @Test
    @DisplayName("4b. 0 összegű befizetésnél nincs értesítés, az egyenleg nem változik")
    void zeroDepositSendsNoNotification() {
        assertThrows(IllegalArgumentException.class, () -> bankService.deposit(account, 0));

        verifyNoInteractions(notificationService);
        assertEquals(0.0, account.getBalance(), DELTA);
    }

    @Test
    @DisplayName("4c. Sikeres, majd sikertelen befizetés: csak egy értesítés megy ki")
    void failedDepositAfterSuccessfulOneSendsOnlyOneNotification() {
        bankService.deposit(account, 1000);
        assertThrows(IllegalArgumentException.class, () -> bankService.deposit(account, -50));

        verify(notificationService, times(1)).send("Deposit successful");
        assertEquals(1000.0, account.getBalance(), DELTA);
    }

    @Test
    @DisplayName("null NotificationService nem adható át")
    void constructorRejectsNull() {
        assertThrows(IllegalArgumentException.class, () -> new BankService(null));
    }
}
