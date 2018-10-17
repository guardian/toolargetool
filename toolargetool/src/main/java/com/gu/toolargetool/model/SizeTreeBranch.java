package com.gu.toolargetool.model;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * A branch in a size tree with no size of its own but a collection of sub-trees, which may be
 * either leaves or more branches, that contribute to its total size.
 */
public final class SizeTreeBranch extends SizeTree {

    @NonNull private final List<SizeTree> subTrees;

    protected SizeTreeBranch(@NonNull String key, @NonNull List<SizeTree> subTrees) {
        super(key);
        this.subTrees = subTrees;
    }

    @Override
    public int getSize() {
        int total = 0;
        for (SizeTree tree: subTrees) {
            total += tree.getSize();
        }
        return total;
    }

    @NonNull
    @Override
    public List<SizeTree> getSubTrees() {
        return subTrees;
    }
}
