/**
 * List of taxes for single filers.
 */
public abstract class Single {

    /** Standard deduction for single filers */
    public static final int SD = 6200;

    /** Tax bracket thesholds, not including Standard deduction */
    public static final int[] BRACKETS = {9075, 36900, 89350, 186350, 405100, 406750};

    /** Maximum wages subject to SS tax */
    public static final int SS_THRESHOLD = 117000;

    /** Wage threshold before Additional Medicare tax */
    public static final int MEDIC_ADD_THRESH = 200000; // 250000 for MFJ

    /** Convenience for {@link Integer#MAX_VALUE} */
    public static final int INF = Integer.MAX_VALUE;

    static final Tax ST_DED = new Tax("Standard Deduction",       0.0,                  0,               SD);
    static final Tax BRACKET_10 = new Tax("10% Bracket",         10.0,                 SD, BRACKETS[0] + SD);
    static final Tax BRACKET_15 = new Tax("15% Bracket",         15.0,   BRACKETS[0] + SD, BRACKETS[1] + SD);
    static final Tax BRACKET_25 = new Tax("25% Bracket",         25.0,   BRACKETS[1] + SD, BRACKETS[2] + SD);
    static final Tax BRACKET_28 = new Tax("28% Bracket",         28.0,   BRACKETS[2] + SD, BRACKETS[3] + SD);
    static final Tax BRACKET_33 = new Tax("33% Bracket",         33.0,   BRACKETS[3] + SD, BRACKETS[4] + SD);
    static final Tax BRACKET_35 = new Tax("35% Bracket",         35.0,   BRACKETS[4] + SD, BRACKETS[5] + SD);
    static final Tax BRACKET_39 = new Tax("39.6% Bracket",       39.6,   BRACKETS[5] + SD, INF);
    static final Tax SS = new Tax("Social Security Tax",          6.2,                  0, SS_THRESHOLD);
    static final Tax MEDIC = new Tax("Medicare Tax",              1.45,                 0, INF);
    static final Tax MEDIC_ADD = new Tax("Addit'l Medicare Tax",  0.9,   MEDIC_ADD_THRESH, INF);

}