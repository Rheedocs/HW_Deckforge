SET SQL_SAFE_UPDATES = 0;

USE deckforge;

INSERT INTO player (
    username, email, password,
    role, collection_visibility
) VALUES
      (
          'Admin',
          'admin@deckforge.dk',
          '$2a$10$dyP3gXQFSDuHPxXWxhmod.SGFZGWsGneD0lalz2UpgywiVfiJKPMu',
          'ADMIN',
          'PUBLIC'
      ),
      (
          'Con Galo',
          'goncalo@deckforge.dk',
          '$2a$10$dyP3gXQFSDuHPxXWxhmod.SGFZGWsGneD0lalz2UpgywiVfiJKPMu',
          'PLAYER',
          'PUBLIC'
      ),
      (
          'Mattam Ei',
          'mattias@deckforge.dk',
          '$2a$10$dyP3gXQFSDuHPxXWxhmod.SGFZGWsGneD0lalz2UpgywiVfiJKPMu',
          'PLAYER',
          'TRADE_ONLY'
      ),
      (
          'Wicky Icki',
          'nicki@deckforge.dk',
          '$2a$10$dyP3gXQFSDuHPxXWxhmod.SGFZGWsGneD0lalz2UpgywiVfiJKPMu',
          'PLAYER',
          'PRIVATE'
      ),
      (
          'Holger Bluetooth',
          'holger@deckforge.dk',
          '$2a$10$dyP3gXQFSDuHPxXWxhmod.SGFZGWsGneD0lalz2UpgywiVfiJKPMu',
          'PLAYER',
          'PUBLIC'
      ),
      (
          'testspiller',
          'test@deckforge.dk',
          '$2a$10$dyP3gXQFSDuHPxXWxhmod.SGFZGWsGneD0lalz2UpgywiVfiJKPMu',
          'PLAYER',
          'TRADE_ONLY'
      );

INSERT INTO card (
    name, card_type, color, set_name,
    rarity, rule_text, image_url
) VALUES
      (
          'Shivan Dragon', 'CREATURE', 'RED', 'Foundations',
          'UNCOMMON',
          'Flying. R: This creature gets +1/+0 until end of turn.',
          'https://cards.scryfall.io/normal/front/7/0/702c4781-670b-49ae-b511-90ed119841b0.jpg?1775600379'
      ),
      (
          'Black Lotus', 'ARTIFACT', 'COLORLESS', 'Vintage Masters',
          'MYTHIC_RARE',
          'T, Sacrifice this artifact: Add three mana of any one color.',
          'https://cards.scryfall.io/normal/front/b/d/bd8fa327-dd41-4737-8f19-2cf5eb1f7cdd.jpg?1614638838'
      ),
      (
          'Lightning Bolt', 'INSTANT', 'RED', 'Ravnica: Clue Edition',
          'COMMON',
          'Lightning Bolt deals 3 damage to any target.',
          'https://cards.scryfall.io/normal/front/7/7/77c6fa74-5543-42ac-9ead-0e890b188e99.jpg?1706239968'
      ),
      (
          'Forest', 'LAND', 'GREEN', 'Foundations',
          'COMMON',
          '(T: Add G.)',
          'https://cards.scryfall.io/normal/front/5/f/5f533364-0f91-4e49-aaeb-83c4c1f6d316.jpg?1777658419'
      ),
      (
          'Counterspell', 'INSTANT', 'BLUE', 'Duskmourn Commander',
          'UNCOMMON',
          'Counter target spell.',
          'https://cards.scryfall.io/normal/front/4/f/4f616706-ec97-4923-bb1e-11a69fbaa1f8.jpg?1751282477'
      ),
      (
          'Sol Ring', 'ARTIFACT', 'COLORLESS', 'Commander Masters',
          'UNCOMMON',
          'T: Add CC.',
          'https://cards.scryfall.io/normal/front/8/7/870ec754-a76c-40ea-9b81-81b3dca1f62c.jpg?1775940518'
      ),
      (
          'Wrath of God', 'SORCERY', 'WHITE', 'Commander Masters',
          'RARE',
          'Destroy all creatures. They can''t be regenerated.',
          'https://cards.scryfall.io/normal/front/5/3/537d2b05-3f52-45d6-8fe3-26282085d0c6.jpg?1697121198'
      ),
      (
          'Llanowar Elves', 'CREATURE', 'GREEN', 'Foundations',
          'COMMON',
          'T: Add G.',
          'https://cards.scryfall.io/normal/front/6/a/6a0b230b-d391-4998-a3f7-7b158a0ec2cd.jpg?1731652605'
      ),
      (
          'Giada, Font of Hope', 'CREATURE', 'WHITE',
          'Streets of New Capenna', 'RARE',
          'Flying, vigilance. Each other Angel you control enters '
              'the battlefield with an additional +1/+1 counter on it '
              'for each Angel you already control.',
          'https://cards.scryfall.io/normal/front/8/a/8ae6fc26-cfad-4da8-98d9-49c27c24d293.jpg?1730489128'
      ),
      (
          'Serra Angel', 'CREATURE', 'WHITE', 'Magic 2021',
          'UNCOMMON',
          'Flying, vigilance.',
          'https://cards.scryfall.io/normal/front/b/8/b8c5e74c-96e7-4a1f-93b7-14d776fe4b2d.jpg?1775599758'
      ),
      (
          'Path to Exile', 'INSTANT', 'WHITE',
          'Double Masters 2022', 'UNCOMMON',
          'Exile target creature. Its controller may search their '
              'library for a basic land card, put that card onto the '
              'battlefield tapped, then shuffle.',
          'https://cards.scryfall.io/normal/front/9/0/90b690f4-9647-4e67-b7cb-b2692ea149b1.jpg?1775940727'
      ),
      (
          'Swords to Plowshares', 'INSTANT', 'WHITE',
          'Double Masters 2022', 'UNCOMMON',
          'Exile target creature. Its controller gains life equal '
              'to its power.',
          'https://cards.scryfall.io/normal/front/6/8/68ec2aed-7662-48ae-ab25-04f74ece1e41.jpg?1775940857'
      ),
      (
          'Brainstorm', 'INSTANT', 'BLUE', 'Modern Horizons 2',
          'COMMON',
          'Draw three cards, then put two cards from your hand on '
              'top of your library in any order.',
          'https://cards.scryfall.io/normal/front/8/b/8beb987c-1b67-4a4e-ae71-58547afad2a0.jpg?1726284649'
      ),
      (
          'Cyclonic Rift', 'INSTANT', 'BLUE',
          'Double Masters 2022', 'RARE',
          'Return target nonland permanent you don''t control to '
              'its owner''s hand. Overload 6U.',
          'https://cards.scryfall.io/normal/front/d/f/dfb7c4b9-f2f4-4d4e-baf2-86551c8150fe.jpg?1702429366'
      ),
      (
          'Dark Ritual', 'INSTANT', 'BLACK', 'Anthologies',
          'COMMON',
          'Add BBB.',
          'https://cards.scryfall.io/normal/front/9/5/95f27eeb-6f14-4db3-adb9-9be5ed76b34b.jpg?1753711947'
      ),
      (
          'Demonic Tutor', 'SORCERY', 'BLACK',
          'Double Masters 2022', 'RARE',
          'Search your library for a card and put that card into '
              'your hand. Then shuffle.',
          'https://cards.scryfall.io/normal/front/a/2/a24b4cb6-cebb-428b-8654-74347a6a8d63.jpg?1763472867'
      ),
      (
          'Vampire Nighthawk', 'CREATURE', 'BLACK', 'Zendikar',
          'UNCOMMON',
          'Flying, deathtouch, lifelink.',
          'https://cards.scryfall.io/normal/front/7/a/7aff07f9-9528-4149-9af0-f4e3c66c9dc5.jpg?1775600339'
      ),
      (
          'Chaos Warp', 'INSTANT', 'RED',
          'Double Masters 2022', 'RARE',
          'The owner of target permanent shuffles it into their '
              'library, then reveals the top card of their library.',
          'https://cards.scryfall.io/normal/front/6/5/65ae241c-8955-4c97-8be6-7356fbee2195.jpg?1775941250'
      ),
      (
          'Goblin Guide', 'CREATURE', 'RED', 'Zendikar',
          'RARE',
          'Haste. Whenever Goblin Guide attacks, defending player '
              'reveals the top card of their library.',
          'https://cards.scryfall.io/normal/front/3/c/3c0f5411-1940-410f-96ce-6f92513f753a.jpg?1599706366'
      ),
      (
          'Cultivate', 'SORCERY', 'GREEN', 'Commander 2021',
          'COMMON',
          'Search your library for up to two basic land cards.',
          'https://cards.scryfall.io/normal/front/f/1/f1cc00f9-ae7b-4f7b-95f2-bc5c00e4bd72.jpg?1775941453'
      ),
      (
          'Kodama''s Reach', 'SORCERY', 'GREEN',
          'Commander 2021', 'COMMON',
          'Search your library for up to two basic land cards.',
          'https://cards.scryfall.io/normal/front/9/0/90c423cc-1264-4067-9c50-e7c88c68ef2d.jpg?1767731241'
      ),
      (
          'Arcane Signet', 'ARTIFACT', 'COLORLESS',
          'Commander Masters', 'COMMON',
          'T: Add one mana of any color in your commander''s color identity.',
          'https://cards.scryfall.io/normal/front/7/8/7811dd72-61b9-4067-ac20-cea153e625d2.jpg?1775940512'
      ),
      (
          'Command Tower', 'LAND', 'COLORLESS',
          'Commander Masters', 'COMMON',
          'T: Add one mana of any color in your commander''s color identity.',
          'https://cards.scryfall.io/normal/front/c/4/c46a217c-0ed2-4b3c-9a01-ee38d12d76f3.jpg?1775940525'
      ),
      (
          'Plains', 'LAND', 'WHITE', 'Foundations',
          'COMMON',
          '(T: Add W.)',
          'https://cards.scryfall.io/normal/front/2/4/24dc369c-020a-4115-a4bb-d60a44de64e3.jpg?1777658393'
      ),
      (
          'Island', 'LAND', 'BLUE', 'Foundations',
          'COMMON',
          '(T: Add U.)',
          'https://cards.scryfall.io/normal/front/7/3/739aaaac-c424-4ea7-a084-62a6fc0438b0.jpg?1777658399'
      ),
      (
          'Mountain', 'LAND', 'RED', 'Foundations',
          'COMMON',
          '(T: Add R.)',
          'https://cards.scryfall.io/normal/front/5/1/51acfb01-4b0b-48fc-9704-a9b4a1e43a23.jpg?1777658413'
      ),
      (
          'Swamp', 'LAND', 'BLACK', 'Foundations',
          'COMMON',
          '(T: Add B.)',
          'https://cards.scryfall.io/normal/front/c/5/c5f590a3-9993-4ac4-a93c-1beb44eda17b.jpg?1777658405'
      );

INSERT INTO player_card (
    player_id, card_id, quantity, for_trade
) VALUES
      -- Admin ejer bred demo-samling
      (1, 1, 4, TRUE),
      (1, 2, 1, FALSE),
      (1, 3, 4, TRUE),
      (1, 4, 40, FALSE),
      (1, 5, 4, FALSE),
      (1, 6, 4, TRUE),
      (1, 7, 4, FALSE),
      (1, 8, 4, FALSE),
      (1, 9, 4, FALSE),
      (1, 10, 4, FALSE),
      (1, 11, 4, FALSE),
      (1, 12, 4, FALSE),
      (1, 13, 4, FALSE),
      (1, 14, 4, FALSE),
      (1, 15, 4, FALSE),
      (1, 16, 4, FALSE),
      (1, 17, 4, FALSE),
      (1, 18, 4, TRUE),
      (1, 19, 4, TRUE),
      (1, 20, 4, FALSE),
      (1, 21, 4, FALSE),
      (1, 22, 4, FALSE),
      (1, 23, 4, FALSE),
      (1, 24, 40, FALSE),
      (1, 25, 40, FALSE),
      (1, 26, 40, FALSE),
      (1, 27, 40, FALSE),

      -- Con Galo
      (2, 1, 4, TRUE),
      (2, 3, 4, TRUE),
      (2, 4, 40, FALSE),
      (2, 6, 4, TRUE),
      (2, 8, 4, FALSE),
      (2, 18, 4, TRUE),
      (2, 19, 4, FALSE),
      (2, 20, 4, FALSE),
      (2, 21, 4, FALSE),
      (2, 22, 4, FALSE),
      (2, 23, 4, FALSE),
      (2, 24, 20, FALSE),
      (2, 25, 20, FALSE),
      (2, 26, 50, FALSE),
      (2, 27, 20, FALSE),

      -- Mattam Ei
      (3, 2, 4, FALSE),
      (3, 4, 30, FALSE),
      (3, 5, 4, TRUE),
      (3, 6, 4, FALSE),
      (3, 13, 4, TRUE),
      (3, 14, 4, FALSE),
      (3, 15, 4, TRUE),
      (3, 16, 4, FALSE),
      (3, 17, 4, FALSE),
      (3, 22, 4, FALSE),
      (3, 23, 4, FALSE),
      (3, 24, 30, FALSE),
      (3, 25, 50, FALSE),
      (3, 26, 20, FALSE),
      (3, 27, 50, FALSE),

      -- Wicky Icki
      (4, 4, 40, FALSE),
      (4, 7, 4, FALSE),
      (4, 8, 4, FALSE),
      (4, 9, 4, TRUE),
      (4, 10, 4, TRUE),
      (4, 11, 4, FALSE),
      (4, 12, 4, FALSE),
      (4, 20, 4, FALSE),
      (4, 24, 50, FALSE),

      -- Holger Bluetooth
      (5, 1, 4, TRUE),
      (5, 2, 1, FALSE),
      (5, 3, 4, FALSE),
      (5, 4, 40, FALSE),
      (5, 5, 4, TRUE),
      (5, 6, 4, FALSE),
      (5, 9, 4, TRUE),
      (5, 10, 4, FALSE),
      (5, 14, 1, FALSE),
      (5, 16, 1, FALSE),
      (5, 17, 4, FALSE),
      (5, 18, 4, FALSE),
      (5, 21, 4, TRUE),
      (5, 22, 4, FALSE),
      (5, 23, 4, FALSE),
      (5, 24, 40, FALSE),
      (5, 25, 50, FALSE),
      (5, 26, 40, FALSE),
      (5, 27, 40, FALSE),

      -- testspiller
      (6, 3, 4, TRUE),
      (6, 4, 40, FALSE),
      (6, 5, 4, FALSE),
      (6, 6, 4, TRUE),
      (6, 10, 4, FALSE),
      (6, 11, 4, FALSE),
      (6, 12, 4, TRUE),
      (6, 13, 4, FALSE),
      (6, 17, 4, FALSE),
      (6, 20, 4, FALSE),
      (6, 22, 4, FALSE),
      (6, 23, 4, FALSE),
      (6, 24, 50, FALSE),
      (6, 25, 40, FALSE),
      (6, 26, 30, FALSE),
      (6, 27, 40, FALSE);

INSERT INTO deck (
    player_id, name, format, visibility
) VALUES
      (1, 'Admin Arena Toolbox', 'CASUAL', 'PUBLIC'),
      (2, 'Goncalos Goblin Rush', 'STANDARD', 'PUBLIC'),
      (3, 'Mattias Mind Control', 'COMMANDER', 'PUBLIC'),
      (4, 'Nickis Angel Grove', 'CASUAL', 'PRIVATE'),
      (5, 'Holgers Shop Meta', 'STANDARD', 'PUBLIC'),

      -- Ekstra decks til test og fremlæggelse
      (1, 'Admin Dragon Council', 'COMMANDER', 'PUBLIC'),
      (1, 'Admin Lightning League', 'STANDARD', 'PUBLIC'),
      (1, 'Admin Draft Survival Kit', 'DRAFT', 'PUBLIC'),
      (2, 'Goncalos Command Zone Chaos', 'COMMANDER', 'PUBLIC'),
      (2, 'Goncalos Kitchen Table Burn', 'CASUAL', 'PUBLIC'),
      (3, 'Mattias Counterspell Clinic', 'STANDARD', 'PUBLIC'),
      (3, 'Mattias Booster Brainstorm', 'DRAFT', 'PUBLIC'),
      (4, 'Nickis Draft Angels', 'DRAFT', 'PRIVATE'),
      (5, 'Holgers Friday Night Pile', 'CASUAL', 'PUBLIC'),
      (5, 'Holgers Commander Counter', 'COMMANDER', 'PUBLIC'),
      (6, 'Testspillers Sofa Magic', 'CASUAL', 'PUBLIC'),
      (6, 'Testspillers Store Showdown', 'STANDARD', 'PUBLIC'),
      (6, 'Testspillers Legendary Pile', 'COMMANDER', 'PUBLIC');

INSERT INTO deck_card (
    deck_id, card_id, quantity
) VALUES
      -- Deck 1: Admin Arena Toolbox (casual)
      (1, 1, 2),
      (1, 3, 4),
      (1, 7, 1),
      (1, 6, 1),
      (1, 26, 10),

      -- Deck 2: Goncalos Goblin Rush (standard, 60 kort)
      (2, 1, 4),
      (2, 3, 4),
      (2, 6, 4),
      (2, 8, 4),
      (2, 18, 4),
      (2, 19, 4),
      (2, 20, 4),
      (2, 21, 4),
      (2, 22, 4),
      (2, 23, 4),
      (2, 4, 4),
      (2, 24, 4),
      (2, 25, 4),
      (2, 26, 4),
      (2, 27, 4),

      -- Deck 3: Mattias Mind Control (commander, 100 kort)
      (3, 2, 1),
      (3, 5, 1),
      (3, 6, 1),
      (3, 13, 1),
      (3, 14, 1),
      (3, 15, 1),
      (3, 16, 1),
      (3, 17, 1),
      (3, 22, 1),
      (3, 23, 1),
      (3, 4, 20),
      (3, 24, 18),
      (3, 25, 25),
      (3, 26, 10),
      (3, 27, 25),

      -- Deck 4: Nickis Angel Grove (casual)
      (4, 7, 1),
      (4, 8, 4),
      (4, 9, 1),
      (4, 10, 2),
      (4, 11, 2),
      (4, 20, 3),
      (4, 24, 10),
      (4, 4, 10),

      -- Deck 5: Holgers Shop Meta (standard, 60 kort)
      (5, 1, 4),
      (5, 3, 4),
      (5, 5, 4),
      (5, 6, 4),
      (5, 9, 4),
      (5, 10, 4),
      (5, 17, 4),
      (5, 18, 4),
      (5, 21, 4),
      (5, 23, 4),
      (5, 4, 4),
      (5, 24, 4),
      (5, 25, 4),
      (5, 26, 4),
      (5, 27, 4),

      -- Deck 6: Admin Dragon Council (commander, 100 kort)
      (6, 1, 1),
      (6, 3, 1),
      (6, 5, 1),
      (6, 6, 1),
      (6, 7, 1),
      (6, 18, 1),
      (6, 19, 1),
      (6, 20, 1),
      (6, 21, 1),
      (6, 22, 1),
      (6, 23, 1),
      (6, 4, 24),
      (6, 24, 20),
      (6, 25, 10),
      (6, 26, 25),
      (6, 27, 10),

      -- Deck 7: Admin Lightning League (standard, 60 kort)
      (7, 3, 4),
      (7, 5, 4),
      (7, 6, 4),
      (7, 8, 4),
      (7, 12, 4),
      (7, 13, 4),
      (7, 18, 4),
      (7, 19, 4),
      (7, 20, 4),
      (7, 22, 4),
      (7, 4, 4),
      (7, 24, 4),
      (7, 25, 4),
      (7, 26, 4),
      (7, 27, 4),

      -- Deck 8: Admin Draft Survival Kit (draft, 40 kort)
      (8, 10, 2),
      (8, 11, 2),
      (8, 12, 2),
      (8, 13, 2),
      (8, 20, 2),
      (8, 4, 8),
      (8, 24, 8),
      (8, 25, 8),
      (8, 26, 4),
      (8, 27, 4),

      -- Deck 9: Goncalos Command Zone Chaos (commander, 100 kort)
      (9, 1, 1),
      (9, 3, 1),
      (9, 6, 1),
      (9, 8, 1),
      (9, 18, 1),
      (9, 19, 1),
      (9, 20, 1),
      (9, 21, 1),
      (9, 22, 1),
      (9, 23, 1),
      (9, 4, 25),
      (9, 24, 10),
      (9, 25, 10),
      (9, 26, 35),
      (9, 27, 10),

      -- Deck 10: Goncalos Kitchen Table Burn (casual)
      (10, 1, 3),
      (10, 3, 4),
      (10, 18, 3),
      (10, 19, 3),
      (10, 6, 1),
      (10, 26, 16),

      -- Deck 11: Mattias Counterspell Clinic (standard, 60 kort)
      (11, 2, 4),
      (11, 5, 4),
      (11, 6, 4),
      (11, 13, 4),
      (11, 14, 4),
      (11, 15, 4),
      (11, 16, 4),
      (11, 17, 4),
      (11, 22, 4),
      (11, 23, 4),
      (11, 4, 4),
      (11, 24, 4),
      (11, 25, 4),
      (11, 26, 4),
      (11, 27, 4),

      -- Deck 12: Mattias Booster Brainstorm (draft, 40 kort)
      (12, 5, 3),
      (12, 13, 3),
      (12, 15, 2),
      (12, 17, 2),
      (12, 25, 15),
      (12, 27, 15),

      -- Deck 13: Nickis Draft Angels (draft, 40 kort)
      (13, 7, 1),
      (13, 9, 1),
      (13, 10, 3),
      (13, 11, 2),
      (13, 12, 2),
      (13, 8, 3),
      (13, 4, 14),
      (13, 24, 14),

      -- Deck 14: Holgers Friday Night Pile (casual)
      (14, 1, 1),
      (14, 3, 2),
      (14, 5, 2),
      (14, 9, 1),
      (14, 17, 2),
      (14, 21, 2),
      (14, 23, 1),
      (14, 6, 1),
      (14, 4, 8),
      (14, 25, 8),
      (14, 26, 8),

      -- Deck 15: Holgers Commander Counter (commander, 100 kort)
      (15, 1, 1),
      (15, 2, 1),
      (15, 5, 1),
      (15, 6, 1),
      (15, 9, 1),
      (15, 14, 1),
      (15, 16, 1),
      (15, 17, 1),
      (15, 21, 1),
      (15, 22, 1),
      (15, 23, 1),
      (15, 4, 20),
      (15, 24, 15),
      (15, 25, 25),
      (15, 26, 15),
      (15, 27, 24),

      -- Deck 16: Testspillers Sofa Magic (casual)
      (16, 3, 2),
      (16, 6, 1),
      (16, 12, 2),
      (16, 24, 8),
      (16, 25, 6),

      -- Deck 17: Testspillers Store Showdown (standard, 60 kort)
      (17, 3, 4),
      (17, 5, 4),
      (17, 6, 4),
      (17, 10, 4),
      (17, 11, 4),
      (17, 12, 4),
      (17, 13, 4),
      (17, 17, 4),
      (17, 22, 4),
      (17, 23, 4),
      (17, 4, 4),
      (17, 24, 4),
      (17, 25, 4),
      (17, 26, 4),
      (17, 27, 4),

      -- Deck 18: Testspillers Legendary Pile (commander, 100 kort)
      (18, 3, 1),
      (18, 5, 1),
      (18, 6, 1),
      (18, 11, 1),
      (18, 12, 1),
      (18, 13, 1),
      (18, 20, 1),
      (18, 22, 1),
      (18, 23, 1),
      (18, 4, 20),
      (18, 24, 30),
      (18, 25, 20),
      (18, 26, 10),
      (18, 27, 21);

INSERT INTO event (
    name, location, date,
    format, max_players, status
) VALUES
      (
          'Commander Clash i Næstved',
          'Deckforge Naestved',
          '2026-06-15',
          'COMMANDER',
          8,
          'UPCOMING'
      ),
      (
          'Standard Showdown Søndag',
          'Deckforge Naestved',
          '2026-06-20',
          'STANDARD',
          16,
          'UPCOMING'
      ),
      (
          'Casual Kort & Kaffe',
          'Deckforge Naestved',
          '2026-04-15',
          'CASUAL',
          12,
          'COMPLETED'
      ),
      (
          'Draft Night: Boosters og Bragging Rights',
          'Deckforge Naestved',
          '2026-06-25',
          'DRAFT',
          8,
          'UPCOMING'
      ),
      (
          'Fuldt Hus: Casual Chaos',
          'Deckforge Naestved',
          '2026-07-01',
          'CASUAL',
          2,
          'UPCOMING'
      ),
      (
          'Standard Sprint: Én Plads Tilbage',
          'Deckforge Naestved',
          '2026-07-05',
          'STANDARD',
          3,
          'UPCOMING'
      ),
      (
          'Commander Finale: Kongen af Bordet',
          'Deckforge Naestved',
          '2026-04-30',
          'COMMANDER',
          8,
          'COMPLETED'
      );

INSERT INTO event_registration (
    player_id, event_id, deck_id, registration_date
) VALUES
      -- Commander Clash i Næstved, event 1
      (1, 1, 6, '2026-05-01'),  -- Admin Dragon Council
      (3, 1, 3, '2026-05-02'),  -- Mattias Mind Control

      -- Casual Kort & Kaffe, event 3, afsluttet
      (1, 3, 1, '2026-04-10'),  -- Admin Arena Toolbox
      (4, 3, 4, '2026-04-10'),  -- Nickis Angel Grove
      (5, 3, 14, '2026-04-11'), -- Holgers Friday Night Pile

      -- Fuldt Hus: Casual Chaos, event 5, max 2
      (4, 5, 4, '2026-05-12'),  -- Nickis Angel Grove
      (6, 5, 16, '2026-05-12'), -- Testspillers Sofa Magic

      -- Standard Sprint: Én Plads Tilbage, event 6, max 3
      (2, 6, 2, '2026-05-13'),  -- Goncalos Goblin Rush
      (5, 6, 5, '2026-05-13'),  -- Holgers Shop Meta

      -- Commander Finale: Kongen af Bordet, event 7
      (1, 7, 6, '2026-04-20'),  -- Admin Dragon Council
      (3, 7, 3, '2026-04-20'),  -- Mattias Mind Control
      (5, 7, 15, '2026-04-21'); -- Holgers Commander Counter

INSERT INTO trade (
    proposer_id, receiver_id, status,
    created_at, expires_at
) VALUES
      -- Con Galo har sendt et forslag til Holger (Dine forslag)
      (2, 5, 'PENDING', '2026-05-25 09:00:00', '2026-06-13 09:00:00'),
      -- Con Galo har sendt endnu et forslag til Testspiller (Dine forslag)
      (2, 6, 'PENDING', '2026-05-24 14:00:00', '2026-06-13 14:00:00'),
      -- Mattam Ei har sendt forslag til Con Galo (Kræver handling)
      (3, 2, 'PENDING', '2026-05-25 11:00:00', '2026-06-13 11:00:00'),
      -- Holger har sendt forslag til Con Galo (Kræver handling)
      (5, 2, 'PENDING', '2026-05-24 16:00:00', '2026-06-13 16:00:00'),
      -- Con Galo og Testspiller afventer gennemførsel (Aktive bytter)
      (2, 6, 'ACCEPTED', '2026-05-22 10:00:00', '2026-06-13 10:00:00'),
      -- Mattam Ei og Con Galo gennemført (Historik)
      (3, 2, 'COMPLETED', '2026-05-15 09:00:00', '2026-05-16 09:00:00'),
      -- Con Galo afvist af Holger (Historik)
      (2, 5, 'DECLINED', '2026-05-10 10:00:00', '2026-05-11 10:00:00'),
      -- Con Galo annullerede selv et forslag (Historik)
      (2, 3, 'CANCELLED', '2026-05-05 12:00:00', '2026-05-06 12:00:00');

INSERT INTO trade_card (trade_id, player_card_id, role) VALUES
      -- Trade 1: Con Galo tilbyder Shivan Dragon, vil have Counterspell fra Holger
      (1, 28, 'PROPOSER'),
      (1, 81, 'RECEIVER'),
      -- Trade 2: Con Galo tilbyder Lightning Bolt, vil have Sol Ring fra Testspiller
      (2, 29, 'PROPOSER'),
      (2, 97, 'RECEIVER'),
      -- Trade 3: Mattam Ei tilbyder Counterspell, vil have Sol Ring fra Con Galo
      (3, 45, 'PROPOSER'),
      (3, 31, 'RECEIVER'),
      -- Trade 4: Holger tilbyder Shivan Dragon, vil have Chaos Warp fra Con Galo
      (4, 77, 'PROPOSER'),
      (4, 33, 'RECEIVER'),
      -- Trade 5: Con Galo tilbyder Sol Ring, vil have Lightning Bolt fra Testspiller
      (5, 31, 'PROPOSER'),
      (5, 96, 'RECEIVER'),
      -- Trade 6: Mattam Ei tilbyder Brainstorm, Con Galo tilbød Chaos Warp (COMPLETED)
      (6, 47, 'PROPOSER'),
      (6, 33, 'RECEIVER'),
      -- Trade 7: Con Galo tilbød Shivan Dragon, Holger afviste (DECLINED)
      (7, 28, 'PROPOSER'),
      (7, 87, 'RECEIVER'),
      -- Trade 8: Con Galo annullerede forslag til Mattam Ei (CANCELLED)
      (8, 29, 'PROPOSER'),
      (8, 48, 'RECEIVER');

INSERT INTO result (
    player_id, event_id, placement
) VALUES
      -- Casual Kort & Kaffe, event 3
      (4, 3, 1),
      (5, 3, 2),
      (1, 3, 3),

      -- Commander Finale: Kongen af Bordet, event 7
      (3, 7, 1),
      (1, 7, 2),
      (5, 7, 3);

SET SQL_SAFE_UPDATES = 1;