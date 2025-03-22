package co.nz.tsb.interview.bankrecmatchmaker;

import java.util.ArrayList;
import java.util.List;

/**
 * This class uses to automatically find the matching items of remaining total
 */
public class MatchFinder {
    //Result class to store the matching result and remain total value
    public static class MatchResult {
        public final List<Integer> matchedIndexes;
        public final float remainingTotal ;

        public MatchResult(List<Integer> matchedIndexes, float remainingTotal) {
            this.matchedIndexes = matchedIndexes;
            this.remainingTotal = remainingTotal;
        }
    }

    /**
     * Loop all the match items and find the item or items that equals to target
     * @param items
     * @param target
     * @return
     */
    public MatchResult findInitialMatches(List<MatchItem> items, float target) {
        List<Integer> matchedIndexes = new ArrayList<>();
        float remainingTotal = target;

        int singleMatchedIndex = findSingleMatch(items, remainingTotal);

        if (singleMatchedIndex != -1) {
            matchedIndexes.add(singleMatchedIndex);
            remainingTotal -= items.get(singleMatchedIndex).getTotal();
        }
        return new MatchResult(matchedIndexes, remainingTotal);
    }

    /**
     * Find the single match item that matches with the target value, return the index or -1 if can't
     * find
     * @param items
     * @param target
     * @return
     */
    private int findSingleMatch(List<MatchItem> items, float target) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getTotal() - target == 0) {
                return i;
            }
        }
        return -1;
    }
}
