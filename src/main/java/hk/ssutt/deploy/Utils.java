package hk.ssutt.deploy;

import java.util.Comparator;

public abstract class Utils {
    // Usage: Collections.sort(list, Utils.stringReverseOrderCmp), where list - a class, which implements List<String>
    public static final Comparator<String> stringReverseOrderCmp = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            return o2.compareTo(o1);
        }
    };
}
