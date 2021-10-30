package net.informatikag.thomapp.ui.screens.thomsline

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.informatikag.thomapp.databinding.FragmentThomslineBinding
import javax.sql.DataSource

class ThomsLineFragment : Fragment(){

    private lateinit var postAdapter: ThomslineRecyclerAdapter
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

        initRecyclerView()
        addDataSet()

        return root
    }

    private fun addDataSet(){
        postAdapter.submitList(ThomsLineDummyDatasource.createDataSet())
    }

    private fun initRecyclerView(){
        _binding?.thomslineRecyclerView?.apply {
            layoutManager = LinearLayoutManager(this@ThomsLineFragment.context)
            postAdapter = ThomslineRecyclerAdapter()
            addItemDecoration(TopSpacingItemDecoration(30))
            adapter = postAdapter
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
            outRect.left = padding
            outRect.right = padding
        }
    }
}