INSERT INTO player (username, email, password, role, collection_visibility) VALUES
('admin', 'admin@deckforge.dk', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lh', 'ADMIN', 'PUBLIC'),
('goncalo', 'goncalo@deckforge.dk', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lh', 'PLAYER', 'PUBLIC'),
('mattias', 'mattias@deckforge.dk', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lh', 'PLAYER', 'TRADE_ONLY'),
('nicki', 'nicki@deckforge.dk', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lh', 'PLAYER', 'PRIVATE'),
('holger', 'holger@deckforge.dk', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lh', 'PLAYER', 'PUBLIC'),
('testspiller', 'test@deckforge.dk', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lh', 'PLAYER', 'TRADE_ONLY');

INSERT INTO card (name, card_type, color, set_name, rarity, rule_text, image_url) VALUES
('Shivan Dragon', 'CREATURE', 'RED', 'Foundations', 'UNCOMMON', 'Flying. R: This creature gets +1/+0 until end of turn.', 'https://cards.scryfall.io/normal/front/7/0/702c4781-670b-49ae-b511-90ed119841b0.jpg'),
('Black Lotus', 'ARTIFACT', 'COLORLESS', 'Vintage Masters', 'MYTHIC_RARE', 'T, Sacrifice this artifact: Add three mana of any one color.', 'https://cards.scryfall.io/normal/front/b/d/bd8fa327-dd41-4737-8f19-2cf5eb1f7cdd.jpg'),
('Lightning Bolt', 'INSTANT', 'RED', 'Ravnica: Clue Edition', 'COMMON', 'Lightning Bolt deals 3 damage to any target.', 'https://cards.scryfall.io/normal/front/7/7/77c6fa74-5543-42ac-9ead-0e890b188e99.jpg'),
('Forest', 'LAND', 'GREEN', 'The Hobbit', 'COMMON', '(T: Add G.)', 'https://cards.scryfall.io/normal/front/5/f/5f533364-0f91-4e49-aaeb-83c4c1f6d316.jpg'),
('Counterspell', 'INSTANT', 'BLUE', 'Duskmourn Commander', 'UNCOMMON', 'Counter target spell.', 'https://cards.scryfall.io/normal/front/4/f/4f616706-ec97-4923-bb1e-11a69fbaa1f8.jpg'),
('Sol Ring', 'ARTIFACT', 'COLORLESS', 'Secrets of Strixhaven Commander', 'UNCOMMON', 'T: Add CC.', 'https://cards.scryfall.io/normal/front/8/7/870ec754-a76c-40ea-9b81-81b3dca1f62c.jpg'),
('Wrath of God', 'SORCERY', 'WHITE', 'Commander Masters', 'RARE', 'Destroy all creatures. They cannot be regenerated.', 'https://cards.scryfall.io/normal/front/5/3/537d2b05-3f52-45d6-8fe3-26282085d0c6.jpg'),
('Llanowar Elves', 'CREATURE', 'GREEN', 'Foundations', 'COMMON', 'T: Add G.', 'https://cards.scryfall.io/normal/front/6/a/6a0b230b-d391-4998-a3f7-7b158a0ec2cd.jpg');

INSERT INTO player_card (player_id, card_id, quantity, for_trade) VALUES
(2, 1, 2, TRUE),
(2, 3, 4, TRUE),
(2, 4, 6, FALSE),
(3, 2, 1, FALSE),
(3, 5, 3, TRUE),
(3, 6, 2, TRUE),
(4, 7, 1, FALSE),
(4, 8, 4, TRUE),
(5, 1, 1, TRUE),
(5, 3, 2, FALSE);

INSERT INTO deck (player_id, name, format, visibility) VALUES
(2, 'Goncalos Rod Aggro', 'STANDARD', 'PUBLIC'),
(3, 'Mattias Commander Deck', 'COMMANDER', 'PUBLIC'),
(4, 'Nickis Casual', 'CASUAL', 'PRIVATE'),
(5, 'Holgers Standard', 'STANDARD', 'PUBLIC');

INSERT INTO deck_card (deck_id, card_id, quantity) VALUES
(1, 1, 2),
(1, 3, 4),
(1, 7, 1);

INSERT INTO event (name, location, date, format, max_players, status) VALUES
('Commander Aften', 'Deckforge Naestved', '2026-06-15', 'COMMANDER', 8, 'UPCOMING'),
('Standard Turnering', 'Deckforge Naestved', '2026-06-20', 'STANDARD', 16, 'UPCOMING'),
('Casual Meetup', 'Deckforge Naestved', '2026-04-15', 'CASUAL', 12, 'COMPLETED'),
('Draft Night', 'Deckforge Naestved', '2026-06-25', 'DRAFT', 8, 'UPCOMING'),
('FNM Standard', 'Deckforge Naestved', '2026-06-12', 'STANDARD', 16, 'UPCOMING'),
('Commander Aften', 'Deckforge Odense', '2026-06-18', 'COMMANDER', 8, 'UPCOMING'),
('Draft Night', 'Deckforge Odense', '2026-06-28', 'DRAFT', 12, 'UPCOMING'),
('FNM Casual', 'Deckforge Odense', '2026-05-08', 'CASUAL', 16, 'COMPLETED'),
('Standard Turnering', 'Deckforge Aalborg', '2026-06-22', 'STANDARD', 32, 'UPCOMING'),
('Casual Meetup', 'Deckforge Aalborg', '2026-05-10', 'CASUAL', 12, 'COMPLETED'),
('Commander Weekend', 'Deckforge Aalborg', '2026-07-05', 'COMMANDER', 16, 'UPCOMING');

INSERT INTO event_registration (player_id, event_id, deck_id, registration_date) VALUES
(2, 1, 1, '2026-05-01'),
(3, 1, 2, '2026-05-02'),
(4, 3, 3, '2026-04-10'),
(5, 3, 4, '2026-04-10'),
(2, 3, 1, '2026-04-11');

INSERT INTO trade (proposer_id, receiver_id, status, created_at, expires_at) VALUES
(2, 3, 'PENDING', '2026-05-07 10:00:00', '2026-05-08 10:00:00'),
(3, 5, 'ACCEPTED', '2026-05-01 12:00:00', '2026-05-02 12:00:00'),
(4, 2, 'DECLINED', '2026-04-28 09:00:00', '2026-04-29 09:00:00');

INSERT INTO trade_card (trade_id, player_card_id, role) VALUES
(1, 1, 'PROPOSER'),
(1, 5, 'RECEIVER'),
(2, 6, 'PROPOSER'),
(2, 8, 'RECEIVER'),
(3, 7, 'PROPOSER'),
(3, 2, 'RECEIVER');

INSERT INTO result (player_id, event_id, placement) VALUES
(4, 3, 1),
(5, 3, 2),
(2, 3, 3);