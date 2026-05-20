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
      -- Con Galo (id 2) - rød aggro spiller
      (2, 1, 3, TRUE),   -- Shivan Dragon
      (2, 3, 4, TRUE),   -- Lightning Bolt
      (2, 18, 2, TRUE),  -- Chaos Warp
      (2, 19, 2, FALSE), -- Goblin Guide
      (2, 26, 4, FALSE), -- Mountain
      (2, 6, 1, TRUE),   -- Sol Ring
      (2, 22, 1, FALSE), -- Arcane Signet
      -- Mattam Ei (id 3) - blå/sort kontrol spiller
      (3, 2, 1, FALSE),  -- Black Lotus
      (3, 5, 3, TRUE),   -- Counterspell
      (3, 13, 4, TRUE),  -- Brainstorm
      (3, 14, 1, FALSE), -- Cyclonic Rift
      (3, 15, 2, TRUE),  -- Dark Ritual
      (3, 16, 1, FALSE), -- Demonic Tutor
      (3, 25, 4, FALSE), -- Island
      (3, 27, 4, FALSE), -- Swamp
      (3, 6, 1, FALSE),  -- Sol Ring
      -- Wicky Icki (id 4) - hvid/grøn spiller
      (4, 7, 1, FALSE),  -- Wrath of God
      (4, 8, 4, FALSE),  -- Llanowar Elves
      (4, 9, 1, TRUE),   -- Giada, Font of Hope
      (4, 10, 2, TRUE),  -- Serra Angel
      (4, 11, 2, FALSE), -- Path to Exile
      (4, 20, 3, FALSE), -- Cultivate
      (4, 24, 4, FALSE), -- Plains
      (4, 4, 4, FALSE),  -- Forest
      -- Holger Bluetooth (id 5) - mixed spiller
      (5, 1, 1, TRUE),   -- Shivan Dragon
      (5, 3, 2, FALSE),  -- Lightning Bolt
      (5, 5, 2, TRUE),   -- Counterspell
      (5, 9, 1, TRUE),   -- Giada, Font of Hope
      (5, 17, 2, FALSE), -- Vampire Nighthawk
      (5, 21, 2, TRUE),  -- Kodama's Reach
      (5, 23, 1, FALSE), -- Command Tower
      (5, 6, 1, FALSE),  -- Sol Ring
      -- testspiller (id 6) - til bytte test:
      (6, 3, 2, TRUE),   -- Lightning Bolt
      (6, 6, 1, TRUE),   -- Sol Ring
      (6, 12, 2, TRUE),  -- Swords to Plowshares
      (6, 5, 2, FALSE),  -- Counterspell
      (6, 24, 4, FALSE); -- Plains

INSERT INTO deck (
    player_id, name, format, visibility
) VALUES
      (1, 'Admin Test Deck', 'CASUAL', 'PUBLIC'),
      (2, 'Goncalos Rod Aggro', 'STANDARD', 'PUBLIC'),
      (3, 'Mattias Commander Deck', 'COMMANDER', 'PUBLIC'),
      (4, 'Nickis Casual', 'CASUAL', 'PRIVATE'),
      (5, 'Holgers Standard', 'STANDARD', 'PUBLIC');

INSERT INTO deck_card (
    deck_id, card_id, quantity
) VALUES
      -- Deck 1: Admin Test Deck (casual)
      (1, 1, 2),   -- Shivan Dragon
      (1, 3, 4),   -- Lightning Bolt
      (1, 7, 1),   -- Wrath of God
      (1, 6, 1),   -- Sol Ring
      (1, 26, 10), -- Mountain
      -- Deck 2: Goncalos Rod Aggro (standard)
      (2, 1, 3),   -- Shivan Dragon
      (2, 3, 4),   -- Lightning Bolt
      (2, 19, 2),  -- Goblin Guide
      (2, 18, 2),  -- Chaos Warp
      (2, 26, 10), -- Mountain
      (2, 6, 1),   -- Sol Ring
      -- Deck 3: Mattias Commander Deck (commander)
      (3, 5, 3),   -- Counterspell
      (3, 6, 1),   -- Sol Ring
      (3, 13, 4),  -- Brainstorm
      (3, 14, 1),  -- Cyclonic Rift
      (3, 15, 2),  -- Dark Ritual
      (3, 16, 1),  -- Demonic Tutor
      (3, 25, 15), -- Island
      (3, 27, 15), -- Swamp
      (3, 23, 1),  -- Command Tower
      -- Deck 4: Nickis Casual (casual)
      (4, 7, 1),   -- Wrath of God
      (4, 8, 4),   -- Llanowar Elves
      (4, 9, 1),   -- Giada, Font of Hope
      (4, 10, 2),  -- Serra Angel
      (4, 11, 2),  -- Path to Exile
      (4, 20, 3),  -- Cultivate
      (4, 24, 10), -- Plains
      (4, 4, 10),  -- Forest
      -- Deck 5: Holgers Standard (standard)
      (5, 1, 2),   -- Shivan Dragon
      (5, 3, 3),   -- Lightning Bolt
      (5, 5, 2),   -- Counterspell
      (5, 17, 2),  -- Vampire Nighthawk
      (5, 26, 8),  -- Mountain
      (5, 25, 8),  -- Island
      (5, 6, 1);   -- Sol Ring

INSERT INTO event (
    name, location, date,
    format, max_players, status
) VALUES
      (
          'Commander Aften',
          'Deckforge Naestved',
          '2026-06-15',
          'COMMANDER',
          8,
          'UPCOMING'
      ),
      (
          'Standard Turnering',
          'Deckforge Naestved',
          '2026-06-20',
          'STANDARD',
          16,
          'UPCOMING'
      ),
      (
          'Casual Meetup',
          'Deckforge Naestved',
          '2026-04-15',
          'CASUAL',
          12,
          'COMPLETED'
      ),
      (
          'Draft Night',
          'Deckforge Naestved',
          '2026-06-25',
          'DRAFT',
          8,
          'UPCOMING'
      );

INSERT INTO event_registration (
    player_id, event_id, deck_id, registration_date
) VALUES
      (2, 1, 2, '2026-05-01'),
      (3, 1, 3, '2026-05-02'),
      (4, 3, 4, '2026-04-10'),
      (5, 3, 5, '2026-04-10'),
      (2, 3, 2, '2026-04-11');

INSERT INTO trade (
    proposer_id, receiver_id, status,
    created_at, expires_at
) VALUES
      (
          2,
          3,
          'PENDING',
          '2026-05-07 10:00:00',
          '2026-05-08 10:00:00'
      ),
      (
          3,
          5,
          'ACCEPTED',
          '2026-05-01 12:00:00',
          '2026-05-02 12:00:00'
      ),
      (
          4,
          2,
          'DECLINED',
          '2026-04-28 09:00:00',
          '2026-04-29 09:00:00'
      );

INSERT INTO trade_card (
    trade_id, player_card_id, role
) VALUES
      (1, 1, 'PROPOSER'),
      (1, 5, 'RECEIVER'),
      (2, 6, 'PROPOSER'),
      (2, 8, 'RECEIVER'),
      (3, 7, 'PROPOSER'),
      (3, 2, 'RECEIVER');

INSERT INTO result (
    player_id, event_id, placement
) VALUES
      (4, 3, 1),
      (5, 3, 2),
      (2, 3, 3);

SET SQL_SAFE_UPDATES = 1;