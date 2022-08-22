package org.claudemirovsky.noveltomanga

import android.graphics.Color

enum class DefaultThemes : Theme {
    BLACK { // Almost...
        override val backgroundColor = Color.rgb(12, 12, 12)
        override val fontColor = Color.rgb(239, 239, 239)
    },
    DARK {
        override val backgroundColor = Color.rgb(33, 33, 33)
        override val fontColor = Color.rgb(245, 245, 245)
    },
    LIGHT {
        override val backgroundColor = Color.rgb(244, 244, 244)
        override val fontColor = Color.rgb(34, 34, 34)
    }
}

interface Theme {
    val backgroundColor: Int
    val fontColor: Int
}
