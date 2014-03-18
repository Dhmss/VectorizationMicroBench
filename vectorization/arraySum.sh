hostname
uname -sr
project="${HOME}/workspace/vectorization"
build="${project}/bin"
JDK8="/home/nhalli/openjdk8/build/linux-x86_64-normal-server-release/images/j2sdk-image"
classpath="${build}:/home/nhalli/yeppp-1.0.0/binaries/java-1.5/yeppp.jar"
JDK_BIN="${JDK8}/bin"

JAVAC="$JDK_BIN/javac"
JAVA="$JDK_BIN/java"
JAVAH="$JDK_BIN/javah"

if [ "$1" = "-h" ]
then
$JAVAH -verbose -d $project/src/arraySum/jni -jni -cp $project/bin arraySum.arraySum
exit
fi

if [ "$1" = "-so" ]
then
gcc -std=c99 -o $project/dlib/libArraySum.so -lc -shared -fPIC -mavx -I${JDK8}/include -I${JDK8}/include/linux $project/src/arraySum/jni/arraySum.c
exit
fi

opts="-cp $classpath -Dargs.N=10 -Dargs.IT=30000 -Dargs.packet=10 -Dargs.VERBOSE=false -Dargs.nanos=true "
opts+="-Xmx2g "
opts+=" -Djava.library.path=/home/nhalli/yeppp-1.0.0/binaries/linux/x86_64:/home/nhalli/workspace/vectorization/dlib"
opts+=" -server -XX:+UnlockDiagnosticVMOptions -XX:-TieredCompilation" 
opts+=" -XX:-BackgroundCompilation -XX:CICompilerCount=1 -XX:CompileThreshold=1000 -XX:-PrintCompilation -XX:-UseOnStackReplacement -XX:-PrintAssembly" 

opts+=" -XX:+UseSuperWord -XX:-PrintInlining"

classmain="arraySum.arraySum"

#collect -o yeppp.er -p high -j on -S on -A on $JAVA $opts -Dargs.TYPE=SUM_JNI_NAIVE $classmain 

#$JAVA -version
$JAVA $opts -Dargs.TYPE=SUM_NAIVE -XX:CompileCommand=print,vectorSum::sum $classmain
#$JAVA $opts -Dargs.TYPE=SUM_UNROLL4 $classmain
#$JAVA $opts -Dargs.TYPE=SUM_UNROLL8 $classmain
$JAVA $opts -Dargs.TYPE=SUM_JNI_NAIVE $classmain
#$JAVA $opts -Dargs.TYPE=SUM_JNI_CPY $classmain
#$JAVA $opts -Dargs.TYPE=SUM_JNI_AVX $classmain
#$JAVA $opts -Dargs.TYPE=SUM_VECTORIZED $classmain
#$JAVA $opts -Dargs.TYPE=SUM_SAMEOFFSET $classmain
#$JAVA $opts -Dargs.TYPE=SUM_UNSAFE_SAMEOFFSET $classmain
#$JAVA $opts -Dargs.TYPE=SUM_UNSAFE_NAIVE $classmain
#collect $JAVA $opts -Dargs.TYPE=SUM_JNI_YEPPP $classmain
#$JAVA $opts -Dargs.NATIVE=true -Dargs.TYPE=SUM_NAT_NAIVE $classmain
#$JAVA $opts -Dargs.NATIVE=true -Dargs.TYPE=SUM_NAT_JNI_NAIVE $classmain
#$JAVA $opts -Dargs.NATIVE=true -Dargs.TYPE=SUM_NAT_JNI_AVX $classmain
