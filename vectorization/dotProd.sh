hostname
project="${HOME}/workspace/vectorization"
build="${project}/bin"
classpath="${build}:/home/nhalli/yeppp-1.0.0/binaries/java-1.5/yeppp.jar"
JDK_BIN="/home/nhalli/openjdk8/build/linux-x86_64-normal-server-release/images/j2re-image/bin"

JAVAC="$JDK_BIN/javac"
JAVA="$JDK_BIN/java"

 #-XX:CompileCommand=print,test::dotproductU1s 

opts="-cp $classpath -Dargs.N=1283074 -Dargs.IT=100 "
opts+="-XX:+UnlockDiagnosticVMOptions -XX:-UseOnStackReplacement -XX:-PrintCompilation -XX:-PrintInlining -Djava.library.path=/home/nhalli/yeppp-1.0.0/binaries/linux/x86_64 " 
opts+=" -XX:+UseSuperWord -XX:+PrintCommandLineFlags -XX:UseAVX=99 -XX:UseSSE=99 -XX:-TieredCompilation"
classmain="dotprod"

for type in 4 32 128 512 2048 8192 32768
do
$JAVA $opts -Dargs.length=$type $classmain
done
