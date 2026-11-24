package com.brandsh.saborapp.designsystem

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape

@Immutable
data class SaborPalette(
    val background: Color,
    val surfaceDim: Color,
    val containerLowest: Color,
    val containerLow: Color,
    val container: Color,
    val containerHigh: Color,
    val containerHighest: Color,
    val onSurfaceVariant: Color,
    val inverseSurface: Color,
    val inverseOnSurface: Color,
    val sand: Color,
    val sandStrong: Color,
    val secondaryContainer: Color,
    val onSecondaryContainer: Color,
    val tertiaryFixed: Color,
)

private fun saborPalette(dark: Boolean) = if (dark) {
    SaborPalette(
        background = Color(0xFF121110),
        surfaceDim = Color(0xFF151311),
        containerLowest = Color(0xFF100E0C),
        containerLow = Color(0xFF181614),
        container = Color(0xFF201D1B),
        containerHigh = Color(0xFF262320),
        containerHighest = Color(0xFF2E2A27),
        onSurfaceVariant = Color(0xFFBBB5A8),
        inverseSurface = Color(0xFFE7E1DE),
        inverseOnSurface = Color(0xFF32302E),
        sand = Color(0xFF38332E),
        sandStrong = Color(0xFF4A443E),
        secondaryContainer = Color(0xFF1F5240),
        onSecondaryContainer = Color(0xFF8FC3AD),
        tertiaryFixed = Color(0xFFFFDAD3),
    )
} else {
    SaborPalette(
        background = Color(0xFFFCF9F4),
        surfaceDim = Color(0xFFDCDAD5),
        containerLowest = Color(0xFFFFFFFF),
        containerLow = Color(0xFFF6F3EE),
        container = Color(0xFFF0EDE9),
        containerHigh = Color(0xFFEBE8E3),
        containerHighest = Color(0xFFE5E2DD),
        onSurfaceVariant = Color(0xFF58413C),
        inverseSurface = Color(0xFF31302D),
        inverseOnSurface = Color(0xFFF3F0EB),
        sand = Color(0xFFE8E2D8),
        sandStrong = Color(0xFFD8CFC4),
        secondaryContainer = Color(0xFFC5E7D6),
        onSecondaryContainer = Color(0xFF4A695B),
        tertiaryFixed = Color(0xFFFFDEAE),
    )
}

val LocalSaborPalette = staticCompositionLocalOf { saborPalette(dark = false) }

object SaborSpacing {
    val xs = 4.dp
    val sm = 8.dp
    val md = 16.dp
    val lg = 24.dp
    val xl = 32.dp
    val xxl = 48.dp
    val margin = 16.dp
}

object SaborRadius {
    val sm = 4.dp
    val md = 8.dp
    val lg = 16.dp
    val xl = 24.dp
}

@Composable
fun SaborTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val palette = saborPalette(darkTheme)
    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = Color(0xFFE05A3D),
            onPrimary = Color.White,
            primaryContainer = Color(0xFFD95338),
            onPrimaryContainer = Color.White,
            secondary = Color(0xFF9DD2BA),
            onSecondary = Color(0xFF003828),
            secondaryContainer = palette.secondaryContainer,
            onSecondaryContainer = palette.onSecondaryContainer,
            tertiary = Color(0xFFFFB4A4),
            onTertiary = Color(0xFF561F14),
            background = palette.background,
            onBackground = Color(0xFFF5F2EB),
            surface = palette.background,
            onSurface = Color(0xFFF5F2EB),
            surfaceVariant = palette.containerHigh,
            onSurfaceVariant = palette.onSurfaceVariant,
            outline = Color(0xFF8E887E),
            outlineVariant = palette.sand,
            error = Color(0xFFFFB4AB),
            onError = Color(0xFF690005),
            inverseSurface = palette.inverseSurface,
            inverseOnSurface = palette.inverseOnSurface,
            inversePrimary = Color(0xFFA6331B),
        )
    } else {
        lightColorScheme(
            primary = Color(0xFFA6331B),
            onPrimary = Color.White,
            primaryContainer = Color(0xFFC84B31),
            onPrimaryContainer = Color.White,
            secondary = Color(0xFF466557),
            onSecondary = Color.White,
            secondaryContainer = palette.secondaryContainer,
            onSecondaryContainer = palette.onSecondaryContainer,
            tertiary = Color(0xFF7C5400),
            onTertiary = Color.White,
            background = palette.background,
            onBackground = Color(0xFF1C1C19),
            surface = palette.background,
            onSurface = Color(0xFF1C1C19),
            surfaceVariant = palette.containerHigh,
            onSurfaceVariant = palette.onSurfaceVariant,
            outline = Color(0xFF8C716B),
            outlineVariant = Color(0xFFE0BFB9),
            error = Color(0xFFBA1A1A),
            onError = Color.White,
            inverseSurface = palette.inverseSurface,
            inverseOnSurface = palette.inverseOnSurface,
            inversePrimary = Color(0xFFE05A3D),
        )
    }

    val typography = Typography(
        displayLarge = roundedText(34, FontWeight.Bold),
        headlineLarge = roundedText(28, FontWeight.Bold),
        headlineMedium = roundedText(24, FontWeight.SemiBold),
        headlineSmall = roundedText(20, FontWeight.SemiBold),
        titleLarge = roundedText(18, FontWeight.SemiBold),
        titleMedium = roundedText(16, FontWeight.SemiBold),
        titleSmall = roundedText(14, FontWeight.SemiBold),
        bodyLarge = roundedText(16, FontWeight.Normal),
        bodyMedium = roundedText(14, FontWeight.Normal),
        bodySmall = roundedText(12, FontWeight.Normal),
        labelLarge = roundedText(14, FontWeight.SemiBold),
        labelMedium = roundedText(12, FontWeight.SemiBold),
        labelSmall = roundedText(11, FontWeight.Bold),
    )

    CompositionLocalProvider(LocalSaborPalette provides palette) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography,
            shapes = Shapes(
                extraSmall = RoundedCornerShape(SaborRadius.sm),
                small = RoundedCornerShape(SaborRadius.md),
                medium = RoundedCornerShape(SaborRadius.lg),
                large = RoundedCornerShape(SaborRadius.xl),
                extraLarge = RoundedCornerShape(32.dp),
            ),
            content = content,
        )
    }
}

@Composable
private fun roundedText(size: Int, weight: FontWeight) = TextStyle(
    fontFamily = FontFamily.SansSerif,
    fontSize = size.sp,
    fontWeight = weight,
    lineHeight = (size * 1.25).sp,
)
