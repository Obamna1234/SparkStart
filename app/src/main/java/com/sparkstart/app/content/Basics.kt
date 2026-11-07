package com.sparkstart.app.content

import com.sparkstart.app.data.*

internal val basicsModule = Module(
    id = "basics",
    title = "Electricity Basics",
    emoji = "⚡",
    blurb = "What electricity actually is: volts, amps, ohms, power, AC and DC.",
    color = 0xFF1E88E5,
    group = Group.CORE,
    lessons = listOf(
        lesson(
            "b1", "What is electricity?", "⚡", 5, Level.BEGINNER,
            page(
                "Tiny things moving",
                P("Everything is made of atoms. Atoms have tiny particles called electrons. In metals like copper, some electrons can drift from atom to atom."),
                P("Electricity is just those electrons being pushed along a wire. When lots of them move in the same direction, we call it a CURRENT."),
                Tip("Think of a wire as a pipe already full of marbles. Push one in one end and one pops out the other end straight away. That's why a light comes on instantly."),
                Deep("Electrons themselves actually move very slowly (millimetres per second). But the 'push' (the electric field) travels through the wire at close to the speed of light, which is why the effect is instant. By tradition we draw 'conventional current' flowing from + to −, even though electrons actually drift from − to +. In this trade you'll almost always use conventional current.")
            ),
            page(
                "Conductors and insulators",
                P("Some materials let electricity through easily. These are CONDUCTORS: copper, aluminium, steel, water with stuff dissolved in it, and YOU."),
                P("Some materials block it. These are INSULATORS: plastic (PVC), rubber, glass, dry wood, ceramic."),
                P("Cables are made of a conductor (copper) wrapped in an insulator (PVC) so the electricity stays where it's meant to be."),
                Hazard("Water, sweat and wet ground make your body a much better conductor. Wet conditions make shocks far more dangerous. That's a big deal underground and in bathrooms."),
                quiz("Which of these is a good INSULATOR?", 2, "PVC plastic blocks current. That's why it's used as cable insulation.", "Copper", "Salty water", "PVC plastic", "Steel")
            ),
            page(
                "The golden rule: it needs a loop",
                P("Electricity only flows if there's a complete path out AND back to where it came from. This loop is called a CIRCUIT."),
                Vis(Visual.CIRCUIT),
                P("Break the loop anywhere (open a switch, a broken wire, a blown fuse) and the current stops everywhere in that loop."),
                Tip("This rule explains almost every fault you'll ever find: something is either a break in the loop (open circuit) or a shortcut in the loop (short circuit)."),
                quiz("A light won't turn on. The globe is fine and the switch is on. What's the most likely thing to check next?", 1, "If there's no complete loop, nothing flows. Look for a break: loose connection, broken wire, tripped breaker.", "Whether the globe is the right colour", "Whether there's a break somewhere in the loop", "Whether the wire is too long", "Nothing, it should work")
            )
        ),
        lesson(
            "b2", "Volts, amps and ohms", "💧", 7, Level.BEGINNER,
            page(
                "The water pipe trick",
                P("The easiest way to picture electricity is water in a pipe:"),
                bullets(
                    "VOLTAGE (V, volts) = the PRESSURE pushing the water",
                    "CURRENT (I, amps) = how much water FLOWS",
                    "RESISTANCE (R, ohms Ω) = how NARROW the pipe is"
                ),
                Vis(Visual.WATER)
            ),
            page(
                "Voltage: the push",
                P("Voltage is the 'push'. It's measured in volts (V). In Australia a normal power point is 230 V. A car battery is 12 V. An AA battery is 1.5 V."),
                P("Voltage is always measured BETWEEN two points. 'The active is at 230 V' really means '230 V between the active and the neutral (or earth)'."),
                Tip("Because voltage is between two points, your meter always needs TWO probes."),
                Deep("Technically voltage is the energy given to each 'packet' of charge (1 volt = 1 joule per coulomb). That's why a higher voltage can push current through a higher resistance, like your skin.")
            ),
            page(
                "Current: the flow",
                P("Current is how much electricity is flowing, measured in amps (A). A phone charger uses less than 1 A. A kettle uses about 10 A. A whole house might use 30–60 A at its busiest."),
                P("Current is what does the work: heats the kettle, spins the motor. It's also what hurts you in a shock."),
                Hazard("It only takes about 0.03 A (30 milliamps) through your chest to be deadly. That's 300 times less than a kettle uses."),
                Vis(Visual.BODY_CURRENT)
            ),
            page(
                "Resistance: the squeeze",
                P("Resistance fights the flow and is measured in ohms (Ω). Thin wires, long wires and hot wires have more resistance."),
                P("Resistance turns electrical energy into HEAT. That's great in a toaster element and bad in a loose connection, where it can start a fire."),
                Hazard("A loose or corroded terminal has extra resistance. Current through it makes heat, which makes it looser and more corroded, and that can end in a burnt-out switchboard. Tight, clean connections matter."),
                quiz("Which wire has the MOST resistance?", 3, "Long and thin both add resistance. That's one reason cable size matters.", "Short and thick", "Short and thin", "Long and thick", "Long and thin")
            )
        ),
        lesson(
            "b3", "Ohm's law", "📐", 8, Level.BEGINNER,
            page(
                "One formula to rule them all",
                P("Volts, amps and ohms are linked by Ohm's law:"),
                P("V = I × R    (volts = amps × ohms)"),
                P("Flip it around and you get: I = V ÷ R and R = V ÷ I"),
                Tip("Cover-up triangle: write V on top, I and R on the bottom. Cover the one you want. V on top means divide, side by side means multiply."),
                Vis(Visual.OHMS)
            ),
            page(
                "Let's try one",
                P("A 230 V heater element has a resistance of 23 Ω. What current does it draw?"),
                P("I = V ÷ R = 230 ÷ 23 = 10 A"),
                quiz("A 12 V supply is connected to a 4 Ω load. What current flows?", 1, "I = V ÷ R = 12 ÷ 4 = 3 A", "48 A", "3 A", "0.33 A", "16 A"),
                quiz("2 A flows through a 50 Ω resistor. What's the voltage across it?", 2, "V = I × R = 2 × 50 = 100 V", "25 V", "52 V", "100 V", "0.04 V")
            ),
            page(
                "Why sparkies care",
                P("Ohm's law is behind loads of real decisions:"),
                bullets(
                    "Why a short circuit is so violent: almost zero resistance → huge current",
                    "Why loop impedance testing matters: low resistance in the fault path → big enough fault current to trip the breaker fast",
                    "Why long cable runs need bigger cables: resistance causes voltage drop"
                ),
                Deep("In AC circuits you'll hear 'impedance' (Z) instead of resistance. It's resistance plus the extra opposition caused by coils (inductance) and capacitors. For basic calculations on heaters and cables, treating impedance like resistance is close enough. Motors and long cables are where it starts to matter."),
                Tip("Practise in the Practice tab → Calculation practice. It makes endless new questions.")
            )
        ),
        lesson(
            "b4", "Power and energy", "🔋", 6, Level.BEGINNER,
            page(
                "Power = how fast energy is used",
                P("Power is measured in watts (W). 1000 W = 1 kilowatt (kW)."),
                P("P = V × I    (watts = volts × amps)"),
                P("A 2400 W kettle on 230 V: I = P ÷ V = 2400 ÷ 230 ≈ 10.4 A"),
                quiz("A 920 W toaster runs on 230 V. Roughly what current?", 0, "I = P ÷ V = 920 ÷ 230 = 4 A", "4 A", "211 A", "0.25 A", "40 A")
            ),
            page(
                "Energy = power × time",
                P("Your power bill charges for ENERGY in kilowatt-hours (kWh). 1 kWh = using 1 kW for 1 hour."),
                P("A 2 kW heater for 3 hours = 6 kWh."),
                Tip("Watts are like speed (km/h). kWh are like distance (km). Your bill is the distance, not the speed."),
                Deep("For AC loads with motors there's also 'power factor'. The current and voltage get out of step, so a motor draws more current than P = V × I suggests. On three-phase: P = √3 × V(line) × I × power factor. You'll meet this in motor control.")
            )
        ),
        lesson(
            "b5", "Series and parallel", "🔀", 7, Level.BEGINNER,
            page(
                "Two ways to connect things",
                P("SERIES = one after another in a single path. PARALLEL = side by side, each with its own path."),
                Vis(Visual.SERIES_PARALLEL)
            ),
            page(
                "The rules",
                bullets(
                    "Series: same current through everything. Voltages add up. Resistances add up (R total = R1 + R2 + …).",
                    "Parallel: same voltage across everything. Currents add up. Total resistance goes DOWN (it's always less than the smallest one)."
                ),
                P("Every light and power point in a building is wired in PARALLEL, so each gets the full 230 V and works on its own."),
                Tip("Switches, fuses and breakers are in SERIES with what they control. They have to be, so they can break the loop."),
                quiz("Three 10 Ω resistors in series. Total?", 2, "Series: add them. 10 + 10 + 10 = 30 Ω", "3.3 Ω", "10 Ω", "30 Ω", "1000 Ω"),
                quiz("Two 20 Ω resistors in parallel. Total?", 0, "(20 × 20) ÷ (20 + 20) = 400 ÷ 40 = 10 Ω. Two equal resistors in parallel = half of one.", "10 Ω", "40 Ω", "20 Ω", "400 Ω")
            )
        ),
        lesson(
            "b6", "AC and DC", "〰️", 6, Level.BEGINNER,
            page(
                "Two kinds of current",
                Vis(Visual.AC_WAVE),
                bullets(
                    "DC (direct current): flows one way only. Batteries, solar panels, electronics, cars.",
                    "AC (alternating current): flips direction back and forth. Mains power in Australia is AC at 50 Hz."
                ),
                P("We use AC for mains because transformers can easily change its voltage up and down, which makes it cheap to send long distances.")
            ),
            page(
                "Numbers to know (Australia)",
                bullets(
                    "Single phase: 230 V (active to neutral)",
                    "Three phase: 400 V between phases, 230 V phase to neutral",
                    "Frequency: 50 Hz"
                ),
                Std("Section 1 (definitions and supply)", "The Wiring Rules define voltage bands. Extra-low voltage (ELV) is not over 50 V AC or 120 V ripple-free DC. Low voltage (LV) is above ELV up to 1000 V AC or 1500 V DC. High voltage (HV) is above that."),
                Hazard("DC can be just as dangerous as AC. Big battery banks and solar arrays can deliver huge currents and hold dangerous voltages even when the 'switch is off'. Arcs on DC are harder to put out."),
                quiz("What's the nominal single-phase voltage in Australia?", 1, "230 V, 50 Hz. (Older gear may say 240 V, which was the old nominal value.)", "110 V", "230 V", "400 V", "1000 V")
            )
        )
    )
)
