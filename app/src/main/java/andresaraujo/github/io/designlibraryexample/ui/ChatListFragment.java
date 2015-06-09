package andresaraujo.github.io.designlibraryexample.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import andresaraujo.github.io.designlibraryexample.R;

public class ChatListFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.fragment_chat_list, container, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(new ChatListAdapter(getActivity(), randomList(15)));

        return recyclerView;
    }

    private List<String> randomList(int amount) {
        ArrayList<String> list = new ArrayList<String>();
        int i = 0;
        while(list.size() < amount) {
            list.add("Octocat " + (++i));
        }
        return list;
    }

    public static class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatItemViewHolder> {
        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        private List<String> mValues;

        public ChatListAdapter(Context context, List<String> items) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            mValues = items;
        }

        public String getValueAt(int position) {
            return mValues.get(position);
        }

        @Override
        public ChatItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_chat_item, parent, false);
            view.setBackgroundResource(mBackground);

            return new ChatItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ChatItemViewHolder holder, int position) {
            holder.mBoundString =  mValues.get(position);
            holder.mTextView.setText(mValues.get(position));

            holder.mView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, ChatDetailsActivity.class);
                    intent.putExtra(DummyDetailsActivity.EXTRA_NAME, holder.mBoundString);

                    context.startActivity(intent);
                }
            });

            Glide.with(holder.mImageView.getContext())
                    .load(R.drawable.octocat2)
                    .fitCenter()
                    .into(holder.mImageView);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public static class ChatItemViewHolder extends RecyclerView.ViewHolder {
            public String mBoundString;

            public final View mView;
            public final ImageView mImageView;
            public final TextView mTextView;

            public ChatItemViewHolder(View itemView) {
                super(itemView);
                mView = itemView;

                mImageView = (ImageView) itemView.findViewById(R.id.profile_pic);
                mTextView = (TextView) itemView.findViewById(android.R.id.text1);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mTextView.getText();
            }
        }
    }
}
