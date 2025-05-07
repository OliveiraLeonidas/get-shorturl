package com.oliveira.shortener.web.controller;


import com.oliveira.shortener.domain.entities.ShortUrl;
import com.oliveira.shortener.domain.entities.User;
import com.oliveira.shortener.domain.exceptions.ShortUrlNotFoundException;
import com.oliveira.shortener.domain.models.CreateShortUrl;
import com.oliveira.shortener.domain.models.ShortUrlDto;
import com.oliveira.shortener.domain.services.ShortUrlService;
import com.oliveira.shortener.web.ApplicationProperties;
import com.oliveira.shortener.web.dtos.CreateShortUrlForm;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    private final ShortUrlService shortUrlService;
    private final ApplicationProperties properties;
    private final SecurityUtils securityUtils;

    public HomeController(ShortUrlService shortUrlService, ApplicationProperties properties, SecurityUtils securityUtils) {
        this.shortUrlService = shortUrlService;
        this.properties = properties;
        this.securityUtils = securityUtils;
    }


    @GetMapping("/")
    public String home(Model model) {
        List<ShortUrlDto> shortUrls = shortUrlService.findAllPublicShortUrls();
        model.addAttribute("shortUrls", shortUrls);
        model.addAttribute("baseUrl", properties.baseUrl());
        model.addAttribute("createShortUrlForm", new CreateShortUrlForm("", false, null));
        return "index";
    }

    @PostMapping("/short-urls")
    String createShortUrl(@ModelAttribute("createShortUrlForm") @Valid CreateShortUrlForm form,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes,
                          Model model) {
        if (bindingResult.hasErrors()) {
            List<ShortUrlDto> shortUrls = shortUrlService.findAllPublicShortUrls();
            model.addAttribute("shortUrls", shortUrls);
            model.addAttribute("baseUrl", properties.baseUrl());
            return "index";
        }

        try {
            Long userId = securityUtils.getCurrentUserId();
            CreateShortUrl createShortUrl = new CreateShortUrl(
                    form.originalUrl(),
                    form.isPrivate(),
                    form.expirationInDays(),
                    userId);
            var shortDto = shortUrlService.createShortUrl(createShortUrl);
            redirectAttributes.addFlashAttribute("successMessage", "Short URL created successfully " + properties.baseUrl() +"/s/"+shortDto.shortKey());
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to create short URL");
        }

        return "redirect:/";
    }

    @GetMapping("/s/{shortKey}")
    String redirectToOriginalUrl(@PathVariable String shortKey) {
        Long userId = securityUtils.getCurrentUserId();
        Optional<ShortUrlDto> shortUrlDtoOptional = shortUrlService.accessShortUrl(shortKey, userId);
        if(shortUrlDtoOptional.isEmpty()) {
            throw new ShortUrlNotFoundException("Invalid short key: "+shortKey);
        }
        ShortUrlDto shortUrlDto = shortUrlDtoOptional.get();
        return "redirect:"+shortUrlDto.originalUrl();
    }

    @GetMapping("/login")
    String loginForm() {
        return "login";
    }

}
