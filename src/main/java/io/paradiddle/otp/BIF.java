/*
 * paradiddle-otp A library that builds on OpenJDK Project Loom to bring Erlang idioms to Java.
 * Copyright (C) 2021 Michael Juliano
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package io.paradiddle.otp;

public abstract class BIF {
    private static final PostOffice postOffice = new PostOffice.Generic();

    public static PID spawn(final Runnable task) {
        final var thread = Thread.startVirtualThread(task);
        final var mailbox = new Mailbox.Generic();
        postOffice.register(thread.getId(), mailbox);
        return new PID.Generic(mailbox);
    }

    public static void receive(final MessagePattern... patterns) throws InterruptedException {
        postOffice
            .boxFor(Thread.currentThread().getId())
            .retrieve(patterns);
    }
}
