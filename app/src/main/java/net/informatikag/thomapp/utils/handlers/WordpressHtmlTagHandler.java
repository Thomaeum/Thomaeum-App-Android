package net.informatikag.thomapp.utils.handlers;

import android.text.Editable;
import android.text.Html;
import android.text.Layout;
import android.text.Spannable;
import android.text.style.AlignmentSpan;
import android.text.style.StrikethroughSpan;

import org.xml.sax.XMLReader;

public class WordpressHtmlTagHandler implements Html.TagHandler {
    @Override
    public void handleTag(boolean opening, String tag, Editable output,
                          XMLReader xmlReader) {
        if(tag.equalsIgnoreCase("figure")) {
            handleTag(opening, output);
        }
    }

    private void handleTag(boolean opening, Editable output) {
        output.append("\n");
        int end = output.length();
        if(opening) {
        } else {
            String[] split = output.toString().split("\n");
            int start = output.length() - 2*(split[split.length - 2].length()+1) - (split[split.length - 1].length()-1);

            if (start < end) {
                output.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), start, end, 0);
            }
        }
    }

    private Object getLast(Editable text, Class kind) {
        Object[] objs = text.getSpans(0, text.length(), kind);
        if (objs.length != 0) {
            for(int i = objs.length;i>0;i--) {
                if(text.getSpanFlags(objs[i-1]) == Spannable.SPAN_MARK_MARK) {
                    return objs[i-1];
                }
            }
        }
        return null;
    }
}
