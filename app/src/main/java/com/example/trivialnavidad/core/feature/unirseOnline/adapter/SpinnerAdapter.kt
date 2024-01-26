package com.example.trivialnavidad.core.feature.unirseOnline.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.example.trivialnavidad.R

class SpinnerAdapter(context: Context, private val avatars: List<Int?>) : ArrayAdapter<Int>(context, 0, avatars) {
    private val avatarImages = context.resources.obtainTypedArray(R.array.avatar_images)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    private fun createView(position: Int, convertView: View?, parent: ViewGroup): View {
        val avatar = avatarImages.getDrawable(avatars[position]!!)
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.seleccion_spinner, parent, false)

        // Configurar la imagen del avatar en la vista
        val avatarImageView: ImageView = view.findViewById(R.id.iv_avatar)
        avatarImageView.setImageDrawable(avatar)
        avatarImageView.tag = avatars[position]


        return view
    }
}