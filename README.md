# TryThreads
This app has an intent to benchmarking different kinds of threads for running millions of rand function calls to mimic monte carlo simulation. Here you can find six different types of time calculations using "System.currentTimeMillis()" function before and after calling each thread. Of course, this calculation are not precise, because the virtual machine can put threads on sleep or sth else, the right thing should be doing was calculating the cpu time for each. But this project is still on going. The six type threads are:

Single Thread on "run method" calling Java Random Function 1 million times.
Single Async Task on "doInBackground method" caling Java Random Function 1 million times.
10 Multi-Thread on "run method" calling each 100,000 times the rand function.
10 Multi-Callable on "executor.invokeAll method" 100,000 times the rand function each.
Renderscript functionality to test the CPU/GPU capability on running 1 million times rsRand function
Native code using JNI/C++ scheme compiling one code doing 1 million times on built-in rand function calculations.
Native code using JNI/C++/OPENMP scheme to speedup the calculations and measure performance.

Attached to the this, there is two additional options related to Feynman-Kac Formula to solve PDE, which is the final intent of this project. The first, solves to the 1D Laplace Problem for 1 point in the middle of the interval [1,2]. the Second option, calculates doInBackground 9 points inside the same interval calling native C++ code based on the idea of prof. John Burkdart (FSU). Also, the results was plotted for evaluation and verification. A lot more must be done, but so far I would like to thanks prof. John Burkdart, Graphview Team, and Opencsv Team.
