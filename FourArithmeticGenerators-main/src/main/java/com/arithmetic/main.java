package com.arithmetic;

import com.arithmetic.bean.Expression;
import com.arithmetic.bean.Fraction;
import com.arithmetic.calculate.Calculator;
import com.arithmetic.generate.GenerateRandomExpression;
import com.arithmetic.utils.FileUtil;
import com.arithmetic.utils.FractionParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class main {
    public static void main(String[] args) throws IOException{
        if (args.length != 4) {
            System.out.println("Usage: \n生成模式: -n 数量 -r 范围\n校对模式: -e 题目文件 -a 答案文件");
            return;
        }

        // 判断模式
        boolean isGenerate = args[0].equals("-n") && args[2].equals("-r");
        boolean isGrade = args[0].equals("-e") && args[2].equals("-a");
        if (!isGenerate && !isGrade) {
            System.out.println("参数错误！请检查命令格式");
            return;
        }

        // 生成问题、答案
        if (isGenerate) {
            // 问题数目
            int n = Integer.parseInt(args[1]);
            // 操作数范围
            int r = Integer.parseInt(args[3]);

            // 储存题目、答案
            StringBuilder ex = new StringBuilder(), ans = new StringBuilder();

            GenerateRandomExpression gen = new GenerateRandomExpression(r);
            for (int i = 1; i <= n; i++) {
                // 生成
                Expression expr = gen.generateRandomExpression();
                ex.append(i).append(". ").append(expr.toProblemString()).append(" =\n");
                ans.append(i).append(". ").append(Calculator.calculate(expr)).append("\n");
            }

            // 写入文件
            FileUtil.write("Exercises.txt", ex.toString());
            FileUtil.write("Answers.txt", ans.toString());
            System.out.println("生成完成：Exercises.txt 和 Answers.txt");
        }
        // 校对题目答案
        else {
            String exerciseFile = args[1];
            String answerFile = args[3];

            try {
                // 读取文件内容
                List<String> exerciseLines = FileUtil.readLines(exerciseFile);
                List<String> answerLines = FileUtil.readLines(answerFile);

                // 提取题目和答案
                List<String> problems = extractContent(exerciseLines);
                List<String> answers = extractContent(answerLines);

                // 正确和错误题号
                List<Integer> correctNumbers = new ArrayList<>();
                List<Integer> wrongNumbers = new ArrayList<>();

                for (int i = 0; i < problems.size(); i++) {
                    // 获取题目和对应答案
                    String problem = problems.get(i);
                    String userAnswerStr = answers.get(i);

                    try {
                        // 计算正确答案
                        Fraction correctAnswer = Calculator.calculate(problem);
                        // 解析用户答案（转化为分数类）
                        Fraction userAnswer = FractionParser.parse(userAnswerStr);

                        // 对比结果 放入题号
                        if (correctAnswer.equals(userAnswer)) {
                            correctNumbers.add(i + 1);
                        } else {
                            wrongNumbers.add(i + 1);
                        }
                    } catch (Exception e) {
                        System.err.println("题目 " + (i+1) + " 处理失败: " + e.getMessage());
                        wrongNumbers.add(i + 1);
                    }
                }

                // 生成统计结果
                String result = buildResult(correctNumbers, wrongNumbers);
                FileUtil.write("Grade.txt", result);
                System.out.println("统计结果已生成至 Grade.txt");

            } catch (IOException e) {
                System.err.println("文件处理失败: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }


    // 提取每行的内容（去掉序号）
    private static List<String> extractContent(List<String> lines) {
        // 拆分序号和内容
        // 将集合转化为流  按.分割字符串 取分割后的第二部分
        return lines.stream()
                .map(line -> line.split("\\. ")[1])
                .collect(Collectors.toList());
    }

    // 构建结果字符串
    private static String buildResult(List<Integer> correct, List<Integer> wrong) {
        return String.format(
                "Correct: %d (%s)\nWrong: %d (%s)",
                correct.size(), formatNumbers(correct),
                wrong.size(), formatNumbers(wrong)
        );
    }

    // 格式化数字列表
    private static String formatNumbers(List<Integer> numbers) {
        // 排序
        Collections.sort(numbers);
        // 转化为流 收集数字用,连接
        return numbers.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
    }
}

