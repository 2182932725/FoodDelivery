package com.sky.controller.admin;


import com.sky.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController") //设置在Bean容器中的名称，防止和前端的ShopController名称冲突
@RequestMapping("/admin/shop")
public class ShopController {

    public static final String SHOP_STATUS = "SHOP_STATUS";

    @Autowired
    private RedisTemplate redisTemplate;


    @PutMapping("/{status}")
    //设置店铺状态
    public Result setStatus(@PathVariable Integer status){
        redisTemplate.opsForValue().set(SHOP_STATUS,status);
        return Result.success();
    }

    @GetMapping("/status")
    //设置店铺状态
    public Result<Integer> getStatus(){
        Object shopStatus = redisTemplate.opsForValue().get(SHOP_STATUS);

        return Result.success((Integer) shopStatus);
    }



}
