package com.sparkstart.app.content

import com.sparkstart.app.data.*

internal val supplyModule = Module(
    id = "supply",
    title = "Supply & Earthing (MEN)",
    emoji = "🏠",
    blurb = "How power reaches a building, and how the MEN earthing system keeps you alive.",
    color = 0xFF43A047,
    group = Group.CORE,
    lessons = listOf(
        lesson(
            "e1", "From power station to power point", "🗼", 6, Level.BEGINNER,
            page(
                "The journey",
                bullets(
                    "Power station makes electricity",
                    "Step-up transformers raise it to very high voltage for long-distance lines (less current = less loss)",
                    "Substations step it down",
                    "A street transformer drops it to 400/230 V",
                    "Service/consumer mains run into the building's main switchboard",
                    "The switchboard splits it into circuits (final subcircuits) for lights, power and appliances"
                ),
                Vis(Visual.TRANSFORMER, "Transformers change voltage by the turns ratio.")
            ),
            page(
                "The three main conductors",
                bullets(
                    "ACTIVE (A): the 'live' one at 230 V. This is the dangerous one.",
                    "NEUTRAL (N): the return path. Normally close to 0 V, but it carries current and can bite.",
                    "EARTH (E): the safety path. Normally carries NO current. It's only there for when something goes wrong."
                ),
                Hazard("A neutral is not 'safe'. If a neutral is broken, the load side of it can sit at full active voltage. Treat every conductor as live until proven dead."),
                quiz("Which conductor normally carries NO current?", 2, "Earth only carries current during a fault. That's its whole job.", "Active", "Neutral", "Earth", "All of them carry current")
            )
        ),
        lesson(
            "e2", "The MEN system", "🔗", 10, Level.BEGINNER,
            page(
                "Multiple Earthed Neutral",
                P("Australia uses the MEN system. At the main switchboard, the neutral bar and the earth bar are joined by the MEN LINK. The neutral is also earthed at the transformer and at other points along the network."),
                P("This gives fault current a low-resistance path back to the transformer, so the breaker trips fast."),
                Vis(Visual.MEN_FAULT, "Make a fault, then try removing the MEN link to see what goes wrong.")
            ),
            page(
                "Why it works",
                P("If an active touches a metal case, the current goes: case → earth wire → earth bar → MEN link → neutral → back to the transformer."),
                P("That path has very low resistance, so by Ohm's law the current is HUGE, and the circuit breaker trips almost instantly. The case is only live for a split second."),
                WhyNot("Why can't the fault current just go into the ground through the earth stake? Soil has far too much resistance. The current would be too small to trip the breaker, and the metal case would stay live. The MEN link gives it a metal path back."),
                Std("Section 5 (earthing arrangements)", "Section 5 of the Wiring Rules covers earthing arrangements, the MEN connection, the main earthing conductor and the electrode."),
                quiz("What does the MEN link connect?", 1, "It joins the neutral bar to the earth bar at the main switchboard.", "Active to neutral", "Neutral bar to earth bar", "Earth to the water pipe", "The meter to the main switch")
            ),
            page(
                "There's only ONE MEN link",
                P("The MEN connection is made at the main switchboard only. Sub-boards keep neutral and earth SEPARATE."),
                WhyNot("Why can't you join neutral and earth again at a sub-board? Normal neutral current would then share the earth conductors and metalwork. Earths would carry current all the time, RCDs would nuisance trip, and metal parts could rise in voltage."),
                Hazard("A missing or broken MEN link, or a broken neutral in the street, can leave metalwork and even water pipes live. Tingles from taps or showers are a classic sign. Report them immediately."),
                Deep("If the supply neutral breaks between the street and the house, the installation's neutral is only connected to earth through the electrode and soil. Unbalanced loads can then push the neutral voltage up, and earthed metalwork, including pipes, can become live. That's why a shock from a tap or shower is treated as urgent.")
            )
        ),
        lesson(
            "e3", "Earthing and bonding", "🌍", 7, Level.INTERMEDIATE,
            page(
                "Earthing vs bonding",
                bullets(
                    "Protective EARTHING: connecting exposed metal parts of equipment (cases, frames) to the earth system so faults trip the protection.",
                    "Equipotential BONDING: connecting metal things together (pipes, pool steel, shower bases in some cases) so they're at the same voltage, so there's no voltage difference to shock you."
                ),
                Tip("Shock needs a voltage DIFFERENCE. If everything you could touch is bonded together at the same voltage, current has no reason to flow through you.")
            ),
            page(
                "The main earth",
                P("The main earthing conductor runs from the main earth bar to the earth electrode (stake). Its resistance is tested as part of verification."),
                Std("Clauses 8.3.5 and 5.5.1.4", "Earth continuity testing includes the main earthing conductor and bonding conductors. In an MEN installation, the resistance from the earth electrode to the main earthing terminal must not exceed 0.5 Ω."),
                Hazard("Never disconnect a main earth or MEN link with the installation energised. Fault or neutral current could be flowing through it, and you could become the path."),
                quiz("What is bonding mainly for?", 3, "Bonding keeps metal parts at the same voltage so a person can't get a shock between them.", "Making lights brighter", "Reducing your bill", "Carrying neutral current", "Keeping metal parts at the same voltage")
            )
        )
    )
)

internal val protectionModule = Module(
    id = "protection",
    title = "Protection Devices",
    emoji = "🛡️",
    blurb = "Fuses, circuit breakers and RCDs: what trips, why, and what they can't protect you from.",
    color = 0xFFFB8C00,
    group = Group.CORE,
    lessons = listOf(
        lesson(
            "p1", "Fuses and circuit breakers", "🔌", 8, Level.BEGINNER,
            page(
                "What they protect",
                P("Fuses and circuit breakers protect the CABLE (and the installation) from too much current, which would overheat it and start a fire."),
                P("They protect against two things:"),
                bullets(
                    "OVERLOAD: a bit too much current for a while (too many heaters on one circuit)",
                    "SHORT CIRCUIT: a massive current instantly (active touching neutral or earth)"
                ),
                Vis(Visual.BREAKER)
            ),
            page(
                "Inside a breaker",
                bullets(
                    "Thermal part (bimetal strip): bends when it heats up. Handles overloads, and is slower.",
                    "Magnetic part (coil): snaps open on huge current. Handles shorts, and is instant."
                ),
                P("Breakers come with trip curves. B, C and D curves trip magnetically at roughly 3–5×, 5–10× and 10–20× their rating. C is common for general circuits. D suits big inrush loads like some motors and transformers."),
                WhyNot("Why can't you just put in a bigger breaker if it keeps tripping? The breaker is matched to the CABLE. A bigger breaker lets the cable overheat before tripping, which is a fire risk. Find out WHY it trips."),
                quiz("A circuit breaker mainly protects…", 1, "Breakers protect cables and equipment from overcurrent. They are NOT designed to save a person from a shock. That's the RCD's job.", "People from electric shock", "The cable from overheating", "The meter", "Against lightning")
            )
        ),
        lesson(
            "p2", "RCDs (safety switches)", "🦺", 10, Level.BEGINNER,
            page(
                "How an RCD works",
                P("A Residual Current Device compares the current going OUT on the active with what comes BACK on the neutral. If some is missing (leaking to earth, maybe through a person), it trips."),
                Vis(Visual.RCD, "Try all three scenarios. The third one is really important."),
                P("Personal-protection RCDs trip at 30 mA or less, fast enough to make a shock survivable in most cases.")
            ),
            page(
                "What an RCD can't do",
                WhyNot("Why doesn't an RCD protect you if you touch active AND neutral? The current goes in one hand and out the other, straight back on the neutral. Out still equals back, so the RCD sees nothing wrong. Only isolation protects you there."),
                bullets(
                    "Doesn't protect against overload or short circuit on its own (an RCBO = RCD + breaker in one does)",
                    "Doesn't stop the shock. It limits how long it lasts.",
                    "Must be tested: press the test button regularly, and trip-time test with an RCD tester"
                ),
                Deep("RCDs come in types (AC, A, F, B) depending on what kind of leakage current they can detect. Modern electronics, inverters, EV chargers and variable speed drives can create leakage that a basic Type AC RCD can't see properly, so the right type must be chosen. Check the equipment manufacturer's requirements and the Wiring Rules."),
                quiz("A person touches the active with one hand and the neutral with the other (feet insulated). Will a 30 mA RCD trip?", 1, "No. The current goes out and comes straight back on the neutral, so there's no imbalance for the RCD to detect.", "Yes, instantly", "No, because out = back", "Only if it's raining", "Only on three phase")
            ),
            page(
                "RCD rules in homes",
                Std("Clause 2.6.3.2.2 (domestic & residential, Australia)", "The 2018 Wiring Rules require 30 mA RCD protection on ALL final subcircuits in new domestic and residential installations, including things like cooktops, ovens, hot water and air-con. The well-known rules of no more than three final subcircuits per RCD and lighting circuits spread across RCDs still apply."),
                Std("WA Electricity Regulations 1947 (regs 12–16)", "In WA, homes being sold, rented or hired must have at least two RCDs protecting the socket-outlet and lighting final subcircuits, with no more than three final subcircuits per RCD and lighting circuits split between the RCDs. This is the minimum for existing homes. It's different from AS/NZS 3000:2018, which applies to new installations (WA: built after 1 January 2019). Source: WA Building and Energy."),
                WhyNot("Why can't you put every circuit on one RCD? One fault or nuisance trip would kill power to the whole house, including all the lights, so you'd be in the dark next to the fault. Spreading circuits (and lights) across several RCDs, or using RCBOs, keeps some power on.")
            )
        )
    )
)
