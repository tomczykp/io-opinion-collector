# Opinion Collector
## :clipboard:Opis
- Aplikacja realizowana na laboratoriach z przedmiotu Inżynieria Oprogramowania.
- Aplikacja ma na celu zbieranie opinii o produktach z różnych kategorii.
- Zespół realizujący aplikację składał sie z 14 osób podzielonych na 6 dwuosobowych zespołów projektantów i jeden dwuosobowy zespół architektów.
  - Architekci odpowiedzialni byli za stworzenie projektu systemu, podział systemu na moduły i integrowanie zaimplementowanych modułów ze sobą.
  - Projektanci odpowiedzialni byli za zaprojektowanie szczegółowe swojego modułu oraz jego implementację.

Szczegółowy opis odpowiedzialności modułów można znaleźć w [prezentacji architektów](https://docs.google.com/presentation/d/1gJLK1fOHmTmXDPFg25hWTcFlB06_Zm_l1iuSYqm3Dr4/edit?usp=sharing)

### 🛠Użyte technologie
- Spring Boot
- Angular
- Hibernate ORM
- PostgreSQL
- Materialize
- Bootstrap
- OAuth 2.0 (Google i Facebook)
- JWT

### Architekci
- [Piotr Tomczyk](https://github.com/tomczykp)
- [Kamil Graczyk](https://github.com/St0n3k)
### Projektanci wraz z realizowanym przez nich modułem:
|Nazwa modułu|Projektant|Projektant|
|-|-|-|
|Moduł zarządzania produktami|[Bartłomiej Czajkowski](https://github.com/AverageCoffeeEnjoyer)|[Jan Rubacha](https://github.com/Firedog01)|
|Moduł kategorii|[Klaudia Hołonowicz](https://github.com/alice4book)|[Stanisław Jarecki](https://github.com/StanislawJ2)|
|Moduł użytkowników|[Rafał Strzałkowski](https://github.com/rstrzalkowski)|[Mateusz Sochacki](https://github.com/236652)|
|Moduł opinii|[Łukasz Stępień](https://github.com/Lukasz0104)|Tomasz Lesiak|
|Moduł katalogu produktów oraz pytań i odpowiedzi|[Jakub Marszał](https://github.com/jcoby99)|[Mateusz Dargiel](https://github.com/Remuluson2)|
|Moduł obsługi zdarzeń|[Świędrych Szymon](https://github.com/S1NU5-P1)|[Bartosz Drągowski](https://github.com/bortok1)|

## Aby uruchomić aplikację należy:

- zainstalować oprogramowanie `Docker` w systemie operacyjnym (w przypadku Windowsa potrzebny jest
  również `Docker Desktop`) https://docs.docker.com/desktop/install/windows-install/
- zbudować kontener zawierający bazę danych poprzez uruchomienie z poziomu IntelliJ pliku `docker-compose.yml` lub z
  terminala z folderu zawierającego ten plik poleceniem `docker compose up`
- sprawdzić, czy baza PostgreSQL została poprawnie utworzona, nawiązując połączenie z poziomu IntelliJ (z prawej strony
  klikamy `Database` > `New` > `Data Source` > `PostgreSQL`, oraz wpisujemy nazwę użytkownika(`iouser`),
  hasło(`iopassword`) oraz nazwę bazy danych(`iodb`) oraz klikamy `Test Connection`)
- jeśli można się połączyć z bazą danych, aplikacja backendowa powinna bez problemu się uruchomić, by to zrobić
  uruchamiamy główną klasę aplikacji (`OpinionCollectorApplication`)
- aby uruchomić frontend, należy mieć zainstalowanego `node.js`
  oraz `Angular CLI` (https://www.knowledgehut.com/blog/web-development/install-angular-on-windows), wtedy należy
  przejść w terminalu do katalogu `frontend` w projekcie i stamtąd wykonać polecenie `npm install`, następnie `ng serve`
- zainstalować certyfikat
### Instalacja certyfikatu
Do /etc/hosts należy dodać następującą linjkę:

```127.0.0.1 opinioncollector.com```

np.:

```
# Standard host addresses
127.0.0.1  localhost
::1        localhost ip6-localhost ip6-loopback
ff02::1    ip6-allnodes
ff02::2    ip6-allrouters

# Custom host addresess
127.0.1.1  opinioncollector.com
```

oraz zainstalować certyfikat:
[opinoncollector.crt](res/opinioncollector.crt)

#### Windows:
  wystarczy kliknąć 2 razy i zainstalować jako główny urząd certyfikacji
#### Unix:
  w 70% przypadków również, ale można to też zrobić za pomocą polecenia:

  ```sudo cp opinioncollector.crt /usr/local/share/ca-certificates; sudo update-ca-trust```

w obu przypadkach należy zrestartować przeglądarkę.

##### Uwaga! #1

Aby na systemach Unixowych (Linux i Mac OS) dać użytkownikom nieuprzywilejowanym dostęp do portów 443 należy użyć komendy:

```setcap 'cap_net_bind_service=+ep' /usr/bin/node```

która daje node.js dostęp do powyższego portu.

##### Uwaga! #2

Firefox nie przyjmie tego certyfikatu więc trzeba korzystać z chromium.

## Porty, na których będzie uruchomiona aplikacja:

- baza danych PostgreSQL - `localhost`:`5432`
- backend Spring Boot - `https://opinioncollector.com:8443/api` (jest dostępny pod URL `localhost:8443/api`)
- frontend Angular - `https://opinioncollector.com/`
