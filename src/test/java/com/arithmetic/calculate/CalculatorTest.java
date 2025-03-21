package com.arithmetic.calculate;

import com.arithmetic.bean.Expression;
import com.arithmetic.bean.Fraction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    @Test
    void calculate() {
        // 测试单个数值
        Expression numExpr = new Expression(new Fraction(5));
        assertEquals(new Fraction(5), Calculator.calculate(numExpr));

        // 测试加法
        Expression addExpr = new Expression("+",
                new Expression(new Fraction(3)),
                new Expression(new Fraction(4))
        );
        assertEquals(new Fraction(7), Calculator.calculate(addExpr));

        // 测试分数运算
        Expression fractionAdd = new Expression("+",
                new Expression(new Fraction(1, 2)),
                new Expression(new Fraction(1, 3))
        );
        assertEquals(new Fraction(5, 6), Calculator.calculate(fractionAdd));

        // 测试复杂表达式树
        Expression complexExpr = new Expression("×",
                new Expression("+",
                        new Expression(new Fraction(3)),
                        new Expression(new Fraction(2))
                ),
                new Expression("÷",
                        new Expression(new Fraction(4)),
                        new Expression(new Fraction(2))
                )
        );
        assertEquals(new Fraction(10), Calculator.calculate(complexExpr));
    }

    // 测试用String运算式算结果
    @Test
    void testCalculate() {
        // 测试运算符优先级
        assertEquals(new Fraction(11), Calculator.calculate("3+4×2"));

        // 测试带括号运算
        assertEquals(new Fraction(14), Calculator.calculate("(3+4)×2"));

        // 测试带分数解析
        assertEquals(new Fraction(28, 5), Calculator.calculate("5'3/5"));

        // 测试复杂表达式
        assertEquals(new Fraction(1), Calculator.calculate("4 - (1/3×8÷(8/9))"));

        // 测试除法真分数结果
        assertEquals(new Fraction(3, 8), Calculator.calculate("3/4 ÷ 2"));

        // 测试减法结果非负
        assertEquals(new Fraction(3), Calculator.calculate("5-2"));

        // 测试复杂带分数运算
        assertEquals(new Fraction(21, 8), Calculator.calculate("(5-4-1/6×3/4)×3"));
    }
}