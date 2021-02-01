package com.jhoysbou.TBot.controllers;

import com.jhoysbou.TBot.models.MenuItem;
import com.jhoysbou.TBot.services.EditingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/")
public class WebController {
    private static final Logger log = LoggerFactory.getLogger(WebController.class);
    private final EditingService editingService;

    @Autowired
    public WebController(EditingService editingService) {
        this.editingService = editingService;
    }

    @GetMapping
    public String index(@RequestParam(required = false) Optional<Long> id,
                        final Model model) {
        MenuItem item = editingService.getRoot();

        if (id.isPresent()) {
            item = editingService.getMenuItemById(id.get());
        }

        model.addAttribute("item", item);

        return "index";
    }
}
