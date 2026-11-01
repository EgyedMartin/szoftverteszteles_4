package hu.bank;

/**
 * Bemutató program a BankAccount osztály használatához.
 */
public class Main {

    public static void main(String[] args) {
        BankAccount account = new BankAccount();
        System.out.println("Induló egyenleg: " + account.getBalance());

        account.deposit(1000);
        System.out.println("Befizetés: 1000 -> egyenleg: " + account.getBalance());

        account.deposit(500);
        System.out.println("Befizetés: 500 -> egyenleg: " + account.getBalance());

        account.withdraw(300);
        System.out.println("Felvétel: 300 -> egyenleg: " + account.getBalance());

        tryOperation("Felvétel: 5000", () -> account.withdraw(5000), account);
        tryOperation("Befizetés: -100", () -> account.deposit(-100), account);
        tryOperation("Befizetés: 0", () -> account.deposit(0), account);
        tryOperation("Felvétel: 0", () -> account.withdraw(0), account);

        System.out.println("Végső egyenleg: " + account.getBalance());
    }

    private static void tryOperation(String label, Runnable operation, BankAccount account) {
        try {
            operation.run();
            System.out.println(label + " -> sikeres, egyenleg: " + account.getBalance());
        } catch (IllegalArgumentException e) {
            System.out.println(label + " -> HIBA: " + e.getMessage()
                    + " (egyenleg változatlan: " + account.getBalance() + ")");
        }
    }
}
