package hk.ssutt.utils;

import java.util.Comparator;

public abstract class Utils {
    // Usage: Collections.sort(list, Utils.stringReverseOrderCmp), where list - a class, which implements List<String>
    public static final Comparator<String[]> groupArrayDirectOrderCmp = new Comparator<String[]>() {
        @Override
        public int compare(String[] o1, String[] o2) {
            return o1[0].compareTo(o2[0]);
        }
    };
    public static final Comparator<String[]> groupArrayReverseOrderCmp = new Comparator<String[]>() {
        @Override
        public int compare(String[] o1, String[] o2) {
            return o2[0].compareTo(o1[0]);
        }
    };
}
