package com.arithmetic.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class FileUtil {
      // 文件写入
      public static void write(String fileName,String content) throws IOException{
          try{
              Path filePath = Paths.get(fileName);
              if(filePath.getParent() != null){
                  Files.createDirectories(filePath.getParent());
              }
              Files.write(filePath, content.getBytes(StandardCharsets.UTF_8));
          }catch (IOException e){
              System.err.println("写入文件失败: " + e.getMessage());
          }
      }

    public static List<String> readLines(String fileName) throws IOException {
        try {
            return Files.readAllLines(Paths.get(fileName));
        } catch (IOException e) {
            System.err.println("文件读取失败: " + fileName);
            return Collections.emptyList();
        }
    }
}
