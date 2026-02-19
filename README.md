Berikut README **yang disesuaikan langsung dengan kode `package org.example.project` yang kamu kirim**, dan tetap fokus pada spesifikasi tugas.

Bisa langsung copy sebagai `README.md`.

---

````markdown
# News Feed Simulator

Aplikasi simulasi News Feed menggunakan Kotlin + Jetpack Compose dengan penerapan:
- Flow
- StateFlow
- Operator Flow (filter & map)
- Coroutine

Package:
```kotlin
package org.example.project
````

---

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



