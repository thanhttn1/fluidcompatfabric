package me.thanhtnt2.fluidcompat.compat.energizedpower.guard;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

/** Method calls and field reads of one method in instruction order, for the shape checks. Pure ASM, no game classes. */
final class BytecodeCalls {

    record Call(String owner, String name, String descriptor) {
    }

    /** Field reads are recorded in the same list so the order against the calls can be checked. */
    static final String FIELD_PREFIX = "field:";

    private final List<Call> calls = new ArrayList<>();

    private BytecodeCalls() {
    }

    static BytecodeCalls of(ClassNode classNode, String methodName, String methodDescriptor) {
        for (MethodNode method : classNode.methods) {
            if (methodName.equals(method.name) && methodDescriptor.equals(method.desc)) {
                return read(method);
            }
        }
        return null;
    }

    private static BytecodeCalls read(MethodNode method) {
        BytecodeCalls result = new BytecodeCalls();
        for (AbstractInsnNode instruction : method.instructions) {
            if (instruction instanceof MethodInsnNode call) {
                result.calls.add(new Call(call.owner, call.name, call.desc));
            } else if (instruction instanceof FieldInsnNode field) {
                result.calls.add(new Call(field.owner, FIELD_PREFIX + field.name, field.desc));
            }
        }
        return result;
    }

    int count(String owner, String name, String descriptor) {
        int found = 0;
        for (Call call : calls) {
            if (matches(call, owner, name, descriptor)) {
                found++;
            }
        }
        return found;
    }

    int countNamed(String name) {
        int found = 0;
        for (Call call : calls) {
            if (name.equals(call.name())) {
                found++;
            }
        }
        return found;
    }

    /** Position of the n-th (0-based) matching call, or -1. */
    int indexOf(String owner, String name, String descriptor, int occurrence) {
        int seen = 0;
        for (int index = 0; index < calls.size(); index++) {
            if (matches(calls.get(index), owner, name, descriptor) && seen++ == occurrence) {
                return index;
            }
        }
        return -1;
    }

    private static boolean matches(Call call, String owner, String name, String descriptor) {
        return call.owner().equals(owner) && call.name().equals(name) && call.descriptor().equals(descriptor);
    }
}
