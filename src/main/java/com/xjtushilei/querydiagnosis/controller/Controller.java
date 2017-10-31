package com.xjtushilei.querydiagnosis.controller;

import com.xjtushilei.querydiagnosis.lucene.search.Search;
import com.xjtushilei.querydiagnosis.sym.SymMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * @author shilei
 * @Date 2017/10/31.
 */
@RequestMapping("/api")
@RestController
public class Controller {

    @RequestMapping(value = "/search")
    public ArrayList<String> search(String input, int num) {
        return Search.search(input, num);
    }

    @RequestMapping(value = "/symRecommend")
    public HashMap<String, Object> symRecommend(@RequestParam(required = false, value = "input[]") ArrayList<String>
                                                        input) {
        return SymMethod.getSymptomRecommend(input);
    }

    @RequestMapping(value = "/getDisease")
    public HashMap<String, Object> getDisease(@RequestParam(required = false, value = "input[]") ArrayList<String>
                                                      input) {
        return SymMethod.getDisease(input);
    }
}
