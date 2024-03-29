package com.jhoysbou.TBot.controllers;

import com.jhoysbou.TBot.models.MenuItem;
import com.jhoysbou.TBot.models.UpdateItemDto;
import com.jhoysbou.TBot.services.EditingService;
import com.jhoysbou.TBot.utils.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
  public ResponseEntity<String> editItem(@RequestParam(required = false) Optional<Long> id,
      @RequestBody(required = true) UpdateItemDto body,
      Model model) {
    log.info("editing item {} with trigger {} and responseText {}", id, body.getTrigger(), body.getResponseText());
    try {
      editingService.updateMenuItem(
          id.orElse(editingService.getRoot().getId()),
          body.getTrigger(),
          body.getResponseText(),
          body.getIsSubscriptionRequired());
    } catch (ValidationException e) {
      log.info("Validation exception: {}", e.getMessage());
      return ResponseEntity
          .status(HttpStatus.NOT_ACCEPTABLE)
          .body(e.getMessage());
    }

    return ResponseEntity
        .status(HttpStatus.ACCEPTED)
        .body("redirect:/");
  }

  @PostMapping("/add")
  public ResponseEntity<String> createItem(@RequestParam(required = false) Optional<Long> id) {
    log.info("creating new element parent = {}", id);
    try {
      editingService.createNewMenuItem(
          id.orElse(
              editingService.getRoot().getId()));
    } catch (ValidationException e) {
      log.info("Validation exception {}", e.getMessage());
      return ResponseEntity
          .status(HttpStatus.NOT_ACCEPTABLE)
          .body(e.getMessage());
    }

    return ResponseEntity
        .status(HttpStatus.ACCEPTED)
        .body("redirect:/");
  }

  @PostMapping("/delete")
  public String deleteItem(@RequestParam(required = false) Optional<Long> id) {
    log.info("deleting element with id {}", id);
    editingService.deleteMenuItem(
        id.orElse(
            editingService.getRoot().getId()));
    return "redirect:/";
  }
}
