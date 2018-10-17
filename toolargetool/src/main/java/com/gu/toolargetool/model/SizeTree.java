package com.gu.toolargetool.model;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Abstract base class representing a tree of items/subtrees with sizes.
 */
public abstract class SizeTree {

    @NonNull private final String key;

    SizeTree(@NonNull String key) {
        this.key = key;
    }

    @NonNull
    public final String getKey() {
        return key;
    }

    public abstract int getSize();

    @NonNull
    public abstract List<SizeTree> getSubTrees();
}
