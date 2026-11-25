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

## API

Wszystkie endpointy przyjmują lub zwracają dane w formacie JSON.

| Metoda | Endpoint | Działanie |
| --- | --- | --- |
| `POST` | `/buildings` | Dodaje budynek wraz z poziomami i pomieszczeniami albo zastępuje budynek o tym samym ID. Zwraca zapisany budynek jako JSON. |
| `GET` | `/buildings/{buildingId}` | Zwraca budynek oraz sumaryczną powierzchnię jego poziomów. |
| `GET` | `/buildings/{buildingId}/levels/{levelId}` | Zwraca poziom budynku oraz sumaryczną powierzchnię jego pomieszczeń. |
| `GET` | `/buildings/{buildingId}/levels/{levelId}/rooms/{roomId}` | Zwraca pomieszczenie wraz z jego parametrami. |

Przykładowe żądanie `POST /buildings` (`Content-Type: application/json`):

```json
{
  "id": 1,
  "name": "Budynek A",
  "levels": [
    {
      "id": 10,
      "name": "Parter",
      "rooms": [
        {
          "id": 101,
          "name": "Sala 101",
          "area": 25.0,
          "cube": 75.0,
          "heating": 120.5,
          "light": 300.0
        }
      ]
    }
  ]
}
```

## Zespół projektowy

- **Scrum Master:** Nikodem Witkowski, [@SkoczekBoczek](https://github.com/SkoczekBoczek)
- **Proxy Product Owner:** Adrian Ciesielski, [@Kruwczak](https://github.com/Kruwczak)
- **Developer:** Jakub Stasierski, [@JakubS-001](https://github.com/JakubS-001)
- **Developer:** Jakub Walkowiak, [@Lejkus](https://github.com/Lejkus)
