package com.gu.toolargetool.model;

import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.List;

/**
 * A leaf in a {@link SizeTree} with a size and no sub-trees.
 */
public final class SizeTreeLeaf extends SizeTree {

    private final int size;

    protected SizeTreeLeaf(@NonNull String key, int size) {
        super(key);
        this.size = size;
    }

    @Override
    public int getSize() {
        return size;
    }

    @NonNull
    @Override
    public List<SizeTree> getSubTrees() {
        return Collections.emptyList();
    }
}
