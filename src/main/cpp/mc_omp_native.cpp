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
Java_com_example_rsdomi_trythreads_MainActivity_MCOMPFromFK1D(     JNIEnv *env,
                                                                   jobject instance,
                                                                   jdouble    x) {
    const double h = 0.0001;
    const int nr_mc_iter = 20000;
    //double u[nr_mc_iter];
    //double t;
    //double time_ave;
    double rth;
    double x_til;
    double x_a;
    double x_b;
    double x_m;
    double deltax;
    double dx;
    double w;
    double us;
    double chk;

    int i;
    int seed = 98462323;

    //return fx value @x from any point in [a,b]
    jdouble fx;

    x_a = 1.0;
    x_b = 2.0;
    w = 0;
    rth = sqrt (h);
    x_m = 0.5*(x_a+x_b);
    deltax = 0.5*fabs(x_b-x_a);

    omp_set_dynamic(0);     // Explicitly disable dynamic team
    omp_set_num_threads(8); // Use 4 threads for all consecutive parallel regions

# pragma omp parallel for \
      private (i, us, dx, chk, x_til)    \
      reduction(+:w)
    for (i = 1; i <= nr_mc_iter; i++)
    {
        //t = 0;

        x_til = x;

        chk = 0.0;

        while (chk < 1.0 ){

            us = r8_uniform_01 ( &seed ) - 0.5;

            if ( us < 0.0 ) { dx = - rth; }
            else            { dx = + rth; }

            x_til = x_til + dx;

            //t = t + h;

            chk = pow( (x_til-x_m)/deltax, 2);
        }

        w = w + g(x_til);

    }

    fx = w / (double) (nr_mc_iter);
    //time_ave = (double) t / (double) (nr_mc_iter);
    return fx;

}
