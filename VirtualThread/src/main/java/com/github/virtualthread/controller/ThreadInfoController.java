package com.github.virtualthread.controller;

import com.github.virtualthread.context.TraceContext;
import com.github.virtualthread.model.ThreadInfoResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/thread")
public class ThreadInfoController {

    @GetMapping("/current")
    public ThreadInfoResponse current() {
        Thread thread = Thread.currentThread();
        return new ThreadInfoResponse(
                thread.toString(),
                thread.getName(),
                thread.isVirtual(),
                TraceContext.get()
        );
    }
}
