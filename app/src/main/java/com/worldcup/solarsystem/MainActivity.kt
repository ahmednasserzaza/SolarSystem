package com.worldcup.solarsystem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.foundation.pager.PagerState
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
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.roundToInt
import kotlin.math.sin

// region Design tokens -------------------------------------------------------

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

private val StartSkyBrush = Brush.verticalGradient(StartSkyGradient)
private val EndSkyBrush = Brush.verticalGradient(EndSkyGradient)

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
    letterSpacing = TextUnit(0.25f, TextUnitType.Sp),
    color = White88,
)

private val PlanetTaglineStyle = TextStyle(
    fontFamily = RubikFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    letterSpacing = 0.25.sp,
    color = White66,
)

private val StatLabelStyle = TextStyle(
    fontFamily = RubikFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp,
    letterSpacing = 0.25.sp,
    color = White66,
)

private val StatValueStyle = TextStyle(
    fontFamily = RubikFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 12.sp,
    letterSpacing = 0.25.sp,
    color = White88,
)

private val StatNoteStyle = TextStyle(
    fontFamily = RubikFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp,
    letterSpacing = 0.25.sp,
    lineHeight = 18.sp,
    color = White66,
)

private val HeaderEarthSize = 220.dp
private val EndHeaderHeight = 300.dp
private val SwipeHintHeight = 160.dp
private val CardHeight = 300.dp
private val CardGap = 16.dp
private val CardStackPeek = 16.dp
private val CardEnterRise = 60.dp
private val PlanetOverhang = 20.dp
private val PlanetImageSize = 112.dp
private val PlanetGlowBlur = 100.dp
private val EarthGlowBlur = 50.dp
private val EarthGlowOffset = (-12).dp
private val ScreenPadding = 24.dp
private val TitleTopPadding = 56.dp
private val SwipeHintBottomPadding = 28.dp
private val ChevronSize = 28.dp
private val ChevronTravel = 10.dp
private const val ChevronCycleMs = 1400
private const val ChevronStaggerMs = 160
private const val ChevronMinAlpha = 0.15f

private const val BackPlanetMinAlpha = 0.35f
private const val StarFieldAlpha = 0.4f
private const val EarthImageFade = 0.5f
private const val EarthStartWidthFactor = 2f
private const val EarthStartCenterFactor = 0.24f
private const val MidProgress = 0.5f
private const val CardEnterDurationMs = 450
private const val CardStaggerMs = 80

private val TransitionSpring = spring<Float>(dampingRatio = 0.80f, stiffness = 16f)
private val EnterTween = tween<Float>(durationMillis = 700, easing = FastOutSlowInEasing)

// endregion

// region Transition curves ---------------------------------------------------

private fun startAlpha(progress: Float) = (1f - progress / MidProgress).coerceIn(0f, 1f)

private fun endAlpha(progress: Float) = ((progress - MidProgress) / MidProgress).coerceIn(0f, 1f)

private fun exitFraction(progress: Float) = 1f - startAlpha(progress)

// endregion

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

@Immutable
private class TransitionMetrics(
    private val screenHeightPx: Float,
    private val headerEarthPx: Float,
    val earthTopEnd: Dp,
    private val earthCenterEnd: Float,
    val earthScaleStart: Float,
    val earthRise: Float,
    private val stackAnchor: Float,
    private val earthCardGap: Float,
    private val stepPx: Float,
    private val peekPx: Float,
    val transitionPx: Float,
    val enterFromPx: Float,
    val hintEnterFromPx: Float,
) {
    fun scrollOf(page: Float): Float =
        if (page <= 1f) page * transitionPx
        else transitionPx + (page - 1f) * (stepPx - peekPx)

    fun progressOf(scroll: Float): Float = (scroll / transitionPx).coerceIn(0f, 1f)

    fun earthScale(progress: Float): Float = lerp(earthScaleStart, 1f, progress)

    fun cardOffsetY(index: Int, scroll: Float, progress: Float): Int {
        val natural = screenHeightPx + index * stepPx - scroll
        val stick = stackTop(progress) + index * peekPx
        return natural.coerceAtLeast(stick).roundToInt()
    }

    fun planetDepthAlpha(index: Int, scroll: Float): Float {
        val frontPos = ((scroll - transitionPx) / (stepPx - peekPx)).coerceAtLeast(0f)
        val depth = (frontPos - index).coerceIn(0f, 1f)
        return 1f - depth * (1f - BackPlanetMinAlpha)
    }

    private fun stackTop(progress: Float): Float {
        val earthBottom = earthCenterEnd + (1f - progress) * earthRise +
            headerEarthPx / 2f * earthScale(progress)
        return (earthBottom + earthCardGap).coerceAtLeast(stackAnchor)
    }
}

@Composable
private fun rememberTransitionMetrics(
    maxWidth: Dp,
    maxHeight: Dp,
    topInset: Dp,
    bottomInset: Dp,
    cardHeightPx: Float,
): TransitionMetrics {
    val density = LocalDensity.current
    return remember(maxWidth, maxHeight, topInset, bottomInset, cardHeightPx, density) {
        with(density) {
            val screenHeightPx = maxHeight.toPx()
            val headerEarthPx = HeaderEarthSize.toPx()
            val earthTopEnd = topInset + (EndHeaderHeight - HeaderEarthSize) / 2
            val earthCenterEnd = earthTopEnd.toPx() + headerEarthPx / 2f
            val startEarthWidth = maxWidth.toPx() * EarthStartWidthFactor
            val earthCenterStart = screenHeightPx - startEarthWidth * EarthStartCenterFactor
            val stackAnchor = (topInset + EndHeaderHeight).toPx()
            val earthBottomEnd = earthCenterEnd + headerEarthPx / 2f
            TransitionMetrics(
                screenHeightPx = screenHeightPx,
                headerEarthPx = headerEarthPx,
                earthTopEnd = earthTopEnd,
                earthCenterEnd = earthCenterEnd,
                earthScaleStart = startEarthWidth / headerEarthPx,
                earthRise = earthCenterStart - earthCenterEnd,
                stackAnchor = stackAnchor,
                earthCardGap = stackAnchor - earthBottomEnd,
                stepPx = cardHeightPx + CardGap.toPx(),
                peekPx = CardStackPeek.toPx(),
                transitionPx = (screenHeightPx - stackAnchor).coerceAtLeast(1f),
                enterFromPx = -(EndHeaderHeight + topInset).toPx(),
                hintEnterFromPx = (SwipeHintHeight + bottomInset).toPx(),
            )
        }
    }
}

@Composable
private fun SolarSystemScreen() {
    val density = LocalDensity.current
    val insets = WindowInsets.systemBars.asPaddingValues()
    val topInset = insets.calculateTopPadding()
    val bottomInset = insets.calculateBottomPadding()

    BoxWithConstraints(Modifier.fillMaxSize()) {
        var cardHeightPx by remember { mutableFloatStateOf(with(density) { CardHeight.toPx() }) }
        val metrics = rememberTransitionMetrics(maxWidth, maxHeight, topInset, bottomInset, cardHeightPx)
        val pagerState = rememberPagerState(pageCount = { planets.size + 1 })
        val scroll = remember(metrics, pagerState) {
            { metrics.scrollOf(pagerState.currentPage + pagerState.currentPageOffsetFraction) }
        }
        val progress = remember(metrics, scroll) { { metrics.progressOf(scroll()) } }
        val topZIndex = planets.size.toFloat()

        Box(Modifier.fillMaxSize()) {
            SkyBackdrop(progress)
            ScrollDriver(pagerState)
            PlanetCardStack(metrics, pagerState, scroll, progress) { cardHeightPx = it }
            HeaderLayer(metrics, topInset, topZIndex, progress)
            AnimatedTitle(metrics.enterFromPx, topZIndex, progress)
            AnimatedSwipeHint(metrics.hintEnterFromPx, topZIndex, progress)
        }
    }
}

@Composable
private fun SkyBackdrop(progress: () -> Float) {
    Box(Modifier.fillMaxSize().background(SpaceBlack))
    Box(Modifier.fillMaxSize().background(StartSkyBrush))
    Box(
        Modifier
            .fillMaxSize()
            .graphicsLayer { alpha = endAlpha(progress()) }
            .background(EndSkyBrush),
    )
    StarBackground(Modifier.alpha(StarFieldAlpha))
}

@Composable
private fun ScrollDriver(pagerState: PagerState, modifier: Modifier = Modifier) {
    VerticalPager(
        state = pagerState,
        flingBehavior = PagerDefaults.flingBehavior(pagerState, snapAnimationSpec = TransitionSpring),
        modifier = modifier.fillMaxSize(),
    ) {}
}

@Composable
private fun PlanetCardStack(
    metrics: TransitionMetrics,
    pagerState: PagerState,
    scroll: () -> Float,
    progress: () -> Float,
    onCardHeight: (Float) -> Unit,
) {
    val cardEnters = remember { List(planets.size) { Animatable(0f) } }
    LaunchedEffect(pagerState, cardEnters) {
        snapshotFlow { pagerState.currentPage >= 1 }.collectLatest { inStack ->
            coroutineScope {
                cardEnters.forEachIndexed { index, anim ->
                    launch {
                        val steps = if (inStack) index else planets.lastIndex - index
                        anim.animateTo(
                            targetValue = if (inStack) 1f else 0f,
                            animationSpec = tween(
                                durationMillis = CardEnterDurationMs,
                                delayMillis = steps * CardStaggerMs,
                                easing = FastOutSlowInEasing,
                            ),
                        )
                    }
                }
            }
        }
    }

    planets.forEachIndexed { index, planet ->
        val cardEnter = cardEnters[index]
        PlanetCard(
            planet = planet,
            planetAlpha = { metrics.planetDepthAlpha(index, scroll()) },
            modifier = Modifier
                .zIndex(index.toFloat())
                .fillMaxWidth()
                .onSizeChanged { onCardHeight(it.height.toFloat()) }
                .offset { IntOffset(0, metrics.cardOffsetY(index, scroll(), progress())) }
                .graphicsLayer { translationY = (1f - cardEnter.value) * CardEnterRise.toPx() },
        )
    }
}

@Composable
private fun HeaderLayer(
    metrics: TransitionMetrics,
    topInset: Dp,
    topZIndex: Float,
    progress: () -> Float,
) {
    Box(Modifier.fillMaxSize().zIndex(topZIndex)) {
        EarthHeader(metrics, progress, Modifier.align(Alignment.TopCenter))
        HeaderTexts(
            topInset = topInset,
            modifier = Modifier.graphicsLayer {
                translationY = (1f - endAlpha(progress())) * metrics.enterFromPx
            },
        )
    }
}

@Composable
private fun EarthHeader(
    metrics: TransitionMetrics,
    progress: () -> Float,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .padding(top = metrics.earthTopEnd)
            .size(HeaderEarthSize)
            .graphicsLayer {
                val p = progress()
                val scale = metrics.earthScale(p)
                scaleX = scale
                scaleY = scale
                translationY = (1f - p) * metrics.earthRise
            },
    ) {
        GlowingImage(
            painter = painterResource(R.drawable.earth),
            contentDescription = stringResource(R.string.cd_earth),
            glowColor = EarthGlow.copy(alpha = 1f),
            glowBlur = EarthGlowBlur,
            imageSize = HeaderEarthSize,
            glowOffsetY = EarthGlowOffset,
            glowAlpha = { EarthGlow.alpha * (1f - progress()) },
            imageAlpha = { 1f - progress() * EarthImageFade },
        )
    }
}

@Composable
private fun BoxScope.AnimatedTitle(enterFromPx: Float, topZIndex: Float, progress: () -> Float) {
    val launch by rememberEnterOffset(enterFromPx)
    TitleBlock(
        modifier = Modifier
            .align(Alignment.TopCenter)
            .zIndex(topZIndex)
            .graphicsLayer { translationY = launch + exitFraction(progress()) * enterFromPx },
    )
}

@Composable
private fun BoxScope.AnimatedSwipeHint(enterFromPx: Float, topZIndex: Float, progress: () -> Float) {
    val launch by rememberEnterOffset(enterFromPx)
    SwipeHint(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .zIndex(topZIndex)
            .graphicsLayer {
                translationY = launch + exitFraction(progress()) * enterFromPx
                alpha = startAlpha(progress())
            },
    )
}

@Composable
private fun rememberEnterOffset(enterFromPx: Float) = run {
    var entered by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { entered = true }
    animateFloatAsState(
        targetValue = if (entered) 0f else enterFromPx,
        animationSpec = EnterTween,
        label = "enterOffset",
    )
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
            .padding(top = TitleTopPadding, start = ScreenPadding, end = ScreenPadding),
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
                .padding(horizontal = ScreenPadding),
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
            .padding(bottom = SwipeHintBottomPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Bottom),
    ) {
        val transition = rememberInfiniteTransition(label = "chevrons")
        Column {
            repeat(3) { index ->
                UpChevron(transition = transition, index = index)
            }
        }
        Text(text = stringResource(R.string.swipe_to_explore), style = SwipeHintStyle)
    }
}

@Composable
private fun UpChevron(
    transition: InfiniteTransition,
    index: Int,
    modifier: Modifier = Modifier,
) {
    val progress = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = ChevronCycleMs, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
            initialStartOffset = StartOffset(index * ChevronStaggerMs),
        ),
        label = "chevron-$index",
    )
    val travelPx = with(LocalDensity.current) { ChevronTravel.toPx() }
    Icon(
        painter = painterResource(R.drawable.icon_arrow_up),
        contentDescription = null,
        tint = White,
        modifier = modifier
            .size(ChevronSize)
            .graphicsLayer {
                val p = progress.value
                alpha = ChevronMinAlpha + (1f - ChevronMinAlpha) * sin(p * PI).toFloat()
                translationY = -travelPx * p
            }
    )
}

@Composable
private fun GlowingImage(
    painter: Painter,
    contentDescription: String?,
    glowColor: Color,
    glowBlur: Dp,
    imageSize: Dp,
    glowAlpha: () -> Float,
    imageAlpha: () -> Float,
    modifier: Modifier = Modifier,
    glowOffsetY: Dp = 0.dp,
) {
    Box(modifier, contentAlignment = Alignment.Center) {
        Image(
            painter = painter,
            contentDescription = null,
            colorFilter = ColorFilter.tint(glowColor),
            modifier = Modifier
                .size(imageSize)
                .offset(y = glowOffsetY)
                .blur(glowBlur, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                .graphicsLayer { alpha = glowAlpha() },
        )
        Image(
            painter = painter,
            contentDescription = contentDescription,
            modifier = Modifier
                .size(imageSize)
                .graphicsLayer { alpha = imageAlpha() },
        )
    }
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
    GlowingImage(
        painter = painterResource(planet.imageRes),
        contentDescription = planet.name,
        glowColor = planet.glowColor,
        glowBlur = PlanetGlowBlur,
        imageSize = PlanetImageSize,
        glowAlpha = { planet.glowColor.alpha * alpha() },
        imageAlpha = alpha,
        modifier = modifier,
    )
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
        StatRow(
            leading = { StatCell(R.drawable.icon_weight, stringResource(R.string.stat_weight), planet.weight, modifier = it) },
            trailing = { StatCell(R.drawable.icon_sun, stringResource(R.string.stat_day), planet.dayLength, modifier = it) },
        )
        HorizontalDivider(
            color = White16,
            thickness = 0.5.dp,
            modifier = Modifier.padding(vertical = 16.dp),
        )
        StatRow(
            leading = { StatCell(R.drawable.icon_thermometer, stringResource(R.string.stat_temperature), planet.temperature, it,planet.temperatureNote ) },
            trailing = { StatCell(R.drawable.icon_info, stringResource(R.string.stat_additional), planet.additionalInfo, modifier = it) },
        )
    }
}

@Composable
private fun StatRow(
    leading: @Composable (Modifier) -> Unit,
    trailing: @Composable (Modifier) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        leading(Modifier.weight(1f))
        StatDivider()
        trailing(Modifier.weight(1f))
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
    modifier: Modifier = Modifier,
    note: String? = null,
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
