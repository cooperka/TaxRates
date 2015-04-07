import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<TaxTuple> results = Tax.printTaxList(Arrays.asList(
            Single.ST_DED,
            Single.BRACKET_10,
            Single.BRACKET_15,
            Single.BRACKET_25,
            Single.BRACKET_28,
            Single.BRACKET_33,
            Single.BRACKET_35,
            Single.BRACKET_39,
            Single.SS,
            Single.MEDIC,
            Single.MEDIC_ADD)
        );

        for (TaxTuple t : results) {
            System.out.println(String.format("%d, %.2f", t.mIncomeLevel, t.mTotalTax));
        }
    }
}
