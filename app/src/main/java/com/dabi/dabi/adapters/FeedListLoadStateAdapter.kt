import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dabi.dabi.adapters.FeedListPlaceholderAdapter
import com.dabi.dabi.databinding.FeedListLoadStateBinding

class FeedListLoadStateAdapter(var retry: () -> Unit) :
    LoadStateAdapter<FeedListLoadStateAdapter.ViewHolder>() {
    class ViewHolder(private var binding: FeedListLoadStateBinding, private var retry: () -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.placeholder.adapter = FeedListPlaceholderAdapter(Array(12) { i -> i })
            binding.placeholder.isVerticalScrollBarEnabled = false
        }

        fun bind(loadState: LoadState) {
            binding.placeholder.isVisible = loadState is LoadState.Loading
            binding.errorView.isVisible = loadState is LoadState.Error
            binding.retryBtn.setOnClickListener { retry() }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        val binding = FeedListLoadStateBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return ViewHolder(binding, retry)
    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }
}
