package org.claudemirovsky.noveltomanga

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking

class NovelToManga {
    companion object {
        private const val DEFAULT_WIDTH = 1080
        private const val DEFAULT_HEIGHT = 1536
    }

    private lateinit var TEXTPAINT: TextPaint
    var theme: Theme = DefaultThemes.DARK
    var separateLines: Boolean = true
    var fontSize: Float = 25F
    var margin: Float = 25F
    var alignment = Layout.Alignment.ALIGN_NORMAL

    private val LIMIT_HEIGHT: Int
        get() = DEFAULT_HEIGHT - (margin * 2).toInt()

    private val LIMIT_WIDTH: Int
        get() = DEFAULT_WIDTH - (margin * 2).toInt()

    private fun setTextPaint() {
        TEXTPAINT = TextPaint().apply {
            isAntiAlias = true
            color = theme.fontColor
            textSize = fontSize
        }
    }

    // https://github.com/onikx/PagedTextView/blob/master/lib/src/main/java/com/onik/pagedtextview/PagedTextView.kt#L128
    private fun getTextPages(page: CharSequence): List<CharSequence> {
        val layout = createLayoutFromText(page)
        val lines = layout.lineCount
        var startOffset = 0
        var height = LIMIT_HEIGHT
        val pageList = mutableListOf<CharSequence>()
        for (i in 0 until lines) {
            if (height < layout.getLineBottom(i)) {
                pageList.add(
                    layout.text.subSequence(startOffset, layout.getLineStart(i))
                )
                startOffset = layout.getLineStart(i)
                height = layout.getLineTop(i) + LIMIT_HEIGHT
            }

            if (i == lines - 1) {
                pageList.add(
                    layout.text.subSequence(startOffset, layout.getLineEnd(i))
                )
            }
        }
        return pageList
    }

    fun getMangaPages(lines: List<String>) = getMangaPages(lines.joinToString("\n"))

    fun getMangaPages(text: String): List<Bitmap> {
        setTextPaint()
        val spaced = if (separateLines) text.replace("\n", "\n\n") else text
        val pages = getTextPages(spaced)
        return pages.parallelMap(::drawPage)
    }

    private fun drawPage(page: CharSequence): Bitmap {
        val staticLayout = createLayoutFromText(page)
        val bitmap = Bitmap.createBitmap(
            DEFAULT_WIDTH,
            staticLayout.height + (margin * 2).toInt(),
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap).apply {
            drawColor(theme.backgroundColor)
        }

        canvas.save()
        canvas.translate(margin, margin)
        staticLayout.draw(canvas)
        canvas.restore()

        return bitmap
    }

    private fun createLayoutFromText(text: CharSequence): StaticLayout {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StaticLayout.Builder
                .obtain(text, 0, text.length, TEXTPAINT, LIMIT_WIDTH)
                .setAlignment(alignment)
                .build()
        } else {
            @Suppress("DEPRECATION")
            StaticLayout(text, TEXTPAINT, LIMIT_WIDTH, alignment, 1F, 0F, false)
        }
    }
    private fun <A, B> Iterable<A>.parallelMap(f: suspend (A) -> B): List<B> =
        runBlocking {
            map { async(Dispatchers.Default) { f(it) } }.awaitAll()
        }
}
