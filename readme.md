# java程序graalvm打包编译成exe

### 下载安装JDK17

https://download.oracle.com/java/17/latest/jdk-17_windows-x64_bin.exe

### 下载安装graalvm

教程https://www.graalvm.org/latest/docs/getting-started/#install-graalvm

### 安装Native Image

```
gu install native-image
```

### 编写程序

```
 public class HelloWorld {
     public static void main(String[] args) {
         System.out.println("Hello, Native World!");
     }
 }

```

### 打包程序可执行jar（使用java -jar xxx.jar可执行）

### 编译jar成exe文件

 native-image -jar xxx.jar



### 注意事项

配置环境变量，各系统可能不一样，可根据错误提示的，配置具体位置

```
JAVA_HOME  C:\Users\chenyun\tools\graalvm-ce-java17-22.3.1

INCLUDE D:\Windows Kits\10\Include\10.0.19041.0\ucrt;C:\Program Files\Microsoft Visual Studio\2022\Community\VC\Tools\MSVC\14.34.31933\include;D:\Windows Kits\10\Include\10.0.19041.0\um;D:\Windows Kits\10\Include\10.0.19041.0\shared;

LIB  D:\Windows Kits\10\Lib\10.0.19041.0\um\x64;C:\Program Files\Microsoft Visual Studio\2022\Community\VC\Tools\MSVC\14.34.31933\lib\x64;D:\Windows Kits\10\Lib\10.0.19041.0\ucrt\x64;



path  C:\Users\chenyun\tools\graalvm-ce-java17-22.3.1\bin;C:\Program Files\Microsoft Visual Studio\2022\Community\VC\Tools\MSVC\14.34.31933\bin\Hostx86\x64
```

