package hu.bank;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BankAccountTest {

    private static final double DELTA = 1e-9;

    private BankAccount account;

    @BeforeEach
    void setUp() {
        account = new BankAccount();
    }

    @Test
    @DisplayName("1. Új számla egyenlege 0")
    void newAccountHasZeroBalance() {
        assertEquals(0.0, account.getBalance(), DELTA);
    }

    @Test
    @DisplayName("2. deposit(1000) után az egyenleg 1000")
    void depositIncreasesBalance() {
        account.deposit(1000);
        assertEquals(1000.0, account.getBalance(), DELTA);
    }

    @Test
    @DisplayName("3. Több befizetés helyesen összeadódik")
    void multipleDepositsAddUp() {
        account.deposit(1000);
        account.deposit(500);
        account.deposit(250.5);
        assertEquals(1750.5, account.getBalance(), DELTA);
    }

    @Test
    @DisplayName("4. Befizetés után a withdraw() helyesen csökkenti az egyenleget")
    void withdrawDecreasesBalance() {
        account.deposit(1000);
        account.withdraw(300);
        assertEquals(700.0, account.getBalance(), DELTA);
    }

    @Test
    @DisplayName("5. A teljes egyenleg felvehető")
    void entireBalanceCanBeWithdrawn() {
        account.deposit(1000);
        account.withdraw(1000);
        assertEquals(0.0, account.getBalance(), DELTA);
    }

    @Test
    @DisplayName("6. Túl nagy összeg felvétele kivételt dob, az egyenleg nem változik")
    void withdrawMoreThanBalanceThrows() {
        account.deposit(1000);
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(1000.01));
        assertEquals(1000.0, account.getBalance(), DELTA);
    }

    @Test
    @DisplayName("6b. Üres számláról történő felvétel kivételt dob, az egyenleg 0 marad")
    void withdrawFromEmptyAccountThrows() {
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(1));
        assertEquals(0.0, account.getBalance(), DELTA);
    }

    @Test
    @DisplayName("7. Negatív befizetés kivételt dob, az egyenleg nem változik")
    void negativeDepositThrows() {
        account.deposit(500);
        assertThrows(IllegalArgumentException.class, () -> account.deposit(-100));
        assertEquals(500.0, account.getBalance(), DELTA);
    }

    @Test
    @DisplayName("8. 0 összegű befizetés kivételt dob, az egyenleg nem változik")
    void zeroDepositThrows() {
        account.deposit(500);
        assertThrows(IllegalArgumentException.class, () -> account.deposit(0));
        assertEquals(500.0, account.getBalance(), DELTA);
    }

    @Test
    @DisplayName("9. Negatív pénzfelvétel kivételt dob, az egyenleg nem változik")
    void negativeWithdrawThrows() {
        account.deposit(500);
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(-100));
        assertEquals(500.0, account.getBalance(), DELTA);
    }

    @Test
    @DisplayName("10. 0 összegű pénzfelvétel kivételt dob, az egyenleg nem változik")
    void zeroWithdrawThrows() {
        account.deposit(500);
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(0));
        assertEquals(500.0, account.getBalance(), DELTA);
    }

    @Test
    @DisplayName("Sikertelen műveletek sorozata után az egyenleg változatlan")
    void failedOperationsDoNotChangeBalance() {
        account.deposit(1000);
        account.withdraw(200);

        assertThrows(IllegalArgumentException.class, () -> account.deposit(-1));
        assertThrows(IllegalArgumentException.class, () -> account.deposit(0));
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(-1));
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(0));
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(10_000));
        assertThrows(IllegalArgumentException.class, () -> account.deposit(Double.NaN));

        assertEquals(800.0, account.getBalance(), DELTA);
    }
}
