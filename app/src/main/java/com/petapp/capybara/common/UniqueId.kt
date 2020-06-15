package com.petapp.capybara.common

import java.util.concurrent.atomic.AtomicInteger

class UniqueId {
    companion object {
        private val c = AtomicInteger(0)

        fun generateId(): Int {
            return c.incrementAndGet()
        }
    }
}