hostname
uname -sr
project="${HOME}/git/vectorization/vectorization"
build="${project}/build"
JDK8="${HOME}/openjdk8/build/linux-x86_64-normal-server-release/images/j2sdk-image"
classpath="${build}:/home/nhalli/yeppp-1.0.0/binaries/java-1.5/yeppp.jar"
JDK_BIN="${JDK8}/bin"

JAVAC="$JDK_BIN/javac"
JAVA="$JDK_BIN/java"
JAVAH="$JDK_BIN/javah"


if [ "$1" = "-h" ]
then
$JAVAH -verbose -d $project/src/polynomialComputation/jni -jni -cp $project/bin polynomialComputation.polyEval
exit
fi

if [ "$1" = "-c" ]
then
javaFile="${project}/src/polynomialComputation/polyEval.java"

if [ "$2" = "clean" ]
then
rm -rf build
mkdir build
fi

$JAVAC -d "${project}/build" -sourcepath "${project}/src" -classpath $classpath $javaFile

exit
fi

if [ "$1" = "-so" ]
then
gcc -O3 -m64 -mavx -Wall -std=c99 -ftree-vectorize -ftree-vectorizer-verbose=6 -o ${project}/dlib/libPolyEval.so -lc -shared -fPIC -I${JDK8}/include -I${JDK8}/include/linux $project/src/polynomialComputation/jni/polyEval.c
exit
fi

opts="-cp $classpath -Dargs.Nb2=10 -Dargs.D=64 -Dargs.IT=10 -Dargs.WU=10"
opts+=" -Xmx500m "
opts+=" -Djava.library.path=/home/nhalli/yeppp-1.0.0/binaries/linux/x86_64:${project}/dlib"
opts+=" -XX:ObjectAlignmentInBytes=64 -Dargs.alignment=6 "
opts+=" -server -XX:+UnlockDiagnosticVMOptions -XX:-TieredCompilation -XX:-BackgroundCompilation -XX:CICompilerCount=1 -XX:CompileThreshold=1000 -XX:+UseOnStackReplacement" 
opts+=" -XX:-PrintCompilation -XX:-PrintAssembly -XX:-PrintIntrinsics -XX:-PrintInlining -XX:-PrintCommandLineFlags -XX:-PrintTieredEvents" 
opts+=" -XX:+UseSuperWord"
opts+=" -XX:CompileCommandFile=compiler_directives"
#-XX:OptoLoopAlignment=16 -XX:-UseUnalignedLoadAndStores -XX:+AlignedVector
classmain="polynomialComputation.polyEval"

$JAVA -version
taskset -c 0 $JAVA $opts $classmain


