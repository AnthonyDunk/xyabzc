package com.anthonydunk.deputychallenge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ListContent {


    public static final List<Item> ITEMS = new ArrayList<Item>();
    public static final Map<String, Item> ITEM_MAP = new HashMap<String, Item>();
    //private static final int COUNT = 0;

    public static void addItem(Item item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static class Item {
        public final String id;
        public final String content;
        public final ShiftDetails details;

        public Item(String id, String content, ShiftDetails details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
