package com.example.demo.controller;

import com.example.demo.dto.WriteDTO;
import com.example.demo.service.ReadWriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/user")
public class ReadWriteController {

    private final ReadWriteService readWriteService;

    @Autowired
    public ReadWriteController(ReadWriteService readWriteService) {
        this.readWriteService = readWriteService;
    }

    @PostMapping(value = "/write")
    public ResponseEntity<Void> write(@RequestBody WriteDTO writeDTO) throws ExecutionException, InterruptedException {
        return readWriteService.write(writeDTO);
    }

    @GetMapping(value = "/read")
    public ResponseEntity<WriteDTO> read(@RequestParam(required = false) String userName) {
        if (Objects.nonNull(userName)) {
            readWriteService.readSingleUser(userName);
        } else {
            readWriteService.readAllUsers();
        }
        return ResponseEntity.ok().build();
    }

//    @GetMapping(value = {"/read", "/read/{user}"})
//    public ResponseEntity<WriteDTO> read(@PathVariable(required = false) String userName) {
//        if (Objects.nonNull(userName)) {
//            readWriteService.readSingleUser(userName);
//        } else {
//            readWriteService.readAllUsers();
//        }
//        return ResponseEntity.ok().build();
//    }

}
