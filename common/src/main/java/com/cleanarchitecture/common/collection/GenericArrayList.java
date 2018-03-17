package com.cleanarchitecture.common.collection;

import java.util.ArrayList;

public class GenericArrayList<T> extends GenericCollection {

    public GenericArrayList(Class<T> aclass) {
        super(ArrayList.class, aclass);
    }
}
