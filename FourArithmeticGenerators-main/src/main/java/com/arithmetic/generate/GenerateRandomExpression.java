package com.arithmetic.generate;

import com.arithmetic.bean.Expression;
import com.arithmetic.bean.Fraction;
import com.arithmetic.calculate.Calculator;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GenerateRandomExpression {
    private static final String[] OPS = {"+", "-", "×", "÷"};
    private static final Random rand = new Random();
    private final int maxNumber;
    private final int maxOperators;
    private final Set<String> generatedExpressions = new HashSet<>(); // 去重集合

    public GenerateRandomExpression(int maxNumber) {
        this(maxNumber, 3);
    }

    public GenerateRandomExpression(int maxNumber, int maxOperators) {
        this.maxNumber = maxNumber;
        this.maxOperators = maxOperators;
    }

    public Expression generateRandomExpression() {
        Expression expr;
        String exprStr;
        int retryCount = 0;

        do {
            expr = generateOperatorNode(maxOperators - 1);
            exprStr = expr.toProblemString();
            retryCount++;
        } while (generatedExpressions.contains(exprStr) && retryCount < 20); // 最多重试20次

        generatedExpressions.add(exprStr);
        return expr;
    }

    private Expression generateOperatorNode(int remainingOperators) {
        // 降低除法概率（1/6），优先选择其他运算符
        String op = OPS[rand.nextInt(6) < 4 ? rand.nextInt(3) : 3];

        // 动态分配运算符次数
        int leftRemaining = remainingOperators > 0 ?
                rand.nextInt(remainingOperators + 1) : 0;
        int rightRemaining = remainingOperators - leftRemaining;

        Expression left = generateSubExpression(leftRemaining);
        Expression right = generateSubExpression(rightRemaining);

        // 处理减法和除法约束
        if (op.equals("-")) {
            Fraction leftVal = Calculator.calculate(left);
            Fraction rightVal = Calculator.calculate(right);
            if (leftVal.compareTo(rightVal) < 0) {
                return generateOperatorNode(remainingOperators); // 重新生成
            }
        } else if (op.equals("÷")) {
            int retries = 0;
            while (retries < 10) {
                Fraction rightVal = Calculator.calculate(right);
                if (rightVal.equals(new Fraction(1)) ||
                        Calculator.calculate(left).divide(rightVal).getNumerator() >= rightVal.getDenominator()) {
                    right = generateValueNode();
                    retries++;
                } else {
                    break;
                }
            }
            if (retries >= 10) {
                op = OPS[rand.nextInt(3)]; // 更换为其他运算符
            }
        }

        return new Expression(op, left, right);
    }

    private Expression generateSubExpression(int remainingOperators) {
        return remainingOperators > 0 ?
                generateOperatorNode(remainingOperators - 1) :
                generateValueNode();
    }

    private Expression generateValueNode() {
        return rand.nextBoolean() ?
                new Expression(generateInteger()) :
                new Expression(generateFraction());
    }

    private Fraction generateInteger() {
        return new Fraction(rand.nextInt(maxNumber) + 1); // 生成1~maxNumber的整数
    }

    private Fraction generateFraction() {
        int denominator = rand.nextInt(maxNumber - 1) + 2; // 分母范围2~maxNumber
        int numerator = rand.nextInt(denominator - 1) + 1;  // 分子范围1~denominator-1
        return new Fraction(numerator, denominator);
    }
}