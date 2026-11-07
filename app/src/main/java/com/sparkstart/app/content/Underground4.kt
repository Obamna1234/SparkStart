package com.sparkstart.app.content

import com.sparkstart.app.data.*

private const val OEM = "Terminal numbers, switch names, settings and layouts change between makes, models and builds. Always use the OEM manual and electrical drawings for the exact machine."

internal val ugRigsModule = Module(
    id = "ug_rigs",
    title = "Drill Rig Electrical Maintenance",
    emoji = "🚜",
    blurb = "Jumbos, production drills and raise borers: safety shutdown tests, slip rings, cable reel limits, cabinets, and the pump on the rig.",
    color = 0xFFFFA000,
    group = Group.UNDERGROUND,
    lessons = listOf(
        lesson(
            "rig1", "How an electric drill rig is powered", "🔌", 8, Level.BEGINNER,
            page(
                "Diesel to tram, electric to drill",
                P("Development jumbos, production drills (e.g. 'Solos') and raise borers usually TRAM on a diesel engine but DRILL on electric power: 1000 V through a trailing/reeling cable wound on a cable reel."),
                bullets(
                    "Restrained plug → cable reel → SLIP RINGS (rotating contacts so the reel can turn while connected) → main electrical cabinet",
                    "Main isolator / 1000 V main switch, with a mechanical interlock and pilot interlock",
                    "Motor starters for the hydraulic power pack, compressor and water pump",
                    "Transformers for lower control voltages",
                    "24 V DC battery system for the engine and controls (with its own isolator)"
                ),
                Vis(Visual.INDUCTION_MOTOR, "The power pack, compressor and water pump are driven by 3-phase motors, so phase rotation matters.")
            ),
            page(
                "Safety shutdown circuits",
                P("A rig has many electrical safety circuits that must be TESTED regularly to prove they still shut it down:"),
                bullets(
                    "Emergency stops (often 6–8 around the rig)",
                    "Anti-tramming interlock: turn on the diesel ignition or tram, and mains power must drop out",
                    "Slip-ring cover pilot interlock switch",
                    "Cable reel limit switches",
                    "Low hydraulic oil level and high oil temperature",
                    "Shank lube (rock oil) level",
                    "Low water pressure and low air pressure",
                    "Compressor high temperature"
                ),
                WhyNot("Why does the rig cut mains power when you switch the diesel on? If someone trams away while still plugged in, the reeling cable gets dragged, ripped out or run over while live. The anti-tram interlock makes it impossible to tram on mains."),
                Ask(OEM)
            )
        ),
        lesson(
            "rig2", "Testing the safety circuits", "🧪", 12, Level.INTERMEDIATE,
            page(
                "General rules",
                bullets(
                    "Nobody working on or near the rig when mains is applied",
                    "Commissioning tag on (often on the steering wheel) while testing",
                    "Re-seal every sensor you unplugged and re-apply sealing tape (e.g. Densyl) to opened enclosures",
                    "Record everything on the service sheet"
                )
            ),
            page(
                "E-stops and anti-tram",
                order(
                    "Order the emergency-stop test",
                    "Commissioning tag on, area clear",
                    "Plug in and energise the rig on mains",
                    "Start the hydraulic power pack",
                    "Push one E-stop fully in",
                    "Check the power pack stops and the E-stop alarm shows",
                    "Reset and repeat for every E-stop",
                    "Switch off mains and remove the tag"
                ),
                P("Anti-tram test: power pack running on mains → switch the diesel ignition ON (or select tram) → mains must drop out. Ignition off, press PILOT RESET, and mains should come back."),
                quiz("During the anti-tram test you turn the ignition on and mains stays on. What now?", 3, "The interlock has FAILED. The rig must not be used until it's fixed. Tag it out of service and investigate (e.g. the pilot diesel relay and its wiring).", "Carry on, it's fine", "Turn it off and on again", "Bridge the relay", "Tag it out of service and fix the interlock")
            ),
            page(
                "Level, pressure and temperature",
                bullets(
                    "Hydraulic oil level: slowly lift the level switch out (or unplug the sensor) with the power pack running. It must shut down with a low-oil alarm.",
                    "Shank lube level: same idea. Remove the level switch and check the alarm and level change on the screen.",
                    "Water pressure: close the water shut-off with water connected. Below about 2.8 bar (on some models) the low-pressure alarm must come on.",
                    "Air pressure: with the compressor running the switch should open within seconds. Simulate a fault and you get the low-air alarm, the shank lube stops, and the drill levers won't latch.",
                    "Compressor temperature: check the screen against an infrared thermometer, and/or heat the sensor with a HEATER (never an open flame) until it gives a notice, then an alarm and shutdown (around 100–105 °C and 115 °C on some models)."
                ),
                Hazard("Never remove a compressor temperature sensor from its housing while the system is pressurised. Depressurise first."),
                WhyNot("Why heat the temperature sensor with a heater and not a lighter? An open flame overheats and damages the sensor, it's a fire risk around oil, and you can't control the temperature to check the actual trip points.")
            )
        ),
        lesson(
            "rig3", "Slip rings, reel limits and the cabinet", "🔄", 12, Level.INTERMEDIATE,
            page(
                "Slip ring & reeling cable inspection",
                P("Isolated (rig unplugged with a plug lockout, 24 V isolated, proven dead):"),
                bullets(
                    "No moisture or dirt inside. Brush or lint-free cloth, vacuum out carbon dust.",
                    "Terminals clean and tight",
                    "Carbon brushes seated properly and not worn. Replace at about 3 mm at the thinnest point, and replace them ALL together.",
                    "Slip rings not worn or badly grooved",
                    "Restrained plug clean, cable anchor fitted (about 1.5–2 m minimum depending on rig)",
                    "Test the pilot reset button's stuck/held timer and latching relay",
                    "Record earth continuity between the plug earth and the cabinet earth"
                )
            ),
            page(
                "Slip-ring cover pilot switch test",
                order(
                    "Order the slip-ring pilot interlock test",
                    "Unplug the rig, fit the plug lockout, lock and tag",
                    "Isolate the 24 V battery switch and lock it",
                    "Prove isolation",
                    "Open the cabinet and find the pilot terminal",
                    "Remove the slip-ring cover (switch now open)",
                    "Ohms from the plug pilot pin to that terminal: OPEN",
                    "Close the switch by hand: CLOSED circuit",
                    "Refit the cover with the seal seated watertight"
                ),
                WhyNot("Why does removing the slip-ring cover kill the pilot? Under the cover the slip rings carry 1000 V. The switch makes sure the rig can't be energised with the cover off, so nobody gets a hand in there live.")
            ),
            page(
                "Cable reel limit switches",
                P("Set up with the commissioning tag on and the rig on its jacks (wheels off the ground) so the drum can turn without the rig moving:"),
                bullets(
                    "Reel off until about 4–5 layers/rounds remain: FIRST LIMIT. Warning light and the park brake applies.",
                    "Override (hold the button) and keep reeling off to about 2 layers: SECOND LIMIT. Alarm, the park brake applies and can't be overridden. Some rigs only let you reel back in with the reel remote.",
                    "Adjust the cams if the limits are wrong, then wind back on and confirm the park brake resets"
                ),
                WhyNot("Why does the rig stop with layers still on the drum? If the cable runs right off, the rig would rip the cable out of its anchor and slip rings while live. The remaining layers also take the pulling strain off the terminations."),
                Ask(OEM)
            ),
            page(
                "Main cabinet checks",
                bullets(
                    "Insulation resistance and continuity tests",
                    "No moisture or dust inside. Check door seals.",
                    "Terminals tight with no discolouration (a sign of heat from loose connections)",
                    "Contactors, relays and mountings secure",
                    "Transformer: no signs of overheating. Clean or replace the air filter.",
                    "Looms and conduits neat and clear of pinch points",
                    "Main isolator mechanical interlock and pilot interlock work",
                    "Protection settings noted and correct",
                    "Diode test from the rig's plug (with the main switch on, plug locked out)"
                ),
                Tip("Discoloured terminals or a burnt smell = a high-resistance joint heating up. Find it during the service before it fails in the face.")
            )
        ),
        lesson(
            "rig4", "The pump on the rig", "💧", 9, Level.INTERMEDIATE,
            page(
                "Rig-mounted dewatering pump",
                P("Some jumbos carry their own dewatering pump (e.g. a self-priming diaphragm/vacuum-assisted unit) that plugs into the rig's PUMP RECEPTACLE on the carrier, so the face can be dewatered while drilling."),
                order(
                    "Order the rig pump set-up",
                    "Set the jumbo up at the face",
                    "Isolate the rig's main isolator and plug the pump into the rig's pump receptacle",
                    "Connect the suction hose and submerge the strainer where it won't get hit",
                    "Connect the discharge hose with dropper valves open and the bleed valve closed",
                    "Flush the non-return valve briefly",
                    "Start the rig's compressor and open the air priming valve until water flows",
                    "Back the air valve off a little and start the pump from the cab"
                ),
                Vis(Visual.PUMP_SHARED, "Compare the pump in the rig's own pump outlet with a pump on a separate starter."),
                Tip("These pumps can run dry for a while, but not for long (e.g. under an hour). They're also designed to run ONE way only, so check rotation with a short bump start.")
            ),
            page(
                "Shut down and faults",
                bullets(
                    "Stop the pump, close the air priming valve, disconnect suction",
                    "OPEN THE BLEED VALVE to release discharge pressure before disconnecting the hose",
                    "Check seal coolant oil daily or weekly. Some emulsifying is normal, but change it at the first sign of sludge."
                ),
                Hazard("A long discharge line up a decline holds a LOT of water under pressure. Disconnecting without bleeding it can whip the hose or spray at high pressure."),
                faults(
                    "Won't prime: suction lift too high, strainer blocked, suction hose not airtight or collapsed, non-return valve not seating, ejector/air supply problem",
                    "Not enough water: discharge head too high, impeller worn or blocked, delivery hose blocked or punctured, WRONG ROTATION",
                    "High power draw: obstruction in the body/impeller, liquid too thick",
                    "Vibration/overheating: cavitation from excessive suction lift, worn mechanical seal",
                    "Rebuilt pump check: discharge head pressure around 6.5 bar on one common model"
                )
            )
        ),
        lesson(
            "rig5", "Raise borers", "🌀", 6, Level.ADVANCED,
            page(
                "Same ideas, different machine",
                bullets(
                    "Same isolation: unplug the trailing cable with a plug lockout, isolate 24 V",
                    "Same slip-ring, brush, anchor, pilot interlock and reel-limit tests",
                    "Control system watches hydraulic oil level and temperature: warning first, then shutdown (e.g. warning around 65 °C, alarm around 75 °C on one model)",
                    "Operational tests: buttons, lights, E-stops and their reset, pump controls (ELCB and earth continuity relay), work lights, main switch / phase changeover switch, remote pilot reset, earth leakage devices"
                ),
                Ask(OEM)
            )
        )
    )
)

internal val ugBevModule = Module(
    id = "ug_bev",
    title = "Battery Electric Machines (BEV)",
    emoji = "🔋",
    blurb = "Battery-electric drills and loaders, chargers and coolers: high-voltage DC, isolation levels, insulation monitoring.",
    color = 0xFF1B5E20,
    group = Group.UNDERGROUND,
    lessons = listOf(
        lesson(
            "bev1", "What's different about BEVs", "⚡", 9, Level.ADVANCED,
            page(
                "Big batteries, big voltages",
                bullets(
                    "Traction/main batteries commonly around 650 V DC; AC inverter outputs up to about 700+ V AC",
                    "Some machines also have a separate tramming battery pack (a few hundred volts DC)",
                    "24 V DC system that enables the HV system",
                    "Inverters for drive motors, compressor, water pump and auxiliaries, plus brake resistors",
                    "Insulation monitoring that continuously watches the HV DC system's insulation to the chassis",
                    "Charging sockets (e.g. CCS2) and battery heater / genset inlets"
                ),
                Hazard("Voltages stay present for a time AFTER shutdown (capacitors). Never remove covers on energised parts, including straight after shutdown."),
                Std("ISO 19296, ISO 14990", "Functional and electrical safety standards for underground mobile machines and electric-drive machines. BEV chargers and battery systems also fall under AS/NZS 3000 and AS/NZS 5139.")
            ),
            page(
                "Levels of isolation",
                P("Many BEVs isolate in levels. Use the HIGHEST level the job needs:"),
                bullets(
                    "Level 1: ignition off, cabin isolator to manual-off (shuts modules down properly), then the 24 V service isolator. Lock, tag and try to start: nothing should light up. Good for general maintenance.",
                    "Level 2: also open the main HV DC isolator, WAIT (e.g. 15 minutes) for capacitors to discharge, then prove dead at the battery measurement point. Needed for drive motors, inverters and fuses.",
                    "Drills with a 1000 V supply: also lock the main switch, unplug the reeling cable with a plug lockout, and lock out the genset and heater inlets."
                ),
                WhyNot("Why use the cabin isolator BEFORE the service isolator? The cabin isolator lets the control modules shut down in an orderly way. Yanking the service isolator first can damage electronics."),
                quiz("You've turned off a BEV's main DC isolator. Is everything between the batteries and that isolator now dead?", 2, "No. DC is present between the batteries and the line side of the isolator at ALL times. The battery is the source.", "Yes", "Yes, after 15 minutes", "No, the battery side is always live", "Only if it's charging")
            )
        ),
        lesson(
            "bev2", "Chargers, coolers and monitoring", "🔌", 8, Level.ADVANCED,
            page(
                "Chargers and coolers",
                bullets(
                    "Charging units can hold or produce high voltage even when isolated from mains. Remove and lock out the battery connection before service.",
                    "Door and service-door interlocks stop charging when opened",
                    "Wait for capacitors (e.g. 10 minutes) before working inside",
                    "Cooling units hold pressurised hot coolant, which can cause burns and eye injury. Let them cool, wear eye protection and gloves.",
                    "The charge plug locks in the socket while charging. Stop charging properly (charger stop, the socket stop button, or an E-stop) and it releases. Never force it."
                ),
                Tip("Use a meter rated CAT IV 600 V / CAT III 1000 V and a proving unit that can do DC.")
            ),
            page(
                "Insulation monitoring",
                P("An insulation monitoring device measures the resistance between the HV DC system and the chassis all the time. As insulation drops it gives a WARNING, then an ALARM (for example 500 kΩ warning and 100 kΩ alarm on one machine)."),
                bullets(
                    "It self-tests at power-up and periodically",
                    "A manual test button checks it works (one test changes the output relays, one doesn't)",
                    "Settings are not to be adjusted"
                ),
                WhyNot("Why do BEVs need insulation monitoring instead of a normal earth-leakage relay? The HV DC system floats (isn't earthed) on the machine. A first fault to chassis doesn't cause a big current, so it can't trip a normal device, but a SECOND fault could. The monitor catches the first fault early so it can be fixed."),
                Ask("Each OEM's BEV procedures are detailed and model-specific. Follow them exactly and only work on BEV HV systems if you're trained and authorised.")
            )
        )
    )
)
