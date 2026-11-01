package hu.bank;

/**
 * Banki műveleteket végző szolgáltatás.
 * Az értesítési szolgáltatást konstruktoron keresztül kapja (Dependency Injection).
 */
public class BankService {

    public static final String DEPOSIT_MESSAGE = "Deposit successful";

    private final NotificationService notificationService;

    public BankService(NotificationService notificationService) {
        if (notificationService == null) {
            throw new IllegalArgumentException("A NotificationService nem lehet null.");
        }
        this.notificationService = notificationService;
    }

    /**
     * Befizetést hajt végre, majd sikeres befizetés esetén értesítést küld.
     * Ha a befizetés sikertelen (IllegalArgumentException), értesítés nem megy ki.
     */
    public void deposit(BankAccount account, double amount) {
        account.deposit(amount);
        notificationService.send(DEPOSIT_MESSAGE);
    }
}
