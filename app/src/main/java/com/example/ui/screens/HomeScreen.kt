package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LiveTv
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.model.CatalogCategory
import com.example.ui.model.CatalogData
import com.example.ui.model.CatalogRow
import com.example.ui.model.MediaTitleItem
import com.example.ui.model.PosterArtworkStyle
import com.example.ui.model.PosterVisual
import com.example.ui.permissions.MediaAccessStatus
import com.example.ui.permissions.MediaPermissionHelper
import com.example.ui.theme.NavyBackground
import com.example.ui.theme.NavyBorder
import com.example.ui.theme.NavySurface
import com.example.ui.theme.NavySurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TogetherBlue
import com.example.ui.theme.TogetherBlueBright
import com.example.ui.theme.TogetherBlueGlow

enum class HomeTab(
  val testTag: String,
  val contentDescRes: Int,
  val selectedIcon: ImageVector,
  val unselectedIcon: ImageVector,
) {
  HOME("nav_home", R.string.nav_home, Icons.Filled.Home, Icons.Outlined.Home),
  MOVIES("nav_movies", R.string.nav_movies, Icons.Filled.Movie, Icons.Outlined.Movie),
  SERIES("nav_series", R.string.nav_series, Icons.Filled.LiveTv, Icons.Outlined.LiveTv),
  SEARCH("nav_search", R.string.nav_search, Icons.Filled.Search, Icons.Outlined.Search),
  ACCOUNT("nav_account", R.string.nav_account, Icons.Filled.Person, Icons.Outlined.Person),
}

@Composable
fun HomeScreen() {
  var selectedTab by rememberSaveable { mutableStateOf(HomeTab.HOME) }

  BackHandler(enabled = selectedTab != HomeTab.HOME) { selectedTab = HomeTab.HOME }

  Scaffold(
    containerColor = NavyBackground,
    contentWindowInsets = WindowInsets(0, 0, 0, 0),
    bottomBar = {
      TogetherBottomBar(
        selectedTab = selectedTab,
        onSelectTab = { selectedTab = it },
      )
    },
  ) { innerPadding ->
    Box(
      modifier =
        Modifier.fillMaxSize()
          .background(NavyBackground)
          .padding(bottom = innerPadding.calculateBottomPadding())
    ) {
      when (selectedTab) {
        HomeTab.HOME -> HomeFeedContent()
        HomeTab.MOVIES ->
          CategoryCatalogContent(
            headerTitle = "Filme",
            rows =
              listOf(
                CatalogRow("movies_popular", "Beliebte Filme", CatalogData.trendingMovies),
                CatalogRow(
                  "movies_new",
                  "Neue Spielfilme",
                  CatalogData.allItems.filter { it.category == CatalogCategory.MOVIE }.reversed(),
                ),
              ),
          )
        HomeTab.SERIES ->
          CategoryCatalogContent(
            headerTitle = "Serien",
            rows =
              listOf(
                CatalogRow("series_top", "Top Serien", CatalogData.topSeries),
                CatalogRow(
                  "series_more",
                  "Empfohlene Serien",
                  CatalogData.allItems.filter { it.category == CatalogCategory.SERIES }.reversed(),
                ),
              ),
          )
        HomeTab.SEARCH -> SearchCatalogContent()
        HomeTab.ACCOUNT -> AccountOverviewContent()
      }
    }
  }
}

@Composable
private fun TogetherBottomBar(
  selectedTab: HomeTab,
  onSelectTab: (HomeTab) -> Unit,
) {
  Column(modifier = Modifier.fillMaxWidth().background(NavySurface)) {
    HorizontalDivider(thickness = 1.dp, color = NavyBorder.copy(alpha = 0.6f))
    NavigationBar(
      containerColor = NavySurface,
      tonalElevation = 0.dp,
      windowInsets = NavigationBarDefaults.windowInsets,
      modifier = Modifier.testTag("bottom_navigation_bar"),
    ) {
      HomeTab.entries.forEach { tab ->
        val selected = tab == selectedTab
        val description = stringResource(tab.contentDescRes)
        NavigationBarItem(
          selected = selected,
          onClick = { onSelectTab(tab) },
          icon = {
            Icon(
              imageVector = if (selected) tab.selectedIcon else tab.unselectedIcon,
              contentDescription = description,
              modifier = Modifier.size(26.dp),
            )
          },
          label = null,
          alwaysShowLabel = false,
          colors =
            NavigationBarItemDefaults.colors(
              selectedIconColor = TogetherBlueBright,
              unselectedIconColor = TextMuted,
              indicatorColor = TogetherBlue.copy(alpha = 0.16f),
            ),
          modifier = Modifier.testTag(tab.testTag),
        )
      }
    }
  }
}

@Composable
private fun HomeFeedContent() {
  LazyColumn(
    modifier = Modifier.fillMaxSize().testTag("home_feed_list"),
    contentPadding = PaddingValues(bottom = 28.dp),
  ) {
    item(key = "featured_hero") { FeaturedMovieBanner() }
    items(items = CatalogData.homeRows, key = { it.id }) { row ->
      PosterCarouselSection(row = row)
    }
  }
}

@Composable
private fun FeaturedMovieBanner() {
  Box(
    modifier =
      Modifier.fillMaxWidth()
        .height(450.dp)
        .testTag("featured_movie_banner")
  ) {
    Image(
      painter = painterResource(id = R.drawable.img_hero_featured),
      contentDescription = stringResource(R.string.featured_title),
      contentScale = ContentScale.Crop,
      modifier = Modifier.fillMaxSize(),
    )

    // Top subtle scrim for status bar and TOGETHER wordmark legibility
    Box(
      modifier =
        Modifier.fillMaxWidth()
          .height(140.dp)
          .align(Alignment.TopCenter)
          .background(
            Brush.verticalGradient(
              colors =
                listOf(
                  NavyBackground.copy(alpha = 0.88f),
                  NavyBackground.copy(alpha = 0.35f),
                  Color.Transparent,
                )
            )
          )
    )

    // Bottom deep navy gradient blending smoothly into the catalog rows
    Box(
      modifier =
        Modifier.fillMaxWidth()
          .height(250.dp)
          .align(Alignment.BottomCenter)
          .background(
            Brush.verticalGradient(
              colors =
                listOf(
                  Color.Transparent,
                  NavyBackground.copy(alpha = 0.65f),
                  NavyBackground.copy(alpha = 0.96f),
                  NavyBackground,
                )
            )
          )
    )

    // Brand wordmark header
    Row(
      modifier =
        Modifier.fillMaxWidth()
          .statusBarsPadding()
          .padding(horizontal = 20.dp, vertical = 14.dp)
          .align(Alignment.TopStart),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween,
    ) {
      Text(
        text = stringResource(R.string.brand_wordmark),
        style =
          MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold,
            letterSpacing = 4.sp,
          ),
        color = TextPrimary,
      )
      Box(
        modifier =
          Modifier.size(8.dp)
            .clip(CircleShape)
            .background(TogetherBlueBright)
      )
    }

    // Featured title metadata overlay (purely visual / decorative)
    Column(
      modifier =
        Modifier.align(Alignment.BottomStart)
          .fillMaxWidth()
          .padding(horizontal = 20.dp, vertical = 18.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      Surface(
        color = TogetherBlue.copy(alpha = 0.24f),
        shape = RoundedCornerShape(6.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, TogetherBlueBright.copy(alpha = 0.55f)),
      ) {
        Text(
          text = stringResource(R.string.featured_badge),
          style =
            MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.6.sp,
            ),
          color = TogetherBlueGlow,
          modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
        )
      }

      Text(
        text = stringResource(R.string.featured_title),
        style =
          MaterialTheme.typography.headlineLarge.copy(
            fontSize = 30.sp,
            letterSpacing = 1.2.sp,
          ),
        color = TextPrimary,
      )

      Text(
        text = stringResource(R.string.featured_meta),
        style = MaterialTheme.typography.bodyMedium,
        color = TextSecondary,
      )
    }
  }
}

@Composable
private fun PosterCarouselSection(row: CatalogRow) {
  Column(
    modifier = Modifier.fillMaxWidth().padding(top = 18.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp),
  ) {
    Text(
      text = row.title,
      style = MaterialTheme.typography.titleLarge,
      color = TextPrimary,
      modifier = Modifier.padding(horizontal = 20.dp),
    )

    LazyRow(
      contentPadding = PaddingValues(horizontal = 20.dp),
      horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      items(items = row.items, key = { "${row.id}_${it.id}" }) { item ->
        DecorativePosterCard(item = item)
      }
    }
  }
}

@Composable
private fun DecorativePosterCard(
  item: MediaTitleItem,
  modifier: Modifier = Modifier,
) {
  // Strictly decorative poster: no clickable modifier or click handler.
  Box(
    modifier =
      modifier
        .width(126.dp)
        .aspectRatio(2f / 3f)
        .clip(RoundedCornerShape(10.dp))
        .background(NavySurfaceElevated)
        .border(
          width = 1.dp,
          color = NavyBorder.copy(alpha = 0.75f),
          shape = RoundedCornerShape(10.dp),
        )
        .testTag("poster_${item.id}")
  ) {
    when (val visual = item.visual) {
      is PosterVisual.DrawableArt -> {
        Image(
          painter = painterResource(id = visual.resId),
          contentDescription = item.title,
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize(),
        )
      }
      is PosterVisual.ProceduralArt -> {
        ProceduralPosterArt(style = visual.style, modifier = Modifier.fillMaxSize())
      }
    }

    // Bottom vignette for poster title readability
    Box(
      modifier =
        Modifier.fillMaxWidth()
          .height(92.dp)
          .align(Alignment.BottomCenter)
          .background(
            Brush.verticalGradient(
              colors =
                listOf(
                  Color.Transparent,
                  NavyBackground.copy(alpha = 0.85f),
                  NavyBackground.copy(alpha = 0.96f),
                )
            )
          )
    )

    if (item.badge != null) {
      Surface(
        color = TogetherBlue,
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier.align(Alignment.TopStart).padding(8.dp),
      ) {
        Text(
          text = item.badge,
          style =
            MaterialTheme.typography.labelSmall.copy(
              fontSize = 9.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 0.8.sp,
            ),
          color = TextPrimary,
          modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
        )
      }
    }

    Column(
      modifier =
        Modifier.align(Alignment.BottomStart)
          .fillMaxWidth()
          .padding(10.dp),
      verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
      Text(
        text = item.title,
        style =
          MaterialTheme.typography.labelMedium.copy(
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.6.sp,
          ),
        color = TextPrimary,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
      )
      Text(
        text = item.subtitle,
        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
        color = TextMuted,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
      )
    }
  }
}

@Composable
private fun ProceduralPosterArt(
  style: PosterArtworkStyle,
  modifier: Modifier = Modifier,
) {
  Canvas(modifier = modifier) {
    val w = size.width
    val h = size.height
    when (style) {
      PosterArtworkStyle.ORBIT_MONOLITH -> {
        drawRect(
          brush =
            Brush.linearGradient(
              colors = listOf(Color(0xFF0A1428), Color(0xFF102A54), Color(0xFF060B16)),
              start = Offset.Zero,
              end = Offset(w, h),
            )
        )
        drawCircle(
          brush =
            Brush.radialGradient(
              colors = listOf(TogetherBlueBright.copy(alpha = 0.55f), Color.Transparent),
              center = Offset(w * 0.5f, h * 0.36f),
              radius = w * 0.48f,
            ),
          center = Offset(w * 0.5f, h * 0.36f),
          radius = w * 0.48f,
        )
        drawCircle(
          color = TogetherBlueGlow.copy(alpha = 0.7f),
          radius = w * 0.23f,
          center = Offset(w * 0.5f, h * 0.36f),
          style = Stroke(width = 2.5.dp.toPx()),
        )
        drawRect(
          color = Color(0xFF050A14),
          topLeft = Offset(w * 0.43f, h * 0.25f),
          size = androidx.compose.ui.geometry.Size(w * 0.14f, h * 0.44f),
        )
      }
      PosterArtworkStyle.SONAR_ABYSS -> {
        drawRect(
          brush =
            Brush.verticalGradient(
              colors = listOf(Color(0xFF091B36), Color(0xFF061022), Color(0xFF040812))
            )
        )
        listOf(0.16f, 0.28f, 0.40f, 0.54f).forEachIndexed { idx, factor ->
          drawCircle(
            color = TogetherBlueBright.copy(alpha = 0.55f - idx * 0.11f),
            radius = w * factor,
            center = Offset(w * 0.5f, h * 0.42f),
            style = Stroke(width = 1.6.dp.toPx()),
          )
        }
        drawCircle(
          color = TogetherBlueGlow,
          radius = 4.dp.toPx(),
          center = Offset(w * 0.5f, h * 0.42f),
        )
      }
      PosterArtworkStyle.AURORA_PEAK -> {
        drawRect(
          brush =
            Brush.verticalGradient(
              colors = listOf(Color(0xFF081426), Color(0xFF163D73), Color(0xFF080F1E))
            )
        )
        drawCircle(
          brush =
            Brush.radialGradient(
              colors = listOf(TogetherBlueGlow.copy(alpha = 0.42f), Color.Transparent),
              center = Offset(w * 0.65f, h * 0.26f),
              radius = w * 0.55f,
            ),
          center = Offset(w * 0.65f, h * 0.26f),
          radius = w * 0.55f,
        )
        val mountain =
          Path().apply {
            moveTo(0f, h * 0.72f)
            lineTo(w * 0.34f, h * 0.45f)
            lineTo(w * 0.58f, h * 0.58f)
            lineTo(w * 0.82f, h * 0.38f)
            lineTo(w, h * 0.62f)
            lineTo(w, h)
            lineTo(0f, h)
            close()
          }
        drawPath(path = mountain, color = Color(0xFF050B16))
      }
    }
  }
}

@Composable
private fun CategoryCatalogContent(
  headerTitle: String,
  rows: List<CatalogRow>,
) {
  LazyColumn(
    modifier = Modifier.fillMaxSize().statusBarsPadding(),
    contentPadding = PaddingValues(top = 16.dp, bottom = 28.dp),
  ) {
    item {
      Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
      ) {
        Text(
          text = stringResource(R.string.brand_wordmark),
          style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 3.sp),
          color = TogetherBlueBright,
        )
        Text(
          text = headerTitle,
          style = MaterialTheme.typography.headlineLarge,
          color = TextPrimary,
        )
      }
    }
    items(items = rows, key = { it.id }) { row -> PosterCarouselSection(row = row) }
  }
}

@Composable
private fun SearchCatalogContent() {
  var query by rememberSaveable { mutableStateOf("") }
  val filtered =
    remember(query) {
      val trimmed = query.trim()
      if (trimmed.isEmpty()) {
        CatalogData.allItems
      } else {
        CatalogData.allItems.filter {
          it.title.contains(trimmed, ignoreCase = true) ||
            it.subtitle.contains(trimmed, ignoreCase = true)
        }
      }
    }

  Column(
    modifier = Modifier.fillMaxSize().statusBarsPadding().padding(top = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    OutlinedTextField(
      value = query,
      onValueChange = { query = it },
      placeholder = { Text(text = "Filme und Serien suchen", color = TextMuted) },
      leadingIcon = {
        Icon(
          imageVector = Icons.Outlined.Search,
          contentDescription = null,
          tint = TogetherBlueBright,
        )
      },
      singleLine = true,
      shape = RoundedCornerShape(12.dp),
      colors =
        OutlinedTextFieldDefaults.colors(
          focusedBorderColor = TogetherBlueBright,
          unfocusedBorderColor = NavyBorder,
          focusedContainerColor = NavySurfaceElevated,
          unfocusedContainerColor = NavySurface,
          focusedTextColor = TextPrimary,
          unfocusedTextColor = TextPrimary,
          cursorColor = TogetherBlueBright,
        ),
      modifier =
        Modifier.fillMaxWidth()
          .padding(horizontal = 20.dp)
          .testTag("search_input"),
    )

    LazyColumn(
      contentPadding = PaddingValues(horizontal = 20.dp, vertical = 4.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp),
      modifier = Modifier.fillMaxSize(),
    ) {
      val rows = filtered.chunked(3)
      items(items = rows) { rowItems ->
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
          rowItems.forEach { item ->
            DecorativePosterCard(
              item = item,
              modifier = Modifier.weight(1f),
            )
          }
          repeat(3 - rowItems.size) { Spacer(modifier = Modifier.weight(1f)) }
        }
      }
    }
  }
}

@Composable
private fun AccountOverviewContent() {
  val context = LocalContext.current
  val permissionStatus = remember(context) { MediaPermissionHelper.checkCurrentAccessStatus(context) }

  Column(
    modifier =
      Modifier.fillMaxSize()
        .statusBarsPadding()
        .padding(horizontal = 20.dp, vertical = 24.dp),
    verticalArrangement = Arrangement.spacedBy(20.dp),
  ) {
    Text(
      text = "Konto",
      style = MaterialTheme.typography.headlineLarge,
      color = TextPrimary,
    )

    Surface(
      color = NavySurfaceElevated,
      shape = RoundedCornerShape(14.dp),
      border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder),
      modifier = Modifier.fillMaxWidth().widthIn(max = 560.dp),
    ) {
      Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
      ) {
        Text(
          text = stringResource(R.string.brand_wordmark),
          style = MaterialTheme.typography.labelMedium.copy(letterSpacing = 3.sp),
          color = TogetherBlueBright,
        )
        Text(
          text = "Medienberechtigung",
          style = MaterialTheme.typography.titleMedium,
          color = TextPrimary,
        )
        Text(
          text =
            when (permissionStatus) {
              MediaAccessStatus.FULL -> "Vollzugriff auf Fotos und Videos erteilt"
              MediaAccessStatus.PARTIAL -> "Eingeschränkter Zugriff auf ausgewählte Fotos/Videos"
              MediaAccessStatus.DENIED -> "Kein Zugriff auf Fotos oder Videos"
            },
          style = MaterialTheme.typography.bodyMedium,
          color = TextSecondary,
        )
        Text(
          text = "Cloud-Synchronisierung (Google Drive) ist in dieser Version noch nicht aktiviert.",
          style = MaterialTheme.typography.bodyMedium,
          color = TextMuted,
        )
      }
    }
  }
}
