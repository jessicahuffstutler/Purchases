package com.theironyard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

/**
 * Created by jessicahuffstutler on 11/11/15.
 */
@Controller
public class PurchasesController {
    @Autowired
    CustomerRepository customers;

    @Autowired
    PurchaseRepository purchases;

    @PostConstruct
    public void init() throws FileNotFoundException {
        if (customers.count() == 0) {
            Scanner scanner = new Scanner(new File("customers.csv"));
            scanner.nextLine();
            while(scanner.hasNext()) {
                String line = scanner.nextLine();
                String[] columns = line.split(",");
                Customer c = new Customer();
                c.name = columns[0];
                c.email = columns[1];
                customers.save(c);
                }
//            String fileContent = readFile("customers.csv");
//            String[] lines = fileContent.split("/n");
//            for (String line : lines) {
//                if (line == lines[0]) {
//                    continue;
//                } else {
//                    String[] columns = line.split(",");
//                    Customer c = new Customer();
//                    c.name = columns[0];
//                    c.email = columns[1];
//                    customers.save(c);
//                }
//            }
        }

        if (purchases.count() == 0) {
            Scanner scanner = new Scanner(new File("purchases.csv"));
            scanner.nextLine();
            while(scanner.hasNext()) {
                String line = scanner.nextLine();
                String[] columns = line.split(",");
                Purchase p = new Purchase();
                p.date = columns[1];
                p.creditCard = columns[2];
                p.cvv = Integer.valueOf(columns[3]);
                p.category = columns[4];

                int customerId = Integer.valueOf(columns[0]);
                p.customer = customers.findOne(customerId);
                purchases.save(p);
                }
//            String fileContent = readFile("purchases.csv");
//            String[] lines = fileContent.split("/n");
//            for (String line : lines) {
//                if (line == lines[0]) {
//                    continue;
//                } else {
//                    String[] columns = line.split(",");
//                    Purchase p = new Purchase();
//
//                    p.date = columns[1];
//                    p.creditCard = columns[2];
//                    p.cvv = Integer.valueOf(columns[3]);
//                    p.category = columns[4];
//
//                    int customerId = Integer.valueOf(columns[0]);
//                    p.customer = customers.findOne(customerId);
//                    purchases.save(p);
//                }
//            }
        }
    }

    @RequestMapping("/")
    public String home(Model model, String category, @RequestParam(defaultValue = "0") int page) {
        //Pagable is an interface and you cant call it directly "Pageable it abstract and cannot be intantiated"
        PageRequest pr = new PageRequest(page, 10);

        Page p;

        if (category != null) {
            p = purchases.findByCategory(pr, category);
        } else {
            p = purchases.findAll(pr);
        }

        model.addAttribute("nextPage", page+1);
        model.addAttribute("category", category);
        model.addAttribute("purchases", p);
        model.addAttribute("showNext", p.hasNext());
        return "home";
    }

//    static String readFile(String fileName) {
//        File f = new File(fileName);
//        try {
//            FileReader fr = new FileReader(f);
//            int fileSize = (int) f.length();
//            char[] fileContent = new char[fileSize];
//            fr.read(fileContent);
//            return new String(fileContent);
//        } catch (Exception e) {
//            return null;
//        }
//    }
}