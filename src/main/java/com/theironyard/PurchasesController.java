package com.theironyard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileReader;

/**
 * Created by jessicahuffstutler on 11/11/15.
 */
@Controller
public class PurchasesController {
    @Autowired
    CustomerRepository customers;

    @Autowired
    PurchaseRepository purchases;

    //For each CSV file, you should loop over each line, parse each column into a Customer or Purchase object, and add it to a repository
    //Make it so this only happens when the repositories are empty
    @PostConstruct
    public void init() {
        if (customers.count() == 0) {
            String fileContent = readFile("customers.csv");
            String[] lines = fileContent.split("/n");
            for (String line : lines) {
                if (line == lines[0]) {
                    continue;
                }
                String[] columns = line.split(",");
                Customer c = new Customer();
                c.name = columns[0];
                c.email = columns[1];
                customers.save(c);
            }
        }

        if (purchases.count() == 0) {
            String fileContent = readFile("purchases.csv");
            String[] lines = fileContent.split("/n");
            for (String line : lines) {
                if (line == lines[0]) {
                    continue;
                }
                String[] columns = line.split(",");
                Purchase p = new Purchase();
                int customerId = Integer.valueOf(columns[0]);
                p.date = columns[1];
                p.creditCard = columns[2];
                int cvv = Integer.valueOf(columns[3]);
                p.cvv = cvv;
                p.category = columns[4];
                p.customer = customers.findOne(customerId);
                purchases.save(p);
            }
        }
    }

    @RequestMapping("/")
    public String home(
                Model model,
                String category
        ) {
        if (category != null) {
            model.addAttribute("customers", customers.findAll());
            model.addAttribute("purchases", purchases.findByCategory(category));
        }
        model.addAttribute("customers", customers);
        model.addAttribute("purchases", purchases.findAll());
        return "home";
    }

    static String readFile(String fileName) {
        File f = new File(fileName);
        try {
            FileReader fr = new FileReader(f);
            int fileSize = (int) f.length();
            char[] fileContent = new char[fileSize];
            fr.read(fileContent);
            return new String(fileContent);
        } catch (Exception e) {
            return null;
        }
    }
}