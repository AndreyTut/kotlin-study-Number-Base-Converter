package converter

import java.math.BigDecimal
import java.math.BigInteger
import kotlin.math.pow
import kotlin.text.StringBuilder

// Do not delete this line

val mapTo16 = mapOf<Int, String>(
    0 to "0",
    1 to "1",
    2 to "2",
    3 to "3",
    4 to "4",
    5 to "5",
    6 to "6",
    7 to "7",
    8 to "8",
    9 to "9",
    10 to "a",
    11 to "b",
    12 to "c",
    13 to "d",
    14 to "e",
    15 to "f",
    16 to "g",
    17 to "h",
    18 to "i",
    19 to "j",
    20 to "k",
    21 to "l",
    22 to "m",
    23 to "n",
    24 to "o",
    25 to "p",
    26 to "q",
    27 to "r",
    28 to "s",
    29 to "t",
    30 to "u",
    31 to "v",
    32 to "w",
    33 to "x",
    34 to "y",
    35 to "z"
)

val mapFrom16 = mapOf(
    '0' to 0,
    '1' to 1,
    '2' to 2,
    '3' to 3,
    '4' to 4,
    '5' to 5,
    '6' to 6,
    '7' to 7,
    '8' to 8,
    '9' to 9,
    'a' to 10,
    'b' to 11,
    'c' to 12,
    'd' to 13,
    'e' to 14,
    'f' to 15,
    'g' to 16,
    'h' to 17,
    'i' to 18,
    'j' to 19,
    'k' to 20,
    'l' to 21,
    'm' to 22,
    'n' to 23,
    'o' to 24,
    'p' to 25,
    'q' to 26,
    'r' to 27,
    's' to 28,
    't' to 29,
    'u' to 30,
    'v' to 31,
    'w' to 32,
    'x' to 33,
    'y' to 34,
    'z' to 35
)

fun main() {
    while (true) {
        println("Enter two numbers in format: {source base} {target base} (To quit type /exit)")
        val input = readln()
        if (input == "/exit") break
        val (src, base) = input.split(" ")
        convertBetweenBases(src.toInt(), base.toInt())
    }
}

fun convertBetweenBases(src: Int, base: Int) {
    while (true) {
        println("Enter number in base $src to convert to base $base (To go back type /back)")
        val value = readln()
        if (value == "/back") break
        if (!value.contains(".")) {
            val dec = toDec(value, src)
            val result = fromDec(dec, base)
            println("Conversion result: $result")
            println()
        } else {
            val (intSrcPart, decSrcPart) = value.split(".")
            val intResPart = fromDec(toDec(intSrcPart, src), base)
            val fracConvertedToDec = convertFracToDec(decSrcPart, src)
            val decResPart = convertFracFromDec(BigDecimal(fracConvertedToDec), base)
            println("Conversion result: $intResPart.$decResPart")
        }
    }
}

fun toDec(srcNum: String, base: Int): BigInteger {
    val array = srcNum.toCharArray()
    var result = BigInteger.ZERO
    array.reverse()
    for (i in array.indices) {
        result += ((mapFrom16[array[i]]?.toBigInteger() ?: BigInteger.ZERO) * ((base.toBigInteger()).pow(i)))
    }
    return result
}

fun fromDec(sourceNumber: BigInteger, base: Int): String {
    return convertDecToBase(sourceNumber, base);
}

fun convertDecToBase(sourceNumber: BigInteger, intBase: Int): String {
    val resultList = mutableListOf<Int>()
    var currentValue = sourceNumber
    val base = intBase.toBigInteger()
    while (currentValue >= base) {
        resultList.add((currentValue % base).toInt())
        currentValue /= base
    }
    resultList.add(currentValue.toInt())
    resultList.reverse()
    val resultListStr = mutableListOf<String>()
    resultList.forEach { resultListStr.add(mapTo16[it].toString()) }
    return resultListStr.joinToString("")
}

fun convertFracFromDec(sourceNumberFrac: BigDecimal, base: Int): String {
    val result = StringBuilder("")
    var current = sourceNumberFrac.remainder(BigDecimal.ONE)
    var digitCounter = 0
    while (current != BigDecimal.ZERO) {
        current *= BigDecimal(base)
        val (intPart, decPart) = current.divideAndRemainder(BigDecimal.ONE)
        current = decPart
        result.append(mapTo16[intPart.toInt()])
        digitCounter++
        if (result.length == 5) break
    }
    if (digitCounter < 5) {
        val sb = StringBuilder(result.toString())
        for (i in digitCounter until 5)
            sb.append("0")
        return sb.toString()
    }
    return result.toString()
}

fun convertFracToDec(sourceNumberFrac: String, base: Int): Double {
    var result = 0.0
    for (i in sourceNumberFrac.indices) {
        result += (mapFrom16[sourceNumberFrac[i]] ?: 0) * (base.toDouble().pow(-1.0 * (i + 1)))
    }
    return result
}
