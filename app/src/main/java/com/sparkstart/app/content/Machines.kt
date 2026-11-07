package com.sparkstart.app.content

import com.sparkstart.app.data.*

internal val machinesModule = Module(
    id = "machines",
    title = "Motors, Transformers & 3-Phase",
    emoji = "⚙️",
    blurb = "Magnetism, three-phase power, transformers, every common motor type, starters and protection.",
    color = 0xFF6D4C41,
    group = Group.CORE,
    lessons = listOf(
        lesson(
            "m1", "Magnetism made simple", "🧲", 6, Level.BEGINNER,
            page(
                "Current makes magnetism",
                P("Any wire carrying current has a magnetic field around it. Wind the wire into a coil and the field gets much stronger. Put iron inside and it's stronger again. That's an electromagnet."),
                P("And it works backwards: a CHANGING magnetic field near a coil makes (induces) a voltage in it. That one idea runs motors, generators and transformers."),
                Tip("Electricity → magnetism → movement = motor. Movement → magnetism → electricity = generator.")
            ),
            page(
                "Why coils are special in AC",
                P("Coils (inductors) resist changes in current. On AC they oppose the flow more than their plain resistance suggests. This is called inductive reactance."),
                Hazard("When you switch off a coil (a contactor coil, motor or solenoid) the collapsing field can make a big voltage spike. That's why some control circuits have suppressors fitted."),
                quiz("What does a changing magnetic field do to a nearby coil?", 0, "It induces a voltage in the coil. That's the basis of transformers and generators.", "Induces a voltage in it", "Makes it cold", "Nothing at all", "Turns it into a magnet permanently")
            )
        ),
        lesson(
            "m2", "Three-phase power", "🔺", 8, Level.BEGINNER,
            page(
                "Three supplies working together",
                Vis(Visual.THREE_PHASE),
                bullets(
                    "Phase to neutral: 230 V",
                    "Phase to phase: 400 V (that's 230 × √3)",
                    "Used for big loads, motors, commercial and industrial sites, mines"
                ),
                P("If the three phases are evenly loaded (balanced), the currents cancel in the neutral and it carries little or nothing.")
            ),
            page(
                "Phase rotation",
                P("The order the phases peak in (A-B-C) is the phase ROTATION. Swap any two and you reverse the rotation, and every three-phase motor on it spins backwards."),
                Hazard("A motor spinning the wrong way can wreck pumps and fans, or move machinery the wrong way and injure people. Check rotation with a phase rotation tester before connecting motors."),
                WhyNot("Why can't you lose one phase and keep running? A three-phase motor that loses a phase ('single phasing') may keep turning but draws much more current in the remaining windings and overheats. Good protection trips on phase loss."),
                quiz("What's the voltage between two phases in a standard Australian three-phase supply?", 2, "400 V between phases, 230 V phase to neutral.", "230 V", "240 V", "400 V", "690 V")
            )
        ),
        lesson(
            "m3", "Transformers", "🔄", 8, Level.INTERMEDIATE,
            page(
                "How they work",
                Vis(Visual.TRANSFORMER),
                P("Two coils on one iron core. AC in the first (primary) coil makes a changing field that induces a voltage in the second (secondary) coil. Voltage ratio = turns ratio."),
                bullets(
                    "Step-down: fewer turns on the secondary (e.g. 230 V → 12 V for lighting)",
                    "Step-up: more turns on the secondary",
                    "Isolating (1:1): same voltage, but the secondary isn't connected to the supply earth system"
                )
            ),
            page(
                "Three-phase and mine transformers",
                P("Big three-phase transformers feed buildings and mines. Underground mines often use portable/transportable substations to step high voltage down to the machine voltage. You'll meet these in the Mining section."),
                Hazard("A transformer works BOTH ways. Feed 230 V into the low-voltage side and it comes out at high voltage on the other side. That's how generator back-feed kills lineworkers."),
                Vis(Visual.BACKFEED),
                Deep("Transformers have losses: copper losses (heat in the windings, rising with load current) and iron losses (in the core, roughly constant whenever energised). Big ones are oil-filled or have fans to get rid of heat. They also have a vector group (e.g. Dyn11) that describes how the windings are connected and the phase shift. That matters when paralleling transformers.")
            )
        ),
        lesson(
            "m4", "DC motors", "🔋", 7, Level.INTERMEDIATE,
            page(
                "How a DC motor spins",
                Vis(Visual.DC_MOTOR),
                bullets(
                    "Field: permanent magnets or field coils",
                    "Armature: the spinning coil",
                    "Commutator + brushes: flip the current every half-turn"
                )
            ),
            page(
                "Types and faults",
                bullets(
                    "Series wound: huge starting torque (starter motors, some hoists)",
                    "Shunt wound: steadier speed",
                    "Permanent magnet: small motors, tools, car accessories",
                    "Brushless DC (BLDC): electronics do the commutation. Modern fans, tools and EVs."
                ),
                Hazard("A series-wound DC motor with no load can overspeed until it destroys itself. Never run one unloaded."),
                faults(
                    "Check supply voltage at the motor terminals",
                    "Check brushes for wear and that they're seated on the commutator",
                    "Look at the commutator for burning, pitting or flashover",
                    "Isolated: check armature and field resistance and insulation",
                    "Check bearings and that it turns freely"
                )
            )
        ),
        lesson(
            "m5", "Single-phase motors", "🌀", 8, Level.INTERMEDIATE,
            page(
                "The starting problem",
                P("Single-phase AC makes a field that just pulses back and forth. It doesn't rotate, so a plain single-phase motor can't start on its own. The fix is a second winding, offset in time, to create a rotating field to get it going."),
                bullets(
                    "Split-phase: start winding + centrifugal switch that drops it out once running",
                    "Capacitor-start: a capacitor in the start winding gives more starting torque (compressors, pumps)",
                    "Capacitor-start capacitor-run: two capacitors, smoother and more efficient",
                    "Permanent split capacitor (PSC): run capacitor always in (fans, air-con fan motors)",
                    "Shaded pole: tiny, weak, cheap (small fans, exhaust fans)",
                    "Universal motor: has brushes and runs on AC or DC (drills, grinders, vacuums)"
                )
            ),
            page(
                "Common faults",
                faults(
                    "Motor hums but won't start: suspect the start capacitor, the centrifugal switch, or a seized shaft",
                    "Isolate and discharge capacitors before touching them",
                    "Check winding resistances (start winding is usually higher than the run winding)",
                    "Insulation test windings to frame",
                    "Check overload / thermal cut-out and bearings"
                ),
                Hazard("Capacitors store charge after power is off. Discharge them safely before handling."),
                quiz("A capacitor-start motor hums but won't turn on its own. Spin it by hand and it runs. Most likely fault?", 1, "It runs once moving, so the run winding is fine. The starting circuit (capacitor or centrifugal switch) has failed.", "Run winding open circuit", "Start capacitor or centrifugal switch", "Too much voltage", "The fan is too big")
            )
        ),
        lesson(
            "m6", "Three-phase induction motors", "♻️", 9, Level.INTERMEDIATE,
            page(
                "The workhorse of industry",
                Vis(Visual.INDUCTION_MOTOR),
                P("Three-phase induction motors are simple, tough and everywhere: pumps, fans, conveyors, compressors and the hydraulic power packs on mining rigs."),
                P("Speed depends on supply frequency and number of poles: 2-pole ≈ 3000 rpm, 4-pole ≈ 1500 rpm (minus slip) at 50 Hz.")
            ),
            page(
                "Squirrel cage vs wound rotor",
                bullets(
                    "Squirrel cage: rotor bars shorted at each end. No brushes. Most common.",
                    "Slip-ring / wound rotor: rotor windings brought out through slip rings for added resistance at start (big old machines)"
                ),
                P("Nameplate info to know: voltage, full-load current (FLC), kW, rpm, power factor, connection (star/delta), IP rating, duty."),
                faults(
                    "Read the nameplate. Know the FLC and connection.",
                    "Check all three phases are present and balanced at the motor",
                    "Isolated: check winding resistances are equal across all three windings",
                    "Insulation test windings to frame and between windings",
                    "Check for overload: a jammed or worn driven load, bad bearings",
                    "Measure running current on each phase with a clamp meter and compare to the FLC"
                )
            )
        ),
        lesson(
            "m7", "Motor starting & protection", "🚦", 10, Level.ADVANCED,
            page(
                "Why starting matters",
                P("A motor started direct-on-line (DOL) can draw about 6–8 times its full-load current for a few seconds. On a big motor that can dip the voltage for everyone else and stress the gear."),
                Vis(Visual.STAR_DELTA),
                bullets(
                    "DOL: contactor + overload. Simple, high starting current.",
                    "Star-delta: starts in star (about 1/3 the current and torque), then switches to delta",
                    "Soft starter: ramps the voltage up smoothly",
                    "VSD / VFD (variable speed drive): changes frequency to control speed. Gentle starts and big energy savings on fans and pumps."
                )
            ),
            page(
                "Protection",
                bullets(
                    "Short-circuit protection: breaker or fuses",
                    "Overload relay: set to the motor's FLC. Trips on sustained overcurrent.",
                    "Phase-loss / phase-failure protection",
                    "Thermistors in windings on bigger motors",
                    "Earth leakage protection (very important in mines)",
                    "A local isolator so the motor can be isolated near where you're working"
                ),
                Std("Clause 4.13 (motors)", "Motor requirements, including control, isolation and protection, are in Section 4 of the Wiring Rules. This area changed in Amendment 3, so check your copy."),
                WhyNot("Why can't you just turn the overload up when it keeps tripping? The overload is protecting the motor windings. Turning it up hides the real problem (jammed load, low voltage, lost phase, failing bearings) and the motor burns out."),
                Hazard("VSDs keep dangerous DC voltage in their capacitors for minutes after isolation. Follow the manufacturer's wait time and test before touching. They can also cause leakage currents that need the right RCD type."),
                faults(
                    "Motor won't start: check control circuit (stop/start buttons, interlocks, contactor coil voltage) before the motor itself",
                    "Contactor pulls in but motor doesn't run: check main contacts and all three phases out of the contactor",
                    "Overload trips: measure current on all phases, check the load, check settings against nameplate FLC",
                    "VSD fault code: read it from the drive and look it up in the manual"
                )
            )
        )
    )
)

internal val rulesModule = Module(
    id = "rules",
    title = "Using the Wiring Rules",
    emoji = "📘",
    blurb = "How AS/NZS 3000 is laid out, WA rules, licensing, and how to look things up fast.",
    color = 0xFF3949AB,
    group = Group.CORE,
    lessons = listOf(
        lesson(
            "r1", "How AS/NZS 3000 is laid out", "📖", 7, Level.BEGINNER,
            page(
                "Two parts",
                P("AS/NZS 3000 (the Wiring Rules) has two parts:"),
                bullets(
                    "Part 1: scope, application and fundamental principles. The 'what must be achieved'.",
                    "Part 2: the installation and safety practices that 'deem to comply' with Part 1. The 'how'."
                ),
                P("Part 2 sections:"),
                bullets(
                    "Section 2: general arrangement, control and protection (circuits, switchboards, RCDs, breakers)",
                    "Section 3: selection and installation of wiring systems (cables, colours, installation methods)",
                    "Section 4: selection and installation of electrical equipment (outlets, lights, motors, appliances)",
                    "Section 5: earthing arrangements and earthing conductors (MEN)",
                    "Section 6: damp situations (bathrooms, pools and more)",
                    "Section 7: special electrical installations (safety services, generators and others)",
                    "Section 8: verification (inspection and testing)",
                    "Appendices: extra info and tables (e.g. Appendix C, maximum demand)"
                ),
                Std("Current edition", "AS/NZS 3000:2018, incorporating Amendments 1 (2020), 2 (2021) and 3 (May 2023). Check whether there's been a newer amendment or ruling since.")
            ),
            page(
                "How to find things fast",
                bullets(
                    "Start with the index at the back, not the contents page",
                    "Learn the definitions in Section 1. Words like 'final subcircuit' and 'exposed conductive part' have exact meanings.",
                    "Tab your copy by section",
                    "Read the notes under clauses. They often explain the reason.",
                    "Exam tip: questions often ask for the clause number as well as the answer"
                ),
                quiz("Which section covers testing and verification?", 3, "Section 8 is verification: inspection and testing.", "Section 2", "Section 5", "Section 6", "Section 8"),
                quiz("Which section covers earthing and the MEN system?", 1, "Section 5: earthing arrangements and earthing conductors.", "Section 3", "Section 5", "Section 7", "Appendix C")
            )
        ),
        lesson(
            "r2", "WA rules and your licence", "🪪", 6, Level.BEGINNER,
            page(
                "On top of the Wiring Rules",
                bullets(
                    "WA Electrical Requirements (WAER): WA-specific rules from the regulator and network operators. Free to download.",
                    "Electricity (Licensing) Regulations: who can do electrical work",
                    "Work Health and Safety laws: general safety duties (and mining-specific regulations for mines)",
                    "Network rules: Western Power / Horizon Power service and metering rules"
                ),
                Ask("Apprentices in WA need an electrical training licence and must be supervised. Your employer and RTO will walk you through it. Make sure yours is current and carry it.")
            ),
            page(
                "Other standards you'll hear about",
                bullets(
                    "AS/NZS 3008.1.1: cable selection (current rating, voltage drop)",
                    "AS/NZS 3017: verification (testing) guidelines",
                    "AS/NZS 3019: periodic verification of existing installations",
                    "AS/NZS 4836: safe working on low-voltage installations",
                    "AS/NZS 3760: in-service safety inspection and testing (test & tag)",
                    "AS/NZS 3012: construction and demolition sites",
                    "AS/NZS 3010: generating sets",
                    "AS/NZS 3112: plugs and socket-outlets",
                    "AS/NZS 61439 series: switchgear assemblies (switchboards)",
                    "AS 2293: emergency escape lighting and exit signs",
                    "AS/NZS 4777: inverter connections (solar); AS/NZS 5033: PV arrays; AS/NZS 5139: battery systems",
                    "AS/NZS 3003: patient areas (hospitals, medical)",
                    "AS/NZS 1768: lightning protection",
                    "Mining: AS/NZS 3007 (mines and quarries), AS/NZS 4871 (mining equipment), AS/NZS 2802 (reeling/trailing cables), AS/NZS 2081 (mining protection devices), AS/NZS 60079 series (explosive atmospheres), AS 2067 (HV substations)"
                ),
                Tip("You don't need to memorise these. Just know they exist and roughly what each one covers.")
            )
        )
    )
)
