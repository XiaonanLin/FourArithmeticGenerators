package com.arithmetic.calculate;

import com.arithmetic.bean.Expression;
import com.arithmetic.bean.Fraction;

public class Calculator {
    // 计算表达式树的值
    public static Fraction cauculate(Expression expr) {
        if (expr.isLeaf()) {
            return expr.getValue();
        }

        Fraction leftVal = cauculate(expr.getLeft());
        Fraction rightVal = cauculate(expr.getRight());

        switch (expr.getOperator()) {
            case "+": return leftVal.add(rightVal);
            case "-": return leftVal.sub(rightVal);
            case "×": return leftVal.multiply(rightVal);
            case "÷": return leftVal.divide(rightVal);
            default: throw new IllegalArgumentException("未知运算符: " + expr.getOperator());
        }
    }
}
