package net.informatikag.thomapp.thomsline.fragments.ArticleView

import android.os.Bundle
import android.text.Html
import android.util.Log
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
        contentView.setText(Html.fromHtml(
            args.content.replace("figcaption", "p", true),
            Html.FROM_HTML_MODE_LEGACY,
            ThomsLineArticleImageGetter(resources, contentView),
            null)
        )

        return binding.root
    }
}