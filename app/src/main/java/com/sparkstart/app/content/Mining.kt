package com.sparkstart.app.content

import com.sparkstart.app.data.*

private const val SITE_NOTE =
    "Voltages, protection settings, equipment layouts and procedures differ between mines and between equipment models. Your site's electrical standards, the manufacturer's manuals and your supervisor ALWAYS come first."

internal val miningModule = Module(
    id = "mining",
    title = "Underground Mining (WA)",
    emoji = "⛏️",
    blurb = "Jumbo boxes, DBs, fan and pump starters, trailing cables, jumbo/Solo electrical cabinets, transformers, generators, and finding faults underground.",
    color = 0xFFF4511E,
    group = Group.INDUSTRY,
    lessons = listOf(
        lesson(
            "u1", "How power gets underground", "🗺️", 8, Level.BEGINNER,
            page(
                "The big picture",
                bullets(
                    "Surface: incoming supply or site power station feeds a surface substation",
                    "High-voltage (HV) cable runs down the decline or shaft",
                    "Underground substations (often skid-mounted / transportable) step HV down to the machine voltage",
                    "That voltage feeds distribution gear: jumbo boxes, distribution boards (DBs), fan and pump starters",
                    "Trailing cables connect mobile machines (jumbos, Solos, bolters) to their outlets",
                    "Small transformers step down further for lighting and small power"
                ),
                P("Many Australian hard-rock mines run mobile electric machines at 1000 V, but sites vary."),
                Ask(SITE_NOTE)
            ),
            page(
                "The rules that apply",
                bullets(
                    "WA Work Health and Safety (Mines) Regulations 2022 (replaced the old Mines Safety and Inspection regs on 31 March 2022)",
                    "AS/NZS 3000 Wiring Rules still apply as the base",
                    "AS/NZS 3007: electrical equipment in mines and quarries",
                    "AS/NZS 4871 series: electrical equipment for mines (incl. mobile machines)",
                    "AS/NZS 2802: reeling and trailing cables (non-coal)",
                    "AS/NZS 2081: electrical protection devices for mines",
                    "Electrical log book: inspections, tests and work are recorded"
                ),
                Tip("WA currently has no underground COAL mines. WA underground mines are mostly hard rock (gold, nickel, etc.), so the coal-specific explosion-protection rules you might read about from NSW/QLD often don't apply, but hazardous areas can still exist."),
                quiz("Which regulations replaced the WA Mines Safety and Inspection laws for safety in 2022?", 1, "The WHS (Mines) Regulations 2022 took effect on 31 March 2022.", "AS/NZS 3000", "WHS (Mines) Regulations 2022", "AS/NZS 3760", "The WAER")
            )
        ),
        lesson(
            "u2", "Hazards underground", "⚠️", 7, Level.BEGINNER,
            page(
                "Why underground is harder",
                bullets(
                    "Water everywhere: on the floor, dripping, in cable plugs",
                    "Mobile plant running over or snagging cables",
                    "Rock falls and blasting damaging cables and gear",
                    "Heat, humidity and dust getting into enclosures",
                    "Diesel exhaust and blasting fumes, which need ventilation (that's what the big fans do)",
                    "Long escape routes and limited access",
                    "HV close by"
                ),
                Hazard("Water + damaged cable + a person standing in water is the classic underground electrocution setup. Treat any damaged cable as live until it's isolated and proven dead."),
                Vis(Visual.BODY_CURRENT)
            ),
            page(
                "Isolation underground",
                P("Mines have strict isolation procedures. Often that means isolating at the supply outlet or jumbo box, removing the plug, locking, tagging, and testing. HV work needs permits and authorised people."),
                WhyNot("Why can't you just hit the stop button on the jumbo and work on it? A stop button is a CONTROL device, not an isolation. The cable, the electrical cabinet and parts of the control gear are still live, and someone could restart it."),
                Ask(SITE_NOTE)
            )
        ),
        lesson(
            "u3", "Trailing cables (jumbo & pump cables)", "🧵", 10, Level.BEGINNER,
            page(
                "What's inside",
                bullets(
                    "Three power (phase) cores",
                    "Earth core(s)",
                    "A PILOT core for earth-continuity monitoring",
                    "Screens and tough outer sheath (depending on cable type)"
                ),
                Std("AS/NZS 2802 + WHS (Mines) Regs r.641E", "In WA, trailing and reeling cables must conform to AS/NZS 2802 and have a pilot core arranged to cut off supply if earth continuity is lost (per the WA regulator's guidance)."),
                Vis(Visual.PILOT_CORE, "Break the earth, cut the pilot, or unplug. Watch the relay trip.")
            ),
            page(
                "Why the pilot matters",
                P("A machine on a trailing cable is only safe while its earth core is connected. If that earth breaks and a fault occurs, the WHOLE machine frame can go live, with people standing in water next to it."),
                P("The earth-continuity relay keeps checking the earth via the pilot loop. Lose it and power is cut. It fails safe."),
                WhyNot("Why can't you link out (bypass) the pilot to stop nuisance trips? You'd remove the only thing that proves the machine's earth is connected. Next time the earth breaks, nothing notices, and a fault could make the machine live. Find the real fault: plug pins, damaged cable, or the termination at the machine end."),
                quiz("An earth-continuity relay trips. What's it telling you?", 2, "The pilot/earth monitoring loop is broken, so it can't confirm the machine is earthed.", "The machine is overloaded", "Phase rotation is wrong", "The earth/pilot loop isn't intact", "It's a normal trip, just reset it")
            ),
            page(
                "Damage, repairs, handling",
                bullets(
                    "Hang cables on proper hooks/supports, out of the way of plant",
                    "Never drive over a trailing cable unless it's protected for that",
                    "Check cables and plugs for cuts, crush damage and water",
                    "Damaged cables are tagged out and repaired by approved methods (e.g. vulcanised repairs by a specialist workshop), then tested"
                ),
                WhyNot("Why can't you tape up a cut in a trailing cable? Tape can't restore insulation strength, the screen, the earth, or the toughness the cable needs. Water gets in. A taped cable can fail without warning while people are standing next to it."),
                Hazard("A cable under tension or on a reel can whip or crush. Stay clear of the reel when the machine moves."),
                faults(
                    "Isolate at the outlet, remove the plug, lock and tag",
                    "Visually inspect the whole length and both plugs",
                    "Continuity test each core end to end, including pilot and earth",
                    "Insulation test cores to each other and to earth at the voltage your site specifies",
                    "Record results in the electrical log book"
                )
            )
        ),
        lesson(
            "u4", "Jumbo boxes & distribution boards", "🔌", 10, Level.INTERMEDIATE,
            page(
                "What a jumbo box does",
                P("A 'jumbo box' is a portable distribution unit / outlet box that feeds a drill rig (jumbo or Solo) or other machine through its trailing cable. Typical things inside (exact gear varies by maker and site):"),
                bullets(
                    "Incoming connection and an isolator you can lock off",
                    "A circuit breaker for each outlet",
                    "Earth-leakage protection set for underground use",
                    "Earth-continuity (pilot) protection for each trailing cable",
                    "Restrained plug outlets (couplers) with interlocks",
                    "Indicator lights, test buttons and reset controls"
                ),
                Tip("WA companies such as Murray Engineering build this type of gear (starter panels, distribution boards, transportable substations) for mines. Each manufacturer's internals differ. Always use the drawings and manual for the actual unit.")
            ),
            page(
                "Distribution boards (DBs) underground",
                P("DBs underground distribute power to fixed loads: lighting, small power (often via a step-down transformer), pumps and fans. They work on the same principles as surface DBs, but built tough: high IP rating, rugged enclosures, and protection suited to mine earthing."),
                Vis(Visual.NER, "Many mine systems limit earth-fault current with a resistor, which is why sensitive earth-leakage protection is used."),
                WhyNot("Why can't you reset an earth-leakage trip over and over until it holds? The trip means current is leaking to earth, maybe through water, a damaged cable, or a person. Each reset re-applies the fault. Find the cause first. Many sites require a trip to be investigated and recorded before re-energising."),
                faults(
                    "Outlet won't close: is the earth-continuity relay healthy? (pilot loop, plug seated, cable OK, termination at the machine)",
                    "Earth-leakage tripped: isolate, then find which cable or machine is leaking with insulation tests",
                    "Check any interlocks (door, plug) are made",
                    "Check incoming supply on all phases (authorised people, correct PPE)",
                    "Record the fault and fix in the log book"
                ),
                Ask(SITE_NOTE)
            )
        ),
        lesson(
            "u5", "Fan starters & pump starters", "🌬️", 11, Level.INTERMEDIATE,
            page(
                "Ventilation fans",
                P("Fans push fresh air through the mine to clear diesel exhaust, blasting fumes and heat. Primary fans move air for the whole mine. Auxiliary fans blow air through vent bag/ducting into headings."),
                P("A FAN STARTER is the enclosure that controls the fan motor. Typical parts:"),
                bullets(
                    "Isolator (lockable)",
                    "Breaker or fuses (short-circuit protection)",
                    "Starter: DOL contactor, soft starter or VSD",
                    "Overload protection",
                    "Earth-leakage protection (plus earth-continuity if fed by trailing cable)",
                    "Control transformer, start/stop, run lights, sometimes remote stop"
                ),
                Vis(Visual.INDUCTION_MOTOR, "Fan motors are three-phase induction motors. Swap two phases and it runs backwards, and backwards fans move far less air."),
                Hazard("A stopped fan isn't just a broken machine. Air quality in that area can become dangerous. Sites have rules about who can stop fans and what happens when one stops."),
                faults(
                    "Fan trips on overload: check for a damaged impeller, blocked/collapsed ducting, bearings, and current on all phases",
                    "Earth-leakage trip: water in the motor, cable or plug? Isolate and insulation test.",
                    "Won't start: check the control circuit (stops, interlocks, coil voltage) before the motor",
                    "Runs but weak airflow: check rotation direction and ducting"
                )
            ),
            page(
                "Pumps and pump starters",
                P("Dewatering pumps (often submersible pumps sitting in the sump) keep the mine from flooding. Pump starters are like fan starters, plus LEVEL control (floats or probes) so the pump starts and stops on water level, and protection against running dry."),
                Vis(Visual.PUMP_SHARED, "Compare a pump on its own outlet with a pump fed from the jumbo, then make a fault."),
                WhyNot("Why shouldn't a submersible pump just be plugged into the jumbo? The general principles: the pump sits in water and its cable gets dragged around, so it's a likely fault point. Fed from the jumbo, it shares the jumbo's supply and earth path. A pump fault can then put the jumbo frame and the water around it into the fault path, with an operator standing right there. A pump fault also takes the jumbo off line. Giving the pump its own outlet keeps the fault separate."),
                Ask("We're not guessing at your site's exact rule or reasoning. Ask your supervisor or site electrical engineer what's allowed for pumps and jumbos at your mine, and why. Then add a 🚩 note here so you remember."),
                faults(
                    "Pump won't start: check water level controls (floats stuck/probes dirty), control circuit, protection trips",
                    "Starts then trips: overload (blocked impeller, wrong rotation) or earth leakage (water in motor/cable)",
                    "Runs but no water: wrong rotation, air-locked, blocked suction, worn impeller",
                    "Always isolate and remove the plug before pulling a pump out of the sump"
                )
            )
        ),
        lesson(
            "u6", "Jumbos, Solos & the electrical cabinet ('A cab')", "🚜", 11, Level.INTERMEDIATE,
            page(
                "What's electric on a drill rig",
                P("Development jumbos and production drills (like Solos) usually tram on a diesel engine but DRILL on electric power from a trailing cable on a cable reel. The 1000 V (or site voltage) comes in through the cable to the rig's electrical cabinet."),
                P("The electrical cabinet (what many crews call the 'A cab') typically contains:"),
                bullets(
                    "Main isolator / main circuit breaker for the incoming supply",
                    "Motor starters for the hydraulic power-pack motor(s), compressor and water booster pump",
                    "Earth-fault / insulation monitoring on the rig (depending on design)",
                    "Control transformer(s) stepping down to lower voltages for controls and lights",
                    "Control system / PLC interface, relays and contactors",
                    "Cable reel motor control"
                ),
                Ask("Layouts differ between makes and models (e.g. Sandvik, Epiroc) and even between builds of the same model. Use the manufacturer's electrical drawings for the exact rig before working on it.")
            ),
            page(
                "Hazards and 'why not'",
                Hazard("The cabinet has full supply voltage inside, from the trailing cable, whenever the rig is plugged in. Hitting the rig's emergency stop does NOT isolate the incoming cable."),
                WhyNot("Why can't you open the cabinet with the rig still plugged in, just to 'have a look'? Incoming terminals and the main isolator's line side stay live from the trailing cable. Isolate at the jumbo box, remove the plug, lock and tag, then prove dead inside."),
                WhyNot("Why does phase rotation matter on a rig? Hydraulic pumps and compressors are driven by three-phase motors. Wrong rotation can spin a pump backwards and damage it in seconds. Rotation is checked after cable or plug work."),
                Hazard("Electrical isn't the only stored energy: hydraulic pressure, accumulators, and the cable reel under tension. Follow the rig's full isolation procedure."),
                faults(
                    "Rig won't power up: is the jumbo box outlet on? Earth-continuity healthy? Rig main breaker on?",
                    "Power-pack won't start: check interlocks and start conditions, motor starter, overloads, and fault codes on the display",
                    "Trips on start: check for low voltage at the rig (long cable runs) and the starter type/settings",
                    "Earth-fault alarm/trip: isolate, then insulation test sections (cable, reel, motors) to find the leak",
                    "Always read and record fault codes. The manual explains them."
                )
            )
        ),
        lesson(
            "u7", "Underground transformers & substations", "🏭", 9, Level.ADVANCED,
            page(
                "Transportable substations",
                P("Underground substations take the HV feed and transform it down to the distribution voltage. They're usually skid-mounted or on wheels so they can be moved as the mine develops."),
                bullets(
                    "HV switchgear (to switch and protect the incoming HV)",
                    "The transformer (often three-phase, with a neutral earthing arrangement such as an NER)",
                    "LV outgoing circuit breakers with protection relays",
                    "Earth-leakage and earth-continuity protection for outgoing circuits"
                ),
                Vis(Visual.TRANSFORMER),
                Vis(Visual.NER)
            ),
            page(
                "HV rules",
                Hazard("HV can arc across air gaps without contact. Only trained, authorised people do HV switching, under permits and procedures."),
                WhyNot("Why can't an apprentice operate HV switchgear 'just to help out'? HV switching needs specific training, authorisation and procedures. A wrong operation can cause an arc flash or energise equipment someone is working on."),
                Std("AS 2067 / AS/NZS 3007", "HV installations and mine electrical installations have their own standards on top of AS/NZS 3000."),
                Ask(SITE_NOTE)
            )
        ),
        lesson(
            "u8", "Diesel generators", "🛢️", 9, Level.ADVANCED,
            page(
                "Generators on mine sites",
                P("Diesel generators supply power where there's no grid (remote sites), act as standby for critical loads (fans, pumps, refuge chambers, comms) and power portable gear."),
                bullets(
                    "Alternator (generator end) + diesel engine + control panel",
                    "Voltage regulator (AVR) holds the voltage. Governor holds the speed/frequency (50 Hz).",
                    "Output circuit breaker and protection (overload, short circuit, earth fault)",
                    "Earthing arrangement for the generator neutral and frame"
                ),
                Std("AS/NZS 3010", "Generating sets have their own standard covering earthing, protection and how they connect to installations.")
            ),
            page(
                "Back-feed and changeover",
                Vis(Visual.BACKFEED),
                WhyNot("Why can't a generator be hooked straight onto a board that's also fed from the normal supply? Without an interlocked changeover, the generator can back-feed the supply (energising 'dead' lines people are working on) or be connected out of step with the grid, which causes huge currents and damage."),
                Hazard("Diesel exhaust underground must be ventilated. Fuel and hot surfaces are fire risks."),
                faults(
                    "No output voltage: check the output breaker, AVR, excitation and control panel faults",
                    "Voltage OK but frequency wrong: governor or fuel problem, or overloaded engine",
                    "Earth-fault trips: insulation test the connected circuits and alternator (isolated!)",
                    "Changeover won't transfer: check interlocks and control supply"
                )
            )
        ),
        lesson(
            "u9", "Finding faults underground", "🔎", 10, Level.ADVANCED,
            page(
                "A method that works",
                Vis(Visual.HALF_SPLIT),
                faults(
                    "Get the story: what tripped, what was running, what changed (moved machine, new cable, rain, water)?",
                    "Check the electrical log book for history on that gear",
                    "Read relay flags and fault codes BEFORE resetting anything",
                    "Isolate, remove plugs, lock, tag, and prove dead",
                    "Split the system: supply side vs cable vs machine",
                    "Insulation test each section at the voltage your site specifies, plus continuity of earth and pilot",
                    "Fix the cause, test, record in the log book, then return to service"
                )
            ),
            page(
                "Common underground faults",
                bullets(
                    "Earth-leakage trips: water in plugs, damaged trailing cables, motor insulation breakdown",
                    "Earth-continuity trips: plug not seated, damaged pins, cable damage, termination at the machine end",
                    "Overloads: blocked fans/pumps, wrong rotation, low voltage on long runs",
                    "Low voltage at machines: long cable runs and big starting currents"
                ),
                WhyNot("Why can't you swap plugs between outlets until one 'works'? You might move a faulty cable onto another outlet and trip that, or hide the real fault. Worse, you might energise a damaged cable someone's touching. Find the fault properly."),
                quiz("A jumbo trips on earth leakage each time it starts drilling in a wet heading. Best first step after isolating?", 3, "Inspect and insulation test the cable, plugs and rig sections to find where the leak is. Water ingress is a strong suspect.", "Turn up the earth-leakage setting", "Link out the relay", "Keep resetting it until it holds", "Inspect and insulation test the cable, plugs and rig to find the leak")
            )
        )
    )
)
