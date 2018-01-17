package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Yarden on 1/17/2018.
 */
public class ExecutorsUtils {
    private static final Logger logger = LoggerFactory.getLogger(ExecutorsUtils.class);

    public static void shutdownExecutor(ExecutorService executorService){
        executorService.shutdown();
        try{
            if (!executorService.awaitTermination(10000, TimeUnit.MILLISECONDS)){
                logger.error("timeout passed while shutdown of {}. trying force shutdown.", executorService.getClass().getSimpleName());
                executorService.shutdownNow();
            }
        }catch (InterruptedException e){
            logger.error("shutdown of {} was interrupted. trying force shutdown", executorService.getClass().getSimpleName(), e);
            executorService.shutdownNow();
        }    
    }
}
