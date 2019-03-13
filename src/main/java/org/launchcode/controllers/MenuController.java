package org.launchcode.controllers;

import org.launchcode.models.Category;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.launchcode.models.forms.AddMenuItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping(value="menu")
public class MenuController {

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private CheeseDao cheeseDao;

    @RequestMapping(value="")
    public String index(Model model) {
        model.addAttribute("title", "Menus");
        model.addAttribute("menus", menuDao.findAll());

        return "menu/index";
    }

    @RequestMapping(value="add", method=RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute("title", "Add Menu");
        model.addAttribute("menu", new Menu());

        return "menu/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(Model model, @ModelAttribute @Valid Menu menu, Errors errors) {
        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Menu");
            return "menu/add";
        }
        menuDao.save(menu);
        return "redirect:view/" + menu.getId();
    }

    @RequestMapping(value="view/{id}", method=RequestMethod.GET)
    public String viewMenu(Model model, @PathVariable("id") int id) {
        Menu menu = menuDao.findOne(id);
        model.addAttribute("title", "View Menu: " + menu.getName());
        model.addAttribute("menu", menuDao.findOne(id));

        return "menu/view";
    }

    @RequestMapping(value="add-item/{id}", method=RequestMethod.GET)
    public String addItem(Model model, @PathVariable("id") int id) {
        Menu menu = menuDao.findOne(id);
        AddMenuItemForm form = new AddMenuItemForm(menu, cheeseDao.findAll());

        model.addAttribute("form", form);
        model.addAttribute("title", "Add Item to Menu: " + menu.getName());
        return "menu/add-item";
    }

    @RequestMapping(value="add-item/{id}", method=RequestMethod.POST)
    public String addItem(Model model, @PathVariable("id") int id, @ModelAttribute @Valid AddMenuItemForm form, Errors errors) {

        System.out.println("HERE");
        Menu menu = menuDao.findOne(id);
        if (errors.hasErrors()) {
            model.addAttribute("title", "View Menu: " + menu.getName());
            return "menu/add";
        }

        menu.add(cheeseDao.findOne(form.getCheeseId()));
        menuDao.save(menu);

        return "redirect:/menu/view/" + menu.getId();
    }

}
