package com.arithmetic.bean;

import java.util.Objects;

public class Fraction {
    //分子
    private final int numerator;

    //分母
    private final int denominator;

    public int getNumerator() {
        return numerator;
    }

    public int getDenominator() {
        return denominator;
    }

    public Fraction(int numerator, int denominator) {
        if (denominator == 0) {
            System.out.println("分母不能为零");
        }

        // 确保分母为正
        if (denominator < 0) {
            numerator = -numerator;
            denominator = -denominator;
        }

        // 求最大公约数并约分
        int gcd = gcd(Math.abs(numerator), denominator);
        this.numerator = numerator / gcd;
        this.denominator = denominator / gcd;
    }

    // 将自然数转化为分数
    public Fraction(int num) {
        // 调用另一个构造函数
        this(num, 1);
    }


    // 加法
    public Fraction add(Fraction other) {
        int newNumerator = this.numerator * other.denominator
                + other.numerator * this.denominator;
        int newDenominator = this.denominator * other.denominator;
        return new Fraction(newNumerator, newDenominator);
    }

    // 减法（利用加法）
    public Fraction sub(Fraction other) {
        return this.add(new Fraction(-other.numerator, other.denominator));
    }

    // 乘法
    public Fraction multiply(Fraction other) {
        return new Fraction(this.numerator * other.numerator,
                this.denominator * other.denominator);
    }

    // 除法
    public Fraction divide(Fraction other) {
        return new Fraction(
                this.numerator * other.denominator,
                this.denominator * other.numerator);
    }


    public int compareTo(Fraction other) {
        long left = (long) this.numerator * other.denominator;
        long right = (long) other.numerator * this.denominator;
        return Long.compare(left, right);
    }

    // 将分数输出为字符串
    @Override
    public String toString() {
        // 自然数
        if (denominator == 1) {
            return Integer.toString(numerator);
        }

        // 假分数(以带分数形式输出)
        if (Math.abs(numerator) > denominator) {
            int whole = numerator / denominator;
            int remainder = Math.abs(numerator % denominator);
            return String.format("%d'%d/%d", whole, remainder, denominator);
        }

        // 真分数
        return String.format("%d/%d", numerator, denominator);
    }

    // 将字符串转化为分数
    public static Fraction parse(String s) {
        if (s.contains("'")) {
            // 带分数
            String[] parts = s.split("['/]");
            int whole = Integer.parseInt(parts[0]);
            int num = Integer.parseInt(parts[1]);
            int den = Integer.parseInt(parts[2]);
            return new Fraction(whole * den + num, den);
        } else if (s.contains("/")) {
            // 真分数
            String[] parts = s.split("/");
            return new Fraction(
                    Integer.parseInt(parts[0]),
                    Integer.parseInt(parts[1])
            );
        } else {
            // 自然数
            return new Fraction(Integer.parseInt(s));
        }
    }

    // 生成最大公约数
    private static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    // 重写equals和hashCode 提高效率
    @Override
    public boolean equals(Object o) {

        // 地址相同
        if (this == o) {
            return true;
        }

        // 类型不同
        if (o == null || getClass() != o.getClass()){
            return false;
        }

        Fraction other = (Fraction) o;
        // 交叉相乘比较值是否相等（如 3/3 和 1/1 视为相等）
        return this.numerator * other.denominator == this.denominator * other.numerator;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numerator, denominator);
    }
}
