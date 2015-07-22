package com.michaelmiklavcic.falconer.test.util;

public class TypedVal {
    public Class<?> type;
    public Object val;

    public TypedVal(Class<?> type, Object val) {
        this.type = type;
        this.val = val;
        if (this.type == null) {
            throw new RuntimeException("Type argument cannot be null");
        }
    }

    public static TypedVal of(Class<?> type, Object val) {
        return new TypedVal(type, val);
    }

    public static TypedVal[] list(Object... items) {
        if (items == null) {
            return new TypedVal[0];
        }
        if (items.length % 2 != 0) {
            throw new RuntimeException("Expected an even number of items but got: " + items.length);
        }
        TypedVal[] typedVals = new TypedVal[items.length / 2];
        for (int i = 0; i < items.length; i++) {
            TypedVal tv = TypedVal.of((Class<?>) items[i], items[++i]);
            typedVals[i / 2] = tv;
        }
        return typedVals;
    }
}
