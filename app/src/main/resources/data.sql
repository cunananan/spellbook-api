INSERT INTO users (username, email, password, role)
VALUES	('radagon', 'radagon@goldenorder.gov', '$2a$10$zntd1NwbRnf5VRIV.EwyMOj0BBdFbG77FlfuSGl/NpkPJPaqWEt5K', 'ADMIN'),
		('mArgIt', 'king_morgott@mail.leyndell.org', '$2a$10$PmTkxznnQ5WpDPIdrJYcMegYLegGguiudvwhUYzaTY8nUK73TDasa', 'STAFF'),
		('m00n_queen', 'rennala.caria@raya-lucaria.edu', '$2a$10$.LTWffMeflMHkHL.iSP3oOW4Sg6dtWGg.wXXeCBJDucPEPjmOLg46', 'STAFF'),
		('goldenboi96', 'godrickRULZ1996@storm-mail.org', '$2a$10$oz82iRK5qCqzw3Q1kRMnVO0vuRRtrhJJ19oc9NeQaA7MEXFd/Hp5K', 'STAFF'),
		('teh_all_knowing', 'gideon_o@erdmail.com', '$2a$10$/J1NhFLQoi9.cs0cNV5p3u3FjzMLaaFvSHPWZSpphlapSMt7vSBGa', 'USER'),
		('daughterOfTheCosmos', 'sellen@raya-lucaria.edu', '$2a$10$gfqr/PIXD9sXWrqLJUyJ2ejN86o1RYKQVHMPPe4qqrttUZn8jBUN6', 'USER'),
		('xX_alexander_Xx', 'iron_fist_alex@erdmail.com', '$2a$10$kR2BgHEp5LP/cbcdO97HzuTR57dRPliYruT6PxRbeHCeB/nqhxybm', 'USER'),
		('malenia-blade-of-miquella', 'malenia-blade-of-miquella@haligmail.org', '$2a$10$W/gGpNEnwjHRvgL/9//E4u5K2HOYqeRdKyWsM3.TQ272DVF5Xe/NW', 'USER'),
		('trustypatches', 'kenneth.haight@erdmail.com', '$2a$10$XlRD4zL3yz4UwID.ZV5yxOvy6NY6koh/xVwtJkjwtUUNk6MDiWrXC', 'STAFF');



INSERT INTO spells (category, name, description, price, stock, type, cast_fp_cost, charge_fp_cost, slots_used, int_requirement, fai_requirement, arc_requirement)
VALUES	('SPELL', 'Crystal Burst', 'Fires a burst of glintstone crystal shards', 1000, 8, 'SORCERY', 14, 0, 1, 23, 0, 0),
		('SPELL', 'Rock Sling', 'Summons rocks from the earth and sends them flying', 500, 10, 'SORCERY', 18, 12, 1, 18, 0, 0),
		('SPELL', 'Freezing Mist', 'Releases cold mist before caster', 1800, 0, 'SORCERY', 20, 0, 1, 21, 0, 0),
		('SPELL', 'Scholar`s Armament', 'Enchants right-hand armament with magic damage', 1500, 3, 'SORCERY', 25, 0, 1, 12, 0, 0),
		('SPELL', 'Heal', 'Heals HP for caster and nearby allies', 850, 5, 'INCANTATION', 32, 0, 1, 0, 12, 0),
		('SPELL', 'Divine Fortification', 'Increases holy damage negation', 700, 0, 'INCANTATION', 20, 0, 1, 0, 10, 0),
		('SPELL', 'Lightning Spear', 'Hurls lightning spear before caster', 2100, 7, 'INCANTATION', 18, 36, 1, 0, 17, 0),
		('SPELL', 'Burn, O Flame!', 'Raises a series of flame pillars around caster', 4500, 1, 'INCANTATION', 30, 18, 1, 0, 27, 1),
		('SPELL', 'Oracle Bubbles', 'Launches several small magic bubbles', 400, 2, 'SORCERY', 12, 16, 1, 0, 15, 18),
		('SPELL', 'Bloodflame Blade', 'Enchants right-hand armament with bloodflame', 3200, 0, 'INCANTATION', 20, 0, 1, 0, 12, 10),
		('SPELL', 'Law of Regression', 'Heals all ailments and dispels all special effects', 1800, 2, 'INCANTATION', 55, 0, 1, 37, 0, 0),
		('SPELL', 'Ancient Death Rancor', 'Summons horde of vengeful spirits that chase down foes', 4400, 4, 'SORCERY', 24, 0, 1, 34, 24, 0),
		('SPELL', 'Rennala`s Full Moon', 'Incarnate a full moon and launch it at foes', 24000, 0, 'SORCERY', 55, 0, 2, 70, 0, 0),
		('SPELL', 'Flame of the Fell God', 'Summons raging fireball that explodes and sets the area ablaze', 27000, 1, 'INCANTATION', 50, 30, 2, 0, 41, 0),
		('SPELL', 'Comet Azur', 'Fires a tremendous comet within a starry torrent', 32000, 1, 'SORCERY', 40, 46, 3, 60, 0, 0);