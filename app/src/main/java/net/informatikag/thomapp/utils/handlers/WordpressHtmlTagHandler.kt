package net.informatikag.thomapp.utils.handlers

import android.text.Editable
import android.text.Html.TagHandler
import android.text.Layout
import android.text.style.AlignmentSpan
import org.xml.sax.XMLReader

/**
 * Used for formating the Images contained by the Articles
 */
class WordpressHtmlTagHandler : TagHandler {

    /**
     * This method is called to handle a tag that is not recognized by the default tag handler
     * @param opening Is the tag just opened or closed
     * @param tag the tag name
     * @param output the entire text up to the current element, here possible formatting must be applied
     */
    override fun handleTag(
        opening: Boolean, tag: String, output: Editable,
        xmlReader: XMLReader
    ) {
        // Currently only <figure> tags are processed, all others are ignored.
        // Moreover, they are treated only when the tag is closed
        if (tag.equals("figure", ignoreCase = true) && !opening) {
            // add new line char to end the current paragraph
            output.append("\n")

            var start = output.length + 1 // Start of the text section to be centered
            val split = output.toString().split("\n") // List of all paragraphs to be able to calculate the start later on

            // Include the last 3 paragraphs (image and 2 or one blank paragraphs, caption and image).
            for (i in 0 until minOf(split.size, 3)) {
                start -= (split[split.size - (i+1)] + "\n").length
            }

            // If theres a also include one more Char (Compensate for last not included empty Paragraph)
            if (split.size >= 3 && split[split.size - 3] != "") start -= split[split.size - 3].length
            val end = output.length

            // Insert Spannable
            output.setSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), start, end, 0)
        }
    }
}
