package eu.wewox.lazytable.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import eu.wewox.lazytable.Example
import eu.wewox.lazytable.LazyTable
import eu.wewox.lazytable.LazyTableItem
import eu.wewox.lazytable.data.createCells
import eu.wewox.lazytable.rememberSaveableLazyTableState
import eu.wewox.lazytable.ui.components.TopBar
import kotlinx.coroutines.launch
import lazytable.demo.generated.resources.Res
import lazytable.demo.generated.resources.chevron_right
import org.jetbrains.compose.resources.painterResource

/**
 * Example how lazy table state could be used.
 */
@Composable
fun LazyTableStateScreen(
    onBackClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopBar(
                title = Example.LazyTableState.label,
                onBackClick = onBackClick
            )
        }
    ) { padding ->
        val columns = 10
        val rows = 30
        val cells = remember { createCells(columns, rows) }

        val scope = rememberCoroutineScope()
        val state = rememberSaveableLazyTableState()

        Box(modifier = Modifier.fillMaxWidth().padding(padding)) {
            LazyTable(
                state = state,
                modifier = Modifier.fillMaxWidth()
            ) {
                items(
                    items = cells,
                    layoutInfo = { LazyTableItem(it.first, it.second) }
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surface)
                            .border(Dp.Hairline, MaterialTheme.colorScheme.onSurface)
                            .clickable { scope.launch { state.animateToCell(it.first, it.second) } }
                    ) {
                        Text(text = "$it")
                    }
                }
            }
            CanScrollIndicatorIcon(
                visible = state.canScrollHorizontallyBackward,
                alignment = Alignment.CenterStart,
                rotateDegrees = 180F,
                enter = slideInHorizontally(),
                exit = slideOutHorizontally(),
            )
            CanScrollIndicatorIcon(
                visible = state.canScrollVerticallyBackward,
                alignment = Alignment.TopCenter,
                rotateDegrees = 270F,
                enter = slideInVertically(),
                exit = slideOutVertically(),
            )
            CanScrollIndicatorIcon(
                visible = state.canScrollHorizontallyForward,
                alignment = Alignment.CenterEnd,
                rotateDegrees = 0F,
                enter = slideInHorizontally { it / 2 },
                exit = slideOutHorizontally { it / 2 },
            )
            CanScrollIndicatorIcon(
                visible = state.canScrollVerticallyForward,
                alignment = Alignment.BottomCenter,
                rotateDegrees = 90F,
                enter = slideInVertically { it / 2 },
                exit = slideOutVertically { it / 2 },
            )
        }
    }
}

@Composable
private fun BoxScope.CanScrollIndicatorIcon(
    visible: Boolean,
    alignment: Alignment,
    rotateDegrees: Float,
    enter: EnterTransition,
    exit: ExitTransition,
) {
    AnimatedVisibility(
        modifier = Modifier.align(alignment),
        visible = visible,
        enter = fadeIn() + enter,
        exit = exit + fadeOut()
    ) {
        Icon(
            modifier = Modifier.size(36.dp).rotate(rotateDegrees),
            painter = painterResource(Res.drawable.chevron_right),
            contentDescription = null
        )
    }
}
