package net.informatikag.thomapp.thomsline.fragments.ArticleView

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
import net.informatikag.thomapp.databinding.FragmentThomslineArticleViewBinding
import net.informatikag.thomapp.thomsline.utils.ThomsLineArticleImageGetter
import net.informatikag.thomapp.thomsline.utils.ThomsLineArticleTagHandler

class ThomsLineArticleFragment : Fragment() {

    private val args:ThomsLineArticleFragmentArgs by navArgs()
    private var _binding: FragmentThomslineArticleViewBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentThomslineArticleViewBinding.inflate(inflater, container, false)

        //Change Title TextView
        binding.thomslineArtilcleTitle.setText(args.title)

        //Load Title Image
        val imageView: ImageView = binding.thomslineArticleImage
        if (args.imageURL != null)
            Glide.with(imageView.context)
                .applyDefaultRequestOptions(RequestOptions().error(R.drawable.ic_launcher_background))
                .load(args.imageURL)
                .placeholder(R.drawable.default_article_image)
                .into(imageView)
        else imageView.isGone = true

        //Load Content
        val contentView: TextView = binding.thomslineArticleContent
        var content = args.content

        //Remove new Line Chars
        content = content.replace("\n", "", true)

        //Remove Divs
        content = content.replace("\t*</?div[^>]*>".toRegex(), "")

        //Remove multiple Whitespaces
        content = content.replace("(\\s|&nbsp;)+".toRegex(), " ")

        //Add Linebreak bevore Caption
        content = content.replace("<figcaption>", "<br><figcaption>", true)

        //Load HTML to Textview
        contentView.setText(Html.fromHtml(
            content,
            Html.FROM_HTML_MODE_LEGACY,
            ThomsLineArticleImageGetter(resources, contentView),
            ThomsLineArticleTagHandler())
        )

        //Since Android 8 (O) Block-Text is possible
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) contentView.justificationMode = JUSTIFICATION_MODE_INTER_WORD

        return binding.root
    }
}