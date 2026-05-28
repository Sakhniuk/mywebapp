# Лабораторна робота №1 - MyWebApp

## Індивідуальне завдання
- **Номер залікової книжки (N) = 24**
- **V2 = (24 % 2) + 1 = 1** → аргументи командного рядка, MariaDB
- **V3 = (24 % 3) + 1 = 1** → Notes Service
- **V5 = (24 % 5) + 1 = 5** → порт 5000

## Про застосунок
Простий веб-сервіс для зберігання текстових нотаток.

**Нотатка містить:**
- `id` - унікальний ідентифікатор
- `title` - заголовок
- `content` - текст нотатки
- `created_at` - дата створення

## API Endpoints

| Метод | Шлях | Accept | Відповідь |
|-------|------|--------|-----------|
| GET | `/health/alive` | - | `OK` |
| GET | `/health/ready` | - | `OK` або помилка 500 |
| GET | `/` | `text/html` | Список всіх ендпоінтів |
| GET | `/notes` | `application/json` | JSON масив нотаток |
| GET | `/notes` | `text/html` | HTML таблиця |
| POST | `/notes` | `application/json` | Створити нотатку |
| GET | `/notes/{id}` | `application/json` | JSON однієї нотатки |
| GET | `/notes/{id}` | `text/html` | HTML сторінка |

## Приклади запитів

```bash
# Перевірка здоров'я
curl http://localhost/health/alive

# Перевірка готовності (БД)
curl http://localhost/health/ready

# Створити нотатку
curl -X POST http://localhost/notes \
  -H "Content-Type: application/json" \
  -d '{"title":"Моя нотатка","content":"Привіт світе!"}'

# Отримати всі нотатки (JSON)
curl -H "Accept: application/json" http://localhost/notes

# Отримати всі нотатки (HTML)
curl http://localhost/notes

# Отримати нотатку за ID
curl http://localhost/notes/1
