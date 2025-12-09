package com.example.demo.service;

import com.example.demo.dto.ReadDTO;
import com.example.demo.dto.WriteDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

@Service
public class ReadWriteService {
    @Value("${app.threads}")
    private int numberOfThreads;
    private  ExecutorService exec;
    private  Map<String, ReadDTO> map;

    @PostConstruct
    public void initialize() {
        this.exec = Executors.newFixedThreadPool(numberOfThreads);
        this.map = new HashMap<>();
    }


    public void readSingleUser(String userName) {

        ReadDTO readDTO = map.get(userName);
        System.out.println("User = " + userName
                + " RepeatTimes = " + readDTO.getRepeatTimes()
                + " Text = " + readDTO.getText());
    }

    public void readAllUsers() {
        ExecutorService exec = Executors.newFixedThreadPool(numberOfThreads);

        map.forEach((key, value) ->
                exec.submit(() ->
                        System.out.println("User = " + key
                        + " RepeatTimes = " + value.getRepeatTimes()
                        + " Text = " + value.getText() + " Thread = " + Thread.currentThread().getName())
        ));

        exec.shutdown();
    }

    public void write(WriteDTO writeDTO) throws ExecutionException, InterruptedException {
        CompletionService<String> completionService = new ExecutorCompletionService<>(exec);

        for (int i=0; i < writeDTO.getRepeatTimes(); i++) {
            int index = i;

            completionService.submit(() -> {
                try {
                    if (writeDTO.getDelay() != 0 && index != writeDTO.getRepeatTimes() - 1) {
                        Thread.sleep(writeDTO.getDelay());
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return writeDTO.getText();
            });

        }

        for (int i=0; i < writeDTO.getRepeatTimes(); i++) {
            System.out.println(completionService.take().get() + " Thread = " + Thread.currentThread().getName());
        }

        ReadDTO readDTO = new ReadDTO();
        readDTO.setText(writeDTO.getText());
        readDTO.setRepeatTimes(writeDTO.getRepeatTimes());

        map.put(writeDTO.getUserName(), readDTO);
    }

    //            exec.submit(() -> {
//                try {
//                    System.out.println(writeDTO.getText());
//                    if (writeDTO.getDelay() != 0 && index != writeDTO.getRepeatTimes() - 1) {
//                        Thread.sleep(writeDTO.getDelay());
//                    }
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            });
}
