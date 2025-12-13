# language: nl

Functionaliteit: Als bio-engineer kan ik producten ontwikkelen en valideren

  Als een bio-engineer
  Wil ik producten kunnen ontwikkelen met correcte validatie
  Zodat alleen geldige producten worden toegevoegd aan mijn portfolio

  Achtergrond:
    Gegeven een bio-engineer "Ali Manera" met email "ali.manera@hotmail.com" en specialisatie "vriesdroog"

  # EP1: Ongeldig - null product
  Scenario: Een null product gooit een foutmelding
    Als de bio-engineer een null product probeert te ontwikkelen
    Dan komt er een foutmelding "Product mag niet null zijn"

  # EP2 + BVA: Geldige en ongeldige producten met grenswaarden
  Abstract Scenario: De bio-engineer ontwikkelt product "<naam>" met houdbaarheid <houdbaarheid> dagen en gewicht <gewicht> gram
    Als de bio-engineer product "<naam>" probeert te ontwikkelen met:
      | id            | <id>            |
      | naam          | <naam>          |
      | beschrijving  | <beschrijving>  |
      | houdbaarheid  | <houdbaarheid>  |
      | gewicht       | <gewicht>       |
      | batchCode     | <batchCode>     |
    Dan <resultaat>

  Voorbeelden: Geldige producten
    | id | naam                | beschrijving                  | houdbaarheid | gewicht | batchCode   | resultaat                                    |
    # EP2 - Normale waarde (midden in range)
    | 1  | Banana Chips        | Crispy freeze-dried banana    | 365          | 100     | BATCH-001   | is het product succesvol toegevoegd         |
    # BVA - shelfLife ondergrens (n)
    | 3  | Minimum Shelf Life  | Product met minimale houdbaarheid | 1       | 100     | BATCH-003   | is het product succesvol toegevoegd         |
    # BVA - shelfLife normaal
    | 4  | Normal Shelf Life   | Product met normale houdbaarheid  | 365     | 100     | BATCH-004   | is het product succesvol toegevoegd         |
    # BVA - shelfLife bovengrens (n)
    | 5  | Maximum Shelf Life  | Product met maximale houdbaarheid | 730     | 100     | BATCH-005   | is het product succesvol toegevoegd         |
    # BVA - weight ondergrens (n)
    | 8  | Minimum Weight      | Product met minimaal gewicht      | 365     | 10      | BATCH-008   | is het product succesvol toegevoegd         |
    # BVA - weight normaal
    | 9  | Normal Weight       | Product met normaal gewicht       | 365     | 100     | BATCH-009   | is het product succesvol toegevoegd         |
    # BVA - weight bovengrens (n)
    | 10 | Maximum Weight      | Product met maximaal gewicht      | 365     | 1000    | BATCH-010   | is het product succesvol toegevoegd         |

  Voorbeelden: Ongeldige producten - Grenswaarden overschreden
    | id | naam                | beschrijving                      | houdbaarheid | gewicht | batchCode   | resultaat                                              |
    # BVA - shelfLife ondergrens (n-1)
    | 2  | Invalid Short Life  | Product met te korte houdbaarheid | 0            | 100     | BATCH-002   | komt er een foutmelding "ShelfLife moet minimaal 1 dag zijn" |
    # BVA - shelfLife bovengrens (n+1)
    | 6  | Exceeded Shelf Life | Product met te lange houdbaarheid | 731          | 100     | BATCH-006   | komt er een foutmelding "ShelfLife mag niet meer dan 730 dagen zijn" |
    # BVA - weight ondergrens (n-1)
    | 7  | Too Light Product   | Product met te laag gewicht       | 365          | 9       | BATCH-007   | komt er een foutmelding "Weight moet minimaal 10 gram zijn" |
    # BVA - weight bovengrens (n+1)
    | 11 | Too Heavy Product   | Product met te hoog gewicht       | 365          | 1001    | BATCH-011   | komt er een foutmelding "Weight mag niet meer dan 1000 gram zijn" |
    # EXTRA TEST - Extreem zwaar product (test extreme waarde)
    | 12 | Super Heavy Product | Product veel te zwaar             | 365          | 2000    | BATCH-012   | komt er een foutmelding "Weight mag niet meer dan 1000 gram zijn" |
