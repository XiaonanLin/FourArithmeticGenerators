package com.arithmetic.utils;

import com.arithmetic.bean.Fraction;

public class FractionParser {
    public static Fraction parse(String input) {
        // 去除首尾空格
        input = input.trim();
        try {
            // 带分数
            if (input.contains("'")) {
                // 匹配'/进行分割
                String[] parts = input.split("['/]");
                if (parts.length != 3) {
                    throw new IllegalArgumentException("无效的带分数格式: " + input);
                }
                //整数
                int integer = Integer.parseInt(parts[0]);
                //分子
                int numerator = Integer.parseInt(parts[1]);
                //分母
                int denominator = Integer.parseInt(parts[2]);
                return new Fraction(integer * denominator + numerator, denominator);
            } else if (input.contains("/")) {
                // 分数
                String[] parts = input.split("/");
                if (parts.length != 2) {
                    throw new IllegalArgumentException("无效的分数格式: " + input);
                }
                return new Fraction(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
            } else {
                // 整数
                return new Fraction(Integer.parseInt(input), 1);
            }
        } catch (NumberFormatException e) {
            // 数值错误
            throw new IllegalArgumentException("无法解析的分数: " + input, e);
        }
    }
}
