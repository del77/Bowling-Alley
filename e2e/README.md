> Każdy uruchowmienie testów tworzy nowego usera w bazie, ostrożnie !

# Wymagane oprogramowanie:

## Package manager:

**Jeden z poniższych:**

- [Install yarn!](https://yarnpkg.com/en/docs/install#windows-stable)
- [Install npm!](https://www.npmjs.com/get-npm)

## Przeglądarka:

- chrome (zalecana - uruchamia wiele okien przeglądarki jednocześnie)
- firefox (mogą być problemy z certyfikatem)

# Uruchamianie:

1. w folderze `project_root/e2e` wykonać polecenie `yarn` lub `npm` w dowolnym terminalu
   . uruchomić testy poleceniem:
   - `yarn test` lub `npm test` - dla chrome
   - `yarn test:firefox` lub `npm test:firefox` - dla firefox

# Dokumentacja

- [WebdriverIO](http://v4.webdriver.io/api.html)

# Wymagane konta do testów

## konta do testów e2e:

| login            | haslo            | mail                      |
| ---------------- | ---------------- | ------------------------- |
| testClient       | testClient       | testClient@mail.com       |
| testEmployee     | testEmplotee     | testEmployee@mail.com     |
| testAdmin        | testAdmin        | testAdmin@mail.com        |
| testAllRoles     | testAllRoles     | testAllRoles@mail.com     |
| testChangePasswd | testChangePasswd | testChangePasswd@mail.com |
| testLockUnlock   | testLockUnlock   | testLockUnlock@mail.com   |

## konto które nie może istnieć:

**login:** `unikalneKontoDoTestow`

# Testowanie lokalnie/inny deploy

- zmienić ścieżkę `baseUrl` w `e2e/tests/constants.js`

# Uruchamianie pojedynczego testu:

- `yarn test --spec [sciezka_do_testu]`
- `npm test --spec [sciezka_do_testu]`
