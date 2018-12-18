package com.onemt.skin

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item.view.*
import kotlinx.android.synthetic.main.recycler_activity.*
import org.jetbrains.anko.find

/**
 * @author: chenjinghang
 * @version: 1.0.0
 * @date: 2018/12/18 14:17
 * @see
 */
class RecyclerActivity: BaseSkinActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recycler_activity)
        val data = mutableListOf<String>()
        for (i in 0..200) {
            data.add("hello:$i")
        }
        recycler.adapter = object : RecyclerView.Adapter<ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                val view = layoutInflater.inflate(R.layout.item, parent, false)
                return ViewHolder(view)
            }

            override fun getItemCount(): Int {
                return data.size
            }

            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                holder.setText(data[position])
            }
        }
    }

    internal class ViewHolder(var view: View): RecyclerView.ViewHolder(view) {

        fun setText(content: String) {
            view.tv.text = content
        }
    }
}