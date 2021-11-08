package net.informatikag.thomapp.utils.handlers

import android.text.Editable
import android.text.Html.TagHandler
import android.text.Layout
import android.text.style.AlignmentSpan
import org.xml.sax.XMLReader

class WordpressHtmlTagHandler : TagHandler {
    override fun handleTag(
        opening: Boolean, tag: String, output: Editable,
        xmlReader: XMLReader
    ) {
        if (tag.equals("figure", ignoreCase = true) && !opening) {
            output.append("\n")

            var start = output.length + 1
            val split = output.toString().split("\n")

            //include last 3 Paragraphs (Picture and 2 empty ones or an empty one, Caption and Picture)
            for (i in 0 until 3) {
                start -= (split[split.size - (i+1)] + "\n").length
            }

            //If theres a also include one more Char (Compensate for last not included empty Paragraph)
            if (split[split.size - 3] != "") start -= split[split.size - 3].length
            val end = output.length

            output.setSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), start, end, 0)
        }
    }
}
