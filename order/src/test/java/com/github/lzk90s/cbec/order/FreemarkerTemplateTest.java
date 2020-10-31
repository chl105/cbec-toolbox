package com.github.lzk90s.cbec.order;

import com.github.lzk90s.cbec.order.dao.entity.OrderEntity;
import freemarker.template.TemplateException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import java.io.IOException;
import java.util.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class FreemarkerTemplateTest {
    private static final String TEMPLATE_NAME = "unhandled-order.html";

    @Autowired
    private FreeMarkerConfig freeMarkerConfig;

    @Test
    public void testTemplate(){
        List<OrderEntity> orderEntityList = new ArrayList<>();
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId("11");
        orderEntity.setImageUrl("https://image-tb.vova.com/image/262_262/filler/6b/cc/1035aa9aefc6b869ad9ce4e985966bcc.jpg");
        orderEntity.setNum(1);
        orderEntity.setPlatform("vova");
        orderEntity.setPrice(100.20f);
        orderEntity.setType("集运订单");
        orderEntity.setConfirmTime(new Date());
        orderEntityList.add(orderEntity);

        OrderEntity orderEntity1 = new OrderEntity();
        orderEntity1.setId("11");
        orderEntity1.setImageUrl("https://image-tb.vova.com/image/262_262/filler/6b/cc/1035aa9aefc6b869ad9ce4e985966bcc.jpg");
        orderEntity1.setNum(1);
        orderEntity1.setPlatform("vova");
        orderEntity1.setPrice(100.20f);
        orderEntity1.setType("集运订单");
        orderEntity1.setConfirmTime(new Date());
        orderEntity1.setLastNotifyTime(new Date());
        orderEntityList.add(orderEntity1);

        OrderEntity orderEntity2 = new OrderEntity();
        orderEntity2.setId("11");
        orderEntity2.setImageUrl("https://image-tb.vova.com/image/262_262/filler/6b/cc/1035aa9aefc6b869ad9ce4e985966bcc.jpg");
        orderEntity2.setNum(1);
        orderEntity2.setPlatform("vova");
        orderEntity2.setPrice(100.20f);
        orderEntity2.setType("集运订单");
        orderEntity2.setConfirmTime(new Date());
        orderEntityList.add(orderEntity2);

        var msg = buildNotifyMessage(orderEntityList);
        System.out.println(msg);
    }

    private String buildNotifyMessage(List<OrderEntity> orderDTOList) {
        String title = "你有" + orderDTOList.size() + "个未处理订单，请及时处理！";
        Map<String, Object> model = new HashMap<>();
        model.put("title", title);
        model.put("orders", orderDTOList);

        try {
            var template = freeMarkerConfig.getConfiguration().getTemplate(TEMPLATE_NAME);
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
