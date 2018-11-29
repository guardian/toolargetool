package com.gu.toolargetool

import android.graphics.Point
import android.os.Bundle
import org.junit.Assert.assertTrue
import org.junit.ComparisonFailure
import org.junit.Test
import kotlin.math.abs

class TooLargeToolTest {

    @Test
    fun sizeTreeFromBundle() {
    }

    @Test
    fun bundleSizeAsParcelableWorksForEmptyBundle() {
        val bundle = Bundle()
        val size = TooLargeTool.sizeAsParcel(bundle)
        assertTrue("size is not negative", size >= 0)
        assertTrue("size is small", size < 100)
    }

    @Test
    fun bundleSizeAsParcelableWorksForBundleContaining1KbByteArray() {
        val bundle = Bundle()
        bundle.putByteArray("bytes", ByteArray(1000))
        val size = TooLargeTool.sizeAsParcel(bundle)
        assertTrue("size is not negative", size >= 0)
        assertRoughly("size is roughly 1KB", 1000, size, delta = 0.1)
    }

    @Test
    fun bundleSizeAsParcelableWorksForBundleContaining10KbByteArray() {
        val bundle = Bundle()
        bundle.putByteArray("bytes", ByteArray(10000))
        val size = TooLargeTool.sizeAsParcel(bundle)
        assertTrue("size is not negative", size >= 0)
        assertRoughly("size is roughly 10KB", 10000, size, delta = 0.01)
    }

    @Test
    fun bundleSizeAsParcelableWorksForBundleContaining10x10KbByteArray() {
        val bundle = Bundle()
        repeat(10) {
            bundle.putByteArray("bytes$it", ByteArray(10000))
        }
        val size = TooLargeTool.sizeAsParcel(bundle)
        assertTrue("size is not negative", size >= 0)
        assertRoughly("size is roughly 100KB", 100000, size, delta = 0.01)
    }

    @Test
    fun parcelableSizeAsParcelWorksForPoint() {
        val point = Point(1, 2)
        val size = TooLargeTool.sizeAsParcel(point)
        assertTrue("size is not negative", size >= 0)
        assertTrue("size is small", size < 100)
    }

    @Test
    fun parcelableSizeAsParcelWorksFor1KbParcelable() {
        val parcelable = ParcelableByteArray(1000)
        val size = TooLargeTool.sizeAsParcel(parcelable)
        assertTrue("size is not negative", size >= 0)
        assertRoughly("size is roughly 1KB", 1000, size, delta = 0.1)
    }

    @Test
    fun parcelableSizeAsParcelWorksFor10KbParcelable() {
        val parcelable = ParcelableByteArray(10000)
        val size = TooLargeTool.sizeAsParcel(parcelable)
        assertTrue("size is not negative", size >= 0)
        assertRoughly("size is roughly 10KB", 10000, size, delta = 0.01)
    }

    private fun assertRoughly(message:String, expected: Int, actual: Int, delta: Double) {
        val ratio = expected.toDouble() / actual.toDouble()
        if (abs(ratio - 1.0) > delta) {
            throw ComparisonFailure("$message (ratio=$ratio delta=$delta)", expected.toString(), actual.toString())
        }
    }
}
