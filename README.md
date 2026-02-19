
# News Feed Simulator
## Identitas

Nama  : Muhammad Fadhilah Akbar

NIM   : 123140003

Prodi : Teknik Informatika

---

## Implementasi Berdasarkan Kode

### 1️⃣ Flow Update Setiap 2 Detik

Di dalam `NewsRepository`:

```kotlin
fun getNewsStream(): Flow<News> = flow {
    while (true) {
        emit(mockData.random())
        delay(2000)
    }
}
```

---

### 2️⃣ Filter Berdasarkan Kategori

Di dalam `LaunchedEffect`:

```kotlin
repository.getNewsStream()
    .filter { it.category == selectedCategory }
```

---

### 3️⃣ Transformasi Data dengan map

```kotlin
.map { news -> 
    NewsItem(uid = counter++, title = news.title) 
}
```
---

### 4️⃣ StateFlow untuk Total Berita Dibaca

```kotlin
private val _readCount = MutableStateFlow(0)
val readCount: StateFlow<Int> = _readCount.asStateFlow()
```


```kotlin
val readCount by repository.readCount.collectAsState()
```

---

### 5️⃣ Coroutine untuk Fetch Detail Berita

Di dalam `NewsItemCard`:

```kotlin
scope.launch {
    isLoading = true
    detailText = repo.fetchNewsDetail()
    repo.incrementReadCount()
    isLoading = false
}
```

---

## Screenshot Aplikasi

<img width="575" height="1280" alt="Image" src="https://github.com/user-attachments/assets/63ac73f0-03fe-46de-b159-52cfe8b74ba1" />

