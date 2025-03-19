package com.arithmetic.bean;

public class Expression {
    private static final String[] OPS = {"+", "-", "×", "÷"};
    private final String operator;
    private final Expression left;
    private final Expression right;
    private final Fraction value;
    private final int operatorCount;

    // 叶子节点构造
    public Expression(Fraction value) {
        this.value = value;
        this.operator = null;
        this.left = null;
        this.right = null;
        this.operatorCount = 0;
    }

    // 运算符节点构造
    public Expression(String op, Expression left, Expression right) {
        this.operator = op;
        this.left = left;
        this.right = right;
        this.value = null;
        this.operatorCount = left.operatorCount + right.operatorCount + 1;
    }

    // 生成规范字符串（去重关键）
    public String getCanonicalForm() {
        if(isLeaf()){
            return value.toString();
        }

        String leftStr = left.getCanonicalForm();
        String rightStr = right.getCanonicalForm();

        // 处理可交换运算符
        if (isCommutative()) {
            if (leftStr.compareTo(rightStr) > 0) {
                String temp = leftStr;
                leftStr = rightStr;
                rightStr = temp;
            }
        }
        return "(" + leftStr + operator + rightStr + ")";
    }

    private boolean isCommutative() {
        return operator.equals("+") || operator.equals("×");
    }

    // 生成带格式的题目字符串
    public String toProblemString() {
        if(isLeaf()) {
            return value.toString();
        }

        String leftStr = needBrackets(left, true) ?
                "(" + left.toProblemString() + ")" : left.toProblemString();
        String rightStr = needBrackets(right, false) ?
                "(" + right.toProblemString() + ")" : right.toProblemString();

        return leftStr + " " + operator + " " + rightStr;
    }

    private boolean needBrackets(Expression child, boolean isLeft) {
        if (child.isLeaf()){
            return false;
        }
        // 根据运算符优先级判断是否需要括号
        int parentPriority = getPriority(this.operator);
        int childPriority = getPriority(child.operator);
        if (childPriority < parentPriority){
            return true;
        }
        if (childPriority == parentPriority) {
            if (!isLeft && ("-".equals(this.operator) || "÷".equals(this.operator))) {
                return true;
            }
        }
        return false;
    }

    // 优先级
    private int getPriority(String op) {
        return switch (op) {
            case "+", "-" -> 1;
            case "×", "÷" -> 2;
            default -> 0;
        };
    }

    // 判断是否是叶子节点（数值节点）
    public boolean isLeaf() {
        return operator == null;
        // 或更严格的判断：return operator == null && left == null && right == null;
    }


}
