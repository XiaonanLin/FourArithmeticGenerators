package com.arithmetic.generate;

import com.arithmetic.bean.Expression;
import com.arithmetic.bean.Fraction;
import com.arithmetic.calculate.Calculator;
import com.arithmetic.utils.FileUtil;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
            Fraction leftValue = Calculator.calculate(left);
            Fraction rightValue = Calculator.calculate(right);

            // 比较值的大小（或字符串表示）
            if (leftValue.compareTo(rightValue) > 0) {
                Expression temp = left;
                left = right;
                right = temp;
            }
        }
        // 处理减法不能产生负数
        if (op.equals("-")) {
            Fraction leftVal = Calculator.calculate(left);
            Fraction rightVal = Calculator.calculate(right);
            if (leftVal.compareTo(rightVal) < 0) {
                // 交换左右子树
                Expression temp = left;
                left = right;
                right = temp;
            }
        }

        // 处理除法必须结果为真分数
        if (op.equals("÷")) {
            Fraction rightVal = Calculator.calculate(right);
            // 最大重试次数
            int maxRetries = 50;
            // 重试次数
            int retryCount = 0;
            // 如果右操作数是0或结果非真分数(分子大于等于分母)，重新生成右子树
            // 防止生成不出要的右子树而陷入死循环
            while ((rightVal.getNumerator() == 0 ||
                    Calculator.calculate(left).divide(rightVal).getNumerator() >= rightVal.getDenominator()) && retryCount < maxRetries) {
                right = generateValueNode();
                rightVal = Calculator.calculate(right);
                retryCount++;
            }

            if(retryCount >= maxRetries){
                // 返回默认右子树
                return new Expression("÷",new Expression(new Fraction(1)),
                        new Expression(new Fraction(1)));
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

    public static void main(String[] args) {
        GenerateRandomExpression generator = new GenerateRandomExpression(10);
        StringBuilder exercises = new StringBuilder();
        StringBuilder answers = new StringBuilder();

        try{
            for (int i = 0; i < 10000; i++) {
                Expression expr = generator.generateRandomExpression();
                if (expr == null) {
                    System.out.println("第 " + (i + 1) + " 个表达式生成失败");
                    continue;
                }

                String problem = expr.toProblemString();
                Fraction answer = Calculator.calculate(expr);

                // 添加题目到 exercises
                exercises.append(i + 1)
                        .append(". ")
                        .append(problem)
                        .append("\n");

                // 添加答案到 answers
                answers.append(i + 1)
                        .append(". ")
                        .append(answer)
                        .append("\n");
                }


            FileUtil.write("Exercises.txt", exercises.toString());
            FileUtil.write("Answers.txt", answers.toString());
            System.out.println("文件生成成功！");
        }catch (IOException e){
            System.err.println("文件写入失败: " + e.getMessage());
            e.printStackTrace();
        }
    }


}
