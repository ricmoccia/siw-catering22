package it.progetto.catering.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.progetto.catering.controller.validator.ChefValidator;
import it.progetto.catering.model.Chef;
import it.progetto.catering.service.ChefService;

@Controller
public class ChefController {

	@Autowired
	private ChefService chefService;

	@Autowired
	private ChefValidator chefValidator;	

	@PostMapping("/chef")
	public String addChef(@Valid @ModelAttribute("chef") Chef chef, BindingResult bindingResult, Model model) {
		chefValidator.validate(chef, bindingResult);//se lo chef che cerco di inserire e gia presente annullo l'inserimento, bindingResult da l'errore
		//prima di salvare l'ogg. persona dobbiamo verificare che non ci siano stati errori di validazione
		if(!bindingResult.hasErrors()) {//se non ci sono stati err di validazione
			chefService.save(chef);
			model.addAttribute("chef", chef);
			return "chef.html"; //pagina con chef aggiunto
		}
		return "chefForm.html";  //altrimenti ritorna alla pagina della form(ci sono stati degli errori)
	}

	@GetMapping("/chef/{id}")
	public String getChef(@PathVariable("id") Long id, Model model) {
		Chef chef = chefService.findById(id);
		model.addAttribute("chef", chef);//la stringa mi indica che nelle viste, per recuperare l'ogg lo chiamiamo persona
		return "chef.html"; //la vista successiva mostra i dettagli della persona
	}

	/*Questo metodo che ritorna la form, prima di ritornarla, mette nel modello un ogg persona appena creato
	 * */
	@GetMapping("/chefForm")
	public String getChefForm(Model model) {
		//in questo modo chefForm ha un ogg Chef a disposizione(senza questa op. non l'avrebbe avuto)
		model.addAttribute("chef", new Chef());
		return "chefForm.html"; 		
	}

	@GetMapping("/chef")
	public String getAllChefs(Model model) {
		List<Chef> chefs = chefService.findAll();
		model.addAttribute("chefs", chefs);
		return "chefs.html";
	}

	@Transactional
	@GetMapping("/modificaChef/{id}")
	public String modificaChef(@PathVariable Long id, @Valid @ModelAttribute("chef") Chef chef, BindingResult bindingResults, Model model) {
		if(!bindingResults.hasErrors()) {//se non ci sono stati err di validazione
			Chef vecchioChef = chefService.findById(id);
			vecchioChef.setNome(chef.getNome());
			vecchioChef.setCognome(chef.getCognome());
			vecchioChef.setNazionalita(chef.getNazionalita());
			this.chefService.save(vecchioChef);
			model.addAttribute("chef", chef);
			return "chef.html";//pagina con lo chef modificato
		} 
		return "modificaChefForm.html";//se ci sono stati degli errori ritorna alla pagina per la modifica dello chef

	}

	@GetMapping("/toDeleteChef/{id}")
	public String toDeleteChef(@PathVariable("id") Long id, Model model) {
		model.addAttribute("chef", chefService.findById(id));
		return "toDeleteChef.html";
	}
	
	@Transactional
	@GetMapping("/deleteChef/{id}")
	public String deleteChef(@PathVariable("id")Long id, Chef chef, BindingResult bindingResult,Model model) {
		chefService.deleteChefById(id);
		model.addAttribute("chefs", chefService.findAll());
		return "chefs.html";
	}



	@GetMapping("/modificaChefForm/{id}")
	public String getChefForm(@PathVariable Long id, Model model) { 
		model.addAttribute("chef", chefService.findById(id));
		return "modificaChefForm.html";
	}












}
