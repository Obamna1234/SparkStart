package com.sparkstart.app.content

import com.sparkstart.app.data.*

internal val ugCablesModule = Module(
    id = "ug_cables",
    title = "Cable Work Underground",
    emoji = "🧵",
    blurb = "Running and hanging cable, removing it, stripping safely, fitting restrained plugs, crimping, LV splices, and lowering cable down holes.",
    color = 0xFF5D4037,
    group = Group.UNDERGROUND,
    lessons = listOf(
        lesson(
            "cab1", "Running and hanging cable", "🪝", 10, Level.BEGINNER,
            page(
                "Set up",
                bullets(
                    "Pre-start the tool carrier (IT) and work basket",
                    "Check the cable stand: weight within its safe working load, no cracks, current certification label",
                    "Load the drum with a correctly rated axle, bolted in",
                    "Workplace inspection, isolate services in the area if needed",
                    "Tell people, and put 'Personnel Working in Decline' signs and RED flashing lights above and below the work area"
                )
            ),
            page(
                "Running it out",
                bullets(
                    "Anchor the cable end, then reverse the IT so the cable rolls off with no tension",
                    "Crossing water/air lines or vent bag? Use rated cable rollers hung from a rated anchor (e.g. 16 mm eyebolt with 8 mm chain and rated shackles)",
                    "Stay out of the LINE OF FIRE: a cable under tension can whip if an anchor fails. Set exclusion zones."
                ),
                Hazard("A cable under tension is stored energy. If the anchor or sock lets go, it can whip back. Stand to the side, never in line with it.")
            ),
            page(
                "Hanging it neatly",
                bullets(
                    "Hang on DEDICATED cable supports, not on other services' hooks",
                    "On meshed ground: hangers or straps on the mesh about every 3 m",
                    "On shotcrete: rated eyebolts, catenary wire, or chains between ground-support plates",
                    "Straight as possible, separated from other services, crossing them at 90°",
                    "At panels: cables down the side, tied neatly, never blocking outlets (often supply on the left, outgoing on the right)",
                    "Glands fitted with the right shrouds",
                    "Cable tags (cattle tags) at BOTH ends: source, destination, size and details"
                ),
                quiz("Why do cables cross other services at 90°?", 2, "A right-angle crossing minimises contact length and rubbing, and makes cables easy to trace.", "It looks nicer", "It's cheaper", "Less contact and wear between services, and easier to trace", "So it carries more current")
            )
        ),
        lesson(
            "cab2", "Removing gear and cutting cable", "✂️", 9, Level.INTERMEDIATE,
            page(
                "Removal is planned",
                P("The electrical supervisor decides what comes out, after checking with mine planning and the mining crew. Then:"),
                order(
                    "Order the steps to remove a panel",
                    "Workplace inspection",
                    "Confirm the equipment and identify its supply",
                    "Isolate the supply breaker, lock and tag",
                    "Remove the supply tails and tag them",
                    "Go to the panel and confirm it's isolated",
                    "Disconnect and remove the cable from the panel",
                    "Bag and tag the cable",
                    "Lift the panel off with rated lifting gear",
                    "Record it so drawings and the asset register are updated"
                )
            ),
            page(
                "Never cut the wrong cable",
                bullets(
                    "Only cut a cable you have POSITIVELY identified: see both ends, check drawings, eliminate others",
                    "Any doubt at all? Don't cut it. Tell your supervisor.",
                    "Danger and Out of Service tags at BOTH ends so it can't be connected while you work",
                    "Afterwards: bag and tag the ends, and update the OOS tag with the new length and end location"
                ),
                WhyNot("Why can't you just cut the cable that 'looks like' the dead one? Underground cables run together for hundreds of metres. Cutting into a live 1000 V cable can kill you or cause a massive arc. Positive identification and isolation at both ends is the only safe way."),
                Tip("Reclaiming cable in a basket: everyone in the basket and the operator lock the IT's implement valve. Respect the machine's static tipping load (people + cable). Prefer working uphill and figure-8 the cable into the basket.")
            )
        ),
        lesson(
            "cab3", "Stripping cable safely", "🔪", 7, Level.BEGINNER,
            page(
                "Right tools only",
                bullets(
                    "Only approved SELF-RETRACTING safety knives. No fixed blades or homemade knives.",
                    "Industrial snips for armour; hacksaw (24 TPI) for ringing SWA",
                    "Cut-resistant gloves, safety glasses, long sleeves",
                    "Check the knife retracts the moment it leaves the cutting surface. If not, don't use it."
                ),
                P("Secure the cable in a vice where you can. Cut AWAY from your body, with your supporting hand behind the blade. Only score the outer sheath, don't slice into what's underneath."),
                Hazard("Stripped steel armour wires are sharp and springy. Fold long lengths back to avoid puncture wounds."),
                Tip("Leave at least about 75 mm of inner sheath past the prepared SWA so it protrudes through the gland and protects the cores from the gland's edges. No armour should be visible once the gland is fitted.")
            )
        ),
        lesson(
            "cab4", "Fitting a restrained plug", "🔌", 10, Level.INTERMEDIATE,
            page(
                "On a reeling/trailing cable",
                order(
                    "Put the plug fitting steps in order",
                    "Inspect PPE, tools, plug parts and cable",
                    "Confirm plug voltage, current rating and grommet suit the cable",
                    "Slide the gland cover and grommet onto the cable",
                    "Clean, then strip the outer sheath",
                    "Score off the semicon from the power cores (don't cut the insulation)",
                    "Remove the semicon cradle from the pilot core carefully",
                    "Terminate the cores (soldered, grub-screw or crimped thimbles)",
                    "Fit the thimbles and earth pins in the correct orientation",
                    "Fit the nose cone, grommet and gland, then torque everything",
                    "Fit the cable anchor/sock and bump stop"
                ),
                Tip("Cable sock eyelet points toward the starter end, at least about 2–2.5 m from the plug. The bump stop goes just before the anchor.")
            ),
            page(
                "Test before it goes in service",
                bullets(
                    "Point-to-point resistance of every core (power, earth, pilot), recorded",
                    "Rough guide for a 50 mm² trailing cable: pilot about 5 Ω per 100 m, power core about 0.05 Ω per 100 m",
                    "Check phase identification and orientation at each end",
                    "Insulation resistance between phases and phase to earth at the approved test voltage",
                    "Label with the asset number and record it in the cable register"
                ),
                Std("AS/NZS 1747", "Reeling, trailing and feeder cables for mining: repair, testing and fitting of accessories (including soldering requirements)."),
                quiz("A new 100 m trailing cable's pilot core measures about 5 Ω end to end. Is that reasonable?", 0, "Yes. The pilot is a much smaller conductor than the power cores, so a few ohms per 100 m is normal.", "Yes, that's normal for a pilot core", "No, it should be 0.05 Ω", "No, it should be OL", "No, it should be 500 Ω")
            )
        ),
        lesson(
            "cab5", "Crimping & LV splices", "🔧", 9, Level.INTERMEDIATE,
            page(
                "Battery crimpers",
                bullets(
                    "Battery OUT before changing dies, so it can't fire unexpectedly",
                    "Use the die that matches the cable size and lug/link",
                    "Fingers clear of the jaws when crimping",
                    "Remove the battery, clean and store the die after use"
                ),
                Hazard("Hydraulic crimpers exert tonnes of force. A finger in the jaws is a serious crush injury.")
            ),
            page(
                "Splicing SWA cable (heat or cold shrink)",
                bullets(
                    "Isolate, lock out and positively identify the cable. If you can't, isolate EVERYTHING that could be it.",
                    "Hot work: workplace inspection, hot-work permit if needed, extinguisher within reach, approved gas bottle, ONLY ONE torch at a time",
                    "Strip to the kit dimensions (they change with cable size)",
                    "Armour support ring and tinned copper braid keep earth continuity across the joint",
                    "Crimp the links with an approved tool and file off burrs",
                    "Shrink with a soft, BRUSHING flame from the centre outward. No wrinkles.",
                    "Let it cool before moving it, and support both sides of the joint"
                ),
                WhyNot("Why not just keep a pencil-sharp flame on one spot to shrink faster? It scorches and weakens the insulation and leaves voids, which become the weak point where the joint fails later. Keep the flame moving."),
                Tip("Cold-shrink joints use mastic and tapes instead of heat. Stagger the connectors on bigger cables (16 mm² and up) to keep the joint slim.")
            )
        ),
        lesson(
            "cab6", "Lowering cable down holes", "⬇️", 10, Level.ADVANCED,
            page(
                "Plan it",
                bullets(
                    "Cable lowering preparation sheet and a specific JHA, reviewed with the whole team",
                    "Calculate cable + clamp + wire rope weight, and choose rated gear for it",
                    "Access at the top AND bottom, a sentry at the bottom, radios",
                    "Barricade both ends, fall protection near open holes"
                ),
                Hazard("Never manually lower more than about 20 m of cable. It gets away from you. Use mobile plant, a winch or a lowering device.")
            ),
            page(
                "Securing it",
                bullets(
                    "Two cable socks: one takes the weight at the anchor, the second is a backup with no load on it",
                    "Clamps or bandit straps with rubber insertion about every 8 m on the catenary",
                    "Each anchor on a SEPARATE anchor point, proven before anyone goes near or under the cable",
                    "With a winch: once the tie-off takes the weight, wait about 30 minutes to make sure clamps and anchors hold before cutting the main rope",
                    "Fill the top of a service hole so rocks can't fall down it",
                    "Cattle tag the cable at the top and bottom"
                ),
                WhyNot("Why can't cables of different voltages share a service hole? A fault or damage in one could involve the other. It also makes isolation and identification confusing. Different voltages and other services get their own holes."),
                Tip("Reel winches and reclaimers have rated suspended weights and lengths for each cable size (bigger cable = shorter max suspended length). Nobody stands under an elevated winch, R-pins must be in, and run the IT at idle.")
            )
        )
    )
)

internal val ugHvModule = Module(
    id = "ug_hv",
    title = "HV Cables (11 kV XLPE)",
    emoji = "⚡",
    blurb = "What's inside an HV cable, why stress control matters, terminations, splices, and commissioning tests.",
    color = 0xFFAD1457,
    group = Group.UNDERGROUND,
    lessons = listOf(
        lesson(
            "hv1", "Inside an XLPE cable", "🎯", 8, Level.ADVANCED,
            page(
                "Layer by layer",
                Vis(Visual.XLPE_LAYERS),
                Deep("Why stress control? In a cable, the earthed screen keeps the electric field even, spreading out from the conductor through the XLPE. At a termination you cut the screen back, and right at that cut edge the field lines crowd together. That concentrated stress can break down the air and insulation over time (tracking, then failure). Stress-control mastic and tubes spread the field out smoothly so it doesn't concentrate.")
            ),
            page(
                "Termination key points",
                bullets(
                    "Kit must suit cable type, size, voltage and indoor/outdoor use",
                    "Secure the cable (vice / pipe vice)",
                    "Seal and fold back screen wires into sealing mastic",
                    "Fit and shrink the breakout (crutch) first",
                    "Remove the semicon cleanly to the right length. Smooth with fine abrasive if needed, and clean from the XLPE toward the semicon.",
                    "Stress mastic over the semicon cut, then the stress-control tube overlapping the semicon",
                    "Lugs, sealing mastic, then non-tracking tubes. Creepage sheds for outdoor terminations."
                ),
                WhyNot("Why can't you clean the XLPE by wiping from the semicon toward the insulation? You'd drag conductive semicon particles onto the insulation surface, creating a path for tracking. Always wipe from the insulation side toward the semicon."),
                Hazard("HV cable work on installed cables needs an HV switching program and access permit. The cable must be isolated, earthed and proven dead by appointed people.")
            )
        ),
        lesson(
            "hv2", "Commissioning an HV cable", "✅", 9, Level.ADVANCED,
            page(
                "The tests",
                bullets(
                    "Visual walk of the whole install: labels, supports, clearance from other services, vertical anchors",
                    "SHEATH test: 1000 V DC for 1 minute between the metallic screen and outside the sheath. A common minimum is about 100 MΩ. Do it before AND after installing to catch damage.",
                    "INSULATION RESISTANCE: phase to phase and phase to earth with an HV insulation tester (e.g. 5 kV). Hold until the reading is stable, because the cable's capacitance takes time to charge.",
                    "EARTH CONTINUITY: everything connected except one earth at the origin, then measure the loop"
                ),
                Hazard("Test voltages appear on the far end of the cable. Close up panels, barricade, post signs or use an observer so nobody touches the other end. Discharge the cable after IR testing because it holds charge like a capacitor.")
            ),
            page(
                "Energising and phasing",
                bullets(
                    "Prefer REMOTE switching for first energisation, from a switch with full fault-make capability",
                    "Non-essential people out of the area",
                    "Often left at operating voltage for about 24 h before load is applied",
                    "PHASING: before closing a ring between two supplies, check each phase matches with a phase comparator. Green = phases equal, red = NOT equal."
                ),
                WhyNot("Why can't you close a ring switch when the phase comparator shows red on one phase? Two supplies with mismatched phases connected together is effectively a phase-to-phase short circuit. That means a huge fault, an arc flash and damaged switchgear."),
                quiz("Why must the IR test on a long HV cable be held until the reading stabilises?", 1, "The cable acts like a big capacitor. The reading climbs as it charges, so an early reading is wrong.", "The tester needs to warm up", "The cable's capacitance charges up first", "To heat the cable", "It doesn't need to be")
            )
        )
    )
)
