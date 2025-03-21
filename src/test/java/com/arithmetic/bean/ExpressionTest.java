package com.arithmetic.bean;

import static org.junit.jupiter.api.Assertions.*;

class ExpressionTest {

    @org.junit.jupiter.api.Test
    void toProblemString() {
        // 测试加法
        Expression add = new Expression("+",
                new Expression(new Fraction(3)),
                new Expression(new Fraction(5))
        );
        assertEquals("3 + 5", add.toProblemString());

        // 测试自然数显示
        Expression naturalNumber = new Expression(new Fraction(7));
        assertEquals("7", naturalNumber.toProblemString());

        // 测试真分数显示
        Expression properFraction = new Expression(new Fraction(2, 3));
        assertEquals("2/3", properFraction.toProblemString());

        // 测试带分数显示
        Expression mixedFraction = new Expression(new Fraction(7, 3));
        assertEquals("2'1/3", mixedFraction.toProblemString());

        // 测试混合优先级（加法+乘法）
        Expression innerMul = new Expression("×",
                new Expression(new Fraction(5)),
                new Expression(new Fraction(2))
        );
        Expression addWithMul = new Expression("+",
                new Expression(new Fraction(3)),
                innerMul
        );
        assertEquals("3 + 5 × 2", addWithMul.toProblemString());

        // 测试需要左括号的情况（加法在乘法左侧）
        Expression addNode = new Expression("+",
                new Expression(new Fraction(3)),
                new Expression(new Fraction(5))
        );
        Expression mulWithAdd = new Expression("×",
                addNode,
                new Expression(new Fraction(4))
        );
        assertEquals("(3 + 5) × 4", mulWithAdd.toProblemString());

        // 测试除法右操作数需要括号
        Expression sub = new Expression("-",
                new Expression(new Fraction(5)),
                new Expression(new Fraction(3))
        );
        Expression div = new Expression("÷",
                new Expression(new Fraction(10)),
                sub
        );
        assertEquals("10 ÷ (5 - 3)", div.toProblemString());

        // 测试减法右操作数为同级运算（需要括号）
        Expression addRight = new Expression("+",
                new Expression(new Fraction(3)),
                new Expression(new Fraction(1))
        );
        Expression subWithAdd = new Expression("-",
                new Expression(new Fraction(8)),
                addRight
        );
        assertEquals("8 - (3 + 1)", subWithAdd.toProblemString());

        // 测试复杂嵌套结构
        Expression innerDiv = new Expression("÷",
                new Expression(new Fraction(6)),
                new Expression(new Fraction(2))
        );
        Expression mulLayer = new Expression("×",
                new Expression(new Fraction(5)),
                innerDiv
        );
        Expression finalExpr = new Expression("-",
                mulLayer,
                new Expression(new Fraction(1, 2))
        );
        assertEquals("5 × 6 ÷ 2 - 1/2", finalExpr.toProblemString());

        // 测试连续除法需要括号
        Expression div1 = new Expression("÷",
                new Expression(new Fraction(12)),
                new Expression(new Fraction(3))
        );
        Expression div2 = new Expression("÷",
                div1,
                new Expression(new Fraction(2))
        );
        assertEquals("12 ÷ 3 ÷ 2", div2.toProblemString());
    }
}