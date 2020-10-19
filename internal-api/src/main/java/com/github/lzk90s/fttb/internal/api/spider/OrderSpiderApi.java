package com.github.lzk90s.fttb.internal.api.spider;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface OrderSpiderApi {
    @GetMapping("/list_unhandled_order")
    List<OrderDTO> listUnhandledOrder(@RequestParam("platform") String platform,
                                      @RequestParam("user") String user,
                                      @RequestParam("password") String password);
}
