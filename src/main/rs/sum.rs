// Needed directive for RS to work
#pragma version(1)

// Change java_package_name directive to match your Activity's package path
#pragma rs java_package_name(com.example.rsdomi.trythreads)

// This kernel function will just sum 2 to every input element
// * in -> Current Allocation element
// * x  -> Current element index
int __attribute__((kernel)) sum2(int in, uint32_t x) {
     uint sum = 0;
     uint count = 0;

    // Performs the sum and returns the new value
    for (int i =1; i <= 1000000; i++) {
          sum += rsRand(0,9);
          }
      in += sum/1000000.0;

    return in;
}