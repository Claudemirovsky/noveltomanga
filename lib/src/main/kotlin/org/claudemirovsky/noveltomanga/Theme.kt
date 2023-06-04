package org.claudemirovsky.noveltomanga

enum class DefaultThemes : Theme {
    BLACK { // Almost...
        override val backgroundColor = rgbaColor(12, 12, 12)
        override val fontColor = rgbaColor(239, 239, 239)
    },
    DARK {
        override val backgroundColor = rgbaColor(33, 33, 33)
        override val fontColor = rgbaColor(245, 245, 245)
    },
    LIGHT {
        override val backgroundColor = rgbaColor(244, 244, 244)
        override val fontColor = rgbaColor(34, 34, 34)
    },
}

interface Theme {
    val backgroundColor: Int
    val fontColor: Int
}

// We use this instead of android.graphics.Color.rgb()
// because extensions-inspector would explode in flames due to
// its implementation being just a stub.
fun Theme.rgbaColor(R: Int, G: Int, B: Int, A: Int = 255): Int {
    return (A shl 24) or (R shl 16) or (G shl 8) or B
}
