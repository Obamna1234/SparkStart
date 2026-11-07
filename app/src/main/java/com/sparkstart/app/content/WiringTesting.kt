package com.sparkstart.app.content

import com.sparkstart.app.data.*

internal val wiringModule = Module(
    id = "wiring",
    title = "Cables, Wiring & Circuits",
    emoji = "🔌",
    blurb = "Cable colours and types, switching, circuits, voltage drop and cable sizing.",
    color = 0xFF8E24AA,
    group = Group.CORE,
    lessons = listOf(
        lesson(
            "w1", "Cable colours", "🎨", 6, Level.BEGINNER,
            page(
                "Fixed wiring",
                bullets(
                    "ACTIVE: red is preferred for single phase (other colours are allowed, but never green/yellow, green or yellow)",
                    "NEUTRAL: black or light blue",
                    "EARTH: green/yellow (older installs may have plain green)",
                    "Three-phase actives: red, white, blue"
                ),
                Std("Section 3 (identification of cores)", "Core colour rules are in Section 3 of the Wiring Rules. Check the exact list of allowed and banned colours in your copy."),
                Hazard("NEVER trust colours alone in an existing installation. Someone before you may have used the wrong colour. Test, don't guess.")
            ),
            page(
                "Flexible cords (appliances and leads)",
                bullets(
                    "ACTIVE: brown",
                    "NEUTRAL: light blue",
                    "EARTH: green/yellow"
                ),
                Tip("Brown = active is an easy trap for beginners used to red. Cords and fixed wiring use different colour codes."),
                quiz("In a flexible cord, which colour is the active?", 1, "Brown is active in flexible cords. Blue is neutral, green/yellow is earth.", "Red", "Brown", "Blue", "Black"),
                quiz("Which colour must ONLY ever be used for earth?", 3, "Green/yellow is reserved for earth. Never use it for anything else.", "Black", "White", "Light blue", "Green/yellow")
            )
        ),
        lesson(
            "w2", "Switching: always break the active", "💡", 7, Level.BEGINNER,
            page(
                "Switch the active",
                Vis(Visual.SWITCH_ACTIVE, "Flip between the two and turn the switch off."),
                P("Single-pole switches must be in the ACTIVE so that when they're off, everything after them is dead."),
                WhyNot("Why can't you switch the neutral instead? The light still goes off, but the lampholder stays connected to 230 V through the active. The next person changing a globe gets a shock from a light that looks 'off'.")
            ),
            page(
                "Two-way and intermediate switching",
                P("Two-way switching lets you control a light from two places, like the top and bottom of stairs. Two 2-way switches are linked by two 'strapper' wires. Add intermediate switches between them for three or more places."),
                Tip("A switch loop's return conductor (the 'switched active') is live when the switch is on. Its colour may not be red, so test it."),
                Deep("In a two-way circuit the active goes to the 'common' of switch 1. Strappers connect the two switches' L1 and L2 terminals. The common of switch 2 feeds the light. Flip either switch and you swap which strapper is connected, which completes or breaks the path. Intermediate switches cross the strappers over.")
            )
        ),
        lesson(
            "w3", "Cable types and sizes", "🧵", 8, Level.INTERMEDIATE,
            page(
                "Common cables",
                bullets(
                    "TPS (thermoplastic sheathed), e.g. '2.5 twin and earth': the standard house cable",
                    "Orange circular: heavier-duty sheathed cable",
                    "Single insulated in conduit: commercial and industrial",
                    "SWA / armoured: underground and tough areas",
                    "Fire-rated cables: for safety services that must keep working in a fire",
                    "Flexible cords and trailing cables: anything that moves"
                ),
                P("Cable size (cross-sectional area, mm²) sets how much current it can carry. Common in houses: 1.5 mm² for lighting, 2.5 mm² for power, larger for ovens, cooktops and submains.")
            ),
            page(
                "What decides the size?",
                bullets(
                    "Current-carrying capacity: how much current before it overheats",
                    "How it's installed: in insulation, bunched, in conduit, buried (these change the rating)",
                    "Voltage drop: long runs need bigger cable",
                    "Fault current and earth fault-loop impedance",
                    "Temperature around it"
                ),
                Std("AS/NZS 3008.1.1", "Cable current ratings and voltage drop values come from AS/NZS 3008.1.1, used together with the Wiring Rules."),
                Vis(Visual.VOLTAGE_DROP),
                Std("Clause 3.6 (voltage drop)", "The commonly taught limit is 5% voltage drop from the point of supply to any point in the installation (11.5 V on 230 V). Check the clause and any special cases."),
                Hazard("Cable covered in ceiling insulation can't get rid of heat and its rating drops a lot. That's a common cause of overheating.")
            )
        ),
        lesson(
            "w4", "Circuits and the switchboard", "🗄️", 8, Level.INTERMEDIATE,
            page(
                "Final subcircuits and submains",
                bullets(
                    "Consumer mains: supply → main switchboard",
                    "Submains: main switchboard → distribution board (sub-board)",
                    "Final subcircuits: board → the actual lights, power points and appliances"
                ),
                P("Each final subcircuit has its own protection (breaker/RCBO) sized to its cable. Typical house circuits: lights, power points, oven, cooktop, hot water, air-con.")
            ),
            page(
                "Maximum demand",
                P("Maximum demand is how much current an installation will realistically draw at its busiest. It's used to size consumer mains, submains and the main switch. Not everything is on at once, so the rules allow for this."),
                Std("Appendix C (maximum demand)", "Maximum demand calculation methods and tables are in Appendix C of the Wiring Rules. You'll practise these a lot in your apprenticeship."),
                quiz("What is a 'final subcircuit'?", 0, "It's the circuit from the board out to the actual points (lights, outlets, appliances).", "A circuit from the board to the lights/outlets/appliances", "The cable from the street", "The cable between two boards", "The earth wire")
            )
        )
    )
)

internal val testingModule = Module(
    id = "testing",
    title = "Testing Equipment",
    emoji = "🧪",
    blurb = "Multimeters, testers, insulation resistance, continuity, loop impedance, RCD and clamp meters.",
    color = 0xFF00897B,
    group = Group.CORE,
    lessons = listOf(
        lesson(
            "t1", "Voltage testers & meter safety", "🩺", 7, Level.BEGINNER,
            page(
                "Choose the right tester",
                bullets(
                    "Two-pole voltage tester: the go-to for proving dead",
                    "Proving unit: a small known source to prove your tester works",
                    "Multimeter: volts, ohms, continuity (and sometimes amps)",
                    "Non-contact tester ('volt stick'): quick check only, NOT for proving dead"
                ),
                Std("AS/NZS 4836", "Testing for de-energised and the test-prove-test method are part of safe working practice. Follow your training and workplace procedure.")
            ),
            page(
                "CAT ratings",
                P("Meters and leads carry a CAT (category) rating and voltage, like CAT III 600 V. Higher categories are built to survive the bigger energy spikes found closer to the supply."),
                bullets(
                    "CAT II: things plugged into outlets",
                    "CAT III: fixed installation, distribution boards, sub-circuits",
                    "CAT IV: the origin (supply, mains, meters, outdoor lines)"
                ),
                Hazard("A cheap meter not rated for where you're testing can explode if there's a spike or you choose the wrong range. Check the meter, the leads and the fuses."),
                Tip("Before every use: check the case and leads for damage, check the probes have shrouds, and make sure the battery's OK.")
            )
        ),
        lesson(
            "t2", "Using a multimeter", "🔢", 10, Level.BEGINNER,
            page(
                "Try it",
                Vis(Visual.MULTIMETER, "Try every mode on every target. The red screens are the ones that hurt people."),
                P("Golden rules: pick the mode BEFORE touching the probes on. Volts can be measured live. Ohms and continuity only on DEAD circuits. Amps go in series, or better, use a clamp meter.")
            ),
            page(
                "Reading results",
                bullets(
                    "OL = 'open loop' / overload: no path (or the value is beyond the range)",
                    "Near 0 Ω = good continuous path",
                    "Check leads first: touch the probes together. It should read near 0 Ω or beep."
                ),
                quiz("Which mode should NEVER be used on a live circuit?", 2, "Resistance/continuity modes put the meter's own small voltage into the circuit. On a live circuit they read rubbish, can be damaged, and can be dangerous.", "AC volts", "DC volts", "Ohms / continuity", "Hold button")
            )
        ),
        lesson(
            "t3", "Insulation resistance tester", "🧲", 8, Level.INTERMEDIATE,
            page(
                "What it does",
                P("An insulation resistance (IR) tester, often called a 'megger', puts a high DC test voltage (usually 500 V for 230/400 V circuits) across the insulation and measures how much leaks through, in megohms (MΩ)."),
                Vis(Visual.INSULATION),
                Std("Clause 8.3.6 (insulation resistance)", "The commonly taught minimum is 1 MΩ between live conductors and earth when tested at 500 V DC for low-voltage circuits. There are special cases (e.g. some heating elements). Check the clause."),
            ),
            page(
                "Doing it safely",
                order(
                    "Order the steps for an insulation test on a circuit",
                    "Isolate and prove the circuit dead",
                    "Disconnect or unplug sensitive electronics and loads",
                    "Check the tester on its own (leads open, then shorted)",
                    "Test active and neutral (joined) to earth at 500 V DC",
                    "Record the result and discharge the circuit",
                    "Reconnect everything you disconnected"
                ),
                WhyNot("Why can't you megger with everything still plugged in? 500 V DC can destroy electronics (LED drivers, dimmers, smart switches, surge protectors), and connected loads give false low readings."),
                Hazard("The tester makes a high voltage. Don't touch the circuit while testing, and let long cables discharge afterwards because they can hold a charge.")
            )
        ),
        lesson(
            "t4", "Continuity, polarity & connections", "🔁", 9, Level.INTERMEDIATE,
            page(
                "Earth continuity",
                P("You prove every exposed metal part is solidly connected back to the main earth, using a low-resistance ohmmeter and usually a long trailing lead from the main earth bar."),
                Std("Section 8 (earthing continuity)", "Main earthing and bonding conductors: commonly taught max 0.5 Ω. Circuit protective earth conductors are checked against values in a Section 8 table based on cable size and length."),
                Tip("Zero (null) your leads first so their resistance isn't counted.")
            ),
            page(
                "Polarity and correct circuit connections",
                bullets(
                    "Polarity: actives connected to active terminals, switches in the active, active and neutral on the correct terminals of every outlet",
                    "Correct circuit connections: no mix-ups between circuits, no neutral shared with the wrong circuit, no active-to-earth swap"
                ),
                Hazard("Reversed polarity (active and neutral swapped) can leave appliance parts live when switched off, like the lamp example in the Wiring module."),
                faults(
                    "Isolate and prove dead",
                    "Use a continuity test between the active at the board and the active terminal at each point",
                    "Check switches break the active (continuity changes when you operate the switch)",
                    "Check no continuity between conductors of different circuits",
                    "Record results and fix anything wrong before energising"
                )
            )
        ),
        lesson(
            "t5", "Loop impedance & RCD testing", "⏱️", 8, Level.ADVANCED,
            page(
                "Earth fault-loop impedance",
                P("This checks that if a fault happened, the fault current would be big enough to trip the protection fast enough. It's measured live with a loop tester, by competent people only."),
                Vis(Visual.LOOP_IMPEDANCE),
                Std("Section 8 / Table 8.1", "Maximum earth fault-loop impedance values for each protective device are listed in a table in Section 8. Check your copy for the exact numbers.")
            ),
            page(
                "RCD testing",
                P("An RCD tester creates a controlled leakage to earth and measures how fast the RCD trips. Personal-protection RCDs must trip within the time limits for their type."),
                Ask("Exact trip times and test methods are in the Wiring Rules and AS/NZS 3017 (verification guidelines). Your RTO will teach the method they assess."),
                Vis(Visual.CLAMP, "A clamp meter is another handy tool for finding leakage and load current.")
            )
        ),
        lesson(
            "t6", "The test order (Section 8)", "📋", 6, Level.INTERMEDIATE,
            page(
                "Dead tests first, then live",
                order(
                    "Put the commonly taught verification order in sequence",
                    "Visual inspection",
                    "Continuity of the earthing system",
                    "Insulation resistance",
                    "Polarity",
                    "Correct circuit connections",
                    "Earth fault-loop impedance",
                    "RCD operation"
                ),
                Std("Section 8 (verification)", "The mandatory tests and their requirements are in Section 8 of the Wiring Rules. AS/NZS 3017 gives guidance on how to do them."),
                WhyNot("Why can't you do the RCD test first to save time? Live tests on an installation you haven't proven safe can make metalwork live or hide faults. If the earth is missing, a live test could electrify the very thing you're touching. Dead tests prove it's safe to liven.")
            ),
            page(
                "Fault-finding mindset",
                Vis(Visual.HALF_SPLIT),
                faults(
                    "Ask what happened. When did it stop? What changed? Did something trip?",
                    "Check the obvious: is there supply? Has protection tripped? Has a fuse blown?",
                    "Isolate and prove dead before touching anything",
                    "Use dead tests (continuity, insulation) to find opens and shorts",
                    "Halve the circuit to narrow down where the fault is",
                    "Fix the CAUSE, not just the symptom",
                    "Re-test before re-energising, then confirm it works"
                )
            )
        )
    )
)
