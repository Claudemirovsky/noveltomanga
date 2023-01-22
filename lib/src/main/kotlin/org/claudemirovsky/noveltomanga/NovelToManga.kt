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
import org.davidmoten.text.utils.WordWrap

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

    private var LINE_HEIGHT: Float = fontSize
    private val LIMIT_WIDTH: Int
        get() = DEFAULT_WIDTH - (margin * 2).toInt()

    private val DEFAULT_BITMAP = Bitmap.createBitmap(
        DEFAULT_WIDTH,
        DEFAULT_HEIGHT,
        Bitmap.Config.ARGB_8888
    )


    private fun setTextPaint() {
        TEXTPAINT = TextPaint().apply {
            isAntiAlias = true
            color = theme.fontColor
            textSize = fontSize
        }
        val fm = TEXTPAINT.getFontMetrics()
        LINE_HEIGHT = fm.bottom - fm.top + fm.leading
    }

    private fun wrapText(text: String): List<String> {
        val wrapped = WordWrap.from(text)
            .maxWidth(LIMIT_WIDTH)
            .stringWidth({ c: CharSequence -> TEXTPAINT.measureText(c.toString()) })
            .wrapToList()
        return when (separateLines) {
            true -> wrapped + listOf("")
            else -> wrapped
        }
    }

    private fun getTextPages(lines: List<String>): List<String> {
        val totalLines = lines.parallelMap(::wrapText).flatten()
        val maxLines = (DEFAULT_HEIGHT / LINE_HEIGHT).toInt()
        val pageChunks = totalLines.chunked(maxLines)
        return pageChunks.map { it.joinToString("\n") }
    }

    fun getMangaPages(text: String) = getMangaPages(text.split("\n"))

    fun getMangaPages(lines: List<String>): List<Bitmap> {
        setTextPaint()
        val pages = getTextPages(lines)
        return pages.parallelMap(::drawPage)
    }

    private fun drawPage(page: String): Bitmap {
        val bitmap = DEFAULT_BITMAP.copy(DEFAULT_BITMAP.getConfig(), true)
        val canvas = Canvas(bitmap).apply {
            drawColor(theme.backgroundColor)
        }

        val staticLayout: StaticLayout =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                StaticLayout.Builder
                    .obtain(page, 0, page.length, TEXTPAINT, LIMIT_WIDTH)
                    .setAlignment(alignment)
                    .build()
            } else {
                @Suppress("DEPRECATION")
                StaticLayout(page, TEXTPAINT, LIMIT_WIDTH, alignment, 1F, 0F, false)
            }

        canvas.save()
        canvas.translate(margin, margin)
        staticLayout.draw(canvas)
        canvas.restore()

        return bitmap
    }

    private fun <A, B> Iterable<A>.parallelMap(f: suspend (A) -> B): List<B> =
        runBlocking {
            map { async(Dispatchers.Default) { f(it) } }.awaitAll()
        }
}
