package me.jmoulins.year_2021

import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigInteger

class Day16KtTest {

    @Test
    fun sumAllVersionNumbers() {
        assertEquals(6, Packet.build(convertHexToBooleanArray("D2FE28")).sumAllVersionNumbers())
        assertEquals(9, Packet.build(convertHexToBooleanArray("38006F45291200")).sumAllVersionNumbers())
        assertEquals(14, Packet.build(convertHexToBooleanArray("EE00D40C823060")).sumAllVersionNumbers())
        assertEquals(16, Packet.build(convertHexToBooleanArray("8A004A801A8002F478")).sumAllVersionNumbers())
        assertEquals(12, Packet.build(convertHexToBooleanArray("620080001611562C8802118E34")).sumAllVersionNumbers())
        assertEquals(23, Packet.build(convertHexToBooleanArray("C0015000016115A2E0802F182340")).sumAllVersionNumbers())
        assertEquals(31, Packet.build(convertHexToBooleanArray("A0016C880162017C3686B18A3D4780")).sumAllVersionNumbers())
    }

    @Test
    fun evaluateExpression() {
        assertEquals(3.toBigInteger(), Packet.build(convertHexToBooleanArray("C200B40A82")).evaluateExpression())
        assertEquals(54.toBigInteger(), Packet.build(convertHexToBooleanArray("04005AC33890")).evaluateExpression())
        assertEquals(7.toBigInteger(), Packet.build(convertHexToBooleanArray("880086C3E88112")).evaluateExpression())
        assertEquals(9.toBigInteger(), Packet.build(convertHexToBooleanArray("CE00C43D881120")).evaluateExpression())
        assertEquals(BigInteger.ONE, Packet.build(convertHexToBooleanArray("D8005AC2A8F0")).evaluateExpression())
        assertEquals(BigInteger.ZERO, Packet.build(convertHexToBooleanArray("F600BC2D8F")).evaluateExpression())
        assertEquals(BigInteger.ZERO, Packet.build(convertHexToBooleanArray("9C005AC2F8F0")).evaluateExpression())
        assertEquals(BigInteger.ONE, Packet.build(convertHexToBooleanArray("9C0141080250320F1802104A08")).evaluateExpression())
    }
}