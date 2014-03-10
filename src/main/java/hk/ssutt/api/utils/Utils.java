package hk.ssutt.api.utils;

import java.util.Comparator;

public abstract class Utils {
    // Usage: Collections.sort(list, Utils.stringReverseOrderCmp), where list - a class, which implements List<String>
    public static final Comparator<String[]> groupArrayDirectOrderCmp = new Comparator<String[]>() {
        @Override
        public int compare(String[] o1, String[] o2) {
            return o1[0].compareTo(o2[0]);
        }
    };

    public static final Comparator<String> stringReverseOrderCmp = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            return o2.compareTo(o1);
        }
    };

    public static String elderGroup(String group) {
        String result = null;
         /*
            for fuck's sake, if there where no social science students
            and some strange guys who don't like numbers
            it was much much easier
            I just can't stand all of you
            by fau when writing this code
            */
        try {
            int elder = Integer.parseInt(group);
            elder += 100;
            result = Integer.toString(elder);
        } catch (NumberFormatException ex) {
            return null;
        }
        return result;
    }
}
