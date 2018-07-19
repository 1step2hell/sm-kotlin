package com.step2hell.newsmth.widgets

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.step2hell.newsmth.R
import com.step2hell.newsmth.data.remote.dto.AdRsp
import kotlinx.android.synthetic.main.fragment_dialog_ad.*

class AdDialog(context: Context) : Dialog(context) {

    lateinit var adRsp: AdRsp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_dialog_ad)
        badge.setOnClickListener { dismiss() }
        image.setOnClickListener { Toast.makeText(context,adRsp.article,Toast.LENGTH_SHORT).show() } // Todo: jump to AD article
    }

    override fun onStart() {
        super.onStart()
        window.attributes.dimAmount = 0.2f
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)
    }

    fun setAd(ad: AdRsp) {
        adRsp = ad
        loadImage(ad.wholeImageUrl)
    }

    private fun loadImage(imageUrl: String) {
        Glide.with(context)
                .load(imageUrl)
                .apply(RequestOptions()
                        .fitCenter()
                        .placeholder(R.mipmap.ic_launcher)
                        .transform(RoundedCorners(1 shl 3)))
                .into(image)
    }

}