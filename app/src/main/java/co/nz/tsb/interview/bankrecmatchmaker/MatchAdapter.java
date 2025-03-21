package co.nz.tsb.interview.bankrecmatchmaker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mainText;
        private TextView total;
        private TextView subtextLeft;
        private TextView subtextRight;
        private CheckedListItem checkedListItem;

        public ViewHolder(View itemView) {
            super(itemView);
            checkedListItem = (CheckedListItem) itemView;
            mainText = itemView.findViewById(R.id.text_main);
            total = itemView.findViewById(R.id.text_total);
            subtextLeft = itemView.findViewById(R.id.text_sub_left);
            subtextRight = itemView.findViewById(R.id.text_sub_right);
        }

        /**
         * We should pass in the item check listener and position to bind with the view, once user
         * checked the MatchItem, subscribers will get the update
         * @param matchItem
         * @param listener
         * @param position
         */
        public void bind(MatchItem matchItem, OnItemCheckedListener listener, int position) {
            mainText.setText(matchItem.getPaidTo());
            total.setText(Float.toString(matchItem.getTotal()));
            subtextLeft.setText(matchItem.getTransactionDate());
            subtextRight.setText(matchItem.getDocType());
            // Handle click event to toggle checkbox
            checkedListItem.setOnClickListener(v -> {
                boolean isChecked = !checkedListItem.isChecked();
                checkedListItem.setChecked(isChecked);
                //notify subscribers
                listener.onItemChecked(matchItem, isChecked);
            });
        }
    }

    private List<MatchItem> matchItems;
    /**
     * OnItemCheckedListener, listen the check behavior when user checked the MatchItem
     */
    private OnItemCheckedListener listener;

    /**
     * provide an interface to handle onItemChecked event
     */
    public interface OnItemCheckedListener {
        void onItemChecked(MatchItem item, boolean isChecked);
    }

    public MatchAdapter(List<MatchItem> matchItems, OnItemCheckedListener listener) {
        this.matchItems = matchItems;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        CheckedListItem listItem = (CheckedListItem) layoutInflater.inflate(R.layout.list_item_match, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MatchItem matchItem = matchItems.get(position);
        holder.bind(matchItem, listener, position);
    }

    @Override
    public int getItemCount() {
        return matchItems.size();
    }

}