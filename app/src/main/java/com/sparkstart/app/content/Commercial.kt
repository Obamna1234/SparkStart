package com.sparkstart.app.content

import com.sparkstart.app.data.*

internal val commercialModule = Module(
    id = "commercial",
    title = "Commercial (Shops, Offices, Sites)",
    emoji = "🏢",
    blurb = "Distribution boards, three-phase, lighting control, emergency lights, safety services, test & tag, construction sites.",
    color = 0xFF546E7A,
    group = Group.INDUSTRY,
    lessons = listOf(
        lesson(
            "c1", "Commercial vs domestic", "🏙️", 7, Level.BEGINNER,
            page(
                "What's different",
                bullets(
                    "Mostly three-phase supply",
                    "A main switchboard (MSB) feeding several distribution boards (DBs) through submains",
                    "Cable tray, ladder and conduit in ceilings and risers",
                    "Lots of lighting circuits and controls",
                    "Mechanical services (air-con, fans, pumps), lifts, fire systems",
                    "Emergency and exit lighting"
                ),
                Vis(Visual.THREE_PHASE),
                Tip("Balance loads across the three phases. Unbalanced loads overload one phase and push current into the neutral.")
            )
        ),
        lesson(
            "c2", "Distribution boards (DBs)", "🗄️", 10, Level.BEGINNER,
            page(
                "Inside a DB",
                bullets(
                    "Main switch / isolator for the DB (fed from a submain)",
                    "Busbars (often a three-phase chassis) the breakers clip onto",
                    "Circuit breakers / RCBOs for each final subcircuit",
                    "Neutral bar(s) and earth bar (NO MEN link in a sub-board)",
                    "Sometimes contactors, timers or control gear",
                    "Escutcheon (inner cover) so you can't touch live parts",
                    "Circuit schedule / labelling"
                ),
                Std("AS/NZS 61439 + Section 2", "Switchboard construction falls under the AS/NZS 61439 series. Switchboard location, access and labelling are in Section 2 of the Wiring Rules.")
            ),
            page(
                "Hazards and rules",
                Hazard("Behind the escutcheon are bare live busbars carrying large fault currents, so arc flash risk is real. Removing covers on an energised board needs a risk assessment and authorisation."),
                WhyNot("Why can't you link neutral and earth in a DB 'because there's a spare terminal'? The MEN link belongs only at the main switchboard. A second link sends neutral current through earths and metalwork, and RCDs won't work properly."),
                WhyNot("Why can't you land two circuits' neutrals under one terminal? Neutrals must be identifiable with their own circuit so isolating one circuit isolates its neutral. Shared or mixed neutrals cause RCD trips and can leave a 'dead' circuit's neutral carrying current."),
                faults(
                    "Circuit dead: is the breaker tripped? Is the DB main switch on? Is the submain supply there?",
                    "Measure voltage at the DB incoming terminals (all three phases and neutral). Live testing is for competent people only.",
                    "Lost one phase in the DB: check upstream protection for that submain",
                    "Hot spots: loose busbar or breaker connections. Thermal imaging finds these.",
                    "Isolate before tightening or replacing anything"
                )
            )
        ),
        lesson(
            "c3", "Lighting control & emergency lights", "🚨", 8, Level.BEGINNER,
            page(
                "Lighting control",
                bullets(
                    "Contactors switch big banks of lights from a small control signal",
                    "Time clocks, motion sensors and daylight sensors save power",
                    "Building management systems or networked lighting (e.g. DALI) control lights digitally"
                ),
                Tip("A contactor is just a big switch operated by a coil. Check the coil voltage before you troubleshoot (e.g. 230 V or 24 V).")
            ),
            page(
                "Emergency and exit lighting",
                P("Emergency lights and exit signs have batteries and come on when normal power fails, so people can get out."),
                WhyNot("Why can't an emergency light run off the switched lighting circuit only? It needs an UNSWITCHED supply to keep its battery charged and to sense when power fails. If it's wired after the light switch, it thinks there's a blackout every time someone turns the lights off, and flattens its battery."),
                Std("AS/NZS 2293 series", "Emergency lighting and exit signs follow the AS/NZS 2293 series: Part 1 design and installation, Part 2 routine service and maintenance, and Part 3 the luminaires and exit signs. Includes regular testing and record-keeping."),
                faults(
                    "Emergency light fails its discharge test: suspect the battery first (they age)",
                    "Check it has a permanent unswitched supply",
                    "Check the charging indicator and the lamp/LED module",
                    "Record the fault and the repair in the logbook"
                )
            )
        ),
        lesson(
            "c4", "Test & tag and construction sites", "🏷️", 8, Level.INTERMEDIATE,
            page(
                "Test and tag",
                P("Portable appliances and leads are inspected and tested at set intervals and tagged with the date and next due date."),
                Std("AS/NZS 3760:2022", "In-service safety inspection and testing of electrical equipment, including test intervals based on the environment."),
                WhyNot("Why can't you tape up a damaged lead and keep using it? Tape doesn't restore the mechanical strength or insulation. The conductors may be damaged inside, and the earth may be broken. Damaged = tagged out and repaired or replaced.")
            ),
            page(
                "Construction sites",
                bullets(
                    "Temporary switchboards ('site boxes') with RCD protection",
                    "Leads kept off the ground (lead stands) and away from water and traffic",
                    "Frequent test and tag because the environment is harsh"
                ),
                Std("AS/NZS 3012:2019", "Electrical installations on construction and demolition sites."),
                Hazard("Leads run over by plant, cut by tools or lying in water are a big shock risk on construction sites.")
            )
        ),
        lesson(
            "c5", "Mechanical & safety services", "🔥", 9, Level.INTERMEDIATE,
            page(
                "Mechanical services",
                P("Air-con, ventilation fans, pumps and boilers are often fed from a mechanical services switchboard (MSSB), with VSDs and motor starters (see the Motors module)."),
                Vis(Visual.STAR_DELTA)
            ),
            page(
                "Safety services",
                P("Safety services are things that must keep working in a fire: fire pumps, smoke control fans, fire detection, evacuation systems, some lifts."),
                WhyNot("Why aren't fire services on the general main switch? Firefighters often shut off general power in a burning building. Safety services are arranged with their own clearly marked main switches and protected cabling so they keep running while everything else is off."),
                Std("Section 7 (safety services)", "Supply arrangements, cabling and protection for safety services are in Section 7. For example, a fire pump's main switch must be labelled 'IN THE EVENT OF FIRE DO NOT SWITCH OFF' (Clause 7.2.4.4), and fire pump protection must be sized so it won't trip during normal running. Look up the details rather than assuming normal rules apply."),
                Hazard("Isolating a safety service (fire pump, smoke fan) can leave a building unprotected. It must be coordinated with the building's fire systems people. Never isolate without permission.")
            )
        ),
        lesson(
            "c6", "Commercial fault-finding", "🔎", 10, Level.ADVANCED,
            page(
                "Problems you don't see at home",
                bullets(
                    "Hot neutrals: lots of LED drivers and computers create 'harmonic' currents that ADD UP in the neutral instead of cancelling",
                    "Nuisance RCD trips: many small leakage currents from IT equipment add up on one RCD",
                    "Phase imbalance: one phase overloaded",
                    "Loose connections on busbars and big terminals (find with thermal imaging)"
                ),
                Vis(Visual.CLAMP, "A clamp meter on each phase and the neutral quickly shows imbalance and hot neutrals."),
                faults(
                    "Clamp each phase and the neutral and compare",
                    "If the neutral current is high with balanced phases, suspect harmonics",
                    "For nuisance RCD trips, measure standing leakage with a leakage clamp and spread loads across more RCDs/RCBOs",
                    "Thermal-scan boards under load (authorised people) to find hot joints",
                    "Isolate before fixing. Re-test after."
                )
            )
        )
    )
)
