package hu.bank;

/**
 * Külső értesítési szolgáltatás (pl. e-mail, SMS).
 * A tesztekben Mockito mock objektummal helyettesítjük.
 */
public interface NotificationService {

    void send(String message);
}
