package com.example.recycleviewtest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recycleviewtest.data.Data
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject
import java.io.FileWriter
import java.io.PrintWriter
import java.nio.charset.Charset

class MyAdapter(private val users: List<Data>, private val onClickListener:OnClickListener): RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    private var loginList = listOf("venya", "privet", "hello","ogo","aga","meow")
    private var idList = listOf(1,2,3,4,5,6)
    private var imageList = listOf(R.drawable.luffy, R.drawable.luffy, R.drawable.luffy,R.drawable.luffy, R.drawable.luffy, R.drawable.luffy)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyAdapter.ViewHolder, position: Int) {
        val user = users[position]

        holder.loginItem.text = "${user.login}"
        holder.idItem.text = "${user.id}"

        Picasso.get().load(user.avatar_url).into(holder.imageItem)

        holder.itemView.setOnClickListener {
            onClickListener.onClicked(user.html_url)
        }
//        val json = JSONObject()
//
//        try {
//            json.put("login", "Microsoft")
//            json.put("id", 182268)
//            json.put("node_id", "")
//            json.put("avatar_url", "")
//            json.put("gravatar_id", "")
//            json.put("url", "")
//            json.put("html_url", "")
//            json.put("followers_url", "")
//            json.put("gists_url", "")
//            json.put("starred_url", "")
//            json.put("subscriptions_url", "")
//            json.put("organizations_url", "")
//            json.put("repos_url", "")
//            json.put("events_url", "")
//            json.put("received_events_url", "")
//            json.put("type", "")
//            json.put("site_admin", "")
//
//        } catch (e: JSONException) {
//            e.printStackTrace()
//        }
//
//        try {
//            PrintWriter(FileWriter(path, Charset.defaultCharset()))
//                .use { it.write(json.toString()) }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }

    }

    override fun getItemCount(): Int {
        return users.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var imageItem: ImageView
        var loginItem: TextView
        var idItem: TextView

        init {
            imageItem = itemView.findViewById(R.id.item_image)
            loginItem = itemView.findViewById(R.id.login_item)
            idItem = itemView.findViewById(R.id.item_id)
        }

    }
}