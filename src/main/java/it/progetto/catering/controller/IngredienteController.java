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

import it.progetto.catering.controller.validator.IngredienteValidator;
import it.progetto.catering.model.Ingrediente;
import it.progetto.catering.model.Piatto;
import it.progetto.catering.service.IngredienteService;

@Controller
public class IngredienteController {

	@Autowired
	private IngredienteService ingredienteService;

	@Autowired
	private IngredienteValidator ingredienteValidator;	

	@PostMapping("/ingrediente")
	public String addIngrediente(@Valid @ModelAttribute("ingrediente") Ingrediente ingrediente, BindingResult bindingResult, Model model) {
		ingredienteValidator.validate(ingrediente, bindingResult);//se l'ingrediente che cerco di inserire e gia presente annullo l'inserimento, bindingResult da l'errore
		//prima di salvare l'ogg. ingrediente dobbiamo verificare che non ci siano stati errori di validazione
		if(!bindingResult.hasErrors()) {//se non ci sono stati err di validazione
			ingredienteService.save(ingrediente);
			model.addAttribute("ingrediente", ingrediente);
			return "ingrediente.html";
		}
		return "ingredienteForm.html";  //altrimenti ritorna alla pagina della form
	}

	@GetMapping("/ingrediente/{id}")
	public String getIngrediente(@PathVariable("id") Long id, Model model) {
		Ingrediente ingrediente = ingredienteService.findById(id);
		model.addAttribute("ingrediente", ingrediente);//la stringa mi indica che nelle viste, per recuperare l'ogg lo chiamiamo persona
		return "ingrediente.html"; //la vista successiva mostra i dettagli della persona
	}

	@GetMapping("/ingredienti")
	public String getAllIngredienti(Model model) {
		List<Ingrediente> ingredienti = ingredienteService.findAll();
		model.addAttribute("ingredienti", ingredienti);
		return "ingredienti.html";
	}


	@Transactional
	@PostMapping("/modificaIngrediente/{id}")
	public String modificaIngrediente(@PathVariable Long id, @Valid @ModelAttribute("ingrediente") Ingrediente ingrediente, BindingResult bindingResults, Model model) {
		if(!bindingResults.hasErrors()) {
			Ingrediente vecchioIngrediente = ingredienteService.findById(id);
			vecchioIngrediente.setNome(ingrediente.getNome());
			vecchioIngrediente.setDescrizione(ingrediente.getDescrizione());
			vecchioIngrediente.setOrigine(ingrediente.getOrigine());
			this.ingredienteService.save(vecchioIngrediente);
			model.addAttribute("ingrediente", ingrediente);
			return "ingrediente.html";
		} 
		return "modificaIngredientForm.html";
	}

	
	@GetMapping("/toDeleteIngredinete/{id}")
	public String toDeleteIngrediente(@PathVariable("id") Long id, Model model) {
		model.addAttribute("ingrediente", ingredienteService.findById(id));
		return "toDeleteIngrediente.html";
	}

	@Transactional
	@GetMapping("/deleteIngrediente/{id}")
	public String deleteIngrediente(@PathVariable("id")Long id, Ingrediente ingrediente, BindingResult bindingResult,Model model) {
		ingredienteService.deleteIngredienteById(id);
		model.addAttribute("ingredienti", ingredienteService.findAll());
		return "ingredienti.html";
	}


	@GetMapping("/ingredienteForm")
	public String getIngredienteForm(Model model) {
		model.addAttribute("ingrediente", new Ingrediente());
		return "ingredienteForm.html";
	}




}
