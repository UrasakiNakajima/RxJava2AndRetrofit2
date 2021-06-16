package com.mobile.resource_module.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.mikepenz.fontawesome_typeface_library.FontAwesome
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic
import com.mobile.common_library.callback.RcvOnItemViewClickListener
import com.mobile.common_library.custom_view.ColorTextView
import com.mobile.common_library.manager.ScreenManager
import com.mobile.resource_module.R
import com.mobile.resource_module.bean.Result2

class ResourceAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = "ResourceAdapter"
    private var list: MutableList<Result2> = mutableListOf()

    fun clearData() {
        this.list.clear()
        notifyDataSetChanged()
    }

    fun addAllData(list: MutableList<Result2>) {
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
                holder.layoutCardView.setVisibility(View.GONE)
                holder.layoutPicture.setVisibility(View.VISIBLE)
//                Picasso.with(context).load(list.get(position).url).placeholder(R.mipmap.ic_launcher_round).into(imvResource)

//                Glide.with(context).load(list.get(position).url)
//                        .placeholder(R.mipmap.picture_miyawaki_sakura)
//                        .error(R.mipmap.picture_miyawaki_sakura)
//                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC) //自动选择缓存策略
//                        .priority(Priority.NORMAL)     // 设置加载的优先级
//                        .into(holder.imvResource)

                holder.layoutPicture.setOnClickListener() {
                    rcvOnItemViewClickListener!!.onItemClickListener(position, it);
                }
                Glide.with(context).load(list.get(position).url)
                        .placeholder(R.mipmap.picture_miyawaki_sakura)
                        .error(R.mipmap.picture_miyawaki_sakura)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC) //自动选择缓存策略
                        .priority(Priority.NORMAL)     // 设置加载的优先级
                        .listener(object : RequestListener<Drawable> {

                            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                                holder.imvResource.visibility = View.VISIBLE
                                return false
                            }

                            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                return false
                            }
                        })
                        .into(holder.imvResource)
            } else {
                holder.layoutCardView.setVisibility(View.VISIBLE)
                holder.layoutPicture.setVisibility(View.GONE)
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

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    protected class ContentHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val layoutCardView: CardView = itemView.findViewById(R.id.layout_card_view)
        val tevResource: ColorTextView = itemView.findViewById(R.id.tev_resource)
        val layoutPicture: FrameLayout = itemView.findViewById(R.id.layout_picture)
        val imvResource: ImageView = itemView.findViewById(R.id.imv_resource)
    }

    private var rcvOnItemViewClickListener: RcvOnItemViewClickListener? = null

    fun setRcvOnItemViewClickListener(rcvOnItemViewClickListener: RcvOnItemViewClickListener) {
        this.rcvOnItemViewClickListener = rcvOnItemViewClickListener
    }

    private fun setIconDrawable(view: TextView, icon: IIcon) {
        view.setCompoundDrawablesWithIntrinsicBounds(IconicsDrawable(context)
                .icon(icon)
                .color(ContextCompat.getColor(context, R.color.color_4876FF))
                .sizeDp(14), null, null, null)
        view.compoundDrawablePadding = ScreenManager.dpToPx(context, 5f)
    }

}