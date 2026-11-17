# Fluid Compat (Fabric 26.3)

An unofficial intermediary compatibility mod that helps **Flowing Fluids** understand other mods better.
This is the Fabric 26.3 line. It is a separate project from the NeoForge 1.21.1 and Forge 1.20.1 lines and has its own
version numbers.

What it contains:

- the Fluid Compat API and core (the Flowing Fluids drain backend, the Vanilla fallback backend, config, own log file);
- machine compat: see the table below.

## Supported mods

| Mod | Tested against | What it does |
|---|---|---|
| Energized Power | 3.0.9 (26.3) | The Fluid Pump, Advanced Fluid Pump and Drain put exactly the water they took into their tank. Taking thin water used to be counted as a full bucket, the pumps still use up their cobblestone but no longer place the block |
| Fluidloggable  |  3.1.3  | pipe blocks can hold fluid (off by default, needs acceptDuplicationRisk) |

## Requirements

- Minecraft 26.3, Fabric Loader 0.20.1 or newer, Fabric API, Java 25
- Flowing Fluids (Fabric 26.3, 1.0.8 or newer): required, never bundled
- YetAnotherConfigLib 3.9.0 or newer (optional): adds the settings screen, open it with `/fluidcompat config`

## Build (IntelliJ IDEA)

1. Put the Flowing Fluids jar and the YetAnotherConfigLib jar into `libs/` (keep their original file names).
2. Open this folder as a Gradle project. Set the Gradle JVM and the Project SDK to Java 25.
3. Run `./gradlew build`. The mod jar is `build/libs/fluidcompat-fabric-26.3-<version>.jar`.
4. For the IDE run configurations use the Gradle task `runClient`.

## Settings

- `config/fluidcompat-common.json`: finite water / lava, drain percent (same keys as the other lines)
- `config/fluidcompat-compat.json`: master switch, logging, one section per supported mod

## License

MIT, see `LICENSE.txt`. Credits are in `CREDITS.md`.
