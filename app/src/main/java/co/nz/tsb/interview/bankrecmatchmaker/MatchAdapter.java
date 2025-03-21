package co.nz.tsb.interview.bankrecmatchmaker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
         * @param checkedPositions
         */
        public void bind(MatchItem matchItem, OnItemCheckedListener listener, int position,
                         Set<Integer> checkedPositions) {
            mainText.setText(matchItem.getPaidTo());
            total.setText(Float.toString(matchItem.getTotal()));
            subtextLeft.setText(matchItem.getTransactionDate());
            subtextRight.setText(matchItem.getDocType());

            // Set the checkbox based on stored checked positions
            checkedListItem.setChecked(checkedPositions.contains(position));

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
     * Create a HashSet to store the checked items
     */
    private Set<Integer> checkedPositions = new HashSet<>();

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
        holder.bind(matchItem, listener, position, checkedPositions);
    }

    @Override
    public int getItemCount() {
        return matchItems.size();
    }

    /**
     * Function allow us to manually set the checked item
     * @param position
     * @param isChecked
     */
    public void setChecked(int position, boolean isChecked) {
        if (isChecked) {
            checkedPositions.add(position);
        } else {
            checkedPositions.remove(position);
        }
        // Update item list state
        notifyItemChanged(position);
        // Notify subscribers
        listener.onItemChecked(matchItems.get(position), isChecked);
    }

}