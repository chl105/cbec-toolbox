package com.github.lzk90s.cbec.logistics.vendor.yw56;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author liuzhikun
 */
@Data
@ConfigurationProperties(prefix = "logistics.yw56")
public class Yw56Properties {
    private boolean enable;
    private String priceUrl;
    private String userId;
    private String apiToken;
    private String vendor;
}
