package com.aphc.displayboard.controller;

import com.aphc.displayboard.model.Item;
import com.aphc.displayboard.model.ItemWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
public class DisplayBoardController {

    private static final Logger log = LoggerFactory.getLogger(DisplayBoardController.class);

    @Autowired
    // restTemplate is coming from the bean factory method in main class
    private RestTemplate restTemplate;

    @RequestMapping("/status/all")
    public ItemWrapper getStatus() {
        String response;
        List<Item> itemList = new ArrayList<>();
        try {
            response = getForObject();
            log.info("Data fetched successfully");
        } catch (Exception e) {
            log.error("Error occurred while fetching the list");
            return new ItemWrapper(itemList);
        }
        if(response != null) {
            // Splitting and traversing through the items fetched
            // Items are separated by $
            for (String item : response.trim().split("\\$")) {
                // Splitting the item to extract the status
                // Data in each item is separated by @
                String[] content = item.split("@");
                if (content.length >= 3) {
                    // Creating new item with Court No. and Case No.
                    // Item is a model class which contains two String attributes (courtNumber and caseNumber)
                    itemList.add(new Item(content[0], content[2]));
                }
            }
        }
        // ItemWrapper is a model class which contains one ArrayList of items (itemList)
        return new ItemWrapper(itemList);
    }

    @RequestMapping("/status/{court}")
    public Item getByItem(@PathVariable("court") String courtNo) {
        String response;
        try {
            response = getForObject();
            log.info("Data fetched successfully");
        } catch (Exception e) {
            log.error("Error occurred while fetching the list");
            return new Item(courtNo, "Not Available");
        }
        if(response != null) {
            // Splitting and traversing through the items fetched
            // Items are separated by $
            for (String item : response.trim().split("\\$")) {
                // Splitting the item to extract the status
                // Data in each item is separated by @
                String[] content = item.split("@");
                if (content.length >= 3 && content[0].equals(courtNo)) {
                    // Creating new item with Court No. and Case No.
                    return new Item(courtNo, content[2]);
                }
            }
        }
        return new Item(courtNo, "Not Available");
    }

    private String getForObject() {
        // aphc rest api uri is hardcoded here and response is captured in String format
        // Todo: move this uri to application.properties
        return restTemplate.getForObject("https://hc.ap.nic.in/Hcdbs/displaytext.jsp", String.class);
    }
}
