package me.thanhtnt2.fluidcompat.compat.fluidloggable.guard;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * Fluidloggable's block selection: matchesConfiguration(Block) is the one place that decides, and
 * isConfigured(BlockState) asks it exactly once. If that changes, the hook stays off.
 */
public final class ConfiguredSupportShape {

    public static final String SUPPORT_CLASS = "com/moigferdsrte/fluidloggable/block/ConfiguredFluidloggableBlockSupport";
    public static final String MATCHES = "matchesConfiguration";
    public static final String MATCHES_DESC = "(Lnet/minecraft/world/level/block/Block;)Z";
    public static final String IS_CONFIGURED = "isConfigured";
    public static final String IS_CONFIGURED_DESC = "(Lnet/minecraft/world/level/block/state/BlockState;)Z";

    private static final int ACC_STATIC = 0x0008;

    private ConfiguredSupportShape() {
    }

    public static boolean matches(ClassNode supportClass) {
        MethodNode matches = find(supportClass, MATCHES, MATCHES_DESC);
        MethodNode isConfigured = find(supportClass, IS_CONFIGURED, IS_CONFIGURED_DESC);
        if (matches == null || isConfigured == null) {
            return false;
        }
        boolean bothStatic = (matches.access & ACC_STATIC) != 0 && (isConfigured.access & ACC_STATIC) != 0;
        return bothStatic && callsOfMatches(isConfigured) == 1;
    }

    private static MethodNode find(ClassNode node, String name, String descriptor) {
        for (MethodNode method : node.methods) {
            if (name.equals(method.name) && descriptor.equals(method.desc)) {
                return method;
            }
        }
        return null;
    }

    private static int callsOfMatches(MethodNode method) {
        int calls = 0;
        for (AbstractInsnNode instruction : method.instructions) {
            if (instruction instanceof MethodInsnNode call && SUPPORT_CLASS.equals(call.owner)
                    && MATCHES.equals(call.name) && MATCHES_DESC.equals(call.desc)) {
                calls++;
            }
        }
        return calls;
    }
}
