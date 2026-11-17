# Changelog (Fabric 26.3 line)

Versioning: `1.X.0` for a big update (new mod support), `1.X.Y` for a small update or fix.

## 1.0.2

* **Fluidloggable (3.1.3) pipes, built in and switchable.** Fluid Compat can make the pipe blocks of other mods
  fluidloggable, so fluid can enter them like any other fluidloggable block.
  * Pipes are found by Java class name, the same way as on the other lines: every block that is an instance of a class
    in `fluidloggable_pipes.blockClasses` is covered. The default list has the Energized Power fluid pipe and the pipe
    and cable classes of Modern Industrialization, Modern Dynamics, XNet, Refined Storage and Mekanism. Names of mods
    that are not installed do nothing. Create blocks are always left out, Create has its own fluid settings.
  * **Off by default.** Fluidloggable keeps its own copy of the fluid in every chunk section, next to the block state.
    Fluidlogged did the same on the NeoForge line and duplicated water together with Flowing Fluids. Set
    `fluidloggable_pipes.acceptDuplicationRisk` to true to turn it on (needs a restart, because block states are built
    at startup). `fluidloggable_pipes.enabled` and `general.compatEnabled` switch it off completely.
  * The hook has its own code check: Fluidloggable's block selection must still have the same shape, otherwise the pipes
    stay as they are and the log says why.
* **Submerged pipes for Flowing Fluids** (ported from the other lines, without the orphan water cleanup).
  * Every pipe block from the list above is registered through the Flowing Fluids API as a block that never pushes fluid
    out of its cell (`flowing_fluids_pipes.registerAsNonDisplacers`, default true).
  * The Flowing Fluids waterlog flow mode is set to `IGNORE` in memory when a world starts, so waterlogged pipes neither
    take nor give water. Nothing is written to the Flowing Fluids config file. This is a global Flowing Fluids setting,
    so it also applies to slabs, stairs and other waterlogged blocks. `waterlogFlowMode` can be set to
    `KEEP_FLOWING_FLUIDS_SETTING` to leave it alone; any other mode needs `allowExperimentalWaterlogModes`.
* New settings screen entries for all of the above, and the config files gained two sections
  (`fluidloggable_pipes`, `flowing_fluids_pipes`); existing keys are untouched.

## 1.0.1

* **Mod Menu.** With Mod Menu and YetAnotherConfigLib installed, the Fluid Compat entry in the mod list now has a
  config button. `/fluidcompat config` still works.
* **Energized Power Fluid Pump and Advanced Fluid Pump: no more cobblestone blocks.**
  * The pump still only works with a cobblestone in its slot and still uses one up on every pickup, but it no longer
    places a cobblestone block where it drained. That block was only there to stop vanilla water from refilling the
    cell, and with finite water it just plugged the pond.
  * The pump keeps sweeping its area in its usual order and drains the real water it meets, source blocks and thin
    layers alike, a bucket at a time. The tank still gets exactly what the world gave up.
  * Only finite fluids are changed. A fluid that is not finite (for example lava with `finiteLava` off) keeps the
    original cobblestone block.
  * New switch: `energized_power.placeCobblestone` (default false). Set it to true for the original behaviour.
  * The pump's code check now also covers the cobblestone step: the cobblestone must still be taken from the slot
    before the single block placement, otherwise all three pump hooks stay off with a log line.

## 1.0.0

First release of the Fabric 26.3 line.

* **Fluid Compat API and core.** The same API as the other lines: `FluidCompatApi`, `FiniteFluidService`, drain backends
  (Flowing Fluids at priority 1000, Vanilla fallback at 0) and the compat module registry. The intake events are Fabric
  events here (`FluidIntakeEvents.PRE` and `POST`) because Fabric has no cancellable event bus.
* **Own log file** in `logs/fluidcompat/` (`fluidcompat.log`, `-previous`, 16 MB rollover).
* **Settings** in `config/fluidcompat-common.json` and `config/fluidcompat-compat.json`. Missing keys are added, existing
  keys are never reset. With YetAnotherConfigLib installed, `/fluidcompat config` opens a settings screen (Core,
  Mod Compat List, Logging).
* **Energized Power (3.0.9): Fluid Pump, Advanced Fluid Pump and Drain.**
  * These machines pick the water up with the game's bucket pickup and then tank the amount of the bucket item. When the
    cell holds only a thin layer, Flowing Fluids hands out a partly filled bucket (its durability bar shows how empty
    it is), and the machine still tanked a full bucket, which is water the world never lost.
  * The tank now receives exactly the levels in that bucket (one level is 1/8 bucket, 10125 droplets). Full buckets and
    every other path are untouched. If the tank has less room than that, it still takes less, never more.
  * Each machine has its own code check: the pump's work step and the Drain's tick must still pick up, read the
    bucket's amount and insert into the tank in that order, with no other way of removing the block. If an Energized
    Power update changes that, the hook switches itself off with a log line.
  * Switch: `energized_power.enabled` (default true). `general.compatEnabled` turns every module off.
