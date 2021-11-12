package net.informatikag.thomapp.utils.handlers

import android.text.Editable
import android.text.Html.TagHandler
import android.text.Layout
import android.text.style.AlignmentSpan
import org.xml.sax.XMLReader

/**
 * Wird verwendet um die Bilder, inclusive Caption im Textview zentriert werden
 */
class WordpressHtmlTagHandler : TagHandler {

    /**
     * Diese Methode wird aufgerufen um ein Tag zu handeln welches nicht vom standard Taghandler erkannt wird
     * @param opening Wird der tag gerade geöffnet oder geschlossen
     * @param tag der name des Tags
     * @param output der gesammte Text bis zum aktuellen element, hier müssen mögliche Formatierungen angebracht werden
     */
    override fun handleTag(
        opening: Boolean, tag: String, output: Editable,
        xmlReader: XMLReader
    ) {
        // Aktuell werden nur <figure> Tags bearbeitet, alle anderen werden ignoriert.
        // Außerdem werden sie erst behandelt wenn der Tag geschlossen wird
        if (tag.equals("figure", ignoreCase = true) && !opening) {
            //new Line char hinzufügen um den aktuellen Paragraphen zu beenden
            output.append("\n")

            var start = output.length + 1 //Start des zu zentrierenden Text abschnitts
            val split = output.toString().split("\n") // Liste aller Paragraphen

            //include last 3 Paragraphs (Picture and 2 empty ones or an empty one, Caption and Picture)
            for (i in 0 until minOf(split.size, 3)) {
                start -= (split[split.size - (i+1)] + "\n").length
            }

            //If theres a also include one more Char (Compensate for last not included empty Paragraph)
            if (split.size >= 3 && split[split.size - 3] != "") start -= split[split.size - 3].length
            val end = output.length

            //Spannable einfügen
            output.setSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), start, end, 0)
        }
    }
}
