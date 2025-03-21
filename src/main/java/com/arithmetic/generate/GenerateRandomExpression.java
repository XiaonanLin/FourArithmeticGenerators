package com.arithmetic.generate;

import com.arithmetic.bean.Expression;
import com.arithmetic.bean.Fraction;
import com.arithmetic.calculate.Calculator;

import java.util.Random;

public class GenerateRandomExpression {
    private static final String[] OPS = {"+", "-", "×", "÷"};
    private static final Random rand = new Random();
    // 数值范围
    private final int maxNumber;

    // 最大运算符个数
    private final int maxOperators = 3;


    public GenerateRandomExpression(int maxNumber) {
        this.maxNumber = maxNumber;
    }

    public Expression generateRandomExpression() {
        return generateExpression(0);
    }

    // 生成树
    private Expression generateExpression(int operatorCount) {
        // 防止超过最大运算符个数（3个）
        if (operatorCount >= maxOperators) {
            return generateValueNode();
        }
        // 50%概率生成运算符节点
        if (rand.nextBoolean()) {
            Expression node = generateOperatorNode(operatorCount + 1);
            return node;
        } else {
            return generateValueNode();
        }
    }

    // 判断是否为“+” "×"
    private boolean isCommutative(String op) {
        return op.equals("+") || op.equals("×");
    }

    // 生成运算符节点
    private Expression generateOperatorNode(int operatorCount) {
        // 随机选择运算符
        String op = OPS[rand.nextInt(4)];

        // 递归生成左右子树
        Expression left = generateExpression(operatorCount);
        Expression right = generateExpression(operatorCount);

        // 判断是否为可交换运算符
        if(isCommutative(op)){
            // 计算左右子树的值
            Fraction leftValue = Calculator.cauculate(left);
            Fraction rightValue = Calculator.cauculate(right);

            // 比较值的大小（或字符串表示）
            if (leftValue.compareTo(rightValue) > 0) {
                Expression temp = left;
                left = right;
                right = temp;
            }
        }
        // 处理减法不能产生负数
        if (op.equals("-")) {
            Fraction leftVal = Calculator.cauculate(left);
            Fraction rightVal = Calculator.cauculate(right);
            if (leftVal.compareTo(rightVal) < 0) {
                // 交换左右子树
                Expression temp = left;
                left = right;
                right = temp;
            }
        }

        // 处理除法必须结果为真分数
        if (op.equals("÷")) {
            Fraction rightVal = Calculator.cauculate(right);
            // 如果右操作数是0或结果非真分数(分子大于等于分母)，重新生成右子树
            while (rightVal.getNumerator() == 0 ||
                    Calculator.cauculate(left).divide(rightVal).getNumerator() >= rightVal.getDenominator()) {
                right = generateValueNode();
                rightVal = Calculator.cauculate(right);
            }
        }

        return new Expression(op, left, right);
    }

    private Expression generateValueNode() {
        // 50%概率生成整数，50%生成分数
        if (rand.nextBoolean()) {
            return new Expression(new Fraction(rand.nextInt(maxNumber - 1) + 1));
        } else {
            return generateFraction();
        }
    }

    private Expression generateFraction() {
        // 分母[1, maxNumber)
        int denominator = rand.nextInt(maxNumber - 1) + 1;
        // 分子[0, denominator-1]
        int numerator = rand.nextInt(denominator);


        return new Expression(new Fraction(numerator, denominator));
    }

    // 示例测试
    public static void main(String[] args) {
        GenerateRandomExpression generator = new GenerateRandomExpression(10);
        for (int i = 0; i < 120; i++) {
            Expression expr = generator.generateRandomExpression();
            System.out.println("题目: " + expr.toProblemString());
            System.out.println("答案: " + Calculator.cauculate(expr) + "\n");
        }
    }

}
