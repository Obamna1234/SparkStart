package com.sparkstart.app.content

import com.sparkstart.app.data.*

private const val SITE = "Every mine has its own procedures, forms and settings. What's here is typical underground practice. Your site's documents and your supervisor always come first."

internal val ugStartModule = Module(
    id = "ug_start",
    title = "Underground Sparky 101",
    emoji = "⛏️",
    blurb = "Start here: your role as an apprentice, the words everyone uses, and how underground work is planned.",
    color = 0xFFF4511E,
    group = Group.UNDERGROUND,
    lessons = listOf(
        lesson(
            "ug1", "What an apprentice can and can't do", "🧑‍🎓", 8, Level.BEGINNER,
            page(
                "Three levels of supervision",
                bullets(
                    "DIRECT: your supervisor is right there with you the whole time, guiding and watching.",
                    "GENERAL: your supervisor is on the same site and close enough to help, checking in regularly.",
                    "BROAD: for familiar tasks. Your supervisor doesn't have to stay on site but checks your work at least daily."
                ),
                P("As you move through your years, you get trusted with more, but only as your supervisor sees you're competent."),
                Std("WA electrical licensing (apprentice supervision)", "These supervision levels come from WA's safe-working guidance for electrical apprentices, which sits under the Electricity (Licensing) Regulations. Employers can be stricter than the minimum."),
                Ask(SITE)
            ),
            page(
                "The big rules for years 1–3",
                bullets(
                    "You may NOT do an electrical isolation on a live installation",
                    "You CAN fit your lock and tag, once your supervisor has proven the gear dead",
                    "You CAN isolate plug-fed equipment by unplugging it (coupling/uncoupling)",
                    "Testing and fault-finding on DE-ENERGISED gear: direct supervision early on, relaxing as you progress"
                ),
                P("In your final year, you may do isolations on live installations and live testing, but only under DIRECT supervision once you've been assessed as competent."),
                WhyNot("Why can't a 1st year apprentice do a live isolation even if they 'know how'? Isolation is the moment you're closest to live parts and the most can go wrong: wrong breaker, failed tester, arc flash. Experience is part of the safety system."),
                quiz("As a 2nd year apprentice, can you unplug a pump cable from a starter to isolate it?", 1, "Yes. Isolating plug-fed equipment by uncoupling is allowed for apprentices. Live isolations on installations aren't, until final year under direct supervision.", "No, never", "Yes, coupling/uncoupling plug-fed gear is allowed", "Only on night shift", "Only if nobody's watching")
            ),
            page(
                "Your work gets checked",
                bullets(
                    "Everything an apprentice does is checked and tested by the supervising electrician before it's energised",
                    "Paperwork that needs a licence number is signed by BOTH of you",
                    "Keep your logbook/profiling up to date. Gaps can delay your licence at the end.",
                    "The Capstone assessment near the end is the final safety test before your licence"
                ),
                Tip("Never start a task you feel unprepared for. Asking 'why are we doing it this way?' is part of the job. Good supervisors want that question.")
            )
        ),
        lesson(
            "ug2", "Words you'll hear every shift", "🗣️", 8, Level.BEGINNER,
            page(
                "Dead, de-energised, isolated?",
                bullets(
                    "DE-ENERGISED: no supply connected right now, but NOT necessarily locked off. Someone could switch it on.",
                    "ISOLATED: separated from all energy sources AND made so it can't be energised by accident",
                    "POSITIVELY ISOLATED: all current-carrying conductors open, with measures (locks) so they can't re-close. Not positive if a control circuit could switch it back on.",
                    "DEAD: disconnected from all supply, but could still hold capacitive charge or have induced voltage",
                    "LIVE / ENERGISED: connected to a supply, or carrying dangerous induced or capacitive voltage"
                ),
                quiz("A starter's contactor is open but nobody has locked the isolator. Is the outgoing cable positively isolated?", 2, "No. A control circuit or a person could close it again. Positive isolation needs the isolating device open AND locked.", "Yes", "Yes, if the light is off", "No, it can be re-energised by a control circuit or person", "Only on 1000 V")
            ),
            page(
                "Live work vs live testing",
                bullets(
                    "LIVE WORK: working on or near energised parts with tools: changing a breaker live, terminating live. Generally NOT permitted. Loss of production is never a valid reason.",
                    "LIVE TESTING: using approved meters on or near energised parts to monitor, commission or fault-find. Allowed under strict procedures, PPE and competency.",
                    "FAULT FINDING: measurements to locate faults, preferably on de-energised gear"
                ),
                Std("Voltage bands", "ELV: not over 50 V AC / 120 V DC. LV: above ELV up to 1000 V AC / 1500 V DC. HV: above 1000 V AC / 1500 V DC. Underground mobile plant is commonly 1000 V, which is the TOP of low voltage."),
                Hazard("Lethal current is commonly taken as more than 10 mA AC or 300 mA DC through the body (from AS/NZS 60479 work on current effects). That's tiny.")
            ),
            page(
                "Paperwork that keeps you alive",
                bullets(
                    "WPI (workplace inspection): look for and fix hazards before starting",
                    "JHA (job hazard analysis): for jobs that are unusual or not covered by a procedure",
                    "SWMS (safe work method statement): planned steps and controls for a type of task",
                    "SWI (safe work instruction): exact steps and test criteria for a specific job (like 'test a fan')",
                    "Shift plan/log: what's planned for the shift, breakdowns, and which SWMS/WPIs apply",
                    "Electrical logbook: installations, maintenance, incidents, appointments and licences",
                    "Service sheets: where you record test results for each piece of gear"
                ),
                Tip("Hierarchy of controls, best to worst: Eliminate → Substitute → Isolate/Engineer → Administrative (procedures, signs) → PPE. PPE is the LAST line of defence."),
                order(
                    "Put the hierarchy of controls in order (best first)",
                    "Elimination", "Substitution", "Isolation / engineering", "Administration", "PPE"
                )
            )
        ),
        lesson(
            "ug3", "Restricted areas & who's who", "🚧", 6, Level.BEGINNER,
            page(
                "Keep people out",
                P("Electrical panels, starters, substations, switch rooms and transformer yards are restricted to authorised people. They're kept locked, fenced or guarded, with warning signs."),
                bullets(
                    "Every isolation point must be lockable",
                    "Every starter, DB and substation should have its as-built drawing inside",
                    "Cables and gear are labelled with source and voltage",
                    "Single-line diagrams are kept up to date after changes"
                ),
                Hazard("Within about 10 m of live overhead lines or exposed HV conductors you're in an exclusion/vicinity zone. Machines need written permission (a permit) to work there.")
            ),
            page(
                "Roles on site",
                bullets(
                    "Electrical supervisor: responsible for the site's electrical gear and people, the logbook and investigations",
                    "Electrical worker: isolate and verify, comply with AS/NZS 3000, test and record before energising",
                    "Apprentice: work within your supervision level, follow instructions, ask questions",
                    "HV switching operator: trained, assessed and APPOINTED in writing to do HV switching and permits",
                    "Safety observer: trained in CPR and rescue, watches high-risk work (testing, HV switching), can stop the job and does nothing else"
                ),
                quiz("What must a safety observer NOT do during the task?", 3, "A safety observer's only job is to watch, keep people out and be ready to isolate and rescue. Doing other work defeats the purpose.", "Know CPR", "Know where the isolation point is", "Stop the job if it gets unsafe", "Do other work at the same time")
            )
        )
    )
)

internal val ugIsolationModule = Module(
    id = "ug_isolation",
    title = "Isolation, Locks & Tags",
    emoji = "🔒",
    blurb = "LV isolation step by step, tag colours, plug-fed gear, HV switching and permits, and isolating battery-electric machines.",
    color = 0xFFD32F2F,
    group = Group.UNDERGROUND,
    lessons = listOf(
        lesson(
            "iso1", "Find every energy source", "🔎", 7, Level.BEGINNER,
            page(
                "It's not just electricity",
                P("Before isolating anything, list EVERY energy source that could hurt someone or make something move:"),
                bullets(
                    "Main isolators, circuit breakers and disconnect switches",
                    "24 V DC battery/engine isolators on mobile plant",
                    "Air, water, hydraulic, fuel and gas valves",
                    "Stored pressure (accumulators, air receivers, long water lines)",
                    "Mechanical: springs, suspended loads, rotating parts, counterweights, gravity",
                    "Stored electrical: capacitors (drives, chargers, BEVs)"
                ),
                Tip("Use drawings, single-line diagrams, cable tags, equipment labels and the manufacturer's manual. If in doubt, ask someone who knows the gear."),
                Hazard("A pump line running up a decline can hold tonnes of water under pressure even with the pump off. Drain it down before opening anything.")
            )
        ),
        lesson(
            "iso2", "Locks and tag colours", "🏷️", 6, Level.BEGINNER,
            page(
                "What each one means",
                bullets(
                    "PERSONAL LOCK: yours only. Every person working on the gear fits their own (use a scissor hasp or group box). Only YOU remove it.",
                    "DANGER TAG (red/white/black): 'Person working, do NOT operate.' One per person, per energy source. Only the person who fitted it removes it.",
                    "OUT OF SERVICE TAG (yellow/black): 'This gear is faulty / not to be used.' Anyone can fit one. Removed by a competent person once it's safe.",
                    "COMMISSIONING TAG (pink/black, common on WA mines): 'Testing in progress. Treat as LIVE.' Fitted and removed only by competent people."
                ),
                quiz("You finish your job but the machine still isn't fixed. What do you leave on it?", 2, "Remove your personal lock and danger tag (they're about YOU), but leave an Out of Service tag so nobody uses faulty gear.", "Nothing", "Your danger tag", "An Out of Service tag", "A commissioning tag"),
                Ask("Tag colours and rules can differ between sites and companies. Learn your site's isolation procedure.")
            )
        ),
        lesson(
            "iso3", "LV isolation, step by step", "🪜", 10, Level.BEGINNER,
            page(
                "Prepare",
                bullets(
                    "Tooling ready: personal lock, danger and OOS tags, lockout devices",
                    "Test gear inspected: CAT III 1000 V multimeter with rated leads, plus a proving unit",
                    "Arc-flash PPE ready (face shield, rated jacket, rated gloves)",
                    "Workplace inspection done, hazards fixed, SWMS read and signed"
                )
            ),
            page(
                "The sequence",
                order(
                    "Put the LV isolation steps in order",
                    "Notify everyone affected",
                    "Remove the loads (close valves, stop engines/generators)",
                    "Open the isolator / breaker at the point of supply",
                    "Put on arc-flash PPE",
                    "Prove the meter on a known live source",
                    "Test the load side: all phases to earth",
                    "Re-prove the meter on the known source",
                    "Fit lock, danger tag and OOS tag",
                    "Do the work"
                ),
                Tip("EVERY person working on the gear tests it themselves. Don't rely on someone else's 'it's dead'.")
            ),
            page(
                "Why phase to EARTH?",
                P("On 1000 V systems you test each phase to earth (or neutral) rather than phase to phase."),
                WhyNot("Why not test phase to phase on 1000 V gear? Phase to phase on a 1000 V system is the full 1000 V (more with spikes), right at or above the rating of a CAT III 1000 V meter. Phase to earth is lower (roughly 580 V on a 1000 V system). It keeps you inside the meter's rating and still proves each phase dead."),
                Deep("On a three-phase system, phase-to-earth voltage = phase-to-phase ÷ √3 (1000 ÷ 1.732 ≈ 577 V). Underground systems are often earthed through a resistor (an IT/impedance-earthed arrangement). A phase-to-earth reading still shows if the phase is energised."),
                quiz("Why is the meter proved on a known live source AFTER testing too?", 1, "If the meter died during your test, your 'dead' reading means nothing. Proving after confirms it was working the whole time.", "To use up the battery", "To confirm the meter didn't fail during your dead test", "Because the procedure is long", "It isn't necessary")
            ),
            page(
                "Mobile plant on a plug",
                P("For plug-fed gear (rigs, pumps), isolation = UNPLUG the cable from the starter and fit a plug lockout device, with your lock, danger tag and OOS tag on it."),
                bullets(
                    "Also isolate the machine's 24 V battery isolator and lock it",
                    "Panels without an external isolator handle need arc-flash PPE to operate",
                    "Back on: remove locks and tags (each person their own), re-energise, function test, tell everyone"
                ),
                Vis(Visual.PILOT_CORE, "Unplugging also breaks the pilot loop, so the outlet won't re-energise.")
            )
        ),
        lesson(
            "iso4", "High voltage isolation & permits", "⚡", 10, Level.ADVANCED,
            page(
                "Only appointed people",
                P("Only a trained, assessed and APPOINTED HV switching operator opens or closes HV switchgear. Apprentices may not even be allowed to act as safety observer for HV switching."),
                bullets(
                    "HV switching program: a written step-by-step sequence of every switching, testing, earthing and lockout operation, prepared and checked BEFORE switching",
                    "Access permit: issued by the switching operator to a Recipient, who controls the working party",
                    "Working party: everyone signs onto the permit and fits their own locks and tags",
                    "Operator earths and working earths: connections to earth so the conductors can't become live while you work"
                )
            ),
            page(
                "The HV sequence",
                order(
                    "Put the HV isolation sequence in order",
                    "Identify isolation points from the single-line diagram and check in the field",
                    "Prepare and approve the switching program",
                    "Notify everyone affected",
                    "Open both access points (two ways out)",
                    "Remove the load, set barriers, safety observer in position",
                    "Put on the HV switching suit",
                    "Switch in sequence, visually confirming contacts open",
                    "Prove dead with an approved HV tester (proved before and after)",
                    "Apply earths, then lock and tag",
                    "Issue the access permit; working party signs on"
                ),
                Hazard("HV can jump (flash over) air gaps without contact. Switching gear can fail violently. That's why HV switching PPE is rated around 40 cal/cm² with hood and HV gloves."),
                Tip("If the HV tester can't detect through a cover or armoured cable, prove dead downstream first (e.g. the LV side of the substation), then test at the terminals.")
            ),
            page(
                "Handing it back",
                bullets(
                    "Everyone removes their tools, earths, locks and tags and signs off the permit",
                    "The Recipient surrenders the permit",
                    "The switching operator confirms it's safe, then follows the program to restore supply"
                ),
                quiz("Who can open or close an HV circuit breaker?", 2, "Only an authorised, appointed HV switching operator, following an approved switching program.", "Any electrician", "The mine foreman", "An appointed HV switching operator", "Whoever has the key")
            )
        ),
        lesson(
            "iso5", "Isolating battery-electric machines", "🔋", 9, Level.ADVANCED,
            page(
                "More than one source",
                P("Battery-electric (BEV) drills and loaders can have several isolation points: the 1000 V supply/reeling cable, a 24 V system, and a high-voltage DC battery system (often around 650 V DC, plus a few hundred volts on some tram packs). Some also have charging and heater inlets that must be locked."),
                Hazard("DC is ALWAYS present between the batteries and their isolators, even with everything switched off. The battery IS the energy source."),
                bullets(
                    "Hazardous-voltage cabling is usually ORANGE",
                    "Isolate to the highest level the job needs",
                    "Wait the stated discharge time (often 10–15 minutes) for capacitors in inverters and chargers",
                    "Prove dead at the designated measurement points, using a meter and proving unit that can handle DC"
                ),
                WhyNot("Why can't you open an inverter cabinet the moment you hit the isolator? Drive and charger capacitors hold lethal charge for minutes. The wait time and a dead test are what make it safe."),
                Ask("Each BEV model has its own levels of isolation and procedure (from the OEM). Follow it exactly.")
            )
        )
    )
)

internal val ugPpeModule = Module(
    id = "ug_ppe",
    title = "PPE & Arc Flash",
    emoji = "🥽",
    blurb = "1000 V gloves, arc-rated clothing, face shields, HV switching suits, and testing your gloves before use.",
    color = 0xFF6A1B9A,
    group = Group.UNDERGROUND,
    lessons = listOf(
        lesson(
            "ppe1", "What to wear for LV testing", "🧤", 8, Level.BEGINNER,
            page(
                "The LV kit",
                bullets(
                    "Arc-flash rated face shield",
                    "Arc-rated jacket or clothing, at least 8 cal/cm², covering neck to below the knees",
                    "1000 V insulating gloves (Class 0) with leather protective outers",
                    "Hearing protection where required"
                ),
                P("Worn for live testing, proving dead, commissioning and adjusting live gear. Higher-rated PPE is needed where an arc-flash study says the energy is higher."),
                Hazard("Arc flash: temperatures over 20,000 °C, molten metal, toxic gas and a pressure blast that can blow panel doors off. Most likely when gear is being operated: switching breakers and contactors."),
                Tip("Take off conductive stuff before live testing: watch, rings, chains, metal-rimmed glasses, phones and pens in pockets.")
            )
        ),
        lesson(
            "ppe2", "Checking your 1000 V gloves", "🎈", 7, Level.BEGINNER,
            page(
                "Before every use",
                order(
                    "Put the glove check in order",
                    "Take gloves and outers out of their bag",
                    "Check the test date (re-test or replace within 12 months of first opening)",
                    "Look over both gloves for cuts, cracks, holes, swelling or chemical damage",
                    "Trap air and roll the cuff to pressurise the glove",
                    "Listen and feel for air leaking out",
                    "Fit leather outers over the top"
                ),
                Std("AS/NZS IEC 60903:2022", "Live working: electrical insulating gloves. Covers testing, storage and care."),
                WhyNot("Why can't you skip the air test 'just this once'? A pinhole you can't see can let current straight through to your hand. The air test is how you find it. If EITHER glove fails, the pair is out.")
            ),
            page(
                "Looking after them",
                bullets(
                    "Store in a proper bag. Not folded, squashed, or near heat, sunlight or ozone sources (motors, welders).",
                    "Ideal storage about 10–35 °C",
                    "Keep away from oils and solvents",
                    "Outers must be the right size so they don't stretch the insulating glove",
                    "Failed gloves: remove from service immediately and dispose of properly"
                ),
                quiz("How often must 1000 V gloves be re-tested or replaced?", 1, "Within 12 months, counted from when a new sealed pair is first opened.", "Every 5 years", "Every 12 months", "Only when they look worn", "Never, they don't wear out")
            )
        ),
        lesson(
            "ppe3", "HV switching PPE", "🦺", 5, Level.ADVANCED,
            page(
                "Stepping it up",
                bullets(
                    "HV switching: arc-flash suit around 40 cal/cm² with hood and visor, safety glasses underneath, HV switching gloves with leather gauntlets",
                    "The same level is typically needed to locally operate big LV breakers (e.g. over about 1500 A)",
                    "Remote switching (from a distance) is preferred where available, but earth switches may still need to be applied by hand in the suit"
                ),
                Tip("Equipment may carry an arc-flash label stating the PPE needed. Where there's no label, a JHA or your supervisor decides."),
                quiz("Which of these needs the highest-rated arc-flash PPE?", 3, "Operating HV switchgear needs the heavy HV switching suit (around 40 cal/cm²).", "Visual inspection from outside a panel", "Testing a 240 V power board", "Changing a work light", "Locally operating an HV circuit breaker")
            )
        )
    )
)
