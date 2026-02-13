package com.sky.controller.admin;


import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin/dish")
@Slf4j
public class DishController {


    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 新增菜品
     * @param dishDTO
     * @return
     */
    @PostMapping
    public Result save(@RequestBody DishDTO dishDTO) {

        dishService.saveWithFlavor(dishDTO);

        String key= "dish_"+dishDTO.getCategoryId();
        cleanCahce(key);

        return Result.success();
    }


    @GetMapping("/page")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
        PageResult pageResult= dishService.page(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    //RequestParam注解用于获取请求参数,可以动态解析字符串并放在list当中
    @DeleteMapping
    public Result delete(@RequestParam List<Long> ids) {

        dishService.deleteWithFlavor(ids);

        cleanCahce("dish_*");


        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<DishVO> get(@PathVariable Long id) {
        DishVO dishVO = dishService.getByIdWithFlavor(id);
        return Result.success(dishVO);
    }

    @PutMapping
    public Result update(@RequestBody DishDTO dishDTO) {
        dishService.updateWithFlavor(dishDTO);

        cleanCahce("dish_*");

        return Result.success();
    }

    @GetMapping("/list")
    public Result<List<Dish>> getByCategoryId(Long categoryId) {
        List<Dish> dishList = dishService.getByCategoryId(categoryId);
        return Result.success(dishList);
    }

    @PostMapping("/status/{status}")
    public Result startOrStop(@PathVariable Integer status, Long id) {
        dishService.updateStatus(status, id);

        cleanCahce("dish_*");

        return Result.success();
    }

    private void cleanCahce(String key) {
        //将所有的菜品缓存都清理掉，所有以dish_开头的缓存都删除
        Set keys = redisTemplate.keys(key);
        redisTemplate.delete(keys);
    }



}
