# ⚙️ GeoNews — Backend

> API REST construit cu Spring Boot care agregă date din multiple surse externe — informații despre țări, istoric generat de Gemini AI, știri GNews pe categorii și fotografii Unsplash.

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.9-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![Gemini](https://img.shields.io/badge/Gemini_AI-Google-4285F4?style=for-the-badge&logo=google&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-ready-2496ED?style=for-the-badge&logo=docker&logoColor=white)

---

## ✨ Features

- 🌐 **REST Countries API** — date oficiale despre țară (capitală, populație, limbă, monedă, steag)
- 🤖 **Gemini AI** — rezumat istoric generat prin Google AI SDK
- 📰 **GNews API** — știri recente filtrate per țară și categorie
- 🖼️ **Unsplash API** — fotografii reprezentative per țară
- 🔀 **Agregare** — toate datele returnate într-un singur endpoint principal
- ⚠️ **Global Exception Handler** — răspunsuri de eroare uniforme cu status codes HTTP
- 🔒 **CORS configurat** pentru frontend

---

## 🛠️ Tech Stack

| Tehnologie | Rol |
|-----------|-----|
| Java 21 | Limbaj de programare |
| Spring Boot 3.x | Framework principal |
| Spring Web | REST controllers & RestTemplate |
| Google AI SDK | Integrare Gemini AI |
| Jackson | Parsare JSON |
| Maven | Build tool |

---

## 🚀 Pornire locală

### Cerințe
- Java 21+
- Maven 3.9+
- API Keys pentru GNews, Unsplash și Gemini

### Configurare API Keys

În `src/main/resources/application.properties`:

```properties
gnews.key=GNEWS_KEY_HERE
unsplash.key=UNSPLASH_KEY_HERE
gemini.key=GEMINI_KEY_HERE
```

Obține keys gratuit de la:
- 🔑 **GNews**: [gnews.io](https://gnews.io)
- 🔑 **Unsplash**: [unsplash.com/developers](https://unsplash.com/developers)
- 🔑 **Gemini**: [aistudio.google.com](https://aistudio.google.com)

### Rulare

```bash
./mvnw spring-boot:run
```

Serverul pornește pe **http://localhost:8080**

Frontend-ul React (după build) este servit automat din `src/main/resources/static/`.

---

## 📡 API Reference

### `GET /api/country/{code}`
Returnează toate informațiile despre o țară.

```
GET http://localhost:8080/api/country/RO
```

**Response:**
```json
{
  "name": "Romania",
  "code": "RO",
  "capital": "Bucharest",
  "population": "19.036.031",
  "language": "Romanian",
  "currency": "Romanian leu (lei)",
  "continent": "Europe",
  "area": "238.391 km²",
  "flagUrl": "https://flagcdn.com/w320/ro.png",
  "photos": ["https://images.unsplash.com/..."],
  "news": [
    {
      "title": "Titlul știrii",
      "description": "Descriere scurtă",
      "url": "https://...",
      "publishedAt": "2026-03-25T22:46:33Z",
      "source": "Prosport",
      "image": "https://..."
    }
  ]
}
```

---

### `GET /api/country/history/{code}`
Returnează un rezumat istoric generat de Gemini AI.

```
GET http://localhost:8080/api/country/history/RO
```

**Response:** `text/plain` — paragraf istoric generat de AI.

---

### `GET /api/country/{code}/{category}`
Returnează știri filtrate pe categorie.

```
GET http://localhost:8080/api/country/RO/TECHNOLOGY
```

**Categorii disponibile:**

| Categorie | Descriere |
|-----------|-----------|
| `GENERAL` | Știri generale |
| `WORLD` | Știri internaționale |
| `NATION` | Știri naționale |
| `BUSINESS` | Afaceri & economie |
| `TECHNOLOGY` | Tehnologie |
| `ENTERTAINMENT` | Divertisment |
| `SPORTS` | Sport |
| `SCIENCE` | Știință |
| `HEALTH` | Sănătate |

---

## ⚠️ Error Handling

Aplicația folosește un `HttpRequestErrorHandler` global care interceptează toate excepțiile și returnează răspunsuri uniforme cu status codes HTTP corespunzătoare.

| Status Code | Situație |
|-------------|---------|
| `400` | Request invalid (cod țară incorect, categorie invalidă) |
| `404` | Țara nu a fost găsită |
| `500` | Eroare internă (API extern indisponibil) |
| `503` | Serviciu extern temporar indisponibil |

---

## 📁 Structura proiectului

```
src/main/java/com/geo/news/
├── config/
│   ├── GeminiConfig.java          # Configurare Google AI SDK
│   └── WebConfig.java             # Configurare CORS & static files
├── controller/
│   └── CountryController.java     # REST endpoints
├── exception/
│   └── HttpRequestErrorHandler.java  # Global exception handler
├── model/
│   ├── Category.java              # Enum categorii știri
│   └── CountryInfo.java           # Model de date
├── service/
│   ├── CountryService.java        # Agregare date principale
│   ├── NewsService.java           # GNews API + filtrare pe categorii
│   ├── UnsplashService.java       # Unsplash photos
│   └── WikipediaService.java      # Gemini AI history
└── NewsApplication.java
```

---

## ⚙️ Configurare completă `application.properties`

```properties
spring.application.name=world-explorer-api
server.port=8080

# API Keys
gnews.key=${GNEWS_KEY}
unsplash.key=${UNSPLASH_KEY}
gemini.key=${GEMINI_KEY}

# CORS
cors.allowed.origins=http://localhost:5173
```

---

## 🐳 Docker

```bash
# Build
docker build -t geonews-backend .

# Rulare
docker run -p 8080:8080 \
  -e GNEWS_KEY=your_key \
  -e UNSPLASH_KEY=your_key \
  -e GEMINI_KEY=your_key \
  geonews-backend
```

---

## ☁️ Deploy pe Render

1. Push pe GitHub
2. Conectează repo-ul pe [render.com](https://render.com)
3. Language: **Docker**
4. Adaugă Environment Variables:
   - `GNEWS_KEY`
   - `UNSPLASH_KEY`
   - `GEMINI_KEY`
5. Deploy!

---

## 🔗 Surse de date externe

| API | Documentație | Plan gratuit |
|-----|-------------|-------------|
| REST Countries | [restcountries.com](https://restcountries.com) | ✅ Fără key |
| Gemini AI | [aistudio.google.com](https://aistudio.google.com) | ✅ Free tier |
| GNews | [gnews.io/docs](https://gnews.io/docs) | ✅ 100 req/zi |
| Unsplash | [unsplash.com/developers](https://unsplash.com/developers) | ✅ 50 req/oră |
