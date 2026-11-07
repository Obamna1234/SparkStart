package com.sparkstart.app.content

import com.sparkstart.app.data.*

internal val ugPowerModule = Module(
    id = "ug_power",
    title = "The Underground Power System",
    emoji = "🏭",
    blurb = "How mine power is earthed (IT system), substations, DBs, starters, cuddies, cables and plugs, and why it's all built the way it is.",
    color = 0xFF00838F,
    group = Group.UNDERGROUND,
    lessons = listOf(
        lesson(
            "pow1", "The first fault must be small", "🛡️", 9, Level.INTERMEDIATE,
            page(
                "A different earthing philosophy",
                P("A house uses the MEN system: a fault makes a huge current so the breaker trips. Underground, that big current flowing through wet ground, rock and machine frames would be dangerous on its own."),
                P("So underground power is designed around one idea: the FIRST earth fault should be a LOW-energy fault. That's done with an IT (impedance-earthed) system. The transformer star point goes to earth through a resistor that limits earth-fault current to a small value."),
                Vis(Visual.NER),
                Std("WA mines practice / AS/NZS 4871.1", "Underground power distribution typically uses an IT (impedance-earthed) system. Surface LV is typically TN-S. Touch, step and transfer voltage limits are addressed in the AS/NZS 4871 series."),
                WhyNot("Why can't underground just use MEN like a house? With a solid earth, an earth fault on a trailing cable could push thousands of amps through earth paths near people standing in water, causing dangerous touch and step voltages. Limiting the fault current keeps those voltages low while sensitive earth-leakage protection trips.")
            ),
            page(
                "Earth leakage limits",
                bullets(
                    "Circuits supplying portable gear and hand tools: 30 mA (personal protection RCDs)",
                    "Low-voltage circuits underground: earth leakage protection that trips at no more than about 1 A",
                    "High-voltage circuits underground: no more than about 2 A",
                    "Earth leakage protection on ALL circuits underground"
                ),
                P("Earth leakage devices underground are tested (injection or function test) about monthly, more often if your supervisor decides."),
                Ask("Exact trip settings are engineered for each outlet and site, and recorded. Never change a protection setting unless you're authorised to."),
                quiz("Why do underground systems limit earth-fault current with a resistor?", 2, "Limiting the current keeps touch and step voltages on frames and wet ground low, so the first fault is low energy.", "To save power", "To make breakers trip slower", "To keep touch and step voltages low during an earth fault", "Because copper is expensive")
            )
        ),
        lesson(
            "pow2", "Substations, DBs and starters", "🗄️", 9, Level.BEGINNER,
            page(
                "From the shaft to the face",
                bullets(
                    "HV feed (e.g. 11 kV) comes down the decline, shaft or a service hole",
                    "Skid-mounted SUBSTATIONS step it down to the mine distribution voltage (often 1000 V)",
                    "Distribution boards (DBs) split it out to several circuits",
                    "STARTER PANELS (outlets) feed one machine each: jumbo, fan, pump. Many have a restrained receptacle.",
                    "Smaller transformers make 415/240 V for lighting, workshops and small power"
                ),
                P("Modern multifunction outlet starters combine the circuit breaker, contactor and all the protection electronics in one box, with a touchscreen to set Normal, Fan or Pump mode.")
            ),
            page(
                "Cuddies and hanging panels",
                bullets(
                    "Panels live in a cut-out called a CUDDY, with a level floor, no trip hazards, and compliant ground support",
                    "Panels hang from rated eyebolts/bolts. Small panels on at least two 6 mm chains with rated shackles; DBs and bigger panels on at least three eyebolts with 8 mm chain.",
                    "Hung level, not sticking out past the cuddy",
                    "Lifted with a rated crane or a tool carrier (IT) with a jib, by a ticketed operator",
                    "Every starter with a receptacle needs an anchor point for the trailing cable"
                ),
                WhyNot("Why can't you hang a starter off the same hooks as the water and air lines? Service hooks aren't rated for electrical gear, and a falling or dragged panel is a crush and electrical hazard. Electrical gear gets dedicated, rated supports."),
                quiz("When connecting a newly installed panel, which end gets connected FIRST?", 0, "Connect the LOAD end first, then the supply end. You're never working on a cable that's already connected to a live supply.", "The load end", "The supply end", "Both at once", "It doesn't matter")
            )
        ),
        lesson(
            "pow3", "Cables, plugs and couplers", "🔌", 8, Level.BEGINNER,
            page(
                "Common underground cables",
                bullets(
                    "SWA (steel wire armoured) XLPE/PVC multicore: fixed cables hung in the backs",
                    "Type 241.1 reeling/trailing cable: flexible, with screened power cores, earth and a PILOT core. Used for jumbos, Solos and pumps.",
                    "Restrained plugs and receptacles (commonly 150 A and 60 A): the plug is held in so it can't be pulled out under load",
                    "Back-to-back couplers: join two restrained-plug cables",
                    "11 kV XLPE: HV feeders to substations"
                ),
                Vis(Visual.PILOT_CORE)
            ),
            page(
                "Bagged and tagged",
                P("A cable that's disconnected but left in place gets BAGGED AND TAGGED: all phase conductors shorted together AND to earth, covered, and tagged."),
                WhyNot("Why short the cores of a dead cable to earth? If someone ever connects the other end by mistake, the short makes the protection trip instantly instead of leaving a live cable end hanging in the drive. It also drains any induced or capacitive charge."),
                quiz("A disconnected cable left in situ must be…", 1, "Bagged and tagged: cores shorted together and to earth, covered and labelled.", "Cut off at the panel", "Bagged and tagged (cores shorted to earth)", "Taped up with electrical tape", "Left as is")
            )
        )
    )
)

internal val ugProtectionModule = Module(
    id = "ug_protection",
    title = "Outlet Protection Explained",
    emoji = "🧠",
    blurb = "Earth leakage, earth continuity, earth fault lockout, frozen contact and more: what each trip means and how to deal with it.",
    color = 0xFF283593,
    group = Group.UNDERGROUND,
    lessons = listOf(
        lesson(
            "prot1", "The protection family", "🛡️", 10, Level.INTERMEDIATE,
            page(
                "What a mining outlet watches",
                bullets(
                    "EARTH LEAKAGE (EL): current leaking to earth above the set level → trip",
                    "EARTH CONTINUITY (EC): the pilot/earth loop to the machine is broken (open) or shorted → trip",
                    "EARTH FAULT LOCKOUT (EFLO): BEFORE closing, checks the outgoing cable's insulation to earth. Fault → won't close.",
                    "FROZEN CONTACT (FC): contactor told to open but voltage is still on the outgoing side (welded contacts) → trips the circuit breaker",
                    "OVERLOAD (OL), SHORT CIRCUIT (SC), UNDERVOLTAGE (UV), CURRENT IMBALANCE (CB), UNDERCURRENT (UC, e.g. pump running dry)"
                ),
                Std("AS/NZS 2081", "Electrical protection devices for mines and quarries. For example, frozen-contact relays are required to operate at 24 V or below."),
                P("Equipment fed by a flexible (trailing/reeling) cable must have earth continuity, earth fault lockout and frozen contact protection, on top of short-circuit and earth-leakage protection.")
            ),
            page(
                "See it work",
                Vis(Visual.OUTLET_START, "Hide a fault, press START, and see which protection catches it."),
                quiz("An outlet refuses to close and shows EFLO. What does that tell you?", 1, "Earth Fault Lockout found low insulation to earth on the outgoing cable or machine before closing.", "The pilot loop is open", "There's an insulation fault to earth on the outgoing side", "The contactor is welded", "The fan is backwards")
            ),
            page(
                "Settings differ by outlet type",
                P("Example settings from typical practice (yours may differ):"),
                bullets(
                    "Pump outlet: earth leakage ~30 mA in ~50 ms (pumps sit IN water); earth continuity trip ~15 Ω; undercurrent ON to catch dry-running",
                    "Jumbo outlet: earth leakage ~100 mA in ~50 ms; earth continuity trip ~25 Ω",
                    "Fan outlet: earth leakage ~200 mA in ~200 ms; pulse/burp start and post-blast delay settings",
                    "Short circuit commonly around 6× full-load current in ~40 ms; undervoltage ~90%"
                ),
                Deep("Why is the pump outlet the most sensitive? A submersible pump and its cable are submerged in water that people stand in, so even small leakage matters most there. Fans are fixed, armoured installations, so they get less sensitive settings to avoid nuisance trips. Each setting is a balance between protecting people and keeping critical gear (like ventilation) running."),
                Ask("Protection settings are designed and set by authorised people and recorded. Check your site's settings sheet. Never adjust a setting to 'stop it tripping'.")
            )
        ),
        lesson(
            "prot2", "After a trip: resetting", "🔁", 7, Level.INTERMEDIATE,
            page(
                "Reset rules",
                bullets(
                    "First, LOOK: inspect for damage or an obvious fault before re-energising",
                    "If it trips again after energising, DON'T keep resetting. Investigate and fix first.",
                    "Some trips (like overload, undervoltage, earth continuity, EFLO) can be cleared with a general reset once the cause is gone",
                    "Serious trips (earth leakage, frozen contact, short circuit, contactor faults) usually need an AUTHORISED reset by a competent electrical worker",
                    "Only authorised electrical people investigate and restore power"
                ),
                WhyNot("Why can't you just keep hitting reset on an earth-leakage trip until it holds? Every reset puts the fault back on, maybe through water, a damaged cable or a person. If it 'holds' eventually, the fault is still there, just intermittent. Find it first."),
                Vis(Visual.HALF_SPLIT)
            )
        ),
        lesson(
            "prot3", "The pilot circuit on a rig", "⛓️", 9, Level.INTERMEDIATE,
            page(
                "One loop, lots of parts",
                P("On a drill rig the pilot loop doesn't just run down the cable. It passes through several safety parts on the machine. ALL must be healthy before the supply outlet will power the rig."),
                Vis(Visual.PILOT_CHAIN, "Open any part and watch the whole loop drop out."),
                faults(
                    "Rig won't energise: check the outlet. Is it showing an earth continuity trip?",
                    "Isolated: diode test from the rig's plug (pilot to earth)",
                    "Check the slip-ring cover switch is made and the cover is on",
                    "Check cabinet door switches and the main isolator",
                    "Check the diesel ignition is OFF (anti-tram interlock breaks the pilot)",
                    "Press the pilot reset button(s); check undervoltage relays on the breakers",
                    "Check the plug pins and cable for damage"
                ),
                Tip("Some cabinet door limit switches can be overridden for CONTROLLED fault-finding. That's only for authorised people following procedure, and they must be put back.")
            )
        )
    )
)

internal val ugTestingModule = Module(
    id = "ug_testing",
    title = "Testing Outlets & Installations",
    emoji = "🧪",
    blurb = "Mining outlet testers, earth leakage injection, frozen contact, earth continuity, diode tests, and the AS/NZS 3000 tests for new installations.",
    color = 0xFF2E7D32,
    group = Group.UNDERGROUND,
    lessons = listOf(
        lesson(
            "tst1", "The outlet test unit", "📟", 10, Level.INTERMEDIATE,
            page(
                "What it tests",
                P("A mining outlet tester plugs into the starter's receptacle and lets you test the outlet's protection:"),
                bullets(
                    "Earth leakage LOCKOUT: puts a resistor from a phase to earth (diode or other pilot mode) and checks the outlet locks out and won't start",
                    "Frozen contact: slowly raises a voltage on a phase until the breaker trips. It must stay at 24 V or below.",
                    "Earth continuity: simulates an OPEN pilot, then a SHORTED pilot, and checks both trip",
                    "Earth leakage: injects a set leakage current and measures the trip time in milliseconds (local and upstream)",
                    "Phase information: phase voltages and phase rotation"
                ),
                Hazard("The tester's plug pins are LIVE during parts of the testing. Keep the leads connected until ALL tests are done, and isolate both the tester and the starter before unplugging."),
                Tip("Fit a commissioning tag while testing and make sure everyone knows. If an earth leakage time looks odd (electrical noise), repeat at least twice per phase and throw out obviously bad readings.")
            )
        ),
        lesson(
            "tst2", "The diode test", "🔺", 6, Level.BEGINNER,
            page(
                "Proving the pilot loop",
                Vis(Visual.DIODE_TEST),
                quiz("You diode-test a pump plug and get about 0.55 V one way and OL the other. That means…", 0, "Healthy. The diode conducts one way only.", "Healthy pilot diode", "Shorted diode", "Open pilot", "Bad meter")
            )
        ),
        lesson(
            "tst3", "Testing a new installation", "📋", 10, Level.INTERMEDIATE,
            page(
                "Before you design",
                P("Before running cable, the circuit is designed considering: load, prospective fault current, voltage drop, cable size, earth leakage protection, and overload and short-circuit settings. It's also planned with mining for traffic and isolations."),
                Vis(Visual.VOLTAGE_DROP)
            ),
            page(
                "The minimum AS/NZS 3000 tests",
                order(
                    "Put the new-installation tests in order",
                    "Visual check (touch protection, labels, terminations, settings)",
                    "Earth continuity (≤ 0.5 Ω with one earth disconnected)",
                    "Insulation resistance at the correct test voltage",
                    "Polarity / phase rotation and pilot and earth-leakage function",
                    "Correct circuit connections",
                    "Earth fault-loop impedance (test or calculate)"
                ),
                Std("AS/NZS 3000 Section 8 + Table 8.1", "Mandatory verification tests. On 1000 V circuits a loop-impedance tester usually isn't available, so the loop impedance is CALCULATED and checked against Table 8.1."),
                Tip("For the earth continuity test, connect the whole earth system except ONE earth at the starter end, null your meter, and measure across that gap (including the SWA gland). Then reconnect it!")
            ),
            page(
                "1000 V vs 415/240 V circuits",
                bullets(
                    "1000 V: a test plug confirms phase rotation; an outlet test unit confirms pilot function and earth leakage operation, local AND upstream",
                    "415/240 V: polarity plug, earth leakage injection tester, earth fault-loop tester"
                ),
                Tip("Record every result on the installation sheet. The supervisor updates the drawings and asset register, and installing work is recorded as required locally.")
            )
        )
    )
)
