package com.example.rsdomi.trythreads;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.RadioGroup;
import android.renderscript.*;
import android.widget.Toast;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.lang.String;
import static java.util.Arrays.asList;

public class MainActivity extends AppCompatActivity {

    private RadioGroup rThreadGroup;
    private RadioButton radioButton;
    private View ThreadTypeButton;
    private Button btn_run;
    private TextView tv;
    private ProgressDialog pbar;

    static {
        System.loadLibrary("teste");
    }

    static {
        System.loadLibrary("testeomp");
    }

    static {
        System.loadLibrary("mc_omp_native");
    }

    public class MyThread extends Thread
    {
        private int nStart, nEnd;
        private int[] Results;
        private int SIZE_RESULT;
        Random rand = new Random();

        private MyThread( int nStart, int nEnd )
            {
                this.nStart = nStart;
                this.nEnd = nEnd;
                SIZE_RESULT = nEnd - nStart + 1;
                Results = new int[SIZE_RESULT];
                }

        @Override
        public void run()
            {
                for( int i=0; i<SIZE_RESULT-1; i++){
                    Results[i] = 1 + rand.nextInt(6);}
    //            return( 0L );
                }

    }

    protected void DoThreadCalculation(){

        try{
            //
            tv = findViewById(R.id.textView1);

            long lStart = System.currentTimeMillis();
//
            MyThread Thread1 = new MyThread(0, 1000000);

            Thread1.start();
            //
            Thread1.join();
//
            long lElapsed = System.currentTimeMillis() - lStart;
//
            tv.setText("Time was "+ lElapsed + " ms");
        }
        catch(Exception ex)
        {
//
        }
    }

    protected void DoThreadParallelCalculation()
    {
        try
        {
            MyThread[] Threads = new MyThread[10];
//
            tv = findViewById(R.id.textView1);
            long lStart = System.currentTimeMillis();
//
            for (int i = 0; i < Threads.length; i++) {
                Threads[i] = new MyThread(i*100000,(i+1)*100000);
                Threads[i].start();
            }
//
            for (MyThread Thread : Threads) {
                Thread.join();
            }
//
            long lElapsed = System.currentTimeMillis() - lStart;

            tv.setText("Time was "+ lElapsed + " ms");
        }
        catch(Exception ex)
        {
//
        }
    }

    private class Calc implements Callable<Long>
    {
        int nStart, nEnd;
        Random rand = new Random();
        private int[] Results;
        private int SIZE_RESULT;

        private Calc( int nStart, int nEnd )
        {
            this.nStart = nStart;
            this.nEnd = nEnd;
            SIZE_RESULT = nEnd - nStart + 1;
            Results = new int[SIZE_RESULT];

        }
//
        @Override
        public Long call()
        {
            for( int i=0; i<SIZE_RESULT-1; i++){
                Results[i] = 1 + rand.nextInt(6);}

            return( 0L );
        }

    }

    protected void DoCallableParallelCalculations()
    {
        try
        {
            tv = findViewById(R.id.textView1);
            long lStart = System.currentTimeMillis();

            ExecutorService executor = Executors.newFixedThreadPool(10);
            List<Future<Long>> results = executor.invokeAll(
                    asList(
                            new Calc(0     ,100000), new Calc(100000,200000),
                            new Calc(200000,300000), new Calc(300000,400000),
                            new Calc(400000,500000), new Calc(500000,600000),
                            new Calc(600000,700000), new Calc(700000,800000),
                            new Calc(800000,900000), new Calc(900000,1000000)
                    ));

            long lElapsed = System.currentTimeMillis() - lStart;

            tv.setText("Time was "+ lElapsed + " ms");
        }
        catch(Exception ex)
        {
//
        }
    }

    private static class MyTaskParams{
        int nStart;
        int nEnd;

        MyTaskParams(int nStart, int nEnd){
             this.nStart = nStart;
             this.nEnd   = nEnd;
        }
    }

    protected class MCTask extends AsyncTask<MyTaskParams, Long, String> {

        Random rand = new Random();
        Long lStart, lEnd;

        @Override
        protected void onPreExecute() {

            lStart = System.currentTimeMillis();
        }

        @Override
        protected String doInBackground(MyTaskParams... params) {

            int nStart = params[0].nStart;
            int nEnd   = params[0].nEnd;
            int SIZE_RESULT = nEnd - nStart + 1;
            int[] Results = new int[SIZE_RESULT];

            for(int i=0; i<SIZE_RESULT-1; i++) {
                Results[i] = 1 + rand.nextInt(6);
            }

            return("");
        }

        @Override
        protected void onPostExecute(String s) {

            lEnd = System.currentTimeMillis();
//
            tv = findViewById(R.id.textView1);

            long tempTime = lEnd - lStart;
//
            tv.setText("Time was "+ tempTime + " ms");

        }
    }

    protected void DoAsyncTaskCalculations(){

        try
        {
//
            MCTask runner = new MCTask();
//
            MyTaskParams params = new MyTaskParams(0, 1000000);
//
            runner.execute(params);
//
        }
        catch(Exception ex)
        {
//
        }
    }

    private void DoSingleonRenderScript() {
        // initialize time count
        long lStart = System.currentTimeMillis();

        // Instantiates the RenderScript context
        RenderScript mRS = RenderScript.create(this);

        // Creates an input array, containing some numbers
        int inputArray[] = new int[]{0};

        // Instantiates the input Allocation, which will contain our sample numbers
        Allocation inputAllocation = Allocation.createSized(mRS, Element.I32(mRS), inputArray.length);

        // Copies the input array into the input Allocation
        inputAllocation.copyFrom(inputArray);

        // Instantiates the output Allocation, which will contain the result of the process
        Allocation outputAllocation = Allocation.createSized(mRS, Element.I32(mRS), inputArray.length);

        // Instantiates the sum script
        ScriptC_sum myScript = new ScriptC_sum(mRS);

        // Run the sum process, taking elements that are inside inputAllocation and
        // placing the process results inside the outputAllocation
        myScript.forEach_sum2(inputAllocation, outputAllocation);

        // Copies the result of the process from the outputAllocation to
        // a simple int array
        int outputArray[] = new int[inputArray.length];
        outputAllocation.copyTo(outputArray);

        // time to write out the result from RS
        long lElapsed = System.currentTimeMillis() - lStart;

        tv = findViewById(R.id.textView1);
        tv.setText("Time was " + lElapsed + "ms");

    }

    private void DoSingleJNI(){

        long lStart = System.currentTimeMillis();

        Thread JNIThread = new Thread("New Thread") {
            double resultado;
            public void run(){
                try{
                    resultado = MCFromJNI();
                }
                catch(Exception e){

                }
            }
        };

        try {
            JNIThread.start();
        //
            JNIThread.join();
        //
        } catch (InterruptedException e) {
                    e.printStackTrace();
        }

        long lElapsed = System.currentTimeMillis() - lStart;

        tv = findViewById(R.id.textView1);
        tv.setText("Time was "+ lElapsed + "ms");

    }

    private void DoOMPJNI(){

        long lStart = System.currentTimeMillis();

        Thread JNIThread = new Thread("New Thread") {
            double res;
            public void run(){
                try{
                    res = MCOMPFromJNI();
                }
                catch(Exception e){

                }
            }
        };

        try {
            JNIThread.start();
            //
            JNIThread.join();
            //
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long lElapsed = System.currentTimeMillis() - lStart;

        TextView tv = (TextView) findViewById(R.id.textView1);
        tv.setText("Time was "+ lElapsed + "ms");

    }

    private void DoFK1DJNI(){

        long lStart = System.currentTimeMillis();
        final double[] sol = {1.0};

        Thread JNIThread = new Thread("New Thread") {

            public void run(){
                try{
                    sol[0] = MCOMPFromFK1D(1.5);
                }
                catch(Exception e){

                }
            }
        };

        try {
            JNIThread.start();
            //
            JNIThread.join();
            //
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long lElapsed = System.currentTimeMillis() - lStart;

        TextView tv1 = (TextView) findViewById(R.id.textView1);
        tv1.setText("f(1.5) equals to " + sol[0]);

        TextView tv2 = (TextView) findViewById(R.id.textView2);
        tv2.setText("Time was "+ lElapsed + "ms");

    }

    protected class MyFK1DTask extends AsyncTask<String, Integer, String> {

        Long lStart, lEnd;
        final double[] xr = {0,0,0,0,0,0,0,0,0};
        final double[] fx = {0,0,0,0,0,0,0,0,0};

        @Override
        protected void onPreExecute() {

            lStart = System.currentTimeMillis();

            Toast.makeText(MainActivity.this, "Start FK 1D Interval",
                    Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... strings) {


            for(int i =0; i < 9; i++)
            {
                try{
                    xr[i] = (1.0 + ((double) (i + 1))/10.0);
                    fx[i] = MCOMPFromFK1D( xr[i]);
                }
                catch(Exception e){

                }
                publishProgress(i);
            }
            return "Finished Calculations";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            super.onProgressUpdate(values);

            pbar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {

            lEnd = System.currentTimeMillis();

            Toast.makeText(getApplicationContext(), "Finished FK Application!",
                    Toast.LENGTH_LONG).show();

            pbar.dismiss();

            tv = (TextView) findViewById(R.id.textView1);

            long tempTime = (lEnd - lStart)/1000;
//
            tv.setText("Time was "+ tempTime + " s");

//       open new activite transfer sol[]  double through

            try {
                Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
                Bundle bundle = new Bundle();
                bundle.putDoubleArray("fxArray", fx);
                bundle.putDoubleArray("xrArray", xr);
                intent.putExtras(bundle);
                startActivity(intent);

            } catch (Exception e){

            }
        }
    }

    private void  DoFK1DMultiPointAsyncTask(){

        pbar = new ProgressDialog(MainActivity.this);
        pbar.setCancelable(false);
        pbar.setTitle("Running FK - 1D ...");
        pbar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pbar.setMax(9);
        pbar.setProgress(0);
        pbar.show();

        // execute Async doInbackground calculations
        new MyFK1DTask().execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //
        super.onCreate(savedInstanceState);
        //
        setContentView(R.layout.activity_main);

        ThreadTypeButton = findViewById(R.id.rSThread);

        selectThreadMethodButton();
    }

    public void selectThreadMethodButton(){

        rThreadGroup = findViewById(R.id.radioThreads);
        btn_run = findViewById(R.id.button_run);

        btn_run.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                tv = findViewById(R.id.textView2);

                // get selected radio button from radioGroup
                int selectedId = rThreadGroup.getCheckedRadioButtonId();

                switch (selectedId) {
                    case R.id.rSThread:
                         tv.setText( R.string.single_thread);
                        DoThreadCalculation();
                        //
                        break;
                    case R.id.rSAsyncTask:
                         tv.setText( R.string.single_async_task);
                        DoAsyncTaskCalculations();
                        //
                        break;
                    case R.id.rMThreads:
                        tv.setText(R.string.multi_thread);
                        DoThreadParallelCalculation();
                        //
                        break;
                    case R.id.rMultiCall:
                        tv.setText(R.string.multi_callable);
                        DoCallableParallelCalculations();
                        //
                        break;
                    case R.id.rsMonteCarlo:
                        tv.setText(R.string.run_renderscript);
                        DoSingleonRenderScript();
                        //
                        break;
                    case R.id.rMCJNI:
                        tv.setText(R.string.run_jni);
                        DoSingleJNI();
                        //
                        break;
                    case R.id.rMCOMPJNI:
                        tv.setText(R.string.run_jni_omp);
                        DoOMPJNI();
                        //
                        break;
                    case R.id.rMCFK1DJNI:
                        DoFK1DJNI();
                        //
                        break;
                    case R.id.rMCFK1DJNIMultPoint:
                        DoFK1DMultiPointAsyncTask();
                        //
                        break;
                }

            }
        });
    }

    public native double MCFromJNI();
    public native double MCOMPFromJNI();
    public native double MCOMPFromFK1D(double point);

}
