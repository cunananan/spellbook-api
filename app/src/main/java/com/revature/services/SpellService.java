package com.revature.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.revature.exceptions.InsufficientFundsException;
import com.revature.exceptions.ItemNotFoundException;
import com.revature.exceptions.OutOfStockException;
import com.revature.exceptions.ValidationException;
import com.revature.models.Spell;
import com.revature.models.Spell.SpellType;
import com.revature.models.SpellDto;
import com.revature.repositories.SpellRepository;

import io.micrometer.core.annotation.Timed;

@Service
public class SpellService {
	
	private SpellRepository sr;
	
	@Autowired
	public SpellService(SpellRepository sr) {
		this.sr = sr;
	}
	
	@Timed(value="spell.time")
	public List<SpellDto> getSpells() {
		List<Spell> spells = sr.findAllByOrderByIdAsc();
		if (spells.isEmpty()) {
			throw new ItemNotFoundException("No spells were found");
		}
		// Convert List<Spell> to List<SpellDto>
		return spells.stream().map(SpellDto::new).collect(Collectors.toList());
	}
	
	@Timed(value="spell.time")
	@Transactional
	public List<SpellDto> getSpellsByQuery(String search, SpellType type, int priceCap, Boolean inStock,
            int intCap, int faiCap, int arcCap)
	{	
		List<Spell> spells = (StringUtils.isBlank(search))
		                         ? sr.findAllByOrderByIdAsc()
	                             : findBySearchNameAndDescription(search);
		Stream<Spell> ss = spells.stream();
		if (type != null && type != SpellType.NOT_SET)
			ss = ss.filter(s -> s.getType() == type);
		if (priceCap >= 0)
			ss = ss.filter(s -> s.getPrice() <= priceCap);
		if (inStock != null)
			// if inStock checked, allow spells with stock > 0; else only allow spells with stock <= 0
			ss = ss.filter(s -> Boolean.TRUE.equals(inStock) ^ s.getStock() <= 0);
		if (intCap >= 0)
			ss = ss.filter(s -> s.getIntRequirement() <= intCap);
		if (faiCap >= 0)
			ss = ss.filter(s -> s.getFaiRequirement() <= faiCap);
		if (arcCap >= 0)
			ss = ss.filter(s -> s.getArcRequirement() <= arcCap);
		
		List<SpellDto> spellsDto = ss.map(SpellDto::new).collect(Collectors.toList());
		if (spellsDto.isEmpty()) {
			throw new ItemNotFoundException("No spells were found");
		}
		// Convert List<Spell> to List<SpellDto>
		return spellsDto;
	}
	
	@Timed(value="spell.time")
	public SpellDto getSpellById(int id) {
		Spell spell = sr.findById(id).orElseThrow(() -> 
		                                 new ItemNotFoundException("Spell not found") );
		return new SpellDto(spell);
	}
	
	@Timed(value="spell.time")
	@Transactional
	public UUID buySpell(int id, int funds) throws OutOfStockException, InsufficientFundsException {
		Spell spell = sr.findById(id).orElseThrow(() -> 
                                         new ItemNotFoundException("Spell not found") );
		if (spell.getStock() <= 0) {
			throw new OutOfStockException("Out of stock");
		}
		if (funds < spell.getPrice()) {
			throw new InsufficientFundsException("Insufficient funds; price is " + spell.getPrice());
		}
		spell.setStock(spell.getStock() - 1);
		sr.save(spell);
		// Return a product key for the spell
		return UUID.randomUUID();
	}
	
	@Timed(value="spell.time")
	public SpellDto addSpell(SpellDto newSpell) {
		if (newSpell == null) newSpell = new SpellDto();
		newSpell.id = -1;	// Ensure we don't overwrite an existing item
		Spell spell = new Spell();
		newSpell.copyTo(spell);
		newSpell.copyFrom(sr.save(spell));
		return newSpell;
	}
	
	@Timed(value="spell.time")
	@Transactional
	public SpellDto updateSpell(SpellDto spellUpdates) {
		if (spellUpdates == null) {
			throw new ValidationException("No updates were provided");
		} else {
			Spell spell = sr.findById(spellUpdates.id).orElseThrow(() -> 
			                                              new ItemNotFoundException("Spell not found") );
			spellUpdates.copyTo(spell);
			spellUpdates.copyFrom(sr.save(spell));
		}
		return spellUpdates;
	}
	
	@Timed(value="spell.time")
	@Transactional
	public SpellDto deleteSpell(int id) {
		Spell removedSpell = sr.findById(id).orElseThrow(() -> 
		                                        new ItemNotFoundException("Spell not found") );
		sr.deleteById(id);
		return new SpellDto(removedSpell);
	}
	
	// This is slow and I kind of hate it
	@Timed(value="spell.time")
	private List<Spell> findBySearchNameAndDescription(String search) {
		List<Spell> nameList = sr.findByNameContainingIgnoreCaseOrderByIdAsc(search);
		List<Spell> descList = sr.findByDescriptionContainingIgnoreCaseOrderByIdAsc(search);
		// The returned items should be in the following order:
		// - items that match by their name, by id asc
		// - items that match by their description, by id asc
		for (int i = 0; i < descList.size(); i++) {
			if (!nameList.contains(descList.get(i))) {
				nameList.add(descList.get(i));
			}
		}
		return nameList;
	}
}





