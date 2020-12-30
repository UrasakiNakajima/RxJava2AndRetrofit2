package com.mobile.rxjava2andretrofit2.kotlin.resource.adapter

import android.content.Context
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mikepenz.fontawesome_typeface_library.FontAwesome
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic
import com.mobile.rxjava2andretrofit2.R
import com.mobile.rxjava2andretrofit2.callback.RcvOnItemViewClickListener
import com.mobile.rxjava2andretrofit2.custom_view.ColorTextView
import com.mobile.rxjava2andretrofit2.kotlin.resource.bean.Result
import com.mobile.rxjava2andretrofit2.manager.ScreenManager

class ResourceAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = "ResourceAdapter"
    private var list: MutableList<Result> = mutableListOf()

    fun clearData() {
        this.list.clear()
        notifyDataSetChanged()
    }

    fun addAllData(list: MutableList<Result>) {
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_resource, parent, false)
        return ContentHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ContentHolder) {
            if (list.get(position).type.equals("福利")) {
                holder.imvResource.setVisibility(View.VISIBLE)
                holder.tevResource.setVisibility(View.GONE)
//                Picasso.with(context).load(list.get(position).url).placeholder(R.mipmap.ic_launcher_round).into(imvResource)
                Glide.with(context).load(list.get(position).url).into(holder.imvResource)
            } else {
                holder.imvResource.setVisibility(View.GONE)
                holder.tevResource.setVisibility(View.VISIBLE)
                holder.tevResource.setLinkTextColor(context.resources.getColor(R.color.color_4876FF))
                holder.tevResource.setText(Html.fromHtml("<a href=\""
                        + list.get(position).url + "\">"
                        + list.get(position).desc + "</a>"
                        + "[" + list.get(position).who + "]"))
                holder.tevResource.setMovementMethod(LinkMovementMethod.getInstance())
                when (list.get(position).type) {
                    "Android" -> setIconDrawable(holder.tevResource, MaterialDesignIconic.Icon.gmi_android)
                    "iOS" -> setIconDrawable(holder.tevResource, MaterialDesignIconic.Icon.gmi_apple)
                    "休息视频" -> setIconDrawable(holder.tevResource, MaterialDesignIconic.Icon.gmi_collection_video)
                    "前端" -> setIconDrawable(holder.tevResource, MaterialDesignIconic.Icon.gmi_language_javascript)
                    "拓展资源" -> setIconDrawable(holder.tevResource, FontAwesome.Icon.faw_location_arrow)
                    "App" -> setIconDrawable(holder.tevResource, MaterialDesignIconic.Icon.gmi_apps)
                    "瞎推荐" -> setIconDrawable(holder.tevResource, MaterialDesignIconic.Icon.gmi_more)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


    protected class ContentHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tevResource: ColorTextView = itemView.findViewById(R.id.tev_resource)
        var imvResource: ImageView = itemView.findViewById(R.id.imv_resource)
    }

    private var rcvOnItemViewClickListener: RcvOnItemViewClickListener? = null

    fun setRcvOnItemViewClickListener(rcvOnItemViewClickListener: RcvOnItemViewClickListener) {
        this.rcvOnItemViewClickListener = rcvOnItemViewClickListener
    }

    private fun setIconDrawable(view: TextView, icon: IIcon) {
        view.setCompoundDrawablesWithIntrinsicBounds(IconicsDrawable(context)
                .icon(icon)
                .color(context.resources.getColor(R.color.color_4876FF))
                .sizeDp(14), null, null, null)
        view.compoundDrawablePadding = ScreenManager.dipTopx(context, 5f)
    }

}