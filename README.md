# BankAccount – bankszámla Java nyelven, JUnit 5 és Mockito tesztekkel

Egy egyszerű bankszámlát modellező `BankAccount` osztály, kiegészítve egy értesítést küldő
`BankService`-szel és egy árfolyam-szolgáltatással. A külső függőségeket (értesítés, árfolyam)
a tesztekben **Mockito** mock objektumok helyettesítik.

## Osztályok

| Típus | Leírás |
|---|---|
| `BankAccount` | Bankszámla: `deposit()`, `withdraw()`, `getBalance()`. Induló egyenleg 0. |
| `NotificationService` (interfész) | Külső értesítési szolgáltatás: `void send(String message)`. |
| `BankService` | Befizetést hajt végre, majd értesítést küld. A `NotificationService`-t konstruktoron kapja (Dependency Injection). |
| `ExchangeRateService` (interfész) | Külső árfolyam-szolgáltatás: `double getRate(String from, String to)`. |
| `CurrencyConverter` | Pénzváltó, az árfolyamot az `ExchangeRateService`-től kéri le. |
| `Main` | Bemutató program a `BankAccount` használatához. |

### BankAccount szabályai

- Negatív vagy 0 összegű befizetés / pénzfelvétel nem engedélyezett.
- Nem lehet többet felvenni, mint az aktuális egyenleg.
- Hibás művelet esetén `IllegalArgumentException` kivétel keletkezik, és az egyenleg nem változik.

### BankService működése

```java
public void deposit(BankAccount account, double amount) {
    account.deposit(amount);                          // hiba esetén kivételt dob...
    notificationService.send("Deposit successful");   // ...így ide el sem jut
}
```

Sikertelen befizetésnél a kivétel még az értesítés előtt keletkezik, így értesítés nem megy ki.

## Projektstruktúra

```
bank-account/
├── pom.xml
├── README.md
└── src
    ├── main/java/hu/bank/
    │   ├── BankAccount.java
    │   ├── BankService.java
    │   ├── NotificationService.java
    │   ├── ExchangeRateService.java
    │   ├── CurrencyConverter.java
    │   └── Main.java
    └── test/java/hu/bank/
        ├── BankAccountTest.java
        ├── BankServiceTest.java
        └── ExchangeRateServiceTest.java
```

## Követelmények

- Java 17 vagy újabb (JDK)
- Apache Maven 3.8+

Függőségek (a `pom.xml`-ben, a Maven automatikusan letölti):

- JUnit 5 (`org.junit.jupiter:junit-jupiter:5.11.3`)
- Mockito (`org.mockito:mockito-junit-jupiter:5.14.2`)

## Tesztek futtatása

```bash
mvn clean test
```

Sikeres futás esetén a kimenet végén ez látható:

```
Tests run: 25, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

Csak egy tesztosztály futtatása:

```bash
mvn test -Dtest=BankServiceTest
mvn test -Dtest=ExchangeRateServiceTest
```

IDE-ből (IntelliJ IDEA, Eclipse, VS Code) a projekt Maven-projektként megnyitható; a tesztek
a tesztosztályokon jobb klikk → *Run* paranccsal futtathatók.

> Java 21+ alatt a Mockito figyelmeztetést írhat ki a dinamikus agent-betöltésről
> („Mockito is currently self-attaching…”). Ez csak figyelmeztetés, a tesztek ettől sikeresek.

## Bemutató program futtatása

```bash
mvn compile
java -cp target/classes hu.bank.Main
```

## Lefedett tesztesetek

### BankAccountTest (JUnit 5, 12 teszt)

1. Új számla egyenlege 0.
2. `deposit(1000)` után az egyenleg 1000.
3. Több befizetés helyesen összeadódik.
4. Befizetés után a `withdraw()` helyesen csökkenti az egyenleget.
5. A teljes egyenleg felvehető.
6. Túl nagy összeg felvétele `IllegalArgumentException` kivételt dob (üres számláról is).
7–10. Negatív / 0 összegű befizetés és pénzfelvétel kivételt dob.

Minden hibás esetben ellenőrzött, hogy az egyenleg változatlan marad.

### BankServiceTest (Mockito, 8 teszt)

A `NotificationService` helyett `mock(NotificationService.class)` szerepel, így a tesztek nem
használnak valódi értesítési szolgáltatást.

1. A befizetés megfelelően növeli a számla egyenlegét.
2. Sikeres befizetés után meghívódik a `send()` metódus.
3. `verify(notificationService, times(1)).send("Deposit successful")`: pontosan egyszer, a megfelelő üzenettel.
4. Sikertelen (negatív, 0) befizetés esetén nincs értesítés (`never()`, `verifyNoInteractions()`).
5. Sikeres, majd sikertelen befizetés után csak egy értesítés ment ki.

### ExchangeRateServiceTest (Mockito, 5 teszt)

- `when(rateService.getRate("EUR", "HUF")).thenReturn(400.0)`: a mock a beállított árfolyamot adja vissza.
- Be nem állított devizapárra a mock alapértéke `0.0`.
- Több devizapár eltérő árfolyammal állítható be.
- A `CurrencyConverter` a mockolt árfolyammal számol (100 EUR → 40 000 HUF).
- Érvénytelen összegnél az árfolyam-szolgáltatás nem hívódik meg.

### Használt Mockito-lehetőségek

| Lehetőség | Hol |
|---|---|
| `mock()` | Mindkét Mockito tesztosztály `setUp()` metódusa |
| `verify()` (+ `times()`, `never()`) | `BankServiceTest`, `ExchangeRateServiceTest` |
| `when().thenReturn()` | `ExchangeRateServiceTest` |
