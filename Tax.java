import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Base class describing different types of taxes.
 *
 * Create separate instances to describe different brackets/types of taxes.
 */
public class Tax {

    private final String mName;
    private final float mRatePercent;
    private final int mBegin, mEnd;

    /**
     * @param percent: give in terms of [0, 100]
     */
    public Tax(String name, double percent, int beginningThreshold, int endingThreshold) {
        mName = name;
        mRatePercent = (float) percent;
        mBegin = beginningThreshold;
        mEnd = endingThreshold;
    }

    public static List<TaxTuple> printTaxList(Collection<Tax> taxes) {
        final List<TaxTuple> results = new ArrayList<TaxTuple>();

        // We will introduce taxes from the first queue as they phase in (and put them into the second queue).
        // Then we will discard them from the second queue as they phase out.
        final Queue<Tax> taxesToIntroduce = new PriorityQueue<Tax>(11, phaseInComparator());
        final Queue<Tax> taxesInEffect = new PriorityQueue<Tax>(11, phaseOutComparator());
        taxesToIntroduce.addAll(taxes);

        float totalTax = 0.0f;
        int lastIncomeLevel = 0;
        while (!taxesToIntroduce.isEmpty() || !taxesInEffect.isEmpty()) {
            // See if we should add and/or remove the next tax(es),
            // to determine the next income level where the marginal rate changes.
            final Tax taxToAdd = taxesToIntroduce.peek();
            final Tax taxToRemove = taxesInEffect.peek();

            final int addingIncomeLevel = (taxToAdd != null ? taxToAdd.mBegin : Integer.MAX_VALUE);
            final int removalIncomeLevel = (taxToRemove != null ? taxToRemove.mEnd : Integer.MAX_VALUE);
            
            final boolean shouldAdd = (addingIncomeLevel <= removalIncomeLevel);
            final boolean shouldRemove = (removalIncomeLevel <= addingIncomeLevel);

            // Compute tax at this income level
            final int currentIncomeLevel = Math.min(addingIncomeLevel, removalIncomeLevel);
            System.out.println(String.format("At income level of $%d", currentIncomeLevel));

            // Compute marginal tax rate in effect, up to this income level
            System.out.println("\tTaxes in effect up to this point:");
            float rate = 0.0f;
            for (Tax inEffect : taxesInEffect) {
                rate += inEffect.mRatePercent;
                System.out.println(String.format("\t%s (%.2f%%)", inEffect.mName, inEffect.mRatePercent));
            }

            // Add marginal tax incurred by moving from previous income level
            totalTax += (currentIncomeLevel - lastIncomeLevel) * rate / 100;
            lastIncomeLevel = currentIncomeLevel;
            System.out.println();
            System.out.println(String.format("\tMarginal rate is %.2f%% up to this point", rate));
            System.out.println(String.format("\tTotal tax is $%.2f up to this point", totalTax));
            System.out.println(String.format("\tAverage tax rate is %.2f%% up to this point", totalTax / currentIncomeLevel * 100));
            System.out.println();
            results.add(new TaxTuple(currentIncomeLevel, totalTax));

            // Get all taxes that phase in at this exact income level, if any,
            // and similarly all taxes that phase out, if any.
            final List<Tax> addNow = new ArrayList<Tax>();
            final List<Tax> removeNow = new ArrayList<Tax>();
            if (shouldAdd) {
                while (!taxesToIntroduce.isEmpty() && taxesToIntroduce.peek().mBegin == addingIncomeLevel) {
                    addNow.add(taxesToIntroduce.poll());
                }
            }
            if (shouldRemove) {
                while (!taxesInEffect.isEmpty() && taxesInEffect.peek().mEnd == removalIncomeLevel) {
                    removeNow.add(taxesInEffect.poll());
                }
            }

            // Phase taxes in and/or out
            for (Tax toRemove : removeNow) {
                System.out.println("\tRemoving " + toRemove.mName + " at " + removalIncomeLevel);
                // already removed from queue
            }
            for (Tax toAdd : addNow) {
                System.out.println("\tAdding " + toAdd.mName + " at " + addingIncomeLevel);
                taxesInEffect.add(toAdd);
                // already removed from queue
            }

            System.out.println();
        }

        return results;
    }

    /**
     * @return a Comparator that sorts Taxes by their beginning threshold, ascending,
     *   used to choose which Tax(es) to phase in next.
     */
    private static Comparator<Tax> phaseInComparator() {
        return new Comparator<Tax>() {
            @Override
            public int compare(Tax a, Tax b) {
                return compareTwoInts(a.mBegin, b.mBegin);
            }
        };
    }

    /**
     * @return a Comparator that sorts Taxes by their ending threshold, ascending,
     *   used to choose which Tax(es) to phase out.
     */
    private static Comparator<Tax> phaseOutComparator() {
        return new Comparator<Tax>() {
            @Override
            public int compare(Tax a, Tax b) {
                return compareTwoInts(a.mEnd, b.mEnd);
            }
        };
    }

    private static int compareTwoInts(int a, int b) {
        // Checking case-by-case, to avoid numerical over/underflow with MAX_VALUE
        if (a < b) {
            return -1;
        } else if (a > b) {
            return 1;
        } else {
            return 0;
        }
    }
}
