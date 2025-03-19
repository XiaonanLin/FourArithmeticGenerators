package com.arithmetic.bean;

import java.util.Objects;

public class Fraction {
    private final int numerator;    // 分子
    private final int denominator;  // 分母（始终为正）

    //------------------------ 构造函数 ------------------------//
    /**
     * 构造分数（自动约分和标准化）
     * @param numerator 分子
     * @param denominator 分母（不能为0）
     */
    public Fraction(int numerator, int denominator) {
        if (denominator == 0) {
            throw new IllegalArgumentException("分母不能为零");
        }

        // 处理符号：确保分母始终为正
        if (denominator < 0) {
            numerator = -numerator;
            denominator = -denominator;
        }

        // 求最大公约数并约分
        int gcd = gcd(Math.abs(numerator), denominator);
        this.numerator = numerator / gcd;
        this.denominator = denominator / gcd;
    }

    /**
     * 构造自然数分数（分母=1）
     * @param number 自然数值
     */
    public Fraction(int number) {
        this(number, 1);
    }

    //------------------------ 基本运算 ------------------------//
    public Fraction add(Fraction other) {
        int newNumerator = this.numerator * other.denominator
                + other.numerator * this.denominator;
        int newDenominator = this.denominator * other.denominator;
        return new Fraction(newNumerator, newDenominator);
    }

    public Fraction subtract(Fraction other) {
        return this.add(new Fraction(-other.numerator, other.denominator));
    }

    public Fraction multiply(Fraction other) {
        return new Fraction(
                this.numerator * other.numerator,
                this.denominator * other.denominator
        );
    }

    public Fraction divide(Fraction other) {
        return new Fraction(
                this.numerator * other.denominator,
                this.denominator * other.numerator
        );
    }

    //------------------------ 比较运算 ------------------------//
    public int compareTo(Fraction other) {
        long left = (long) this.numerator * other.denominator;
        long right = (long) other.numerator * this.denominator;
        return Long.compare(left, right);
    }

    //------------------------ 格式化和解析 ------------------------//
    @Override
    public String toString() {
        if (denominator == 1) {
            return Integer.toString(numerator); // 自然数形式
        }

        if (Math.abs(numerator) > denominator) {
            int whole = numerator / denominator;
            int remainder = Math.abs(numerator % denominator);
            return String.format("%d'%d/%d", whole, remainder, denominator);
        }
        return String.format("%d/%d", numerator, denominator);
    }

    /**
     * 从字符串解析分数（支持格式：5, 3/4, 2'3/4）
     */
    public static Fraction parse(String s) {
        if (s.contains("'")) {
            // 带分数格式：2'3/4 → 11/4
            String[] parts = s.split("['/]");
            int whole = Integer.parseInt(parts[0]);
            int num = Integer.parseInt(parts[1]);
            int den = Integer.parseInt(parts[2]);
            return new Fraction(whole * den + num, den);
        } else if (s.contains("/")) {
            // 真分数格式：3/4
            String[] parts = s.split("/");
            return new Fraction(
                    Integer.parseInt(parts[0]),
                    Integer.parseInt(parts[1])
            );
        } else {
            // 自然数格式：5
            return new Fraction(Integer.parseInt(s));
        }
    }

    //------------------------ 工具方法 ------------------------//
    private static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    //------------------------ 访问器 ------------------------//
    public int getNumerator() {
        return numerator;
    }

    public int getDenominator() {
        return denominator;
    }

    //------------------------ 相等性判断 ------------------------//
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        Fraction fraction = (Fraction) o;
        return numerator == fraction.numerator
                && denominator == fraction.denominator;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numerator, denominator);
    }
}
