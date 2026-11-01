package hu.bank;

/**
 * Egyszerű bankszámla modell. Az induló egyenleg 0.
 */
public class BankAccount {

    private double balance;

    public BankAccount() {
        this.balance = 0.0;
    }

    /**
     * Befizetés a számlára.
     *
     * @param amount a befizetendő összeg (pozitív kell legyen)
     * @throws IllegalArgumentException ha az összeg nem pozitív
     */
    public void deposit(double amount) {
        validatePositive(amount, "A befizetett összegnek pozitívnak kell lennie.");
        balance += amount;
    }

    /**
     * Pénzfelvétel a számláról.
     *
     * @param amount a felvenni kívánt összeg (pozitív, legfeljebb az egyenleg)
     * @throws IllegalArgumentException ha az összeg nem pozitív, vagy nagyobb az egyenlegnél
     */
    public void withdraw(double amount) {
        validatePositive(amount, "A felvett összegnek pozitívnak kell lennie.");
        if (amount > balance) {
            throw new IllegalArgumentException("Nincs elegendő fedezet a számlán.");
        }
        balance -= amount;
    }

    /**
     * @return az aktuális egyenleg
     */
    public double getBalance() {
        return balance;
    }

    private static void validatePositive(double amount, String message) {
        // A !(amount > 0) feltétel a NaN értéket is elutasítja.
        if (!(amount > 0) || Double.isInfinite(amount)) {
            throw new IllegalArgumentException(message);
        }
    }
}
