package com.sparkstart.app.content

import com.sparkstart.app.data.*

internal val ugFansPumpsModule = Module(
    id = "ug_fanspumps",
    title = "Vent Fans & Dewatering Pumps",
    emoji = "🌬️",
    blurb = "Testing and servicing fans and pumps: earth continuity, IR, rotation, timers, greasing, submersible and positive-displacement pumps.",
    color = 0xFF0277BD,
    group = Group.UNDERGROUND,
    lessons = listOf(
        lesson(
            "fan1", "Testing a vent fan", "🌀", 10, Level.INTERMEDIATE,
            page(
                "When and what",
                P("Fans are tested before going into service and at regular intervals (e.g. 3-monthly), with the fan starter isolated, locked and tagged:"),
                order(
                    "Order the fan electrical tests",
                    "Visual: cables undamaged, no armour exposed, glands secure, labels at both ends",
                    "Earth continuity starter to motor frame (≤ 0.5 Ω), with the earth disconnected at the starter",
                    "Reconnect the earth and check armour continuity",
                    "Insulation resistance: supply cable and each stage, phase to phase and phase to earth",
                    "Motor winding resistances across the phases",
                    "Check all connections are tight",
                    "De-isolate and test the earth leakage protection and starter circuits/timers"
                ),
                Hazard("If air flowing through the fan is turning it, the motor acts like a generator and your IR readings can be wrong. A spinning motor can also make voltage on its terminals."),
                WhyNot("Why disconnect the earth at the starter for the earth continuity test? Otherwise your meter also measures through other parallel earth paths (armour, other cables, steelwork), so a broken fan earth could still read 'fine'. Disconnecting one end means you measure ONLY that earth.")
            )
        ),
        lesson(
            "fan2", "Two-stage fan rotation", "🔄", 8, Level.INTERMEDIATE,
            page(
                "Get the direction right",
                Vis(Visual.FAN_ROTATION),
                bullets(
                    "Stage 1 = the incoming stage",
                    "Test each stage ALONE by isolating the other (its breaker, selector switch or run relay)",
                    "Jog it and watch the vent bag: inflates = right, deflates = backwards",
                    "Wrong? Isolate, prove dead, swap TWO phases, then re-test",
                    "Finally run both together, standing where you can hit STOP"
                ),
                quiz("Stage 2 of a fan deflates the bag when jogged on its own. What's the fix?", 1, "Isolate, prove dead, and swap any two phases on stage 2's motor, then jog it again to confirm.", "Swap all three phases", "Isolate, prove dead and swap two phases on stage 2", "Run both stages together", "Turn the fan around")
            )
        ),
        lesson(
            "fan3", "Fan timers and servicing", "⏱️", 9, Level.INTERMEDIATE,
            page(
                "Why fans start slowly",
                bullets(
                    "PULSE START: short bursts (e.g. 1 s on, 6 s off, about 4 times, around 28 s) fill the vent bag gently. More cycles may be needed for very long bag runs.",
                    "SECOND-STAGE DELAY (e.g. about 40 s): starts stage 2 after stage 1, so both motors don't hit the supply at once",
                    "DELAY START: holds fans off for a time around firing (blasting) to protect the vent bag. The time is set per site."
                ),
                WhyNot("Why not just start the fan at full speed straight away? An empty vent bag hit with full airflow can blow off the fan, split at joins or tear at worn spots, and that cuts ventilation to the heading. Pulsing inflates it gently."),
                Hazard("A contactor pulsing too much causes nuisance overload trips and wears the contactor out early. Settings matter.")
            ),
            page(
                "Mechanical service",
                bullets(
                    "Tell people first: stopping a fan affects the air in that area",
                    "Isolate at the starter's point of supply",
                    "Check fan tails are secured to the terminal-box guard (no strain on the glands)",
                    "Housing and silencer bolts, intake mesh, cracks, seals, corrosion",
                    "Fan mounts, SAFETY CHAINS and shackles in good order (photograph them for the records)",
                    "Grease bearings to the OEM schedule. Typical figures: 2-pole motors around 100 g per bearing every 1000 h, bigger 4-pole bearings around 130 g every 1500 h."
                ),
                Tip("Working at height on a fan means checking your harness and its test date before you go up.")
            )
        ),
        lesson(
            "pump1", "Submersible pumps", "💧", 10, Level.INTERMEDIATE,
            page(
                "Testing before service",
                bullets(
                    "Cable undamaged and the correct reeling/trailing type",
                    "Restrained plug not cracked, correct pin configuration, clean and dry inside",
                    "Earth continuity plug earth → motor frame ≤ 0.5 Ω",
                    "Pilot diode test across pilot and earth at the plug",
                    "Insulation resistance of the cable and motor",
                    "Motor winding resistances",
                    "Plug in, energise, test the protection devices",
                    "Check ROTATION: on start, the pump kicks (reacts) in a set direction shown by the manufacturer"
                ),
                Vis(Visual.DIODE_TEST)
            ),
            page(
                "Inspecting the pump",
                bullets(
                    "Work on a solid bench or flat ground. Chock the pump so it can't roll.",
                    "Rated lifting gear, nobody under a suspended load",
                    "Bolts tight, lifting handles/chains OK, casing not dented or cracked",
                    "WATER in the inspection chamber? Check the inspection screw and its O-ring, and the cable entry for leaks",
                    "OIL in the inspection chamber? The inner mechanical seal is probably damaged.",
                    "Oil chamber sample: heavily emulsified (creamy) or water settled out means change the oil",
                    "Adjust the impeller to the suction plate as it wears"
                ),
                quiz("You open a submersible pump's inspection chamber and find oil in it. Likely cause?", 2, "Oil getting into the inspection chamber points to the inner mechanical seal leaking.", "Normal", "Too much grease", "Damaged inner mechanical seal", "Wrong rotation")
            )
        ),
        lesson(
            "pump2", "Positive displacement (mono) pumps", "⚙️", 8, Level.INTERMEDIATE,
            page(
                "Never run them dry",
                P("A positive displacement pump has a steel rotor in a rubber stator. The water flowing through keeps it lubricated and cool. With no flow it heats up FAST and fails."),
                bullets(
                    "Low-flow switch/sensor in the discharge: stops the pump if flow drops",
                    "Low-level float cut-off: stops it before the sump runs dry",
                    "Motor overload: trips if the discharge is blocked",
                    "Flow meters (often with low-flow protection)",
                    "Some have stator thermistors to spot wear early and plan repairs"
                ),
                Hazard("Running dry or 'dead-headed' (against a closed valve) quickly destroys the stator and can burn drive belts. After maintenance, CHECK every valve is back in the operating position."),
                faults(
                    "Pump trips on overload: blocked or closed discharge? seized rotor? check current on all phases",
                    "Pump stops on low flow: valve shut, blockage, worn stator, air in suction",
                    "Won't start: float/level control, remote start/stop, control circuit, protection trips",
                    "Electrical tests as for fans: earth ≤ 0.5 Ω (earth disconnected at starter), IR, winding resistance, rotation"
                )
            )
        )
    )
)

internal val ugWorkshopModule = Module(
    id = "ug_workshop",
    title = "Portable Gear, Welders & Ladders",
    emoji = "🧰",
    blurb = "Test and tag, welders and VRDs, ladders, portable tools underground, and moving substations.",
    color = 0xFF757575,
    group = Group.UNDERGROUND,
    lessons = listOf(
        lesson(
            "ws1", "Test and tag", "🏷️", 10, Level.BEGINNER,
            page(
                "The checks",
                order(
                    "Order the test-and-tag steps",
                    "Isolate and unplug the appliance",
                    "Visual and physical inspection",
                    "Earth continuity (Class I)",
                    "Insulation resistance at 500 V DC",
                    "RCD test (power boards with RCDs)",
                    "Fill in the tag, the test sheet and the register"
                ),
                bullets(
                    "Earth continuity: plug earth pin to exposed metal ≤ 1 Ω (minus lead resistance)",
                    "Insulation resistance: actives and neutral joined, to earth or exposed metal, switch ON, ≥ 1 MΩ",
                    "Portable RCDs: trip at 30 mA within 40 ms (Type II)",
                    "Double insulated (Class II) gear has the square-inside-a-square symbol, so there's no earth test, just IR to accessible metal"
                ),
                Hazard("Never file the earth pin down on a 15 A plug to make it fit a 10 A socket. That defeats the protection the bigger earth pin gives."),
                Std("AS/NZS 3760", "In-service safety inspection and testing. Intervals depend on the environment. Underground and construction areas are about every 3 months.")
            ),
            page(
                "Tags and intervals",
                bullets(
                    "Colour-coded quarterly tags (outside NSW): RED Dec–Feb, GREEN Mar–May, BLUE Jun–Aug, YELLOW Sep–Nov",
                    "Other colours are used for yearly, 2-yearly and 5-yearly intervals",
                    "Tag shows: tag number, owner, test date, next test date, tester's name and licence number",
                    "Failed gear: out-of-service tag, repaired or destroyed, removed from the register",
                    "Braided/screened leads in high-traffic areas, with the braid earthed at both ends"
                ),
                WhyNot("Why can't you wrap insulation tape around a nicked lead sheath and keep using it? Tape isn't a sheath. It doesn't restore mechanical protection or insulation, and it hides damage. The lead fails the visual check."),
                quiz("Which test-tag colour is used for tests done in March–May?", 1, "Green for March to May. Red Dec–Feb, Blue Jun–Aug, Yellow Sep–Nov.", "Red", "Green", "Blue", "Yellow")
            )
        ),
        lesson(
            "ws2", "Portable tools underground", "🔨", 6, Level.BEGINNER,
            page(
                "Battery first",
                bullets(
                    "Battery-powered tools are preferred everywhere, especially underground",
                    "Mains tools must be on RCD-protected outlets (30 mA; 10 mA preferred underground)",
                    "Domestic-type and plain double-insulated tools are generally unsuitable underground",
                    "Leads over about 3 m: screened, with the screen earthed",
                    "Current test tag, no damage, proper plugs and sockets (industrial, around IP56 in workshops)"
                ),
                WhyNot("Why prefer a 10 mA RCD underground when 30 mA is the normal limit? It's wet, people stand in water, and current flows more easily through a wet body. A more sensitive RCD trips at a lower current, giving more safety margin.")
            )
        ),
        lesson(
            "ws3", "Welders and VRDs", "🔥", 9, Level.INTERMEDIATE,
            page(
                "Monthly checks (de-energised)",
                bullets(
                    "Gas bottles OFF, welder unplugged or isolated and locked",
                    "Plug, supply lead and lead entry undamaged, current test tag",
                    "Inside: no damage, overheating or dust/swarf build-up (vacuum or dry rag)",
                    "Earth continuity plug earth → any exposed metal (and wire feeder) ≤ 1 Ω",
                    "IR at 500 V DC with circuit boards disconnected and jumpers across inputs/outputs: e.g. input to frame ≥ 1 MΩ, input to output ≥ 10 MΩ",
                    "Welding leads and handpiece: no cuts, burns or exposed conductors"
                ),
                Hazard("Welders contain dangerous voltages and stored energy inside. Only qualified electrical workers open them.")
            ),
            page(
                "Voltage Reduction Device (VRD)",
                P("A VRD keeps the welder's no-load output voltage at a SAFE level (typically around 12 V; it must be under 35 V DC / 25 V AC per AS 1674.2). When the electrode touches the work (below about 200 Ω) it switches to full welding output."),
                bullets(
                    "VRD tester: check the tester's resistances first (e.g. about 170–200 Ω, and 35–45 Ω with the button pressed)",
                    "Live test with a qualified welder operating it, wearing 1000 V gloves and outers",
                    "Electrode on the strike point: no more than 35 V DC",
                    "Press the button: no more than the welder's open-circuit voltage",
                    "Some brands ship with the VRD DISABLED, so check it's on"
                ),
                WhyNot("Why does a welder need a VRD underground? A welder's open-circuit voltage can be high enough to give a dangerous shock to a sweaty welder kneeling in a wet drive. The VRD keeps it low until you actually strike an arc."),
                Tip("Engine-driven welder-generators: preferably no socket outlet. If there is one, follow the generator rules (one Class II appliance without an earth stake; an earth stake and RCD if more than one appliance).")
            )
        ),
        lesson(
            "ws4", "Ladders", "🪜", 5, Level.BEGINNER,
            page(
                "Pick and use them right",
                bullets(
                    "FIBREGLASS (non-conductive) ladders for electrical work",
                    "Right type and height, within its load rating (you + tools)",
                    "Inspect: rails, steps, feet, braces, rivets, labels, no splinters or fraying",
                    "Flat, stable base, test before climbing, one person at a time",
                    "Three points of contact always; don't lean out or stand above the marked top step",
                    "Extension ladders: about 1 m above the landing, tied off, 1:4 angle",
                    "Don't carry tools up. Use a bag and rope.",
                    "Inspected and tagged quarterly (same colour seasons)"
                ),
                quiz("Why fibreglass ladders for electrical work?", 0, "Fibreglass doesn't conduct, so the ladder can't become a path to earth if you touch something live.", "They don't conduct electricity", "They're lighter", "They're cheaper", "They're taller")
            )
        ),
        lesson(
            "ws5", "Moving a substation", "🚛", 7, Level.ADVANCED,
            page(
                "Heavy and high-risk",
                bullets(
                    "Risk assessment and JHA with everyone involved; only essential people present",
                    "Pre-start the tool carrier (IT) and moving device: drawbar, wheels, coupling points, pins, clips, plates",
                    "Rated chains and shackles (e.g. 10 t minimum) with current inspection tags",
                    "Operator stays at the controls the WHOLE time; spotter in a safe position",
                    "Lift the skid onto cribbing blocks to fit the wheels. NOBODY under the suspended load.",
                    "Bolts to spec (e.g. grade 8.8 torqued to the stated value)",
                    "Safety chains between the device and the skid in case it uncouples",
                    "Camera on the skid tested before tramming; spotters in light vehicles; constant radio contact"
                ),
                Tip("Tramming in reverse puts the IT between the substation and decline traffic. If it uncouples, the machine acts as a barrier.")
            )
        )
    )
)

internal val ugIncidentsModule = Module(
    id = "ug_incidents",
    title = "Shocks, Incidents & Records",
    emoji = "🚑",
    blurb = "What to do after an electric shock, machine contact with power, cable damage, live-work rules, and why the records matter.",
    color = 0xFFB71C1C,
    group = Group.UNDERGROUND,
    lessons = listOf(
        lesson(
            "inc1", "Electric shock procedure", "🚑", 8, Level.BEGINNER,
            page(
                "Every shock, every time",
                order(
                    "Order the electric shock response",
                    "Make it safe: isolate the power before touching the person",
                    "DRSABCD: Danger, Response, Send for help, Airway, Breathing, CPR, Defibrillator",
                    "Get them to the medical centre for an ECG (they must NOT drive themselves)",
                    "Preserve the scene",
                    "Report and investigate",
                    "Release the scene only with the right permission"
                ),
                Hazard("A shock can cause a dangerous irregular heartbeat HOURS, days or even weeks later, even if the person feels fine. That's why EVERY shock above ELV gets a medical check and is reported."),
                Tip("Electricians must hold current CPR / first aid. Low-voltage rescue training is expected too.")
            ),
            page(
                "Other incidents",
                bullets(
                    "Every shock, burn, electrical fire and dangerous occurrence is reported straight away to your supervisor",
                    "Don't disturb the scene of a serious accident except to save life or prevent injury (or with an inspector's permission)",
                    "Machine contacted voltage over 1000 V AC (e.g. a power line)? It's parked up for about 24 h because the TYRES can explode or catch fire",
                    "Cable damage: photograph it and report it. Only authorised people restore power, after testing proves it's safe."
                ),
                quiz("A worker gets a 'small tingle' from a 240 V tool and feels fine. What should happen?", 3, "Every shock above ELV goes to medical for an ECG and gets reported. Heart effects can show up later.", "Nothing, they feel fine", "Tell them to rest for 5 minutes", "Report it next week", "Medical centre for an ECG and report it now")
            )
        ),
        lesson(
            "inc2", "Live work rules", "⛔", 6, Level.INTERMEDIATE,
            page(
                "Only when there's truly no alternative",
                P("Live WORK (not just testing) needs written authorisation and only happens if ALL of these are met:"),
                bullets(
                    "It's necessary for safety, or the work genuinely can't be done de-energised, and there's no reasonable alternative",
                    "A full risk assessment shows the risk can be made acceptably low",
                    "Done to a SWMS by qualified, trained people with proper PPE and tools",
                    "Isolation point identified and reachable fast; clear exit; area barricaded",
                    "A safety observer trained in rescue and CPR is present"
                ),
                WhyNot("Why isn't 'we'll lose production' a reason to work live? Lost production can be recovered. A dead electrician can't. Regulators and standards specifically rule it out as a justification."),
                Tip("Not live work: non-contact inspections from outside approach limits (like thermal imaging), and cleaning the outside of gear that poses no electrical danger.")
            )
        ),
        lesson(
            "inc3", "Records that matter", "📒", 6, Level.BEGINNER,
            page(
                "Write it down",
                bullets(
                    "Electrical logbook: appointments, licences, installations, incidents, monthly maintenance, repairs, modifications, damage, single-line drawings",
                    "Test records: who tested (and licence number), date, result, next due date",
                    "Drawings updated within a few days of any change; as-built schematics inside every starter, DB and substation",
                    "Single-line HV drawings at every substation",
                    "Records kept for years (e.g. 7) or as long as the law requires",
                    "Hazardous-area dossier where relevant"
                ),
                WhyNot("Why does it matter if the drawings are a week out of date? In an emergency, or during an isolation, people rely on the drawings to find the right breaker. An out-of-date drawing can send someone to isolate the wrong thing and work on a live cable."),
                quiz("What must every test record include?", 0, "Who tested (with licence number), the date, the result, and when the next test is due.", "Tester, licence no., date, result and next due date", "Just the date", "Just a tick", "The weather")
            )
        )
    )
)
