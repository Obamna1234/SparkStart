package com.sparkstart.app.content

import com.sparkstart.app.data.Flashcard
import com.sparkstart.app.data.Lesson
import com.sparkstart.app.data.Module
import com.sparkstart.app.data.Quiz

object Course {
    val modules: List<Module> = listOf(
        ugStartModule,
        miningModule,
        ugIsolationModule,
        ugPpeModule,
        ugPowerModule,
        ugProtectionModule,
        ugTestingModule,
        ugCablesModule,
        ugHvModule,
        ugRigsModule,
        ugBevModule,
        ugFansPumpsModule,
        ugBoxesModule,
        ugRelaysModule,
        ugTroubleModule,
        ugWorkshopModule,
        ugIncidentsModule,
        basicsModule,
        safetyModule,
        supplyModule,
        protectionModule,
        wiringModule,
        testingModule,
        machinesModule,
        rulesModule,
        domesticModule,
        commercialModule
    )

    private val lessonIndex: Map<String, Pair<Module, Lesson>> =
        modules.flatMap { m -> m.lessons.map { it.id to (m to it) } }.toMap()

    val totalLessons: Int = lessonIndex.size

    fun module(id: String): Module? = modules.firstOrNull { it.id == id }
    fun lesson(id: String): Lesson? = lessonIndex[id]?.second

    fun nextLesson(id: String): Lesson? {
        val (m, l) = lessonIndex[id] ?: return null
        val i = m.lessons.indexOf(l)
        return m.lessons.getOrNull(i + 1)
    }

    fun quizzes(moduleId: String?): List<Quiz> {
        val mods = if (moduleId == null) modules else modules.filter { it.id == moduleId }
        return mods.flatMap { m -> m.lessons.flatMap { l -> l.pages.flatMap { p -> p.blocks.filterIsInstance<Quiz>() } } } +
            extraQuizzes.filter { moduleId == null || it.first == moduleId }.map { it.second }
    }

    private val extraQuizzes: List<Pair<String, Quiz>> = listOf(
        "basics" to Quiz("What unit is current measured in?", listOf("Volts", "Ohms", "Amps", "Watts"), 2, "Current is measured in amps (A)."),
        "basics" to Quiz("What is 1 kW in watts?", listOf("10 W", "100 W", "1000 W", "1,000,000 W"), 2, "Kilo = 1000."),
        "safety" to Quiz("Which tester should NOT be used on its own to prove a circuit dead?", listOf("Two-pole voltage tester", "Non-contact 'volt stick'", "Multimeter on V~ (CAT rated)", "Approved voltage indicator"), 1, "Non-contact testers can miss voltage. Use an approved contact tester with test-prove-test."),
        "safety" to Quiz("Who can remove your personal danger lock?", listOf("Anyone on the crew", "The leading hand", "Only you", "Whoever finishes last"), 2, "Only the person whose lock it is removes it (except under a strict formal procedure)."),
        "supply" to Quiz("Where is the MEN link located?", listOf("At every sub-board", "At the main switchboard only", "In every power point", "At the earth stake"), 1, "One MEN link, at the main switchboard."),
        "protection" to Quiz("What does an RCBO combine?", listOf("Fuse and meter", "RCD and circuit breaker", "Switch and light", "Two RCDs"), 1, "RCBO = residual current breaker with overcurrent protection: RCD + MCB in one."),
        "testing" to Quiz("What test voltage is commonly used for insulation resistance testing of 230/400 V circuits?", listOf("12 V AC", "50 V DC", "500 V DC", "5000 V AC"), 2, "500 V DC is commonly used for LV circuits. Check the Wiring Rules for special cases."),
        "machines" to Quiz("How do you reverse a three-phase induction motor?", listOf("Swap active and neutral", "Swap any two phases", "Reverse the earth", "Lower the voltage"), 1, "Swapping any two phases reverses the rotating field."),
        "commercial" to Quiz("Where does the MEN link go in a building with a main switchboard and several DBs?", listOf("In every DB", "Only at the main switchboard", "In the biggest DB", "Nowhere in commercial"), 1, "One MEN link, at the main switchboard. DBs keep neutral and earth separate."),
        "commercial" to Quiz("Why must an emergency light have an unswitched supply?", listOf("So it's brighter", "To charge its battery and detect real power failure", "To save power", "It doesn't need one"), 1, "Without an unswitched supply it can't tell a blackout from someone turning the lights off, and its battery runs flat."),
        "commercial" to Quiz("Which standard covers test and tag of portable equipment?", listOf("AS/NZS 2293", "AS/NZS 3760", "AS/NZS 3012", "AS/NZS 3008.1.1"), 1, "AS/NZS 3760: in-service safety inspection and testing."),
        "commercial" to Quiz("Phases are balanced but the neutral is running hot. Likely cause?", listOf("Too much voltage", "Harmonic currents from LED drivers and electronics", "Too many RCDs", "A loose earth stake"), 1, "Certain harmonic currents add up in the neutral instead of cancelling, which overheats it."),
        "commercial" to Quiz("Why do fire services have their own marked main switch?", listOf("They use more power", "So they keep running when general power is shut off", "To save money", "Because of the meter"), 1, "Firefighters may shut off general power, and safety services must keep working."),
        "domestic" to Quiz("Solar panels are installed. You turn off the AC main switch. Are the panels' DC cables dead?", listOf("Yes", "No, panels make voltage whenever there's light", "Only at night they're live", "Only if it's raining"), 1, "PV panels generate in daylight regardless of the AC main switch. Follow the shutdown procedure and isolate the DC side."),
        "mining" to Quiz("What is the pilot core in a trailing cable for?", listOf("Extra power", "Data for the radio", "Monitoring earth continuity", "Lighting"), 2, "The pilot is part of the earth-continuity monitoring loop.")
    )

    val flashcards: List<Flashcard> = listOf(
        Flashcard("f1", "Ohm's law", "V = I × R\n(volts = amps × ohms)", "basics"),
        Flashcard("f2", "Power formula", "P = V × I\n(watts = volts × amps)", "basics"),
        Flashcard("f3", "Australian single-phase voltage & frequency", "230 V, 50 Hz", "basics"),
        Flashcard("f4", "Voltage between two phases (3-phase)", "400 V", "basics"),
        Flashcard("f5", "ELV is not more than…", "50 V AC or 120 V ripple-free DC", "basics"),
        Flashcard("f6", "Series resistors: total", "Add them: R1 + R2 + R3…", "basics"),
        Flashcard("f7", "Two resistors in parallel: total", "(R1 × R2) ÷ (R1 + R2). Always less than the smallest.", "basics"),
        Flashcard("f8", "Rough current that can kill", "About 30 mA and up across the chest, depending on time and path", "safety"),
        Flashcard("f9", "Test-prove-test", "Prove tester on known live source → test circuit dead → re-prove tester", "safety"),
        Flashcard("f10", "Is a non-contact tester OK to prove dead?", "No. Quick check only.", "safety"),
        Flashcard("f11", "First thing to do if someone's being shocked", "Don't touch them. Isolate the supply if safe, then call 000.", "safety"),
        Flashcard("f12", "MEN stands for", "Multiple Earthed Neutral", "supply"),
        Flashcard("f13", "MEN link joins…", "The neutral bar to the earth bar, at the MAIN switchboard only", "supply"),
        Flashcard("f14", "Why the MEN link matters", "It gives fault current a low-resistance path back to the transformer, so protection trips fast", "supply"),
        Flashcard("f15", "Max resistance: earth electrode to main earthing terminal (MEN)", "0.5 Ω (Clauses 8.3.5 and 5.5.1.4)", "supply"),
        Flashcard("f16", "Breaker: thermal part protects against…", "Overload (slow)", "protection"),
        Flashcard("f17", "Breaker: magnetic part protects against…", "Short circuit (instant)", "protection"),
        Flashcard("f18", "Personal-protection RCD rating", "30 mA (or less)", "protection"),
        Flashcard("f19", "Why an RCD won't trip on A-to-N through a person", "Current out = current back, so there's no imbalance", "protection"),
        Flashcard("f20", "New homes (2018 rules): which circuits need RCDs?", "All final subcircuits (check clause 2.6.3.2.2)", "protection"),
        Flashcard("f21", "Max final subcircuits per RCD (domestic, and WA sale/rent rules)", "Three", "protection"),
        Flashcard("f22", "Fixed wiring: active / neutral / earth colours", "Active: red preferred (never green/yellow, green, yellow, black or light blue)\nNeutral: black or light blue\nEarth: green/yellow", "wiring"),
        Flashcard("f23", "Flexible cord: active / neutral / earth", "Brown / light blue / green-yellow", "wiring"),
        Flashcard("f24", "Three-phase active colours", "Red, white, blue", "wiring"),
        Flashcard("f25", "Switches must break which conductor?", "The ACTIVE", "wiring"),
        Flashcard("f26", "Max voltage drop (Clause 3.6.2)", "5% from point of supply to any point (11.5 V on 230 V). 7% if supplied from a dedicated on-site substation.", "wiring"),
        Flashcard("f27", "Cable selection standard", "AS/NZS 3008.1.1:2025 (replaced 2017)", "wiring"),
        Flashcard("f28", "Insulation resistance: test voltage & minimum (230/400 V)", "500 V DC, 1 MΩ (Clause 8.3.6)", "testing"),
        Flashcard("f29", "Order of tests (Clause 8.3.3, after visual)", "Visual → earth continuity → insulation resistance → polarity → correct circuit connections → fault-loop impedance → RCD", "testing"),
        Flashcard("f30", "What does OL mean on a meter?", "Open loop / over limit: no path or above range", "testing"),
        Flashcard("f31", "CAT IV meters are for…", "The origin of supply (mains, meters, service)", "testing"),
        Flashcard("f32", "Which section of AS/NZS 3000 covers verification?", "Section 8", "rules"),
        Flashcard("f33", "Which section covers earthing?", "Section 5", "rules"),
        Flashcard("f34", "Which section covers damp situations (bathrooms, pools)?", "Section 6", "rules"),
        Flashcard("f35", "Maximum demand methods are in…", "Appendix C", "rules"),
        Flashcard("f36", "Test & tag standard", "AS/NZS 3760", "rules"),
        Flashcard("f37", "Construction sites standard", "AS/NZS 3012", "rules"),
        Flashcard("f38", "Synchronous speed of a 4-pole motor at 50 Hz", "1500 rpm (rotor slightly less due to slip)", "machines"),
        Flashcard("f39", "DOL starting current is about…", "6–8 × full-load current", "machines"),
        Flashcard("f40", "Star-delta starting current vs DOL", "About one-third", "machines"),
        Flashcard("f41", "Universal motor", "Has brushes, runs on AC or DC: drills, grinders, vacuums", "machines"),
        Flashcard("f42", "Transformers only work on…", "AC (the field must keep changing)", "machines"),
        Flashcard("f43", "Why emergency lights need an unswitched supply", "To keep charging and to detect real power failure", "commercial"),
        Flashcard("f44", "Emergency lighting standard", "AS/NZS 2293 series", "commercial"),
        Flashcard("f45", "Why fire services have their own main switch", "So they keep running when general power is shut off in a fire", "commercial"),
        Flashcard("f46", "Main switch off: what's still live in a house board?", "Consumer mains, service fuse, meter, line side of main switch", "domestic"),
        Flashcard("f47", "IP rating: first digit / second digit", "Solids & dust / water", "domestic"),
        Flashcard("f48", "Tingle from a tap means…", "Possible broken neutral or earthing fault. URGENT.", "domestic"),
        Flashcard("f49", "Trailing cable pilot core", "Part of the earth-continuity monitoring loop. Lose it and power trips.", "mining"),
        Flashcard("f50", "Trailing cable standard (non-coal)", "AS/NZS 2802", "mining"),
        Flashcard("f51", "WA mine safety regs since 31 March 2022", "WHS (Mines) Regulations 2022", "mining"),
        Flashcard("f52", "What does an NER do?", "Limits earth-fault current to a small set value, lowering touch voltage", "mining"),
        Flashcard("f53", "Is the rig's E-stop an isolation?", "No. It's a control device. Isolate at the outlet and remove the plug.", "mining"),
        Flashcard("f54", "Mine electrical equipment standards", "AS/NZS 3007 (installations), AS/NZS 4871 (equipment)", "mining"),
        // ---- underground track ----
        Flashcard("u01", "Direct supervision", "Supervisor stays with you, in close proximity, the whole task", "ug_start"),
        Flashcard("u02", "General supervision", "Supervisor on the same site, readily available, checks periodically", "ug_start"),
        Flashcard("u03", "Broad supervision", "Familiar tasks. Supervisor needn't stay on site but checks the work at least daily.", "ug_start"),
        Flashcard("u04", "Can a 1st–3rd year do a live isolation?", "No. They can fit lock and tag once it's proven dead, and uncouple plug-fed gear.", "ug_start"),
        Flashcard("u05", "Positive isolation", "All current-carrying conductors open AND secured (locked) so they can't re-close, even by a control circuit", "ug_start"),
        Flashcard("u06", "Lethal current (commonly quoted)", "More than about 10 mA AC or 300 mA DC through the body", "ug_start"),
        Flashcard("u07", "Hierarchy of controls", "Eliminate → Substitute → Isolate/Engineer → Administrative → PPE", "ug_start"),
        Flashcard("u08", "Danger tag colours", "Red / white / black. 'Person working.' Only the person who fitted it removes it.", "ug_isolation"),
        Flashcard("u09", "Out of Service tag colours", "Yellow / black. Faulty or not to be used. Anyone can fit; a competent person removes.", "ug_isolation"),
        Flashcard("u10", "Commissioning tag (common in WA mines)", "Pink / black. Testing in progress, treat as live.", "ug_isolation"),
        Flashcard("u11", "Why test phase-to-EARTH on 1000 V gear?", "Phase-to-phase is ~1000 V, at the limit of a CAT III 1000 V meter. Phase-to-earth (~577 V) stays inside its rating.", "ug_isolation"),
        Flashcard("u12", "Isolating plug-fed mobile plant", "Unplug at the starter, plug lockout device + lock + danger + OOS tag, isolate the 24 V too", "ug_isolation"),
        Flashcard("u13", "Who does HV switching?", "Only an appointed HV switching operator, to an approved switching program", "ug_isolation"),
        Flashcard("u14", "BEV: is the battery side of the DC isolator ever dead?", "No. DC is always present between the batteries and the isolator.", "ug_isolation"),
        Flashcard("u15", "LV arc-flash minimum (typical)", "Face shield, ≥ 8 cal/cm² jacket neck-to-knee, 1000 V gloves + leather outers", "ug_ppe"),
        Flashcard("u16", "HV switching PPE (typical)", "~40 cal/cm² suit with hood, HV gloves with gauntlets, glasses under the visor", "ug_ppe"),
        Flashcard("u17", "1000 V glove re-test", "Within 12 months of first opening. Air-roll test before every use.", "ug_ppe"),
        Flashcard("u18", "Underground earthing system", "IT (impedance-earthed): first fault must be a LOW-energy fault", "ug_power"),
        Flashcard("u19", "Max earth-leakage trip: portable gear / LV underground / HV underground", "30 mA / about 1 A / about 2 A", "ug_power"),
        Flashcard("u20", "Underground earth-leakage testing", "About monthly (injection or functional test)", "ug_power"),
        Flashcard("u21", "Bagged and tagged", "All phase cores shorted together and to earth, covered and tagged", "ug_power"),
        Flashcard("u22", "Panel connection order", "Load end first, then supply end", "ug_power"),
        Flashcard("u23", "EFLO", "Earth Fault Lockout: tests outgoing insulation to earth BEFORE closing", "ug_protection"),
        Flashcard("u24", "Frozen contact protection", "Voltage still on the outgoing side after the contactor should have opened → trips the breaker. Relay operates at ≤ 24 V (AS/NZS 2081).", "ug_protection"),
        Flashcard("u25", "Earth continuity trips on…", "Pilot loop OPEN or SHORTED", "ug_protection"),
        Flashcard("u26", "Which outlet is most sensitive (example settings)?", "Pump (~30 mA), then jumbo (~100 mA), then fan (~200 mA)", "ug_protection"),
        Flashcard("u27", "Healthy pilot diode test", "About 0.5–0.6 V one way, OL the other", "ug_testing"),
        Flashcard("u28", "Diode test faults", "Low reading or reading both ways = short. OL both ways = open.", "ug_testing"),
        Flashcard("u29", "Earth continuity limit (installations, fans, pumps)", "≤ 0.5 Ω, with one earth disconnected for the test", "ug_testing"),
        Flashcard("u30", "Loop impedance on 1000 V circuits", "Calculated and checked against AS/NZS 3000 Table 8.1 (testers usually not available at 1000 V)", "ug_testing"),
        Flashcard("u31", "Outlet tester pins", "LIVE during some tests. Isolate before unplugging the leads.", "ug_testing"),
        Flashcard("u32", "Cable hanging on mesh", "Hangers/straps about every 3 m on dedicated supports", "ug_cables"),
        Flashcard("u33", "Inner sheath past the SWA", "At least ~75 mm so it passes through the gland", "ug_cables"),
        Flashcard("u34", "Brush replacement on slip rings", "At ~3 mm at the thinnest point. Replace all together.", "ug_rigs"),
        Flashcard("u35", "Trailing cable approx. resistance (50 mm²)", "Pilot ~5 Ω per 100 m; power core ~0.05 Ω per 100 m", "ug_cables"),
        Flashcard("u36", "Max manual cable lower", "About 20 m. Beyond that use plant, a winch or a lowering device.", "ug_cables"),
        Flashcard("u37", "Clamps on a vertical cable", "About every 8 m with rubber insertion", "ug_cables"),
        Flashcard("u38", "Shrinking heat-shrink", "Soft brushing flame, centre outward, one torch only, extinguisher nearby", "ug_cables"),
        Flashcard("u39", "HV sheath test", "1000 V DC for 1 minute, typically ≥ 100 MΩ", "ug_hv"),
        Flashcard("u40", "Why stress control at HV terminations?", "The field crowds at the screen cut-back; stress control spreads it out", "ug_hv"),
        Flashcard("u41", "Phase comparator: green / red", "Green = phases equal (OK to close). Red = NOT equal, DON'T close.", "ug_hv"),
        Flashcard("u42", "Anti-tram interlock test", "Ignition/tram on → mains must drop out; pilot reset to restore", "ug_rigs"),
        Flashcard("u43", "Cable reel limits", "1st limit (~4–5 layers): warning + park brake. 2nd limit (~2 layers): alarm, can't override.", "ug_rigs"),
        Flashcard("u44", "Slip-ring cover off →", "Pilot interlock opens, so the rig can't be energised", "ug_rigs"),
        Flashcard("u45", "Heating a temp sensor to test it", "Use a heater, NEVER an open flame", "ug_rigs"),
        Flashcard("u46", "Rig pump hose disconnect", "Open the bleed valve to release discharge pressure first", "ug_rigs"),
        Flashcard("u47", "BEV capacitor wait time (typical)", "About 10–15 minutes, then prove dead", "ug_bev"),
        Flashcard("u48", "BEV insulation monitor", "Watches HV DC to chassis: warning, then alarm as insulation drops", "ug_bev"),
        Flashcard("u49", "Vent fan pulse start (typical)", "1 s on / 6 s off × ~4 (about 28 s) to inflate the bag gently", "ug_fanspumps"),
        Flashcard("u50", "Second-stage fan delay (typical)", "About 40 s, to cut the starting load", "ug_fanspumps"),
        Flashcard("u51", "Oil in a submersible's inspection chamber", "Inner mechanical seal likely damaged", "ug_fanspumps"),
        Flashcard("u52", "Mono pump golden rule", "Never run dry or dead-headed. The stator overheats fast.", "ug_fanspumps"),
        Flashcard("u53", "Test & tag: earth / IR / portable RCD", "≤ 1 Ω / ≥ 1 MΩ at 500 V / 30 mA within 40 ms", "ug_workshop"),
        Flashcard("u54", "Quarterly tag colours", "Red Dec–Feb, Green Mar–May, Blue Jun–Aug, Yellow Sep–Nov", "ug_workshop"),
        Flashcard("u55", "VRD limit (AS 1674.2)", "No-load voltage under 35 V DC / 25 V AC", "ug_workshop"),
        Flashcard("u56", "Electric shock: always…", "Isolate first, DRSABCD, medical ECG (they don't drive), report", "ug_incidents"),
        Flashcard("u57", "Machine contacted > 1000 V AC", "Park it up about 24 h (tyre explosion/fire risk)", "ug_incidents"),
        Flashcard("u58", "Valid reason for live work?", "Only real necessity with no alternative. NEVER lost production.", "ug_incidents")
    ) + boxFlashcards
}
