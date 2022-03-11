package net.informatikag.thomapp.viewables.fragments.thomaeum

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import net.informatikag.thomapp.R
import net.informatikag.thomapp.databinding.ThomaeumMainFragmentBinding
import net.informatikag.thomapp.utils.ListSpacingDecoration
import net.informatikag.thomapp.utils.handlers.WordpressRecyclerAdapter
import net.informatikag.thomapp.utils.models.ArticleClickHandler
import net.informatikag.thomapp.utils.models.data.WordpressArticleData
import net.informatikag.thomapp.utils.models.view.ThomaeumViewModel

class ThomaeumFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener, ArticleClickHandler {

    private var _binding: ThomaeumMainFragmentBinding?= null                  // Verweis zum Layout
    private val viewModel: ThomaeumViewModel by activityViewModels()   // Das Viewmodel in dem die wichtigen Daten des Fragments gespeichert werden
    private lateinit var recyclerAdapter: WordpressRecyclerAdapter              // Hier werden die Artikel angezeigt
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout                 // wird benutz um die Artikel neu zu laden

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate Layout
        _binding = ThomaeumMainFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //Instantiate Variables
        recyclerAdapter =  WordpressRecyclerAdapter(this, this, viewModel)

        //Add Observer to articles to update Recyclerview
        viewModel.articles.observe(viewLifecycleOwner, Observer {
            swipeRefreshLayout.isRefreshing = false
        })

        //region Init SwipeRefresh Layout
        swipeRefreshLayout = root.findViewById(R.id.thomaeum_swipe_container)
        swipeRefreshLayout.setOnRefreshListener(this)
        swipeRefreshLayout.setColorSchemeResources(
            R.color.primaryColor,
            R.color.secondaryColor
        )

        if(viewModel.isEmpty()) {
            swipeRefreshLayout.post {
                // Display Refresh Indicator
                swipeRefreshLayout.isRefreshing = true
                // Load First Article Page
                viewModel.loadArticles(0, true, requireContext(), recyclerAdapter)
            }
        }
        //endregion

        //region Init Recycler View

        binding.thomaeumRecyclerView?.apply {
            layoutManager = LinearLayoutManager(this@ThomaeumFragment.context)
            addItemDecoration(ListSpacingDecoration())
            adapter = recyclerAdapter
        }
        //endregion

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Called when the SwipeRefresh Layout is triggerd
     */
    override fun onRefresh() {
        viewModel.loadArticles(0, true, requireContext(), recyclerAdapter)
    }

    /**
     * Called when a Article is clicked
     */
    override fun onItemClick(wordpressArticleData: WordpressArticleData) {
        val action = ThomaeumFragmentDirections.actionNavThomaeumToThomaeumArticleFragment(
            wordpressArticleData.id
        )
        findNavController().navigate(action)
    }
}