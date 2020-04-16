package com.petapp.capybara.view

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.petapp.capybara.R
import com.petapp.capybara.extensions.visible
import com.petapp.capybara.presenter.Presenter

/*view не зависит от activity/fragment*/
class MainViewImpl(private val rootView: View) : MainView {

    companion object {
        const val id = 744
    }

    lateinit var presenter: Presenter
    lateinit var data: TextView
    lateinit var progressBar: ProgressBar

    fun onFinishInflate(presenter: Presenter) {
        this.presenter = presenter
        data = rootView.findViewById(R.id.tv_data)
        progressBar = rootView.findViewById(R.id.progress_bar)
        data.setOnClickListener {
            presenter.onDateClicked(id)
        }
    }

    override fun showData(data: String) {
        this.data.text = data
    }

    override fun showError(error: String) {
        this.data.text = error
    }

    override fun inProgress(isShow: Boolean) {
        progressBar.visible(isShow)
    }

}