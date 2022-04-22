package com.revature.controllers;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.revature.exceptions.AccessDeniedException;
import com.revature.models.Spell.SpellType;
import com.revature.models.SpellDto;
import com.revature.models.User.UserRole;
import com.revature.services.AuthService;
import com.revature.services.SpellService;

@RestController
@RequestMapping("/spells")
public class SpellController {
	
	private SpellService ss;
	private AuthService as;
	private static final String ENDPOINT = "/spells";
	
	private static final Logger LOG = LoggerFactory.getLogger(SpellController.class);
	
	@Autowired
	public SpellController(SpellService ss, AuthService as) {
		super();
		this.ss = ss;
		this.as = as;
	}
	
	@GetMapping
	public ResponseEntity<List<SpellDto>> getSpells(@RequestHeader(name="Authorization", required=false) String token,
	                                                @RequestParam(name="search", required=false) String searchStr,
	                                                @RequestParam(name="type", required=false) String typeStr,
	                                                @RequestParam(name="priceCap", required=false) String priceCapStr,
	                                                @RequestParam(name="inStock", required=false) String inStockStr,
	                                                @RequestParam(name="intCap", required=false) String intCapStr,
	                                                @RequestParam(name="faiCap", required=false) String faiCapStr,
	                                                @RequestParam(name="arcCap", required=false) String arcCapStr)
	{
		setMDC(ENDPOINT, "GET", token);
		
		SpellType type = null;
		int priceCap = -1;
		Boolean inStock = null;
		int intCap = -1;
		int faiCap = -1;
		int arcCap = -1;
		boolean queriesAllNull = true;
		
		if (!StringUtils.isBlank(searchStr)) {
			queriesAllNull = false;
		}
		if (!StringUtils.isBlank(typeStr)) {
			type = spellTypeFromString(typeStr);
			if (type != SpellType.NOT_SET) queriesAllNull = false;
		}
		if (!StringUtils.isBlank(priceCapStr)) {
			priceCap = intFromString(priceCapStr);
			if (priceCap >= 0) queriesAllNull = false;
		}
		if (!StringUtils.isBlank(inStockStr)) {
			inStock = booleanFromString(inStockStr);
			if (inStock != null) queriesAllNull = false;
		}
		if (!StringUtils.isBlank(intCapStr)) {
			intCap = intFromString(intCapStr);
			if (intCap >= 0) queriesAllNull = false;
		}
		if (!StringUtils.isBlank(faiCapStr)) {
			faiCap = intFromString(faiCapStr);
			if (faiCap >= 0) queriesAllNull = false;
		}
		if (!StringUtils.isBlank(arcCapStr)) {
			arcCap = intFromString(arcCapStr);
			if (arcCap >= 0) queriesAllNull = false;
		}
		
		List<SpellDto> spells = (queriesAllNull)
		                            ? ss.getSpells()
		                            : ss.getSpellsByQuery(searchStr, type, priceCap, inStock, intCap, faiCap, arcCap);
		LOG.info("{} spells were returned", spells.size());
		return new ResponseEntity<>(spells, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<SpellDto> getSpellById(@RequestHeader(name="Authorization", required=false) String token,
	                                             @PathVariable("id") int id)
	{
		setMDC(ENDPOINT + "/" + id, "GET", token);
		
		SpellDto spell = ss.getSpellById(id);
		LOG.info("{} was returned", spell);
		return new ResponseEntity<>(spell, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<String> addSpell(@RequestHeader(name="Authorization", required=false) String token,
	                                       @RequestBody SpellDto newSpell)
	{
		setMDC(ENDPOINT, "POST", token);
		
		if (!as.authorizeRole(token, UserRole.STAFF, UserRole.ADMIN)) {
			throw new AccessDeniedException("Not authorized to add spells");
		}
		SpellDto spell = ss.addSpell(newSpell);
		LOG.info("{} was created", spell);
		return new ResponseEntity<>("New spell \"" + spell.name + "\" was added at index " + spell.id, HttpStatus.CREATED);
	}
	
	@PostMapping("/{id}/buy")
	public ResponseEntity<String> buySpell(@RequestHeader(name="Authorization", required=false) String token,
	                                       @PathVariable("id") int id,
	                                       @RequestParam(name="funds", defaultValue="0") String funds)
	{
		setMDC(ENDPOINT + "/" + id + "/buy", "POST", token);
		
		return null;
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<SpellDto> updateSpell(@RequestHeader(name="Authorization", required=false) String token,
	                                            @PathVariable("id") int id, @RequestBody SpellDto updates)
	{
		setMDC(ENDPOINT + "/" + id, "PUT", token);
		
		if (!as.authorizeRole(token, UserRole.STAFF, UserRole.ADMIN)) {
			throw new AccessDeniedException("Not authorized to modify spells");
		}
		updates.id = id;
		SpellDto spell = ss.updateSpell(updates);
		LOG.info("Spell #{} was updated to the following: {}", id, spell);
		return new ResponseEntity<>(spell, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteSpell(@RequestHeader(name="Authorization", required=false) String token,
	                                          @PathVariable("id") int id)
	{
		setMDC(ENDPOINT + "/" + id, "DELETE", token);
		
		if (!as.authorizeRole(token, UserRole.STAFF, UserRole.ADMIN)) {
			throw new AccessDeniedException("Not authorized to delete spells");
		}
		SpellDto spell = ss.deleteSpell(id);
		LOG.info("{} was deleted", spell);
		return new ResponseEntity<>("Deleted spell \"" + spell.name + "\"", HttpStatus.OK);
	}
	
	private void setMDC(String endpoint, String method, String token) {
		MDC.clear();
		MDC.put("endpoint", endpoint);
		MDC.put("method", method);
		MDC.put("user", as.extractUsernameFromToken(token));
		MDC.put("requestId", UUID.randomUUID().toString());
	}
	
	private int intFromString(String intStr) {
		try {
			return Integer.parseInt(intStr);
		} catch (NumberFormatException e) {
			LOG.debug("SpellController.intFromString() is catching exception: {}", e.getMessage());
			LOG.warn("User is passing bad argument through an integer param: {}", intStr);
			return -1;
		}
	}
	
	private Boolean booleanFromString(String boolStr) {
		if (boolStr.equalsIgnoreCase("true")) {
			return Boolean.TRUE;
		} else if (boolStr.equalsIgnoreCase("false")) {
			return Boolean.FALSE;
		} else {
			LOG.warn("User is passing bad argument through a boolean param: {}", boolStr);
			return null;
		}
	}
	
	private SpellType spellTypeFromString(String typeStr) {
		try {
			return SpellType.valueOf(typeStr.toUpperCase());
		} catch (IllegalArgumentException e) {
			LOG.debug("SpellController.spellTypeFromString() is catching exception: {}", e.getMessage());
			LOG.warn("User is passing bad argument through a SpellType param: {}", typeStr);
			return SpellType.NOT_SET;
		}
	}
}




