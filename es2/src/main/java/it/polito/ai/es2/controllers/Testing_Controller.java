package it.polito.ai.es2.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/testing")
public class Testing_Controller {
  
  @GetMapping("/testmap")
  @ResponseBody
  public Map<String, String> testMapBinding() {
    Map<String, String> m = new HashMap<String, String>(); //Map.of
    m.put("key1", "value1");
    m.put("key2", "value2");
    return m;
  }
  
  @RequestMapping(value = {"/", "/hello"}, produces = "application/json; charset=UTF-8")
  @ResponseBody
  public String hello() {
    return "{\"nombre\":\"Donell\", \"dni\":\"351351P\"}";
  }
  
  @GetMapping("/hello2")
  @ResponseBody //default: text/plain
  public String hello2() {
    return "Hello World 2";
  }
}
