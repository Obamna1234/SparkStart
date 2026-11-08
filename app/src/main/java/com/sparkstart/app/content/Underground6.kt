package com.sparkstart.app.content

import com.sparkstart.app.data.*

// Built from real 1000 V starter panels, drawings and components. Recreated generically,
// with no owner names, asset numbers or site names. Product model names are publicly sold items.

internal val ugBoxesModule = Module(
    id = "ug_boxes",
    title = "Know Your Boxes: Jumbo, Pump & Fan Starters",
    emoji = "🗄️",
    blurb = "Walk through real 1000 V starter boxes: the door, what's inside, how the control circuit works, and the maths for overloads and earth leakage.",
    color = 0xFF1E4FA8,
    group = Group.UNDERGROUND,
    lessons = listOf(
        lesson(
            "box1", "The jumbo / pump starter box (JF)", "🚜", 12, Level.BEGINNER,
            page(
                "What it is",
                P("A '1000 V 150 A jumbo / 8–37 kW pump starter' is a skid-mounted box that sits near the face. One 1000 V supply comes in. Two things come out: a 150 A outlet for the jumbo trailing cable, and a smaller restrained outlet (about 60–65 A) for a submersible (e.g. Flygt) pump."),
                bullets(
                    "Main isolator: lockable, kills the whole box",
                    "Pump isolator: kills just the pump side",
                    "JUMBO lamps: Power Available (green), Pilot Healthy (blue), Run (red), E/L Fault (amber)",
                    "PUMP lamps: the same, plus O/L Fault (amber)",
                    "Buttons: E/L Test, E/L Reset, Stop, E-stop, and for the pump Start and O/L Reset",
                    "A covered selector MANUAL / AUTO / FLOAT and a covered PUMP CURRENT SET button (for the pump controller)"
                ),
                Vis(Visual.BOX_DOOR, "A recreated door. Play with it: start, stop, test and trip things and watch the lamps."),
                Tip("Lamp colours tell a story: green = power is there, blue = pilot is healthy, red = running, amber = something tripped.")
            ),
            page(
                "Inside: the power side",
                P("Follow the power from the incoming cable to the outlets:"),
                order(
                    "Order the jumbo power path",
                    "1000 V supply in",
                    "Main circuit breaker (about 160 A, adjustable thermal + magnetic)",
                    "Jumbo contactor (JC)",
                    "E/L toroid: all 3 phases pass through it",
                    "150 A jumbo receptacle (pilot goes to the pilot relay)"
                ),
                bullets(
                    "Pump side: pump breaker (about 50 A) → E/L toroid → 4–20 mA current transducer (for the pump controller) → pump contactor → electronic overload → 60 A restrained receptacle",
                    "Control transformer: 1000 V → 24 V AC, fed through two small 1000 V fuses",
                    "Control MCBs: separate small breakers for the jumbo control, pump control and area light",
                    "Pilot relays (e.g. Ampcontrol MEC-1): one for the jumbo, one for the pump",
                    "E/L relays: one for each outlet"
                ),
                WhyNot("Why isn't the pump just plugged into the jumbo? It can be, into the jumbo's own pump receptacle. What you must NOT do is run the pump off this box's pump outlet while the pump is stuck under the jumbo at the face. A fault can then drive current through the water and the jumbo's stabiliser legs and frame back to the jumbo's earth. See the 'pump & jumbo' lesson."),
                Hazard("The sign on the door says it: perform THREE-PHASE ISOLATION before coupling or uncoupling. Never pull a plug under load. The 1000 V receptacles are marked 'do not separate with power on'.")
            ),
            page(
                "The box can be started from the rig",
                P("Another sign says: '1000 V starter panel can be controlled remotely from drill rig'. The jumbo operator starts the box's contactor from the rig, using a start signal sent down the pilot core."),
                Hazard("Because it can start remotely, the box can come alive without anyone touching it. Turning the contactor off with Stop is NOT isolation. Isolate at the main isolator, lock it and test for dead."),
                quiz("You press STOP on the jumbo side. Is the jumbo cable safe to work on?", 1,
                    "Stop only drops the contactor. The rig can start it again remotely, and the breaker side is still live. Isolate, lock, tag, test.",
                    "Yes", "No. Stop isn't isolation, and it can be restarted from the rig", "Only if the Run lamp is off")
            )
        ),
        lesson(
            "box2", "Reading the 24 V control circuit", "🪜", 12, Level.INTERMEDIATE,
            page(
                "Ladder diagrams in 30 seconds",
                P("The control schematic is drawn like a ladder. The left rail is 24 V AC, the right rail is the neutral/return, and each 'rung' is one circuit. A coil only switches on when every contact in its rung is closed."),
                bullets(
                    "NO contact = open until the device operates",
                    "NC contact = closed until the device operates (e.g. overload 95-96, stop buttons)",
                    "Contacts are named after their device: PPR-2 = Pump Pilot Relay contact number 2"
                ),
                P("The pump RUN rung on a jumbo/pump starter reads, left to right:"),
                order(
                    "Put the pump run rung in order",
                    "24 V from the pump control MCB",
                    "E/L relay contact: opens if E/L trips",
                    "Overload contact: opens if O/L trips",
                    "Pump pilot relay contact: closed only if the pilot is healthy",
                    "Stop float (in FLOAT mode)",
                    "MANUAL/AUTO/FLOAT selector, then Start button (+ hold-in contact), pump controller or start float",
                    "Pump control relay coil, which then switches the pump contactor"
                ),
                Tip("Every protection is just a contact in series. If ANY one opens, the coil drops out and the pump stops. That's why 'it won't start' fault-finding is just walking along the rung.")
            ),
            page(
                "The E/L test circuit",
                P("The E/L TEST button doesn't fake anything. It makes a REAL leakage current through the toroid: 24 V AC → test button → a big resistor → one wire passed once through the toroid → back to the return."),
                bullets(
                    "Jumbo side: 220 Ω 50 W. 24 ÷ 220 ≈ 0.109 A = 109 mA. The setting is 100 mA, so it trips ✅",
                    "Pump side: 680 Ω 50 W. 24 ÷ 680 ≈ 0.035 A = 35 mA. The setting is 30 mA, so it trips ✅"
                ),
                Vis(Visual.ELR_TEST, "Change the setting and test resistor and see when the test proves the relay."),
                Deep("Why do the turns matter? The toroid measures ampere-turns. If the test wire went through TWICE, the relay would see 2 × 35 = 70 mA. So if someone changes the E/L setting, the resistor (or passes) may need to change too. Otherwise the test button might not trip a relay set too high, or might trip it 'easily' and hide a problem. It's the same for the 50 W rating: the resistor gets hot if you hold the button."),
                quiz("Someone turns the pump E/L relay up to 50 mA. With the 680 Ω test resistor, what happens when you press E/L TEST?", 2,
                    "24 ÷ 680 ≈ 35 mA, which is less than 50 mA, so it won't trip. The test 'fails', which correctly shows the setting is wrong. 30 mA is the label setting for pump starters.",
                    "Trips normally", "Blows the control fuse", "Won't trip: 35 mA is less than 50 mA")
            ),
            page(
                "Pilot relay contacts and remote start",
                P("The pilot relay (e.g. MEC-1) has its own output contacts. One contact lights the blue Pilot Healthy lamp. Another is in series with the run rung. Lose the pilot, and the contactor can't close or drops out."),
                Vis(Visual.PILOT_RELAY, "Pilot relay: loop resistance, trip setting, open and short faults."),
                Std("AS/NZS 2081", "The pilot (earth continuity) relays in these boxes are made to AS/NZS 2081. A MEC-1 has trip settings of 10–45 Ω, trip times of 50–500 ms, and DIP switches for REMOTE START and LATCHING. In remote-start mode, a 100 Ω start resistor added into the pilot loop by the start button tells the relay to close (the button must be held about 0.5 s and released within about 15 s or it trips 'Start Timed Out')."),
                Ask("What trip Ω and ms settings and latching mode does your site use on each box? Check the box, the drawing and your electrical supervisor. Don't change them yourself.")
            )
        ),
        lesson(
            "box3", "Mono / Flygt pump starter", "💧", 12, Level.INTERMEDIATE,
            page(
                "Two pumps, one box",
                P("A '1 kV 55 kW Mono / 8–37 kW Flygt pump starter' runs two different pumps:"),
                bullets(
                    "MONO pump (positive displacement, rubber stator and screw rotor, 55 kW): fixed-wired into the box through glands",
                    "FLYGT pump (submersible centrifugal, 8–37 kW): plugs into a 60 A restrained receptacle with a pilot, the same as the JF pump side"
                ),
                P("Door: MONO lamps Power / Run / No Flow Fault / O/L Fault, and FLYGT lamps Power / Pilot Healthy / Run / E/L Fault / O/L Fault. Buttons include NO FLOW RESET, an AUTO/MAN selector, a pump MANUAL/AUTO/FLOAT selector, PUMP CURRENT SET and an hour meter."),
                Vis(Visual.BOX_DOOR, "Pick 'Mono + Flygt pump' and try running the mono with the valve shut.")
            ),
            page(
                "Mono pump: CT-fed overload and no-flow protection",
                P("55 kW at 1000 V is about 40 A. Instead of passing 40 A through a small overload relay, this box puts three 100/5 CTs on the motor cables. The overload (a small electronic one with a 1–5 A dial) only sees the CT secondary current."),
                Vis(Visual.CT_OVERLOAD, "Slide to 55 kW and pick 100/5 CTs."),
                P("Maths: 40 A × 5 ÷ 100 = 2.0 A on the dial."),
                P("No-flow protection: when the mono runs, a timer (a delay-on-operate timer) starts. If flow isn't proven before it times out, it opens the run circuit and latches NO FLOW FAULT until you press No Flow Reset."),
                WhyNot("Why does a mono pump need no-flow protection when a submersible doesn't? A positive-displacement mono pump relies on the liquid to lubricate and cool its rubber stator. Run it dry, or against a shut valve, and it can wreck the stator (or burst a line) quickly. A centrifugal pump just churns."),
                Tip("Real drawings often have red 'redline' mark-ups where the box was changed (e.g. an old flow instrument removed, a stop button added). Always use the latest as-built drawing and check that the box matches before you trust it.")
            ),
            page(
                "Breakers: protecting the cable vs the motor",
                P("Each branch has its own moulded-case breaker. On the photographed box, the mono branch breaker has an adjustable thermal of 48–80 A and a fixed magnetic of 800 A. The Flygt branch has 30–50 A thermal and 500 A magnetic. The incoming main is rated 1000 V with a 30 kA breaking capacity."),
                bullets(
                    "Breaker: protects the CABLE against short circuits and heavy overloads",
                    "Overload relay: protects the MOTOR, set close to its full-load current",
                    "E/L relay + pilot relay: protect PEOPLE (earth faults, broken earths)"
                ),
                Deep("Contactor ratings drop as voltage goes up. On an ABB AF205-30 nameplate, AC-3 (motor) duty is 205 A at 400 V but only 100 A (132 kW) at 1000 V. Never swap in a 'same size' contactor without checking its 1000 V rating."),
                quiz("A 55 kW mono overload is fed from 100/5 CTs and set to 4 A. What motor current does that allow?", 0,
                    "4 A × 100 ÷ 5 = 80 A. That's double the 40 A FLC, so the motor would cook before the overload tripped. It should be 2.0 A.",
                    "80 A, so set way too high", "4 A", "40 A, so correct")
            )
        ),
        lesson(
            "box4", "Dual 110 kW fan starter", "🌬️", 12, Level.INTERMEDIATE,
            page(
                "What's in it",
                bullets(
                    "Main breaker 250 A with an electronic trip unit (ABB Tmax with PR222DS/P)",
                    "Two big contactors (Schneider TeSys Giga LC1G225), FC1 and FC2",
                    "Each fan: three 100/5 CTs → electronic overload (OL1 / OL2) with a remote reset module",
                    "Control transformer 1000/24 V about 400 VA, fed through two 1000 V fuses; a small 24 V DC converter",
                    "Timers: stage timers T1/T2, a repeat-cycle timer (CT) and a delay-start timer (DST), plus relays CR, FR1, FR2, DSR"
                ),
                P("Door: Power Available, Fan 1 Run, Fan 1 O/L Fault, Fan 2 Run, Fan 2 O/L Fault, Delay Start Activated (white). Buttons: Start, Stop (E-stop), Delay Start, Fan 1 O/L Reset, Fan 2 O/L Reset, and a FAN 1 / FAN 1&2 selector."),
                Vis(Visual.BOX_DOOR, "Pick 'Dual fan' and press START, then try DELAY START.")
            ),
            page(
                "Why the fans start one after the other",
                Vis(Visual.FAN_STAGING, "Compare starting both together with staged starts."),
                P("On the drawing, Fan 2's relay can only energise after a stage timer contact closes, with the selector on FAN 1&2 and Fan 2's overload healthy. Spreading the two direct-on-line starts apart cuts the peak current and the voltage dip on a long 1000 V cable."),
                P("Overload maths: 110 kW at 1 kV = 79 A (door label). With 100/5 CTs: 79 × 5 ÷ 100 = 3.95 A on the overload."),
                Ask("This drawing also shows a delay-start timer and a repeat-cycle timer labelled '2 ON 4 OFF'. Exactly how they're used (and the times set) depends on the site's ventilation plan. Check the drawing in YOUR box and ask your supervisor. Never change fan timers without approval: the fans keep people's air safe.")
            ),
            page(
                "Reading the electronic breaker trip unit",
                P("The PR222DS/P trip unit has DIP switches instead of dials. The formula is printed on its face:"),
                bullets(
                    "L (long time, overload): I1 = In × (0.4 + the switches ON from 0.02 / 0.04 / 0.08 / 0.16 / 0.32), so 0.4–1.0 × In. t1 = trip time at 6 × I1 (3 / 6 / 9 s / max)",
                    "S (short time): I2 = In × the sum of switches ON (0.6 / 1.2 / 2.4 / 5.8), with t2 delay 0.05–0.5 s",
                    "I (instantaneous): I3 = In × the sum of switches ON (1.5 / 2.5 / 3 / 5)"
                ),
                P("Worked example (made-up switch positions): In = 250 A, L switches 0.32 and 0.08 ON → I1 = 250 × (0.4 + 0.40) = 200 A."),
                Hazard("The trip unit settings were chosen with the cable size, fault levels and grading in mind. Read them; don't 'tweak' them."),
                quiz("In = 250 A and the L switches 0.16 and 0.04 are ON. What is I1?", 1,
                    "0.4 + 0.16 + 0.04 = 0.6, and 0.6 × 250 = 150 A.", "100 A", "150 A", "250 A")
            )
        ),
        lesson(
            "box5", "Overload & earth leakage maths drill", "🧮", 10, Level.INTERMEDIATE,
            page(
                "Motor full-load current",
                P("FLC = kW × 1000 ÷ (1.732 × V × pf × efficiency). At 1000 V with pf × eff ≈ 0.8, that's about 0.7 A per kW."),
                Vis(Visual.CT_OVERLOAD),
                bullets(
                    "Door label (1 kV): 5 kW 3.2 A · 8 kW 6 A · 18 kW 13 A · 20 kW 14.5 A · 37 kW 26 A",
                    "45 kW 32 A · 55 kW 40 A · 75 kW 54 A · 90 kW 65 A · 110 kW 79 A · 132 kW 95 A"
                ),
                Tip("Always use the MOTOR NAMEPLATE FLC first. The label and the 0.7 A/kW rule are a sanity check."),
                quiz("A 37 kW submersible on a direct overload (no CT). Roughly where should the overload be set?", 0,
                    "The label says 26 A (37 × 0.7 ≈ 26). Check the nameplate.", "About 26 A", "About 37 A", "About 52 A")
            ),
            page(
                "Earth leakage settings: grading",
                P("Earth leakage settings get SMALLER the closer you are to the machine and the people. The door label lists:"),
                bullets(
                    "Substation star point NER: 1000 mA, 300–400 ms",
                    "Substation main 1000 V breaker: 1000 mA · substation breakers: 500 mA",
                    "First distribution board: 300 mA · second DB: 200 mA",
                    "Jumbo boxes: 100 mA · pump starters: 30 mA",
                    "GPOs, lighting and portable plugged appliances: 30 mA",
                    "All 'instantaneous' except the NER. Small time steps may be added upstream for discrimination, by the electrical supervisor, case by case"
                ),
                Vis(Visual.ELR_TEST, "Slide the leakage up and watch which levels would trip."),
                quiz("A 250 mA earth fault on a pump cable. Which devices WANT to trip?", 2,
                    "Anything set at or below 250 mA: the 30 mA pump starter and the 100 mA and 200 mA levels if the fault current flows through them. Grading and time steps make the closest one clear first.",
                    "Only the NER", "Only the second DB", "The pump starter (30 mA) and every level up to 200 mA it passes through")
            )
        )
    )
)

internal val ugRelaysModule = Module(
    id = "ug_relays",
    title = "Inside the Relays & Components",
    emoji = "🔬",
    blurb = "Earth leakage relays, pilot relays, electronic overloads, pump snore controllers, timers, power supplies and leaky-feeder radio parts: what each one does and how to set it.",
    color = 0xFFE65100,
    group = Group.UNDERGROUND,
    lessons = listOf(
        lesson(
            "rel1", "Earth leakage relay (ELRC-1 type)", "🟡", 10, Level.INTERMEDIATE,
            page(
                "How it works",
                P("A toroid (doughnut-shaped CT) goes around ALL the phase conductors of the outgoing circuit. Healthy: whatever goes out comes back, so the magnetic fields cancel and the toroid outputs nothing. Earth fault: some current returns through earth instead, so the toroid sees the difference. Above the setting, the relay's output contact changes over and the contactor/breaker opens."),
                Vis(Visual.ELR_TEST),
                WhyNot("Why must the EARTH conductor NOT go through the toroid? Leakage current comes back on the earth. If the earth also passes through, the out and back cancel and the relay goes blind to the fault.")
            ),
            page(
                "Reading the front",
                bullets(
                    "IΔn dial (A): 0.25, 0.5, 1, 1.5, 2, 2.5, times a DIP multiplier ×0.1 / ×1 / ×10",
                    "t (s) dial: 0.02, 0.05, 0.1, 0.2, 0.3, 0.4, 0.5, times a DIP ×1 or ×10",
                    "DIP auto / man: auto-reset or manual-reset after a trip",
                    "Buttons: TEST, RESET · LEDs: ON, TRIP",
                    "Terminals 0, 1, 2, 3: supply (the label shows 0 V common, 24 V and 48 V AC/DC). Terminals 4-5-6: changeover output contact"
                ),
                P("Example: dial 1 × multiplier 0.1 = 0.1 A = 100 mA (a jumbo box). Dial 2 × 0.1 = 200 mA (second DB)."),
                Hazard("A multiplier DIP on ×10 instead of ×0.1 makes the relay 100 times LESS sensitive. A 100 mA setting becomes 10 A, and it will never protect anyone. Always check BOTH the dial and the multiplier against the settings label, then prove it with the E/L TEST button."),
                Ask("If the label setting (e.g. 30 mA) doesn't sit exactly on a printed dial step, don't guess. Check the relay manual or ask your supervisor. Never set it higher than the label."),
                quiz("IΔn dial on 0.5, multiplier DIP on ×1. What's the setting?", 1, "0.5 × 1 = 0.5 A = 500 mA, which is substation-breaker level and far too high for a pump.",
                    "50 mA", "500 mA", "5 A")
            )
        ),
        lesson(
            "rel2", "Pilot relay (Ampcontrol MEC-1)", "🟠", 10, Level.INTERMEDIATE,
            page(
                "What it proves",
                P("The pilot relay proves the machine is earthed. It sends a small DC voltage (±24 V) down the pilot core, through a diode at the machine end, and back along the earth core. It checks the loop resistance AND that the diode is there."),
                Vis(Visual.PILOT_RELAY),
                bullets(
                    "TRIP Ω dial: 10, 15, 20, 25, 35, 45 Ω (series resistance trip, ±3 Ω)",
                    "TRIP ms dial: 50, 100, 200, 300, 400, 500 ms",
                    "DIP 1 REMOTE START, DIP 2 LATCHING (latched trips need a manual reset)",
                    "LEDs: STATUS, HEALTHY, SHORT CIRCUIT, OPEN CIRCUIT, START TIMED OUT",
                    "Terminals: PILOT and earth on top; RST, +, −, and output contacts NC1/NO1/C1, NO2/C2 underneath"
                ),
                WhyNot("Why a diode, not just a wire? If the pilot core got crushed onto the earth or armour somewhere along the cable, a plain loop would still look 'healthy' even with the earth broken beyond the crush. The relay looks for the diode's one-way behaviour, so a short shows up as a SHORT CIRCUIT trip.")
            ),
            page(
                "Fault-finding a pilot trip",
                faults(
                    "Read the LED: OPEN CIRCUIT (broken loop or too much resistance) or SHORT CIRCUIT (pilot to earth, diode shorted)",
                    "Is the plug fully engaged and locked? Are the pins clean and dry?",
                    "Isolate. With the cable unplugged, measure pilot-to-earth at the plug both ways with a meter on diode test: one way conducts, the other doesn't",
                    "Reads open both ways: broken pilot core, diode open, or a pilot-chain switch open on the machine (e.g. door, cover switch)",
                    "Reads low both ways: diode shorted, or pilot crushed to earth in the cable",
                    "Half-split: test the cable on its own, then the machine end"
                ),
                Vis(Visual.DIODE_TEST, "Diode test practice"),
                Hazard("Never bridge out a pilot relay or fit a diode at the starter end to 'get it going'. That removes the proof the machine is earthed.")
            )
        ),
        lesson(
            "rel3", "Electronic overload relays", "🌡️", 12, Level.INTERMEDIATE,
            page(
                "Three types you'll see",
                bullets(
                    "Sprecher+Schuh CEP7: clips under the contactor. Dial in amps (e.g. 5.4–27 A, or 1–5 A when fed from CTs), blue RESET, red TEST, trip-status window; 95-96 NC trip, 97-98 NO alarm",
                    "Allen-Bradley 193-EF1A (older): Ie set by adding up DIP switch values; second DIP bank sets the trip time at 6 × Ie; TEST (6 × Ie) and RESET; LEDs for power, thermal overload, phase loss/unbalance and PTC (motor thermistor)",
                    "NHP NEF-160: Ie = 33 + the DIP switches that are ON (1, 2, 4… 64), so 33–160 A; two CLASS switches pick trip class 5/10/20/30; LEDs PTC, I>>, T>>, unbalance; TEST/RESET"
                ),
                P("Remote reset modules (e.g. A-B 193-RB1, 24–48 V AC/DC) let the door O/L RESET button reset the overload without opening the box."),
                Tip("Write the setting on the relay ('Ie = __ A') after you set it. Many relays have a box printed for exactly that.")
            ),
            page(
                "Trip class",
                P("Trip CLASS = the most seconds the overload may take to trip at 7.2 × its setting (from cold). Class 10 → within 10 s. Class 20 → within 20 s. Class 5 → within 5 s."),
                bullets(
                    "Class 10: normal for most motors",
                    "Class 20/30: heavy, slow-starting loads (big fans, conveyors) that would nuisance-trip on class 10",
                    "Class 5 / fast: some submersible pump makers ask for quick tripping because their motors are cooled by the water and run close to their limits"
                ),
                Ask("Which trip class does the pump or fan maker specify? Check the pump/motor manual and your site standard."),
                quiz("NEF-160 with DIP switches 32 and 8 ON. What is Ie?", 2, "33 + 32 + 8 = 73 A.", "40 A", "65 A", "73 A")
            ),
            page(
                "CT-fed overloads",
                Vis(Visual.CT_OVERLOAD),
                P("Dial setting = motor FLC × CT secondary ÷ CT primary. 110 kW fan, 79 A, 100/5 CTs → 3.95 A."),
                WhyNot("Why can't you leave a CT secondary open-circuit (e.g. pulling the overload out while the motor runs)? The CT tries to keep pushing its current and can develop a dangerously high voltage across the open terminals, enough to arc, burn out the CT or shock you. Short the CT secondaries or isolate first.")
            )
        ),
        lesson(
            "rel4", "Pump snore controllers (2iB SC10, Sistec PC1)", "😴", 10, Level.INTERMEDIATE,
            page(
                "What 'snoring' is",
                P("When the sump is nearly empty, a submersible starts sucking air ('snoring'). Its flow and its current DROP. A snore controller watches motor current through a 4–20 mA current transducer (e.g. CCT40-200 with 0–10 / 0–20 / 0–50 A ranges) and pauses the pump when the current falls below the Stop Current Setpoint, before it runs dry and burns its seals."),
                Vis(Visual.SNORE, "Set the setpoint at the right time and watch it cycle."),
                P("2iB SC10: the setpoint = measured current + Current Window. Example from the manual: snoring at 50% + window 10% = setpoint 60%. Press ▲ and ▼ together (SET), or the door PUMP CURRENT SET button, WHILE the pump is snoring."),
                bullets(
                    "Display: CURRENT %",
                    "LEDs: Power, Run/Pause, Stop input/Low float, Start input/High float, Alarm, Fail to Run, Hi Current, Lo Current",
                    "DIP switches: CT range, target pump cycle time, alarm conditions, float contact type, current window"
                )
            ),
            page(
                "Setting it on the box",
                order(
                    "Order the door procedure (from a typical setup plate)",
                    "Switch the pump to AUTO",
                    "Let it run until the water has dropped and the pump is drawing air",
                    "Press PUMP CURRENT SET: this becomes the new setpoint",
                    "Watch it pause, then restart",
                    "Close and lock the cover over the set button",
                    "Repeat now and then: as pumps wear, their currents change"
                ),
                P("The older Sistec PC1 does the same job with a SET CURRENT knob, Power/Rest LEDs, a CT input and an NC relay. The SC10 is sold as a retrofit replacement for it."),
                WhyNot("Why not set it while the pump is pumping normally? The setpoint would land ABOVE normal running current. The pump would stop straight away, restart, stop… short-cycling, which overheats the motor and can trip the overload."),
                quiz("Pump draws 70% when pumping and 45% when snoring. Window is 10%. Pressed SET while snoring. Setpoint?", 1,
                    "45 + 10 = 55%. That sits between 45 (snoring) and 70 (normal). ✅", "80%", "55%", "35%")
            )
        ),
        lesson(
            "rel5", "Timers, control transformers & power supplies", "⏱️", 8, Level.BEGINNER,
            page(
                "Timers",
                bullets(
                    "Delay-on-operate (e.g. Carlo Gavazzi DAA01): coil on → waits the set time → contact changes. Used for no-flow timers",
                    "Multifunction (e.g. Eaton TRL04): pick the function (E = on-delay, R = off-delay, Wu, Bp…) plus time range and fine dial",
                    "Asymmetric repeat-cycle (e.g. Eaton TRW27): separate ON and OFF times that repeat, like '2 ON 4 OFF'"
                ),
                Tip("Time = range knob × fine dial. A timer set on '1 h' instead of '1 s' looks 'dead'. Check the range first.")
            ),
            page(
                "Where the 24 V comes from",
                P("A control transformer drops 1000 V to 24 V AC (some boxes also have a 110 V winding). Its primary is fed through two small 1000 V fuses (e.g. 6 A links in 63 A-rated 1200 V carriers). Its secondary feeds small control MCBs."),
                Deep("Many of these transformers are labelled NON-short-circuit-proof (AS/NZS 61558). They can't survive a short on their output by themselves, so the fuses and MCBs must be the right size. Don't up-size a control fuse because it 'keeps blowing'. Find the short."),
                P("DC power supplies (e.g. Weidmüller PRO ECO 48 V 5 A) have an adjust pot (42–56 V), a DC OK LED (green = OK, yellow = over 90% load, red = fault) and a DC OK relay contact (13-14). The label recommends a 5–10 A C-curve breaker on the input."),
                Hazard("Control circuits are only 24 V, but they sit right next to 1000 V terminals in the same box. Fault-finding inside a live box is live work: it needs your site's permit, risk assessment, PPE and a second person.")
            )
        ),
        lesson(
            "rel6", "Underground radio: leaky feeder", "📻", 8, Level.BEGINNER,
            page(
                "How it works",
                P("Radio waves don't travel far through rock. Mines run a 'leaky feeder': a special coax cable along the drives with slots in its shield, so it leaks signal out to the radios (and picks them up) along its whole length."),
                bullets(
                    "Head end: connects the underground system to the surface radio network",
                    "Line amplifiers (e.g. Tunnel Radio): top the signal back up every so often",
                    "Splitters (e.g. GCS GV-2SC two-way): branch the cable down another drive (the board marks branch losses like −3 dB / −6 dB)",
                    "Cable joiners and end terminations"
                ),
                Vis(Visual.LEAKY_FEEDER, "Signal strength in dB along the cable"),
                P("dB maths: −3 dB = half the power, −10 dB = a tenth, −20 dB = a hundredth. A two-way split roughly halves the power in each branch (−3 dB)."),
                Ask("In many systems, the amplifiers are powered by DC sent along the same coax. Check how YOUR system is powered and who's allowed to work on it (often a comms contractor) before you disconnect anything.")
            )
        )
    )
)

internal val ugTroubleModule = Module(
    id = "ug_trouble",
    title = "Troubleshooting Trips: Pumps, Jumbos & Starters",
    emoji = "🛠️",
    blurb = "Why submersible pumps trip on overload or earth leakage, why jumbos trip, why a starter won't start, and how to find it step by step.",
    color = 0xFFB71C1C,
    group = Group.UNDERGROUND,
    lessons = listOf(
        lesson(
            "tr1", "First rules for any trip", "🧭", 6, Level.BEGINNER,
            page(
                "Before you touch the reset",
                order(
                    "Order your first moves after a trip",
                    "Read every lamp and every relay LED: which protection tripped?",
                    "Write it down (time, lamps, what the machine was doing)",
                    "Ask the operator what happened just before",
                    "Check that the area and machine are safe (water, damaged cable, smell, smoke)",
                    "Only then decide: one permitted reset, or isolate and fault-find"
                ),
                Hazard("A protection trip means something was wrong. Resetting again and again onto an earth fault or a short puts energy into the fault every time. Cables burn, plugs explode, and water can become live."),
                Ask("What is your site's rule for resetting after an E/L or pilot trip (e.g. no reset without an electrician, or one reset only)? Follow it exactly."),
                Tip("Different lamps point to different causes. O/L = too much current for too long (usually mechanical/hydraulic). E/L = insulation broke down (usually water or damage). Pilot = earth path broken (plug, cable, diode, interlock).")
            )
        ),
        lesson(
            "tr2", "Why submersible pumps trip on overload", "🔥", 12, Level.INTERMEDIATE,
            page(
                "The usual suspects",
                P("Overload trips mean the motor drew more current than its setting for long enough. Group the causes:"),
                bullets(
                    "Mechanical: impeller jammed or clogged (rock, mud, rags, fines), worn or seized bearings, worn wear-ring rubbing",
                    "Load: pumping thick slurry/mud instead of water (heavier liquid = more power)",
                    "Hydraulic: not enough head. Discharge hose burst, uncoupled or very short, so the pump runs at 'run-out', moving a lot of water and drawing more current",
                    "Electrical: LOST PHASE (blown fuse, burnt contactor tip, bad plug pin, damaged core). The other two phases carry much more current, so it trips on overload/phase-loss",
                    "Electrical: low voltage (long or undersized cable, big loads starting nearby) → more current for the same power",
                    "Settings: overload set below FLC, wrong CT maths, too-fast trip class",
                    "Cycling: too many starts per hour (floats too close, snore setpoint set wrong). Each start heats the motor and the overload's thermal memory"
                ),
                WhyNot("A BLOCKED or kinked discharge usually makes a centrifugal pump draw LESS current (less flow = less work), not more. If the current is low and the pump isn't moving water, think blocked discharge, wrong rotation or air-locked, not 'overloaded'.")
            ),
            page(
                "Find it",
                faults(
                    "Look at the relay: did it trip on thermal overload, phase loss/unbalance, or PTC (motor thermistor = motor got hot)?",
                    "Check the setting against the nameplate FLC (do the CT maths if CT-fed)",
                    "If your site allows a test run: clamp all THREE phases while it runs. Compare with FLC and with each other. One phase near zero = lost phase. All high = mechanical/hydraulic/low voltage",
                    "Measure the incoming voltage on all three phases",
                    "Isolate, lock, tag, test dead. Then IR-test the cable and motor, and measure winding resistances: all three should be nearly equal",
                    "Pull the pump (isolated): does the impeller spin freely by hand? Look for debris and wear",
                    "Check the discharge: hose connected and intact, head reasonable, check valve OK",
                    "Check rotation after any re-termination (a reversed submersible pumps poorly)"
                ),
                Vis(Visual.CT_OVERLOAD, "Is the overload set right?"),
                quiz("Clamp readings: L1 38 A, L2 39 A, L3 0 A on a 26 A-rated pump. Most likely cause?", 2,
                    "One phase missing: the other two carry far more than FLC. Check fuses, contactor tips, plug pins and the cable core.",
                    "Jammed impeller", "Overload set too low", "Lost phase (single phasing)")
            )
        ),
        lesson(
            "tr3", "Earth leakage trips on pumps", "💦", 12, Level.INTERMEDIATE,
            page(
                "Why pumps trip on E/L",
                bullets(
                    "Water in the cable: cuts, crushing by machinery or rock, cable dragged over sharp edges",
                    "Water in the plug or receptacle: plug left lying in water, damaged seals, caps missing",
                    "Motor seal failure: water gets past the mechanical seals into the motor. Many submersibles have a moisture/leakage sensor for this",
                    "Damaged cable entry on the pump",
                    "Water inside the starter box (door seal, gland, wash-down)",
                    "Long cables: normal capacitive leakage adds up and eats into a sensitive 30 mA setting"
                ),
                Hazard("A pump earth fault in a flooded sump can make the WATER live. Keep everyone out of the water until the cause is found and the circuit is isolated."),
                Tip("Hang pump plugs up off the floor and keep the caps on unused receptacles. Most pump E/L trips are water where it shouldn't be.")
            ),
            page(
                "Half-split it",
                faults(
                    "Note the E/L trip. Don't keep resetting",
                    "Isolate, lock, tag and test dead at the starter",
                    "Unplug the pump. IR-test the starter outlet side, then the cable + pump side, phase to earth and phase to phase",
                    "If the cable + pump side is low: separate the cable from the pump (at a joint or the pump's entry, if allowed) and test each on its own",
                    "Low on the cable: find the damage and replace or repair it to standard. Low on the motor: the pump goes to the workshop (wet motor, seal failure)",
                    "Everything reads good? Look at the starter: water inside, toroid wiring, relay setting/multiplier, and test the relay with the E/L TEST button",
                    "After the repair: IR test again, plug in, prove E/L TEST works, then run"
                ),
                Vis(Visual.HALF_SPLIT, "Half-splitting a fault"),
                quiz("Pump unplugged: starter side IR is fine, cable + pump reads 0.05 MΩ to earth. Next step?", 0,
                    "Split it again: test the cable and the pump motor separately to see which one holds the fault.",
                    "Separate the cable from the pump and test each", "Reset and run it", "Replace the E/L relay")
            )
        ),
        lesson(
            "tr4", "Jumbo trips: E/L, pilot & overload", "🚜", 12, Level.INTERMEDIATE,
            page(
                "Which lamp?",
                bullets(
                    "E/L FAULT on the jumbo side: insulation failure somewhere downstream. Most often the TRAILING CABLE (run over, rock fall, pinched in the reeler or boom). Then plug water, and motors in the 1000 V cabinet (power pack, compressor, water booster), heaters, or water from wash-down",
                    "PILOT HEALTHY goes out: plug not fully home, dirty/wet pins, a broken pilot core (the smallest core, so often the first to break), a failed diode, or an open interlock in the rig's pilot chain (cabinet door switches, slip-ring cover, isolator, undervoltage reset, ignition interlock)",
                    "Overload/short-circuit at the box: a seized motor, a phase lost, low voltage while starting the power pack, or a short in the cable"
                ),
                Vis(Visual.PILOT_CHAIN, "The rig's pilot chain: any open switch drops the box"),
                WhyNot("Why not just turn the jumbo E/L up from 100 mA to stop it 'nuisance tripping'? The 100 mA setting is graded for people working around the rig and in water. Turning it up hides a real insulation fault until it becomes a bigger one: a shock, a cable fire or a backfeed through the water.")
            ),
            page(
                "Find it",
                faults(
                    "Ask the operator what the rig was doing at the time (tramming, cable reeling in, booming over, washing down)",
                    "Walk the trailing cable from the box to the rig: look for crush marks, cuts and water-filled sections",
                    "Isolate at the box and at the rig. Lock, tag, test dead",
                    "Unplug at the box. IR-test the cable + rig from the plug end. Then split: disconnect the cable at the rig and test the cable alone",
                    "Cable OK → test the rig's 1000 V side section by section (main breaker, each motor circuit), using its drawings",
                    "Pilot faults: diode test at the plug, then follow the rig's pilot chain switch by switch",
                    "Fix it, retest, and prove the E/L and pilot trip before handing back"
                ),
                Ask("Jumbo 1000 V cabinets have their own isolation points and drawings. Follow the rig maker's and your site's isolation procedure. The box AND the rig both need isolating.")
            )
        ),
        lesson(
            "tr5", "Starter won't start (pump / JF / fan)", "❓", 12, Level.INTERMEDIATE,
            page(
                "Walk the rung",
                P("A box that 'does nothing' is just a series circuit with one contact open. Use the lamps to jump straight to the right part of the rung."),
                faults(
                    "No Power Available lamp: is the main isolator on? Upstream outlet/breaker on? Control transformer fuses or control MCB tripped? (The breaker's aux contact also feeds the control. Breaker off = no control.)",
                    "Power on but no Pilot Healthy: plug, cable pilot, diode, machine interlocks (see the pilot lesson)",
                    "E/L or O/L Fault lamp lit: find the cause, then reset",
                    "Stop button or E-stop latched in? Twist/pull to release",
                    "Selector position: MANUAL / AUTO / FLOAT. In AUTO the pump controller may be PAUSED (Run/Pause LED). In FLOAT the start float may not be up",
                    "Mono: No Flow fault latched?",
                    "Fan: selector on FAN 1 only? Delay-start timer still running (white lamp)? Fan 1 overload drops BOTH fans on this drawing",
                    "Contactor doesn't pull in with everything healthy: check the coil supply and the control relay. This is live testing. Only do it if you're authorised, under the site's procedure"
                ),
                Vis(Visual.BOX_DOOR, "Use the recreated doors to practise reading the lamps."),
                Order("A pump box has Power and Pilot lamps on, no faults, but won't start in AUTO. Order your checks.", listOf(
                    "Check the selector is really in AUTO",
                    "Look at the pump controller: Run/Pause LED and current %",
                    "Check the start float / level signal",
                    "Try MANUAL start (if permitted) to prove the contactor circuit",
                    "If manual works, the fault is in the auto/float path"
                ))
            ),
            page(
                "Control fuse keeps blowing",
                bullets(
                    "Shorted contactor or relay coil (measure the coil resistance, isolated)",
                    "Pinched control wire in the door hinge loom (the door flexes every time it opens)",
                    "Water in a door button or lamp",
                    "Wrong fuse/MCB size fitted earlier"
                ),
                WhyNot("Why not fit a bigger control fuse? The small control transformer is often non-short-circuit-proof. The fuse is what stops it burning out (and burning the box). Find the short.")
            )
        )
    )
)

internal val boxFlashcards = listOf(
    Flashcard("b01", "Jumbo box E/L test resistor maths", "24 V ÷ 220 Ω ≈ 109 mA > 100 mA setting → trips", "ug_boxes"),
    Flashcard("b02", "Pump starter E/L test resistor maths", "24 V ÷ 680 Ω ≈ 35 mA > 30 mA setting → trips", "ug_boxes"),
    Flashcard("b03", "FLC rule of thumb at 1000 V", "About 0.7 A per kW (55 kW ≈ 40 A, 110 kW ≈ 79 A)", "ug_boxes"),
    Flashcard("b04", "CT-fed overload dial setting", "FLC × 5 ÷ CT primary. E.g. 40 A on 100/5 → 2.0 A", "ug_boxes"),
    Flashcard("b05", "E/L grading (typical label)", "NER 1000 mA 300–400 ms → DBs 300/200 → jumbo 100 → pump 30 mA", "ug_boxes"),
    Flashcard("b06", "Is pressing STOP isolation?", "No. The box can be restarted from the rig. Isolate, lock, test.", "ug_boxes"),
    Flashcard("b07", "Why stage two fan starts?", "Cuts peak starting current and voltage dip, so upstream protection doesn't trip", "ug_boxes"),
    Flashcard("b08", "Why must earth NOT pass through an E/L toroid?", "The leakage returning on it would cancel out, so the relay goes blind", "ug_relays"),
    Flashcard("b09", "MEC-1 trip Ω settings", "10, 15, 20, 25, 35, 45 Ω (±3 Ω); 50–500 ms", "ug_relays"),
    Flashcard("b10", "Pilot relay SHORT CIRCUIT LED", "Pilot to earth or diode shorted: the diode can't be seen", "ug_relays"),
    Flashcard("b11", "Trip class 10 means…", "Trips within 10 s at 7.2 × setting", "ug_relays"),
    Flashcard("b12", "Snore controller setpoint", "Measured current while SNORING + window (e.g. 50% + 10% = 60%)", "ug_relays"),
    Flashcard("b13", "Open CT secondary danger", "Very high voltage across the open terminals. Short it or isolate first", "ug_relays"),
    Flashcard("b14", "−3 dB means…", "Half the power", "ug_relays"),
    Flashcard("b15", "Pump O/L: one phase reads 0 A", "Single phasing: check fuses, contactor tips, plug pins, cable", "ug_trouble"),
    Flashcard("b16", "Most common pump E/L cause", "Water: damaged cable, wet plug, or motor seal failure", "ug_trouble"),
    Flashcard("b17", "Blocked discharge on a centrifugal pump", "Current usually DROPS, not rises", "ug_trouble"),
    Flashcard("b18", "Most common jumbo E/L cause", "Damaged trailing cable (run over, crushed, pinched)", "ug_trouble")
)
