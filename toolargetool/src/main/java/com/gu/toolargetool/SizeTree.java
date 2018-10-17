package com.gu.toolargetool;

import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.List;

/**
 * Abstract base class representing a tree of items/subtrees with sizes.
 */
public final class SizeTree {

    @NonNull private final String key;
    private final int size;
    @NonNull private final List<SizeTree> subTrees;

    SizeTree(@NonNull String key, int size) {
        this(key, size, Collections.<SizeTree>emptyList());
    }

    SizeTree(@NonNull String key, int size, @NonNull List<SizeTree> subTrees) {
        this.key = key;
        this.size = size;
        this.subTrees = subTrees;
    }

    @NonNull
    public final String getKey() {
        return key;
    }

    public int getSize() {
        return size;
    }

    @NonNull
    public List<SizeTree> getSubTrees() {
        return subTrees;
    }
}
