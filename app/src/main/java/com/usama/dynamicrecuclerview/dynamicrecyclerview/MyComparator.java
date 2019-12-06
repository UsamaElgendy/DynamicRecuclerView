package com.usama.dynamicrecuclerview.dynamicrecyclerview;

import com.usama.dynamicrecuclerview.utils.Product;

import java.util.Comparator;

public class MyComparator {
    public final Comparator<Product> orderPorPosition = new Comparator<Product>() {
        @Override
        public int compare(Product mProduct1, Product mProduct2) {
            return mProduct1.getPosition() - mProduct2.getPosition();
        }
    };
}
