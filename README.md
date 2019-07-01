# KafkaTool2LicenseGenerate

KafkaTool2 License生成工具,仅供学习使用.

## 使用方式

1. 打包程序
  进入程序根目录执行`mvn clean package`

2. 生成License
  打包后进入`target`目录,执行jar.
  命令如下:
  ```
  java -jar KafkaTool2LicenseGenerate.jar <name> <company> [directory]
  ```

  若未指定License生成路径,将会生成在当前目录下,参考命令如下:
  ```
  java -jar KafkaTool2LicenseGenerate.jar Hello World
  ```

