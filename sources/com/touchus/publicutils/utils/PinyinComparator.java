package com.touchus.publicutils.utils;

import com.touchus.publicutils.bean.Person;
import java.util.Comparator;

public class PinyinComparator implements Comparator<Person> {
    public int compare(Person o1, Person o2) {
        if (o1.getRemark() == null) {
            return 0;
        }
        if (o1.getRemark().equals("@") || o2.getRemark().equals("#")) {
            return -1;
        }
        if (o1.getRemark().equals("#") || o2.getRemark().equals("@")) {
            return 1;
        }
        return o1.getRemark().compareTo(o2.getRemark());
    }
}
