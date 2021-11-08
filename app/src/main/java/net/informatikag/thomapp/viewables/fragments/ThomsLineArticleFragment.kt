package net.informatikag.thomapp.viewables.fragments

import android.graphics.text.LineBreaker.JUSTIFICATION_MODE_INTER_WORD
import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isGone
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import net.informatikag.thomapp.R
import net.informatikag.thomapp.databinding.ThomslineArticleFragmentBinding
import net.informatikag.thomapp.utils.handlers.ThomsLineArticleImageGetter
import net.informatikag.thomapp.utils.handlers.ThomsLineArticleTagHandler
import java.util.*

class ThomsLineArticleFragment : Fragment() {

    private val args: ThomsLineArticleFragmentArgs by navArgs()
    private var _binding: ThomslineArticleFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ThomslineArticleFragmentBinding.inflate(inflater, container, false)

        //Change Title TextView
        binding.thomslineArtilcleTitle.setText(args.title)

        //Load Author
        binding.thomslineArticleAuthor.setText(args.authors)

        //Load Date
        val date = Date(args.date)
        binding.thomslineArticleDate.setText(
            "${date.hours}:${date.minutes} - ${date.date}.${date.month}.${date.year}"
        )

        //Load Title Image
        val imageView: ImageView = binding.thomslineArticleImage
        if (args.imageURL != null)
            Glide.with(imageView.context)
                .applyDefaultRequestOptions(RequestOptions().error(R.drawable.img_thomsline_article_image_default))
                .load(args.imageURL)
                .placeholder(R.drawable.img_thomsline_article_image_default)
                .into(imageView)
        else imageView.isGone = true

        //Load Content
        val contentView: TextView = binding.thomslineArticleContent
        var content = args.content

        //Remove multiple Whitespaces
        content = content.replace("(\\s|&nbsp;)+".toRegex(), " ")

        //Add Linebreak bevore Caption
        content = content.replace("<figcaption>", "<br><figcaption>", true)

        //Load HTML to Textview
        contentView.setText(Html.fromHtml(
            content,
            Html.FROM_HTML_MODE_LEGACY,
            ThomsLineArticleImageGetter(resources, contentView),
            ThomsLineArticleTagHandler()
        )
        )

        //Since Android 8 (O) Block-Text is possible
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) contentView.justificationMode = JUSTIFICATION_MODE_INTER_WORD

        return binding.root
    }
}