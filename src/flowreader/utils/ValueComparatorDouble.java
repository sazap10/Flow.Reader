/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.utils;

import java.util.Comparator;
import java.util.Map;

/**
 *
 * @author D-Day
 */
public class ValueComparatorDouble implements Comparator<String> {

    Map<String, Double> base;
    public ValueComparatorDouble(Map<String, Double> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.    
    @Override
    public int compare(String a, String b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}
