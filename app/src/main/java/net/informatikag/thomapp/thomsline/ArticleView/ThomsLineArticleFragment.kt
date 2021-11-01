package net.informatikag.thomapp.thomsline.ArticleView

import android.graphics.drawable.LevelListDrawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isGone
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import net.informatikag.thomapp.R
import net.informatikag.thomapp.databinding.FragmentThomslineArticleViewBinding
import net.informatikag.thomapp.databinding.FragmentThomslineBinding
import org.w3c.dom.Text

class ThomsLineArticleFragment : Fragment() {

    private val args:ThomsLineArticleFragmentArgs by navArgs()
    private var _binding: FragmentThomslineArticleViewBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = ThomsLineArticleFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentThomslineArticleViewBinding.inflate(inflater, container, false)


        val titleView: TextView = binding.thomslineArtilcleTitle
        val imageView: ImageView = binding.thomslineArticleImage
        val contentView: WebView = binding.thomslineArticleContent

        titleView.setText(args.title)

        if (args.imageURL != null)
            Glide.with(imageView.context)
                .applyDefaultRequestOptions(RequestOptions().error(R.drawable.ic_launcher_background))
                .load(args.imageURL)
                .placeholder(R.drawable.default_article_image)
                .into(imageView)
        else imageView.isGone = true

        contentView.loadDataWithBaseURL("", args.content, "text/html", "UTF-8", "")

        return binding.root
    }
}