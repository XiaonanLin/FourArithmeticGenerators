package com.arithmetic.calculate;

import com.arithmetic.bean.Expression;
import com.arithmetic.bean.Fraction;
import com.arithmetic.utils.FractionParser;

import java.util.Stack;

public class Calculator {
    // 计算表达式树的值
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

    // 直接解析并计算表达式字符串
    public static Fraction calculate(String expression) {
        expression = expression.replaceAll("\\s+", "") + "#";
        Stack<Fraction> numStack = new Stack<>();
        Stack<Character> opStack = new Stack<>();
        opStack.push('#');

        int index = 0;
        while (index < expression.length()) {
            char c = expression.charAt(index);
            if (Character.isDigit(c) || c == '\'' || c == '/') {
                // 解析数字
                StringBuilder numStr = new StringBuilder();
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
                numStack.push(parseNumber(numStr.toString()));
            } else if (c == '(') {
                opStack.push(c);
                index++;
            } else if (c == ')') {
                while (opStack.peek() != '(') {
                    Fraction b = numStack.pop();
                    Fraction a = numStack.pop();
                    numStack.push(applyOp(opStack.pop(), a, b));
                }
                opStack.pop();
                index++;
            } else if (isOperator(c)) {
                // 强制处理高优先级运算符
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
            numStack.push(applyOp(opStack.pop(), a, b));
        }

        return numStack.pop();
    }



    private static Fraction parseNumber(String numStr) {
        return FractionParser.parse((numStr));
    }

    private static boolean isOperator(char c) {
        return "+-×÷".indexOf(c) != -1;
    }

    private static int getPriority(char op) {
        return switch (op) {
            case '(' -> 0; // 左括号优先级最低
            case '+', '-' -> 1;
            case '×', '÷' -> 2;
            case '#' -> -1;
            default -> throw new IllegalArgumentException("未知运算符: " + op);
        };
    }

    private static int comparePriority(char op1, char op2) {
        if (op1 == '('){
            return -1; // 左括号不弹出（除非遇到右括号）
        }
        return getPriority(op1) - getPriority(op2);
    }

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
