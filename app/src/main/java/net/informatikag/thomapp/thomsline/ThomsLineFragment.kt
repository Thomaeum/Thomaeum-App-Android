package net.informatikag.thomapp.thomsline

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import net.informatikag.thomapp.R
import net.informatikag.thomapp.databinding.FragmentThomslineBinding
import net.informatikag.thomapp.thomsline.RecyclerView.ItemClickListener
import net.informatikag.thomapp.thomsline.RecyclerView.ThomslineRecyclerAdapter

class ThomsLineFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener, ItemClickListener {

    private lateinit var postAdapter: ThomslineRecyclerAdapter
    private lateinit var viewModel: ThomsLineFragmentViewModel
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var thomslineRecyclerAdapter: ThomslineRecyclerAdapter
    private var _binding: FragmentThomslineBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentThomslineBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel = ViewModelProvider(this).get(ThomsLineFragmentViewModel::class.java)
        viewModel.articles.observe(viewLifecycleOwner, Observer {
            thomslineRecyclerAdapter.setPages(it, viewModel.lastPage)
            mSwipeRefreshLayout.isRefreshing = false
        })

        thomslineRecyclerAdapter =  ThomslineRecyclerAdapter(this, viewModel)

        initSwipeRefreshLayout(root)
        initRecyclerView()

        return root
    }

    private fun initRecyclerView(){
        _binding?.thomslineRecyclerView?.apply {
            layoutManager = LinearLayoutManager(this@ThomsLineFragment.context)
            postAdapter = thomslineRecyclerAdapter
            addItemDecoration(TopSpacingItemDecoration(30))
            adapter = postAdapter
        }
    }

    private fun initSwipeRefreshLayout(root: View) {
        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        mSwipeRefreshLayout = root.findViewById(R.id.thomsline_swipe_container)
        mSwipeRefreshLayout.setOnRefreshListener(this)
        mSwipeRefreshLayout.setColorSchemeResources(
            R.color.primaryColor,
            R.color.secondaryColor
        )


        if (viewModel.articles.value == null) mSwipeRefreshLayout.post {
            mSwipeRefreshLayout.isRefreshing = true
            viewModel.loadArticles(0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class TopSpacingItemDecoration(private val padding: Int): RecyclerView.ItemDecoration(){
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.top = padding
            outRect.bottom = padding
            outRect.left = padding
            outRect.right = padding
        }
    }

    override fun onRefresh() {
        viewModel.loadArticles(0)
    }


    override fun onItemClick(wordpressArticle: WordpressArticle) {
        val action = ThomsLineFragmentDirections.actionNavThomslineToNavThomslineArticleView(wordpressArticle.title, wordpressArticle.imageURL, wordpressArticle.content)
        findNavController().navigate(action)
    }
}