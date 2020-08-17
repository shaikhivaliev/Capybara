package com.petapp.capybara

import java.util.concurrent.atomic.AtomicInteger

class UniqueId {
    companion object {
        private val c = AtomicInteger(0)
        val id: Int
            get() = c.incrementAndGet()
    }
}
