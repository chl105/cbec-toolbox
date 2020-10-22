package com.github.lzk90s.cbec.internal.api.spider;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface OrderSpiderApi {
    @GetMapping("/list_unhandled_order/{platform}")
    List<OrderDTO> listUnhandledOrder(@PathVariable("platform") String platform,
                                      @RequestParam("user") String user,
                                      @RequestParam("password") String password);
}
