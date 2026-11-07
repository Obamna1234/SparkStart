package com.sparkstart.app.content

import com.sparkstart.app.data.*

internal val domesticModule = Module(
    id = "domestic",
    title = "Domestic (Houses & Units)",
    emoji = "🏡",
    blurb = "Switchboards, lights and power, wet areas, big appliances, solar, and common house faults.",
    color = 0xFF7CB342,
    group = Group.INDUSTRY,
    lessons = listOf(
        lesson(
            "d1", "Tour of a house switchboard", "🗄️", 8, Level.BEGINNER,
            page(
                "What's in the meter box",
                bullets(
                    "Service fuse / service protection device: belongs to the network. Hands off.",
                    "Meter(s): the network's equipment, often sealed",
                    "Main switch: turns off the installation's circuits",
                    "RCDs and/or RCBOs: shock protection",
                    "Circuit breakers (MCBs): one per final subcircuit",
                    "Neutral bar and earth bar, joined by the MEN link",
                    "Main earthing conductor out to the earth stake",
                    "Circuit labels / schedule"
                ),
                Vis(Visual.MEN_FAULT, "The MEN link in the switchboard is what makes faults trip fast.")
            ),
            page(
                "The part that's ALWAYS live",
                Hazard("Turning off the main switch does NOT make the whole board dead. The consumer mains, service fuse, meter and the line side of the main switch stay live. Only the network can isolate those."),
                WhyNot("Why can't you just pull the service fuse yourself? It's network property, it's often sealed, it may be under load, and pulling it can cause an arc. In WA, network work on service equipment is controlled. Follow your employer's and the network's rules."),
                Hazard("Older meter boards can contain asbestos. Don't drill, cut or break old boards. Your employer must manage this under asbestos rules."),
                quiz("You turn off the main switch. Which part of the board is still live?", 2, "The line side of the main switch, the meter and the consumer mains stay live.", "Nothing at all", "Only the RCDs", "The meter, consumer mains and line side of the main switch", "Only the earth bar")
            )
        ),
        lesson(
            "d2", "Lights and power points", "💡", 8, Level.BEGINNER,
            page(
                "Power points (GPOs)",
                bullets(
                    "Most home outlets are 10 A. 15 A outlets have a wider earth pin, for bigger gear.",
                    "Double outlets usually still share one 10 A or 16/20 A circuit",
                    "Outlets have an earth pin. The earth must be connected and tested."
                ),
                Hazard("A double adaptor or powerboard doesn't add capacity. It just lets you overload one outlet more easily.")
            ),
            page(
                "Lights",
                bullets(
                    "Lights are switched in the active (remember the Wiring module)",
                    "LED downlights have drivers (small power supplies) that can fail and cause flicker",
                    "Downlights need the right clearance from ceiling insulation unless they're rated for contact (IC-rated)"
                ),
                WhyNot("Why can't you just push insulation over any downlight? Many fittings and drivers get hot. Covered, they can overheat and start a ceiling fire. Only fittings rated for contact with insulation can be covered."),
                faults(
                    "Light doesn't work: check globe/LED and driver first",
                    "Isolate and prove dead",
                    "Check for active at the switch, then the switched active at the light",
                    "Check the neutral continuity back to the board",
                    "Look for loose terminals in the switch and at the fitting",
                    "Re-test and confirm"
                ),
                quiz("Downlights flicker on and off when dimmed. Most likely cause?", 1, "Mismatched LED and dimmer, or a failing driver, is the classic cause. Check the dimmer and driver are compatible.", "The earth stake", "Dimmer and LED driver not compatible, or a failing driver", "Too much voltage drop in the street", "The meter")
            )
        ),
        lesson(
            "d3", "Bathrooms, outdoors and pools", "🛁", 8, Level.INTERMEDIATE,
            page(
                "Water + electricity = zones",
                P("Wet areas are split into ZONES around baths, showers and pools. The closer to the water, the stricter the rules on what equipment can go there and how well it's protected (IP rating)."),
                Std("Section 6 (damp situations)", "Zone sizes and what's allowed in each are in Section 6 of the Wiring Rules, with diagrams. Look them up. They're commonly examined."),
                WhyNot("Why can't you put a power point right next to a bath? Wet hands, wet floor, and a body in water make a perfect path for current. A plugged-in appliance falling in could be fatal. Distances and zones keep outlets out of reach of the water."),
                bullets(
                    "Outdoor gear needs a suitable IP rating (dust and water protection)",
                    "Pools and spas need equipotential bonding of conductive parts",
                    "RCD protection is a must"
                )
            ),
            page(
                "IP ratings",
                P("IP (Ingress Protection) has two numbers. The first is protection from solids and dust (0–6). The second is protection from water (0–8). IP56 = dust protected + strong water jets."),
                quiz("In 'IP65', what does the 5 mean?", 1, "The second digit is water protection. 5 = water jets.", "Dust protection", "Water protection (jets)", "Voltage rating", "Number of terminals")
            )
        ),
        lesson(
            "d4", "Big appliances", "🍳", 9, Level.INTERMEDIATE,
            page(
                "Dedicated circuits",
                bullets(
                    "Oven and cooktop: often their own circuits with bigger cables",
                    "Storage hot water: an element and thermostat on its own circuit (sometimes off-peak)",
                    "Heat pump hot water: a compressor (motor) inside",
                    "Air-con: an outdoor isolator next to the unit",
                    "EV chargers: high continuous load, so they need their own circuit and the right RCD type"
                ),
                Std("Clause 2.6.3.2.2", "In new homes, these circuits need 30 mA RCD protection too, under the 2018 rules. Check the clause."),
                WhyNot("Why does the air-con need an isolator outside? So whoever services the unit can isolate it right where they're working and see that it's off, instead of trusting someone not to flip a breaker inside.")
            ),
            page(
                "Hot water fault-finding",
                faults(
                    "No hot water: check the breaker/RCD and any off-peak timer or contactor",
                    "Isolate, lock, tag and prove dead at the board AND the isolator",
                    "Check the over-temperature cut-out (it may have tripped and need resetting, then find WHY)",
                    "Check element resistance. Open circuit = burnt out.",
                    "Insulation test the element to earth. A low reading means a leaking element, which also trips RCDs.",
                    "Check thermostat operation"
                ),
                Hazard("Hot water systems have stored hot water and pressure as well as electrical hazards. Follow the manufacturer's procedure. Plumbing parts are for licensed plumbers.")
            )
        ),
        lesson(
            "d5", "Solar and batteries at home", "☀️", 9, Level.ADVANCED,
            page(
                "More than one supply",
                P("A house with solar (and maybe a battery) has more than one source of supply: the grid, the solar panels (DC), and the battery (DC)."),
                Hazard("Solar panels make dangerous DC voltage whenever light hits them. You can't turn off the sun. Turning off the AC main switch does NOT make the panels or DC cables dead."),
                WhyNot("Why can't you work on the board just because the main switch is off? The inverter or battery may still be connected. Every source must be isolated using the shutdown procedure on the signage at the board."),
                Std("AS/NZS 4777, AS/NZS 5033, AS/NZS 5139", "Inverter connection, PV arrays and battery systems have their own standards. Installers need extra accreditation."),
                Tip("Inverters have 'anti-islanding': they shut down when the grid goes off so they don't back-feed the street. Don't rely on that for your safety though. Isolate properly.")
            )
        ),
        lesson(
            "d6", "Common house faults", "🔎", 10, Level.ADVANCED,
            page(
                "RCD keeps tripping",
                faults(
                    "Ask: does it trip straight away, randomly, or when something's switched on? When it rains?",
                    "Unplug everything on the affected circuits and reset. If it holds, plug things back in one by one to find a leaky appliance.",
                    "If it still trips: isolate and switch off all the circuit breakers on that RCD",
                    "Turn them on one at a time to find the circuit",
                    "Isolate that circuit and insulation test it to find the leak (often water in an outdoor light or outlet, or a damaged cable)",
                    "Fix it, re-test, and confirm the RCD holds and trips correctly when tested"
                ),
                WhyNot("Why can't you just bypass or swap out an RCD that 'keeps tripping'? It's tripping because it found leakage, possibly through a person next time. Remove it and you remove the protection that's doing its job.")
            ),
            page(
                "Tingles and flickers",
                Hazard("A tingle from a tap, shower or metal fence is URGENT. It can mean a broken neutral or a missing MEN/earth connection. Report it immediately and treat it as dangerous."),
                bullets(
                    "All lights flicker, some get brighter and some dimmer: suspect a loose or broken neutral (very dangerous on three-phase)",
                    "Half the house dead on a three-phase supply: likely lost a phase (check with the network)",
                    "Burning smell or warm switch: loose, high-resistance connection. Isolate and investigate."
                ),
                quiz("A homeowner says they get a tingle from the shower tap. What do you do?", 3, "Treat it as dangerous. It can mean a broken neutral or earthing fault. Report it immediately and follow your employer's emergency process.", "Tell them it's static", "Put tape on the tap", "Wait and see if it gets worse", "Treat it as urgent and report it straight away")
            )
        )
    )
)
