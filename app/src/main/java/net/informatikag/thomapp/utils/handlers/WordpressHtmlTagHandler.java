package net.informatikag.thomapp.utils.handlers;

import android.text.Editable;
import android.text.Html;
import android.text.Layout;
import android.text.Spannable;
import android.text.style.AlignmentSpan;
import android.text.style.StrikethroughSpan;

import org.xml.sax.XMLReader;

//public class WordpressHtmlTagHandler implements Html.TagHandler {
//    @Override
//    public void handleTag(boolean opening, String tag, Editable output,
//                          XMLReader xmlReader) {
//        if(tag.equalsIgnoreCase("figure") && !opening) {
//            output.append("\n");
//
//            String[] split = output.toString().split("\n");
//            int start = output.length() - 2*(split[split.length - 2].length()+1) - (split[split.length - 1].length()-1);
//            int end = output.length();
//
//            if (start < end) { output.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), start, end, 0); }
//        }
//    }
//}
