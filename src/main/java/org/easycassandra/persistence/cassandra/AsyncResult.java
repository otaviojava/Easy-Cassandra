package org.easycassandra.persistence.cassandra;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.ResultSetFuture;

/**
 * util to async process.
 * @author otaviojava
 */
enum AsyncResult {
    INSTANCE;

    {
        int processorsNumber = Runtime.getRuntime().availableProcessors();
        service = Executors.newFixedThreadPool(processorsNumber * 2);
    }
    private ExecutorService service;

    public <T> void runSelect(ResultAsyncCallBack<List<T>> callBack,
            ResultSetFuture result, Class<?> class1) {

        result.addListener(new RunSelectRunnable<>(callBack, result, class1),
                service);
    }

    public <T> void runKey(ResultAsyncCallBack<T> callBack,
            ResultSetFuture result, Class<?> class1) {

        result.addListener(new RunFindKeyRunnable<>(callBack, result, class1),
                service);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <T> void runKeys(ResultAsyncCallBack<List<T>> callBack,
            List<ResultSetFuture> results, Class<?> class1) {
        service.execute(new RunFindKeysRunnable(callBack, results, class1));
    }

    public void runUpdate(ResultAsyncCallBack<Boolean> callBack, ResultSetFuture result) {
        result.addListener(new RunUpdateRunnable<>(callBack), service);
    }

    public void runCount(ResultAsyncCallBack<Long> callBack, ResultSetFuture result) {
        result.addListener(new RunCountRunnable<>(callBack, result), service);
    }


    /**
     * to run select async.
     * @author otaviojava
     * @param <T>
     */
    class RunSelectRunnable<T> implements Runnable {

        private ResultAsyncCallBack<List<T>> callBack;
        private ResultSetFuture resultSetFuture;
        private Class<?> beanClass;

        public RunSelectRunnable(ResultAsyncCallBack<List<T>> callBack,
                ResultSetFuture resultSetFuture, Class<?> beanClass) {
            this.callBack = callBack;
            this.resultSetFuture = resultSetFuture;
            this.beanClass = beanClass;
        }

        @Override
        public void run() {
            ResultSet result;
            try {
                result = resultSetFuture.get();
                @SuppressWarnings("unchecked")
                List<T> beans = (List<T>) RecoveryObject.INTANCE.recoverObjet(beanClass, result);
                callBack.result(beans);
            } catch (InterruptedException | ExecutionException exception) {
                throw new AsyncExecption(exception);
            }
        }

    }

    /**
     * to run select async.
     * @author otaviojava
     * @param <T>
     */
    class RunFindKeyRunnable<T> implements Runnable {

        private ResultAsyncCallBack<T> callBack;
        private ResultSetFuture resultSetFuture;
        private Class<?> beanClass;

        public RunFindKeyRunnable(ResultAsyncCallBack<T> callBack,
                ResultSetFuture resultSetFuture, Class<?> beanClass) {
            this.callBack = callBack;
            this.resultSetFuture = resultSetFuture;
            this.beanClass = beanClass;
        }

        @Override
        public void run() {
            ResultSet result;
            try {
                result = resultSetFuture.get();
                @SuppressWarnings("unchecked")
                List<T> beans = (List<T>) RecoveryObject.INTANCE.recoverObjet(beanClass, result);

                if (!beans.isEmpty()) {
                    callBack.result(beans.get(0));
                } else {
                    callBack.result(null);
                }

            } catch (InterruptedException | ExecutionException exception) {
                throw new AsyncExecption(exception);
            }
        }

    }
    /**
     * to run select count async.
     * @author otaviojava
     * @param <T>
     */
    class RunCountRunnable<T> implements Runnable {

        private ResultAsyncCallBack<Long> callBack;
        private ResultSetFuture resultSetFuture;

        public RunCountRunnable(ResultAsyncCallBack<Long> callBack,
                ResultSetFuture resultSetFuture) {
            this.callBack = callBack;
            this.resultSetFuture = resultSetFuture;
        }

        @Override
        public void run() {
            ResultSet result;
            try {
                result = resultSetFuture.get();
                callBack.result(result.all().get(0).getLong(0));
            } catch (InterruptedException | ExecutionException exception) {
                throw new AsyncExecption(exception);
            }
        }

    }
    /**
     * to run update, insert, delete async.
     * @author otaviojava
     * @param <T>
     */
    class RunUpdateRunnable<T> implements Runnable {

        private ResultAsyncCallBack<Boolean> callBack;

        public RunUpdateRunnable(ResultAsyncCallBack<Boolean> callBack) {
            this.callBack = callBack;
        }

        @Override
        public void run() {
            callBack.result(Boolean.TRUE);
        }

    }

    /**
     * to run select async.
     * @author otaviojava
     * @param <T>
     */
    class RunFindKeysRunnable<T> implements Runnable {

        private ResultAsyncCallBack<List<T>> callBack;
        private List<ResultSetFuture> results;
        private Class<?> beanClass;
        private List<T> beanList;


        public RunFindKeysRunnable(ResultAsyncCallBack<List<T>> callBack,
                List<ResultSetFuture> results, Class<?> beanClass) {
            this.callBack = callBack;
            this.results = results;
            this.beanClass = beanClass;
            beanList = new LinkedList<>();

        }

        @Override
        public void run() {
            try {

                for (ResultSetFuture resultFutre : results) {
                    ResultSet result = resultFutre.get();
                    @SuppressWarnings("unchecked")
                    List<T> beans = (List<T>) RecoveryObject.INTANCE
                            .recoverObjet(beanClass, result);
                    if (!beans.isEmpty()) {
                        beanList.add(beans.get(0));
                    }

                }
                callBack.result(beanList);
            } catch (InterruptedException | ExecutionException exception) {
                throw new AsyncExecption(exception);
            }
        }

    }
    /**
     * execption to async process.
     * @author otaviojava
     */
    public class AsyncExecption extends RuntimeException {
        private static final long serialVersionUID = 1L;
        private static final String ERROR_MENSAGE = "a problem trying to retrieve the select query";
        /**
         * The Constructor for Exception.
         */
        public AsyncExecption(Throwable throwable) {
            super(ERROR_MENSAGE, throwable);
        }
    }
}
