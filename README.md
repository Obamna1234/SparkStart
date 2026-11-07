# ⚡ SparkStart

A free, offline Android app that teaches electrical basics to Australian apprentices in plain English, with interactive diagrams. Based on Australian practice and AS/NZS 3000 (the Wiring Rules).

**No account, no ads, no tracking, no internet permission.** Progress is saved on your phone only.

## What's inside

- **Core skills:** electricity basics, staying safe, supply and MEN earthing, protection devices, cables and circuits, testing equipment, motors/transformers/3-phase, and using the Wiring Rules
- **Industries:** Domestic, Commercial and Underground Mining (WA), each with Beginner 🟢, Intermediate 🟡 and Advanced 🔴 lessons
- **Every equipment lesson** covers ⚠️ hazards, 🚫 why you can't do it that way, and 🔧 how to find faults
- **25 interactive diagrams:** circuits, Ohm's law, MEN fault path, RCDs, breakers, multimeter, insulation tester, motors, star-delta, pilot core, NER, generator back-feed, half-split fault finding and more
- **Learning modes:** lessons with quick checks, put-in-order steps, flashcards (spaced repetition), quizzes, endless calculation practice
- **Progress:** XP, streak, resume where you left off, review completed lessons
- **🚩 Flags:** mark anything that looks wrong and copy the list to show your lecturer or supervisor

## ⚠️ Disclaimer

This is a study aid, not the Wiring Rules. It can't check its content against the actual text of AS/NZS 3000 (a paid, copyrighted standard). Anything tagged 📘 or 🧑‍🔧 must be checked in your own copy of the standard or with your site. It doesn't replace your RTO training, supervisor, site procedures or the standards. Never work live.

## Install

1. Open the [Releases](../../releases) page on your phone.
2. Download the latest `SparkStart-x.x.x.apk`.
3. Open it and allow "install unknown apps" for your browser when asked.

Installing a newer version over an older one keeps your progress.

## How the build works

Every push to `main` triggers GitHub Actions (`.github/workflows/build.yml`). It builds a signed release APK and publishes it as a new GitHub Release automatically.

The app is signed with `app/sparkstart.keystore` (committed on purpose) so every build can install over the last one. Because that key is public, only install SparkStart APKs from this repo's Releases page.

## Adding or fixing content

All lesson content lives in `app/src/main/java/com/sparkstart/app/content/`. Each lesson is a list of pages made of blocks: `P` (text), `Tip`, `Hazard`, `WhyNot`, `faults(...)`, `Std` (standard reference), `Ask` (confirm on site), `Deep` (tap for more), `Vis` (interactive diagram), `quiz(...)` and `order(...)`.
