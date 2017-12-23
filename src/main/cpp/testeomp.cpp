// Modified for Academic Purpose only by rsdomi on 08/11/2017.
//# include <cstdlib>
# include <cmath>
# include <omp.h>
# include <jni.h>


//****************************************************************************80
double r8_uniform_01 ( int *seed );
//****************************************************************************80
double g (double x);
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
//****************************************************************************80
//
double g ( double x )
{
    double tol = 0.0001;
    double a = 1.0;
    double b = 2.0;
    double fa = 1.0;
    double fb = 2.0;
    double f = fa;

    if ( fabs(x - a) <= tol ){
            f=fa;
                    }
    if ( fabs(x - b) <= tol){
            f=fb;
                    }
    if ((fabs(x - a) >= tol)&&(fabs(x - b) >= tol)){
            f=0;
                    }
    return f;
}
// Created by rsdomi on 29/11/2017.
//

extern "C"
JNIEXPORT jdouble JNICALL
Java_com_example_rsdomi_trythreads_MainActivity_MCOMPFromJNI(JNIEnv      *env,
                                                             jobject instance) {

    double a = 2.0;
    const int n = 1000000;
    int i;
    int seed = 239956;

    jdouble results;

    omp_set_dynamic(0);     // Explicitly disable dynamic team
    omp_set_num_threads(8); // Use 8 threads for all consecutive parallel regions

    # pragma omp parallel for \
      private (i, seed)       \
      reduction(+:a)
        for (i = 1; i <= n; i++)
            a += r8_uniform_01(&seed);

    results = a / (double) n ;

    return results;

}//