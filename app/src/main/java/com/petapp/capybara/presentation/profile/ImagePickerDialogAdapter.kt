package com.petapp.capybara.presentation.profile

import com.bumptech.glide.Glide
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import com.petapp.capybara.R
import com.petapp.capybara.common.ListItem
import com.petapp.capybara.common.ListItemDiffCallback
import com.petapp.capybara.data.model.DeleteImage
import com.petapp.capybara.data.model.ImagePicker
import kotlinx.android.synthetic.main.item_image_picker.view.*

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
) = adapterDelegateLayoutContainer<ImagePicker, ListItem>(R.layout.item_image_picker) {

    bind {
        with(itemView) {
            setOnClickListener { itemClick.invoke(item) }
            title.text = getString(item.name)
            Glide.with(this)
                .load(item.icon)
                .into(image_picker_icon)
        }
    }
}

fun deleteImageDialogAdapterDelegate(
    deleteImage: () -> Unit
) = adapterDelegateLayoutContainer<DeleteImage, ListItem>(R.layout.item_delete_image) {

    bind {
        with(itemView) {
            setOnClickListener { deleteImage.invoke() }
        }
    }
}
