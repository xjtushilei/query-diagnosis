package com.xjtushilei.querydiagnosis.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author shilei
 * @Date 2017/10/31.
 */
@RequestMapping("/api")
@RestController
public class Controller {

    //    @RequestMapping(value = "/search")
    //    public ArrayList<HashMap<String,String>> search(String input, int num) {
    //        return Search.search(input, Arrays.asList(), num);
    //    }
    //
    //    @RequestMapping(value = "/symRecommend")
    //    public HashMap<String, Object> symRecommend(@RequestParam(required = false, value = "input[]") ArrayList<String>
    //                                                        input) {
    //        return SymMethod.getSymptomRecommend(input);
    //    }
    //
    //    @RequestMapping(value = "/getDisease")
    //    public HashMap<String, Object> getDisease(@RequestParam(required = false, value = "input[]") ArrayList<String>
    //                                                      input) {
    //        return SymMethod.getDisease(input);
    //    }
}
