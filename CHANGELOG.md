# Changelog (Fabric 26.3 line)

Versioning: `1.X.0` for a big update (new mod support), `1.X.Y` for a small update or fix.

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
