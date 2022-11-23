package com.example.fetchrewards;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

    /**
     * This is a recyclerview adapter class, the purpose of this class is to act as a bridge between the
     * collection (arraylist) and the view (recyclerview). This class provides 3 methods that are
     * utilised for binding the data to the view. The explanation of each method is provided in comments
     * within the methods.
     */
    public class ListAdapter extends RecyclerView.Adapter<ItemViewholder> {

        private final List<JsonItem> itemList;
        private final Context context;

        /**
         * Creates a PersonAdapter with the provided arraylist of Person objects.
         *
         * @param itemList    arraylist of person object.
         * @param context   context of the activity used for inflating layout of the viewholder.
         */
        public ListAdapter(List<JsonItem> itemList, Context context) {
            this.itemList = itemList;
            this.context = context;
        }

        @NonNull
        @Override
        public ItemViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ItemViewholder(LayoutInflater.from(context).inflate(R.layout.item_json, null));
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewholder holder, int position) {
            holder.name.setText(itemList.get(position).getName());
            holder.id.setText(String.valueOf(itemList.get(position).getId()));
            holder.listid.setText(String.valueOf(itemList.get(position).getListId()));
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }
    }

