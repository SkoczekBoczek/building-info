# Building Info

## Opis projektu

Dla administratorów budynków, którzy pragną optymalizować koszty zarządzania budynkami, nasza aplikacja **Building Info** umożliwi pozyskanie informacji o parametrach budynku na poziomie pomieszczeń, kondygnacji oraz całych budynków. Aplikacja będzie dostępna poprzez GUI, a także jako zdalne API, dzięki czemu można ją zintegrować z istniejącymi narzędziami.

## Struktura danych

- **Lokacja** to budynek, poziom lub pomieszczenie.
- **Budynek** może składać się z poziomów, a te z pomieszczeń.
- **Każda lokalizacja** jest charakteryzowana przez:
  - `id` – unikalny identyfikator
  - `name` – opcjonalna nazwa lokalizacji
- **Pomieszczenie** dodatkowo jest charakteryzowane przez:
  - `area` – powierzchnia w m²
  - `cube` – kubatura pomieszczenia w m³
  - `heating` – poziom zużycia energii ogrzewania (`float`)
  - `light` – łączna moc oświetlenia

## Zespół projektowy

- **Scrum Master:** Nikodem Witkowski, @SkoczekBoczek
- **Proxy Product Owner:** Adrian Ciesielski, @Kruwczak
- **Developer:** Jakub Stasierski, @JakubS-001
- **Developer:** Jakub Walkowiak, @Lejkus
