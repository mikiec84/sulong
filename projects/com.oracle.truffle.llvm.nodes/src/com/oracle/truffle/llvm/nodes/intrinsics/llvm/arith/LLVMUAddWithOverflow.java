/*
 * Copyright (c) 2016, Oracle and/or its affiliates.
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials provided
 * with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
 * AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.oracle.truffle.llvm.nodes.intrinsics.llvm.arith;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeChildren;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.llvm.nodes.intrinsics.llvm.LLVMBuiltin;
import com.oracle.truffle.llvm.runtime.LLVMAddress;
import com.oracle.truffle.llvm.runtime.memory.LLVMMemory;
import com.oracle.truffle.llvm.runtime.nodes.api.LLVMExpressionNode;

public class LLVMUAddWithOverflow {

    @NodeChildren({@NodeChild(value = "left", type = LLVMExpressionNode.class), @NodeChild(value = "right", type = LLVMExpressionNode.class),
                    @NodeChild(value = "target", type = LLVMExpressionNode.class)})
    public abstract static class LLVMUAddWithOverflowI8 extends LLVMBuiltin {

        private final int secondValueOffset;

        public LLVMUAddWithOverflowI8(int secondValueOffset) {
            this.secondValueOffset = secondValueOffset;
        }

        @Specialization
        public LLVMAddress executeIntrinsic(byte left, byte right, LLVMAddress addr) {
            final int res = (left & LLVMExpressionNode.I8_MASK) + (right & LLVMExpressionNode.I8_MASK);
            final boolean overflow = (res & (1 << Byte.SIZE)) != 0;

            LLVMMemory.putI8(addr, (byte) (res));
            LLVMMemory.putI1(addr.getVal() + secondValueOffset, overflow);
            return addr;
        }

    }

    @NodeChildren({@NodeChild(value = "left", type = LLVMExpressionNode.class), @NodeChild(value = "right", type = LLVMExpressionNode.class),
                    @NodeChild(value = "cin", type = LLVMExpressionNode.class), @NodeChild(value = "cout", type = LLVMExpressionNode.class)})
    public abstract static class LLVMUAddWithOverflowAndCarryI8 extends LLVMBuiltin {

        @Specialization
        public byte executeIntrinsic(byte left, byte right, byte cin, LLVMAddress cout) {
            final int res = (left & LLVMExpressionNode.I8_MASK) + (right & LLVMExpressionNode.I8_MASK) + (cin & LLVMExpressionNode.I8_MASK);
            final boolean overflow = (res & (0xF << Byte.SIZE)) != 0;

            LLVMMemory.putI8(cout, (byte) (overflow ? 1 : 0));
            return (byte) res;
        }

    }

    @NodeChildren({@NodeChild(value = "left", type = LLVMExpressionNode.class), @NodeChild(value = "right", type = LLVMExpressionNode.class),
                    @NodeChild(value = "target", type = LLVMExpressionNode.class)})
    public abstract static class LLVMUAddWithOverflowI16 extends LLVMBuiltin {

        private final int secondValueOffset;

        public LLVMUAddWithOverflowI16(int secondValueOffset) {
            this.secondValueOffset = secondValueOffset;
        }

        @Specialization
        public LLVMAddress executeIntrinsic(short left, short right, LLVMAddress addr) {
            final int res = (left & LLVMExpressionNode.I16_MASK) + (right & LLVMExpressionNode.I16_MASK);
            final boolean overflow = (res & (1 << Short.SIZE)) != 0;

            LLVMMemory.putI16(addr, (short) (res));
            LLVMMemory.putI1(addr.getVal() + secondValueOffset, overflow);
            return addr;
        }
    }

    @NodeChildren({@NodeChild(value = "left", type = LLVMExpressionNode.class), @NodeChild(value = "right", type = LLVMExpressionNode.class),
                    @NodeChild(value = "cin", type = LLVMExpressionNode.class), @NodeChild(value = "cout", type = LLVMExpressionNode.class)})
    public abstract static class LLVMUAddWithOverflowAndCarryI16 extends LLVMBuiltin {

        @Specialization
        public short executeIntrinsic(short left, short right, short cin, LLVMAddress cout) {
            final int res = (left & LLVMExpressionNode.I16_MASK) + (right & LLVMExpressionNode.I16_MASK) + (cin & LLVMExpressionNode.I16_MASK);
            final boolean overflow = (res & (0xF << Short.SIZE)) != 0;

            LLVMMemory.putI16(cout, (short) (overflow ? 1 : 0));
            return (short) res;
        }

    }

    @NodeChildren({@NodeChild(value = "left", type = LLVMExpressionNode.class), @NodeChild(value = "right", type = LLVMExpressionNode.class),
                    @NodeChild(value = "target", type = LLVMExpressionNode.class)})
    public abstract static class LLVMUAddWithOverflowI32 extends LLVMBuiltin {

        private final int secondValueOffset;

        public LLVMUAddWithOverflowI32(int secondValueOffset) {
            this.secondValueOffset = secondValueOffset;
        }

        @Specialization
        public LLVMAddress executeIntrinsic(int left, int right, LLVMAddress addr) {
            final int res = left + right;
            final boolean overflow = ((~res & left) | (~res & right) | (left & right)) < 0;

            LLVMMemory.putI32(addr, res);
            LLVMMemory.putI1(addr.getVal() + secondValueOffset, overflow);
            return addr;
        }
    }

    @NodeChildren({@NodeChild(value = "left", type = LLVMExpressionNode.class), @NodeChild(value = "right", type = LLVMExpressionNode.class),
                    @NodeChild(value = "cin", type = LLVMExpressionNode.class), @NodeChild(value = "cout", type = LLVMExpressionNode.class)})
    public abstract static class LLVMUAddWithOverflowAndCarryI32 extends LLVMBuiltin {

        @Specialization
        public int executeIntrinsic(int left, int right, int cin, LLVMAddress cout) {
            final int res1 = left + right;
            final boolean overflow1 = ((~res1 & left) | (~res1 & right) | (left & right)) < 0;

            final int res2 = res1 + cin;
            final boolean overflow2 = ((~res2 & res1) | (~res2 & cin) | (res1 & cin)) < 0;

            LLVMMemory.putI32(cout, (overflow1 | overflow2) ? 1 : 0);
            return res2;
        }
    }

    @NodeChildren({@NodeChild(value = "left", type = LLVMExpressionNode.class), @NodeChild(value = "right", type = LLVMExpressionNode.class),
                    @NodeChild(value = "target", type = LLVMExpressionNode.class)})
    public abstract static class LLVMUAddWithOverflowI64 extends LLVMBuiltin {

        private final int secondValueOffset;

        public LLVMUAddWithOverflowI64(int secondValueOffset) {
            this.secondValueOffset = secondValueOffset;
        }

        @Specialization
        public LLVMAddress executeIntrinsic(long left, long right, LLVMAddress addr) {
            final long res = left + right;
            final boolean overflow = ((~res & left) | (~res & right) | (left & right)) < 0;

            LLVMMemory.putI64(addr, res);
            LLVMMemory.putI1(addr.getVal() + secondValueOffset, overflow);
            return addr;
        }
    }

    @NodeChildren({@NodeChild(value = "left", type = LLVMExpressionNode.class), @NodeChild(value = "right", type = LLVMExpressionNode.class),
                    @NodeChild(value = "cin", type = LLVMExpressionNode.class), @NodeChild(value = "cout", type = LLVMExpressionNode.class)})
    public abstract static class LLVMUAddWithOverflowAndCarryI64 extends LLVMBuiltin {

        @Specialization
        public long executeIntrinsic(long left, long right, long cin, LLVMAddress cout) {
            final long res1 = left + right;
            final boolean overflow1 = ((~res1 & left) | (~res1 & right) | (left & right)) < 0;

            final long res2 = res1 + cin;
            final boolean overflow2 = ((~res2 & res1) | (~res2 & cin) | (res1 & cin)) < 0;

            LLVMMemory.putI64(cout, (overflow1 | overflow2) ? 1 : 0);
            return res2;
        }
    }
}
