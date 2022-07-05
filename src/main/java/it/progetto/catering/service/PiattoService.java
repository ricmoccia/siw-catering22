package it.progetto.catering.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.progetto.catering.model.Piatto;
import it.progetto.catering.repository.PiattoRepository;

@Service
public class PiattoService {

	@Autowired
	private PiattoRepository piattoRepository;

	@Transactional
	public void save(Piatto piatto) {
		piattoRepository.save(piatto);
	}
	
	@Transactional
	public Piatto inserisci (Piatto piatto) {
		return piattoRepository.save(piatto);
	}
	
	public Piatto findById(Long id) {
		//quando uso un metodo optional, devo usare get() per farmi ritornare l'oggetto
		return piattoRepository.findById(id).get();
	}
	
	public Piatto findByNome (String nome) {
		return piattoRepository.findByNome(nome);
	}
	
	public List<Piatto> findByIds (List<Long> ids) {
		var i = piattoRepository.findAllById(ids);
		List<Piatto> listaPiatti = new ArrayList<>(); 
		for(Piatto piatto : i)
			listaPiatti.add(piatto);
		return listaPiatti;
	}
	
	public List<Piatto> findAll(){
		List<Piatto> piatti= new ArrayList<>();
		for(Piatto p: piattoRepository.findAll()) {
			piatti.add(p);
		}
		return piatti;
	}
	
	/*bisogna verificare se uno chef e gia nel database, devo chiedere al repository*/
	public boolean alreadyExists(Piatto piatto) {
		return piattoRepository.existsByNomeAndDescrizione(piatto.getNome(), piatto.getDescrizione());		 
	}

	@Transactional
	public void delete(Piatto piatto) {
		piattoRepository.delete(piatto);
	}
	
	public void deletePiattoById(Long id) {
		piattoRepository.deleteById(id);
	}	
}
