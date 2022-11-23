package com.phone.aidl_client.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.phone.aidl_client.Book;
import com.phone.aidl_client.R;

import java.util.ArrayList;
import java.util.List;

/**
 * author : Urasaki
 * e-mail : 1164688204@qq.com
 * date   : 2021/6/28 13:34
 * desc   :
 * version: 1.0
 */
public class BookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = BookAdapter.class.getSimpleName();
    private Context mContext;
    private List<Book> mBookList = new ArrayList<>();

    public BookAdapter(Context context) {
        mContext = context;
    }

    public void clearData() {
        notifyItemRangeRemoved(0, this.mBookList.size());
        notifyItemRangeChanged(0, this.mBookList.size());
        this.mBookList.clear();
    }

    public void addData(List<Book> bookList) {
        notifyItemRangeInserted(mBookList.size(), bookList.size());
        mBookList.addAll(bookList);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_book, parent, false);
        return new BodyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BodyHolder) {
            BodyHolder bodyHolder = (BodyHolder) holder;
            bodyHolder.tevBookName.setText(mBookList.get(position).getName());
            bodyHolder.tevBookContent.setText(mBookList.get(position).getContent());
        }
    }

    @Override
    public int getItemCount() {
        return mBookList.size();
    }

    private static class BodyHolder extends RecyclerView.ViewHolder {

        private TextView tevBookName;
        private TextView tevBookContent;

        public BodyHolder(@NonNull View itemView) {
            super(itemView);

            tevBookName = (TextView) itemView.findViewById(R.id.tev_book_name);
            tevBookContent = (TextView) itemView.findViewById(R.id.tev_book_content);
        }
    }
}
