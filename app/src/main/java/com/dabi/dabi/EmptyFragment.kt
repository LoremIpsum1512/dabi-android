package com.dabi.dabi

import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.dabi.dabi.data.HandledException
import com.dabi.dabi.databinding.FragmentErrorBinding
import kotlinx.android.parcel.Parcelize

class EmptyFragment : Fragment() {
    lateinit var binding: FragmentErrorBinding

    companion object {
        const val requestKey = "ERROR_FRAGMENT_REQUEST_KEY"
        const val argsKey = "FRAGMENT_ARGS_KEY"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentErrorBinding.inflate(
            inflater,
            container,
            false
        )

        requireActivity().supportFragmentManager.setFragmentResultListener(
            requestKey,
            viewLifecycleOwner
        ) { _, bundle ->
            val parcel = when {
                SDK_INT >= Build.VERSION_CODES.TIRAMISU -> bundle.getParcelable(
                    argsKey,
                    EmptyFragmentParcelable::class.java
                )
                else -> @Suppress("DEPRECATION")
                bundle.getParcelable(argsKey)
            }
            parcel?.let { it ->
                binding.title.text = it.title
                binding.description.text = it.description
                binding.graphic.setBackgroundResource(it.imageResource)
                if (it.retry == null)
                    binding.retryBtn.isVisible = false
                else
                    binding.retryBtn.setOnClickListener { _ -> it.retry.invoke() }

            }


        }
        return binding.root
    }

}

@Parcelize
class EmptyFragmentParcelable(
    val title: String,
    val description: String? = null,
    val imageResource: Int = R.drawable.generic_error,
    val retry: (() -> Unit)? = null,
) : Parcelable {
    companion object {
        fun fromException(throwable: Throwable): EmptyFragmentParcelable {
            val meaning = HandledException.of(throwable).meaning
            return EmptyFragmentParcelable(
                title = meaning.title,
                description = meaning.description,
            )
        }
    }
}