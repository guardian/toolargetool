package com.gu.toolargetool

import android.graphics.Point
import android.os.Bundle
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.ComparisonFailure
import org.junit.Test
import kotlin.math.abs
import kotlin.random.Random

class TooLargeToolTest {

    @Test
    fun sizeTreeFromBundleWorksForEmptyBundle() {
        val bundle = Bundle()
        val sizeTree = sizeTreeFromBundle(bundle)
        assertTrue("totalSize is not negative", sizeTree.totalSize >= 0)
        assertTrue("totalSize is small", sizeTree.totalSize < 100)
    }

    @Test
    fun sizeTreeFromBundleWorksForBundleContaining1KbByteArray() {
        val bundle = Bundle()
        bundle.putByteArray("bytes", ByteArray(1000))
        val sizeTree = sizeTreeFromBundle(bundle)
        assertRoughly("totalSize is roughly 10KB", 1000, sizeTree.totalSize, delta = 0.1)
        assertEquals("sizeTree has 1 item", 1, sizeTree.subTrees.size)
        assertRoughly("item is roughly 10KB", 1000, sizeTree.subTrees[0].totalSize, delta = 0.1)
    }

    @Test
    fun sizeTreeFromBundleWorksForBundleContaining10KbByteArray() {
        val bundle = Bundle()
        bundle.putByteArray("bytes", ByteArray(10000))
        val sizeTree = sizeTreeFromBundle(bundle)
        assertRoughly("totalSize is roughly 10KB", 10000, sizeTree.totalSize, delta = 0.01)
        assertEquals("sizeTree has 1 item", 1, sizeTree.subTrees.size)
        assertRoughly("item is roughly 10KB", 10000, sizeTree.subTrees[0].totalSize, delta = 0.01)
    }

    @Test
    fun sizeTreeFromBundleWorksForBundleContaining10x10KbByteArrays() {
        val bundle = Bundle()
        repeat(10) {
            bundle.putByteArray("bytes$it", ByteArray(10000))
        }
        val sizeTree = sizeTreeFromBundle(bundle)
        assertRoughly("totalSize is roughly 100KB", 100000, sizeTree.totalSize, delta = 0.01)
        assertEquals("sizeTree has 10 items", 10, sizeTree.subTrees.size)
        sizeTree.subTrees.forEach {
            assertRoughly("${it.key} is roughly 10KB", 10000, it.totalSize, delta = 0.01)
        }
    }

    @Test
    fun sizeTreeFromBundleWorksForBundleContainingRandomByteArrays() {
        val bundle = Bundle()
        val expectedSizes = mutableMapOf<String, Int>()
        var totalSize = 0
        repeat(10) {
            val kbs = (abs(Random.nextInt()) % 15) + 5
            val size = kbs * 1000
            val key = "bytes$it"
            expectedSizes[key] = size
            totalSize += size
            bundle.putByteArray(key, ByteArray(size))
        }
        val sizeTree = sizeTreeFromBundle(bundle)
        assertRoughly("totalSize is roughly $totalSize", totalSize, sizeTree.totalSize, delta = 0.01)
        assertEquals("sizeTree has 10 items", 10, sizeTree.subTrees.size)
        sizeTree.subTrees.forEach {
            val expectedSize = expectedSizes[it.key]!!
            assertRoughly("${it.key} is roughly $expectedSize", expectedSize, it.totalSize, delta = 0.01)
        }
    }

    @Test
    fun sizeAsParcelWorksForEmptyBundle() {
        val bundle = Bundle()
        val size = sizeAsParcel(bundle)
        assertTrue("size is not negative", size >= 0)
        assertTrue("size is small", size < 100)
    }

    @Test
    fun sizeAsParcelWorksForBundleContaining1KbByteArray() {
        val bundle = Bundle()
        bundle.putByteArray("bytes", ByteArray(1000))
        val size = sizeAsParcel(bundle)
        assertRoughly("size is roughly 1KB", 1000, size, delta = 0.1)
    }

    @Test
    fun sizeAsParcelWorksForBundleContaining10KbByteArray() {
        val bundle = Bundle()
        bundle.putByteArray("bytes", ByteArray(10000))
        val size = sizeAsParcel(bundle)
        assertRoughly("size is roughly 10KB", 10000, size, delta = 0.01)
    }

    @Test
    fun sizeAsParcelWorksForBundleContaining10x10KbByteArrays() {
        val bundle = Bundle()
        repeat(10) {
            bundle.putByteArray("bytes$it", ByteArray(10000))
        }
        val size = sizeAsParcel(bundle)
        assertRoughly("size is roughly 100KB", 100000, size, delta = 0.01)
    }

    @Test
    fun sizeAsParcelWorksForPoint() {
        val point = Point(1, 2)
        val size = sizeAsParcel(point)
        assertTrue("size is not negative", size >= 0)
        assertTrue("size is small", size < 100)
    }

    @Test
    fun sizeAsParcelWorksFor1KbParcelable() {
        val parcelable = ParcelableByteArray(1000)
        val size = sizeAsParcel(parcelable)
        assertRoughly("size is roughly 1KB", 1000, size, delta = 0.1)
    }

    @Test
    fun sizeAsParcelWorksFor10KbParcelable() {
        val parcelable = ParcelableByteArray(10000)
        val size = sizeAsParcel(parcelable)
        assertRoughly("size is roughly 10KB", 10000, size, delta = 0.01)
    }

    private fun assertRoughly(message:String, expected: Int, actual: Int, delta: Double) {
        val ratio = expected.toDouble() / actual.toDouble()
        if (abs(ratio - 1.0) > delta) {
            throw ComparisonFailure("$message (ratio=$ratio delta=$delta)", expected.toString(), actual.toString())
        }
    }
}
