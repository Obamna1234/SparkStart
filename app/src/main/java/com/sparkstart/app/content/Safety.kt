package com.sparkstart.app.content

import com.sparkstart.app.data.*

internal val safetyModule = Module(
    id = "safety",
    title = "Staying Alive",
    emoji = "🦺",
    blurb = "How shocks happen, isolation, test-before-you-touch, arc flash and first aid.",
    color = 0xFFE53935,
    group = Group.CORE,
    lessons = listOf(
        lesson(
            "s1", "How electricity hurts you", "💥", 6, Level.BEGINNER,
            page(
                "You become part of the circuit",
                P("You get a shock when your body completes a loop. Usually that's between something LIVE and something EARTHED (the ground, a metal pipe, a machine frame) or between active and neutral."),
                Vis(Visual.BODY_CURRENT),
                Hazard("Current across the chest (hand to hand, or hand to feet) is the deadliest path because it goes through the heart.")
            ),
            page(
                "The four ways it gets you",
                bullets(
                    "Electric shock: stops your heart or breathing, locks muscles",
                    "Burns: inside and out, often far worse than they look",
                    "Arc flash / arc blast: an explosion of light, heat and molten metal from a short circuit",
                    "Secondary injuries: falling off a ladder after a 'small' shock"
                ),
                Tip("Any electric shock, even a small one, should be reported to your supervisor and checked by a medical professional. Heart rhythm problems can show up later."),
                Ask("In WA, serious electrical incidents must be reported to the regulator. Your employer will have a procedure. Find out what it is.")
            )
        ),
        lesson(
            "s2", "Isolation: test before you touch", "🔒", 8, Level.BEGINNER,
            page(
                "Never work live",
                P("The number one rule: work on circuits DEAD. Live work is banned except in very limited situations with strict controls, and never by an apprentice."),
                P("Making something dead and keeping it dead is called ISOLATION. It has a set order."),
                Std("AS/NZS 4836", "AS/NZS 4836 covers safe working on or near low-voltage electrical installations and equipment. Your training and your workplace procedures are based on it."),
                order(
                    "Put the isolation steps in order",
                    "Identify the right circuit / equipment",
                    "Switch off and isolate the supply",
                    "Lock it off and fit your personal danger tag",
                    "Prove your tester works on a known live source",
                    "Test that the circuit is dead (all conductors)",
                    "Re-prove your tester on the known source",
                    "Now you can start work"
                )
            ),
            page(
                "Why prove your tester twice?",
                P("If your tester is broken, it reads '0 V' on a live circuit, and you'd believe it. Proving it on a known live source BEFORE and AFTER is the only way to know the 'dead' reading is real."),
                WhyNot("Why can't you just trust the breaker label? Boards get mislabelled, circuits get changed, and there can be more than one supply (solar, batteries, generators, another board). Labels tell you where to LOOK, not that it's dead."),
                WhyNot("Why can't you use a non-contact 'volt stick' alone to prove dead? They can miss voltage, especially through shielded cable or with flat batteries. They're handy for a quick check but are not an approved way to prove de-energised."),
                Hazard("Test between ALL conductors: active to neutral, active to earth, neutral to earth. A neutral can carry current from other circuits (shared neutral) and be live even when 'your' circuit is off."),
                quiz("You test a circuit and get 0 V. What must you do next?", 2, "Re-prove the tester on a known live source. If it fails now, your '0 V' meant nothing.", "Start work straight away", "Test it once more the same way", "Re-prove your tester on a known live source", "Turn the circuit back on to check")
            ),
            page(
                "Locks and tags",
                P("Your personal lock and danger tag mean: 'I'm working on this, do NOT turn it on.' Only YOU remove your lock."),
                bullets(
                    "Personal danger tag: your name, date, what you're doing",
                    "Out-of-service tag: marks faulty equipment so nobody uses it",
                    "Group isolation (lockout boxes) is used on bigger jobs. Everyone puts on their own lock."
                ),
                WhyNot("Why can't your mate pull your lock off at the end of the day? The person whose lock it is is the only one who knows they're clear. Removing someone else's lock is how people get killed, and it's a sackable offence on most sites."),
                Ask("Every site has its own isolation procedure, permits and lock rules (especially mines and HV). Learn yours before you touch anything.")
            )
        ),
        lesson(
            "s3", "Arc flash and PPE", "🔥", 6, Level.INTERMEDIATE,
            page(
                "What is an arc flash?",
                P("When a short circuit happens through the air (a dropped spanner across busbars, a failed breaker, testing on the wrong range), the current jumps as a blinding arc hotter than the surface of the sun."),
                P("It throws out intense heat, light, sound pressure and molten copper. It's most likely at switchboards and big supplies, where available fault current is highest."),
                Hazard("Arc flash can cause severe burns, blindness and hearing damage metres away, even if you're not touching anything.")
            ),
            page(
                "Protection",
                bullets(
                    "Best: make it dead. No energy = no arc.",
                    "Use the right tools: insulated tools, CAT-rated meters, correct test leads",
                    "Arc-rated clothing and face protection where required. Ratings are matched to the hazard.",
                    "No synthetic clothing (it melts onto skin)",
                    "Never use a multimeter on amps mode across a supply (see the Testing module)"
                ),
                Vis(Visual.MULTIMETER, "Try putting the meter in amps mode on a live power point and see why it's dangerous."),
                Ask("PPE ratings and when arc-rated gear is required are set by your employer's risk assessment and procedures.")
            )
        ),
        lesson(
            "s4", "First aid for electric shock", "🚑", 5, Level.BEGINNER,
            page(
                "Don't become the second victim",
                order(
                    "Someone is being shocked. Put the response in order.",
                    "DON'T touch them",
                    "Switch off / isolate the power if you safely can",
                    "Call 000 (or your site emergency number)",
                    "Check for danger, then response and airway",
                    "Start CPR if they're not breathing normally, and use an AED"
                ),
                Hazard("If you grab someone who's still connected, the current can flow through you too. For low voltage, if you can't switch off, use something dry and non-conducting to move them, but only if it's safe. For high voltage: stay well back and wait for the supply authority."),
                Tip("Learn CPR. Low-voltage rescue and CPR training is a normal requirement for electrical workers. Keep it current.")
            )
        )
    )
)
