package com.arithmetic.calculate;

import com.arithmetic.bean.Expression;
import com.arithmetic.bean.Fraction;
import com.arithmetic.utils.FractionParser;

import java.util.Stack;

// 计算
public class Calculator {
    // 计算表达式树的值（生成节点时需要使用）
    public static Fraction calculate(Expression expr) {
        if (expr.isLeaf()) {
            return expr.getValue();
        }

        Fraction leftVal = calculate(expr.getLeft());
        Fraction rightVal = calculate(expr.getRight());

        switch (expr.getOperator()) {
            case "+": return leftVal.add(rightVal);
            case "-": return leftVal.sub(rightVal);
            case "×": return leftVal.multiply(rightVal);
            case "÷": return leftVal.divide(rightVal);
            default: throw new IllegalArgumentException("未知运算符: " + expr.getOperator());
        }
    }

    // 直接解析并计算表达式字符串（比较两个文件时使用）
    public static Fraction calculate(String expression) {
        // (匹配一个或多个连续空格)去除空格 添加“#”作为结束符
        expression = expression.replaceAll("\\s+", "") + "#";

        // 操作数栈
        Stack<Fraction> numStack = new Stack<>();
        // 运算符栈
        Stack<Character> opStack = new Stack<>();

        //初始化
        opStack.push('#');

        // 字符下标
        int index = 0;

        // 逐个字符处理表达式
        while (index < expression.length()) {
            char c = expression.charAt(index);

            // 遇到数字、带分数、分数分隔符
            if (Character.isDigit(c) || c == '\'' || c == '/') {

                // 解析数字
                StringBuilder numStr = new StringBuilder();

                // 连续读取数字部分
                while (index < expression.length() && (Character.isDigit(c) || c == '/' || c == '\'')) {
                    numStr.append(c);
                    index++;
                    if (index < expression.length()){
                        c = expression.charAt(index);
                    }
                    else{
                        break;
                    }
                }

                // 将数字字符串转化为Fraction对象入操作数栈
                numStack.push(parseNumber(numStr.toString()));
            } else if (c == '(') {
                // 遇到左括号入符号栈
                opStack.push(c);
                index++;
            } else if (c == ')') {
                // 遇到右括号触发括号内运算
                while (opStack.peek() != '(') {
                    // 没检索到左括号前就不断弹出
                    Fraction b = numStack.pop();
                    Fraction a = numStack.pop();

                    // 计算完入操作数栈
                    numStack.push(applyOp(opStack.pop(), a, b));
                }
                opStack.pop();
                index++;
            } else if (isOperator(c)) {
                // 如果栈顶运算符优先级 先计算它的运算
                // 栈顶的先进晚出 优先级高的话不先计算被后面运算符入栈 之后弹出计算就晚了
                while (comparePriority(opStack.peek(), c) >= 0) {
                    Fraction b = numStack.pop();
                    Fraction a = numStack.pop();
                    numStack.push(applyOp(opStack.pop(), a, b));
                }
                opStack.push(c);
                index++;
            } else {
                index++;
            }
        }

        // 处理剩余运算符
        while (opStack.peek() != '#') {
            Fraction b = numStack.pop();
            Fraction a = numStack.pop();
            // 用弹出的运算符进行运算
            numStack.push(applyOp(opStack.pop(), a, b));
        }

        return numStack.pop();
    }

    // 直接调用FractionParser.parse 将数字字符串转化为Fraction对象
    private static Fraction parseNumber(String numStr) {
        return FractionParser.parse((numStr));
    }

    // 判断是否为运算符（获取c在字符串中的索引）
    private static boolean isOperator(char c) {
        return "+-×÷".indexOf(c) != -1;
    }

    // 获取优先级
    private static int getPriority(char op) {
        return switch (op) {
            // 左括号优先级最低
            case '(' -> 0;
            case '+', '-' -> 1;
            case '×', '÷' -> 2;
            case '#' -> -1;
            default -> throw new IllegalArgumentException("未知运算符: " + op);
        };
    }

    // 比较优先级
    private static int comparePriority(char op1, char op2) {
        if (op1 == '('){
            return -1; // 左括号不弹出（除非遇到右括号）
        }
        return getPriority(op1) - getPriority(op2);
    }

    // 将符号用于运算
    private static Fraction applyOp(char op, Fraction a, Fraction b) {
        switch (op) {
            case '+': return a.add(b);
            case '-': return a.sub(b);
            case '×': return a.multiply(b);
            case '÷': return a.divide(b);
            default: throw new IllegalArgumentException("未知运算符: " + op);
        }
    }
}
