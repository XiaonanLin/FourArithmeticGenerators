package com.arithmetic.utils;

import com.arithmetic.bean.Fraction;

public class FractionParser {
    public static Fraction parse(String input) {
        input = input.trim();
        try {
            if (input.contains("'")) {
                // 修复正则表达式：使用 ['/] 替代 '|/
                String[] parts = input.split("['/]");
                if (parts.length != 3) {
                    throw new IllegalArgumentException("无效的带分数格式: " + input);
                }
                int integer = Integer.parseInt(parts[0]);
                int numerator = Integer.parseInt(parts[1]);
                int denominator = Integer.parseInt(parts[2]);
                return new Fraction(integer * denominator + numerator, denominator);
            } else if (input.contains("/")) {
                String[] parts = input.split("/");
                if (parts.length != 2) {
                    throw new IllegalArgumentException("无效的分数格式: " + input);
                }
                return new Fraction(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
            } else {
                return new Fraction(Integer.parseInt(input), 1);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("无法解析的分数: " + input, e);
        }
    }
}
