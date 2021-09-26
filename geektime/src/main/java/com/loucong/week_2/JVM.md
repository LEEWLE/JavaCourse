# JVM 启动参数
### 参数分类
#### -D 开头
设置系统属性
#### -X 开头
非标准参数，基本都是传给 JVM 的，默认 JVM 实现这些参数的功能，但是并不保证所有的 JVM 实现都满足，且不保证向后兼容。可以使用 `Java -X` 命令查看当前 JVM 支持的非标准参数
#### -XX 开头
非稳定参数，专门用于控制 JVM 的行为，跟具体的 JVM 实现有关，随时可能会在下版本取消。`-XX:+/-Flags 形式，+- 是对布尔值进行开关` `-XX:key=value 形式，指定某个选项的值`


### 系统属性参数
| 参数 | 意义 |
| --- | --- |
| -Dfile.encoding=UTF-8 | 设置文件编码 |
| -Duser.timezone=GMT+08 | 设置时区 |
| -Dmaven.test.skip | maven 测试跳过 |
| -Dio.netty.eventLoopThreads=8 | netty 线程 |

代码设置系统参数：
`System.setProperty("key","value")`
### 运行模式参数
| 参数 | 意义 |
| --- | --- |
| -server | 设置 JVM 使用 server 模式，特点是启动速度比较慢，但运行时性能和内存管理效率很高，适用于生产环境。64位的 JDK 环境下默认启动该模式，忽略 -client 模式 |
| -client | JDK 7 之前在 32 位的 x86 机器上的默认值是 -client 选项。设置 JVM 使用 client 模式，特点是启动速度特别快，但运行时性能和内存管理效率不高，通常用于客户端应用程序或者 PC 应用开发和调试 |
| -Xint | 在解释模式下运行，-Xint 标记会强制 JVM 解释执行所有的字节码，会降低运行速度，通常低 10倍或更多 |
| -Xcomp | JVM 第一次使用时会把所有的字节码编译成本地代码，从而带来最大程度的优化 |
| -Xmixed | 混合模式，将解释模式和编译模式进行混合使用，由 JVM 自己决定，这是 JVM 的默认模式，也是推荐模式。（java -version 可以看某些信息） |

### 堆内存设置参数
| 参数 | 意义 |
| --- | --- |
| -Xmx | 指定最大堆内存 |
| -Xms | 指定堆内存空间的初始大小 |
| -Xmn | 等价于 -XX:NewSize，使用 G1 不应该设置，在其他场景可以设置。官方建议设置为 -Xmx 的 二分之一到四分之一 |
| -XX:MaxPermSize=size | JDK 7之前永久代大小 |
| -XX:MaxMetaspaceSize=size | Java8 默认不限制 Meta 空间，一半不允许设置该选项 |
| -XX:MaxDirectMemorySize=size | 系统可以使用的最大堆外内存，这个参数跟 -Dsun.nio.MaxDirectMemorySize 效果xiangtong |
| -Xss | 设置每个线程栈的字节数，影响栈的深度 |

### GC 设置参数
| 参数 | 意义 |
| --- | --- |
| -XX:+UseG1GC | 使用 G1 垃圾回收器 |
| -XX:+UseConcMarkSweepGC | 使用 CMS 垃圾回收器 |
| -XX:+UseSerialGC | 使用串行垃圾回收器 |
| -XX:+UseParallelGC | 使用并行垃圾回收器 |
|  XX：+UnlockExperimentalVMOptions -XX:+UseZGC   | JDK11 ，使用 ZGC |
|  -XX：+UnlockExperimentalVMOptions -XX:+UseShenandoahGC   | JDK12，使用 ShenandoahGC   |

### 分析诊断参数
| 参数 | 意义 |
| --- | --- |
| -XX:+/-HeapDumpOnOutOfMemoryError | 内存溢出时，自动 Dump 堆内存 |
| -XX:HeapDumpPath | 指定内存溢出时 Dump 文件的目录，配合上个命令使用， 没有指定则默认为启动 Java 程序的工作目录 |
| -XX:OnError | 发现致命错误执行的脚本 |
| -XX:OnOutOfMemoryError   | 抛出 OutOfMemoryError 错误时执行的脚本   |
| -XX：ErrorFile=filename   | 致命错误的日志文件名,绝对路径或者相对路径   |

### JavaAgent 参数
Agent 是 JVM 中的一项黑科技，可以通过无侵入方式来做很多事情，比如注入 AOP 代码，执行统 计等等，权限非常大。

| 参数 | 意义 |
| --- | --- |
| -agentlib:libname[=options]   | 启用 native 方式的 agent，参考 LD_LIBRARY_PATH 路径   |
| -agentpath:pathname[=options]   | 启用 native 方式的 agent   |
| -javaagent:jarpath[=options]   | 启用外部的 agent 库，比如 pinpoint.jar 等等   |
| -Xnoagent   | 禁用所有 agent   |

# JDK 命令行工具
### Jps 命令
#### 查询所有 Java 进程
```shell
ldeMacBook-Pro-3:geektime lx$ jps

389
52262 Jps
48728
51998 jar
```
#### 查询所有 Java 进程号
```shell
ldeMacBook-Pro-3:geektime lx$ jps -q

389
48728
52282
51998
```
#### 查询所有 Java 进程并输出主类或 Jar 路径
```shell
ldeMacBook-Pro-3:geektime lx$ jps -l

389
48728
52285 sun.tools.jps.Jps
51998 gateway-server-0.0.1-SNAPSHOT.jar
```
#### 查询所有 Java 进程虚拟机参数
```shell
ldeMacBook-Pro-3:geektime lx$ jps -v

389  -Xms128m -Xmx1024m -XX:ReservedCodeCacheSize=512m -XX:+UseG1GC -XX:SoftRefLRUPolicyMSPerMB=50 -XX:CICompilerCount=2 -XX:+HeapDumpOnOutOfMemoryError -XX:-OmitStackTraceInFastThrow -ea -Dsun.io.useCanonCaches=false -Djdk.http.auth.tunneling.disabledSchemes="" -Djdk.attach.allowAttachSelf=true -Djdk.module.illegalAccess.silent=true -Dkotlinx.coroutines.debug=off -XX:ErrorFile=/Users/lx/java_error_in_idea_%p.log -XX:HeapDumpPath=/Users/lx/java_error_in_idea.hprof -Djb.vmOptionsFile=/Users/lx/Library/Application Support/JetBrains/IntelliJIdea2021.1/idea.vmoptions -Didea.paths.selector=IntelliJIdea2021.1 -Didea.executable=idea -Didea.home.path=/Applications/IntelliJ IDEA.app/Contents -Didea.vendor.name=JetBrains
52310 Jps -Dapplication.home=/Library/Java/JavaVirtualMachines/jdk1.8.0_291.jdk/Contents/Home -Xms8m
48728  -Xms128m -Xmx1024m -XX:ReservedCodeCacheSize=512m -XX:+UseG1GC -XX:SoftRefLRUPolicyMSPerMB=50 -XX:CICompilerCount=2 -XX:+HeapDumpOnOutOfMemoryError -XX:-OmitStackTraceInFastThrow -ea -Dsun.io.useCanonCaches=false -Djdk.http.auth.tunneling.disabledSchemes="" -Djdk.attach.allowAttachSelf=true -Djdk.module.illegalAccess.silent=true -Dkotlinx.coroutines.debug=off -XX:ErrorFile=/Users/lx/java_error_in_datagrip_%p.log -XX:HeapDumpPath=/Users/lx/java_error_in_datagrip.hprof -Djb.vmOptionsFile=/Users/lx/Library/Application Support/JetBrains/DataGrip2021.1/datagrip.vmoptions -Didea.paths.selector=DataGrip2021.1 -Didea.executable=datagrip -Didea.platform.prefix=DataGrip -Didea.vendor.name=JetBrains -Didea.home.path=/Applications/DataGrip.app/Contents
51998 jar
```
### Jinfo 命令
#### 打印启动参数和系统属性
```shell
# jinfo pid

ldeMacBook-Pro-3:geektime lx$ jinfo 52524
Java System Properties:
#Fri Sep 24 17:37:42 CST 2021
java.runtime.name=Java(TM) SE Runtime Environment
java.protocol.handler.pkgs=org.springframework.boot.loader
sun.boot.library.path=/Library/Java/JavaVirtualMachines/jdk1.8.0_291.jdk/Contents/Home/jre/lib
java.vm.version=25.291-b10

·········

https.proxyPort=7890
socksNonProxyHosts=192.168.0.0/16|*.192.168.0.0/16|10.0.0.0/8|*.10.0.0.0/8|172.16.0.0/12|*.172.16.0.0/12|127.0.0.1|localhost|*.localhost|local|*.local|timestamp.apple.com|*.timestamp.apple.com
ftp.nonProxyHosts=192.168.0.0/16|*.192.168.0.0/16|10.0.0.0/8|*.10.0.0.0/8|172.16.0.0/12|*.172.16.0.0/12|127.0.0.1|localhost|*.localhost|local|*.local|timestamp.apple.com|*.timestamp.apple.com
sun.cpu.isalist=

VM Flags:
-XX:CICompilerCount=3 -XX:ConcGCThreads=1 -XX:G1HeapRegionSize=1048576 -XX:InitialHeapSize=134217728 -XX:MarkStackSize=4194304 -XX:MaxHeapSize=2147483648 -XX:MaxNewSize=1287651328 -XX:MinHeapDeltaBytes=1048576 -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseFastUnorderedTimeStamps -XX:+UseG1GC
```
#### 查询 Java 进程的所有启动参数
```shell
jinfo -flags pid

ldeMacBook-Pro-3:geektime lx$ jinfo -flags 52524
VM Flags:
-XX:CICompilerCount=3 -XX:ConcGCThreads=1 -XX:G1HeapRegionSize=1048576 -XX:InitialHeapSize=134217728 -XX:MarkStackSize=4194304 -XX:MaxHeapSize=2147483648 -XX:MaxNewSize=1287651328 -XX:MinHeapDeltaBytes=1048576 -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseFastUnorderedTimeStamps -XX:+UseG1GC
```
#### 查询 Java 进程的某个启动参数
```shell
jinfo -flag flag_name

ldeMacBook-Pro-3:geektime lx$ jinfo -flag UseG1GC 52524
-XX:+UseG1GC
```
#### 查询系统属性
```shell
jinfo -sysprops pid

ldeMacBook-Pro-3:geektime lx$ jinfo -sysprops 52524
Java System Properties:
#Fri Sep 24 17:37:42 CST 2021
java.runtime.name=Java(TM) SE Runtime Environment
java.protocol.handler.pkgs=org.springframework.boot.loader
sun.boot.library.path=/Library/Java/JavaVirtualMachines/jdk1.8.0_291.jdk/Contents/Home/jre/lib
java.vm.version=25.291-b10

·········

https.proxyPort=7890
socksNonProxyHosts=192.168.0.0/16|*.192.168.0.0/16|10.0.0.0/8|*.10.0.0.0/8|172.16.0.0/12|*.172.16.0.0/12|127.0.0.1|localhost|*.localhost|local|*.local|timestamp.apple.com|*.timestamp.apple.com
ftp.nonProxyHosts=192.168.0.0/16|*.192.168.0.0/16|10.0.0.0/8|*.10.0.0.0/8|172.16.0.0/12|*.172.16.0.0/12|127.0.0.1|localhost|*.localhost|local|*.local|timestamp.apple.com|*.timestamp.apple.com
sun.cpu.isalist=

VM Flags:
-XX:CICompilerCount=3 -XX:ConcGCThreads=1 -XX:G1HeapRegionSize=1048576 -XX:InitialHeapSize=134217728 -XX:MarkStackSize=4194304 -XX:MaxHeapSize=2147483648 -XX:MaxNewSize=1287651328 -XX:MinHeapDeltaBytes=1048576 -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseFastUnorderedTimeStamps -XX:+UseG1GC
```
### Jstat 命令
#### 查询 Jstat 参数选项
```shell
jstat -options

ldeMacBook-Pro-3:geektime lx$ jstat -options
-class        			类加载(class loader)统计
-compiler     			JIT 即时编译器相关的统计信息 
-gc									GC 相关的堆内存信息。
-gccapacity					各个内存池分代空间的容量 
-gccause						查看垃圾收集的统计情况（这个和-gcutil选项一样），如果有发生垃圾收集，它还会显示最后一次及当前正在发生垃圾收集的原因
-gcmetacapacity			元数据空间统计信息
-gcnew							年轻代的统计信息
-gcnewcapacity			年轻代空间大小统计
-gcold							老年代和元数据区的行为统计 
-gcoldcapacity			old 空间大小统计
-gcutil							GC 相关区域的使用率（utilization）统计 
-printcompilation		打印 JVM 编译统计信息
```
#### 基本参数使用
```shell
# -t： 可以在打印的列加上Timestamp列，用于显示系统运行的时间
# -h： 可以在周期性数据数据的时候，可以在指定输出多少行以后输出一次表头
# vmid： Virtual Machine ID（ 进程的 pid）
# interval： 执行每次的间隔时间，单位为毫秒
# count： 用于指定输出多少次记录，缺省则会一直打印
```
#### 查询类加载统计信息
```shell
ldeMacBook-Pro-3:geektime lx$ jstat -class -h 10 -t 52583 1s 10
Timestamp       Loaded  Bytes  Unloaded  Bytes     Time
         1767.6   6523 11826.0        0     0.0       4.22
         1768.7   6523 11826.0        0     0.0       4.22
         1769.7   6523 11826.0        0     0.0       4.22
```
| tiltle | 意义 |
| --- | --- |
| Timestamp | 打印时间 |
| Loaded | 加载class的数量 |
| Bytes | 所占用空间大小 |
| Unloaded | 未加载数量 |
| Bytes | 未加载占用空间 |
| Time | 时间 |



#### 查询JIT 即时编译器相关的统计信息
```shell
jstat -compiler -t -h 3 52583 1s 10

Timestamp       Compiled Failed Invalid   Time   FailedType FailedMethod
         2092.6     3324      2       0    10.01          1 java/net/URLClassLoader$1 run
         2093.6     3324      2       0    10.01          1 java/net/URLClassLoader$1 run
```
| title | 意义 |
| --- | --- |
| Timestamp | 打印时间 |
| Compiled | 编译数量 |
| Failed | 失败数量 |
| Invalid | 不可用数量 |
| Time | 时间 |
| FailedType | 失败类型 |
| FailedMethod | 失败的方法 |

#### 查询 GC 查询相关信息
```shell
# -h 10: 每 10 行打印一个 title
# -t: 打印时间
# 1s 20: 1s 打印一次，打印 20 条
jstat -gc -h 10 -t 52583 1s 20

Timestamp        S0C    S1C    S0U    S1U      EC       EU        OC         OU       MC     MU    CCSC   CCSU   YGC     YGCT    FGC    FGCT    CGC    CGCT     GCT
         1367.4 9216.0 12288.0 9185.9  0.0   207872.0 141854.7  54272.0    11170.2   32816.0 31352.9 4400.0 4020.9      8    0.087   1      0.036   -          -    0.123
         1368.5 9216.0 12288.0 9185.9  0.0   207872.0 141854.7  54272.0    11170.2   32816.0 31352.9 4400.0 4020.9      8    0.087   1      0.036   -          -    0.123
```
| title | 意义 |
| --- | --- |
| Timestamp | 打印时间 |
| S0C | 第一个幸存区的大小 |
| S1C | 第二个幸存区的大小 |
| S0U | 第一个幸存区的使用大小 |
| S1U | 第二个幸存区的使用大小 |
| EC | 伊甸园区的大小 |
| EU | 伊甸园区的使用大小 |
| OC | 老年代大小 |
| OU | 老年代使用大小 |
| MC | 方法区大小 |
| MU | 方法区使用大小 |
| CCSC | 压缩类空间大小 |
| CCSU | 压缩类空间使用大小 |
| YGC | 年轻代垃圾回收次数 |
| YGCT | 年轻代垃圾回收消耗时间 |
| FGC | 老年代垃圾回收次数 |
| FGCT | 老年代垃圾回收消耗时间 |
| CGC | 并发垃圾回收次数 |
| CGCT | 并发垃圾回收消耗时间 |
| GCT | 垃圾回收消耗总时间 |

#### 查询各个内存池分代空间的容量
```shell
jstat -gccapacity -h 10 52583 1s 10

Timestamp        NGCMN    NGCMX     NGC     S0C   S1C       EC      OGCMN      OGCMX       OGC         OC       MCMN     MCMX      MC     CCSMN    CCSMX     CCSC    YGC    FGC   CGC
       3697.8  43520.0 698880.0 232448.0 9216.0 12288.0 207872.0    87552.0  1398272.0    54272.0    54272.0      0.0 1077248.0  32816.0      0.0 1048576.0   4400.0      8     1     -
```
| title | 意义 |
| --- | --- |
| Timestamp | 打印时间 |
| NGCMN | 新生代最小容量 |
| NGCMX | 新生代最大容量 |
| NGC | 当前新生代容量 |
| S0C | 第一个幸存区大小 |
| S1C | 第二个幸存区大小 |
| EC | 伊甸园大小 |
| OGCMN | 老年代最小容量 |
| OGCMX | 老年代最大容量 |
| OGC | 当前老年代大小 |
| OC | 当前老年代大小 |
| MCMN | 最小元数据容量 |
| MCMX | 最大元数据容量 |
| MC | 当前元数据空间大小 |
| CCSMN | 最小压缩类空间大小 |
| CCSMX | 最大压缩类空间大小 |
| CCSC | 当前压缩类空间大小 |
| YGC | 年轻代 GC 次数 |
| FGC | 老年代 GC 次数 |
| CGC | 并发 GC 次数 |

#### 查询元数据空间统计信息
```shell
jstat -gcmetacapacity -t -h 10 52583 1s 10
Timestamp          MCMN       MCMX        MC       CCSMN      CCSMX       CCSC     YGC   FGC    FGCT    CGC    CGCT     GCT
         4732.2        0.0  1077248.0    32816.0        0.0  1048576.0     4400.0     8     1    0.036     -        -    0.123
```
| title | 意义 |
| --- | --- |
| Timestamp | 打印时间 |
| MCMN | 最小元数据容量 |
| MCMX | 最大元数据容量 |
| MC | 当前元数据空间大小 |
| CCSMN | 最小压缩类空间大小 |
| CCSMX | 最大压缩类空间大小 |
| CCSC | 当前压缩类空间大小 |
| YGC | 年轻代垃圾回收次数 |
| FGC | 老年代垃圾回收次数 |
| FGCT | 老年代垃圾回收消耗时间 |
| CGC | 并发垃圾回收次数 |
| CGCT | 并发垃圾回收时间 |
| GCT | 垃圾回收消耗总时间 |

#### 查询年轻代的统计信息
```shell
jstat -gcnew -h 10 -t 52583 1s 10
Timestamp        S0C    S1C    S0U    S1U   TT MTT  DSS      EC       EU     YGC     YGCT
         5062.4 9216.0 12288.0 9185.9    0.0  3  15 12288.0 207872.0 143933.4      8    0.087
```
| title | 意义 |
| --- | --- |
| Timestamp | 打印时间 |
| S0C | 第一个幸存区大小 |
| S1C | 第二个幸存区的大小 |
| S0U | 第一个幸存区的使用大小 |
| S1U | 第二个幸存区的使用大小 |
| TT | 对象在新生代存活的次数 |
| MTT | 对象在新生代存活的最大次数 |
| DSS | 期望的幸存区大小 |
| EC | 伊甸园区的大小 |
| EU | 伊甸园区的使用大小 |
| YGC | 年轻代垃圾回收次数 |
| YGCT | 年轻代垃圾回收消耗时间 |

#### 查询年轻代空间大小统计
```shell
jstat -gcnewcapacity -h 10 52583 1s 10
  NGCMN      NGCMX       NGC      S0CMX     S0C     S1CMX     S1C       ECMX        EC      YGC   FGC   CGC
   43520.0   698880.0   232448.0 232960.0   9216.0 232960.0  12288.0   697856.0   207872.0     8     1     -
```
| title | 意义 |
| --- | --- |
| Timestamp | 打印时间 |
| NGCMN | 新生代最小容量 |
| NGCMX | 新生代最大容量 |
| NGC | 新生代当前容量 |
| S0CMX | 第一个幸存区最大容量 |
| S0C | 第一个幸存区当前容量 |
| S1CMX | 第二个幸存区最大容量 |
| S1C | 第二个幸存区当前容量 |
| ECMX | 伊甸园区最大容量 |
| EC | 伊甸园区当前容量 |
| YGC | 年轻代 GC 次数 |
| FGC | 老年代 GC 次数 |
| CGC | 并发 GC 次数 |

#### 查询元数据区和老年代行为统计 
```shell
jstat -gcold -h 10 -t 53224 1s 10
Timestamp          MC       MU      CCSC     CCSU       OC          OU       YGC    FGC    FGCT    CGC    CGCT     GCT
          132.6  34480.0  33008.0   4528.0   4233.2     87424.0     17724.1     17     1    0.019     -        -    0.162
```
| title | 意义 |
| --- | --- |
| Timestamp | 打印时间 |
| MC | 元数据区大小 |
| MU | 元数据区使用大小 |
| CCSC | 压缩类空间大小 |
| CCSU | 压缩类空间使用大小 |
| OC | 老年代大小 |
| OU | 老年代使用大小 |
| YGC | 年轻代垃圾回收次数 |
| FGC | 老年代垃圾回收次数 |
| FGCT | 老年代垃圾回收消耗时间 |
| CGC | 并发垃圾回收次数 |
| CGCT | 并发垃圾回收消耗时间 |
| GCT | 垃圾回收消耗总时间 |

#### 查询老年代空间大小统计
```shell
jstat -gcoldcapacity -h 10 -t 53224 1s 10
Timestamp          OGCMN       OGCMX        OGC         OC       YGC   FGC    FGCT    CGC    CGCT     GCT
          774.4     87424.0   1398144.0     87424.0     87424.0    17     1    0.019     -        -    0.162
```
| title | 意义 |
| --- | --- |
| Timestamp | 打印时间 |
| OGCMN | 老年代最小容量 |
| OGCMX | 老年代最大容量 |
| OGC | 当前老年代带下 |
| OC | 老年代大小 |
| YGC | 新生代垃圾回收次数 |
| YGCT | 新生代垃圾回收消耗时间 |
| FGC | 老年代垃圾回收次数 |
| FGCT | 老年代垃圾回收消耗时间 |
| CGC | 并发垃圾回收次数 |
| CGCT | 并发垃圾回收消耗时间 |
| GCT | 垃圾回收消耗总时间 |

#### 查询GC 相关区域的使用率（utilization）统计 
```shell
ldeMacBook-Pro-3:geektime lx$ jstat -gcutil -t 53224 1s 10
Timestamp         S0     S1     E      O      M     CCS    YGC     YGCT    FGC    FGCT    CGC    CGCT     GCT
          970.1   0.00  51.83  77.33  20.27  95.73  93.49     17    0.143     1    0.019     -        -    0.162
```
| title | 意义 |
| --- | --- |
| Timestamp | 打印时间 |
| S0 | 幸存1区当前使用比例 |
| S1 | 幸存2区当前使用比例 |
| E | 伊甸园区使用比例 |
| O | 老年代使用比例 |
| M | 元数据区使用比例 |
| CCS | 压缩使用比例 |
| YGC | 年轻代垃圾回收次数 |
| YGCT | 年轻代垃圾回收消耗时间 |
| FGC | 老年代垃圾回收次数 |
| FGCT | 老年代垃圾回收消耗时间 |
| CGC | 并发垃圾回收次数 |
| CGCT | 并发垃圾回收消耗时间 |
| GCT | 垃圾回收消耗总时间 |

#### 打印 JVM 编译统计信息
```shell
ldeMacBook-Pro-3:geektime lx$ jstat -printcompilation -t 53224 1s 10
Timestamp       Compiled  Size  Type Method
         1137.4     3340      1    1 org/apache/catalina/valves/ValveBase backgroundProcess
```
| title | 意义 |
| --- | --- |
| Timestamp | 打印时间 |
| Compiled | 最近编译方法的数量 |
| Size | 最近编译方法的字节码数量 |
| Type | 最近编译方法的编译类型 |
| Method | 方法名标识 |

### Jmap 命令
#### 查询 Java 堆内存信息
```java
# JDK9 之前
[root@VM-0-14-centos ~]# jmap -heap 15515
    
# JDK9 及之后
[root@VM-0-14-centos ~]# jhsdb jmap --heap --pid 15515
    
# 关联进程 id
Attaching to process ID 15515, please wait...
Debugger attached successfully.
Server compiler detected.
JVM version is 25.302-b08

# 使用 TLAB
using thread-local object allocation.
# 标记-整理算法
Mark Sweep Compact GC

# 堆配置
Heap Configuration:
   # 空闲堆空间的最小百分比，FreeRatio < MinHeapFreeRatio：扩容
   MinHeapFreeRatio         = 40
   # 空闲堆空间的最大百分比，FreeRatio > MaxHeapFreeRatio：缩容
   MaxHeapFreeRatio         = 70
   # JVM 允许最大堆内存
   MaxHeapSize              = 482344960 (460.0MB)
   # JVM 新生代堆空间的默认值
   NewSize                  = 10485760 (10.0MB)
   # JVM 新生代堆空间允许的最大值
   MaxNewSize               = 160759808 (153.3125MB)
   # JVM 老年代堆空间的默认值
   OldSize                  = 20971520 (20.0MB)
   # 新生代堆空间:老年代堆空间 = 1:2
   NewRatio                 = 2
   # S0:S1:Eden = 1:1:8
   SurvivorRatio            = 8
   # JVM 元空间的默认值
   MetaspaceSize            = 21807104 (20.796875MB)
   # 压缩指针空间大小
   CompressedClassSpaceSize = 1073741824 (1024.0MB)
   # 最大元空间大小
   MaxMetaspaceSize         = 17592186044415 MB
   # G1 算法中每个 Region 的大小
   G1HeapRegionSize         = 0 (0.0MB)

# 堆内存使用情况
Heap Usage:
# 新生代：Eden + 1 个 Survivor 区
New Generation (Eden + 1 Survivor Space):
   # 总容量
   capacity = 15335424 (14.625MB)
   # 已使用空间
   used     = 10895072 (10.390350341796875MB)
   # 空闲空间
   free     = 4440352 (4.234649658203125MB)
   # 使用比例
   71.04513054220085% used
# Eden 区
Eden Space:
   # 总容量
   capacity = 13631488 (13.0MB)
   # 已使用空间
   used     = 10433984 (9.95062255859375MB)
   # 空闲空间
   free     = 3197504 (3.04937744140625MB)
   # 使用比例
   76.54325045072116% used
# From 区
From Space:
   # 总容量
   capacity = 1703936 (1.625MB)
   # 已使用空间
   used     = 461088 (0.439727783203125MB)
   # 空闲空间
   free     = 1242848 (1.185272216796875MB)
   # 使用比例
   27.06017127403846% used
To Space:
   # 总容量
   capacity = 1703936 (1.625MB)
   # 已使用空间
   used     = 0 (0.0MB)
   # 空闲空间
   free     = 1703936 (1.625MB)
   # 使用比例
   0.0% used
# 老年代
tenured generation:
   # 总容量
   capacity = 33996800 (32.421875MB)
   # 已使用空间
   used     = 20396280 (19.45140838623047MB)
   # 空闲空间
   free     = 13600520 (12.970466613769531MB)
   # 使用比例
   59.994705384036145% used

15122 interned Strings occupying 2057288 bytes.
```
#### 查询类的占用空间
```java
[root@VM-0-14-centos ~]# jmap -histo:live 15515 | more
[root@VM-0-14-centos ~]# jmap -histo 15515 | more

 num     #instances         #bytes  class name
----------------------------------------------
   1:         38717        6639736  [C
   2:         37675         904200  java.lang.String
   3:          7336         814320  java.lang.Class
   4:         20655         660960  java.util.concurrent.ConcurrentHashMap$Node
   5:          1490         527576  [B
   6:          7371         384456  [Ljava.lang.Object;
   7:          4262         375056  java.lang.reflect.Method
   8:          8478         271296  java.util.HashMap$Node
   9:         15596         249536  java.lang.Object
  10:          2809         247600  [I

```
#### Dump 堆内存
```java
jmap -dump:live,format=b,file=3826.hprof 15515
```
### Jstack 命令
#### 查询线程堆栈信息
```java
# -l：长列表模式，将线程相关的 locks 信息一起输出，比如持有的锁，等待的锁
# -F：强制执行 thread dump，可在 Java 进程卡死（hung 住）时使用，此选项可能需要系统权限
# -m：混合模式（mixed mode)，将 Java 帧和 native 帧一起输出，此选项可能需要系统权限
[root@VM-0-14-centos ~]# jstack -l 15515 | more
2021-09-25 18:14:32
Full thread dump OpenJDK 64-Bit Server VM (25.302-b08 mixed mode):

"Attach Listener" #31 daemon prio=9 os_prio=0 tid=0x00007f879c16f000 nid=0x59f0 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

   Locked ownable synchronizers:
	- None

"DestroyJavaVM" #30 prio=5 os_prio=0 tid=0x00007f87c404b800 nid=0x3c9c waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

   Locked ownable synchronizers:
	- None

"http-nio-8088-AsyncTimeout" #28 daemon prio=5 os_prio=0 tid=0x00007f87c41fd800 nid=0x3cce waiting on condition [0x00007f87a0eb0000]
   java.lang.Thread.State: TIMED_WAITING (sleeping)
	at java.lang.Thread.sleep(Native Method)
	at org.apache.coyote.AbstractProtocol$AsyncTimeout.run(AbstractProtocol.java:1143)
	at java.lang.Thread.run(Thread.java:748)

   Locked ownable synchronizers:
	- None

"http-nio-8088-Acceptor-0" #27 daemon prio=5 os_prio=0 tid=0x00007f87c41fc000 nid=0x3ccd runnable [0x00007f87a0fb1000]
   java.lang.Thread.State: RUNNABLE
	at sun.nio.ch.ServerSocketChannelImpl.accept0(Native Method)
	at sun.nio.ch.ServerSocketChannelImpl.accept(ServerSocketChannelImpl.java:421)
	at sun.nio.ch.ServerSocketChannelImpl.accept(ServerSocketChannelImpl.java:249)
	- locked <0x00000000eda1b978> (a java.lang.Object)
	at org.apache.tomcat.util.net.NioEndpoint$Acceptor.run(NioEndpoint.java:455)
	at java.lang.Thread.run(Thread.java:748)

   Locked ownable synchronizers:
	- None

"http-nio-8088-ClientPoller-0" #26 daemon prio=5 os_prio=0 tid=0x00007f87c4328800 nid=0x3ccc runnable [0x00007f87a10b2000]
   java.lang.Thread.State: RUNNABLE
	at sun.nio.ch.EPollArrayWrapper.epollWait(Native Method)
	at sun.nio.ch.EPollArrayWrapper.poll(EPollArrayWrapper.java:269)
	at sun.nio.ch.EPollSelectorImpl.doSelect(EPollSelectorImpl.java:93)
	at sun.nio.ch.SelectorImpl.lockAndDoSelect(SelectorImpl.java:86)
	- locked <0x00000000eda1bbb8> (a sun.nio.ch.Util$3)
	- locked <0x00000000eda1bba8> (a java.util.Collections$UnmodifiableSet)
	- locked <0x00000000eda1bb60> (a sun.nio.ch.EPollSelectorImpl)
	at sun.nio.ch.SelectorImpl.select(SelectorImpl.java:97)
	at org.apache.tomcat.util.net.NioEndpoint$Poller.run(NioEndpoint.java:798)
	at java.lang.Thread.run(Thread.java:748)

   Locked ownable synchronizers:
	- None

"http-nio-8088-exec-10" #25 daemon prio=5 os_prio=0 tid=0x00007f87c467f800 nid=0x3ccb waiting on condition [0x00007f87a11b3000]
   java.lang.Thread.State: WAITING (parking)
	at sun.misc.Unsafe.park(Native Method)

```
### Jcmd 命令
#### 查询支持参数
```java
[root@VM-0-14-centos ~]# jcmd 15515 help

15515:
The following commands are available:
VM.unlock_commercial_features
JFR.configure
JFR.stop
JFR.start
JFR.dump
JFR.check
VM.native_memory
ManagementAgent.stop
ManagementAgent.start_local
ManagementAgent.start
VM.classloader_stats
GC.rotate_log
Thread.print
GC.class_stats
GC.class_histogram
GC.heap_dump
GC.finalizer_info
GC.heap_info
GC.run_finalization
GC.run
VM.uptime
VM.dynlibs
VM.flags
VM.system_properties
VM.command_line
VM.version
help
```
#### 查询 JVM 版本
```java
[root@VM-0-14-centos ~]# jcmd 15515 VM.version

15515:
OpenJDK 64-Bit Server VM version 25.302-b08
JDK 8.0_302
```
#### 查询启动参数
```java
[root@VM-0-14-centos ~]# jcmd 15515 VM.flags

15515:
-XX:CICompilerCount=2 -XX:InitialHeapSize=31457280 -XX:MaxHeapSize=482344960 -XX:MaxNewSize=160759808 -XX:MinHeapDeltaBytes=196608 -XX:NewSize=10485760 -XX:OldSize=20971520 -XX:+UseCompressedClassPointers -XX:+UseCompressedOops 
```
#### 查询启动命令行
```java
[root@VM-0-14-centos ~]# jcmd 15515 VM.command_line

15515:
VM Arguments:
java_command: gateway-server-0.0.1-SNAPSHOT.jar
java_class_path (initial): gateway-server-0.0.1-SNAPSHOT.jar

```
#### 查询系统参数
```java
[root@VM-0-14-centos ~]# jcmd 15515 VM.system_properties
15515:
#Sat Sep 25 18:44:39 CST 2021
java.runtime.name=OpenJDK Runtime Environment
java.protocol.handler.pkgs=org.springframework.boot.loader
sun.boot.library.path=/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.302.b08-0.el7_9.x86_64/jre/lib/amd64
java.vm.version=25.302-b08
java.vm.vendor=Red Hat, Inc.

·······

catalina.base=/tmp/tomcat.9139479820806968547.8088
file.separator=/
java.vendor.url.bug=https\://bugzilla.redhat.com/enter_bug.cgi?product\=Red%20Hat%20Enterprise%20Linux%207&component\=java-1.8.0-openjdk
sun.io.unicode.encoding=UnicodeLittle
sun.cpu.endian=little
sun.cpu.isalist=

```
#### 查询线程堆栈信息
```java

[root@VM-0-14-centos ~]# jcmd 15515 Thread.print | more
15515:
2021-09-25 18:47:10
Full thread dump OpenJDK 64-Bit Server VM (25.302-b08 mixed mode):

"Attach Listener" #31 daemon prio=9 os_prio=0 tid=0x00007f879c16f000 nid=0x59f0 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"DestroyJavaVM" #30 prio=5 os_prio=0 tid=0x00007f87c404b800 nid=0x3c9c waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"http-nio-8088-AsyncTimeout" #28 daemon prio=5 os_prio=0 tid=0x00007f87c41fd800 nid=0x3cce waiting on condition [0x00007f87a0eb0000]
   java.lang.Thread.State: TIMED_WAITING (sleeping)
	at java.lang.Thread.sleep(Native Method)
	at org.apache.coyote.AbstractProtocol$AsyncTimeout.run(AbstractProtocol.java:1143)
	at java.lang.Thread.run(Thread.java:748)

"http-nio-8088-Acceptor-0" #27 daemon prio=5 os_prio=0 tid=0x00007f87c41fc000 nid=0x3ccd runnable [0x00007f87a0fb1000]
   java.lang.Thread.State: RUNNABLE
	at sun.nio.ch.ServerSocketChannelImpl.accept0(Native Method)
	at sun.nio.ch.ServerSocketChannelImpl.accept(ServerSocketChannelImpl.java:421)
	at sun.nio.ch.ServerSocketChannelImpl.accept(ServerSocketChannelImpl.java:249)
	- locked <0x00000000eda1b978> (a java.lang.Object)
	at org.apache.tomcat.util.net.NioEndpoint$Acceptor.run(NioEndpoint.java:455)
	at java.lang.Thread.run(Thread.java:748)

"http-nio-8088-ClientPoller-0" #26 daemon prio=5 os_prio=0 tid=0x00007f87c4328800 nid=0x3ccc runnable [0x00007f87a10b2000]
   java.lang.Thread.State: RUNNABLE
	at sun.nio.ch.EPollArrayWrapper.epollWait(Native Method)
	at sun.nio.ch.EPollArrayWrapper.poll(EPollArrayWrapper.java:269)
	at sun.nio.ch.EPollSelectorImpl.doSelect(EPollSelectorImpl.java:93)
	at sun.nio.ch.SelectorImpl.lockAndDoSelect(SelectorImpl.java:86)
	- locked <0x00000000eda1bbb8> (a sun.nio.ch.Util$3)
	- locked <0x00000000eda1bba8> (a java.util.Collections$UnmodifiableSet)
	- locked <0x00000000eda1bb60> (a sun.nio.ch.EPollSelectorImpl)
	at sun.nio.ch.SelectorImpl.select(SelectorImpl.java:97)
	at org.apache.tomcat.util.net.NioEndpoint$Poller.run(NioEndpoint.java:798)
	at java.lang.Thread.run(Thread.java:748)

"http-nio-8088-exec-10" #25 daemon prio=5 os_prio=0 tid=0x00007f87c467f800 nid=0x3ccb waiting on condition [0x00007f87a11b3000]
   java.lang.Thread.State: WAITING (parking)
	at sun.misc.Unsafe.park(Native Method)
	- parking to wait for  <0x00000000eda1bd90> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
	at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)
	at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:103)
	at org.apache.tomcat.util.threads.TaskQueue.take(TaskQueue.java:31)
	at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1074)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1134)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
	at java.lang.Thread.run(Thread.java:748)

```
#### 查询系统中类统计信息
```java
[root@VM-0-14-centos ~]# jcmd 15515 GC.class_histogram | more
15515:

 num     #instances         #bytes  class name
----------------------------------------------
   1:         38735        6641960  [C
   2:         37692         904608  java.lang.String
   3:          7340         814744  java.lang.Class
   4:         20660         661120  java.util.concurrent.ConcurrentHashMap$Node
   5:          1491         527632  [B
   6:          7383         385440  [Ljava.lang.Object;
   7:          4262         375056  java.lang.reflect.Method
   8:          8478         271296  java.util.HashMap$Node
   9:         15605         249680  java.lang.Object
  10:          2817         248368  [I
  11:          5994         239760  java.util.LinkedHashMap$Entry
  12:          2962         236568  [Ljava.util.HashMap$Node;
  13:           116         191040  [Ljava.util.concurrent.ConcurrentHashMap$Node;
  14:          2782         155792  java.util.LinkedHashMap
  15:          2185          87400  java.lang.ref.SoftReference
  16:          1679          84400  [Ljava.lang.String;
  17:          3518          77560  [Ljava.lang.Class;
  18:          2925          70200  java.util.ArrayList
```
#### 查看堆内存信息
```java
[root@VM-0-14-centos ~]# jcmd 15515 GC.heap_info
15515:
 def new generation   total 14976K, used 117K [0x00000000e3400000, 0x00000000e4440000, 0x00000000ecd50000)
  eden space 13312K,   0% used [0x00000000e3400000, 0x00000000e341d458, 0x00000000e4100000)
  from space 1664K,   0% used [0x00000000e42a0000, 0x00000000e42a0000, 0x00000000e4440000)
  to   space 1664K,   0% used [0x00000000e4100000, 0x00000000e4100000, 0x00000000e42a0000)
 tenured generation   total 33200K, used 14133K [0x00000000ecd50000, 0x00000000eedbc000, 0x0000000100000000)
   the space 33200K,  42% used [0x00000000ecd50000, 0x00000000edb1d400, 0x00000000edb1d400, 0x00000000eedbc000)
 Metaspace       used 36219K, capacity 37700K, committed 38144K, reserved 1083392K
  class space    used 4527K, capacity 4784K, committed 4864K, reserved 1048576K

```
#### dump 堆内存信息
```java
[root@VM-0-14-centos ~]# jcmd 15515 GC.heap_dump ./aaa

15515:
Heap dump file created

```
### Jjs 命令
#### Console 命令行 
```java
[root@VM-0-14-centos ~]# jjs
jjs> a = 1
1
jjs> a = a+1
2
jjs> a = 3
3
```
### Jrunscript 命令
#### 当 curl 命令用
```java
[root@VM-0-14-centos ~]# jrunscript -e "cat('http://www.baidu.com')"
<!DOCTYPE html>
<!--STATUS OK--><html> <head><meta http-equiv=content-type content=text/html;charset=utf-8><meta http-equiv=X-UA-Compatible content=IE=Edge><meta content=always name=referrer><link rel=stylesheet type=text/css href=http://s1.bdstatic.com/r/www/cache/bdorz/baidu.min.css><title>百度一下，你就知道</title></head> <body link=#0000cc> <div id=wrapper> <div id=head> <div class=head_wrapper> <div class=s_form> <div class=s_form_wrapper> <div id=lg> <img hidefocus=true src=//www.baidu.com/img/bd_logo1.png width=270 height=129> </div> <form id=form name=f action=//www.baidu.com/s class=fm> <input type=hidden name=bdorz_come value=1> <input type=hidden name=ie value=utf-8> <input type=hidden name=f value=8> <input type=hidden name=rsv_bp value=1> <input type=hidden name=rsv_idx value=1> <input type=hidden name=tn value=baidu><span class="bg s_ipt_wr"><input id=kw name=wd class=s_ipt value maxlength=255 autocomplete=off autofocus></span><span class="bg s_btn_wr"><input type=submit id=su value=百度一下 class="bg s_btn"></span> </form> </div> </div> <div id=u1> <a href=http://news.baidu.com name=tj_trnews class=mnav>新闻</a> <a href=http://www.hao123.com name=tj_trhao123 class=mnav>hao123</a> <a href=http://map.baidu.com name=tj_trmap class=mnav>地图</a> <a href=http://v.baidu.com name=tj_trvideo class=mnav>视频</a> <a href=http://tieba.baidu.com name=tj_trtieba class=mnav>贴吧</a> <noscript> <a href=http://www.baidu.com/bdorz/login.gif?login&amp;tpl=mn&amp;u=http%3A%2F%2Fwww.baidu.com%2f%3fbdorz_come%3d1 name=tj_login class=lb>登录</a> </noscript> <script>document.write('<a href="http://www.baidu.com/bdorz/login.gif?login&tpl=mn&u='+ encodeURIComponent(window.location.href+ (window.location.search === "" ? "?" : "&")+ "bdorz_come=1")+ '" name="tj_login" class="lb">登录</a>');</script> <a href=//www.baidu.com/more/ name=tj_briicon class=bri style="display: block;">更多产品</a> </div> </div> </div> <div id=ftCon> <div id=ftConw> <p id=lh> <a href=http://home.baidu.com>关于百度</a> <a href=http://ir.baidu.com>About Baidu</a> </p> <p id=cp>&copy;2017&nbsp;Baidu&nbsp;<a href=http://www.baidu.com/duty/>使用百度前必读</a>&nbsp; <a href=http://jianyi.baidu.com/ class=cp-feedback>意见反馈</a>&nbsp;京ICP证030173号&nbsp; <img src=//www.baidu.com/img/gs.gif> </p> </div> </div> </div> </body> </html>

```
#### 执行 js 脚本片段
```java
jrunscript -e "print('hello,kk.jvm'+1)"
```
#### 执行 Js 文件
```java
jrunscript -l js -f /XXX/XXX/test.js
```
```java
# 当 curl 命令用：


jrunscript -e "print('hello,kk.jvm'+1)"
执行 js 文件
jrunscript -l js -f /XXX/XXX/test.js
```
## JVM 图形化工具
### Jconsole
### Jvisualvm
### VisualGC（IDEA）
### JMC


## JVM 调优
### 选择合适的垃圾回收器
| 垃圾回收器 | 场景 |
| --- | --- |
| Serial + Serial Old（-XX:+UseSerialGC）   | 单核 CPU |
| ParNew +CMS + Serial Old（-XX:+UseConcMarkSweepGC）   | 多核 CPU，低延迟 |
| Parallel Scavenge + Parallel Old（-XX:+UseParalleOldGC）  | 多核 CPU，高吞吐量 |
| G1（-XX:UseG1）   | 多核 CPU，低延迟 |

### 常见的技巧
#### 新生代回收频繁


1. 增大内存：由于内存太小，导致没有空间创建新的对象，会导致 GC；
1. 调整内存比例，默认比例为 1:1:8

​

#### 老年代回收频繁


1. 增大内存：由于内存太小，导致没有空间创建新的对象，会导致 GC；
1. 调整内存比例，默认比例为 1:1:8
1. 调整晋升的年龄，若晋升年龄小会导致大量的对象进入老年代
1. 调整直接晋升老年代对象的大小，若大量大对象进入老年代，会导致 GC 频繁

​

#### 减少程序停顿时间

1. 切换垃圾回收器，例如将 Parallel 垃圾回收器，改为 CMS
# 垃圾回收器总结
![垃圾回收器 (1).jpg](https://cdn.nlark.com/yuque/0/2021/jpeg/21680022/1632669160127-46a1d257-4085-4af7-b1eb-f298016cbc86.jpeg#clientId=u9ac633f5-68af-4&from=drop&id=u8fd2f8b9&margin=%5Bobject%20Object%5D&name=%E5%9E%83%E5%9C%BE%E5%9B%9E%E6%94%B6%E5%99%A8%20%281%29.jpg&originHeight=2740&originWidth=6374&originalType=binary&ratio=1&size=2270908&status=done&style=none&taskId=ubbb509a5-bfac-4c1f-993f-511ad98dc97)














