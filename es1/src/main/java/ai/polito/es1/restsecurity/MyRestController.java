package ai.polito.es1.restsecurity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Date;

@RestController
public class MyRestController {
  @GetMapping("/esempio")
  public MyData home(Principal principal) {
    return MyData.builder()
               .name(principal != null ? principal.getName() : "<NO NAME>") //.name("Prova")x
               .date(new Date())
               .build();
  }
  
  @GetMapping("/r")
  public Date homerest() {
    return new Date();
  }
  
  @GetMapping("/r/{r1}")
//  @RequestMapping(value="/users/{id}",  method= {RequestMethod.GET, RequestMethod.POST})
  public String r1(@PathVariable String r1) {
    return (new Date()).toString() + "-" + r1;
  }
}
