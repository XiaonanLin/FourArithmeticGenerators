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
                // 解析数字或分数
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
            }  else if (c == '(') {
            opStack.push(c);
            index++;
        } else if (c == ')') {
            // 修复括号处理：正确识别括号范围
            while (opStack.peek() != '(') {
                Fraction b = numStack.pop();
                Fraction a = numStack.pop();
                numStack.push(applyOp(opStack.pop(), a, b));
            }
            opStack.pop(); // 弹出左括号
            index++;
        } else if (isOperator(c)) {
            // 严格处理优先级（乘除左结合）
            while (comparePriority(opStack.peek(), c) > 0) {
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

    public static void main(String[] args) {
        System.out.println(calculate("5'3/5"));      // 输出: 28/5
        System.out.println(calculate("3/7 + 8/7"));   // 输出: 11/7
        System.out.println(calculate("4 - (1/3×8÷(8/9))")); // 输出: 1/1
    }

}
