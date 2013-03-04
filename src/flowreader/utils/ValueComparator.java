/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flowreader.utils;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Map;

/**
 *
 * @author D-Day
 */
public class ValueComparator implements Comparator<String> {

    Map<String, BigDecimal> base;
    public ValueComparator(Map<String, BigDecimal> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.    
    @Override
    public int compare(String a, String b) {
        if (base.get(a).intValue() >= base.get(b).intValue()) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}
