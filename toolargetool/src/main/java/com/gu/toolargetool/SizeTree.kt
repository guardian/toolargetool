package com.gu.toolargetool

/**
 * Abstract base class representing a tree of items/subtrees with sizes.
 */
data class SizeTree(
        val key: String,
        val totalSize: Int,
        val subTrees: List<SizeTree> = emptyList()
)
