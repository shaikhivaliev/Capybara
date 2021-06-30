package com.petapp.capybara.presentation.profile

import com.bumptech.glide.Glide
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.petapp.capybara.core.list.ListItem
import com.petapp.capybara.core.list.ListItemDiffCallback
import com.petapp.capybara.data.model.DeleteImage
import com.petapp.capybara.data.model.ImagePicker
import com.petapp.capybara.databinding.ItemDeleteImageBinding
import com.petapp.capybara.databinding.ItemImagePickerBinding

class ImagePickerDialogAdapter(
    private val itemClick: (ImagePicker) -> Unit,
    private val deleteImage: () -> Unit
) : AsyncListDifferDelegationAdapter<ListItem>(ListItemDiffCallback) {
    init {
        with(delegatesManager) {
            addDelegate(takeImageDialogAdapterDelegate(itemClick))
            addDelegate(deleteImageDialogAdapterDelegate(deleteImage))
        }
    }
}

fun takeImageDialogAdapterDelegate(
    itemClick: (ImagePicker) -> Unit
) = adapterDelegateViewBinding<ImagePicker, ListItem, ItemImagePickerBinding>(
    { layoutInflater, root -> ItemImagePickerBinding.inflate(layoutInflater, root, false) }
) {

    bind {
        with(binding) {
            root.setOnClickListener { itemClick.invoke(item) }
            title.text = getString(item.name)
            Glide.with(context)
                .load(item.icon)
                .into(imagePickerIcon)
        }
    }
}

fun deleteImageDialogAdapterDelegate(
    deleteImage: () -> Unit
) = adapterDelegateViewBinding<DeleteImage, ListItem, ItemDeleteImageBinding>(
    { layoutInflater, root -> ItemDeleteImageBinding.inflate(layoutInflater, root, false) }
) {

    bind {
        with(binding) {
            root.setOnClickListener { deleteImage.invoke() }
        }
    }
}
