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
         * Bind the item check listener and check status to the view, once user
         * toggle the MatchItem, subscribers will get the update
         *
         * @param matchItem
         * @param isChecked
         * @param listener
         */
        public void bind(MatchItem matchItem, boolean isChecked, OnItemCheckedListener listener) {
            mainText.setText(matchItem.getPaidTo());
            total.setText(Float.toString(matchItem.getTotal()));
            subtextLeft.setText(matchItem.getTransactionDate());
            subtextRight.setText(matchItem.getDocType());

            // Set the checkbox based on stored checked states
            checkedListItem.setChecked(isChecked);

            // Handle click event to toggle checkbox
            checkedListItem.setOnClickListener(v ->
                    //notify subscribers
                    listener.onItemChecked(getAdapterPosition(), !isChecked)
            );
        }
    }

    private final List<MatchItem> matchItems;
    /**
     * OnItemCheckedListener, listen the check behavior when user checked the MatchItem
     */
    private final OnItemCheckedListener listener;
    /**
     * Create a List to store the checked states
     */
    private final List<Boolean> checkedStates;

    /**
     * provide an interface to handle onItemChecked
     */
    public interface OnItemCheckedListener {
        void onItemChecked(int position, boolean isChecked);
    }

    public MatchAdapter(List<MatchItem> matchItems, List<Boolean> checkedStates, OnItemCheckedListener listener) {
        this.matchItems = matchItems;
        this.checkedStates = checkedStates;
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
        holder.bind(matchItem, checkedStates.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return matchItems.size();
    }

    /**
     * Provide a function to update the check state and re-render the UI
     * @param position
     * @param isChecked
     */
    public void updateCheckedState(int position, boolean isChecked) {
        checkedStates.set(position, isChecked);
        notifyItemChanged(position);
    }

}