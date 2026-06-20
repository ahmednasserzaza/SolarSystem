package com.worldcup.solarsystem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlin.math.roundToInt
import androidx.compose.ui.graphics.shadow.Shadow as LayerShadow

private val SpaceBlack = Color(0xFF0D0608)
private val White = Color(0xFFFFFFFF)
private val White88 = Color(0xE0FFFFFF)
private val White80 = Color(0xCCFFFFFF)
private val White66 = Color(0xA8FFFFFF)
private val White16 = Color(0x29FFFFFF)
private val CardFill = Color(0xFF0B1223)
private val CardBorder = Color(0xFF2F2E2E)
private val StatNoteColor = Color(0xFF8B94A9)
private val EarthGlow = Color(0x404197E7)
private val HintShadow = Color(0x70FFFFFF)

private val StartSkyGradient = listOf(
    Color(0x00000000),
    Color(0xFF060816),
    Color(0xFF0F172A),
    Color(0xFF020D3C),
)

private val EndSkyGradient = listOf(
    Color(0xFF1E1B4B),
    Color(0xFF0F172A),
    Color(0xFF030712),
)

private val RubikFamily = FontFamily(
    Font(R.font.rubik_regular, FontWeight.Normal),
    Font(R.font.rubik_medium, FontWeight.Medium),
    Font(R.font.rubik_bold, FontWeight.Bold),
)

private val ScriptFamily = FontFamily(Font(R.font.lily_script_one_regular, FontWeight.Normal))

private val HeroTitleStyle = TextStyle(
    fontFamily = RubikFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 64.sp,
    letterSpacing = 0.25.sp,
    color = White88,
    textAlign = TextAlign.Center,
    shadow = Shadow(White16, Offset.Zero, 16f),
)

private val HeroSubtitleStyle = TextStyle(
    fontFamily = ScriptFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 16.sp,
    letterSpacing = 0.25.sp,
    color = White80,
    textAlign = TextAlign.Center,
)

private val SwipeHintStyle = TextStyle(
    fontFamily = RubikFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 16.sp,
    letterSpacing = 0.25.sp,
    color = White,
    shadow = Shadow(HintShadow, Offset(0f, 4f), 16f),
)

private val HeaderTitleStyle = TextStyle(
    fontFamily = RubikFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 24.sp,
    letterSpacing = 0.25.sp,
    color = White88,
    textAlign = TextAlign.Center,
    shadow = Shadow(White16, Offset.Zero, 12f),
)

private val HeaderSubtitleStyle = TextStyle(
    fontFamily = ScriptFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    letterSpacing = 0.25.sp,
    color = White80,
    textAlign = TextAlign.Center,
)

private val PlanetNameStyle = TextStyle(
    fontFamily = RubikFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 18.sp,
    color = White88,
)

private val PlanetTaglineStyle = TextStyle(
    fontFamily = RubikFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    color = White66,
)

private val StatLabelStyle = TextStyle(
    fontFamily = RubikFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp,
    color = White66,
)

private val StatValueStyle = TextStyle(
    fontFamily = RubikFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 12.sp,
    color = White88,
)

private val StatNoteStyle = TextStyle(
    fontFamily = RubikFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp,
    lineHeight = 18.sp,
    color = White66,
)

private val HeaderEarthSize = 220.dp
private val EndHeaderHeight = 300.dp
private val CardHeight = 300.dp
private val CardGap = 22.dp
private val CardStackPeek = 16.dp
private const val BackPlanetMinAlpha = 0.35f
private val CardEnterRise = 60.dp
private val PlanetOverhang = 20.dp
private val PlanetImageSize = 112.dp
private val PlanetGlowBlur = 100.dp
private val EarthGlowBlur = 50.dp
private val EarthGlowOffset = (-12).dp

private const val CardEnterDurationMs = 450
private const val CardStaggerMs = 80

private val TransitionSpring = spring<Float>(dampingRatio = 0.80f, stiffness = 16f)
private val EnterTween = tween<Float>(durationMillis = 700, easing = FastOutSlowInEasing)

private enum class CardSlot { Card, Header }

@Immutable
private data class Planet(
    val imageRes: Int,
    val name: String,
    val tagline: String,
    val weight: String,
    val dayLength: String,
    val temperature: String,
    val temperatureNote: String,
    val additionalInfo: String,
    val glowColor: Color,
)

private val planets = listOf(
    Planet(
        R.drawable.saturn, "Saturn", "The Ring Master",
        "70kg → 74kg", "10.7 Hours", "-178°C", "Bring a jacket", "Lighter than water",
        Color(0x80AB4F20),
    ),
    Planet(
        R.drawable.mars, "Mars", "The next colony",
        "70kg → 27kg", "24.6 Hours", "-63°C", "Chilly nights", "Has the tallest volcano",
        Color(0x80FF844E),
    ),
    Planet(
        R.drawable.jupiter, "Jupiter", "The Giant",
        "70kg → 177kg", "9.9 Hours", "-145°C", "Crushing storms", "Great Red Spot still rages",
        Color(0x80FF8332),
    ),
    Planet(
        R.drawable.mercury, "Mercury", "The Swift One",
        "70kg → 26kg", "4222.6 Hours", "167°C", "Scorching days", "Closest to the Sun",
        Color(0x80095B91),
    ),
    Planet(
        R.drawable.venus, "Venus", "Earth's Twin",
        "70kg → 63kg", "2802 Hours", "464°C", "Hotter than an oven", "Spins backwards",
        Color(0x80C69E4A),
    ),
    Planet(
        R.drawable.uranus, "Uranus", "The Sideways Giant",
        "70kg → 62kg", "17.2 Hours", "-224°C", "Coldest of all", "Rolls on its side",
        Color(0x8031CFDB),
    ),
    Planet(
        R.drawable.neptune, "Neptune", "The Windy World",
        "70kg → 79kg", "16.1 Hours", "-214°C", "Brutal winds", "Supersonic storms",
        Color(0x802CA6DB),
    ),
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { SolarSystemScreen() }
    }
}

private fun startAlpha(progress: Float) = (1f - progress / 0.5f).coerceIn(0f, 1f)

private fun endAlpha(progress: Float) = ((progress - 0.5f) / 0.5f).coerceIn(0f, 1f)

@Composable
private fun SolarSystemScreen() {
    val density = LocalDensity.current
    val topInset = WindowInsets.systemBars.asPaddingValues().calculateTopPadding()

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val screenHeightPx = with(density) { maxHeight.toPx() }
        val headerEarthPx = with(density) { HeaderEarthSize.toPx() }
        val earthTopEnd = topInset + (EndHeaderHeight - HeaderEarthSize) / 2
        val earthCenterEnd = with(density) { earthTopEnd.toPx() } + headerEarthPx / 2f
        val startEarthWidth = with(density) { maxWidth.toPx() } * 2f
        val earthCenterStart = screenHeightPx - startEarthWidth * 0.24f
        val earthScaleStart = startEarthWidth / headerEarthPx
        val earthRise = earthCenterStart - earthCenterEnd

        val stackAnchor = with(density) { (topInset + EndHeaderHeight).toPx() }
        var cardHeightPx by remember { mutableFloatStateOf(with(density) { CardHeight.toPx() }) }
        val stepPx = cardHeightPx + with(density) { CardGap.toPx() }
        val peekPx = with(density) { CardStackPeek.toPx() }
        val transitionPx = (screenHeightPx - stackAnchor).coerceAtLeast(1f)

        val pagerState = rememberPagerState(pageCount = { planets.size + 1 })
        val scroll = {
            val page = pagerState.currentPage + pagerState.currentPageOffsetFraction
            if (page <= 1f) page * transitionPx
            else transitionPx + (page - 1f) * (stepPx - peekPx)
        }
        val progress = { (scroll() / transitionPx).coerceIn(0f, 1f) }

        val enterFromPx = -with(density) { (EndHeaderHeight + topInset).toPx() }
        val inSecondState = pagerState.currentPage >= 1

        var titleEntered by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) { titleEntered = true }
        val titleLaunch by animateFloatAsState(
            targetValue = if (titleEntered) 0f else enterFromPx,
            animationSpec = EnterTween,
            label = "titleLaunch",
        )

        Box(modifier = Modifier.fillMaxSize()) {
            Box(Modifier.fillMaxSize().background(SpaceBlack))

            Box(Modifier.fillMaxSize().background(Brush.verticalGradient(StartSkyGradient)))

            Box(
                Modifier
                    .fillMaxSize()
                    .graphicsLayer { alpha = endAlpha(progress()) }
                    .background(Brush.verticalGradient(EndSkyGradient))
            )

            StarBackground(Modifier.alpha(0.4f))

            VerticalPager(
                state = pagerState,
                flingBehavior = PagerDefaults.flingBehavior(
                    state = pagerState,
                    snapAnimationSpec = TransitionSpring,
                ),
                modifier = Modifier.fillMaxSize(),
            ) { }

            planets.forEachIndexed { index, planet ->
                val cardEnter by animateFloatAsState(
                    targetValue = if (inSecondState) 1f else 0f,
                    animationSpec = tween(
                        durationMillis = CardEnterDurationMs,
                        delayMillis = (if (inSecondState) index else planets.lastIndex - index) * CardStaggerMs,
                        easing = FastOutSlowInEasing,
                    ),
                    label = "cardEnter$index",
                )
                PlanetCard(
                    planet = planet,
                    planetAlpha = {
                        val frontPos = ((scroll() - transitionPx) / (stepPx - peekPx))
                            .coerceAtLeast(0f)
                        val depth = (frontPos - index).coerceIn(0f, 1f)
                        1f - depth * (1f - BackPlanetMinAlpha)
                    },
                    modifier = Modifier
                        .zIndex(index.toFloat())
                        .fillMaxWidth()
                        .onSizeChanged { cardHeightPx = it.height.toFloat() }
                        .offset {
                            val natural = screenHeightPx + index * stepPx - scroll()
                            val stick = stackAnchor + index * peekPx
                            IntOffset(0, natural.coerceAtLeast(stick).roundToInt())
                        }
                        .graphicsLayer {
                            translationY = (1f - cardEnter) * CardEnterRise.toPx()
                        },
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(planets.size.toFloat()),
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = earthTopEnd)
                        .size(HeaderEarthSize)
                        .graphicsLayer {
                            val p = progress()
                            val scale = earthScaleStart + (1f - earthScaleStart) * p
                            scaleX = scale
                            scaleY = scale
                            translationY = (1f - p) * earthRise
                        },
                ) {
                    Image(
                        painter = painterResource(R.drawable.earth),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(EarthGlow.copy(alpha = 1f)),
                        modifier = Modifier
                            .matchParentSize()
                            .offset(y = EarthGlowOffset)
                            .blur(EarthGlowBlur, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                            .graphicsLayer { alpha = EarthGlow.alpha * (1f - progress()) },
                    )
                    Image(
                        painter = painterResource(R.drawable.earth),
                        contentDescription = stringResource(R.string.cd_earth),
                        modifier = Modifier
                            .matchParentSize()
                            .graphicsLayer { alpha = 1f - progress() * 0.5f },
                    )
                }
                HeaderTexts(
                    topInset = topInset,
                    modifier = Modifier.graphicsLayer {
                        val enter = ((progress() - 0.5f) / 0.5f).coerceIn(0f, 1f)
                        translationY = (1f - enter) * enterFromPx
                    },
                )
            }

            TitleBlock(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .zIndex(planets.size.toFloat())
                    .graphicsLayer {
                        val exit = (progress() / 0.5f).coerceIn(0f, 1f)
                        translationY = titleLaunch + exit * enterFromPx
                    },
            )

            SwipeHint(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .zIndex(planets.size.toFloat())
                    .graphicsLayer { alpha = startAlpha(progress()) },
            )
        }
    }
}

@Composable
private fun StarBackground(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.art_space_background),
        contentDescription = stringResource(R.string.cd_space_background),
        contentScale = ContentScale.Crop,
        modifier = modifier.fillMaxSize(),
    )
}

@Composable
private fun TitleBlock(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .systemBarsPadding()
            .fillMaxWidth()
            .padding(top = 56.dp, start = 24.dp, end = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(text = stringResource(R.string.earth_title), style = HeroTitleStyle)
        Text(text = stringResource(R.string.earth_subtitle), style = HeroSubtitleStyle)
    }
}

@Composable
private fun HeaderTexts(topInset: Dp, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = topInset)
            .height(EndHeaderHeight),
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(text = stringResource(R.string.solar_system_title), style = HeaderTitleStyle)
            Text(text = stringResource(R.string.solar_system_subtitle), style = HeaderSubtitleStyle)
        }
    }
}

@Composable
private fun SwipeHint(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .systemBarsPadding()
            .fillMaxWidth()
            .padding(bottom = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Bottom),
    ) {
        Column {
            UpChevron(Modifier.size(28.dp).alpha(0.5f))
            UpChevron(Modifier.size(28.dp).alpha(0.6f))
            UpChevron(Modifier.size(28.dp).alpha(0.9f))
        }
        Text(text = stringResource(R.string.swipe_to_explore), style = SwipeHintStyle)
    }
}

@Composable
private fun UpChevron(modifier: Modifier = Modifier) {
    Icon(
        painter = painterResource(R.drawable.icon_arrow_up),
        contentDescription = null,
        tint = White,
        modifier = modifier.dropShadow(
            shape = RoundedCornerShape(24.dp),
            shadow = LayerShadow(
                color = HintShadow,
                radius = 16.dp,
                spread = 0.dp,
                offset = DpOffset(0.dp, 4.dp),
            ),
        ),
    )
}

@Composable
private fun PlanetCard(planet: Planet, planetAlpha: () -> Float, modifier: Modifier = Modifier) {
    SubcomposeLayout(modifier = modifier.padding(horizontal = 20.dp)) { constraints ->
        val header = subcompose(CardSlot.Header) { PlanetHeader(planet, planetAlpha) }
            .first()
            .measure(constraints.copy(minWidth = 0, minHeight = 0))

        val overhang = PlanetOverhang.roundToPx().coerceIn(0, header.height)
        val contentTopInset = (header.height - overhang).toDp()

        val card = subcompose(CardSlot.Card) {
            PlanetCardSurface(planet, contentTopInset)
        }.first().measure(
            constraints.copy(
                minWidth = 0,
                minHeight = 0,
                maxHeight = (constraints.maxHeight - overhang).coerceAtLeast(0),
            ),
        )

        layout(card.width, overhang + card.height) {
            card.placeRelative(0, overhang)
            header.placeRelative(0, 0)
        }
    }
}

@Composable
private fun PlanetCardSurface(planet: Planet, contentTopInset: Dp) {
    val shape = RoundedCornerShape(20.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(CardFill, shape)
            .border(0.5.dp, CardBorder, shape)
            .padding(start = 20.dp, top = contentTopInset, end = 20.dp, bottom = 20.dp),
    ) {
        PlanetStats(planet)
    }
}

@Composable
private fun PlanetImage(planet: Planet, alpha: () -> Float, modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(planet.imageRes),
            contentDescription = null,
            colorFilter = ColorFilter.tint(planet.glowColor),
            modifier = Modifier
                .size(PlanetImageSize)
                .blur(PlanetGlowBlur, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                .graphicsLayer { this.alpha = planet.glowColor.alpha * alpha() },
        )
        Image(
            painter = painterResource(planet.imageRes),
            contentDescription = planet.name,
            modifier = Modifier
                .size(PlanetImageSize)
                .graphicsLayer { this.alpha = alpha() },
        )
    }
}

@Composable
private fun PlanetHeader(planet: Planet, planetAlpha: () -> Float, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(9.dp),
    ) {
        PlanetImage(planet, alpha = planetAlpha)
        Column(
            modifier = Modifier.padding(top = 14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(text = planet.name, style = PlanetNameStyle)
            Text(text = planet.tagline, style = PlanetTaglineStyle)
        }
    }
}

@Composable
private fun PlanetStats(planet: Planet) {
    Column(Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            StatCell(R.drawable.icon_weight, stringResource(R.string.stat_weight), planet.weight, null, Modifier.weight(1f))
            StatDivider()
            StatCell(R.drawable.icon_sun, stringResource(R.string.stat_day), planet.dayLength, null, Modifier.weight(1f))
        }
        HorizontalDivider(
            color = White16,
            thickness = 0.5.dp,
            modifier = Modifier.padding(vertical = 16.dp),
        )
        Row(
            modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            StatCell(R.drawable.icon_thermometer, stringResource(R.string.stat_temperature), planet.temperature, planet.temperatureNote, Modifier.weight(1f))
            StatDivider()
            StatCell(R.drawable.icon_info, stringResource(R.string.stat_additional), planet.additionalInfo, null, Modifier.weight(1f))
        }
    }
}

@Composable
private fun StatDivider() {
    VerticalDivider(
        thickness = 0.5.dp,
        color = White16,
        modifier = Modifier
            .height(30.dp)
            .padding(horizontal = 16.dp),
    )
}

@Composable
private fun StatCell(
    iconRes: Int,
    label: String,
    value: String,
    note: String?,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = White66,
            modifier = Modifier.size(20.dp),
        )
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = label, style = StatLabelStyle)
            if (note == null) {
                Text(text = value, style = StatValueStyle)
            } else {
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(color = White88, fontWeight = FontWeight.Medium)) { append(value) }
                        withStyle(SpanStyle(color = StatNoteColor)) { append(", $note") }
                    },
                    style = StatNoteStyle,
                )
            }
        }
    }
}
