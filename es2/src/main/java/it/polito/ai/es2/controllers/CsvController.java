package it.polito.ai.es2.controllers;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import it.polito.ai.es2.domains.StudentViewModel;
import it.polito.ai.es2.domains.TeamViewModel;
import it.polito.ai.es2.services.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@Controller
@RequestMapping("/csv")
public class CsvController {
  @Autowired
  TeamService teamService;
  
  @GetMapping() //  @ResponseStatus(HttpStatus.CREATED)
  public String index(@ModelAttribute("command") TeamViewModel teamViewModel,
                      BindingResult bindingResult,
                      Model model) {
    model.addAttribute("command", new TeamViewModel(null, "Team_testing_email", "Course_testing_email", null));
    return "home_csv";
  }
  
  @PostMapping("/upload-csv-file")
  public String uploadCSVFile(@RequestParam("file") MultipartFile file, @RequestParam("corso") String corso, Model model) {
    // validate file
    if (file.isEmpty()) {
      model.addAttribute("message", "Please select a CSV file to upload.");
      model.addAttribute("status", false);
    } else {
      // parse CSV file to create a list of `StudentViewModel` objects
      Reader reader;
      try {
        /* -1st problame: inefficente duplicare tutto, ma non sò come fare al momento.
         * -2nd problema: user già presenti nel db vengono mostrati nel report finale (ovviamente, non essendo collegati...) */
        reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        // create csv bean reader
        CsvToBean<StudentViewModel> csvToBean = new CsvToBeanBuilder(reader)
                                                    .withType(StudentViewModel.class)
                                                    .withIgnoreLeadingWhiteSpace(true)
                                                    .build();
        // convert `CsvToBean` object to list of users
        List<StudentViewModel> users = csvToBean.parse();
        // save users list on model
        model.addAttribute("users", users);
        model.addAttribute("status", true);
        
        Reader reader2 = new BufferedReader(new InputStreamReader(file.getInputStream()));
        teamService.addAndEroll(reader2, corso);
      } catch (IOException e) {
        e.printStackTrace();
        model.addAttribute("message", "An error occurred while processing the CSV file.");
        model.addAttribute("status", false);
      }
    }
    return "file-upload-status";
  }
}
