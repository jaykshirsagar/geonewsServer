# ⚙️ GeoNews — Backend

> API REST construit cu Spring Boot care agregă date din multiple surse externe — informații despre țări, rezumate Wikipedia, știri GNews și fotografii Unsplash.

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.9-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-ready-2496ED?style=for-the-badge&logo=docker&logoColor=white)

---

## ✨ Features

- 🌐 **REST Countries API** — date oficiale despre țară (capitală, populație, limbă, monedă, steag)
- 📖 **Wikipedia API** — rezumat istoric în engleză
- 📰 **GNews API** — știri recente filtrate per țară
- 🖼️ **Unsplash API** — fotografii reprezentative per țară
- 🔀 **Agregare paralelă** — toate datele returnate într-un singur endpoint
- 🔒 **CORS configurat** pentru frontend

---

## 🛠️ Tech Stack

| Tehnologie | Rol |
|-----------|-----|
| Java 21 | Limbaj de programare |
| Spring Boot 3.x | Framework principal |
| Spring Web | REST controllers & RestTemplate |
| Spring WebFlux | WebClient pentru HTTP calls |
| Jackson | Parsare JSON |
| Maven | Build tool |

---

## 🚀 Pornire locală

### Cerințe
- Java 21+
- Maven 3.9+
- API Keys pentru GNews și Unsplash

### Configurare API Keys

Deschide `src/main/resources/application.properties` și completează:

```properties
gnews.key=GNEWS_KEY_HERE
unsplash.key=UNSPLASH_KEY_HERE
```

Obține keys gratuit de la:
- 🔑 **GNews**: [gnews.io](https://gnews.io)
- 🔑 **Unsplash**: [unsplash.com/developers](https://unsplash.com/developers)

### Rulare

```bash
# Clonează repo-ul
git clone https://github.com/username/geonews.git
cd geonews/server

# Pornește aplicația
./mvnw spring-boot:run
```

Serverul va porni pe **http://localhost:8080**

---

## 📡 API Reference

### `GET /api/country/{countryCode}`

Returnează toate informațiile despre o țară.

**Parametri:**

| Parametru | Tip | Descriere |
|-----------|-----|-----------|
| `countryCode` | `String` | Codul ISO 3166-1 Alpha-2 (ex: `RO`, `FR`, `US`) |

**Exemplu request:**
```
GET http://localhost:8080/api/country/RO
```

**Exemplu response:**
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
  "historySummary": "Romania is a country in Southeast and Central Europe...",
  "photos": [
    "https://images.unsplash.com/..."
  ],
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

## 📁 Structura proiectului

```
server/
└── src/main/java/com/geo/news/
    ├── controller/
    │   └── CountryController.java   # REST endpoint
    ├── service/
    │   ├── CountryService.java      # Agregare date
    │   ├── WikipediaService.java    # Wikipedia API
    │   ├── UnsplashService.java     # Unsplash API
    │   └── NewsService.java         # GNews API
    ├── model/
    │   └── CountryInfo.java         # Model de date
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

# CORS
cors.allowed.origins=http://localhost:5173
```

---

## 🐳 Docker

```bash
# Build imagine
docker build -t geonews-backend .

# Rulare container cu API keys
docker run -p 8080:8080 \
  -e GNEWS_KEY=your_key \
  -e UNSPLASH_KEY=your_key \
  geonews-backend
```

---

## 🐳 Docker Compose (Frontend + Backend)

Din folderul rădăcină al proiectului:

```bash
# Creează fișierul .env cu keys
echo "GNEWS_KEY=your_gnews_key" >> .env
echo "UNSPLASH_KEY=your_unsplash_key" >> .env

# Pornește ambele servicii
docker-compose up --build
```

| Serviciu | URL |
|---------|-----|
| Frontend | http://localhost:3000 |
| Backend | http://localhost:8080 |

---

## 🔗 Surse de date externe

| API | Documentație | Plan gratuit |
|-----|-------------|-------------|
| REST Countries | [restcountries.com](https://restcountries.com) | ✅ Fără key |
| Wikipedia | [wikimedia.org/api](https://www.mediawiki.org/wiki/API:Main_page) | ✅ Fără key |
| GNews | [gnews.io/docs](https://gnews.io/docs) | ✅ 100 req/zi |
| Unsplash | [unsplash.com/developers](https://unsplash.com/developers) | ✅ 50 req/oră |
