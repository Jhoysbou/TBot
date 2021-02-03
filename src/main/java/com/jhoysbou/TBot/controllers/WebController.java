package com.jhoysbou.TBot.controllers;

import com.jhoysbou.TBot.models.MenuItem;
import com.jhoysbou.TBot.services.EditingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
        log.info("index page accessed with id = {}", id);
        MenuItem item = editingService.getRoot();

        if (id.isPresent()) {
            item = editingService.getMenuItemById(id.get());
        }

        model.addAttribute("item", item);

        return "index";
    }


    @PostMapping("/edit")
    public String editItem(@RequestParam(required = false) Optional<Long> id,
                           @RequestParam(required = false) Optional<String> trigger,
                           @RequestParam(required = false) Optional<String> responseText) {
        log.info("editing item {} with trigger {} and responseText {}", id, trigger, responseText);
        editingService.updateMenuItem(
                id.orElse(editingService.getRoot().getId()),
                trigger,
                responseText);
        return "redirect:/";
    }

    @PostMapping("/add")
    public String createItem(@RequestParam(required = false) Optional<Long> id) {
        log.info("creating new element parent = {}", id);
        editingService.createNewMenuItem(
                id.orElse(
                        editingService.getRoot().getId()
                )
        );
        return "redirect:/";
    }

    @PostMapping("/delete")
    public String deleteItem(@RequestParam(required = false) Optional<Long> id) {
        log.info("deleting element with id {}", id);
        editingService.deleteMenuItem(
                id.orElse(
                        editingService.getRoot().getId()
                )
        );
        return "redirect:/";
    }
}
