package org.easycassandra.persistence.cassandra;

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

    private static final int THREAD_NUMBER = 10;
    private ExecutorService service = Executors.newFixedThreadPool(THREAD_NUMBER);

    public <T> void runSelect(ResultCallBack<List<T>> callBack,
            ResultSetFuture result, Class<?> class1) {

        result.addListener(new RunSelectRunnable<>(callBack, result, class1),
                service);
    }

    public void runUpdate(ResultCallBack<Boolean> callBack, ResultSetFuture resultSet) {
        resultSet.addListener(new RunUpdateRunnable<>(callBack), service);
    }
    /**
     * to run select async.
     * @author otaviojava
     * @param <T>
     */
    class RunSelectRunnable<T> implements Runnable {

        private ResultCallBack<List<T>> callBack;
        private ResultSetFuture resultSetFuture;
        private Class<?> beanClass;

        public RunSelectRunnable(ResultCallBack<List<T>> callBack,
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
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

    }
    /**
     * to run update, insert, delete async.
     * @author otaviojava
     * @param <T>
     */
    class RunUpdateRunnable<T> implements Runnable {

        private ResultCallBack<Boolean> callBack;

        public RunUpdateRunnable(ResultCallBack<Boolean> callBack) {
            this.callBack = callBack;
        }

        @Override
        public void run() {
            callBack.result(Boolean.TRUE);
        }

    }
}
