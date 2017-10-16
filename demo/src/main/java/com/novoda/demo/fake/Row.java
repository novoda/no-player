package com.novoda.demo.fake;

import java.util.List;

class Row {

    final String heading;
    final List<Item> items;

    Row(String heading, List<Item> items) {
        this.heading = heading;
        this.items = items;
    }
}
