package com.petapp.capybara.ui.state

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import io.github.boguszpawlowski.composecalendar.CalendarState
import io.github.boguszpawlowski.composecalendar.header.MonthState
import io.github.boguszpawlowski.composecalendar.selection.SelectionState
import java.time.LocalDate
import java.time.YearMonth

@Stable
class StaticSelectionState(
    selection: List<LocalDate> = emptyList()
) : SelectionState {
    var selection by mutableStateOf(selection)

    override fun isDateSelected(date: LocalDate): Boolean = selection.contains(date)

    override fun onDateSelected(date: LocalDate) = Unit

    internal companion object {
        fun saver(): Saver<StaticSelectionState, Any> {
            return listSaver(
                save = { raw ->
                    listOf(raw.selection.map { it.toString() })
                },
                restore = { restored ->
                    val selection = if (restored.isNotEmpty()) {
                        (restored[0] as? List<String>)?.map { LocalDate.parse(it) }.orEmpty()
                    } else {
                        emptyList()
                    }
                    StaticSelectionState(
                        selection = selection,
                    )
                }
            )
        }
    }
}

@Composable
fun rememberStaticSelectionState(
    initialMonth: YearMonth = YearMonth.now(),
    monthState: MonthState = rememberSaveable(saver = MonthState.Saver()) {
        MonthState(initialMonth = initialMonth)
    },
    selection: List<LocalDate> = emptyList(),
    selectionState: StaticSelectionState = rememberSaveable(
        saver = StaticSelectionState.saver()
    ) {
        StaticSelectionState(selection = selection)
    }
): CalendarState<StaticSelectionState> = remember { CalendarState(monthState, selectionState) }
