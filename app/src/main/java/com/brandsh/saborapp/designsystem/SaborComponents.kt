package com.brandsh.saborapp.designsystem

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Brightness6
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.LocalPizza
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Redeem
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.WineBar
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.brandsh.saborapp.models.SaborImages

@Composable
fun SaborCanvas(modifier: Modifier = Modifier) {
    val palette = LocalSaborPalette.current
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(MaterialTheme.colorScheme.primary.copy(alpha = 0.05f), palette.background),
                    radius = 1100f,
                ),
            ),
    )
}

@Composable
fun SaborCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val palette = LocalSaborPalette.current
    Surface(
        modifier = modifier.shadow(4.dp, MaterialTheme.shapes.medium),
        shape = MaterialTheme.shapes.medium,
        color = palette.containerLowest,
        border = BorderStroke(1.dp, palette.sand),
    ) {
        Column(content = content)
    }
}

@Composable
fun SaborImage(
    key: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    contentDescription: String? = key,
) {
    val palette = LocalSaborPalette.current
    Box(
        modifier = modifier
            .background(Brush.linearGradient(listOf(palette.container, palette.containerHigh))),
        contentAlignment = Alignment.Center,
    ) {
        val imageUrl = key?.let(SaborImages::url)
        if (imageUrl != null) {
            AsyncImage(
                model = imageUrl,
                contentDescription = contentDescription,
                modifier = Modifier.fillMaxSize(),
                contentScale = contentScale,
            )
        } else {
            SaborIcon("restaurant", tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f))
        }
    }
}

@Composable
fun SaborPrimaryButton(
    title: String,
    modifier: Modifier = Modifier,
    icon: String? = "arrow.right",
    iconLeading: String? = null,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().height(52.dp),
        enabled = enabled,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = LocalSaborPalette.current.sandStrong,
        ),
    ) {
        if (iconLeading != null) {
            SaborIcon(iconLeading, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(SaborSpacing.sm))
        }
        Text(title, style = MaterialTheme.typography.labelLarge)
        if (icon != null) {
            Spacer(Modifier.width(SaborSpacing.sm))
            SaborIcon(icon, modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
fun SaborSecondaryButton(
    title: String,
    modifier: Modifier = Modifier,
    icon: String? = null,
    onClick: () -> Unit,
) {
    Surface(
        modifier = modifier
            .height(50.dp)
            .clickable(onClick = onClick),
        shape = CircleShape,
        color = LocalSaborPalette.current.containerLowest,
        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.secondary),
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (icon != null) {
                SaborIcon(icon, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(SaborSpacing.sm))
            }
            Text(title, color = MaterialTheme.colorScheme.secondary, style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
fun SaborAddButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Surface(
        modifier = modifier.size(40.dp).clickable(onClick = onClick),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimary,
    ) {
        Box(contentAlignment = Alignment.Center) {
            SaborIcon("plus", modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
fun SaborChip(
    title: String,
    modifier: Modifier = Modifier,
    icon: String? = null,
    selected: Boolean = false,
    onClick: () -> Unit = {},
) {
    val palette = LocalSaborPalette.current
    Surface(
        modifier = modifier
            .height(36.dp)
            .clickable(onClick = onClick),
        shape = CircleShape,
        color = if (selected) MaterialTheme.colorScheme.secondary else palette.containerLowest,
        border = if (selected) null else BorderStroke(1.dp, palette.sand),
        contentColor = if (selected) MaterialTheme.colorScheme.onSecondary else palette.onSurfaceVariant,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (icon != null) {
                SaborIcon(icon, modifier = Modifier.size(15.dp))
                Spacer(Modifier.width(6.dp))
            }
            Text(title, style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
fun SaborTag(
    title: String,
    modifier: Modifier = Modifier,
    icon: String? = null,
    tint: Color = MaterialTheme.colorScheme.secondary,
) {
    Row(
        modifier = modifier
            .clip(CircleShape)
            .background(tint.copy(alpha = 0.14f))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            SaborIcon(icon, modifier = Modifier.size(12.dp), tint = tint)
            Spacer(Modifier.width(4.dp))
        }
        Text(title, color = tint, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
fun SaborSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    icon: String? = null,
    actionTitle: String? = null,
    onAction: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            SaborIcon(icon, modifier = Modifier.size(18.dp), tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(SaborSpacing.sm))
        }
        Text(title, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)
        Spacer(Modifier.weight(1f))
        if (actionTitle != null && onAction != null) {
            Text(
                actionTitle,
                modifier = Modifier.clip(CircleShape).clickable(onClick = onAction).padding(6.dp),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
fun SaborRating(value: Double, reviews: Int? = null, modifier: Modifier = Modifier) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        SaborIcon("star", modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.tertiary)
        Spacer(Modifier.width(3.dp))
        Text("%.1f".format(value), style = MaterialTheme.typography.labelMedium)
        if (reviews != null) {
            Spacer(Modifier.width(3.dp))
            Text("($reviews)", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun SaborStepper(
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    minValue: Int = 1,
) {
    val palette = LocalSaborPalette.current
    Row(
        modifier = modifier
            .clip(CircleShape)
            .background(palette.container)
            .padding(horizontal = 6.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        IconButton(onClick = { if (value > minValue) onValueChange(value - 1) }, modifier = Modifier.size(32.dp)) {
            SaborIcon("minus", modifier = Modifier.size(16.dp))
        }
        Text(value.toString(), style = MaterialTheme.typography.titleSmall, modifier = Modifier.width(20.dp))
        IconButton(onClick = { onValueChange(value + 1) }, modifier = Modifier.size(32.dp)) {
            SaborIcon("plus", modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
fun SaborToggleRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    icon: String? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            SaborIcon(icon, modifier = Modifier.size(22.dp), tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(SaborSpacing.md))
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleSmall)
            if (subtitle != null) {
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        androidx.compose.material3.Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
fun SaborNavRow(
    title: String,
    icon: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    trailingText: String? = null,
    badgeText: String? = null,
    onClick: () -> Unit,
) {
    val palette = LocalSaborPalette.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Surface(shape = MaterialTheme.shapes.small, color = MaterialTheme.colorScheme.primary.copy(alpha = 0.10f)) {
            Box(Modifier.size(38.dp), contentAlignment = Alignment.Center) {
                SaborIcon(icon, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary)
            }
        }
        Spacer(Modifier.width(SaborSpacing.md))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleSmall)
            if (subtitle != null) {
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = palette.onSurfaceVariant, maxLines = 1)
            }
        }
        if (badgeText != null) SaborTag(badgeText)
        if (trailingText != null) {
            Text(trailingText, style = MaterialTheme.typography.bodySmall, color = palette.onSurfaceVariant)
        }
        Spacer(Modifier.width(6.dp))
        SaborIcon("chevron.right", modifier = Modifier.size(17.dp), tint = palette.onSurfaceVariant.copy(alpha = 0.65f))
    }
}

@Composable
fun SaborTopBar(
    title: String? = null,
    location: String? = null,
    onBack: (() -> Unit)? = null,
    onNotifications: (() -> Unit)? = null,
    onCart: (() -> Unit)? = null,
    onProfile: (() -> Unit)? = null,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(SaborSpacing.sm),
    ) {
        if (onBack != null) {
            SaborRoundAction("arrow.left", onClick = onBack)
        } else if (title == null) {
            SaborLogoMark(size = 30.dp)
        }
        if (title != null) {
            Text(title, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)
        }
        if (location != null) {
            Row(
                modifier = Modifier
                    .weight(1f, fill = false)
                    .clip(CircleShape)
                    .background(LocalSaborPalette.current.container)
                    .padding(horizontal = 10.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SaborIcon("location", modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(4.dp))
                Text(location, style = MaterialTheme.typography.labelMedium, maxLines = 1)
                Spacer(Modifier.width(3.dp))
                SaborIcon("chevron.down", modifier = Modifier.size(13.dp))
            }
        }
        Spacer(Modifier.weight(1f))
        if (onCart != null) SaborRoundAction("cart", onClick = onCart)
        if (onNotifications != null) SaborRoundAction("notifications", onClick = onNotifications)
        if (onProfile != null) SaborAvatar(onClick = onProfile)
    }
}

@Composable
fun SaborRoundAction(icon: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.size(38.dp).clickable(onClick = onClick),
        shape = CircleShape,
        color = LocalSaborPalette.current.container,
    ) {
        Box(contentAlignment = Alignment.Center) {
            SaborIcon(icon, modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
fun SaborAvatar(
    modifier: Modifier = Modifier,
    size: Dp = 36.dp,
    key: String = "profile",
    onClick: (() -> Unit)? = null,
) {
    SaborImage(
        key = key,
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .border(1.dp, LocalSaborPalette.current.sand, CircleShape)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
    )
}

@Composable
fun SaborLogoMark(size: Dp = 30.dp, showWordmark: Boolean = true) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer) {
            Box(Modifier.size(size), contentAlignment = Alignment.Center) {
                SaborIcon("flame", modifier = Modifier.size(size * 0.52f), tint = Color.White)
            }
        }
        if (showWordmark) {
            Text(
                "Sabor",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
fun SaborTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    icon: String? = null,
    isSecure: Boolean = false,
    trailingIcon: String? = null,
    onTrailingClick: (() -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        if (label.isNotEmpty()) {
            Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder, style = MaterialTheme.typography.bodyMedium) },
            leadingIcon = if (icon != null) {
                { SaborIcon(icon, modifier = Modifier.size(18.dp)) }
            } else {
                null
            },
            trailingIcon = if (trailingIcon != null) {
                {
                    IconButton(onClick = { onTrailingClick?.invoke() }) {
                        SaborIcon(trailingIcon, modifier = Modifier.size(18.dp))
                    }
                }
            } else {
                null
            },
            singleLine = true,
            shape = MaterialTheme.shapes.small,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            visualTransformation = if (isSecure) PasswordVisualTransformation() else VisualTransformation.None,
        )
    }
}

@Composable
fun SaborToast(message: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.shadow(12.dp, CircleShape),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.inverseSurface,
        contentColor = MaterialTheme.colorScheme.inverseOnSurface,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            SaborIcon("check.circle", tint = MaterialTheme.colorScheme.secondary)
            Text(message, style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
fun SaborIcon(
    name: String,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
) {
    Icon(
        imageVector = saborIcon(name),
        contentDescription = name,
        modifier = modifier,
        tint = tint,
    )
}

private fun saborIcon(name: String): ImageVector = when (name) {
    "arrow.left", "arrow.backward" -> Icons.Default.ArrowBack
    "arrow.right" -> Icons.Default.ArrowForward
    "notifications", "bell.fill" -> Icons.Default.Notifications
    "location", "location.fill", "location.north.fill" -> Icons.Default.LocationOn
    "chevron.down" -> Icons.Default.ExpandMore
    "chevron.up" -> Icons.Default.ExpandLess
    "heart", "heart.fill" -> if (name.endsWith(".fill")) Icons.Default.Favorite else Icons.Default.FavoriteBorder
    "home", "house", "house.fill" -> Icons.Default.Home
    "menu.book" -> Icons.Default.MenuBook
    "orders", "list.bullet.rectangle.fill" -> Icons.Default.ReceiptLong
    "profile", "person.fill", "person.crop.circle.fill" -> Icons.Default.Person
    "star", "star.fill" -> Icons.Default.Star
    "flame", "flame.fill" -> Icons.Default.LocalFireDepartment
    "magnifyingglass", "search" -> Icons.Default.Search
    "slider.horizontal.3", "tune" -> Icons.Default.Tune
    "bag.fill" -> Icons.Default.ShoppingBag
    "cart", "cart.badge.plus" -> Icons.Default.ShoppingCart
    "minus" -> Icons.Default.Remove
    "plus" -> Icons.Default.Add
    "close", "xmark" -> Icons.Default.Close
    "clock", "timer", "clock.fill" -> Icons.Default.AccessTime
    "leaf.fill" -> Icons.Default.Eco
    "tag.fill", "percent" -> Icons.Default.LocalOffer
    "fork.knife", "restaurant" -> Icons.Default.Restaurant
    "dinner_dining", "lunch_dining" -> Icons.Default.RestaurantMenu
    "ramen_dining", "bowl.fill" -> Icons.Default.Restaurant
    "local_pizza", "triangle.fill" -> Icons.Default.LocalPizza
    "cake", "birthday.cake.fill" -> Icons.Default.Cake
    "wineglass.fill", "local_bar" -> Icons.Default.WineBar
    "bicycle", "moped.fill" -> Icons.Default.DirectionsBike
    "creditcard.fill", "creditcard" -> Icons.Default.CreditCard
    "lock.fill", "lock.shield.fill" -> Icons.Default.Lock
    "security" -> Icons.Default.Security
    "edit", "pencil", "settings" -> Icons.Default.Settings
    "trash", "delete" -> Icons.Default.Delete
    "email", "envelope.fill" -> Icons.Default.Email
    "phone.fill", "phone" -> Icons.Default.Phone
    "place", "location.dot" -> Icons.Default.Place
    "info.circle.fill" -> Icons.Default.Info
    "checkmark", "check" -> Icons.Default.Check
    "checkmark.circle.fill", "check.circle" -> Icons.Default.CheckCircle
    "gift", "gift.fill" -> Icons.Default.Redeem
    "sparkles" -> Icons.Default.AutoAwesome
    "building.2.fill" -> Icons.Default.Business
    "chart.bar.fill" -> Icons.Default.BarChart
    "sun.max.fill" -> Icons.Default.LightMode
    "moon.fill" -> Icons.Default.DarkMode
    "circle.lefthalf.filled" -> Icons.Default.Brightness6
    "checkmark.seal.fill" -> Icons.Default.Verified
    "crown.fill" -> Icons.Default.WorkspacePremium
    "takeoutbag.and.cup.and.straw.fill" -> Icons.Default.ShoppingBag
    "square.and.arrow.up" -> Icons.Default.Share
    "visibility" -> Icons.Default.Visibility
    "visibility.off" -> Icons.Default.VisibilityOff
    "checkmark.square.fill" -> Icons.Default.CheckBox
    "square" -> Icons.Default.CheckBoxOutlineBlank
    "largecircle.fill.circle" -> Icons.Default.RadioButtonChecked
    "circle" -> Icons.Default.RadioButtonUnchecked
    "chevron.right" -> Icons.Default.ArrowForward
    "filter", "filter.list" -> Icons.Default.FilterList
    else -> Icons.Default.Restaurant
}
