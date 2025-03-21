package com.arithmetic.bean;

// 运算式树类
public class Expression {
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
    public String getOperator() {
        return operator;
    }

    public Fraction getValue() {
        return value;
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    public int getOperatorCount(){
        return operatorCount;
    }



    // 生成带格式的题目字符串(加括号是形式上的 实际计算由树结构决定)
    public String toProblemString() {
        if(isLeaf()) {
            return value.toString();
        }

        // 需不需要加括号
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
        // 获取当前运算符和子节点运算符 比较优先级
        int parentPriority = getPriority(this.operator);
        int childPriority = getPriority(child.operator);

        // 子节点优先级小于父节点
        if (childPriority < parentPriority){
            return true;
        }

        // 子节点是右子树 + 子节点优先级与父节点优先级相等 + 父节点是减/除 时 需要括号
        // 优先级相等时 如果是减/除括号会影响运算 因为计算时左右子树需要先算 所以此时得加括号
        if (childPriority == parentPriority) {
            if(!isLeft && ("-".equals(this.operator) || "÷".equals(this.operator))) {
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
    }
}
