package me.jmoulins.utils

import java.math.BigInteger

fun ByteArray.toBooleanArray(): BooleanArray {
    val outputArray = BooleanArray(this.size * 8)
    for (i in indices) {
        outputArray[i * 8] = ((get(i).toInt().shr(7) and 1) == 1)
        outputArray[i * 8 + 1] = ((get(i).toInt().shr(6) and 1) == 1)
        outputArray[i * 8 + 2] = ((get(i).toInt().shr(5) and 1) == 1)
        outputArray[i * 8 + 3] = ((get(i).toInt().shr(4) and 1) == 1)
        outputArray[i * 8 + 4] = ((get(i).toInt().shr(3) and 1) == 1)
        outputArray[i * 8 + 5] = ((get(i).toInt().shr(2) and 1) == 1)
        outputArray[i * 8 + 6] = ((get(i).toInt().shr(1) and 1) == 1)
        outputArray[i * 8 + 7] = ((get(i).toInt().shr(0) and 1) == 1)
    }
    return outputArray
}

val BooleanArray.asBinStr
    inline get() = this.joinToString(separator = "") { if (it) "1" else "0" }

fun Boolean.toInt() = if (this) 1 else 0

fun Boolean.toBigInteger(): BigInteger = if (this) BigInteger.ONE else BigInteger.ZERO