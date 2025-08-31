package com.tibolatte.milbadge.data

import com.tibolatte.milbadge.screens.HomeViewModel

class BadgeEngine(private val viewModel: HomeViewModel) {
    fun sendEvent(event: BadgeEvent) {
        viewModel.completeObjective(event.badgeId, event.delta)
    }

    fun sendStepEvent(steps: Int) {
        viewModel.completeObjective(1, steps)
    }
}