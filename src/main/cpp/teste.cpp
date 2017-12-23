// Modified for Academic Purpose only by rsdomi on 08/11/2017.
//# include <cstdlib>
//# include <cmath>
# include <jni.h>


//****************************************************************************80
double r8_uniform_01 ( int *seed );
//****************************************************************************80

//****************************************************************************80
double r8_uniform_01 ( int *seed )

//****************************************************************************80
//
{
    int i4_huge = 2147483647;
    int k;
    double r;

    k = *seed / 127773;

    *seed = 16807 * ( *seed - k * 127773 ) - k * 2836;

    if ( *seed < 0 )
    {
        *seed = *seed + i4_huge;
    }
//
//
    r = ( double ) ( *seed ) * 4.656612875E-10;

    return r;
}


extern "C"
JNIEXPORT jdouble JNICALL
Java_com_example_rsdomi_trythreads_MainActivity_MCFromJNI(JNIEnv *env, jobject instance) {

    double a = 2.0;
    int n = 1000000;
    int seed = 239956;

    jdouble results;

    for ( int i = 1; i <= n; i++ )
    {

        a+= r8_uniform_01 ( &seed );

    }

    results = a / (double) n ;

    return results;

}