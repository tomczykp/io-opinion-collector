## Opinion Collector

#### Kolejność prezentacji modułów (start 6 zajęcia):
1. Moduł zarządzania produktami - Bartłomiej Czajkowski, Jan Rubacha
2. Moduł kategorii - Klaudia Hołonowicz, Stanisław Jarecki
3. Moduł użytkowników - Mateusz Sochacki, Rafał Strzałkowski
4. Moduł opinii - Łukasz Stępień, Tomasz Lesiak
5. Moduł katalogu produktów oraz pytań i odpowiedzi - Mateusz Dargiel, Jakub Marszał
6. Moduł obsługi zdarzeń - Świędrych Szymon, Bartosz Drągowski    


- [Prezentacja architektów] (https://docs.google.com/presentation/d/1gJLK1fOHmTmXDPFg25hWTcFlB06_Zm_l1iuSYqm3Dr4/edit?usp=sharing)

- [Google docs] (https://docs.google.com/document/d/1KyEn--kIL-sw5VHaI0Z0owEGZXONKBUFkh_0irwIwjU/edit?usp=sharing)

Materiały do Spring Boota można znaleźć na https://www.baeldung.com/ oraz na YT (wystarczy chwilę poszukać, polecam Amigoscode, freeCodeCamp)  
Materiały do Angulara można znaleźć na YT (np. https://youtu.be/3dHNOWTI7H8)

## Aby zacząć rozwijać swój moduł należy:
- zainstalować oprogramowanie `Docker` w systemie operacyjnym (w przypadku Windowsa potrzebny jest również `Docker Desktop`) https://docs.docker.com/desktop/install/windows-install/
- zbudować kontener zawierający bazę danych poprzez uruchomienie z poziomu IntelliJ pliku `docker-compose.yml` lub z terminala z folderu zawierającego ten plik poleceniem `docker compose up`
- sprawdzić, czy baza PostgreSQL została poprawnie utworzona, nawiązując połączenie z poziomu IntelliJ (z prawej strony klikamy `Database` > `New` > `Data Source` > `PostgreSQL`, oraz wpisujemy nazwę użytkownika(`iouser`), hasło(`iopassword`) oraz nazwę bazy danych(`iodb`) oraz klikamy `Test Connection`)
- jeśli można się połączyć z bazą danych, aplikacja backendowa powinna bez problemu się uruchomić, by to zrobić uruchamiamy główną klasę aplikacji (`OpinionCollectorApplication`)
- aby uruchomić frontend, należy mieć zainstalowanego `node.js` oraz `Angular CLI` (https://www.knowledgehut.com/blog/web-development/install-angular-on-windows), wtedy należy przejść w terminalu do katalogu `frontend` w projekcie i stamtąd wykonać polecenie `npm install`, następnie `ng serve`


Porty, na których będzie stała aplikacja:
- baza danych PostgreSQL - `localhost`:`5432`
- backend Spring Boot - `localhost`:`8080` (jest dostępny pod URL `localhost:8080/api`)
- frontend Angular - `localhost`:`4200`

