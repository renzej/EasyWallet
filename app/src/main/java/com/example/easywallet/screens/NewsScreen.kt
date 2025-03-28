package com.example.easywallet.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.easywallet.api.NewsManager
import com.example.easywallet.api.model.Article
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(newsManager: NewsManager) {
    // For the API
    LaunchedEffect(Unit) {
        newsManager.getTopHeadlines() // Fetch data when the screen is loaded
    }

    val focusManager = LocalFocusManager.current

    // Observe the news response
    val newsArticles by newsManager.newsResponse.collectAsState()

    // For scroll
    val scrollState = rememberLazyListState()

    // Track whether we should show the top bar title
    var shouldShowTopBarTitle by remember { mutableStateOf(false) }

    // Update the visibility when the scroll offset exceeds 95
    LaunchedEffect(scrollState.firstVisibleItemIndex) {
        if (scrollState.firstVisibleItemIndex > 0) {
            shouldShowTopBarTitle = true
        } else {
            shouldShowTopBarTitle = false
        }
    }

    // Dynamic top bar color change
    val topBarColor by remember {
        derivedStateOf {
            if (shouldShowTopBarTitle) Color(0xFFE5E5EA) else Color.Transparent
        }
    }

    // State for search query
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    if (shouldShowTopBarTitle) {
                        Text(
                            text = "News",
                            modifier = Modifier.padding(top = 20.dp),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )
                    }
                },
                modifier = Modifier.height(80.dp),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = topBarColor
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding() + 40.dp)
                .pointerInput(Unit) {
                    detectTapGestures(onPress = {
                        // Clear focus when tapping outside
                        focusManager.clearFocus()
                    })
                }
        ) {
            LazyColumn(
                state = scrollState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = 15.dp,
                        bottom = innerPadding.calculateBottomPadding(),
                        end = 15.dp,
                        top = innerPadding.calculateTopPadding()
                    )
            ) {
                item {
                    // **Header Section** (Add this as the first item inside LazyColumn)
                    Text(
                        text = "News",
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                    )
                }

                item {
                    // **Search Bar** Section
                    CustomSearchBar(
                        searchQuery = searchQuery,
                        onSearchQueryChange = { searchQuery = it },
                        onSearchClick = { newsManager.searchNews(searchQuery) }
                    )
                }

                // Check if there are any news articles to display
                if (newsArticles.isNotEmpty()) {
                    items(newsArticles) { article ->
                        NewsItem(article = article)
                        Spacer(modifier = Modifier.height(7.dp))
                    }
                } else {
                    // Loading message if articles are not available
                    item {
                        Text(
                            text = "Loading...",
                            fontSize = 16.sp,
                            modifier = Modifier.padding(15.dp)
                        )
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewsItem(article: Article) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(10.dp))
            .padding(start = 10.dp, bottom = 5.dp, end = 10.dp, top = 10.dp) // Added padding outside to create spacing
    ) {
        Column(
            modifier = Modifier.fillMaxWidth() // Column to arrange the content vertically
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween // Ensures spacing between text and image
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f) // Takes up the remaining space
                        .padding(end = 10.dp) // Adds spacing between text and image
                ) {
                    // Source
                    Text(
                        text = article.source?.name ?: "No Source",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Medium
                    )
                    // Title
                    Text(
                        text = article.title ?: "No title available",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.8).sp,
                        lineHeight = 18.sp
                    )
                }
                // Article Image
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(article.urlToImage)
                        .crossfade(true)
                        .build(),
                    contentDescription = article.title,
                    modifier = Modifier
                        .size(110.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(Color.LightGray),
                    contentScale = ContentScale.Crop
                )
            }

            // Add space between the content and the author/time info
            Spacer(modifier = Modifier.height(5.dp))

            Row {
                // Time ago (time since the article was published)
                article.publishedAt?.let {
                    val timeAgo = formatTimeAgo(it)
                    Text(
                        text = timeAgo,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Light,
                        color = Color.Gray
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                // Author
                Text(
                    text = article.author ?: "",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light,
                    color = Color.Gray
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatTimeAgo(publishedAt: String): String {
    val dateFormatter = DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.systemDefault())
    val instant = Instant.parse(publishedAt)
    val now = Instant.now()
    val duration = java.time.Duration.between(instant, now)

    val hours = TimeUnit.MILLISECONDS.toHours(duration.toMillis())
    val minutes = TimeUnit.MILLISECONDS.toMinutes(duration.toMillis()) % 60

    return when {
        hours >= 1 -> "$hours" + "h ago" // Using "h" for hours
        minutes >= 1 -> "$minutes" + "m ago" // Using "m" for minutes
        else -> "Just now"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearchClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // TextField with dynamic width based on the search query length
        TextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = { Text("Search News") },
            modifier = Modifier
                .weight(1f) // This allows the TextField to shrink horizontally when necessary
                .padding(5.dp)
                .clip(RoundedCornerShape(15.dp))
                .height(50.dp),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon"
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    // Clear Icon (X) when text is entered
                    IconButton(onClick = { onSearchQueryChange("") }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear Icon"
                        )
                    }
                }
            },
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        // Show "Search" clickable text when text is entered
        if (searchQuery.isNotEmpty()) {
            Text(
                text = "Search",
                color = Color.Blue,
                modifier = Modifier
                    .clickable(onClick = onSearchClick)
                    .padding(start = 8.dp)
            )
        }
    }
}


