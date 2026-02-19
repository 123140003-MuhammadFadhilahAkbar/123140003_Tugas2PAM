package org.example.project
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

private val BG         = Color(0xFF0A0A0F)
private val Surface1   = Color(0xFF12121A)
private val Surface2   = Color(0xFF1A1A26)
private val Accent     = Color(0xFF6C63FF)
private val AccentSoft = Color(0x336C63FF)
private val Cyan       = Color(0xFF00E5FF)
private val TextPrime  = Color(0xFFF0F0FF)
private val TextMuted  = Color(0xFF7A7A9A)
private val Divider    = Color(0xFF1E1E2E)

data class CategoryMeta(val label: String, val icon: String, val color: Color)

private val categories = listOf(
    CategoryMeta("Tech",    "💻", Color(0xFF6C63FF)),
    CategoryMeta("Sport",   "⚽", Color(0xFF00C896)),
    CategoryMeta("Finance", "📈", Color(0xFFFFB347)),
    CategoryMeta("Health",  "❤️", Color(0xFFFF6584)),
    CategoryMeta("Global",  "🌍", Color(0xFF00E5FF)),
)

data class News(val id: Int, val title: String, val category: String)
data class NewsItem(val uid: Long, val title: String)

@Composable
fun App() {
    val repository = remember { NewsRepository() }
    var selectedCategory by remember { mutableStateOf("Tech") }
    val newsList = remember { mutableStateListOf<NewsItem>() }
    val readCount by repository.readCount.collectAsState()
    val listState = rememberLazyListState()
    val activeMeta = categories.first { it.label == selectedCategory }

    LaunchedEffect(selectedCategory) {
        newsList.clear()
        var counter = 0L
        repository.getNewsStream()
            .filter { it.category == selectedCategory }
            .map { news -> NewsItem(uid = counter++, title = news.title) } // ← transformasi di sini
            .collect { item ->
                newsList.add(0, item)
                if (newsList.size > 1) {
                    listState.animateScrollToItem(0)
                }
            }
    }

    val pulse = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by pulse.animateFloat(
        initialValue = 1f, targetValue = 0.2f, label = "alpha",
        animationSpec = infiniteRepeatable(tween(900), RepeatMode.Reverse)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BG)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(activeMeta.color.copy(alpha = 0.07f), Color.Transparent),
                        radius = 600f
                    )
                )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "NEWS FEED SIMULATOR",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 4.sp,
                        color = TextPrime
                    )
                    Text(
                        text = "Muhammad Fadhilah Akbar · 123140003",
                        fontSize = 10.sp,
                        letterSpacing = 0.5.sp,
                        color = TextMuted
                    )
                }
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(AccentSoft)
                        .border(1.dp, Accent.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .alpha(pulseAlpha)
                            .clip(CircleShape)
                            .background(Accent)
                    )
                    Text(
                        "LIVE", fontSize = 10.sp, fontWeight = FontWeight.Bold,
                        color = Accent, letterSpacing = 2.sp
                    )
                }
            }
            Spacer(Modifier.height(24.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Surface1)
                    .border(1.dp, Divider, RoundedCornerShape(16.dp))
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            "Data Berita Update setiap 2 detik",
                            fontSize = 15.sp,
                            color = TextMuted,
                            letterSpacing = 0.2.sp
                        )
                        Text(
                            "Total Berita Dibaca",
                            fontSize = 11.sp, color = TextMuted, letterSpacing = 0.5.sp
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "$readCount",
                            fontSize = 42.sp,
                            fontWeight = FontWeight.Black,
                            color = TextPrime,
                            lineHeight = 44.sp
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(activeMeta.color.copy(alpha = 0.15f))
                            .border(
                                1.dp,
                                activeMeta.color.copy(alpha = 0.3f),
                                RoundedCornerShape(14.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(activeMeta.icon, fontSize = 26.sp)
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .align(Alignment.BottomCenter)
                        .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    activeMeta.color.copy(alpha = 0.8f),
                                    Cyan.copy(alpha = 0.5f)
                                )
                            )
                        )
                )
            }
            Spacer(Modifier.height(20.dp))
            Text(
                "KATEGORI", fontSize = 10.sp, color = TextMuted,
                letterSpacing = 1.5.sp, fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { meta ->
                    val selected = selectedCategory == meta.label
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (selected) meta.color.copy(alpha = 0.18f) else Surface1
                            )
                            .border(
                                1.dp,
                                if (selected) meta.color.copy(alpha = 0.6f) else Divider,
                                RoundedCornerShape(10.dp)
                            )
                            .clickable { selectedCategory = meta.label }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(meta.icon, fontSize = 16.sp)
                            Spacer(Modifier.height(4.dp))
                            Text(
                                meta.label,
                                fontSize = 9.sp,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                color = if (selected) meta.color else TextMuted,
                                letterSpacing = 0.3.sp
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(20.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .width(3.dp)
                        .height(18.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(activeMeta.color)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    selectedCategory.uppercase(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrime,
                    letterSpacing = 2.sp
                )
                Spacer(Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(activeMeta.color.copy(alpha = 0.15f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        "${newsList.size}",
                        fontSize = 10.sp,
                        color = activeMeta.color,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {
                itemsIndexed(
                    items = newsList,
                    key = { _, item -> item.uid }
                ) { _, item ->
                    NewsItemCard(
                        newsItem = item,
                        repo = repository,
                        meta = activeMeta
                    )
                }
            }
        }
    }
}

@Composable
fun NewsItemCard(newsItem: NewsItem, repo: NewsRepository, meta: CategoryMeta) {
    val scope = rememberCoroutineScope()
    var detailText by remember(newsItem.uid) { mutableStateOf<String?>(null) }
    var isLoading by remember(newsItem.uid) { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Surface1)
            .border(1.dp, Divider, RoundedCornerShape(14.dp))
    ) {
        Box(
            modifier = Modifier
                .width(3.dp)
                .fillMaxHeight()
                .align(Alignment.CenterStart)
                .background(
                    Brush.verticalGradient(
                        listOf(meta.color, meta.color.copy(alpha = 0.2f))
                    )
                )
        )
        Column(
            modifier = Modifier.padding(
                start = 18.dp, end = 16.dp, top = 14.dp, bottom = 14.dp
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(meta.color)
                )
                Text(
                    meta.label.uppercase(),
                    fontSize = 9.sp,
                    color = meta.color,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )
            }
            Spacer(Modifier.height(6.dp))
            Text(
                newsItem.title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = TextPrime,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (detailText != null) meta.color.copy(alpha = 0.2f) else Surface2
                        )
                        .border(
                            1.dp,
                            if (detailText != null) meta.color.copy(alpha = 0.5f)
                            else Color.Transparent,
                            RoundedCornerShape(8.dp)
                        )
                        .clickable(enabled = !isLoading) {
                            if (detailText == null) {
                                scope.launch {
                                    isLoading = true
                                    detailText = repo.fetchNewsDetail()
                                    repo.incrementReadCount()
                                    isLoading = false
                                }
                            } else {
                                detailText = null
                            }
                        }
                        .padding(horizontal = 14.dp, vertical = 7.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(14.dp),
                            color = meta.color,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            if (detailText == null) "Baca Selengkapnya" else "Tutup",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (detailText != null) meta.color else TextMuted,
                            letterSpacing = 0.3.sp
                        )
                    }
                }
            }
            AnimatedVisibility(
                visible = detailText != null,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column {
                    Spacer(Modifier.height(12.dp))
                    HorizontalDivider(color = Divider, thickness = 1.dp)
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = detailText ?: "",
                        fontSize = 12.sp,
                        color = TextMuted,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

class NewsRepository {
    private val _readCount = MutableStateFlow(0)
    val readCount: StateFlow<Int> = _readCount.asStateFlow()

    fun incrementReadCount() { _readCount.value++ }

    fun getNewsStream(): Flow<News> = flow {
        val mockData = listOf(
            News(1,  "Inovasi AI 2026 Mendatang",            "Tech"),
            News(2,  "Kotlin Multiplatform Stabil",           "Tech"),
            News(3,  "Update Compose Multiplatform 1.8",      "Tech"),
            News(4,  "Terobosan Quantum Computing",           "Tech"),
            News(5,  "Gadget Lipat Terbaru Resmi Rilis",      "Tech"),
            News(6,  "Hasil Pertandingan Semalam",            "Sport"),
            News(7,  "Transfer Pemain Musim Dingin",          "Sport"),
            News(8,  "Persiapan Olimpiade 2028",              "Sport"),
            News(9,  "Rekor Baru Lari 100 Meter",             "Sport"),
            News(10, "Turnamen Esport Terbesar Dimulai",      "Sport"),
            News(11, "IHSG Menguat Pagi Ini",                 "Finance"),
            News(12, "Inflasi Tahunan Menurun",               "Finance"),
            News(13, "Kebijakan Suku Bunga Terbaru",          "Finance"),
            News(14, "Startup Fintech Raih Pendanaan Seri C", "Finance"),
            News(15, "Prediksi Pasar Kripto Kuartal 1",       "Finance"),
            News(16, "Vaksin Generasi Baru Ditemukan",        "Health"),
            News(17, "Tips Menjaga Imun Tubuh",               "Health"),
            News(18, "Pentingnya Tidur 8 Jam Sehari",         "Health"),
            News(19, "Manfaat Diet Mediterania",              "Health"),
            News(20, "Teknologi Operasi Jarak Jauh",          "Health"),
            News(21, "KTT Iklim di Jakarta",                  "Global"),
            News(22, "Perjanjian Dagang Antar Negara",        "Global"),
            News(23, "Eksplorasi Mars Tahap Kedua",           "Global"),
            News(24, "Restorasi Hutan Amazon",                "Global"),
            News(25, "Pertemuan PBB Bahas Keamanan Siber",    "Global")
        )
        while (true) {
            emit(mockData.random())
            delay(2000)
        }
    }.flowOn(Dispatchers.Default)

    suspend fun fetchNewsDetail(): String = withContext(Dispatchers.IO) {
        try {
            delay(1000)
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                    "Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                    "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris."
        } catch (e: Exception) {
            "Gagal memuat detail: ${e.message}"
        }
    }
}