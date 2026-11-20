package com.example.ui.model

import androidx.annotation.DrawableRes
import com.example.R

enum class CatalogCategory {
  MOVIE,
  SERIES,
}

enum class PosterArtworkStyle {
  ORBIT_MONOLITH,
  SONAR_ABYSS,
  AURORA_PEAK,
}

sealed interface PosterVisual {
  data class DrawableArt(@DrawableRes val resId: Int) : PosterVisual
  data class ProceduralArt(val style: PosterArtworkStyle) : PosterVisual
}

data class MediaTitleItem(
  val id: String,
  val title: String,
  val subtitle: String,
  val category: CatalogCategory,
  val badge: String?,
  val visual: PosterVisual,
)

data class CatalogRow(
  val id: String,
  val title: String,
  val items: List<MediaTitleItem>,
)

object CatalogData {
  val trendingMovies =
    listOf(
      MediaTitleItem(
        id = "m_1",
        title = "ORBITAL DRIFT",
        subtitle = "2026 • Sci-Fi",
        category = CatalogCategory.MOVIE,
        badge = "4K",
        visual = PosterVisual.DrawableArt(R.drawable.img_poster_horizon),
      ),
      MediaTitleItem(
        id = "m_2",
        title = "ECLIPSE PROTOCOL",
        subtitle = "2025 • Thriller",
        category = CatalogCategory.MOVIE,
        badge = "TOP 10",
        visual = PosterVisual.ProceduralArt(PosterArtworkStyle.ORBIT_MONOLITH),
      ),
      MediaTitleItem(
        id = "m_3",
        title = "GLASSTURM",
        subtitle = "2026 • Abenteuer",
        category = CatalogCategory.MOVIE,
        badge = "NEU",
        visual = PosterVisual.DrawableArt(R.drawable.img_poster_chronicle),
      ),
      MediaTitleItem(
        id = "m_4",
        title = "TIEFE STILLE",
        subtitle = "2025 • Mystery",
        category = CatalogCategory.MOVIE,
        badge = null,
        visual = PosterVisual.ProceduralArt(PosterArtworkStyle.SONAR_ABYSS),
      ),
      MediaTitleItem(
        id = "m_5",
        title = "NACHTHAFEN",
        subtitle = "2026 • Neo-Noir",
        category = CatalogCategory.MOVIE,
        badge = "4K",
        visual = PosterVisual.DrawableArt(R.drawable.img_poster_nordic),
      ),
      MediaTitleItem(
        id = "m_6",
        title = "POLARLICHT",
        subtitle = "2025 • Drama",
        category = CatalogCategory.MOVIE,
        badge = null,
        visual = PosterVisual.ProceduralArt(PosterArtworkStyle.AURORA_PEAK),
      ),
    )

  val topSeries =
    listOf(
      MediaTitleItem(
        id = "s_1",
        title = "KAI DER STILLE",
        subtitle = "Staffel 2",
        category = CatalogCategory.SERIES,
        badge = "TOP 10",
        visual = PosterVisual.DrawableArt(R.drawable.img_poster_nordic),
      ),
      MediaTitleItem(
        id = "s_2",
        title = "CHRONIK DER SEE",
        subtitle = "Staffel 1",
        category = CatalogCategory.SERIES,
        badge = "NEU",
        visual = PosterVisual.DrawableArt(R.drawable.img_poster_chronicle),
      ),
      MediaTitleItem(
        id = "s_3",
        title = "SIGNAL 09",
        subtitle = "Staffel 3",
        category = CatalogCategory.SERIES,
        badge = null,
        visual = PosterVisual.ProceduralArt(PosterArtworkStyle.SONAR_ABYSS),
      ),
      MediaTitleItem(
        id = "s_4",
        title = "STERNENRAND",
        subtitle = "Staffel 1",
        category = CatalogCategory.SERIES,
        badge = "4K",
        visual = PosterVisual.DrawableArt(R.drawable.img_poster_horizon),
      ),
      MediaTitleItem(
        id = "s_5",
        title = "NORDWACHT",
        subtitle = "Staffel 2",
        category = CatalogCategory.SERIES,
        badge = null,
        visual = PosterVisual.ProceduralArt(PosterArtworkStyle.AURORA_PEAK),
      ),
      MediaTitleItem(
        id = "s_6",
        title = "MONOLITH",
        subtitle = "Miniserie",
        category = CatalogCategory.SERIES,
        badge = "TOP 10",
        visual = PosterVisual.ProceduralArt(PosterArtworkStyle.ORBIT_MONOLITH),
      ),
    )

  val newReleases =
    listOf(
      MediaTitleItem(
        id = "n_1",
        title = "AEON PROJEKT",
        subtitle = "2026 • Sci-Fi",
        category = CatalogCategory.MOVIE,
        badge = "NEU",
        visual = PosterVisual.ProceduralArt(PosterArtworkStyle.ORBIT_MONOLITH),
      ),
      MediaTitleItem(
        id = "n_2",
        title = "BLAUER NEBEL",
        subtitle = "Staffel 1",
        category = CatalogCategory.SERIES,
        badge = "NEU",
        visual = PosterVisual.DrawableArt(R.drawable.img_poster_nordic),
      ),
      MediaTitleItem(
        id = "n_3",
        title = "KRONENMEER",
        subtitle = "2026 • Fantasy",
        category = CatalogCategory.MOVIE,
        badge = "4K",
        visual = PosterVisual.DrawableArt(R.drawable.img_poster_chronicle),
      ),
      MediaTitleItem(
        id = "n_4",
        title = "ABGRUND",
        subtitle = "Staffel 2",
        category = CatalogCategory.SERIES,
        badge = null,
        visual = PosterVisual.ProceduralArt(PosterArtworkStyle.SONAR_ABYSS),
      ),
      MediaTitleItem(
        id = "n_5",
        title = "KEPLER PASS",
        subtitle = "2026 • Sci-Fi",
        category = CatalogCategory.MOVIE,
        badge = "NEU",
        visual = PosterVisual.DrawableArt(R.drawable.img_poster_horizon),
      ),
    )

  val cinemaHighlights =
    listOf(
      MediaTitleItem(
        id = "c_1",
        title = "WINTERGIPFEL",
        subtitle = "2025 • Dokumentar",
        category = CatalogCategory.MOVIE,
        badge = "4K",
        visual = PosterVisual.ProceduralArt(PosterArtworkStyle.AURORA_PEAK),
      ),
      MediaTitleItem(
        id = "c_2",
        title = "STURM & LICHT",
        subtitle = "2026 • Drama",
        category = CatalogCategory.MOVIE,
        badge = null,
        visual = PosterVisual.DrawableArt(R.drawable.img_poster_chronicle),
      ),
      MediaTitleItem(
        id = "c_3",
        title = "MITTERNACHT",
        subtitle = "Staffel 4",
        category = CatalogCategory.SERIES,
        badge = "TOP 10",
        visual = PosterVisual.DrawableArt(R.drawable.img_poster_nordic),
      ),
      MediaTitleItem(
        id = "c_4",
        title = "SOLARIS ECHO",
        subtitle = "2025 • Sci-Fi",
        category = CatalogCategory.MOVIE,
        badge = null,
        visual = PosterVisual.DrawableArt(R.drawable.img_poster_horizon),
      ),
    )

  val homeRows =
    listOf(
      CatalogRow(id = "row_movies", title = "Beliebte Filme", items = trendingMovies),
      CatalogRow(id = "row_series", title = "Top Serien", items = topSeries),
      CatalogRow(id = "row_new", title = "Neuheiten", items = newReleases),
      CatalogRow(id = "row_highlights", title = "Kino-Highlights", items = cinemaHighlights),
    )

  val allItems: List<MediaTitleItem> =
    (trendingMovies + topSeries + newReleases + cinemaHighlights).distinctBy { it.title }
}
