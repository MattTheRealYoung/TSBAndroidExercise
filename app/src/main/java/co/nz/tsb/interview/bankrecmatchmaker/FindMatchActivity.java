package co.nz.tsb.interview.bankrecmatchmaker;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FindMatchActivity extends AppCompatActivity {

    public static final String TARGET_MATCH_VALUE = "co.nz.tsb.interview.target_match_value";
    // Record the remaining total number
    private float remainingTotal;
    private TextView matchText;
    private MatchAdapter adapter;
    private List<MatchItem> items;
    // Record the check states of all the match items
    private List<Boolean> checkedStates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_match);

        // UI initialize
        initializeUI();
        initializeRecyclerView();

        // Update the target matching value
        float target = getIntent().getFloatExtra(TARGET_MATCH_VALUE, 390f);
        matchText.setText(getString(R.string.select_matches, (int) target));
        // Initial remaining number
        remainingTotal = target;
        // Update match text
        updateMatchText();

        // Auto-selecting matching item or items after initialization
        initialMatching(remainingTotal);
    }

    /**
     * UI initialization of toolbar, target text
     */
    private void initializeUI() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        matchText = findViewById(R.id.match_text);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_find_match);
    }

    /**
     * Initialize the recyclerview and integrate the mock data
     */
    private void initializeRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        items = buildMockData();
        checkedStates = new ArrayList<>(Collections.nCopies(items.size(), false));
        adapter = new MatchAdapter(items, checkedStates, this::OnItemChecked);
        recyclerView.setAdapter(adapter);
    }

    /**
     * Auto-select the item or items sum that equals to target value
     * @param target
     */
    private void initialMatching(float target) {
        MatchFinder finder = new MatchFinder();
        MatchFinder.MatchResult result = finder.findInitialMatches(items, target);
        for (int index: result.matchedIndexes) {
            checkedStates.set(index, true);
            adapter.updateCheckedState(index, true);
        }
        remainingTotal = result.remainingTotal;
        updateMatchText();
    }

    /**
     * On item checked listener that handle checking logic
     * @param position
     * @param isChecked
     */
    private void OnItemChecked(int position, boolean isChecked) {
        float itemTotal = items.get(position).getTotal();
        if (isChecked) {
            remainingTotal -= itemTotal;
        } else {
            remainingTotal += itemTotal;
        }
        adapter.updateCheckedState(position, isChecked);
        updateMatchText();
    }

    /**
     * Update match text to reflect the latest result
     */
    private void updateMatchText(){
        matchText.setText(getString(R.string.select_matches, (int) remainingTotal));
    }

    private List<MatchItem> buildMockData() {
        List<MatchItem> items = new ArrayList<>();
        items.add(new MatchItem("City Limousines", "30 Aug", 249.00f, "Sales Invoice"));
        items.add(new MatchItem("Ridgeway University", "12 Sep", 618.50f, "Sales Invoice"));
        items.add(new MatchItem("Cube Land", "22 Sep", 495.00f, "Sales Invoice"));
        items.add(new MatchItem("Bayside Club", "23 Sep", 234.00f, "Sales Invoice"));
        items.add(new MatchItem("SMART Agency", "12 Sep", 250f, "Sales Invoice"));
        items.add(new MatchItem("PowerDirect", "11 Sep", 108.60f, "Sales Invoice"));
        items.add(new MatchItem("PC Complete", "17 Sep", 216.99f, "Sales Invoice"));
        items.add(new MatchItem("Truxton Properties", "17 Sep", 181.25f, "Sales Invoice"));
        items.add(new MatchItem("MCO Cleaning Services", "17 Sep", 170.50f, "Sales Invoice"));
        items.add(new MatchItem("Gateway Motors", "18 Sep", 411.35f, "Sales Invoice"));
        return items;
    }

}